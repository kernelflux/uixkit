package com.kernelflux.uixkitsample

import com.kernelflux.uixkit.debugtool.Feature
import com.kernelflux.uixkit.debugtool.FeatureEntry
import com.kernelflux.uixkit.debugtool.FeatureListActivity
import com.kernelflux.uixkitsample.refresh.RefreshActivity


class MainActivity : FeatureListActivity() {

    override fun getFeatures(): List<FeatureEntry> {
        return listOf(
            Feature.activity(
                "下来刷新RecyclerView",
                activityClass = RefreshActivity::class.java
            ),
        )
    }

}