package com.kernelflux.uixkit.core.baseactivity

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.KeyEvent
import android.view.MotionEvent
interface KernelActivityProxy {
    fun doOnActivityResult(kernelActivity: KernelActivity, requestCode: Int, resultCode: Int, data: Intent?) {}
    fun doOnAttachBaseContext(kernelActivity: KernelActivity, context: Context?) {}
    fun doOnBackPressed(kernelActivity: KernelActivity) {}
    fun doOnConfigurationChanged(kernelActivity: KernelActivity, configuration: Configuration) {}
    fun doOnContentChanged(kernelActivity: KernelActivity) {}
    fun doOnCreate(kernelActivity: KernelActivity, bundle: Bundle?) {}
    fun doOnDestroy(kernelActivity: KernelActivity) {}
    fun doOnDispatchTouchEvent(kernelActivity: KernelActivity, motionEvent: MotionEvent?): Boolean = false
    fun doOnFinish(kernelActivity: KernelActivity) {}
    fun doOnKeyDown(kernelActivity: KernelActivity, keyCode: Int, keyEvent: KeyEvent?): Boolean = false
    fun doOnKeyMultiple(kernelActivity: KernelActivity, keyCode: Int, repeatCount: Int, event: KeyEvent?): Boolean = false
    fun doOnKeyUp(kernelActivity: KernelActivity, keyCode: Int, keyEvent: KeyEvent?): Boolean = false
    fun doOnMultiWindowModeChanged(kernelActivity: KernelActivity, isInMultiWindowMode: Boolean) {}
    fun doOnNewIntent(kernelActivity: KernelActivity, intent: Intent?) {}
    fun doOnPause(kernelActivity: KernelActivity) {}
    fun doOnPictureInPictureModeChanged(kernelActivity: KernelActivity, isInPictureInPictureMode: Boolean, configuration: Configuration) {}
    fun doOnRestart(kernelActivity: KernelActivity) {}
    fun doOnResume(kernelActivity: KernelActivity) {}
    fun doOnSaveInstanceState(kernelActivity: KernelActivity, bundle: Bundle) {}
    fun doOnStart(kernelActivity: KernelActivity) {}
    fun doOnStop(kernelActivity: KernelActivity) {}
    fun doOnTouchEvent(kernelActivity: KernelActivity, motionEvent: MotionEvent?): Boolean = false
    fun doOnWindowFocusChanged(kernelActivity: KernelActivity, hasFocus: Boolean) {}
}