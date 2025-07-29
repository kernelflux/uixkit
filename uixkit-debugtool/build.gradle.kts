plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id("com.kernelflux.android.module")
}

android {
    namespace = "com.kernelflux.uixkit.debugtool"
}

dependencies {
    api(project(":uixkit-core"))
}
