package com.kernelflux.uixkit.adapter

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import androidx.core.view.NestedScrollingParent2
import androidx.core.view.NestedScrollingParentHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min


/**
 * [SmartRefreshContainer] is a robust container for pull-to-refresh and load-more functionality.
 * It leverages the [NestedScrollingParent2] mechanism to implement refresh and load actions, and supports customizable Header and Footer views.
 *
 * Core Improvements:
 * - More precise management of refresh/load states.
 * - Enhanced interception and handling of nested scroll events.
 * - Simple rebound animations.
 * - Coordinated animations and state updates between the Header/Footer and the container.
 */
class SmartRefreshContainer @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
) : FrameLayout(context, attrs, defStyleAttr), NestedScrollingParent2 {

    private val TAG = "SmartRefreshContainer"

    private var recyclerView: RecyclerView? = null
    private var refreshHeader: RefreshHeader? = null
    private var loadMoreFooter: LoadMoreFooter? = null

    @RefreshState.State
    private var currentState: Int = RefreshState.STATE_IDLE
    private var lastState: Int = RefreshState.STATE_IDLE

    private var onRefreshListener: (() -> Unit)? = null
    private var onLoadMoreListener: (() -> Unit)? = null

    private val nestedScrollingParentHelper = NestedScrollingParentHelper(this)

    // For rebound animation
    private var scrollAnimator: ValueAnimator? = null

    var refreshTriggerOffset = dp2px(80)
    var loadMoreTriggerOffset = dp2px(80)
    var maxPullDownOffset = dp2px(160)
    var maxPullUpOffset = dp2px(160)

    private var currentOffset = 0

    init {
        // addRefreshHeader(DefaultRefreshHeader(context)) // 示例
        // addLoadMoreFooter(DefaultLoadMoreFooter(context)) // 示例
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        if (recyclerView == null) {
            for (i in 0 until childCount) {
                val child = getChildAt(i)
                if (child is RecyclerView) {
                    setRecyclerView(child)
                    break
                }
            }
        }
        if (recyclerView == null) {
            logDebug("No RecyclerView found in SmartRefreshContainer children. Please call setRecyclerView() or ensure it's in XML.")
        }
    }


    fun setRecyclerView(rv: RecyclerView) {
        if (recyclerView != null && recyclerView != rv) {
            removeView(recyclerView)
        }
        recyclerView = rv
        val lp = rv.layoutParams as? LayoutParams ?: LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.MATCH_PARENT
        )
        lp.width = LayoutParams.MATCH_PARENT
        lp.height = LayoutParams.MATCH_PARENT
        rv.layoutParams = lp


        if (rv.parent != this) {
            addView(rv, 0)
        }

        if (rv.layoutManager !is LinearLayoutManager && rv.layoutManager !is RecyclerView.LayoutManager) {
            logDebug("RecyclerView's LayoutManager is not LinearLayoutManager. Pull/LoadMore might not behave as expected with custom LayoutManagers.")
        }
    }


    fun addRefreshHeader(header: RefreshHeader) {
        refreshHeader?.let { removeView(it.getView()) }
        refreshHeader = header
        val lp = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        lp.gravity = Gravity.TOP
        addView(header.getView(), lp)
        header.getView().translationY = -header.getView().measuredHeight.toFloat()
    }


    fun addLoadMoreFooter(footer: LoadMoreFooter) {
        loadMoreFooter?.let { removeView(it.getView()) }
        loadMoreFooter = footer
        val lp = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        lp.gravity = Gravity.BOTTOM
        addView(footer.getView(), lp)
        footer.getView().translationY = footer.getView().measuredHeight.toFloat()
    }


    fun setOnRefreshListener(listener: () -> Unit) {
        onRefreshListener = listener
    }


    fun setOnLoadMoreListener(listener: () -> Unit) {
        onLoadMoreListener = listener
    }


    fun finishRefresh() {
        if (currentState == RefreshState.STATE_REFRESHING) {
            setState(RefreshState.STATE_REFRESH_FINISH)
            refreshHeader?.onFinish()
            animateToOffset(0)
        }
    }


    fun finishLoadMore() {
        if (currentState == RefreshState.STATE_LOADING) {
            setState(RefreshState.STATE_LOAD_FINISH)
            loadMoreFooter?.onFinish()
            animateToOffset(0)
        }
    }

    private fun setState(@RefreshState.State state: Int) {
        if (currentState == state) return
        lastState = currentState
        currentState = state
        logDebug("State changed from $lastState to $currentState")

        when (currentState) {
            RefreshState.STATE_PULL_DOWN_TO_REFRESH -> refreshHeader?.onPulling(
                abs(currentOffset).toFloat() / refreshTriggerOffset,
                currentOffset,
                refreshTriggerOffset,
                currentState
            )

            RefreshState.STATE_RELEASE_TO_REFRESH -> refreshHeader?.onReleaseToRefresh()
            RefreshState.STATE_REFRESHING -> refreshHeader?.onRefreshing()
            RefreshState.STATE_PULL_UP_TO_LOAD -> loadMoreFooter?.onPulling(
                abs(currentOffset).toFloat() / loadMoreTriggerOffset,
                currentOffset,
                loadMoreTriggerOffset,
                currentState
            )

            RefreshState.STATE_RELEASE_TO_LOAD -> loadMoreFooter?.onReleaseToLoad()
            RefreshState.STATE_LOADING -> loadMoreFooter?.onLoading()
            RefreshState.STATE_REFRESH_FINISH -> refreshHeader?.onFinish()
            RefreshState.STATE_LOAD_FINISH -> loadMoreFooter?.onFinish()
            RefreshState.STATE_IDLE -> {
                refreshHeader?.onReset()
                loadMoreFooter?.onReset()
            }
        }
    }


    private fun animateToOffset(targetOffset: Int) {
        scrollAnimator?.cancel()
        scrollAnimator = ValueAnimator.ofInt(currentOffset, targetOffset).apply {
            duration = 300
            interpolator = DecelerateInterpolator()
            addUpdateListener { animator ->
                val animatedValue = animator.animatedValue as Int
                setContentViewTranslation(animatedValue)
            }

            val currentFinalState = currentState
            addListener(object : android.animation.AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: android.animation.Animator) {
                    currentOffset = targetOffset
                    when (currentFinalState) {
                        RefreshState.STATE_REFRESH_FINISH, RefreshState.STATE_LOAD_FINISH -> setState(
                            RefreshState.STATE_IDLE
                        )
                    }
                }

                override fun onAnimationCancel(animation: android.animation.Animator) {
                    logDebug("Animation cancelled from state $currentFinalState. Current offset: $currentOffset")
                    if (currentFinalState == RefreshState.STATE_PULL_DOWN_TO_REFRESH || currentFinalState == RefreshState.STATE_PULL_UP_TO_LOAD) {
                        setState(RefreshState.STATE_IDLE)
                    }
                }
            })
            start()
        }
    }


    private fun setContentViewTranslation(offset: Int) {
        currentOffset = offset
        recyclerView?.translationY = offset.toFloat()
        refreshHeader?.getView()?.translationY =
            offset - (refreshHeader?.getView()?.measuredHeight?.toFloat() ?: 0f)
        loadMoreFooter?.getView()?.translationY =
            offset + (loadMoreFooter?.getView()?.measuredHeight?.toFloat() ?: 0f)

        if (offset > 0 && currentState !in setOf(
                RefreshState.STATE_REFRESHING,
                RefreshState.STATE_REFRESH_FINISH
            )
        ) {
            refreshHeader?.onPulling(
                min(1f, offset.toFloat() / refreshTriggerOffset),
                offset,
                refreshTriggerOffset,
                currentState
            )
        } else if (offset < 0 && currentState !in setOf(
                RefreshState.STATE_LOADING,
                RefreshState.STATE_LOAD_FINISH
            )
        ) {
            loadMoreFooter?.onPulling(
                min(1f, abs(offset).toFloat() / loadMoreTriggerOffset),
                offset,
                loadMoreTriggerOffset,
                currentState
            )
        }
    }

    // --- NestedScrollingParent2 Impl ---

    override fun onStartNestedScroll(
        child: View, target: View, axes: Int, type: Int,
    ): Boolean {
        val canStart = (axes and RecyclerView.SCROLL_AXIS_VERTICAL) != 0 &&
                (currentState == RefreshState.STATE_IDLE ||
                        currentState == RefreshState.STATE_PULL_DOWN_TO_REFRESH ||
                        currentState == RefreshState.STATE_RELEASE_TO_REFRESH ||
                        currentState == RefreshState.STATE_PULL_UP_TO_LOAD ||
                        currentState == RefreshState.STATE_RELEASE_TO_LOAD)

        return canStart
    }

    override fun onNestedScrollAccepted(child: View, target: View, axes: Int, type: Int) {
        nestedScrollingParentHelper.onNestedScrollAccepted(child, target, axes, type)
        scrollAnimator?.cancel()
    }


    override fun onNestedPreScroll(
        target: View, dx: Int, dy: Int, consumed: IntArray, type: Int,
    ) {
        if (currentOffset != 0) {
            val willScroll = currentOffset + dy
            if ((currentOffset > 0 && willScroll < 0) || (currentOffset < 0 && willScroll > 0)) {
                consumed[1] = -currentOffset
                setContentViewTranslation(0)
            } else {
                val maxAllowedOffset =
                    if (currentOffset > 0) maxPullDownOffset else -maxPullUpOffset
                val newOffset = min(maxPullDownOffset, max(-maxPullUpOffset, currentOffset + dy))
                consumed[1] = dy
                setContentViewTranslation(newOffset)
            }
            return
        }

        val canScrollDown = target.canScrollVertically(-1)
        val canScrollUp = target.canScrollVertically(1)

        if (dy > 0 && !canScrollUp) {
            if (currentState == RefreshState.STATE_IDLE || currentState == RefreshState.STATE_PULL_UP_TO_LOAD || currentState == RefreshState.STATE_RELEASE_TO_LOAD) {
                val newOffset = min(0, max(currentOffset - dy, -maxPullUpOffset))
                consumed[1] = dy
                setContentViewTranslation(newOffset)
                if (abs(newOffset) >= loadMoreTriggerOffset) {
                    setState(RefreshState.STATE_RELEASE_TO_LOAD)
                } else {
                    setState(RefreshState.STATE_PULL_UP_TO_LOAD)
                }
            }
        }
        else if (dy < 0 && !canScrollDown) {
            if (currentState == RefreshState.STATE_IDLE || currentState == RefreshState.STATE_PULL_DOWN_TO_REFRESH || currentState == RefreshState.STATE_RELEASE_TO_REFRESH) {
                val newOffset = max(0, min(currentOffset - dy, maxPullDownOffset))
                consumed[1] = dy
                setContentViewTranslation(newOffset)
                if (newOffset >= refreshTriggerOffset) {
                    setState(RefreshState.STATE_RELEASE_TO_REFRESH)
                } else {
                    setState(RefreshState.STATE_PULL_DOWN_TO_REFRESH)
                }
            }
        }
    }

    override fun onNestedScroll(
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int,
    ) {
        if (dyUnconsumed != 0) {
            val newOffset = currentOffset - dyUnconsumed
            val boundedOffset = when {
                newOffset > maxPullDownOffset -> maxPullDownOffset
                newOffset < -maxPullUpOffset -> -maxPullUpOffset
                else -> newOffset
            }
            setContentViewTranslation(boundedOffset)

            if (currentOffset > 0) {
                if (currentOffset >= refreshTriggerOffset && currentState == RefreshState.STATE_PULL_DOWN_TO_REFRESH) {
                    setState(RefreshState.STATE_RELEASE_TO_REFRESH)
                } else if (currentOffset < refreshTriggerOffset && currentState == RefreshState.STATE_RELEASE_TO_REFRESH) {
                    setState(RefreshState.STATE_PULL_DOWN_TO_REFRESH)
                }
            } else if (currentOffset < 0) {
                if (abs(currentOffset) >= loadMoreTriggerOffset && currentState == RefreshState.STATE_PULL_UP_TO_LOAD) {
                    setState(RefreshState.STATE_RELEASE_TO_LOAD)
                } else if (abs(currentOffset) < loadMoreTriggerOffset && currentState == RefreshState.STATE_RELEASE_TO_LOAD) {
                    setState(RefreshState.STATE_PULL_UP_TO_LOAD)
                }
            }
        }
    }

    override fun onStopNestedScroll(target: View, type: Int) {
        nestedScrollingParentHelper.onStopNestedScroll(target, type)

        scrollAnimator?.cancel()

        when (currentState) {
            RefreshState.STATE_PULL_DOWN_TO_REFRESH -> {
                animateToOffset(0)
            }

            RefreshState.STATE_RELEASE_TO_REFRESH -> {
                setState(RefreshState.STATE_REFRESHING)
                refreshHeader?.onRefreshing()
                animateToOffset(refreshTriggerOffset)
                onRefreshListener?.invoke()
            }

            RefreshState.STATE_PULL_UP_TO_LOAD -> {
                animateToOffset(0)
            }

            RefreshState.STATE_RELEASE_TO_LOAD -> {
                setState(RefreshState.STATE_LOADING)
                loadMoreFooter?.onLoading()
                animateToOffset(-loadMoreTriggerOffset)
                onLoadMoreListener?.invoke()
            }
        }
    }

    override fun onNestedPreFling(target: View, velocityX: Float, velocityY: Float): Boolean {
        if (currentOffset != 0) {
            return true
        }
        return currentState == RefreshState.STATE_REFRESHING || currentState == RefreshState.STATE_LOADING
    }

    override fun onNestedFling(
        target: View, velocityX: Float, velocityY: Float, consumed: Boolean,
    ): Boolean {
        return false
    }

    override fun getNestedScrollAxes(): Int {
        return nestedScrollingParentHelper.nestedScrollAxes
    }

    private fun dp2px(dp: Int): Int {
        return (dp * resources.displayMetrics.density + 0.5f).toInt()
    }

    private fun logDebug(message: String) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, message)
        }
    }
}


fun SmartRefreshContainer.onRefresh(action: () -> Unit) {
    this.setOnRefreshListener(action)
}

fun SmartRefreshContainer.onLoadMore(action: () -> Unit) {
    this.setOnLoadMoreListener(action)
}

fun Int.dpToPx(context: Context): Int {
    return (this * context.resources.displayMetrics.density + 0.5f).toInt()
}
