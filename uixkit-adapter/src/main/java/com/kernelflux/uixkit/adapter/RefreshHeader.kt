package com.kernelflux.uixkit.adapter

import android.view.View

/**
 * [RefreshHeader] defines the required behaviors for a pull-to-refresh component.
 */
interface RefreshHeader {
    fun getView(): View

    /**
     * Called when the user starts pulling down.
     * @param percent The current pull distance as a percentage of the trigger threshold (0.0 - 1.0+).
     * @param offset The actual pull distance in pixels.
     * @param triggerOffset The pixel distance required to trigger a refresh.
     * @param state The current [RefreshState.State]
     * (e.g., [RefreshState.STATE_PULL_DOWN_TO_REFRESH]).
     */
    fun onPulling(percent: Float, offset: Int, triggerOffset: Int, @RefreshState.State state: Int)

    /**
     * Called when the pull distance has reached the trigger threshold and the finger is still down.
     * Indicates that releasing will start a refresh.
     */
    fun onReleaseToRefresh()

    /**
     * Called when the refresh operation is in progress.
     * The component should show a loading animation.
     */
    fun onRefreshing()

    /**
     * Called when the refresh is complete and the component should return to its initial state.
     */
    fun onFinish()

    /**
     * Called when the component enters an idle state (not refreshing or loading).
     * Used to reset the UI to its original state.
     */
    fun onReset()
}