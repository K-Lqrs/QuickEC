import net.minecrell.pluginyml.bukkit.BukkitPluginDescription

plugins {
    kotlin("jvm") version "2.0.0"
    id("xyz.jpenilla.run-paper") version "2.3.0"
    id("io.papermc.paperweight.userdev") version "1.7.1"
    id("net.minecrell.plugin-yml.bukkit") version "0.6.0"
}

group = "net.rk4z"
version = "2.0.0"

repositories {
    mavenCentral()
    mavenLocal()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    paperweight.paperDevBundle("1.21.3-R0.1-SNAPSHOT")
    compileOnly("io.papermc.paper:paper-api:1.21.3-R0.1-SNAPSHOT")
    implementation("net.rk4z.s1:pluginbase:1.1.4")

    library("com.google.code.gson", "gson", "2.10.1")
    bukkitLibrary("com.google.code.gson", "gson", "2.10.1")
}

kotlin {
    jvmToolchain(21)
}

bukkit {
    main = "net.rk4z.quickec.QuickEC"
    foliaSupported = false
    apiVersion = "1.20"
    load = BukkitPluginDescription.PluginLoadOrder.STARTUP
    authors = listOf("Lars")
    contributors = listOf("Lars", "cotrin_d8")

    depend = listOf("Kotlin")
    softDepend = listOf("LuckPerms")

    commands {
        register("ec") {
            description = "Open your ender chest"
        }

        register("uec") {
            description = "Open another player's ender chest"
        }
    }

    permissions {
        register("quickec.open.self") {
            description = "Allows the player to open their own ender chest"
            default = BukkitPluginDescription.Permission.Default.TRUE
        }

        register("quickec.open.others") {
            description = "Allows the player to open other players' ender chests"
            default = BukkitPluginDescription.Permission.Default.OP
        }

        register("quickec.open.ignore_inventory") {
            description = "Allows the player to open their ender chest even if they don't have one in their inventory"
            default = BukkitPluginDescription.Permission.Default.OP
        }

        register("quickec.open.click") {
            description = "Allows the player to open their ender chest by clicking"
            default = BukkitPluginDescription.Permission.Default.TRUE
        }
    }
}