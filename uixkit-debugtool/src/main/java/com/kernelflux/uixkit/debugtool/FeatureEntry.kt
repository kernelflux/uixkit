package com.kernelflux.uixkit.debugtool

import android.content.Context


interface FeatureEntry {
    val name: String
    val group: String? get() = "General"
    val description: String? get() = ""
    fun invoke(context: Context)
}