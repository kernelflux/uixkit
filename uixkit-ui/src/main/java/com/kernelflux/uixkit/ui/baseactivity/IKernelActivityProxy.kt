package com.kernelflux.uixkit.ui.baseactivity

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.KeyEvent
import android.view.MotionEvent

/**
 * * Activity代理类
 **/
open class IKernelActivityProxy {
    open fun doOnActivityResult(
        kernelActivity: KernelActivity,
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
    }

    open fun doOnAttachBaseContext(
        kernelActivity: KernelActivity,
        context: Context?
    ) {
    }

    open fun doOnBackPressed(kernelActivity: KernelActivity) {}
    open fun doOnConfigurationChanged(
        kernelActivity: KernelActivity,
        configuration: Configuration
    ) {
    }

    open fun doOnContentChanged(kernelActivity: KernelActivity) {}
    open fun doOnCreate(kernelActivity: KernelActivity, bundle: Bundle?) {}
    open fun doOnDestroy(kernelActivity: KernelActivity) {}
    open fun doOnDispatchTouchEvent(
        kernelActivity: KernelActivity,
        motionEvent: MotionEvent?
    ): Boolean {
        return false
    }

    open fun doOnFinish(kernelActivity: KernelActivity) {}
    open fun doOnKeyDown(
        kernelActivity: KernelActivity,
        keyCode: Int,
        keyEvent: KeyEvent?
    ): Boolean {
        return false
    }

    open fun doOnKeyMultiple(
        kernelActivity: KernelActivity,
        keyCode: Int,
        repeatCount: Int,
        event: KeyEvent?
    ): Boolean {
        return false
    }

    open fun doOnKeyUp(
        kernelActivity: KernelActivity,
        keyCode: Int,
        keyEvent: KeyEvent?
    ): Boolean {
        return false
    }

    open fun doOnMultiWindowModeChanged(
        kernelActivity: KernelActivity,
        isInMultiWindowMode: Boolean
    ) {
    }

    open fun doOnNewIntent(kernelActivity: KernelActivity, intent: Intent?) {}
    open fun doOnPause(kernelActivity: KernelActivity) {}
    open fun doOnPictureInPictureModeChanged(
        kernelActivity: KernelActivity,
        isInPictureInPictureMode: Boolean,
        configuration: Configuration
    ) {
    }

    open fun doOnRestart(kernelActivity: KernelActivity) {}
    open fun doOnResume(kernelActivity: KernelActivity) {}
    open fun doOnSaveInstanceState(kernelActivity: KernelActivity, bundle: Bundle) {}
    open fun doOnStart(kernelActivity: KernelActivity) {}
    open fun doOnStop(kernelActivity: KernelActivity) {}
    open fun doOnTouchEvent(kernelActivity: KernelActivity, motionEvent: MotionEvent?): Boolean {
        return false
    }

    open fun doOnWindowFocusChanged(kernelActivity: KernelActivity, hasFocus: Boolean) {}
}