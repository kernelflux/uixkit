package com.kernelflux.uixkit.adapter

import androidx.annotation.IntDef

/**
 * Encapsulates all refresh and load states used by SmartRefreshContainer.
 * External classes can directly access constants via RefreshState.STATE_XXX.
 */
object RefreshState {
    const val STATE_IDLE = 0                  // Idle state
    const val STATE_PULL_DOWN_TO_REFRESH = 1  // Pulling down (not yet reached threshold)
    const val STATE_RELEASE_TO_REFRESH = 2    // Threshold reached, release to refresh
    const val STATE_REFRESHING = 3            // Refreshing in progress
    const val STATE_PULL_UP_TO_LOAD = 4       // Pulling up (not yet reached threshold)
    const val STATE_RELEASE_TO_LOAD = 5       // Threshold reached, release to load
    const val STATE_LOADING = 6               // Loading in progress
    const val STATE_REFRESH_FINISH = 7        // Refresh finished, bouncing back
    const val STATE_LOAD_FINISH = 8           // Load finished, bouncing back

    /**
     * Defines all valid refresh/load states.
     * Used for static code analysis to ensure only legal values are used.
     */
    @IntDef(
        STATE_IDLE, STATE_PULL_DOWN_TO_REFRESH, STATE_RELEASE_TO_REFRESH, STATE_REFRESHING,
        STATE_PULL_UP_TO_LOAD, STATE_RELEASE_TO_LOAD, STATE_LOADING,
        STATE_REFRESH_FINISH, STATE_LOAD_FINISH
    )
    @Retention(AnnotationRetention.SOURCE)
    annotation class State
}