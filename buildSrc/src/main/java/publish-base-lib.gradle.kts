plugins {
    id("maven-publish")
}

afterEvaluate {

    val isAndroid = plugins.hasPlugin("com.android.library")
    tasks.create("buildForDeploy") {
        dependsOn(if (isAndroid) "assembleRelease" else "assemble")
    }

    val artifactName: String = name

    if (artifactName.contains("sample")) return@afterEvaluate

    val libVersion: String
    if (project.hasProperty("libVersion")) {
        libVersion = project.property("libVersion") as String
    } else {
        return@afterEvaluate
    }

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
                    url = uri("https://jitpack.io")
                    credentials {
                        // this should change to build local property manually once Gradle referring 1.5.0
                        username = System.getenv("JITPACK_USERNAME")
                        password = System.getenv("JITPACK_ACCESS_TOKEN")
                    }
                }
            }
        }
    }


}

fun groupId(): String {
    return "io.androidalatan.libs"
}