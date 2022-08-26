import Constants.KotlinVersion
import Constants.KtorVersion
import org.jetbrains.compose.compose
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    id("com.android.library")
    kotlin("plugin.serialization")
}

group = "com.david.remote.server"
version = "1.0"

kotlin {
    android {
        compilations.all {
            kotlinOptions.jvmTarget = "11"
        }
        tasks.withType<KotlinCompile> {
            kotlinOptions {
                jvmTarget = "11"
            }
        }
    }

    jvm("desktop") {
        compilations.all {
            kotlinOptions.jvmTarget = "11"
        }
        tasks.withType<KotlinCompile> {
            kotlinOptions {
                jvmTarget = "11"
            }
        }
    }

    iosX64("uikitX64") {
        binaries {
            executable() {
                freeCompilerArgs += listOf(
                    "-linker-option", "-framework", "-linker-option", "Metal",
                    "-linker-option", "-framework", "-linker-option", "CoreText",
                    "-linker-option", "-framework", "-linker-option", "CoreGraphics"
                )
            }
        }
    }
    iosArm64("uikitArm64") {
        binaries {
            executable() {
                freeCompilerArgs += listOf(
                    "-linker-option", "-framework", "-linker-option", "Metal",
                    "-linker-option", "-framework", "-linker-option", "CoreText",
                    "-linker-option", "-framework", "-linker-option", "CoreGraphics"
                )
                // TODO: the current compose binary surprises LLVM, so disable checks for now.
                freeCompilerArgs += "-Xdisable-phases=VerifyBitcode"
            }
        }
    }

    sourceSets {
        val commonMain by getting {
            resources.srcDirs("resources")
            dependencies {
                //  implementation(project(":server"))
                api(compose.runtime)
                api(compose.foundation)
                api(compose.material)
                implementation("org.jetbrains.kotlin:kotlin-stdlib:$KotlinVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.1")
                implementation("io.ktor:ktor-client-core:$KtorVersion")
                implementation("io.ktor:ktor-client-json:$KtorVersion")
                implementation("io.ktor:ktor-client-websockets:$KtorVersion")
                implementation("io.ktor:ktor-network:$KtorVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.0")
            }
        }

        val androidMain by getting {
            dependencies {
                api("androidx.appcompat:appcompat:1.4.1")
                api("androidx.core:core-ktx:$KotlinVersion")
                implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.1")
                implementation("io.ktor:ktor-client-cio:$KtorVersion")
            }
        }

        val desktopMain by getting {
            dependencies {
                api(compose.preview)
                implementation("io.ktor:ktor-client-cio:$KtorVersion")
            }
        }

        val uikitX64Main by getting {
            dependsOn(commonMain)
            dependencies {
                implementation("io.ktor:ktor-client-darwin:$KtorVersion")
            }
        }

        val uikitArm64Main by getting {
            dependsOn(uikitX64Main)
        }
    }
}

repositories {
    gradlePluginPortal()
    mavenCentral()
}

android {
    compileSdk = 31
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/commonMain/resources")
    defaultConfig {
        minSdk = 24
        targetSdk = 31
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
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