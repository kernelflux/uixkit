package com.kernelflux.uixkit.ui.baseactivity.impl

import android.util.Log
import com.kernelflux.uixkit.ui.baseactivity.IBaseActivityLogger

/**
 * * 日志输出类
 **/
object Logger {
    private var sLogger: IBaseActivityLogger = getDefaultLogger()

    @JvmStatic
    fun d(tag: String, content: String) {
        sLogger.d(tag, content)
    }

    @JvmStatic
    fun e(tag: String, content: String?, th: Throwable?) {
        sLogger.e(tag, content, th)
    }

    @JvmStatic
    private fun getDefaultLogger(): IBaseActivityLogger {
        return DefaultLogger()
    }

    @JvmStatic
    fun i(tag: String, content: String) {
        sLogger.i(tag, content)
    }

    @JvmStatic
    fun setLogger(logger: IBaseActivityLogger?) {
        if (logger != null) {
            sLogger = logger
        }
    }

    @JvmStatic
    fun w(tag: String, content: String) {
        sLogger.w(tag, content)
    }


    class DefaultLogger : IBaseActivityLogger {
        override fun d(tag: String, msg: String) {
            Log.d(tag, msg)
        }

        override fun e(tag: String, msg: String?, th: Throwable?) {
            Log.e(tag, msg, th)
        }

        override fun i(tag: String, msg: String) {
            Log.i(tag, msg)
        }

        override fun w(tag: String, msg: String) {
            Log.w(tag, msg)
        }
    }
}