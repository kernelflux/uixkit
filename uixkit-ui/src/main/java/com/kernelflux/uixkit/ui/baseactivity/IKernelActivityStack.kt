package com.kernelflux.uixkit.ui.baseactivity

import android.app.Activity

/**
 * * Activity页面栈业务接口： 是否入栈、检测App前后台状态
 **/
interface IBaseActivityStack {
    fun isCanPutIntoStack(): Boolean

    fun isCheckAppStatus(): Boolean
}

/**
 * * Activity页面入栈/出栈回调接口
 **/
interface IBaseActivityStackChangeListener {
    fun onActivityAdded(activity: Activity)

    fun onActivityRemoved(activity: Activity)
}

/**
 * * App前后台状态监听接口
 **/
interface IOnAppStatusChangedListener {
    fun onBackground()

    fun onForeground()
}