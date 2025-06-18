import org.gradle.kotlin.dsl.detektPlugins
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.konan.properties.loadProperties

val propertyFile = loadProperties("local.properties")

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.detekt)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.kotlin.serialization)
}

detekt {
    toolVersion = "1.23.8"
    config.setFrom(file("gradle/.detekt.yml"))
    buildUponDefaultConfig = true

    dependencies {
        // Compose Ruleset (BUILD ONLY) | https://github.com/mrmans0n/compose-rules | Apache-2.0
        detektPlugins(libs.detekt.compose)
    }
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_23)
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "shared"
            isStatic = true
        }
    }

    sourceSets {
        androidMain.dependencies {
            implementation(libs.androidx.activity.compose)

            // [Common] Async Client | https://github.com/ktorio/ktor | Apache-2.0
            implementation(libs.ktor.client.okhttp)
        }

        iosMain.dependencies {
            // [Common] Async Client | https://github.com/ktorio/ktor | Apache-2.0
            implementation(libs.ktor.client.darwin)
        }

        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtimeCompose)

            // KotlinX | ... | usually Apache-2.0
            implementation(libs.kotlinx.serialization)
            implementation(libs.kotlinx.coroutines)

            // Model-Driven Navigation | https://github.com/bumble-tech/appyx | Apache-2.0
            implementation(libs.appyx.interactions)
            implementation(libs.appyx.navigation)
            api(libs.appyx.backstack)
            api(libs.appyx.spotlight)
            implementation(libs.appyx.material3)
            implementation(libs.appyx.multiplatform)

            // Glass-morphism | https://github.com/chrisbanes/haze | Apache-2.0
            implementation(libs.haze)

            // Kamel | https://github.com/Kamel-Media/Kamel | Apache-2.0
            // Images not loading? Don't forget to import other optional dependencies...
            implementation(libs.kamel.core)

            // Async Web Client | https://github.com/ktorio/ktor | Apache-2.0
            implementation(libs.ktor.client.core)

            // Lucide Icons | https://github.com/composablehorizons/composeicons | MIT
            //              | https://lucide.dev/ | ISC
            implementation(libs.composables.icons.lucide)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

android {
    namespace = "dev.lancy.studysmith"
    compileSdk = 35

    defaultConfig {
        applicationId = "dev.lancy.studysmith"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    signingConfigs {
        create("release") {
            storeFile = file("../keystore.jks")
            with(propertyFile) {
                storePassword = getProperty("storePassword")
                keyAlias = getProperty("keyAlias")
                keyPassword = getProperty("keyPassword")
            }
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_23
        targetCompatibility = JavaVersion.VERSION_23
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
}
