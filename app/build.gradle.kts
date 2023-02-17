import extenstions.*

plugins {
    id("com.android.application")
    id("dagger.hilt.android.plugin")
    kotlin("android")
    kotlin("kapt")
    id("kotlin-parcelize")
    id("androidx.navigation.safeargs.kotlin")
}

@Suppress("UnstableApiUsage")
android {
    compileSdk = ConfigData.compileSdk
    buildToolsVersion = ConfigData.buildToolsVersion

    defaultConfig {
        applicationId = ConfigData.applicationId
        minSdk = ConfigData.minSdk
        targetSdk = ConfigData.targetSdk
        versionCode = Release.versionCode
        versionName = Release.versionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {

        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        dataBinding = true
    }

    flavorDimensions.add(ConfigData.dimension)
    productFlavors {
        create("staging") {
            buildConfigField(
                type = "String",
                name = "HOST",
                value = "\"https://www.flickr.com/services/\""
            )
            buildConfigField(
                type = "String",
                name = "API_KEY",
                value = "\"fcf5029531c155e1774a25c5615f24ec\""
            )

            applicationIdSuffix = ".staging"
            dimension = ConfigData.dimension
            manifestPlaceholders["appIcon"] = "@drawable/logo"
            manifestPlaceholders["appIconRound"] = "@drawable/logo"
        }

        create("production") {
            buildConfigField(
                type = "String",
                name = "HOST",
                value = "\"https://www.flickr.com/services/\""
            )
            buildConfigField(
                type = "String",
                name = "API_KEY",
                value = "\"fcf5029531c155e1774a25c5615f24ec\""
            )
            dimension = ConfigData.dimension
            manifestPlaceholders["appIcon"] = "@drawable/logo"
            manifestPlaceholders["appIconRound"] = "@drawable/logo"
        }
    }
    testOptions {
        unitTests.isReturnDefaultValues = true
    }
}


dependencies {
    androidComponent()
    addNavigation()
    addHilt()
    addNetwork()
    addRoom()
    addTestsDependencies()
}