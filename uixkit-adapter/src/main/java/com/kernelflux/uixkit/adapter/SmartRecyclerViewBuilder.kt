package com.kernelflux.uixkit.adapter

import android.content.Context
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding


class SmartRecyclerViewBuilder<T : Any>(
    private val context: Context,
    private val recyclerView: RecyclerView,
    private val refreshContainer: SmartRefreshContainer? = null,
) {
    private lateinit var smartAdapter: SmartAdapter<T>
    private var layoutManagerSwitcher: LayoutManagerSwitcher = LayoutManagerSwitcher(recyclerView, context)

    private var defaultDiffCallback: DiffUtil.ItemCallback<T> =
        object : DiffUtil.ItemCallback<T>() {
            override fun areItemsTheSame(oldItem: T, newItem: T): Boolean = oldItem == newItem
            override fun areContentsTheSame(oldItem: T, newItem: T): Boolean = oldItem == newItem
        }
    private val _delegateList = mutableListOf<ItemDelegate<T, out ViewBinding>>()

    init {
        refreshContainer?.setRecyclerView(recyclerView)
    }


    fun registerDelegateInternal(delegate: ItemDelegate<T, out ViewBinding>) {
        _delegateList.add(delegate)
    }

    fun setDiffCallback(callback: DiffUtil.ItemCallback<T>): SmartRecyclerViewBuilder<T> {
        this.defaultDiffCallback = callback
        return this
    }

    inline fun <reified VB : ViewBinding> addDelegate(
        layoutResId: Int,
        noinline isForViewTypePredicate: (item: T, position: Int) -> Boolean,
        noinline onBind: (binding: VB, item: T, position: Int, payloads: List<Any>) -> Unit,
    ): SmartRecyclerViewBuilder<T> {
        val delegate = SimpleItemDelegate<T, VB>(layoutResId, isForViewTypePredicate, onBind)
        registerDelegateInternal(delegate)
        return this
    }

    inline fun <reified VB : ViewBinding> addDelegate(
        layoutResId: Int,
        noinline isForViewTypePredicate: (item: T, position: Int) -> Boolean,
        noinline onBindNoPayload: (binding: VB, item: T, position: Int) -> Unit,
    ): SmartRecyclerViewBuilder<T> {
        val delegate = SimpleItemDelegate<T, VB>(
            layoutResId,
            isForViewTypePredicate
        ) { binding, item, position, _ ->
            onBindNoPayload.invoke(binding, item, position)
        }
        registerDelegateInternal(delegate)
        return this
    }


    fun addDelegate(delegate: ItemDelegate<T, out ViewBinding>): SmartRecyclerViewBuilder<T> {
        registerDelegateInternal(delegate)
        return this
    }

    fun enableRefresh(
        header: RefreshHeader? = null,
        onRefreshAction: () -> Unit,
    ): SmartRecyclerViewBuilder<T> {
        refreshContainer?.let { container ->
            if (header != null) {
                container.addRefreshHeader(header)
            } else {
                container.addRefreshHeader(DefaultRefreshHeader(context))
            }
            container.onRefresh(onRefreshAction)
        } ?: throw IllegalStateException("SmartRefreshContainer not provided to enableRefresh.")
        return this
    }

    fun enableLoadMore(
        footer: LoadMoreFooter? = null,
        onLoadMoreAction: () -> Unit,
    ): SmartRecyclerViewBuilder<T> {
        refreshContainer?.let { container ->
            if (footer != null) {
                container.addLoadMoreFooter(footer)
            } else {
                container.addLoadMoreFooter(DefaultLoadMoreFooter(context))
            }
            container.onLoadMore(onLoadMoreAction)
        } ?: throw IllegalStateException("SmartRefreshContainer not provided to enableLoadMore.")
        return this
    }

    fun getLayoutManagerSwitcher(): LayoutManagerSwitcher {
        return layoutManagerSwitcher
    }


    fun build(): SmartAdapter<T> {
        smartAdapter = SmartAdapter(defaultDiffCallback)
        _delegateList.forEach { smartAdapter.registerDelegate(it) }
        recyclerView.adapter = smartAdapter
        return smartAdapter
    }
}