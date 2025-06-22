package com.kernelflux.uixkit.ui.baseactivity

import android.app.Application

/**
 * * Activity基础配置类
 **/
class BaseActivityConfig private constructor(
    val app: Application?,
    val logger: IBaseActivityLogger?
) {
    data class Builder(
        private var mApp: Application? = null,
        private var mLogger: IBaseActivityLogger? = null
    ) {

        fun setApplication(application: Application) = apply {
            mApp = application
        }

        fun setLogger(loggerITG: IBaseActivityLogger) = apply {
            mLogger = loggerITG
        }

        fun build() = BaseActivityConfig(mApp, mLogger)
    }
}