pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.fabricmc.net/")
        maven("https://maven.architectury.dev")
        maven("https://maven.minecraftforge.net")
        maven("https://maven.neoforged.net/releases/")
        maven("https://maven.kikugie.dev/snapshots")
    }
}

plugins {
    id("dev.kikugie.stonecutter") version "0.9.8"
}

stonecutter {
    kotlinController = true

    //post-deobfuscation versions - one versions/<version>.toml per entry
    val versions = arrayOf(
        "26.1",
        "26.2",
        "26.3"
    )
    for (v in versions) {
        properties.load(file("versions/$v.toml"))
    }

    shared {
        fun mc(version: String, vararg loaders: String) {
            for (loader in loaders) {
                val versionConfig = version("$version-$loader", version);
                versionConfig.buildscript = "build.gradle.kts";
            }
        }
        fun mc_legacy(version: String, vararg loaders: String) {
            for (loader in loaders) {
                val versionConfig = version("$version-$loader", version);
                versionConfig.buildscript = "build.legacyarch.gradle.kts";
            }
        }

        //post-deobfuscation versions
        for (version in versions) {
            mc(version, "fabric","neoforge")
        }

        mc_legacy("fabric", "1.20.1", "1.20.4", "1.21.1", "1.21.3", "1.21.4", "1.21.5", "1.21.6", "1.21.7", "1.21.8", "1.21.9", "1.21.10", "1.21.11")
        mc_legacy("forge", "1.20.1")
        mc_legacy("neoforge", "1.20.4", "1.21.1", "1.21.3", "1.21.4", "1.21.5", "1.21.6", "1.21.7", "1.21.8", "1.21.9", "1.21.10", "1.21.11")

    }
    create(rootProject)
}

rootProject.name = "CustomCursor"