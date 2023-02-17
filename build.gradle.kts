// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath(Build.android)
        classpath(Build.kotlin)
        classpath(Build.hilt)
        classpath(Build.safeArgs)
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        jcenter()
        maven(url = "https://jitpack.io")
    }
}
tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}