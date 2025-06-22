package com.kernelflux.uixkit.adapter

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.graphics.toColorInt
import kotlin.math.abs
import kotlin.math.min

class DefaultLoadMoreFooter(context: Context) : LoadMoreFooter {
    private val textView = TextView(context).apply {
        layoutParams =
            ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 60.dpToPx(context))
        gravity = Gravity.CENTER
        text = "上拉加载"
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
        textView.text = if (abs(offset) >= triggerOffset) "松开加载" else "上拉加载"
        textView.alpha = min(1f, percent * 2)
    }

    override fun onReleaseToLoad() {
        textView.text = "松开加载"
    }

    override fun onLoading() {
        textView.text = "加载中..."
    }

    override fun onFinish() {
        textView.text = "加载完成"
    }

    override fun onReset() {
        textView.text = "上拉加载"
    }
}