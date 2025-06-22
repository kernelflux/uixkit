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
open class IBaseActivityProxy {
    open fun doOnActivityResult(
        baseActivity: BaseActivity,
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
    }

    open fun doOnAttachBaseContext(
        baseActivity: BaseActivity,
        context: Context?
    ) {
    }

    open fun doOnBackPressed(baseActivity: BaseActivity) {}
    open fun doOnConfigurationChanged(
        baseActivity: BaseActivity,
        configuration: Configuration
    ) {
    }

    open fun doOnContentChanged(baseActivity: BaseActivity) {}
    open fun doOnCreate(baseActivity: BaseActivity, bundle: Bundle?) {}
    open fun doOnDestroy(baseActivity: BaseActivity) {}
    open fun doOnDispatchTouchEvent(
        baseActivity: BaseActivity,
        motionEvent: MotionEvent?
    ): Boolean {
        return false
    }

    open fun doOnFinish(baseActivity: BaseActivity) {}
    open fun doOnKeyDown(
        baseActivity: BaseActivity,
        keyCode: Int,
        keyEvent: KeyEvent?
    ): Boolean {
        return false
    }

    open fun doOnKeyMultiple(
        baseActivity: BaseActivity,
        keyCode: Int,
        repeatCount: Int,
        event: KeyEvent?
    ): Boolean {
        return false
    }

    open fun doOnKeyUp(
        baseActivity: BaseActivity,
        keyCode: Int,
        keyEvent: KeyEvent?
    ): Boolean {
        return false
    }

    open fun doOnMultiWindowModeChanged(
        baseActivity: BaseActivity,
        isInMultiWindowMode: Boolean
    ) {
    }

    open fun doOnNewIntent(baseActivity: BaseActivity, intent: Intent?) {}
    open fun doOnPause(baseActivity: BaseActivity) {}
    open fun doOnPictureInPictureModeChanged(
        baseActivity: BaseActivity,
        isInPictureInPictureMode: Boolean,
        configuration: Configuration
    ) {
    }

    open fun doOnRestart(baseActivity: BaseActivity) {}
    open fun doOnResume(baseActivity: BaseActivity) {}
    open fun doOnSaveInstanceState(baseActivity: BaseActivity, bundle: Bundle) {}
    open fun doOnStart(baseActivity: BaseActivity) {}
    open fun doOnStop(baseActivity: BaseActivity) {}
    open fun doOnTouchEvent(baseActivity: BaseActivity, motionEvent: MotionEvent?): Boolean {
        return false
    }

    open fun doOnWindowFocusChanged(baseActivity: BaseActivity, hasFocus: Boolean) {}
}