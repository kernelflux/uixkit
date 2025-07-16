package com.kernelflux.uixkit.core.baseactivity

import android.app.Activity

/**
 * * Activity Page Stack
 **/
interface IBaseActivityStack {
    fun isCanPutIntoStack(): Boolean

    fun isCheckAppStatus(): Boolean
    fun isStrongReference(): Boolean = false
}

interface IBaseActivityStackChangeListener {
    fun onActivityAdded(activity: Activity)

    fun onActivityRemoved(activity: Activity)
}

interface IOnAppStatusChangedListener {
    fun onBackground()

    fun onForeground()
}