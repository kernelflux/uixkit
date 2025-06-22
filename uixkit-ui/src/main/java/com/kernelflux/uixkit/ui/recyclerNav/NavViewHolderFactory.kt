package com.kernelflux.uixkit.ui.recyclerNav

import android.view.ViewGroup
import com.kernelflux.uixkit.ui.recyclerNav.NavViewHolder

abstract class NavViewHolderFactory {

    abstract fun createViewHolder(viewGroup: ViewGroup, viewType: Int): NavViewHolder

}