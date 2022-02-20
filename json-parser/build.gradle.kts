@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id(libs.plugins.kotlinJvm.get().pluginId)
    id(libs.plugins.detekt.get().pluginId)
    id("code-quality")
    id("libs-detekt")
    id("publish-jvm")
}

dependencies {
    compileOnly(libs.androidAnnotation)
    api(libs.moshi)
    api(libs.javaX)
    api(project(":json-parser-api"))
    api(project(":moshi-adapter-factory"))

    testImplementation(libs.junit5)
    testRuntimeOnly(libs.jupiterEngine)
    testRuntimeOnly(libs.jupiterVintage)
}