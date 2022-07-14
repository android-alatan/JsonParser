@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id(libs.plugins.kotlinJvm.get().pluginId)
    id(libs.plugins.ksp.get().pluginId)
    id(libs.plugins.detekt.get().pluginId)
    id("libs-detekt")
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
