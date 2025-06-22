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
open class BaseActivity : AppCompatActivity(), IBaseActivityStack {
    private val mActivityProxyManager: BaseActivityProxyManager = BaseActivityProxyManager()
    private var mBaseInnerProxy: IBaseActivityProxy? = null
    private var mIsDestroyed = false
    private var mIgnoreInnerOnCreate = false

    inner class BaseInnerProxy : IBaseActivityProxy() {
        override fun doOnActivityResult(
            baseActivity: BaseActivity,
            requestCode: Int,
            resultCode: Int,
            data: Intent?
        ) {
            super@BaseActivity.onActivityResult(requestCode, resultCode, data)
        }

        override fun doOnAttachBaseContext(baseActivity: BaseActivity, context: Context?) {
            super@BaseActivity.attachBaseContext(context)
        }

        override fun doOnBackPressed(baseActivity: BaseActivity) {
            super@BaseActivity.onBackPressed()
        }

        override fun doOnConfigurationChanged(
            baseActivity: BaseActivity,
            configuration: Configuration
        ) {
            super@BaseActivity.onConfigurationChanged(configuration)
        }

        override fun doOnContentChanged(baseActivity: BaseActivity) {
            super@BaseActivity.onContentChanged()
        }

        override fun doOnCreate(baseActivity: BaseActivity, bundle: Bundle?) {
            super@BaseActivity.onCreate(bundle)
        }

        override fun doOnDestroy(baseActivity: BaseActivity) {
            super@BaseActivity.onDestroy()
        }

        override fun doOnFinish(baseActivity: BaseActivity) {
            super@BaseActivity.finish()
        }

        override fun doOnNewIntent(baseActivity: BaseActivity, intent: Intent?) {
            super@BaseActivity.onNewIntent(intent)
        }

        override fun doOnPause(baseActivity: BaseActivity) {
            super@BaseActivity.onPause()
        }

        @RequiresApi(Build.VERSION_CODES.O)
        override fun doOnPictureInPictureModeChanged(
            baseActivity: BaseActivity,
            isInPictureInPictureMode: Boolean,
            configuration: Configuration
        ) {
            super@BaseActivity.onPictureInPictureModeChanged(
                isInPictureInPictureMode,
                configuration
            )
        }

        override fun doOnRestart(baseActivity: BaseActivity) {
            super@BaseActivity.onRestart()
        }

        override fun doOnResume(baseActivity: BaseActivity) {
            super@BaseActivity.onResume()
        }

        override fun doOnSaveInstanceState(baseActivity: BaseActivity, bundle: Bundle) {
            super@BaseActivity.onSaveInstanceState(bundle)
        }

        override fun doOnStart(baseActivity: BaseActivity) {
            super@BaseActivity.onStart()
        }

        override fun doOnStop(baseActivity: BaseActivity) {
            super@BaseActivity.onStop()
        }

        override fun doOnWindowFocusChanged(baseActivity: BaseActivity, hasFocus: Boolean) {
            super@BaseActivity.onWindowFocusChanged(hasFocus)
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


    fun getBaseInnerProxy(): IBaseActivityProxy {
        return mBaseInnerProxy?:BaseInnerProxy().also { mBaseInnerProxy=it }
    }

    fun getProxies(): Collection<IBaseActivityProxy> {
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

    fun registerProxy(activityProxy: IBaseActivityProxy?) {
        this.mActivityProxyManager.registerLocalProxy(activityProxy)
    }

    fun unregisterProxy(activityProxy: IBaseActivityProxy?) {
        this.mActivityProxyManager.unregisterLocalProxy(activityProxy)
    }

    fun setIgnoreInnerOnCreate(flag: Boolean) {
        this.mIgnoreInnerOnCreate = flag
    }

    companion object {
        val TAG: String = BaseActivity::class.java.simpleName
    }
}