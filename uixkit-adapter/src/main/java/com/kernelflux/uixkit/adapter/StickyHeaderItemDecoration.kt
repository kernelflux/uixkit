package com.kernelflux.uixkit.adapter

import android.graphics.Canvas
import android.view.View
import androidx.core.graphics.withTranslation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * The [StickyHeaderProvider] interface defines how to identify sticky headers
 * and retrieve their corresponding views.
 */
interface StickyHeaderProvider {
    /**
     * Determines whether the data item at the given position should be treated as a sticky header.
     *
     * @param position The position in the list.
     * @return true if it is a header; false otherwise.
     */
    fun isStickyHeader(position: Int): Boolean

    /**
     * Retrieves the header view for the specified position.
     * Note: This method may be called frequently, so avoid time-consuming operations here.
     *
     * @param recyclerView The RecyclerView instance.
     * @param position The position of the header.
     * @return The header view instance.
     */
    fun getHeaderView(recyclerView: RecyclerView, position: Int): View?
}

/**
 * [StickyHeaderItemDecoration] is an [ItemDecoration] used to implement sticky headers in a RecyclerView.
 * It uses the [StickyHeaderProvider] to obtain header views and pins them to the top during scroll.
 *
 * Note: This implementation assumes the RecyclerView uses a [LinearLayoutManager] or its subclass,
 * and is vertically scrolling.
 */
class StickyHeaderItemDecoration(
    private val provider: StickyHeaderProvider,
) : RecyclerView.ItemDecoration() {
    // Caches the currently pinned header view
    private var headerCache: View? = null

    // Caches the last pinned header position
    private var lastHeaderPosition = -1

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val layoutManager = parent.layoutManager as? LinearLayoutManager ?: return
        // Ensure vertical scrolling
        if (layoutManager.orientation != RecyclerView.VERTICAL) return

        val firstVisiblePosition = layoutManager.findFirstVisibleItemPosition()
        if (firstVisiblePosition == RecyclerView.NO_POSITION) return

        // Find the position of the current header that should be pinned
        var headerPosition = -1
        for (i in firstVisiblePosition downTo 0) {
            if (provider.isStickyHeader(i)) {
                headerPosition = i
                break
            }
        }

        if (headerPosition == -1) {
            // No sticky header found
            headerCache = null
            lastHeaderPosition = -1
            return
        }

        // If header position changed or the cache is invalid, update it
        if (headerPosition != lastHeaderPosition || headerCache == null) {
            headerCache = provider.getHeaderView(parent, headerPosition)
            if (headerCache != null) {
                // Measure and layout the header view
                measureHeaderView(headerCache!!, parent)
                layoutHeaderView(headerCache!!, parent)
                lastHeaderPosition = headerPosition
            }
        }

        val currentHeader = headerCache ?: return

        // Calculate Y offset for the header
        var headerY = 0f
        // Check if the next header is pushing the current one
        val nextHeaderPosition = findNextHeaderPosition(firstVisiblePosition + 1, parent)
        if (nextHeaderPosition != -1) {
            val nextHeaderView = layoutManager.findViewByPosition(nextHeaderPosition)
            if (nextHeaderView != null) {
                val nextHeaderTop = nextHeaderView.top
                if (nextHeaderTop <= currentHeader.height) {
                    headerY = (nextHeaderTop - currentHeader.height).toFloat()
                }
            }
        }

        // Draw the header
        c.withTranslation(0f, headerY) {
            currentHeader.draw(this)
        }
    }

    /**
     * Measures the header view to match the width of the RecyclerView and adapts its height.
     */
    private fun measureHeaderView(header: View, parent: RecyclerView) {
        val widthSpec = View.MeasureSpec.makeMeasureSpec(parent.width, View.MeasureSpec.EXACTLY)
        val heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        header.measure(widthSpec, heightSpec)
    }

    /**
     * Layouts the header view to be positioned at the top of the RecyclerView.
     */
    private fun layoutHeaderView(header: View, parent: RecyclerView) {
        header.layout(
            parent.paddingLeft,
            parent.paddingTop,
            parent.width - parent.paddingRight,
            parent.paddingTop + header.measuredHeight
        )
    }

    /**
     * Finds the position of the next header starting from the given position.
     */
    private fun findNextHeaderPosition(startPosition: Int, parent: RecyclerView): Int {
        val layoutManager = parent.layoutManager as? LinearLayoutManager ?: return -1
        val itemCount = layoutManager.itemCount
        for (i in startPosition until itemCount) {
            if (provider.isStickyHeader(i)) {
                return i
            }
        }
        return -1
    }
}