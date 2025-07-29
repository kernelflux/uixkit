package com.kernelflux.uixkit.debugtool

import android.content.Context

/**
 * @author: QT
 * @date: 2025/5/11
 */
interface FeatureEntry {
    val name: String
    val group: String? get() = "General"
    val description: String? get() = ""
    fun invoke(context: Context)
}