package com.kernelflux.uixkit.adapter

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.graphics.toColorInt
import kotlin.math.min

class DefaultRefreshHeader(context: Context) : RefreshHeader {
    private val textView = TextView(context).apply {
        layoutParams =
            ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 60.dpToPx(context))
        gravity = Gravity.CENTER
        text = "下拉刷新"
        setBackgroundColor("#E0E0E0".toColorInt())
        setTextColor(Color.DKGRAY)
    }

    override fun getView(): View = textView
    override fun onPulling(
        percent: Float,
        offset: Int,
        triggerOffset: Int,
        @RefreshState.State state: Int,
    ) {
        textView.text = if (offset >= triggerOffset) "松开刷新" else "下拉刷新"
        textView.alpha = min(1f, percent * 2)
    }

    override fun onReleaseToRefresh() {
        textView.text = "松开刷新"
    }

    override fun onRefreshing() {
        textView.text = "刷新中..."
    }

    override fun onFinish() {
        textView.text = "刷新完成"
    }

    override fun onReset() {
        textView.text = "下拉刷新"
    }
}
