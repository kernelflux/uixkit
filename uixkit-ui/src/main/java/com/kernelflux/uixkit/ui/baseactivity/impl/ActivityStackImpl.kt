package com.kernelflux.uixkit.ui.baseactivity.impl

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.annotation.AnimRes
import androidx.annotation.IntRange
import com.kernelflux.ktoolbox.core.WeakListenerMgr
import com.kernelflux.uixkit.ui.baseactivity.IBaseActivityStack
import com.kernelflux.uixkit.ui.baseactivity.IBaseActivityStackChangeListener
import com.kernelflux.uixkit.ui.baseactivity.IOnAppStatusChangedListener
import java.lang.StringBuilder
import java.util.*

/**
 * * Activity页面栈实现
 **/
internal class ActivityStackImpl : Application.ActivityLifecycleCallbacks {
    private val mActivityStack: LinkedList<Activity> = LinkedList()
    private val mStackListenerMgr: WeakListenerMgr<IBaseActivityStackChangeListener> =
        WeakListenerMgr()
    private var mConfigCount = 0
    private var mForegroundCount = 0
    private var mIsBackground = false
    private var mOnAppStatusChangeListener: IOnAppStatusChangedListener? = null

    private fun <T> isBeContained(tArr: Array<T>, t2: T): Boolean {
        for (t3 in tArr) {
            if (t2 == t3) {
                return true
            }
        }
        return false
    }


    override fun toString(): String {
        val activityStack = getActivityStack()
        val sb = StringBuilder("Stack:{ ")
        for (activity in activityStack) {
            sb.append(activity.hashCode())
            sb.append(" ")
        }
        sb.append("}")
        return sb.toString()
    }

    fun setAppStatusChangedListener(listener: IOnAppStatusChangedListener) {
        this.mOnAppStatusChangeListener = listener
    }

    fun registerActivityStackChangedListener(listener: IBaseActivityStackChangeListener?) {
        listener?.also {
            this.mStackListenerMgr.register(it)
        }
    }

    fun unregisterActivityStackChangedListener(listener: IBaseActivityStackChangeListener?) {
        listener?.also {
            this.mStackListenerMgr.unregister(it)
        }
    }

    fun finishActivity(activity: Activity) {
        finishActivity(activity, false)
    }

    fun finishOtherActivities(cls: Class<out Activity>) {
        finishOtherActivities(cls, false)
    }

    fun finishOtherActivities(cls: Class<out Activity>, isLoadAnim: Boolean) {
        Logger.d(TAG, "finishOtherActivities $cls isLoadAnim=$isLoadAnim")
        for (activity in getActivityStack()) {
            if (activity.javaClass != cls) {
                finishActivity(activity, isLoadAnim)
            }
        }
    }

    fun finishOtherActivities(
        cls: Class<out Activity>,
        @AnimRes enterAnim: Int,
        @AnimRes exitAnim: Int
    ) {
        Logger.d(TAG, "finishOtherActivities $cls enterAnim=$enterAnim  exitAnim=$exitAnim ")
        for (activity in getActivityStack()) {
            if (activity.javaClass != cls) {
                finishActivity(activity, enterAnim, exitAnim)
            }
        }
    }


    fun finishAllActivities() {
        finishAllActivities(false)
    }

    fun finishAllActivitiesExceptNewest() {
        finishAllActivities(false)
    }

    fun finishActivities(cls: Class<out Activity>) {
        finishActivities(cls, false)
    }

    fun finishActivities(cls: Class<out Activity>, isLoadAnim: Boolean) {
        Logger.d(TAG, "finishActivities $cls isLoadAnim=$isLoadAnim")
        for (activity in getActivityStack()) {
            if (activity.javaClass != cls) {
                finishActivity(activity, isLoadAnim)
            }
        }
    }


    fun finishActivities(clsArr: Array<Class<out Activity>>, isLoadAnim: Boolean) {
        Logger.d(TAG, "finishActivities $clsArr  isLoadAnim=$isLoadAnim")
        for (activity in getActivityStack()) {
            if (!isBeContained(clsArr, activity.javaClass)) {
                finishActivity(activity, isLoadAnim)
            }
        }
    }

    fun finishAllActivitiesExceptNewest(isLoadAnim: Boolean) {
        Logger.d(TAG, "finishAllActivitiesExceptNewest  isLoadAnim=$isLoadAnim")
        val activityStack = getActivityStack()
        for (index in 1 until activityStack.size) {
            finishActivity(activityStack[index], isLoadAnim)
        }
    }

    fun finishAllActivitiesExceptNewest(@AnimRes enterAnim: Int, @AnimRes exitAnim: Int) {
        Logger.d(TAG, "finishAllActivitiesExceptNewest enterAnim:$enterAnim  exitAnim:$exitAnim")
        val activityStack = getActivityStack()
        for (index in 1 until activityStack.size) {
            finishActivity(activityStack[index], enterAnim, exitAnim)
        }
    }


    fun isContainActivity(activity: Activity): Boolean {
        for (itemActivity in getActivityStack()) {
            if (itemActivity == activity) {
                return true
            }
        }
        return false
    }

    fun isContainActivity(cls: Class<out Activity>): Boolean {
        for (itemActivity in getActivityStack()) {
            if (itemActivity.javaClass == cls) {
                return true
            }
        }
        return false
    }

    fun finishAllActivities(isLoadAnim: Boolean) {
        Logger.d(TAG, "finishAllActivities isLoadAnim:$isLoadAnim")
        for (activity in getActivityStack()) {
            activity.finish()
            if (!isLoadAnim) {
                activity.overridePendingTransition(0, 0)
            }
        }
    }


    @SuppressLint("NewApi")
    fun finishAllActivities(
        @AnimRes enterAnim: Int,
        @AnimRes exitAnim: Int
    ) {
        Logger.d(TAG, "finishAllActivities enterAnim:$enterAnim  exitAnim:$exitAnim")
        for (activity in getActivityStack()) {
            activity.finish()
            activity.overridePendingTransition(enterAnim, exitAnim)
        }
    }


    fun getActivityId(activity: Activity): Int {
        return System.identityHashCode(activity)
    }

    fun finishToActivity(cls: Class<out Activity>, isIncludeSelf: Boolean): Boolean {
        return finishToActivity(cls, isIncludeSelf, false)
    }

    fun finishToActivity(
        cls: Class<out Activity>,
        isIncludeSelf: Boolean,
        isLoadAnim: Boolean
    ): Boolean {
        if (!isContainActivity(cls)) {
            return false
        }
        Logger.d(TAG, "finishToActivity $cls isIncludeSelf=$isIncludeSelf isLoadAnim=$isLoadAnim")
        for (activity in getActivityStack()) {
            if (activity.javaClass != cls) {
                finishActivity(activity, isLoadAnim)
            } else if (!isIncludeSelf) {
                return true
            } else {
                finishActivity(activity, isLoadAnim)
                return true
            }
        }
        return false
    }

    fun finishToActivity(
        cls: Class<out Activity>,
        isIncludeSelf: Boolean,
        @AnimRes enterAnim: Int,
        @AnimRes exitAnim: Int
    ): Boolean {
        if (!isContainActivity(cls)) {
            return false
        }
        Logger.d(
            TAG,
            "finishToActivity $cls isIncludeSelf=$isIncludeSelf enterAnim=$enterAnim exitAnim$exitAnim"
        )
        for (activity in getActivityStack()) {
            if (activity.javaClass != cls) {
                finishActivity(activity, enterAnim, exitAnim)
            } else if (!isIncludeSelf) {
                return true
            } else {
                finishActivity(activity, enterAnim, exitAnim)
                return true
            }
        }
        return false
    }

    fun finishToActivity(activity: Activity, isIncludeSelf: Boolean): Boolean {
        return finishToActivity(activity, isIncludeSelf, false)
    }

    fun finishToActivity(activity: Activity, isIncludeSelf: Boolean, isLoadAnim: Boolean): Boolean {
        if (!isContainActivity(activity)) {
            return false
        }
        Logger.d(
            TAG,
            "finishToActivity ${activity.hashCode()} isIncludeSelf=$isIncludeSelf isLoadAnim=$isLoadAnim"
        )
        for (itemActivity in getActivityStack()) {
            if (itemActivity != activity) {
                finishActivity(itemActivity, isLoadAnim)
            } else if (!isIncludeSelf) {
                return true
            } else {
                finishActivity(itemActivity, isLoadAnim)
                return true
            }
        }
        return false
    }

    fun finishToActivity(
        activity: Activity,
        isIncludeSelf: Boolean,
        @AnimRes enterAnim: Int,
        @AnimRes exitAnim: Int
    ): Boolean {
        if (!isContainActivity(activity)) {
            return false
        }
        Logger.d(
            TAG,
            "finishToActivity " + activity.hashCode() + " isIncludeSelf=" + isIncludeSelf + " enterAnim=" + enterAnim + " exitAnim=" + exitAnim
        )
        for (activity2 in getActivityStack()) {
            if (activity2 != activity) {
                finishActivity(activity2, enterAnim, exitAnim)
            } else if (!isIncludeSelf) {
                return true
            } else {
                finishActivity(activity2, enterAnim, exitAnim)
                return true
            }
        }
        return false
    }

    fun getActivityByActivityID(activityId: Int): Activity? {
        if (activityId == 0) {
            return null
        }
        for (activity in getActivityStack()) {
            if (getActivityId(activity) == activityId) {
                return activity
            }
        }
        return null
    }

    fun getActivityByName(str: String?): Activity? {
        if (TextUtils.isEmpty(str)) {
            return null
        }
        val it: Iterator<Activity> = getActivityStack().iterator()
        while (it.hasNext()) {
            val next = it.next()
            if (next.javaClass.name == str) {
                return next
            }
        }
        return null
    }

    fun getActivityBySimpleName(str: String?): Activity? {
        if (TextUtils.isEmpty(str)) {
            return null
        }
        val it: Iterator<Activity> = getActivityStack().iterator()
        while (it.hasNext()) {
            val next = it.next()
            if (next.javaClass.simpleName == str) {
                return next
            }
        }
        return null
    }

    fun getTopActivity(): Activity? {
        for (activity in getActivityStack()) {
            if (ActivityUtils.isActivityAlive(activity)) {
                return activity
            }
        }
        return null
    }

    fun isBackground(): Boolean {
        return this.mIsBackground
    }


    fun isEmpty(): Boolean {
        return getActivityStack().isEmpty()
    }

    @Synchronized
    fun getActivityStack(): LinkedList<Activity> {
        return LinkedList(mActivityStack)
    }

    fun size(): Int {
        return mActivityStack.size
    }

    @Synchronized
    fun init(application: Application?) {
        when {
            application == null -> {
                Log.e(TAG, "app is null", null)
            }
            sApp == null -> {
                sApp = application
                initReally(sApp)
            }
            sApp?.equals(application) != true -> {
                unInit()
                sApp = application
                initReally(sApp)
            }
        }
    }

    @Synchronized
    fun unInit() {
        mActivityStack.clear()
        if (sApp != null) {
            sApp?.unregisterActivityLifecycleCallbacks(this)
            sApp = null
            Logger.d(TAG, "unInit")
        }
    }

    private fun initReally(application: Application?) {
        mActivityStack.clear()
        if (application != null) {
            application.registerActivityLifecycleCallbacks(this)
            for (activity in ActivityUtils.getActivitiesByReflect()) {
                if ((activity !is IBaseActivityStack || (activity as IBaseActivityStack).isCanPutIntoStack()) && !activity.isFinishing) {
                    mActivityStack.addLast(activity)
                }
            }
        }
        Logger.d(TAG, "init activity stack, size=" + size())
    }

    private fun findIntervalActivityIndex(
        list: List<Activity>,
        numArr: Array<Int>,
        cls: Class<out Activity>,
        num: Int
    ): Boolean {
        if (list.isEmpty()) {
            return false
        }
        var activityIndex = 0
        var activityCount = 0
        while (true) {
            if (activityIndex >= list.size) {
                break
            }
            val activity = list[activityIndex]
            if (activity.javaClass == cls) {
                activityCount++
                if (activityCount == num) {
                    numArr[0] = activityIndex
                } else if (activityCount == num + 1) {
                    numArr[1] = activityIndex
                    break
                }
            }
            activityIndex++
        }
        return !(numArr[0] < 0 || numArr[1] <= 0)
    }

    fun finishIntervalActivities(
        cls: Class<out Activity>,
        @IntRange(from = 1) num: Int
    ) {
        finishIntervalActivities(cls, num, false)
    }

    fun finishIntervalActivities(
        cls: Class<out Activity>,
        @IntRange(from = 1) num: Int,
        @AnimRes enterAnim: Int,
        @AnimRes exitAnim: Int
    ) {
        Logger.d(
            TAG,
            "finishIntervalActivities $cls num=$num enterAnim=$enterAnim exitAnim:$exitAnim"
        )
        val activityStack: LinkedList<Activity> = getActivityStack()
        val numArr = arrayOf(-1, -1)
        if (findIntervalActivityIndex(activityStack, numArr, cls, num)) {
            val start = numArr[0]
            val end = numArr[1]
            for (index in start + 1..end) {
                val activity = activityStack[index]
                finishActivity(activity, enterAnim, exitAnim)
            }
        }
    }


    fun finishIntervalActivities(
        cls: Class<out Activity>,
        @IntRange(from = 1) num: Int,
        isLoadAnim: Boolean
    ) {
        Logger.d(TAG, "finishIntervalActivities $cls num=$num isLoadAnim=$isLoadAnim")
        val activityStack: LinkedList<Activity> = getActivityStack()
        val numArr = arrayOf(-1, -1)
        if (findIntervalActivityIndex(activityStack, numArr, cls, num)) {
            val start = numArr[0]
            val end = numArr[1]
            for (index in start + 1..end) {
                val activity = activityStack[index]
                finishActivity(activity, isLoadAnim)
            }
        }
    }

    fun finishActivity(activity: Activity, isLoadAnim: Boolean) {
        Logger.d(TAG, "finishActivity " + activity.hashCode() + " isLoadAnim=" + isLoadAnim)
        activity.finish()
        if (!isLoadAnim) {
            activity.overridePendingTransition(0, 0)
        }
    }

    fun finishActivity(cls: Class<out Activity>) {
        finishActivity(cls, false)
    }

    fun finishActivity(cls: Class<out Activity>, isLoadAnim: Boolean) {
        Logger.d(TAG, "finishActivity $cls isLoadAnim=$isLoadAnim")
        for (activity in getActivityStack()) {
            if (activity.javaClass == cls) {
                activity.finish()
                activity.overridePendingTransition(0, 0)
            }
        }
    }

    fun finishActivity(
        activity: Activity,
        @AnimRes enterAnim: Int,
        @AnimRes exitAnim: Int
    ) {
        Logger.d(
            TAG,
            "finishActivity " + activity.hashCode() + " enterAnim=$enterAnim exitAnim:$exitAnim"
        )
        activity.finish()
        activity.overridePendingTransition(enterAnim, exitAnim)
    }

    fun finishActivity(cls: Class<out Activity>, @AnimRes enterAnim: Int, @AnimRes exitAnim: Int) {
        Logger.d(TAG, "finishActivity $cls enterAnim=$enterAnim exitAnim=$exitAnim")
        for (activity in getActivityStack()) {
            if (activity.javaClass == cls) {
                activity.finish()
                activity.overridePendingTransition(enterAnim, exitAnim)
            }
        }
    }

    @Synchronized
    fun putTopActivity(activity: Activity) {
        if (activity !is IBaseActivityStack || (activity as? IBaseActivityStack)?.isCanPutIntoStack() == true) {
            if (!mActivityStack.contains(activity)) {
                mActivityStack.addFirst(activity)
                Logger.d(TAG, "putTopActivity $activity")
                notifyAdded(activity)
            } else if (mActivityStack.first != activity) {
                mActivityStack.remove(activity)
                mActivityStack.addFirst(activity)
                Logger.d(TAG, "putTopActivity $activity")
                notifyAdded(activity)
            }
        }
    }

    @Synchronized
    fun removeActivity(activity: Activity) {
        mActivityStack.remove(activity)
        notifyRemoved(activity)
    }

    private fun notifyAdded(activity: Activity) {
        mStackListenerMgr.startNotify(object :
            WeakListenerMgr.INotifyCallback<IBaseActivityStackChangeListener> {
            override fun onNotify(listener: IBaseActivityStackChangeListener) {
                listener.onActivityAdded(activity)
            }
        })
    }

    private fun notifyRemoved(activity: Activity) {
        mStackListenerMgr.startNotify(object :
            WeakListenerMgr.INotifyCallback<IBaseActivityStackChangeListener> {
            override fun onNotify(listener: IBaseActivityStackChangeListener) {
                listener.onActivityRemoved(activity)
            }
        })
    }

    private fun isDontCheckAppStatus(activity: Activity): Boolean {
        return activity is IBaseActivityStack && !(activity as IBaseActivityStack).isCheckAppStatus()
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        Logger.d(TAG, "onActivityCreated $activity")
        putTopActivity(activity)
    }

    override fun onActivityStarted(activity: Activity) {
        Logger.d(TAG, "onActivityStarted $activity mIsBackground=$mIsBackground")
        if (!isDontCheckAppStatus(activity)) {
            if (!mIsBackground) {
                putTopActivity(activity)
            }
            val i2 = mConfigCount
            if (i2 < 0) {
                mConfigCount = i2 + 1
            } else {
                mForegroundCount++
            }
        }
    }

    override fun onActivityResumed(activity: Activity) {
        Logger.d(TAG, "onActivityResumed $activity mIsBackground=$mIsBackground")
        putTopActivity(activity)
        if (mIsBackground && !isDontCheckAppStatus(activity)) {
            mIsBackground = false
            mOnAppStatusChangeListener?.onForeground()
        }
    }

    override fun onActivityPaused(activity: Activity) {
    }

    override fun onActivityStopped(activity: Activity) {
        if (!isDontCheckAppStatus(activity)) {
            if (activity.isChangingConfigurations) {
                mConfigCount--
                return
            }
            mForegroundCount--
            if (mForegroundCount <= 0) {
                mIsBackground = true
                mOnAppStatusChangeListener?.onBackground()
            }
        }
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
    }

    override fun onActivityDestroyed(activity: Activity) {
        Logger.d(TAG, "onActivityDestroyed, remove $activity")
        removeActivity(activity)
    }

    companion object {
        val TAG: String = ActivityStackImpl::class.java.simpleName
        val INSTANCE = ActivityStackImpl()
        var sApp: Application? = null
    }
}