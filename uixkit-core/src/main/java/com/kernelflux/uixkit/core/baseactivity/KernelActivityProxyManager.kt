package com.kernelflux.uixkit.core.baseactivity

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.KeyEvent
import android.view.MotionEvent

class KernelActivityProxyManager {
    private val proxies = mutableListOf<KernelActivityProxy>()

    fun register(proxy: KernelActivityProxy) {
        if (!proxies.contains(proxy)) proxies.add(proxy)
    }

    fun unregister(proxy: KernelActivityProxy) {
        proxies.remove(proxy)
    }

    fun doOnActivityResult(kernelActivity: KernelActivity, requestCode: Int, resultCode: Int, data: Intent?) {
        proxies.forEach { it.doOnActivityResult(kernelActivity, requestCode, resultCode, data) }
    }

    fun doOnAttachBaseContext(kernelActivity: KernelActivity, context: Context?) {
        proxies.forEach { it.doOnAttachBaseContext(kernelActivity, context) }
    }

    fun doOnBackPressed(kernelActivity: KernelActivity) {
        proxies.forEach { it.doOnBackPressed(kernelActivity) }
    }

    fun doOnConfigurationChanged(kernelActivity: KernelActivity, configuration: Configuration) {
        proxies.forEach { it.doOnConfigurationChanged(kernelActivity, configuration) }
    }

    fun doOnContentChanged(kernelActivity: KernelActivity) {
        proxies.forEach { it.doOnContentChanged(kernelActivity) }
    }

    fun doOnCreate(kernelActivity: KernelActivity, bundle: Bundle?) {
        proxies.forEach { it.doOnCreate(kernelActivity, bundle) }
    }

    fun doOnDestroy(kernelActivity: KernelActivity) {
        proxies.forEach { it.doOnDestroy(kernelActivity) }
    }

    fun doOnDispatchTouchEvent(kernelActivity: KernelActivity, motionEvent: MotionEvent?): Boolean {
        return proxies.any { it.doOnDispatchTouchEvent(kernelActivity, motionEvent) }
    }

    fun doOnFinish(kernelActivity: KernelActivity) {
        proxies.forEach { it.doOnFinish(kernelActivity) }
    }

    fun doOnKeyDown(kernelActivity: KernelActivity, keyCode: Int, keyEvent: KeyEvent?): Boolean {
        return proxies.any { it.doOnKeyDown(kernelActivity, keyCode, keyEvent) }
    }

    fun doOnKeyMultiple(kernelActivity: KernelActivity, keyCode: Int, repeatCount: Int, event: KeyEvent?): Boolean {
        return proxies.any { it.doOnKeyMultiple(kernelActivity, keyCode, repeatCount, event) }
    }

    fun doOnKeyUp(kernelActivity: KernelActivity, keyCode: Int, keyEvent: KeyEvent?): Boolean {
        return proxies.any { it.doOnKeyUp(kernelActivity, keyCode, keyEvent) }
    }

    fun doOnMultiWindowModeChanged(kernelActivity: KernelActivity, isInMultiWindowMode: Boolean) {
        proxies.forEach { it.doOnMultiWindowModeChanged(kernelActivity, isInMultiWindowMode) }
    }

    fun doOnNewIntent(kernelActivity: KernelActivity, intent: Intent?) {
        proxies.forEach { it.doOnNewIntent(kernelActivity, intent) }
    }

    fun doOnPause(kernelActivity: KernelActivity) {
        proxies.forEach { it.doOnPause(kernelActivity) }
    }

    fun doOnPictureInPictureModeChanged(kernelActivity: KernelActivity, isInPictureInPictureMode: Boolean, configuration: Configuration) {
        proxies.forEach { it.doOnPictureInPictureModeChanged(kernelActivity, isInPictureInPictureMode, configuration) }
    }

    fun doOnRestart(kernelActivity: KernelActivity) {
        proxies.forEach { it.doOnRestart(kernelActivity) }
    }

    fun doOnResume(kernelActivity: KernelActivity) {
        proxies.forEach { it.doOnResume(kernelActivity) }
    }

    fun doOnSaveInstanceState(kernelActivity: KernelActivity, bundle: Bundle) {
        proxies.forEach { it.doOnSaveInstanceState(kernelActivity, bundle) }
    }

    fun doOnStart(kernelActivity: KernelActivity) {
        proxies.forEach { it.doOnStart(kernelActivity) }
    }

    fun doOnStop(kernelActivity: KernelActivity) {
        proxies.forEach { it.doOnStop(kernelActivity) }
    }

    fun doOnTouchEvent(kernelActivity: KernelActivity, motionEvent: MotionEvent?): Boolean {
        return proxies.any { it.doOnTouchEvent(kernelActivity, motionEvent) }
    }

    fun doOnWindowFocusChanged(kernelActivity: KernelActivity, hasFocus: Boolean) {
        proxies.forEach { it.doOnWindowFocusChanged(kernelActivity, hasFocus) }
    }
}