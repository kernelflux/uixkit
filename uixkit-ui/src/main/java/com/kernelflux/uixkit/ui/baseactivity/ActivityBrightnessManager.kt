package com.kernelflux.uixkit.ui.baseactivity

import android.app.Activity
import android.content.Context
import android.database.ContentObserver
import android.os.Handler
import android.provider.Settings
import android.view.WindowManager
import androidx.annotation.IntRange
import java.lang.Exception
import java.lang.RuntimeException

/**
 * * Activity亮度管理类
 **/
object ActivityBrightnessManager {
    private const val MAX_BRIGHTNESS = 255
    private const val SYSTEM_DEF_BRIGHTNESS = 125
    private var sAppContext: Context? = null
    var sBrightnessObserver: ContentObserver? = null
    var sScreenBrightness: Int = -1

    @JvmStatic
    fun getActivityBrightness(): Int {
        val brightness: Int = sScreenBrightness
        if (brightness >= 0) {
            return brightness
        }
        val topActivity: Activity =
            KernelActivityStackManager.getTopActivity() ?: return getSystemBrightness()
        val attributes = topActivity.window.attributes
        return if (!needSystemBrightness(attributes)) {
            (attributes.screenBrightness * MAX_BRIGHTNESS).toInt()
        } else try {
            getSystemBrightness()
        } catch (unused: Exception) {
            SYSTEM_DEF_BRIGHTNESS
        }
    }

    @JvmStatic
    fun getMaxBrightness(): Int {
        return 255
    }

    @JvmStatic
    fun getSystemBrightness(): Int {
        val context: Context? = sAppContext
        if (context != null) {
            return Settings.System.getInt(context.contentResolver, "screen_brightness", 125)
        }
        throw RuntimeException("请先初始化组件")
    }

    @JvmStatic
    fun init(context: Context) {
        sAppContext = context
    }

    @JvmStatic
    fun needSystemBrightness(layoutParams: WindowManager.LayoutParams): Boolean {
        val f2 = layoutParams.screenBrightness
        return f2 == -1.0f || f2 == 0.0f
    }

    @JvmStatic
    fun restoreAppToSystemBrightness() {
        val it: Iterator<Activity> = KernelActivityStackManager.getActivityStack().iterator()
        while (it.hasNext()) {
            val next = it.next()
            if (!next.isFinishing) {
                val window = next.window
                val attributes = window.attributes
                attributes.screenBrightness = -1.0f
                window.attributes = attributes
            }
        }
    }

    @JvmStatic
    fun registerScreenBrightnessObserver() {
        if (sBrightnessObserver == null) {
            sBrightnessObserver = object : ContentObserver(Handler()) {
                override fun onChange(selfChange: Boolean) {
                    super.onChange(selfChange)
                    if (sScreenBrightness >= 0) {
                        restoreAppToSystemBrightness()
                    }
                    sScreenBrightness = -1
                }
            }.also {
                sAppContext?.contentResolver?.registerContentObserver(
                    Settings.System.getUriFor("screen_brightness"),
                    true,
                    it
                )
            }
        }
    }

    @Synchronized
    @JvmStatic
    fun setActivityBrightness(@IntRange(from = 0, to = 255) brightness: Int) {
        synchronized(ActivityBrightnessManager::class.java) {
            val topActivity: Activity? = KernelActivityStackManager.getTopActivity()
            if (topActivity != null) {
                sScreenBrightness = brightness
                updateActivityBrightness(topActivity)
                registerScreenBrightnessObserver()
            }
        }
    }

    @JvmStatic
    fun updateActivityBrightness(activity: Activity?) {
        if (activity != null && sScreenBrightness >= 0) {
            val window = activity.window
            val attributes = window.attributes
            attributes.screenBrightness =
                getMaxBrightness().coerceAtMost(1.coerceAtLeast(sScreenBrightness))
                    .toFloat() / getMaxBrightness().toFloat()
            window.attributes = attributes
        }
    }
}