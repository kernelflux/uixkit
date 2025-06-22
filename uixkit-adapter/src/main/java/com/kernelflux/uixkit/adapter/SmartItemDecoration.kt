package com.kernelflux.uixkit.adapter

import android.graphics.Canvas
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * [SmartItemDecoration] is an extensible base class for [RecyclerView.ItemDecoration].
 * It provides the canvas, item views, and RecyclerView state, allowing subclasses to implement
 * custom drawing behaviors and spacing between items.
 *
 * Subclasses should override the following methods to achieve custom effects:
 * - [onDraw]: Called before an item is drawn (e.g., to draw background decorations).
 * - [onDrawOver]: Called after an item is drawn (e.g., to draw foreground overlays).
 * - [getItemOffsets]: Used to set margins for each item.
 */
abstract class SmartItemDecoration : RecyclerView.ItemDecoration() {

    /**
     * Called before the item view is drawn. Commonly used to draw item backgrounds, dividers, etc.
     *
     * @param c The canvas on which to draw.
     * @param view The item view currently being processed.
     * @param parent The RecyclerView containing the item.
     * @param state The current state of RecyclerView.
     */
    open fun onDraw(
        c: Canvas,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State,
    ) {
        // Default does not draw anything
    }

    /**
     * Called after the item view is drawn. Commonly used to draw foregrounds, overlays,
     * or any elements that should appear above the item.
     *
     * @param c The canvas on which to draw.
     * @param view The item view currently being processed.
     * @param parent The RecyclerView containing the item.
     * @param state The current state of RecyclerView.
     */
    open fun onDrawOver(
        c: Canvas,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State,
    ) {
        // Default does not draw anything
    }

    /**
     * Sets the margins (offsets) for each item. This affects item layout spacing.
     *
     * @param outRect The rectangle of offsets to be applied (left, top, right, bottom).
     * @param view The item view currently being processed.
     * @param parent The RecyclerView containing the item.
     * @param state The current state of RecyclerView.
     */
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State,
    ) {
        super.getItemOffsets(outRect, view, parent, state)
    }

    // Override RecyclerView.ItemDecoration’s onDraw methods and delegate to custom subclass methods
    final override fun onDraw(
        c: Canvas,
        parent: RecyclerView,
        state: RecyclerView.State,
    ) {
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            onDraw(c, child, parent, state)
        }
    }

    final override fun onDrawOver(
        c: Canvas,
        parent: RecyclerView,
        state: RecyclerView.State,
    ) {
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            onDrawOver(c, child, parent, state)
        }
    }
}