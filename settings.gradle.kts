pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
    plugins {
        id("org.jetbrains.compose").version("1.4.0").apply(false)
    }
}
rootProject.name = "DemoComposeMulti"

include(":android", ":desktop", ":server", ":common")
