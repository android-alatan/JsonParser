dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {
            setUrl("https://jitpack.io")
        }
    }
}
rootProject.name = "JsonParser"
include("json-parser-api", "moshi-adapter-factory", "json-parser")
include("moshi-adapter-factory-ksp", "moshi-adapter-factory-sample")