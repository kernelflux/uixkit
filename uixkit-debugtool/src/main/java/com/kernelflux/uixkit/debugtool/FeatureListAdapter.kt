package com.kernelflux.uixkit.debugtool

import android.annotation.SuppressLint
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.core.graphics.toColorInt

/**
 * @author: QT
 * @date: 2025/5/11
 */
class FeatureListAdapter(
    private val featureList: List<FeatureEntry>
) : RecyclerView.Adapter<FeatureListAdapter.VH>() {

    class VH(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val context = parent.context
        val textView = TextView(context).apply {
            layoutParams = ViewGroup.MarginLayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin = dp(8)
                leftMargin = dp(16)
                rightMargin = dp(16)
            }

            setPadding(dp(16), dp(12), dp(16), dp(12))
            setTextColor(Color.BLACK)
            textSize = 16f
            gravity=Gravity.CENTER

            background = GradientDrawable().apply {
                cornerRadius = dp(8).toFloat()
                setColor("#F5F5F5".toColorInt())
            }

            // 点击水波纹
            val typedValue = TypedValue()
            context.theme.resolveAttribute(
                android.R.attr.selectableItemBackground,
                typedValue,
                true
            )
            foreground = ContextCompat.getDrawable(context, typedValue.resourceId)

            isClickable = true
            isFocusable = true
        }
        return VH(textView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: VH, position: Int) {
        val feature = featureList[position]
        (holder.view as TextView).text =
            "${feature.name}${if (feature.description.isNullOrEmpty()) "" else "(${feature.description})"}!"
        holder.view.setOnClickListener { feature.invoke(holder.view.context) }
    }

    override fun getItemCount(): Int = featureList.size

    private fun dp(value: Int): Int =
        (value * Resources.getSystem().displayMetrics.density).toInt()
}