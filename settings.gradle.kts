import java.util.Properties

pluginManagement {
    includeBuild("build-logic")

    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

val localProps = Properties().apply {
    val file = File(rootDir, "private.properties")
    if (file.exists()) {
        file.inputStream().use { load(it) }
    }
}

val gprUser = localProps.getProperty("gpr.user") ?: System.getenv("GPR_USER")
val gprKey = localProps.getProperty("gpr.key") ?: System.getenv("GPR_KEY")


@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()

        maven {
            url = uri("https://maven.pkg.github.com/kernelflux/KToolBox")
            credentials {
                username = gprUser
                password = gprKey
            }
        }

    }
}

rootProject.name = "UixKit"
include(":sample")
include(":uixkit-core")
include(":uixkit-ui")
include(":uixkit-adapter")

