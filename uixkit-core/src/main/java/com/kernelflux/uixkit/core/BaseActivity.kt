package com.kernelflux.uixkit.core

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import com.kernelflux.uixkit.core.baseactivity.KernelActivity

open class BaseActivity : KernelActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (enableImmersiveStatusBar()) {
            handleImmersiveStatusBar()
        }
    }

    open fun enableImmersiveStatusBar(): Boolean {
        return true
    }

    fun handleImmersiveStatusBar() {
        immersiveStatusBar()
        fitStatusBar(true)
        fitNavigationBar(true)
        setLightStatusBar(true)
    }

    override fun setRequestedOrientation(requestedOrientation: Int) {
        try {
            super.setRequestedOrientation(requestedOrientation)
        } catch (t: Throwable) {
            //
        }
    }

    @SuppressLint("MissingInflatedId")
    open fun <T : View> castViewByResId(id: Int): T {
        return findViewById(id)
    }

}