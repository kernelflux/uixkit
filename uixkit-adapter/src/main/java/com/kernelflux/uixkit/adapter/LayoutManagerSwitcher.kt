package com.kernelflux.uixkit.adapter

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

class LayoutManagerSwitcher(
    private val recyclerView: RecyclerView,
    private val context: Context,
) {

    fun switchToLinearVertical() {
        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
    }

    fun switchToLinearHorizontal() {
        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
    }

    fun switchToGrid(
        spanCount: Int,
        @RecyclerView.Orientation orientation: Int = RecyclerView.VERTICAL,
        reverseLayout: Boolean = false,
    ) {
        recyclerView.layoutManager =
            GridLayoutManager(context, spanCount, orientation, reverseLayout)
    }

    fun switchToStaggeredGrid(
        spanCount: Int,
        @RecyclerView.Orientation orientation: Int = RecyclerView.VERTICAL,
    ) {
        recyclerView.layoutManager = StaggeredGridLayoutManager(spanCount, orientation)
    }

    fun getCurrentLayoutManager(): RecyclerView.LayoutManager? {
        return recyclerView.layoutManager
    }
}