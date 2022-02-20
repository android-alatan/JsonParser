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
}