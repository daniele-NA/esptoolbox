plugins {
    alias(libs.plugins.android.library)
}

android {
    namespace = "com.crescenzi.esp32"
    compileSdk = 37

    defaultConfig {
        minSdk = 27
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        buildConfig = true
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
    }
}

dependencies {
    implementation(files("libs/physical_oid.aar"))
    // == usb-serial types leak into the app (UsbSerialProber), so expose it transitively == //
    api(libs.usb.serial)
    implementation(libs.esp.touch)
    implementation(libs.kotlinx.coroutines.android)
}
