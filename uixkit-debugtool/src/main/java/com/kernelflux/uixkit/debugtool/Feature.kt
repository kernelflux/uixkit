package com.kernelflux.uixkit.debugtool

import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.kernelflux.uixkit.core.BaseActivity

/**
 * @author: QT
 * @date: 2025/5/11
 */
object Feature {

    fun activity(
        name: String,
        group: String = "General",
        description: String = "",
        activityClass: Class<out BaseActivity>
    ): FeatureEntry = object : FeatureEntry {
        override val name = name
        override val group = group
        override val description = description
        override fun invoke(context: Context) {
            context.startActivity(Intent(context, activityClass))
        }
    }

    fun toast(
        name: String,
        group: String = "Util",
        description: String = "",
        message: String
    ): FeatureEntry = object : FeatureEntry {
        override val name = name
        override val group = group
        override val description = description
        override fun invoke(context: Context) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    fun action(
        name: String,
        group: String = "General",
        description: String = "",
        action: (Context) -> Unit
    ): FeatureEntry = object : FeatureEntry {
        override val name = name
        override val group = group
        override val description = description
        override fun invoke(context: Context) = action(context)
    }
}