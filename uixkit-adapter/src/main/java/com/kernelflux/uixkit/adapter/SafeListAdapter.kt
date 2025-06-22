package com.kernelflux.uixkit.adapter

import androidx.recyclerview.widget.AdapterListUpdateCallback
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView


/**
 * Overrides the official ListAdapter with partial API modifications,
 * providing safer implementations such as getItem, etc.
 */
abstract class SafeListAdapter<T, VH : RecyclerView.ViewHolder>(
    diffCallback: DiffUtil.ItemCallback<T>,
) : RecyclerView.Adapter<VH>() {
    private val mDiffer: AsyncListDiffer<T> by lazy {
        AsyncListDiffer(
            AdapterListUpdateCallback(this),
            AsyncDifferConfig.Builder(diffCallback).build()
        )
    }

    private val mListener: AsyncListDiffer.ListListener<T> =
        AsyncListDiffer.ListListener<T> { previousList, currentList ->
            this@SafeListAdapter.onCurrentListChanged(
                previousList,
                currentList
            )
        }

    init {
        mDiffer.addListListener(mListener)
    }

    fun setData(list: List<T>?) {
        mDiffer.submitList(list)
    }

    fun setData(list: List<T>?, callback: Runnable?) {
        mDiffer.submitList(list, callback)
    }

    fun getDataList(): List<T> {
        return mDiffer.currentList
    }

    open fun getItem(position: Int): T? {
        if (position < 0 || position >= mDiffer.currentList.size) {
            return null
        }
        return mDiffer.currentList[position]
    }

    override fun getItemCount(): Int {
        return mDiffer.currentList.size
    }

    open fun onCurrentListChanged(preList: List<T>, curList: List<T>) {
    }
}
