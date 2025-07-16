package com.kernelflux.uixkit.core.baseactivity

import android.app.Application
import android.util.Log
import com.kernelflux.uixkit.core.baseactivity.impl.Logger

object KernelActivityAndroidConfig {

    @JvmStatic
    private fun createLogger(): IKernelActivityLogger {
        return object : IKernelActivityLogger {
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

    @JvmStatic
    fun init(baseActivityConfig: BaseActivityConfig) {
        val logger = baseActivityConfig.logger
        if (logger != null) {
            Logger.setLogger(logger)
        }
        val sApp = baseActivityConfig.app
        if (sApp != null) {
            KernelActivityStackManager.init(sApp)
            ActivityBrightnessManager.init(sApp)
            AppStatusChangedObservable.init()
        }
    }

    @JvmStatic
    fun init(application: Application) {
        val baseActivityConfig = BaseActivityConfig.Builder()
            .setApplication(application)
            .setLogger(
                createLogger()
            ).build()
        init(baseActivityConfig)
    }
}