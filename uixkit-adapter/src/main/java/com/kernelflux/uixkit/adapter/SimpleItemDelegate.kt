package com.kernelflux.uixkit.adapter

import androidx.viewbinding.ViewBinding

class SimpleItemDelegate<T : Any, VB : ViewBinding>(
    private val layoutResId: Int,
    private val isForViewTypePredicate: (item: T, position: Int) -> Boolean,
    private val onBind: (binding: VB, item: T, position: Int, payloads: List<Any>) -> Unit,
) : AutoBindingDelegate<T, VB>() {

    override fun isForViewType(item: T, position: Int): Boolean {
        return isForViewTypePredicate(item, position)
    }

    override fun onBindViewHolder(binding: VB, item: T, position: Int, payloads: List<Any>) {
        onBind.invoke(binding, item, position, payloads)
    }

    fun onBindViewHolder(
        binding: VB,
        item: T,
        position: Int,
        onBindNoPayload: (binding: VB, item: T, position: Int) -> Unit,
    ) {
        onBindNoPayload.invoke(binding, item, position)
    }
}