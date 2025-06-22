package com.kernelflux.uixkit.adapter

import android.view.View

/**
 * [LoadMoreFooter] defines the behaviors that a load-more footer component must implement.
 */
interface LoadMoreFooter {

    fun getView(): View

    /**
     * Called when the user starts pulling up.
     * @param percent The current pull distance as a percentage of the trigger threshold (0.0 - 1.0+).
     * @param offset The actual pull distance in pixels (negative value).
     * @param triggerOffset The pixel distance threshold required to trigger loading.
     * @param state The current [RefreshState.State] (e.g., [RefreshState.STATE_PULL_UP_TO_LOAD]).
     */
    fun onPulling(percent: Float, offset: Int, triggerOffset: Int, @RefreshState.State state: Int)

    /**
     * Called when the pull distance has reached the trigger threshold but the finger hasn't been released.
     * Indicates that releasing will trigger a load.
     */
    fun onReleaseToLoad()

    /**
     * Called when the load operation is in progress.
     * The component should display a loading animation.
     */
    fun onLoading()

    /**
     * Called when the load operation is complete.
     * Should restore the component to its initial state.
     */
    fun onFinish()

    /**
     * Called when the component enters an idle state (neither refreshing nor loading).
     * Used to reset the UI to its initial state.
     */
    fun onReset()
}