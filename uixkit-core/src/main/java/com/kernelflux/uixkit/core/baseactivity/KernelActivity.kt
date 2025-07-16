package com.kernelflux.uixkit.core.baseactivity

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.KeyEvent
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity

open class KernelActivity : AppCompatActivity() {
    private val proxyManager = KernelActivityProxyManager()

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase)
        proxyManager.doOnAttachBaseContext(this, newBase)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        proxyManager.doOnCreate(this, savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        proxyManager.doOnStart(this)
    }

    override fun onResume() {
        super.onResume()
        proxyManager.doOnResume(this)
    }

    override fun onPause() {
        proxyManager.doOnPause(this)
        super.onPause()
    }

    override fun onStop() {
        proxyManager.doOnStop(this)
        super.onStop()
    }

    override fun onDestroy() {
        proxyManager.doOnDestroy(this)
        super.onDestroy()
    }

    override fun onRestart() {
        super.onRestart()
        proxyManager.doOnRestart(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        proxyManager.doOnActivityResult(this, requestCode, resultCode, data)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        proxyManager.doOnConfigurationChanged(this, newConfig)
    }

    override fun onContentChanged() {
        super.onContentChanged()
        proxyManager.doOnContentChanged(this)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        proxyManager.doOnSaveInstanceState(this, outState)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        proxyManager.doOnNewIntent(this, intent)
    }

    override fun onBackPressed() {
        proxyManager.doOnBackPressed(this)
        super.onBackPressed()
    }

    override fun onMultiWindowModeChanged(isInMultiWindowMode: Boolean) {
        super.onMultiWindowModeChanged(isInMultiWindowMode)
        proxyManager.doOnMultiWindowModeChanged(this, isInMultiWindowMode)
    }

    override fun onPictureInPictureModeChanged(isInPictureInPictureMode: Boolean, newConfig: Configuration) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig)
        proxyManager.doOnPictureInPictureModeChanged(this, isInPictureInPictureMode, newConfig)
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (proxyManager.doOnDispatchTouchEvent(this, ev)) return true
        return super.dispatchTouchEvent(ev)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (proxyManager.doOnTouchEvent(this, event)) return true
        return super.onTouchEvent(event)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (proxyManager.doOnKeyDown(this, keyCode, event)) return true
        return super.onKeyDown(keyCode, event)
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        if (proxyManager.doOnKeyUp(this, keyCode, event)) return true
        return super.onKeyUp(keyCode, event)
    }

    override fun onKeyMultiple(keyCode: Int, repeatCount: Int, event: KeyEvent?): Boolean {
        if (proxyManager.doOnKeyMultiple(this, keyCode, repeatCount, event)) return true
        return super.onKeyMultiple(keyCode, repeatCount, event)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        proxyManager.doOnWindowFocusChanged(this, hasFocus)
    }

    fun registerProxy(proxy: KernelActivityProxy) {
        proxyManager.register(proxy)
    }

    fun unregisterProxy(proxy: KernelActivityProxy) {
        proxyManager.unregister(proxy)
    }
}