package com.kernelflux.uixkit.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

class SmartAdapter<T : Any>(
    diffCallback: DiffUtil.ItemCallback<T> = object : DiffUtil.ItemCallback<T>() {
        override fun areItemsTheSame(oldItem: T, newItem: T): Boolean = oldItem == newItem

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: T, newItem: T): Boolean = oldItem == newItem
    },
) : SafeListAdapter<T, RecyclerView.ViewHolder>(diffCallback) {
    private val delegates = mutableListOf<ItemDelegate<T, out ViewBinding>>()


    fun registerDelegate(delegate: ItemDelegate<T, out ViewBinding>) {
        delegates.add(delegate)
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position) ?: if (BuildConfig.DEBUG) {
            throw IllegalStateException("getItemViewType called with invalid position: $position or null item.")
        } else {
            return 0
        }

        val viewTypeIndex = delegates.indexOfFirst { it.isForViewType(item, position) }

        if (viewTypeIndex == -1) {
            if (BuildConfig.DEBUG) {
                throw IllegalStateException("No delegate found for item at position $position. Item: $item")
            } else {
                return 0
            }
        }
        return viewTypeIndex
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val delegate = delegates.getOrNull(viewType) ?: run {
            if (BuildConfig.DEBUG) {
                throw IllegalStateException("onCreateViewHolder: No delegate found for viewType: $viewType")
            } else {
                val emptyView = LayoutInflater.from(parent.context)
                    .inflate(android.R.layout.simple_list_item_1, parent, false)
                return SmartViewHolder { emptyView }
            }
        }
        val binding = delegate.onCreateViewBinding(parent)
        return SmartViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: List<Any>,
    ) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            bindViewHolderInternal(holder, position, payloads)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        bindViewHolderInternal(holder, position, emptyList())
    }

    private fun bindViewHolderInternal(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: List<Any>,
    ) {
        val item = getItem(position) ?: if (BuildConfig.DEBUG) {
            throw IllegalStateException("Invalid position: $position in onBindViewHolder")
        } else {
            return
        }

        val delegate = delegates.getOrNull(holder.itemViewType) ?: if (BuildConfig.DEBUG) {
            throw IllegalStateException("No delegate found for viewType: ${holder.itemViewType}")
        } else {
            return
        }

        val smartViewHolder = (holder as? SmartViewHolder) ?: if (BuildConfig.DEBUG) {
            throw IllegalStateException("ViewHolder must be SmartViewHolder")
        } else {
            return
        }

        try {
            @Suppress("UNCHECKED_CAST")
            (delegate as ItemDelegate<T, ViewBinding>).onBindViewHolder(
                smartViewHolder.binding, item, position, payloads
            )
        } catch (e: ClassCastException) {
            if (BuildConfig.DEBUG) {
                throw IllegalStateException(
                    "Delegate or ViewBinding type mismatch at position $position",
                    e
                )
            } else {
                return
            }
        }
    }

    class SmartViewHolder(val binding: ViewBinding) : RecyclerView.ViewHolder(binding.root)
}


inline fun <T : Any, reified D : ItemDelegate<T, *>> SmartAdapter<T>.withDelegate(
    delegateProvider: () -> D,
): SmartAdapter<T> {
    this.registerDelegate(delegateProvider())
    return this
}


fun <T : Any> SmartAdapter<T>.withDelegates(
    vararg delegates: ItemDelegate<T, out ViewBinding>,
): SmartAdapter<T> {
    delegates.forEach { this.registerDelegate(it) }
    return this
}

/**
 * DSL-style extension function to register an ItemDelegate with automatic ViewBinding handling.
 * This provides a more concise API for registering delegates.
 *
 * @param T The type of the data model.
 * @param VB The type of the ViewBinding.
 * @param isForViewType Lambda to determine if this delegate handles the item.
 * @param onBind Lambda to bind data to the view.
 * @return The SmartAdapter instance for method chaining.
 */
inline fun <T : Any, reified VB : ViewBinding> SmartAdapter<T>.withDelegate(
    noinline isForViewType: (item: T, position: Int) -> Boolean,
    noinline onBind: (binding: VB, item: T, position: Int, payloads: List<Any>) -> Unit
): SmartAdapter<T> {
    val delegate = object : AutoBindingDelegate<T, VB>() {
        override fun isForViewType(item: T, position: Int): Boolean = isForViewType(item, position)
        override fun onBindViewHolder(binding: VB, item: T, position: Int, payloads: List<Any>) = onBind(binding, item, position, payloads)
    }
    this.registerDelegate(delegate)
    return this
}

