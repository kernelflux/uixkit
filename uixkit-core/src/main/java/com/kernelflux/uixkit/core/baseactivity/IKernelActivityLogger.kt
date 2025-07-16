package com.kernelflux.uixkit.core.baseactivity

/**
 * * Activity Base Logger
 **/
interface IKernelActivityLogger {
    fun d(tag: String, msg: String)
    fun e(tag: String, msg: String?, th: Throwable?)
    fun i(tag: String, msg: String)
    fun w(tag: String, msg: String)
}