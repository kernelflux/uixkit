package com.kernelflux.uixkit.adapter

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

/**
 * 极简版本的适配器，完全基于 lambda 表达式
 * 无需定义任何类，直接使用 DSL 风格配置
 */
class UltraSimpleAdapter<T : Any>(
    diffCallback: DiffUtil.ItemCallback<T>? = null
) : ListAdapter<T, UltraSimpleAdapter.UltraViewHolder>(
    diffCallback ?: object : DiffUtil.ItemCallback<T>() {
        override fun areItemsTheSame(oldItem: T, newItem: T): Boolean = oldItem == newItem

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: T, newItem: T): Boolean = oldItem == newItem
    }
) {

    @PublishedApi
    internal val itemConfigs = mutableListOf<ItemConfig<T>>()

    /**
     * 配置一个 item 类型（推荐使用）
     */
    inline fun <reified VB : ViewBinding> item(
        noinline isForViewType: (item: T, position: Int) -> Boolean,
        noinline onBind: (binding: VB, item: T, position: Int) -> Unit
    ): UltraSimpleAdapter<T> {
        val config = ItemConfig(
            isForViewType = isForViewType,
            onCreateBinding = { parent ->
                // 使用反射自动创建 ViewBinding
                val vbClass = VB::class.java
                val method = vbClass.getMethod(
                    "inflate",
                    android.view.LayoutInflater::class.java,
                    ViewGroup::class.java,
                    Boolean::class.java
                )
                method.invoke(
                    null,
                    android.view.LayoutInflater.from(parent.context),
                    parent,
                    false
                ) as VB
            },
            onBind = { binding, item, position ->
                @Suppress("UNCHECKED_CAST")
                onBind(binding as VB, item, position)
            }
        )
        itemConfigs.add(config)
        return this
    }

    /**
     * 添加一个自定义的 item 配置
     */
    fun addItem(
        isForViewType: (item: T, position: Int) -> Boolean,
        onCreateBinding: (parent: ViewGroup) -> ViewBinding,
        onBind: (binding: ViewBinding, item: T, position: Int) -> Unit
    ): UltraSimpleAdapter<T> {
        itemConfigs.add(ItemConfig(isForViewType, onCreateBinding, onBind))
        return this
    }

    /**
     * 使用 AutoBindingDelegate 添加 item（兼容旧版本）
     */
    inline fun <reified VB : ViewBinding> addAutoItem(
        noinline isForViewType: (item: T, position: Int) -> Boolean,
        noinline onBind: (binding: VB, item: T, position: Int) -> Unit
    ): UltraSimpleAdapter<T> {
        val delegate = object : AutoBindingDelegate<T, VB>() {
            override fun isForViewType(item: T, position: Int): Boolean =
                isForViewType(item, position)

            override fun onBindViewHolder(
                binding: VB,
                item: T,
                position: Int,
                payloads: List<Any>
            ) {
                onBind(binding, item, position)
            }
        }
        itemConfigs.add(
            ItemConfig(
                isForViewType = { item, position -> delegate.isForViewType(item, position) },
                onCreateBinding = { parent -> delegate.onCreateViewBinding(parent) },
                onBind = { binding, item, position ->
                    delegate.onBindViewHolder(
                        binding as VB,
                        item,
                        position,
                        emptyList()
                    )
                }
            )
        )
        return this
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position) ?: return 0
        return itemConfigs.indexOfFirst { it.isForViewType(item, position) }.takeIf { it >= 0 } ?: 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UltraViewHolder {
        val config = itemConfigs.getOrNull(viewType)
            ?: throw IllegalStateException("No config found for viewType: $viewType")
        val binding = config.onCreateBinding(parent)
        return UltraViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UltraViewHolder, position: Int) {
        val item = getItem(position) ?: return
        val config = itemConfigs.getOrNull(holder.itemViewType) ?: return
        config.onBind(holder.binding, item, position)
    }

    class UltraViewHolder(val binding: ViewBinding) : RecyclerView.ViewHolder(binding.root)

    class ItemConfig<T>(
        val isForViewType: (item: T, position: Int) -> Boolean,
        val onCreateBinding: (parent: ViewGroup) -> ViewBinding,
        val onBind: (binding: ViewBinding, item: T, position: Int) -> Unit
    )
}

/**
 * DSL 风格的扩展函数，直接在 RecyclerView 上配置 UltraSimpleAdapter
 */
inline fun <T : Any> RecyclerView.setupUltraAdapter(
    crossinline block: UltraSimpleAdapter<T>.() -> Unit
): UltraSimpleAdapter<T> {
    val adapter = UltraSimpleAdapter<T>()
    adapter.block()
    this.adapter = adapter
    return adapter
}

/**
 * DSL 风格的扩展函数，直接在 SmartRefreshContainer 上配置 UltraSimpleAdapter
 */
inline fun <T : Any> SmartRefreshContainer.setupUltraAdapter(
    layoutManager: RecyclerView.LayoutManager = androidx.recyclerview.widget.LinearLayoutManager(context),
    crossinline block: UltraSimpleAdapter<T>.() -> Unit
): UltraSimpleAdapter<T> {
    val adapter = UltraSimpleAdapter<T>()
    adapter.block()
    this.setLayoutManager(layoutManager)
    this.setAdapter(adapter)
    return adapter
} 