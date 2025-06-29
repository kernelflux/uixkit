package com.kernelflux.uixkit.ui.baseactivity

import android.app.Activity
import android.app.Application
import androidx.annotation.AnimRes
import androidx.annotation.IntRange
import com.kernelflux.uixkit.ui.baseactivity.impl.ActivityStackImpl
import java.util.*

/**
 * * Activity栈管理外部类
 **/
object KernelActivityStackManager {

    @JvmStatic
    fun finishActivity(activity: Activity) {
        ActivityStackImpl.INSTANCE.finishActivity(activity)
    }

    @JvmStatic
    fun finishAllActivities() {
        ActivityStackImpl.INSTANCE.finishAllActivities()
    }

    @JvmStatic
    fun finishAllActivitiesExceptNewest() {
        ActivityStackImpl.INSTANCE.finishAllActivitiesExceptNewest()
    }

    @JvmStatic
    fun finishIntervalActivities(cls: Class<out Activity>, @IntRange(from = 1) num: Int) {
        ActivityStackImpl.INSTANCE.finishIntervalActivities(cls, num)
    }

    @JvmStatic
    fun finishOtherActivities(cls: Class<out Activity>) {
        ActivityStackImpl.INSTANCE.finishActivities(cls)
    }

    @JvmStatic
    fun finishToActivity(activity: Activity, isIncludeSelf: Boolean): Boolean {
        return ActivityStackImpl.INSTANCE.finishToActivity(activity, isIncludeSelf)
    }

    @JvmStatic
    fun getActivityByActivityID(activityId: Int): Activity? {
        return ActivityStackImpl.INSTANCE.getActivityByActivityID(activityId)
    }

    @JvmStatic
    fun getActivityByName(str: String?): Activity? {
        return ActivityStackImpl.INSTANCE.getActivityByName(str)
    }

    @JvmStatic
    fun getActivityBySimpleName(str: String?): Activity? {
        return ActivityStackImpl.INSTANCE.getActivityBySimpleName(str)
    }

    @JvmStatic
    fun getActivityId(activity: Activity): Int {
        return ActivityStackImpl.INSTANCE.getActivityId(activity)
    }

    @JvmStatic
    fun getActivityStack(): LinkedList<Activity> {
        return ActivityStackImpl.INSTANCE.getActivityStack()
    }

    @JvmStatic
    fun getTopActivity(): Activity? {
        return ActivityStackImpl.INSTANCE.getTopActivity()
    }

    @Synchronized
    @JvmStatic
    fun init(application: Application) {
        synchronized(KernelActivityStackManager::class.java) {
            ActivityStackImpl.INSTANCE.init(
                application
            )
        }
    }

    @JvmStatic
    fun isContainActivity(activity: Activity): Boolean {
        return ActivityStackImpl.INSTANCE.isContainActivity(activity)
    }

    @JvmStatic
    fun isEmpty(): Boolean {
        return ActivityStackImpl.INSTANCE.isEmpty()
    }

    @JvmStatic
    fun putTopActivity(activity: Activity) {
        ActivityStackImpl.INSTANCE.putTopActivity(activity)
    }

    @JvmStatic
    fun registerStackChangedListener(listener: IBaseActivityStackChangeListener?) {
        ActivityStackImpl.INSTANCE.registerActivityStackChangedListener(
            listener
        )
    }

    @JvmStatic
    fun removeActivity(activity: Activity) {
        ActivityStackImpl.INSTANCE.removeActivity(activity)
    }

    @JvmStatic
    fun size(): Int {
        return ActivityStackImpl.INSTANCE.size()
    }

    @Synchronized
    @JvmStatic
    fun unInit() {
        synchronized(KernelActivityStackManager::class.java) { ActivityStackImpl.INSTANCE.unInit() }
    }

    @JvmStatic
    fun unregisterStackChangedListener(listener: IBaseActivityStackChangeListener?) {
        ActivityStackImpl.INSTANCE.unregisterActivityStackChangedListener(
            listener
        )
    }

    @JvmStatic
    fun finishActivity(activity: Activity, isLoadAnim: Boolean) {
        ActivityStackImpl.INSTANCE.finishActivity(activity, isLoadAnim)
    }

    @JvmStatic
    fun finishAllActivities(isLoadAnim: Boolean) {
        ActivityStackImpl.INSTANCE.finishAllActivities(isLoadAnim)
    }

    @JvmStatic
    fun finishAllActivitiesExceptNewest(isLoadAnim: Boolean) {
        ActivityStackImpl.INSTANCE.finishAllActivitiesExceptNewest(isLoadAnim)
    }

    @JvmStatic
    fun finishIntervalActivities(
        cls: Class<out Activity>,
        @IntRange(from = 1) num: Int,
        isLoadAnim: Boolean
    ) {
        ActivityStackImpl.INSTANCE.finishIntervalActivities(cls, num, isLoadAnim)
    }

    @JvmStatic
    fun finishOtherActivities(cls: Class<out Activity>, isLoadAnim: Boolean) {
        ActivityStackImpl.INSTANCE.finishActivities(cls, isLoadAnim)
    }

    @JvmStatic
    fun finishToActivity(activity: Activity, isIncludeSelf: Boolean, isLoadAnim: Boolean): Boolean {
        return ActivityStackImpl.INSTANCE.finishToActivity(activity, isIncludeSelf, isLoadAnim)
    }

    @JvmStatic
    fun isContainActivity(cls: Class<out Activity>): Boolean {
        return ActivityStackImpl.INSTANCE.isContainActivity(cls)
    }

    @JvmStatic
    fun finishActivity(
        activity: Activity,
        @AnimRes enterAnim: Int,
        @AnimRes exitAnim: Int
    ) {
        ActivityStackImpl.INSTANCE.finishActivity(activity, enterAnim, exitAnim)
    }

    @JvmStatic
    fun finishAllActivities(
        @AnimRes enterAnim: Int,
        @AnimRes exitAnim: Int
    ) {
        ActivityStackImpl.INSTANCE.finishAllActivities(enterAnim, exitAnim)
    }

    @JvmStatic
    fun finishAllActivitiesExceptNewest(@AnimRes enterAnim: Int, @AnimRes exitAnim: Int) {
        ActivityStackImpl.INSTANCE.finishAllActivitiesExceptNewest(enterAnim, exitAnim)
    }

    @JvmStatic
    fun finishIntervalActivities(
        cls: Class<out Activity>,
        @IntRange(from = 1) num: Int,
        @AnimRes enterAnim: Int,
        @AnimRes exitAnim: Int
    ) {
        ActivityStackImpl.INSTANCE.finishIntervalActivities(cls, num, enterAnim, exitAnim)
    }

    @JvmStatic
    fun finishActivities(clsArr: Array<Class<out Activity>>, isLoadAnim: Boolean) {
        ActivityStackImpl.INSTANCE.finishActivities(clsArr, isLoadAnim)
    }

    @JvmStatic
    fun finishToActivity(
        activity: Activity,
        isIncludeSelf: Boolean,
        @AnimRes enterAnim: Int,
        @AnimRes exitAnim: Int
    ): Boolean {
        return ActivityStackImpl.INSTANCE.finishToActivity(
            activity,
            isIncludeSelf,
            enterAnim,
            exitAnim
        )
    }

    @JvmStatic
    fun finishActivity(cls: Class<out Activity>) {
        ActivityStackImpl.INSTANCE.finishActivity(cls)
    }

    @JvmStatic
    fun finishOtherActivities(
        cls: Class<out Activity>,
        @AnimRes enterAnim: Int,
        @AnimRes exitAnim: Int
    ) {
        ActivityStackImpl.INSTANCE.finishOtherActivities(cls, enterAnim, exitAnim)
    }

    @JvmStatic
    fun finishToActivity(cls: Class<out Activity>, isIncludeSelf: Boolean): Boolean {
        return ActivityStackImpl.INSTANCE.finishToActivity(cls, isIncludeSelf)
    }

    @JvmStatic
    fun finishActivity(cls: Class<out Activity>, isLoadAnim: Boolean) {
        ActivityStackImpl.INSTANCE.finishActivity(cls, isLoadAnim)
    }

    @JvmStatic
    fun finishToActivity(
        cls: Class<out Activity>,
        isIncludeSelf: Boolean,
        isLoadAnim: Boolean
    ): Boolean {
        return ActivityStackImpl.INSTANCE.finishToActivity(cls, isIncludeSelf, isLoadAnim)
    }

    @JvmStatic
    fun finishActivity(cls: Class<out Activity>, @AnimRes enterAnim: Int, @AnimRes exitAnim: Int) {
        ActivityStackImpl.INSTANCE.finishActivity(cls, enterAnim, exitAnim)
    }

    @JvmStatic
    fun finishToActivity(
        cls: Class<out Activity>,
        isIncludeSelf: Boolean,
        @AnimRes enterAnim: Int,
        @AnimRes exitAnim: Int
    ): Boolean {
        return ActivityStackImpl.INSTANCE.finishToActivity(cls, isIncludeSelf, enterAnim, exitAnim)
    }
}