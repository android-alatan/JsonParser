// Top-level build file where you can add configuration options common to all sub-projects/modules.
@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.androidLibrary).apply(false)
    alias(libs.plugins.androidApp).apply(false)
    alias(libs.plugins.kotlinAndroid).apply(false)
    alias(libs.plugins.kotlinJvm).apply(false)
    alias(libs.plugins.ksp).apply(false)
    alias(libs.plugins.detekt)
}

task("clean", Delete::class) {
    delete(rootProject.buildDir)
    delete("$rootDir/build")
}


allprojects {
    apply(plugin = "io.gitlab.arturbosch.detekt")
    dependencies {
        "detektPlugins"("io.gitlab.arturbosch.detekt:detekt-formatting:1.19.0")
    }
    detekt {
        config.setFrom(files("$rootDir/config/detekt/detekt.yml"))
        reports {
            file("$rootDir/build/reports/test/${project.name}/").mkdirs()
            xml {
                required.set(true)
                outputLocation.set(file("$rootDir/build/reports/detekt/${project.name}.xml"))
            }
            html {
                required.set(true)
                outputLocation.set(file("$rootDir/build/reports/detekt/${project.name}.html"))
            }
        }
    }
}