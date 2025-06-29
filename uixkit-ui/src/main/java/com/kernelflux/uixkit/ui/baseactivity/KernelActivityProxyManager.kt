package com.kernelflux.uixkit.ui.baseactivity

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.KeyEvent
import android.view.MotionEvent
import com.kernelflux.ktoolbox.core.CommonListenerMgr
import java.util.*

/**
 * * Activity代理管理类
 **/
class KernelActivityProxyManager : IKernelActivityProxy() {
    private val mActivityProxies: CommonListenerMgr<IKernelActivityProxy> = CommonListenerMgr()

    init {
        this.mActivityProxies.addAll(S_ACTIVITY_PROXIES.copy())
    }

    companion object {
        private val S_ACTIVITY_PROXIES: CommonListenerMgr<IKernelActivityProxy> = CommonListenerMgr()

        fun registerProxy(iKernelActivityProxy: IKernelActivityProxy?) {
            S_ACTIVITY_PROXIES.register(iKernelActivityProxy)
        }

        fun unregisterProxy(iKernelActivityProxy: IKernelActivityProxy?) {
            S_ACTIVITY_PROXIES.unregister(iKernelActivityProxy)
        }
    }

    private fun postStartNotify(notifyCallback: CommonListenerMgr.INotifyCallback<IKernelActivityProxy>) {
        this.mActivityProxies.startNotify(notifyCallback)
    }

    private fun reverseHandle(boolNotifyCallback: CommonListenerMgr.IBoolNotifyCallback<IKernelActivityProxy>): Boolean {
        return this.mActivityProxies.reverseNotify(boolNotifyCallback)
    }

    override fun doOnAttachBaseContext(kernelActivity: KernelActivity, context: Context?) {
        postStartNotify(object : CommonListenerMgr.INotifyCallback<IKernelActivityProxy> {
            override fun onNotify(listener: IKernelActivityProxy) {
                listener.doOnAttachBaseContext(kernelActivity, context)
            }
        })
    }

    override fun doOnDispatchTouchEvent(
        kernelActivity: KernelActivity,
        motionEvent: MotionEvent?
    ): Boolean {
        return reverseHandle(object : CommonListenerMgr.IBoolNotifyCallback<IKernelActivityProxy> {
            override fun onNotify(listener: IKernelActivityProxy): Boolean {
                return listener.doOnDispatchTouchEvent(kernelActivity, motionEvent)
            }
        })
    }


    override fun doOnActivityResult(
        kernelActivity: KernelActivity,
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        postStartNotify(object : CommonListenerMgr.INotifyCallback<IKernelActivityProxy> {
            override fun onNotify(listener: IKernelActivityProxy) {
                listener.doOnActivityResult(kernelActivity, requestCode, resultCode, data)
            }
        })
    }

    override fun doOnBackPressed(kernelActivity: KernelActivity) {
        postStartNotify(object : CommonListenerMgr.INotifyCallback<IKernelActivityProxy> {
            override fun onNotify(listener: IKernelActivityProxy) {
                listener.doOnBackPressed(kernelActivity)
            }
        })
    }

    override fun doOnConfigurationChanged(
        kernelActivity: KernelActivity,
        configuration: Configuration
    ) {
        postStartNotify(object : CommonListenerMgr.INotifyCallback<IKernelActivityProxy> {
            override fun onNotify(listener: IKernelActivityProxy) {
                listener.doOnConfigurationChanged(kernelActivity, configuration)
            }
        })
    }

    override fun doOnContentChanged(kernelActivity: KernelActivity) {
        postStartNotify(object : CommonListenerMgr.INotifyCallback<IKernelActivityProxy> {
            override fun onNotify(listener: IKernelActivityProxy) {
                listener.doOnContentChanged(kernelActivity)
            }
        })
    }

    override fun doOnCreate(kernelActivity: KernelActivity, bundle: Bundle?) {
        postStartNotify(object : CommonListenerMgr.INotifyCallback<IKernelActivityProxy> {
            override fun onNotify(listener: IKernelActivityProxy) {
                listener.doOnCreate(kernelActivity, bundle)
            }
        })
    }

    override fun doOnDestroy(kernelActivity: KernelActivity) {
        postStartNotify(object : CommonListenerMgr.INotifyCallback<IKernelActivityProxy> {
            override fun onNotify(listener: IKernelActivityProxy) {
                listener.doOnDestroy(kernelActivity)
            }
        })
    }

    override fun doOnFinish(kernelActivity: KernelActivity) {
        postStartNotify(object : CommonListenerMgr.INotifyCallback<IKernelActivityProxy> {
            override fun onNotify(listener: IKernelActivityProxy) {
                listener.doOnFinish(kernelActivity)
            }
        })
    }

    override fun doOnKeyDown(
        kernelActivity: KernelActivity,
        keyCode: Int,
        keyEvent: KeyEvent?
    ): Boolean {
        return reverseHandle(object : CommonListenerMgr.IBoolNotifyCallback<IKernelActivityProxy> {
            override fun onNotify(listener: IKernelActivityProxy): Boolean {
                return listener.doOnKeyDown(kernelActivity, keyCode, keyEvent)
            }
        })
    }

    override fun doOnKeyMultiple(
        kernelActivity: KernelActivity,
        keyCode: Int,
        repeatCount: Int,
        event: KeyEvent?
    ): Boolean {
        return reverseHandle(object : CommonListenerMgr.IBoolNotifyCallback<IKernelActivityProxy> {
            override fun onNotify(listener: IKernelActivityProxy): Boolean {
                return listener.doOnKeyMultiple(kernelActivity, keyCode, repeatCount, event)
            }
        })
    }

    override fun doOnKeyUp(
        kernelActivity: KernelActivity,
        keyCode: Int,
        keyEvent: KeyEvent?
    ): Boolean {
        return reverseHandle(object : CommonListenerMgr.IBoolNotifyCallback<IKernelActivityProxy> {
            override fun onNotify(listener: IKernelActivityProxy): Boolean {
                return listener.doOnKeyUp(kernelActivity, keyCode, keyEvent)
            }
        })
    }

    override fun doOnMultiWindowModeChanged(
        kernelActivity: KernelActivity,
        isInMultiWindowMode: Boolean
    ) {
        postStartNotify(object : CommonListenerMgr.INotifyCallback<IKernelActivityProxy> {
            override fun onNotify(listener: IKernelActivityProxy) {
                listener.doOnMultiWindowModeChanged(kernelActivity, isInMultiWindowMode)
            }
        })
    }

    override fun doOnNewIntent(kernelActivity: KernelActivity, intent: Intent?) {
        postStartNotify(object : CommonListenerMgr.INotifyCallback<IKernelActivityProxy> {
            override fun onNotify(listener: IKernelActivityProxy) {
                listener.doOnNewIntent(kernelActivity, intent)
            }
        })
    }

    override fun doOnPause(kernelActivity: KernelActivity) {
        postStartNotify(object : CommonListenerMgr.INotifyCallback<IKernelActivityProxy> {
            override fun onNotify(listener: IKernelActivityProxy) {
                listener.doOnPause(kernelActivity)
            }
        })
    }

    override fun doOnPictureInPictureModeChanged(
        kernelActivity: KernelActivity,
        isInPictureInPictureMode: Boolean,
        configuration: Configuration
    ) {
        postStartNotify(object : CommonListenerMgr.INotifyCallback<IKernelActivityProxy> {
            override fun onNotify(listener: IKernelActivityProxy) {
                listener.doOnPictureInPictureModeChanged(
                    kernelActivity,
                    isInPictureInPictureMode,
                    configuration
                )
            }
        })
    }

    override fun doOnRestart(kernelActivity: KernelActivity) {
        postStartNotify(object : CommonListenerMgr.INotifyCallback<IKernelActivityProxy> {
            override fun onNotify(listener: IKernelActivityProxy) {
                listener.doOnRestart(kernelActivity)
            }
        })
    }


    override fun doOnResume(kernelActivity: KernelActivity) {
        postStartNotify(object : CommonListenerMgr.INotifyCallback<IKernelActivityProxy> {
            override fun onNotify(listener: IKernelActivityProxy) {
                listener.doOnResume(kernelActivity)
            }
        })
    }

    override fun doOnSaveInstanceState(kernelActivity: KernelActivity, bundle: Bundle) {
        postStartNotify(object : CommonListenerMgr.INotifyCallback<IKernelActivityProxy> {
            override fun onNotify(listener: IKernelActivityProxy) {
                listener.doOnSaveInstanceState(kernelActivity, bundle)
            }
        })
    }


    override fun doOnStart(kernelActivity: KernelActivity) {
        postStartNotify(object : CommonListenerMgr.INotifyCallback<IKernelActivityProxy> {
            override fun onNotify(listener: IKernelActivityProxy) {
                listener.doOnStart(kernelActivity)
            }
        })
    }

    override fun doOnStop(kernelActivity: KernelActivity) {
        postStartNotify(object : CommonListenerMgr.INotifyCallback<IKernelActivityProxy> {
            override fun onNotify(listener: IKernelActivityProxy) {
                listener.doOnStop(kernelActivity)
            }
        })
    }


    override fun doOnTouchEvent(
        kernelActivity: KernelActivity,
        motionEvent: MotionEvent?
    ): Boolean {
        return reverseHandle(object : CommonListenerMgr.IBoolNotifyCallback<IKernelActivityProxy> {
            override fun onNotify(listener: IKernelActivityProxy): Boolean {
                return listener.doOnTouchEvent(kernelActivity, motionEvent)
            }
        })
    }

    override fun doOnWindowFocusChanged(kernelActivity: KernelActivity, hasFocus: Boolean) {
        postStartNotify(object : CommonListenerMgr.INotifyCallback<IKernelActivityProxy> {
            override fun onNotify(listener: IKernelActivityProxy) {
                listener.doOnWindowFocusChanged(kernelActivity, hasFocus)
            }
        })
    }

    fun getProxies(): LinkedList<IKernelActivityProxy> {
        return this.mActivityProxies.copy()
    }

    fun registerLocalProxy(baseActivityProxy: IKernelActivityProxy?) {
        this.mActivityProxies.register(baseActivityProxy)
    }

    fun unregisterLocalProxy(baseActivityProxy: IKernelActivityProxy?) {
        this.mActivityProxies.unregister(baseActivityProxy)
    }
}