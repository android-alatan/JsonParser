@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.detekt)
    id("code-quality")
    id("lib-tasks")
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