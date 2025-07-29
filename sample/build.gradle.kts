plugins {
    alias(libs.plugins.android.application)
    id("com.kernelflux.android.module")
}

android{
    namespace = "com.kernelflux.uixkitsample"
}

dependencies {
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)


    implementation("com.github.bumptech.glide:glide:4.16.0")

    debugImplementation(project(":uixkit-debugtool"))
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