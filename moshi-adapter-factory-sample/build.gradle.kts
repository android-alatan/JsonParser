@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.ksp)
}

kotlin {
    sourceSets {
        getByName("main") {
            kotlin.srcDir("build/generated/ksp/$name/kotlin")
        }
    }
}

dependencies {
    compileOnly(libs.androidAnnotation)
    implementation(libs.moshi)

    implementation(project(":moshi-adapter-factory"))

    ksp(libs.moshiCompiler)
    ksp(project(":moshi-adapter-factory-ksp"))
}

ksp {
    arg("moduleName", project.name)
}
