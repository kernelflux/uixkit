package com.kernelflux.uixkit.ui.recyclerNav


class NavMoveToFocusTask(
    private val recyclerNav: RecyclerNav,
    private val smoothScroll: Boolean,
    private val navItemFocusItemOffsetListener: RecyclerNav.NavFocusItemOffsetListener?
) : Runnable {

    override fun run() {
        recyclerNav.moveToFocus(
            smoothScroll,
            navItemFocusItemOffsetListener
        )
    }
}