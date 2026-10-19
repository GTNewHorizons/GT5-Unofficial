
pluginManagement {
    repositories {
        maven {
            // RetroFuturaGradle
            name = "GTNH Maven"
            url = uri("https://nexus.gtnewhorizons.com/repository/public/")
            mavenContent {
                includeGroup("com.gtnewhorizons")
                includeGroupByRegex("com\\.gtnewhorizons\\..+")
            }
        }
        gradlePluginPortal()
        mavenCentral()
        mavenLocal()
    }
    plugins {
        kotlin("jvm") version "2.2.0"
    }
}

plugins {
    id("com.gtnewhorizons.gtnhsettingsconvention") version("2.0.34")
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
