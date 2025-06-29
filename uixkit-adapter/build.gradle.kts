plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id("com.kernelflux.android.module")
}

android {
    namespace = "com.kernelflux.uixkit.adapter"
}

dependencies {
}

extra["uixkit.version"] = "0.0.2"