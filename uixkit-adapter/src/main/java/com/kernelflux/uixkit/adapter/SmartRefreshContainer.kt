package com.kernelflux.uixkit.adapter

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min


/**
 * [SmartRefreshContainer] is a robust container for pull-to-refresh and load-more functionality.
 * It uses a simplified gesture handling approach to avoid nested scrolling conflicts.
 *
 * Core Features:
 * - Simple touch event handling to avoid nested scrolling conflicts
 * - Smooth animations with proper state management
 * - Customizable Header and Footer views
 * - Automatic refresh and load more functionality
 */
class SmartRefreshContainer @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
) : FrameLayout(context, attrs, defStyleAttr) {

    companion object {
        const val TAG = "SmartRefreshContainer"
    }

    private var recyclerView: RecyclerView? = null
    var refreshHeader: RefreshHeader? = null
        private set
    var loadMoreFooter: LoadMoreFooter? = null
        private set

    @RefreshState.State
    var currentState: Int = RefreshState.STATE_IDLE
        private set
    private var lastState = RefreshState.STATE_IDLE

    private var onRefreshListener: (() -> Unit)? = null
    private var onLoadMoreListener: (() -> Unit)? = null

    // For rebound animation
    private var scrollAnimator: ValueAnimator? = null

    var refreshTriggerOffset = dp2px(80)
    var loadMoreTriggerOffset = dp2px(80)
    var maxPullDownOffset = dp2px(150)
    var maxPullUpOffset = dp2px(150)
    var dampingFactor = 0.15f // 降低阻尼系数，让阻尼更明显
        private set

    // 动画时间配置
    var animationDuration = 300 // 默认动画时间，可配置
    var refreshAnimationDuration = 250 // 刷新动画时间，可配置
    var loadMoreAnimationDuration = 250 // 加载更多动画时间，可配置
        private set

    var currentOffset = 0
        private set
    private var lastTouchY = 0f
    private var isDragging = false
    private var initialTouchY = 0f
    private var touchSlop = 10f // 触摸滑动阈值


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


    private fun setRecyclerView(rv: RecyclerView) {
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

        rv.itemAnimator = null

        if (rv.parent != this) {
            addView(rv, 0)
        }

        if (rv.layoutManager !is LinearLayoutManager &&
            rv.layoutManager !is RecyclerView.LayoutManager
        ) {
            logDebug("RecyclerView's LayoutManager is not LinearLayoutManager. Pull/LoadMore might not behave as expected with custom LayoutManagers.")
        }

    }


    fun addRefreshHeader(header: RefreshHeader) {
        refreshHeader?.let { removeView(it.getView()) }
        refreshHeader = header
        val lp = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        lp.gravity = Gravity.TOP
        addView(header.getView(), lp)
        // 修复初始位置：Header 应该在容器外部
        header.getView().post {
            header.getView().translationY = -header.getView().height.toFloat()
        }
    }


    fun addLoadMoreFooter(footer: LoadMoreFooter) {
        loadMoreFooter?.let { removeView(it.getView()) }
        loadMoreFooter = footer
        val lp = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        lp.gravity = Gravity.BOTTOM
        addView(footer.getView(), lp)
        // 修复初始位置：Footer 应该在容器外部
        footer.getView().post {
            footer.getView().translationY = footer.getView().height.toFloat()
        }
    }


    fun setOnRefreshListener(listener: () -> Unit) {
        onRefreshListener = listener
    }


    fun setOnLoadMoreListener(listener: () -> Unit) {
        onLoadMoreListener = listener
    }

    /**
     * Sets the adapter for the internal RecyclerView.
     * This is a convenient method to avoid manually getting the RecyclerView.
     *
     * @param adapter The adapter to set for the RecyclerView.
     */
    fun setAdapter(adapter: RecyclerView.Adapter<*>) {
        recyclerView?.adapter = adapter
    }

    /**
     * Gets the internal RecyclerView for advanced customization.
     * Use this method only when you need direct access to the RecyclerView.
     *
     * @return The internal RecyclerView instance.
     */
    fun getRecyclerView(): RecyclerView? {
        return recyclerView
    }

    /**
     * Sets the LayoutManager for the internal RecyclerView.
     *
     * @param layoutManager The LayoutManager to set.
     */
    fun setLayoutManager(layoutManager: RecyclerView.LayoutManager) {
        recyclerView?.layoutManager = layoutManager
    }

    /**
     * Adds an ItemDecoration to the internal RecyclerView.
     *
     * @param decoration The ItemDecoration to add.
     */
    fun addItemDecoration(decoration: RecyclerView.ItemDecoration) {
        recyclerView?.addItemDecoration(decoration)
    }

    /**
     * 自动添加默认的刷新和加载更多视图
     */
    fun setupDefaultHeaders() {
        if (refreshHeader == null) {
            addRefreshHeader(DefaultRefreshHeader(context))
        }
        if (loadMoreFooter == null) {
            addLoadMoreFooter(DefaultLoadMoreFooter(context))
        }
    }

    /**
     * 自动刷新（程序触发下拉刷新）
     */
    fun autoRefresh() {
        if (currentState == RefreshState.STATE_IDLE) {
            setState(RefreshState.STATE_REFRESHING)
            refreshHeader?.onRefreshing()
            animateToOffset(refreshTriggerOffset)
            onRefreshListener?.invoke()
        }
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
            RefreshState.STATE_PULL_DOWN_TO_REFRESH -> {
                refreshHeader?.onPulling(
                    abs(currentOffset).toFloat() / refreshTriggerOffset,
                    currentOffset,
                    refreshTriggerOffset,
                    currentState
                )
            }

            RefreshState.STATE_RELEASE_TO_REFRESH -> {
                refreshHeader?.onReleaseToRefresh()
            }

            RefreshState.STATE_REFRESHING -> {
                refreshHeader?.onRefreshing()
            }

            RefreshState.STATE_PULL_UP_TO_LOAD -> {
                loadMoreFooter?.onPulling(
                    abs(currentOffset).toFloat() / loadMoreTriggerOffset,
                    currentOffset,
                    loadMoreTriggerOffset,
                    currentState
                )
            }

            RefreshState.STATE_RELEASE_TO_LOAD -> {
                loadMoreFooter?.onReleaseToLoad()
            }

            RefreshState.STATE_LOADING -> {
                loadMoreFooter?.onLoading()
            }

            RefreshState.STATE_REFRESH_FINISH -> {
                refreshHeader?.onFinish()
            }

            RefreshState.STATE_LOAD_FINISH -> {
                loadMoreFooter?.onFinish()
            }

            RefreshState.STATE_IDLE -> {
                refreshHeader?.onReset()
                loadMoreFooter?.onReset()
            }
        }
    }


    private fun animateToOffset(targetOffset: Int) {
        scrollAnimator?.cancel()

        // 根据目标偏移量选择不同的动画时间
        val duration = when {
            targetOffset == 0 -> animationDuration // 回弹到0
            targetOffset > 0 -> refreshAnimationDuration // 刷新动画
            else -> loadMoreAnimationDuration // 加载更多动画
        }

        scrollAnimator = ValueAnimator.ofInt(currentOffset, targetOffset).apply {
            this.duration = duration.toLong()
            interpolator = DecelerateInterpolator(1.5f) // 调整插值器，让动画更流畅
            addUpdateListener { animator ->
                val animatedValue = animator.animatedValue as Int
                setContentViewTranslation(animatedValue)
            }

            val currentFinalState = currentState
            addListener(object : android.animation.AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: android.animation.Animator) {
                    currentOffset = targetOffset
                    when (currentFinalState) {
                        RefreshState.STATE_REFRESH_FINISH, RefreshState.STATE_LOAD_FINISH -> {
                            setState(RefreshState.STATE_IDLE)
                        }
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

        // 修复 Header 和 Footer 的位置计算
        refreshHeader?.getView()?.let { headerView ->
            headerView.translationY = (offset - headerView.height).toFloat()
        }
        loadMoreFooter?.getView()?.let { footerView ->
            footerView.translationY = (offset + footerView.height).toFloat()
        }

        // 添加调试信息
        if (BuildConfig.DEBUG) {
            logDebug("Translation: offset=$offset, state=$currentState")
        }
    }

    private fun dp2px(dp: Int): Int {
        return (dp * resources.displayMetrics.density + 0.5f).toInt()
    }

    private fun logDebug(message: String) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, message)
        }
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        ev?.let { event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    lastTouchY = event.y
                    initialTouchY = event.y
                    isDragging = false
                    scrollAnimator?.cancel()
                }

                MotionEvent.ACTION_MOVE -> {
                    val deltaY = event.y - lastTouchY

                    // 检查是否可以滚动
                    val canScrollUp = recyclerView?.canScrollVertically(1) ?: false
                    val canScrollDown = recyclerView?.canScrollVertically(-1) ?: false

                    // 判断是否需要拦截触摸事件
                    if (abs(deltaY) > touchSlop) {
                        // 下拉刷新：向下滑动且不能继续向下滚动
                        if (deltaY > 0 && !canScrollDown) {
                            isDragging = true
                            return true
                        }
                        // 上拉加载：向上滑动且不能继续向上滚动
                        if (deltaY < 0 && !canScrollUp) {
                            isDragging = true
                            return true
                        }
                    }
                }
            }
        }
        return super.onInterceptTouchEvent(ev)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.let { ev ->
            when (ev.action) {
                MotionEvent.ACTION_MOVE -> {
                    if (isDragging) {
                        val deltaY = ev.y - lastTouchY
                        handleTouchScroll(deltaY)
                        lastTouchY = ev.y
                        return true
                    }
                }

                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    if (isDragging) {
                        handleTouchEnd()
                        isDragging = false
                        return true
                    }
                }
            }
        }
        return super.onTouchEvent(event)
    }

    private fun handleTouchScroll(deltaY: Float) {
        // 修复方向计算：下拉时 deltaY > 0，应该增加 offset
        val newOffset = currentOffset + deltaY.toInt()

        // 添加阻尼效果
        val dampedOffset = applyDamping(newOffset)

        logDebug("Touch scroll: deltaY=$deltaY, currentOffset=$currentOffset, newOffset=$newOffset, dampedOffset=$dampedOffset")

        setContentViewTranslation(dampedOffset)

        // 更新状态
        updateState(dampedOffset)
    }

    /**
     * 应用阻尼效果
     * 当偏移量超过触发阈值时，减少移动速度，创造阻尼感
     */
    private fun applyDamping(offset: Int): Int {
        return when {
            offset > 0 -> {
                // 下拉刷新阻尼
                if (offset > refreshTriggerOffset) {
                    val overOffset = offset - refreshTriggerOffset
                    // 使用非线性阻尼，让阻尼效果更自然
                    val dampedOverOffset =
                        (overOffset * calculateDampingFactor(overOffset.toFloat())).toInt()
                    val finalOffset = refreshTriggerOffset + dampedOverOffset
                    // 限制最大偏移量
                    min(finalOffset, maxPullDownOffset)
                } else {
                    offset
                }
            }

            offset < 0 -> {
                // 上拉加载阻尼
                if (abs(offset) > loadMoreTriggerOffset) {
                    val overOffset = abs(offset) - loadMoreTriggerOffset
                    // 使用非线性阻尼，让阻尼效果更自然
                    val dampedOverOffset =
                        (overOffset * calculateDampingFactor(overOffset.toFloat())).toInt()
                    val finalOffset = -(loadMoreTriggerOffset + dampedOverOffset)
                    // 限制最大偏移量
                    max(finalOffset, -maxPullUpOffset)
                } else {
                    offset
                }
            }

            else -> offset
        }
    }

    /**
     * 计算非线性阻尼系数
     * 偏移量越大，阻尼越强
     */
    private fun calculateDampingFactor(overOffset: Float): Float {
        val normalizedOffset = overOffset / 50f // 降低归一化分母，让阻尼更早生效
        val damping = dampingFactor * (1f - normalizedOffset * 0.8f) // 增加阻尼强度
        return damping.coerceAtLeast(0.05f) // 降低最小阻尼系数
    }

    private fun updateState(offset: Int) {
        val newState = when {
            offset > 0 -> {
                if (offset >= refreshTriggerOffset) RefreshState.STATE_RELEASE_TO_REFRESH
                else RefreshState.STATE_PULL_DOWN_TO_REFRESH
            }

            offset < 0 -> {
                if (abs(offset) >= loadMoreTriggerOffset) RefreshState.STATE_RELEASE_TO_LOAD
                else RefreshState.STATE_PULL_UP_TO_LOAD
            }

            else -> RefreshState.STATE_IDLE
        }

        logDebug("Update state: offset=$offset, currentState=$currentState, newState=$newState")

        if (newState != currentState) {
            setState(newState)
        }

        // 即使状态没变，也要更新 Header/Footer 的 pulling 状态
        updatePullingState(offset)
    }

    /**
     * 更新 pulling 状态，让视觉反馈更细腻
     */
    private fun updatePullingState(offset: Int) {
        when {
            offset > 0 && refreshHeader != null -> {
                val percent = min(1f, offset.toFloat() / refreshTriggerOffset)
                refreshHeader?.onPulling(percent, offset, refreshTriggerOffset, currentState)
            }

            offset < 0 && loadMoreFooter != null -> {
                val percent = min(1f, abs(offset).toFloat() / loadMoreTriggerOffset)
                loadMoreFooter?.onPulling(percent, offset, loadMoreTriggerOffset, currentState)
            }
        }
    }

    private fun handleTouchEnd() {
        logDebug("Touch end: currentState=$currentState, currentOffset=$currentOffset")

        when (currentState) {
            RefreshState.STATE_PULL_DOWN_TO_REFRESH -> {
                logDebug("Pulling down, animate to 0")
                animateToOffset(0)
            }

            RefreshState.STATE_RELEASE_TO_REFRESH -> {
                logDebug("Release to refresh, start refreshing")
                setState(RefreshState.STATE_REFRESHING)
                refreshHeader?.onRefreshing()
                animateToOffset(refreshTriggerOffset)
                onRefreshListener?.invoke()
            }

            RefreshState.STATE_PULL_UP_TO_LOAD -> {
                logDebug("Pulling up, animate to 0")
                animateToOffset(0)
            }

            RefreshState.STATE_RELEASE_TO_LOAD -> {
                logDebug("Release to load, start loading")
                setState(RefreshState.STATE_LOADING)
                loadMoreFooter?.onLoading()
                animateToOffset(-loadMoreTriggerOffset)
                onLoadMoreListener?.invoke()
            }
        }
    }

    /**
     * 配置阻尼效果
     * @param factor 阻尼系数，范围0.1-1.0，越小阻尼越强
     */
    fun setDampingFactor(factor: Float) {
        dampingFactor = factor.coerceIn(0.1f, 1.0f)
    }

    /**
     * 配置触发偏移量
     * @param refreshOffset 下拉刷新触发偏移量
     * @param loadMoreOffset 上拉加载触发偏移量
     */
    fun setTriggerOffsets(refreshOffset: Int, loadMoreOffset: Int) {
        refreshTriggerOffset = refreshOffset
        loadMoreTriggerOffset = loadMoreOffset
    }

    /**
     * 配置最大偏移量
     * @param maxPullDown 最大下拉偏移量
     * @param maxPullUp 最大上拉偏移量
     */
    fun setMaxOffsets(maxPullDown: Int, maxPullUp: Int) {
        maxPullDownOffset = maxPullDown
        maxPullUpOffset = maxPullUp
    }

    /**
     * 配置动画时间
     * @param defaultDuration 默认动画时间（毫秒）
     * @param refreshDuration 刷新动画时间（毫秒）
     * @param loadMoreDuration 加载更多动画时间（毫秒）
     */
    fun setAnimationDurations(
        defaultDuration: Long,
        refreshDuration: Long,
        loadMoreDuration: Long
    ) {
        animationDuration = defaultDuration.toInt()
        refreshAnimationDuration = refreshDuration.toInt()
        loadMoreAnimationDuration = loadMoreDuration.toInt()
    }

    /**
     * 快速配置动画时间（简化版）
     * @param duration 统一的动画时间（毫秒）
     */
    fun setAnimationDuration(duration: Long) {
        animationDuration = duration.toInt()
        refreshAnimationDuration = duration.toInt()
        loadMoreAnimationDuration = duration.toInt()
    }
}


fun SmartRefreshContainer.onRefresh(action: () -> Unit) {
    this.setOnRefreshListener(action)
}

fun SmartRefreshContainer.onLoadMore(action: () -> Unit) {
    this.setOnLoadMoreListener(action)
}

/**
 * DSL-style extension function to configure SmartRefreshContainer with adapter and layout manager.
 *
 * @param T The type of the data model.
 * @param adapter The SmartAdapter instance.
 * @param layoutManager The LayoutManager to use (defaults to LinearLayoutManager).
 * @param block Lambda to configure the container.
 * @return The SmartRefreshContainer instance for method chaining.
 */
inline fun <T : Any> SmartRefreshContainer.configure(
    adapter: SmartAdapter<T>,
    layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context),
    crossinline block: SmartRefreshContainer.() -> Unit = {}
): SmartRefreshContainer {
    this.setLayoutManager(layoutManager)
    this.setAdapter(adapter)
    this.block()
    return this
}

/**
 * DSL-style extension function to set up refresh and load more listeners.
 *
 * @param onRefresh Lambda to handle refresh action.
 * @param onLoadMore Lambda to handle load more action.
 * @return The SmartRefreshContainer instance for method chaining.
 */
fun SmartRefreshContainer.setupListeners(
    onRefresh: () -> Unit = {},
    onLoadMore: () -> Unit = {}
): SmartRefreshContainer {
    this.setOnRefreshListener(onRefresh)
    this.setOnLoadMoreListener(onLoadMore)
    return this
}

fun Int.dpToPx(context: Context): Int {
    return (this * context.resources.displayMetrics.density + 0.5f).toInt()
}
