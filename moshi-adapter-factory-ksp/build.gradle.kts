@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.detekt)
    id("code-quality")
}

dependencies {

    implementation(libs.kotlinPoet)
    implementation(libs.kspProcessingApi)
    compileOnly(libs.moshi)
    compileOnly(project(":moshi-adapter-factory"))
}