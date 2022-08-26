
import org.jetbrains.compose.compose
import org.jetbrains.compose.experimental.dsl.IOSDevices
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

repositories {
    mavenCentral()
}

kotlin {
    /* Targets configuration omitted. 
    *  To find out how to configure the targets, please follow the link:
    *  https://kotlinlang.org/docs/reference/building-mpp-with-gradle.html#setting-up-targets */

    ios("uikit") {
        binaries {
            executable() {
                entryPoint = "main"
                freeCompilerArgs += listOf(
                    "-linker-option", "-framework", "-linker-option", "Metal",
                    "-linker-option", "-framework", "-linker-option", "CoreText",
                    "-linker-option", "-framework", "-linker-option", "CoreGraphics"
                )
            }
        }
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":common"))
                implementation(compose.ui)
                implementation(compose.foundation)
                implementation(compose.material)
                implementation(compose.runtime)
            }
        }

        val nativeMain by creating {
            dependsOn(commonMain)
        }
        val uikitMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-darwin:2.0.0")
            }
        }
    }
}

repositories {
    gradlePluginPortal()
    mavenCentral()
}

compose.experimental {
    uikit.application {
        bundleIdPrefix = "com.david.remote.server"
        projectName = "common"
        deployConfigurations {
            simulator("IPhone13_Pro_Max") {
                //Usage: ./gradlew iosDeployIPhone8Debug
                device = IOSDevices.IPHONE_13_PRO_MAX
            }
            simulator("IPad_12_9_inches") {
                //Usage: ./gradlew iosDeployIPadDebug
                device = IOSDevices.IPAD_12_9_INCH_5th_Gen
            }
            connectedDevice("Device") {
                //Usage: ./gradlew iosDeployDeviceRelease
            }
        }
    }
}

kotlin {
    targets.withType<KotlinNativeTarget> {
        binaries.all {
            // TODO: the current compose binary surprises LLVM, so disable checks for now.
            freeCompilerArgs += "-Xdisable-phases=VerifyBitcode"
            binaryOptions["memoryModel"] = "experimental"
        }
    }
}