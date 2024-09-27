import net.minecrell.pluginyml.bukkit.BukkitPluginDescription

plugins {
    kotlin("jvm") version "2.0.0"
    id("net.minecrell.plugin-yml.bukkit") version "0.6.0"
}

group = "net.rk4z"
version = "1.0.0"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.20.4-R0.1-SNAPSHOT")

    library("com.google.code.gson", "gson", "2.10.1")
    bukkitLibrary("com.google.code.gson", "gson", "2.10.1")
}

kotlin {
    jvmToolchain(17)
}

bukkit {
    main = "net.rk4z.quickec.QuickEC"
    foliaSupported = false
    apiVersion = "1.20"
    load = BukkitPluginDescription.PluginLoadOrder.STARTUP
    authors = listOf("Lars")
    contributors = listOf("Lars", "cotrin_d8")

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
    }
}