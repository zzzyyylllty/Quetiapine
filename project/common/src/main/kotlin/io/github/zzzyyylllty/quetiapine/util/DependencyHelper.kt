package io.github.zzzyyylllty.quetiapine.util

import org.bukkit.Bukkit


val dependencies = listOf(
    "adventure",
    // "arim",
    "caffeine",
    "datafixerupper",
    "fluxon",
    // "graaljs",
    "gson",
    "jackson",
    // "kotlincrypto",
    "uniitem"
)

object DependencyHelper {

    val mmLib by lazy {
        isPluginInstalled("MythicLib")
    }

    val papi by lazy {
        isPluginInstalled("PlaceholderAPI")
    }

    val pe by lazy {
        isPluginInstalled("packetevents")
    }




    fun isPluginInstalled(name: String): Boolean {
        return (Bukkit.getPluginManager().getPlugin(name) != null)
    }

}


