plugins {
    id("maven-publish")
}

afterEvaluate {
    val artifactName: String = name

    if (artifactName.contains("sample")) return@afterEvaluate

    val libVersion: String = if (project.hasProperty("libVersion")) {
        (project.property("libVersion") as String).substring(0, 7)
    } else {
        "dev"
    }

    val isAndroid = plugins.hasPlugin("com.android.library")

    publishing {
        publications {
            create<MavenPublication>("release") {
                if (isAndroid) {
                    from(components.getByName("release"))
                } else {
                    from(components.getByName("java"))
                }
                groupId = groupId()
                artifactId = artifactName
                this.version = libVersion

                artifact(tasks.getByName("sourceJar"))
            }

            repositories {
                maven {
                    url = uri("https://maven.pkg.github.com/android-alatan/JsonParser")
                    credentials {
                        // this should change to build local property manually once Gradle referring 1.5.0
                        username = System.getenv("GITHUB_ACTOR")
                        password = System.getenv("GITHUB_TOKEN")
                    }
                }
            }
        }
    }

    tasks.create("buildForDeploy") {
        dependsOn(if (isAndroid) "assembleRelease" else "assemble")
    }
}

fun groupId(): String {
    return "io.androidalatan.libs"
}