plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id("com.kernelflux.android.module")
}

android {
    namespace = "com.kernelflux.uixkit.ui"
}

dependencies {

    //noinspection UseTomlInstead
    api("com.kernelflux.ktoolbox:core:0.0.4")
    api("com.kernelflux.ktoolbox:display:0.0.4")

}
