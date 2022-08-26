import Constants.KotlinVersion
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    application
}

repositories {
    mavenCentral()
}

group = "com.demo"
version = "1.0-SNAPSHOT"

dependencies{
    implementation("org.jetbrains.kotlin:kotlin-stdlib:$KotlinVersion")
    implementation(project(":common"))
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}

application {
    mainClass.set("MainKt")
}

tasks.named<JavaExec>("run") {
    standardInput = System.`in`
}

tasks.withType<Wrapper> {
    gradleVersion = "7.5.1"
    distributionType = Wrapper.DistributionType.BIN
}