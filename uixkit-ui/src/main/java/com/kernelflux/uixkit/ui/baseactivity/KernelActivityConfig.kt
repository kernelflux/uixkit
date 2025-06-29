package com.kernelflux.uixkit.ui.baseactivity

import android.app.Application

/**
 * * Activity基础配置类
 **/
class BaseActivityConfig private constructor(
    val app: Application?,
    val logger: IKernelActivityLogger?
) {
    data class Builder(
        private var mApp: Application? = null,
        private var mLogger: IKernelActivityLogger? = null
    ) {

        fun setApplication(application: Application) = apply {
            mApp = application
        }

        fun setLogger(loggerITG: IKernelActivityLogger) = apply {
            mLogger = loggerITG
        }

        fun build() = BaseActivityConfig(mApp, mLogger)
    }
}