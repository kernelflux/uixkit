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
class BaseActivityProxyManager : IBaseActivityProxy() {
    private val mActivityProxies: CommonListenerMgr<IBaseActivityProxy> = CommonListenerMgr()

    init {
        this.mActivityProxies.addAll(S_ACTIVITY_PROXIES.copy())
    }

    companion object {
        private val S_ACTIVITY_PROXIES: CommonListenerMgr<IBaseActivityProxy> = CommonListenerMgr()

        fun registerProxy(iBaseActivityProxy: IBaseActivityProxy?) {
            S_ACTIVITY_PROXIES.register(iBaseActivityProxy)
        }

        fun unregisterProxy(iBaseActivityProxy: IBaseActivityProxy?) {
            S_ACTIVITY_PROXIES.unregister(iBaseActivityProxy)
        }
    }

    private fun postStartNotify(notifyCallback: CommonListenerMgr.INotifyCallback<IBaseActivityProxy>) {
        this.mActivityProxies.startNotify(notifyCallback)
    }

    private fun reverseHandle(boolNotifyCallback: CommonListenerMgr.IBoolNotifyCallback<IBaseActivityProxy>): Boolean {
        return this.mActivityProxies.reverseNotify(boolNotifyCallback)
    }

    override fun doOnAttachBaseContext(baseActivity: BaseActivity, context: Context?) {
        postStartNotify(object : CommonListenerMgr.INotifyCallback<IBaseActivityProxy> {
            override fun onNotify(listener: IBaseActivityProxy) {
                listener.doOnAttachBaseContext(baseActivity, context)
            }
        })
    }

    override fun doOnDispatchTouchEvent(
        baseActivity: BaseActivity,
        motionEvent: MotionEvent?
    ): Boolean {
        return reverseHandle(object : CommonListenerMgr.IBoolNotifyCallback<IBaseActivityProxy> {
            override fun onNotify(listener: IBaseActivityProxy): Boolean {
                return listener.doOnDispatchTouchEvent(baseActivity, motionEvent)
            }
        })
    }


    override fun doOnActivityResult(
        baseActivity: BaseActivity,
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        postStartNotify(object : CommonListenerMgr.INotifyCallback<IBaseActivityProxy> {
            override fun onNotify(listener: IBaseActivityProxy) {
                listener.doOnActivityResult(baseActivity, requestCode, resultCode, data)
            }
        })
    }

    override fun doOnBackPressed(baseActivity: BaseActivity) {
        postStartNotify(object : CommonListenerMgr.INotifyCallback<IBaseActivityProxy> {
            override fun onNotify(listener: IBaseActivityProxy) {
                listener.doOnBackPressed(baseActivity)
            }
        })
    }

    override fun doOnConfigurationChanged(
        baseActivity: BaseActivity,
        configuration: Configuration
    ) {
        postStartNotify(object : CommonListenerMgr.INotifyCallback<IBaseActivityProxy> {
            override fun onNotify(listener: IBaseActivityProxy) {
                listener.doOnConfigurationChanged(baseActivity, configuration)
            }
        })
    }

    override fun doOnContentChanged(baseActivity: BaseActivity) {
        postStartNotify(object : CommonListenerMgr.INotifyCallback<IBaseActivityProxy> {
            override fun onNotify(listener: IBaseActivityProxy) {
                listener.doOnContentChanged(baseActivity)
            }
        })
    }

    override fun doOnCreate(baseActivity: BaseActivity, bundle: Bundle?) {
        postStartNotify(object : CommonListenerMgr.INotifyCallback<IBaseActivityProxy> {
            override fun onNotify(listener: IBaseActivityProxy) {
                listener.doOnCreate(baseActivity, bundle)
            }
        })
    }

    override fun doOnDestroy(baseActivity: BaseActivity) {
        postStartNotify(object : CommonListenerMgr.INotifyCallback<IBaseActivityProxy> {
            override fun onNotify(listener: IBaseActivityProxy) {
                listener.doOnDestroy(baseActivity)
            }
        })
    }

    override fun doOnFinish(baseActivity: BaseActivity) {
        postStartNotify(object : CommonListenerMgr.INotifyCallback<IBaseActivityProxy> {
            override fun onNotify(listener: IBaseActivityProxy) {
                listener.doOnFinish(baseActivity)
            }
        })
    }

    override fun doOnKeyDown(
        baseActivity: BaseActivity,
        keyCode: Int,
        keyEvent: KeyEvent?
    ): Boolean {
        return reverseHandle(object : CommonListenerMgr.IBoolNotifyCallback<IBaseActivityProxy> {
            override fun onNotify(listener: IBaseActivityProxy): Boolean {
                return listener.doOnKeyDown(baseActivity, keyCode, keyEvent)
            }
        })
    }

    override fun doOnKeyMultiple(
        baseActivity: BaseActivity,
        keyCode: Int,
        repeatCount: Int,
        event: KeyEvent?
    ): Boolean {
        return reverseHandle(object : CommonListenerMgr.IBoolNotifyCallback<IBaseActivityProxy> {
            override fun onNotify(listener: IBaseActivityProxy): Boolean {
                return listener.doOnKeyMultiple(baseActivity, keyCode, repeatCount, event)
            }
        })
    }

    override fun doOnKeyUp(
        baseActivity: BaseActivity,
        keyCode: Int,
        keyEvent: KeyEvent?
    ): Boolean {
        return reverseHandle(object : CommonListenerMgr.IBoolNotifyCallback<IBaseActivityProxy> {
            override fun onNotify(listener: IBaseActivityProxy): Boolean {
                return listener.doOnKeyUp(baseActivity, keyCode, keyEvent)
            }
        })
    }

    override fun doOnMultiWindowModeChanged(
        baseActivity: BaseActivity,
        isInMultiWindowMode: Boolean
    ) {
        postStartNotify(object : CommonListenerMgr.INotifyCallback<IBaseActivityProxy> {
            override fun onNotify(listener: IBaseActivityProxy) {
                listener.doOnMultiWindowModeChanged(baseActivity, isInMultiWindowMode)
            }
        })
    }

    override fun doOnNewIntent(baseActivity: BaseActivity, intent: Intent?) {
        postStartNotify(object : CommonListenerMgr.INotifyCallback<IBaseActivityProxy> {
            override fun onNotify(listener: IBaseActivityProxy) {
                listener.doOnNewIntent(baseActivity, intent)
            }
        })
    }

    override fun doOnPause(baseActivity: BaseActivity) {
        postStartNotify(object : CommonListenerMgr.INotifyCallback<IBaseActivityProxy> {
            override fun onNotify(listener: IBaseActivityProxy) {
                listener.doOnPause(baseActivity)
            }
        })
    }

    override fun doOnPictureInPictureModeChanged(
        baseActivity: BaseActivity,
        isInPictureInPictureMode: Boolean,
        configuration: Configuration
    ) {
        postStartNotify(object : CommonListenerMgr.INotifyCallback<IBaseActivityProxy> {
            override fun onNotify(listener: IBaseActivityProxy) {
                listener.doOnPictureInPictureModeChanged(
                    baseActivity,
                    isInPictureInPictureMode,
                    configuration
                )
            }
        })
    }

    override fun doOnRestart(baseActivity: BaseActivity) {
        postStartNotify(object : CommonListenerMgr.INotifyCallback<IBaseActivityProxy> {
            override fun onNotify(listener: IBaseActivityProxy) {
                listener.doOnRestart(baseActivity)
            }
        })
    }


    override fun doOnResume(baseActivity: BaseActivity) {
        postStartNotify(object : CommonListenerMgr.INotifyCallback<IBaseActivityProxy> {
            override fun onNotify(listener: IBaseActivityProxy) {
                listener.doOnResume(baseActivity)
            }
        })
    }

    override fun doOnSaveInstanceState(baseActivity: BaseActivity, bundle: Bundle) {
        postStartNotify(object : CommonListenerMgr.INotifyCallback<IBaseActivityProxy> {
            override fun onNotify(listener: IBaseActivityProxy) {
                listener.doOnSaveInstanceState(baseActivity, bundle)
            }
        })
    }


    override fun doOnStart(baseActivity: BaseActivity) {
        postStartNotify(object : CommonListenerMgr.INotifyCallback<IBaseActivityProxy> {
            override fun onNotify(listener: IBaseActivityProxy) {
                listener.doOnStart(baseActivity)
            }
        })
    }

    override fun doOnStop(baseActivity: BaseActivity) {
        postStartNotify(object : CommonListenerMgr.INotifyCallback<IBaseActivityProxy> {
            override fun onNotify(listener: IBaseActivityProxy) {
                listener.doOnStop(baseActivity)
            }
        })
    }


    override fun doOnTouchEvent(
        baseActivity: BaseActivity,
        motionEvent: MotionEvent?
    ): Boolean {
        return reverseHandle(object : CommonListenerMgr.IBoolNotifyCallback<IBaseActivityProxy> {
            override fun onNotify(listener: IBaseActivityProxy): Boolean {
                return listener.doOnTouchEvent(baseActivity, motionEvent)
            }
        })
    }

    override fun doOnWindowFocusChanged(baseActivity: BaseActivity, hasFocus: Boolean) {
        postStartNotify(object : CommonListenerMgr.INotifyCallback<IBaseActivityProxy> {
            override fun onNotify(listener: IBaseActivityProxy) {
                listener.doOnWindowFocusChanged(baseActivity, hasFocus)
            }
        })
    }

    fun getProxies(): LinkedList<IBaseActivityProxy> {
        return this.mActivityProxies.copy()
    }

    fun registerLocalProxy(baseActivityProxy: IBaseActivityProxy?) {
        this.mActivityProxies.register(baseActivityProxy)
    }

    fun unregisterLocalProxy(baseActivityProxy: IBaseActivityProxy?) {
        this.mActivityProxies.unregister(baseActivityProxy)
    }
}