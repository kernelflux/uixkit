package com.kernelflux.uixkit.adapter

import android.view.ViewGroup
import androidx.viewbinding.ViewBinding


/**
 * [ItemDelegate] is a core interface for handling a specific data type [T] and view binding type [VB].
 * Each distinct list item type should implement its own corresponding delegate.
 *
 * @param T The type of the data model.
 * @param VB The type of the ViewBinding.
 */
interface ItemDelegate<T : Any, VB : ViewBinding> {
    /**
     * Determines whether the current data item should be handled by this delegate.
     *
     * @param item The current data item.
     * @param position The position of the current item in the list.
     * @return Returns true if this delegate is responsible for the item, false otherwise.
     */
    fun isForViewType(item: T, position: Int): Boolean

    /**
     * Creates and returns an instance of the corresponding ViewBinding.
     *
     * @param parent The parent view group.
     * @return An instance of ViewBinding.
     */
    fun onCreateViewBinding(parent: ViewGroup): VB

    /**
     * Binds data to the view.
     *
     * @param binding The ViewBinding instance.
     * @param item The data item to bind.
     * @param position The position of the item in the list.
     * @param payloads A list of change payloads calculated by DiffUtil,
     *                 used for fine-grained partial updates.
     *                 If the list is empty, it indicates either a full refresh
     *                 or no specific payload was provided.
     */
    fun onBindViewHolder(binding: VB, item: T, position: Int, payloads: List<Any>)
}

/**
 * DSL-style function to create an ItemDelegate with automatic ViewBinding handling.
 * This eliminates the need to manually implement onCreateViewBinding.
 *
 * @param T The type of the data model.
 * @param VB The type of the ViewBinding.
 * @param isForViewType Lambda to determine if this delegate handles the item.
 * @param onBind Lambda to bind data to the view.
 * @return An ItemDelegate instance that automatically handles ViewBinding creation.
 */
inline fun <T : Any, reified VB : ViewBinding> createItemDelegate(
    noinline isForViewType: (item: T, position: Int) -> Boolean,
    noinline onBind: (binding: VB, item: T, position: Int, payloads: List<Any>) -> Unit
): ItemDelegate<T, VB> {
    return object : AutoBindingDelegate<T, VB>() {
        override fun isForViewType(item: T, position: Int): Boolean = isForViewType(item, position)
        override fun onBindViewHolder(binding: VB, item: T, position: Int, payloads: List<Any>) = onBind(binding, item, position, payloads)
    }
}

