import java.util.Properties

val localProps = Properties().apply {
    val file = rootProject.file("private.properties")
    if (file.exists()) {
        file.inputStream().use { load(it) }
    }
}

fun getProp(key: String): String? = localProps.getProperty(key)

afterEvaluate {
    extensions.findByType(PublishingExtension::class.java)?.apply {
        publications {
            create<MavenPublication>("release") {
                groupId = "com.kernelflux"
                artifactId = project.name
                version = project.findProperty("uixkit.version")?.toString() ?: "1.0.0"

                when {
                    plugins.hasPlugin("com.android.library") -> {
                        try {
                            from(components["release"])
                        } catch (e: Exception) {
                            logger.warn("No 'release' component found in ${project.name}")
                        }
                    }
                    plugins.hasPlugin("org.jetbrains.kotlin.jvm") -> {
                        from(components["java"])
                    }
                }
            }
        }

        repositories {
            maven {
                name = "GitHubPackages"
                url = uri("https://maven.pkg.github.com/kernelflux/UixKit")
                credentials {
                    username = getProp("gpr.user") ?: ""
                    password = getProp("gpr.key") ?: ""
                }
            }
        }
    }
}