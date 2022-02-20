@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id(libs.plugins.kotlinJvm.get().pluginId)
    id(libs.plugins.detekt.get().pluginId)
    id("code-quality")
    id("libs-detekt")
}

dependencies {
    api(libs.moshi)
}