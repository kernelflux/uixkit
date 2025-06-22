package com.kernelflux.uixkit.ui.baseactivity

/**
 * * Activity基类相关日志打印接口
 **/
interface IBaseActivityLogger {
    fun d(tag: String, msg: String)
    fun e(tag: String, msg: String?, th: Throwable?)
    fun i(tag: String, msg: String)
    fun w(tag: String, msg: String)
}