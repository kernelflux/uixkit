package com.kernelflux.uixkit.core.baseactivity.impl

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Build
import android.view.View
import android.view.Window
import android.view.inputmethod.InputMethodManager
import java.lang.Exception
import java.util.*


object ActivityUtils {
    private const val TAG = "GetCurrentActivities"

    @SuppressLint("PrivateApi")
    @JvmStatic
    fun fixInputMethodManagerLeak(activity: Activity?) {
        val inputMethodManager: InputMethodManager? =
            activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        if (inputMethodManager != null) {
            val strArr = arrayOf("mCurRootView", "mServedView", "mNextServedView", "mLastSrvView")
            val window: Window = activity.window
            for (itemArrIndex in strArr.indices) {
                try {
                    val declaredField = InputMethodManager::class.java.getDeclaredField(
                        strArr[itemArrIndex]
                    )
                    if (!declaredField.isAccessible) {
                        declaredField.isAccessible = true
                    }
                    if (itemArrIndex == 0) {
                        val obj = declaredField[inputMethodManager]
                        if (obj is View) {
                            if (obj !== window.decorView) {
                                return
                            }
                        }
                    }
                    declaredField[inputMethodManager] = null
                } catch (unused: Throwable) {
                    //
                }
            }
        }
    }

    @JvmStatic
    fun getActivitiesByReflect(): List<Activity> {
        val linkedList: LinkedList<Activity> = LinkedList<Activity>()
        var activity: Activity? = null
        return try {
            val activityThread = getActivityThread()
            val declaredField = activityThread?.javaClass?.getDeclaredField("mActivities")
            declaredField?.isAccessible = true
            val obj: Any? = declaredField?.get(activityThread)
            if (obj !is Map<*, *>) {
                return linkedList
            }
            for (obj2 in obj.values) {
                val cls: Class<*>? = obj2?.javaClass
                if (cls != null) {
                    val declaredField2 = cls.getDeclaredField("activity")
                    declaredField2.isAccessible = true
                    val activity2: Activity = declaredField2[obj2] as Activity
                    if (activity == null) {
                        val declaredField3 = cls.getDeclaredField("paused")
                        declaredField3.isAccessible = true
                        if (!declaredField3.getBoolean(obj2)) {
                            activity = activity2
                        } else {
                            linkedList.add(activity2)
                        }
                    } else {
                        linkedList.add(activity2)
                    }
                }
            }
            if (activity != null) {
                linkedList.addFirst(activity)
            }
            linkedList
        } catch (e2: Exception) {
            Logger.e(TAG, "getActivitiesByReflect fail! ", e2)
            linkedList
        }
    }

    @JvmStatic
    fun getActivityThread(): Any? {
        val activityThreadInActivityThreadStaticField =
            getActivityThreadInActivityThreadStaticField()
        return activityThreadInActivityThreadStaticField
            ?: getActivityThreadInActivityThreadStaticMethod()
    }


    @SuppressLint("DiscouragedPrivateApi", "PrivateApi")
    @JvmStatic
    fun getActivityThreadInActivityThreadStaticField(): Any? {
        return try {
            val declaredField = Class.forName("android.app.ActivityThread")
                .getDeclaredField("sCurrentActivityThread")
            declaredField.isAccessible = true
            declaredField[null]
        } catch (e2: Exception) {
            Logger.e(TAG, "getActivityThreadInActivityThreadStaticField: ", e2)
            null
        }
    }

    @SuppressLint("PrivateApi")
    @JvmStatic
    fun getActivityThreadInActivityThreadStaticMethod(): Any? {
        return try {
            Class.forName("android.app.ActivityThread")
                .getMethod("currentActivityThread", *arrayOfNulls<Class<*>>(0))
                .invoke(null, *arrayOfNulls(0))
        } catch (e2: Exception) {
            Logger.e(TAG, "getActivityThreadInActivityThreadStaticMethod: ", e2)
            null
        }
    }

    @SuppressLint("ObsoleteSdkInt")
    @JvmStatic
    fun isActivityAlive(activity: Activity?): Boolean {
        return activity != null && !activity.isFinishing && (Build.VERSION.SDK_INT < 17 || !activity.isDestroyed)
    }
}