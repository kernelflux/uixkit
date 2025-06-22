plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.kernelflux.android.module")
}

android {
    namespace = "com.kernelflux.uixkitsample"
}

dependencies {
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)


    debugImplementation(project(":uixkit-adapter"))
    debugImplementation(project(":uixkit-core"))
    debugImplementation(project(":uixkit-ui"))


    //noinspection UseTomlInstead
    releaseImplementation("com.kernelflux.uixkit:adapter:0.0.1")
    //noinspection UseTomlInstead
    releaseImplementation("com.kernelflux.uixkit:core:0.0.1")
    //noinspection UseTomlInstead
    releaseImplementation("com.kernelflux.uixkit:ui:0.0.1")

}