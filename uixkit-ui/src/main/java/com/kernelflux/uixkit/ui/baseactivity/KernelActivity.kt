package com.kernelflux.uixkit.ui.baseactivity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.view.MotionEvent
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.kernelflux.uixkit.ui.baseactivity.impl.ActivityUtils
import com.kernelflux.uixkit.ui.baseactivity.impl.Logger
import java.lang.Exception

/**
 * * Activity基础类
 **/
open class KernelActivity : AppCompatActivity(), IBaseActivityStack {
    private val mActivityProxyManager: KernelActivityProxyManager = KernelActivityProxyManager()
    private var mBaseInnerProxy: IKernelActivityProxy? = null
    private var mIsDestroyed = false
    private var mIgnoreInnerOnCreate = false

    inner class KernelInnerProxy : IKernelActivityProxy() {
        override fun doOnActivityResult(
            kernelActivity: KernelActivity,
            requestCode: Int,
            resultCode: Int,
            data: Intent?
        ) {
            super@KernelActivity.onActivityResult(requestCode, resultCode, data)
        }

        override fun doOnAttachBaseContext(kernelActivity: KernelActivity, context: Context?) {
            super@KernelActivity.attachBaseContext(context)
        }

        override fun doOnBackPressed(kernelActivity: KernelActivity) {
            super@KernelActivity.onBackPressed()
        }

        override fun doOnConfigurationChanged(
            kernelActivity: KernelActivity,
            configuration: Configuration
        ) {
            super@KernelActivity.onConfigurationChanged(configuration)
        }

        override fun doOnContentChanged(kernelActivity: KernelActivity) {
            super@KernelActivity.onContentChanged()
        }

        override fun doOnCreate(kernelActivity: KernelActivity, bundle: Bundle?) {
            super@KernelActivity.onCreate(bundle)
        }

        override fun doOnDestroy(kernelActivity: KernelActivity) {
            super@KernelActivity.onDestroy()
        }

        override fun doOnFinish(kernelActivity: KernelActivity) {
            super@KernelActivity.finish()
        }

        override fun doOnNewIntent(kernelActivity: KernelActivity, intent: Intent?) {
            super@KernelActivity.onNewIntent(intent)
        }

        override fun doOnPause(kernelActivity: KernelActivity) {
            super@KernelActivity.onPause()
        }

        @RequiresApi(Build.VERSION_CODES.O)
        override fun doOnPictureInPictureModeChanged(
            kernelActivity: KernelActivity,
            isInPictureInPictureMode: Boolean,
            configuration: Configuration
        ) {
            super@KernelActivity.onPictureInPictureModeChanged(
                isInPictureInPictureMode,
                configuration
            )
        }

        override fun doOnRestart(kernelActivity: KernelActivity) {
            super@KernelActivity.onRestart()
        }

        override fun doOnResume(kernelActivity: KernelActivity) {
            super@KernelActivity.onResume()
        }

        override fun doOnSaveInstanceState(kernelActivity: KernelActivity, bundle: Bundle) {
            super@KernelActivity.onSaveInstanceState(bundle)
        }

        override fun doOnStart(kernelActivity: KernelActivity) {
            super@KernelActivity.onStart()
        }

        override fun doOnStop(kernelActivity: KernelActivity) {
            super@KernelActivity.onStop()
        }

        override fun doOnWindowFocusChanged(kernelActivity: KernelActivity, hasFocus: Boolean) {
            super@KernelActivity.onWindowFocusChanged(hasFocus)
        }
    }


    override fun attachBaseContext(context: Context?) {
        super.attachBaseContext(context)
        this.mActivityProxyManager.doOnAttachBaseContext(this, context)
    }

    override fun dispatchTouchEvent(motionEvent: MotionEvent?): Boolean {
        var intercepted = true
        if (!this.mActivityProxyManager.doOnDispatchTouchEvent(this, motionEvent)) {
            intercepted = super.dispatchTouchEvent(motionEvent)
        }
        return intercepted
    }

    override fun finish() {
        try {
            super.finish()
        } catch (e: Exception) {
            Logger.e(TAG, "catch Exception in finish(): ", e)
        }
        this.mActivityProxyManager.doOnFinish(this)
    }


    fun getBaseInnerProxy(): IKernelActivityProxy {
        return mBaseInnerProxy?:KernelInnerProxy().also { mBaseInnerProxy=it }
    }

    fun getProxies(): Collection<IKernelActivityProxy> {
        return this.mActivityProxyManager.getProxies()
    }


    override fun isCanPutIntoStack(): Boolean {
        return true
    }

    override fun isCheckAppStatus(): Boolean {
        return true
    }

    @SuppressLint("ObsoleteSdkInt")
    override fun isDestroyed(): Boolean {
        var sIsDestroyed = this.mIsDestroyed
        if (Build.VERSION.SDK_INT >= 17) {
            sIsDestroyed = sIsDestroyed || super.isDestroyed()
        }
        if (sIsDestroyed || isFinishing) {
            return true
        }
        return false
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        this.mActivityProxyManager.doOnActivityResult(this, requestCode, resultCode, data)
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        this.mActivityProxyManager.doOnBackPressed(this)
    }

    @SuppressLint("MissingSuperCall")
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        this.mActivityProxyManager.doOnConfigurationChanged(this, newConfig)
    }

    override fun onContentChanged() {
        super.onContentChanged()
        this.mActivityProxyManager.doOnContentChanged(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        if (!this.mIgnoreInnerOnCreate) {
            super.onCreate(savedInstanceState)
        }
        this.mActivityProxyManager.doOnCreate(this, savedInstanceState)
    }

    override fun onDestroy() {
        ActivityUtils.fixInputMethodManagerLeak(this)
        super.onDestroy()
        this.mIsDestroyed = true
        this.mActivityProxyManager.doOnDestroy(this)
    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (!this.mActivityProxyManager.doOnKeyDown(this, keyCode, event)) {
            return super.onKeyDown(keyCode, event)
        }
        return true
    }

    override fun onKeyMultiple(keyCode: Int, repeatCount: Int, event: KeyEvent?): Boolean {
        if (!this.mActivityProxyManager.doOnKeyMultiple(this, keyCode, repeatCount, event)) {
            return super.onKeyMultiple(keyCode, repeatCount, event)
        }
        return true
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        if (!this.mActivityProxyManager.doOnKeyUp(this, keyCode, event)) {
            return super.onKeyUp(keyCode, event)
        }
        return true
    }

    @SuppressLint("MissingSuperCall")
    @Deprecated("Deprecated in Java")
    override fun onMultiWindowModeChanged(isInMultiWindowMode: Boolean) {
        super.onMultiWindowModeChanged(isInMultiWindowMode)
        this.mActivityProxyManager.doOnMultiWindowModeChanged(this, isInMultiWindowMode)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        this.mActivityProxyManager.doOnNewIntent(this, intent)
    }

    override fun onPause() {
        super.onPause()
        this.mActivityProxyManager.doOnPause(this)
    }

    @SuppressLint("MissingSuperCall")
    override fun onPictureInPictureModeChanged(
        isInPictureInPictureMode: Boolean,
        newConfig: Configuration
    ) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig)
        this.mActivityProxyManager.doOnPictureInPictureModeChanged(
            this,
            isInPictureInPictureMode,
            newConfig
        )
    }

    override fun onRestart() {
        super.onRestart()
        this.mActivityProxyManager.doOnRestart(this)
    }

    override fun onResume() {
        super.onResume()
        ActivityBrightnessManager.updateActivityBrightness(this)
        this.mActivityProxyManager.doOnResume(this)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        this.mActivityProxyManager.doOnSaveInstanceState(this, outState)
    }

    override fun onStart() {
        super.onStart()
        this.mActivityProxyManager.doOnStart(this)
    }

    override fun onStop() {
        super.onStop()
        this.mActivityProxyManager.doOnStop(this)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (!this.mActivityProxyManager.doOnTouchEvent(this, event)) {
            return super.onTouchEvent(event)
        }
        return true
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        this.mActivityProxyManager.doOnWindowFocusChanged(this, hasFocus)
    }

    fun registerProxy(activityProxy: IKernelActivityProxy?) {
        this.mActivityProxyManager.registerLocalProxy(activityProxy)
    }

    fun unregisterProxy(activityProxy: IKernelActivityProxy?) {
        this.mActivityProxyManager.unregisterLocalProxy(activityProxy)
    }

    fun setIgnoreInnerOnCreate(flag: Boolean) {
        this.mIgnoreInnerOnCreate = flag
    }

    companion object {
        val TAG: String = KernelActivity::class.java.simpleName
    }
}