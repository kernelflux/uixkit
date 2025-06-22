package com.kernelflux.uixkit.ui.minipage


interface ILifeCycleProvider {

    fun registerLifeCycleMonitor(lifeCycleMonitor: LifeCycleMonitor?)

    fun unregisterLifeCycleMonitor(lifeCycleMonitor: LifeCycleMonitor?)

}