@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.kotlinJvm)
}

dependencies {
    compileOnly(libs.androidAnnotation)
    api(libs.moshi)
}