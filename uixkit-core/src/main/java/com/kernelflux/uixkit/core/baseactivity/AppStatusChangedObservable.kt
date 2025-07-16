package com.kernelflux.uixkit.core.baseactivity

import com.kernelflux.ktoolbox.core.WeakListenerMgr
import com.kernelflux.uixkit.core.baseactivity.impl.ActivityStackImpl
import com.kernelflux.uixkit.core.baseactivity.impl.Logger

internal object AppStatusChangedObservable {
    private val LISTENER_MGR: WeakListenerMgr<IOnAppStatusChangedListener> = WeakListenerMgr()
    private val TAG = AppStatusChangedObservable::class.java.simpleName
    private var sIsForeground = false

    @JvmStatic
    fun isForeground() = sIsForeground

    @JvmStatic
    fun init() {
        Logger.d(TAG, "bind lifecycle to detach")
        ActivityStackImpl.INSTANCE.setAppStatusChangedListener(object :
            IOnAppStatusChangedListener {
            override fun onBackground() {
                notifySwitchToBackground()
            }

            override fun onForeground() {
                notifySwitchToForeground()
            }
        })
    }

    @JvmStatic
    private fun notifySwitchToBackground() {
        sIsForeground = false
        Logger.d(TAG, "app switch to background")
        LISTENER_MGR.startNotify(object :
            WeakListenerMgr.INotifyCallback<IOnAppStatusChangedListener> {
            override fun onNotify(listener: IOnAppStatusChangedListener) {
                listener.onBackground()
            }
        })
    }

    @JvmStatic
    private fun notifySwitchToForeground() {
        sIsForeground = true
        Logger.d(TAG, "app switch to foreground")
        LISTENER_MGR.startNotify(object :
            WeakListenerMgr.INotifyCallback<IOnAppStatusChangedListener> {
            override fun onNotify(listener: IOnAppStatusChangedListener) {
                listener.onForeground()
            }
        })
    }

    @JvmStatic
    fun register(listener: IOnAppStatusChangedListener?) {
        listener?.also {
            LISTENER_MGR.register(it)
        }
    }

    @JvmStatic
    fun unregister(listener: IOnAppStatusChangedListener?) {
        listener?.also {
            LISTENER_MGR.unregister(it)
        }
    }
}