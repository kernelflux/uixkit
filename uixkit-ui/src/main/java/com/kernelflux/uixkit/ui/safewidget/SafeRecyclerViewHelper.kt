package com.kernelflux.uixkit.ui.safewidget

import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.kernelflux.ktoolbox.core.ReflectUtil
import java.lang.ref.WeakReference


class SafeRecyclerViewHelper(recyclerView: RecyclerView) : DefaultLifecycleObserver {
    private var mMainHandler: Handler? = null
    private var mRecyclerViewRef: WeakReference<RecyclerView>? = null
    private var mIsAttach = false

    init {
        mMainHandler = Handler(Looper.getMainLooper())
        mRecyclerViewRef = WeakReference(recyclerView)
    }

    private fun getRecyclerView(): RecyclerView? {
        return mRecyclerViewRef?.get()
    }

    fun onAttachedToWindow(recyclerView: RecyclerView) {
        val activity = SafeUtil.getFragmentActivity(recyclerView)
        if (activity != null && !activity.isFinishing && !activity.isDestroyed) {
            this.mIsAttach = true
            activity.lifecycle.addObserver(this)
        }
    }

    fun onDetachedFromWindow() {
        mIsAttach = false
    }


    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        mMainHandler?.postDelayed({
            val recyclerview = getRecyclerView()
            if (mIsAttach && recyclerview != null) {
                val parentView = recyclerview.parent
                if (parentView is ViewGroup) {
                    ReflectUtil.invokeDeclaredMethod(
                        ViewGroup::class.java,
                        "removeDetachedView",
                        parentView,
                        arrayOf(View::class.java, Boolean::class.java),
                        arrayOf(recyclerview, false)
                    )
                }
            }
            mMainHandler?.removeCallbacksAndMessages(null)
        }, 3000)
    }
}