// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        repositories {
            google()
            mavenCentral()
        }
    }
    dependencies {
        classpath(libs.kotlin)
        classpath(libs.detekt)
        classpath(libs.jacoco)
        classpath(libs.ksp)
    }
}

task("clean", Delete::class) {
    delete(rootProject.buildDir)
    delete("$rootDir/build")
}