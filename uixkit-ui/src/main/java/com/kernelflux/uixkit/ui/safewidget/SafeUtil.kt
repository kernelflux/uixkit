package com.kernelflux.uixkit.ui.safewidget

import android.content.ContextWrapper
import android.view.View
import androidx.fragment.app.FragmentActivity


object SafeUtil {

    @JvmStatic
    fun getFragmentActivity(view: View?): FragmentActivity? {
        var tempView = view
        while (true) {
            var context = tempView?.context
            if (context !is FragmentActivity && context is ContextWrapper) {
                context = context.baseContext
            }
            if (context is FragmentActivity) {
                return context
            }
            val parent = tempView?.parent
            if (parent !is View) {
                return null
            }
            tempView = parent
        }
    }
}