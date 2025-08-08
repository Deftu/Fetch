import groovy.lang.MissingPropertyException

pluginManagement {
    repositories {
        // Repositories
        maven("https://maven.deftu.dev/releases")
        maven("https://maven.fabricmc.net")
        maven("https://maven.architectury.dev/")
        maven("https://maven.minecraftforge.net")
        maven("https://repo.essential.gg/repository/maven-public")
        maven("https://server.bbkr.space/artifactory/libs-release/")
        maven("https://jitpack.io/")

        // Snapshots
        maven("https://maven.deftu.dev/snapshots")
        mavenLocal()

        // Default repositories
        gradlePluginPortal()
        mavenCentral()
    }

    plugins {
        kotlin("jvm") version("2.0.20")
        id("dev.deftu.gradle.multiversion-root") version("2.49.0")
    }
}

val projectName: String = extra["mod.name"]?.toString()
    ?: throw MissingPropertyException("mod.name has not been set.")
rootProject.name = projectName
rootProject.buildFileName = "root.gradle.kts"

listOf(
    "1.19.4-forge",
    "1.19.4-fabric",

    "1.20.1-forge",
    "1.20.1-fabric",

    "1.20.4-forge",
    "1.20.4-neoforge",
    "1.20.4-fabric",

    "1.20.6-neoforge",
    "1.20.6-fabric",

    "1.21.1-neoforge",
    "1.21.1-fabric",

    "1.21.2-neoforge",
    "1.21.2-fabric",

    "1.21.3-neoforge",
    "1.21.3-fabric",

    "1.21.4-neoforge",
    "1.21.4-fabric",

    "1.21.5-neoforge",
    "1.21.5-fabric",

    "1.21.6-neoforge",
    "1.21.6-fabric",

    "1.21.7-neoforge",
    "1.21.7-fabric",

    "1.21.8-neoforge",
    "1.21.8-fabric",
).forEach { version ->
    include(":$version")
    project(":$version").apply {
        projectDir = file("versions/$version")
        buildFileName = "../../build.gradle.kts"
    }
}
