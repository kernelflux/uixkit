package com.kernelflux.uixkit.debugtool

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kernelflux.uixkit.core.BaseActivity

/**
 * @author: QT
 * @date: 2025/5/11
 */
abstract class FeatureListActivity : BaseActivity() {

    open fun isAddItemDecoration(): Boolean = false

    open fun getFeatureItemDecorationBg(): Drawable? = null

    abstract fun getFeatures(): List<FeatureEntry>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val recyclerView = RecyclerView(this).apply {
            layoutManager = LinearLayoutManager(this@FeatureListActivity)
            adapter = FeatureListAdapter(getFeatures())
            setBackgroundColor(Color.WHITE)

            // 可选：添加分隔线
            if (isAddItemDecoration()) {
                addItemDecoration(
                    DividerItemDecoration(
                        context,
                        DividerItemDecoration.VERTICAL
                    ).apply {
                        getFeatureItemDecorationBg()?.let { setDrawable(it) }
                    })
            }

        }

        setContentView(recyclerView)



    }


}