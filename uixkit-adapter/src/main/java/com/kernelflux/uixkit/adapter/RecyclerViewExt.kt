package com.kernelflux.uixkit.adapter

import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.addSmartDecoration(decoration: SmartItemDecoration) {
    this.addItemDecoration(decoration)
}

fun RecyclerView.addStickyHeaderDecoration(provider: StickyHeaderProvider) {
    this.addItemDecoration(StickyHeaderItemDecoration(provider))
}