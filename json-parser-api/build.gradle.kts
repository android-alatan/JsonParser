@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.detekt)
    id("code-quality")
}

dependencies {
    compileOnly(libs.androidAnnotation)
    api(libs.moshi)
}