// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.android.library) apply false
}

// 顶部自动加载 private.properties
val localProps = java.util.Properties().apply {
    val file = rootProject.file("private.properties")
    if (file.exists()) {
        file.inputStream().use { load(it) }
    }
    forEach { (k, v) -> project.ext.set(k.toString(), v) }
}


subprojects {
    // 自动引入发布脚本
    apply(from = "${rootDir}/gradle/publish.gradle.kts")
}

tasks.register("publishAllModules") {
    dependsOn(
        ":uixkit-core:publish",
        ":uixkit-ui:publish",
        ":uixkit-adapter:publish"
    )
}