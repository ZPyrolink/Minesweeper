buildscript {
    dependencies {
    }
}

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    kotlin("android") version libs.versions.kotlin apply false
    kotlin("plugin.compose") version libs.versions.kotlin apply false

    alias(libs.plugins.android.app) apply false
    alias(libs.plugins.android.lib) apply false
    alias(libs.plugins.ksp) apply false
}


tasks.register("clean", Delete::class) {
    delete(rootProject.layout.buildDirectory)
}
