pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
    plugins {
        id("org.jetbrains.compose").version("1.2.0-alpha01-dev755").apply(false)
    }
}
rootProject.name = "DemoComposeMulti"

include(":android", "iOS", ":desktop", ":server", ":common")
