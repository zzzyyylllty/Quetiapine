package io.github.zzzyyylllty.quetiapine.function.fluxon.script

import io.github.zzzyyylllty.quetiapine.Quetiapine.fluxonInst
import io.github.zzzyyylllty.quetiapine.util.minimessage.mmUtil
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.NamespacedKey
import org.bukkit.World
import org.bukkit.command.CommandSender
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.Recipe
import org.tabooproject.fluxon.runtime.FluxonRuntime
import org.tabooproject.fluxon.runtime.FunctionContext
import org.tabooproject.fluxon.runtime.NativeFunction.NativeCallable
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import java.util.*
import org.bukkit.*
import org.bukkit.block.data.BlockData
import org.bukkit.boss.BarColor
import org.bukkit.boss.BarFlag
import org.bukkit.boss.BarStyle
import org.bukkit.entity.Entity
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.*
import java.io.File
import java.net.InetAddress
import java.util.function.Consumer

@Awake(LifeCycle.ENABLE)
fun registerFunctionBukkitSimple() {
    FunctionBukkitSimple.init(fluxonInst!!)
}

@Suppress("UNUSED")
object FunctionBukkitSimple {
    fun init(runtime: FluxonRuntime) {
        // --- Server Information and Properties ---

        runtime.registerFunction("quetiapine:bukkit", "getServer", 0, NativeCallable { ctx: FunctionContext<Any?>? -> Bukkit.getServer() })
        runtime.registerFunction("quetiapine:bukkit", "getPluginsFolder", 0, NativeCallable { ctx: FunctionContext<Any?>? -> Bukkit.getPluginsFolder() })
        runtime.registerFunction("quetiapine:bukkit", "getName", 0, NativeCallable { ctx: FunctionContext<Any?>? -> Bukkit.getName() })
        runtime.registerFunction("quetiapine:bukkit", "getVersion", 0, NativeCallable { ctx: FunctionContext<Any?>? -> Bukkit.getVersion() })
        runtime.registerFunction("quetiapine:bukkit", "getBukkitVersion", 0, NativeCallable { ctx: FunctionContext<Any?>? -> Bukkit.getBukkitVersion() })
        runtime.registerFunction("quetiapine:bukkit", "getMinecraftVersion", 0, NativeCallable { ctx: FunctionContext<Any?>? -> Bukkit.getMinecraftVersion() }) // Paper API
        runtime.registerFunction("quetiapine:bukkit", "getVersionMessage", 0, NativeCallable { ctx: FunctionContext<Any?>? -> Bukkit.getVersionMessage() })

        // --- Player Management ---

        runtime.registerFunction("quetiapine:bukkit", "getOnlinePlayers", 0, NativeCallable { ctx: FunctionContext<Any?>? -> Bukkit.getOnlinePlayers() })
        runtime.registerFunction("quetiapine:bukkit", "getMaxPlayers", 0, NativeCallable { ctx: FunctionContext<Any?>? -> Bukkit.getMaxPlayers() })
        runtime.registerFunction("quetiapine:bukkit", "setMaxPlayers", 1, NativeCallable { ctx: FunctionContext<Any?>? -> Bukkit.setMaxPlayers(ctx!!.getNumber(0).toInt()) })

        // getPlayer (overloaded)
        runtime.registerFunction("quetiapine:bukkit", "getPlayer", 1, NativeCallable { ctx: FunctionContext<Any?>? ->
            when (val arg = ctx!!.getArgument(0)) {
                is String -> Bukkit.getPlayer(arg)
                is UUID -> Bukkit.getPlayer(arg)
                else -> throw IllegalArgumentException("Argument for getPlayer must be a String (name) or UUID.")
            }
        })
        runtime.registerFunction("quetiapine:bukkit", "getPlayerByName", 1, NativeCallable { ctx: FunctionContext<Any?>? -> Bukkit.getPlayer(ctx!!.getString(0)!!) })
        runtime.registerFunction("quetiapine:bukkit", "getPlayerByUUID", 1, NativeCallable { ctx: FunctionContext<Any?>? ->
            when (val arg = ctx!!.getArgument(0)) {
                is UUID -> Bukkit.getPlayer(arg)
                is String -> Bukkit.getPlayer(UUID.fromString(arg))
                else -> throw IllegalArgumentException("Argument for getPlayerByUUID must be a String or UUID.")
            }
        })
        runtime.registerFunction("quetiapine:bukkit", "getPlayerExact", 1, NativeCallable { ctx: FunctionContext<Any?>? -> Bukkit.getPlayerExact(ctx!!.getString(0)!!) })
        runtime.registerFunction("quetiapine:bukkit", "matchPlayer", 1, NativeCallable { ctx: FunctionContext<Any?>? -> Bukkit.matchPlayer(ctx!!.getString(0)!!) })
        runtime.registerFunction("quetiapine:bukkit", "getPlayerUniqueId", 1, NativeCallable { ctx: FunctionContext<Any?>? -> Bukkit.getPlayerUniqueId(ctx!!.getString(0)!!) }) // Paper API

        // --- Server Configuration ---

        runtime.registerFunction("quetiapine:bukkit", "getPort", 0, NativeCallable { ctx: FunctionContext<Any?>? -> Bukkit.getPort() })
        runtime.registerFunction("quetiapine:bukkit", "getViewDistance", 0, NativeCallable { ctx: FunctionContext<Any?>? -> Bukkit.getViewDistance() })
        runtime.registerFunction("quetiapine:bukkit", "getSimulationDistance", 0, NativeCallable { ctx: FunctionContext<Any?>? -> Bukkit.getSimulationDistance() })
        runtime.registerFunction("quetiapine:bukkit", "getIp", 0, NativeCallable { ctx: FunctionContext<Any?>? -> Bukkit.getIp() })
        runtime.registerFunction("quetiapine:bukkit", "getWorldType", 0, NativeCallable { ctx: FunctionContext<Any?>? -> Bukkit.getWorldType() })
        runtime.registerFunction("quetiapine:bukkit", "getGenerateStructures", 0, NativeCallable { ctx: FunctionContext<Any?>? -> Bukkit.getGenerateStructures() })
        runtime.registerFunction("quetiapine:bukkit", "getMaxWorldSize", 0, NativeCallable { ctx: FunctionContext<Any?>? -> Bukkit.getMaxWorldSize() })
        runtime.registerFunction("quetiapine:bukkit", "getAllowEnd", 0, NativeCallable { ctx: FunctionContext<Any?>? -> Bukkit.getAllowEnd() })
        runtime.registerFunction("quetiapine:bukkit", "getAllowNether", 0, NativeCallable { ctx: FunctionContext<Any?>? -> Bukkit.getAllowNether() })
        runtime.registerFunction("quetiapine:bukkit", "isLoggingIPs", 0, NativeCallable { ctx: FunctionContext<Any?>? -> Bukkit.isLoggingIPs() })
        runtime.registerFunction("quetiapine:bukkit", "getOnlineMode", 0, NativeCallable { ctx: FunctionContext<Any?>? -> Bukkit.getOnlineMode() })
        runtime.registerFunction("quetiapine:bukkit", "getAllowFlight", 0, NativeCallable { ctx: FunctionContext<Any?>? -> Bukkit.getAllowFlight() })
        runtime.registerFunction("quetiapine:bukkit", "isHardcore", 0, NativeCallable { ctx: FunctionContext<Any?>? -> Bukkit.isHardcore() })
        runtime.registerFunction("quetiapine:bukkit", "shutdown", 0, NativeCallable { ctx: FunctionContext<Any?>? -> Bukkit.shutdown() })
        runtime.registerFunction("quetiapine:bukkit", "restart", 0, NativeCallable { ctx: FunctionContext<Any?>? -> Bukkit.restart() }) // Spigot API

        // --- Broadcasting ---

        // broadcast (flexible, handles String and Component)
        runtime.registerFunction("quetiapine:bukkit", "broadcast", 1, NativeCallable { ctx: FunctionContext<Any?>? ->
            when (val arg = ctx!!.getArgument(0)) {
                is String -> Bukkit.broadcast(mmUtil.deserialize(arg))
                is Component -> Bukkit.broadcast(arg)
                else -> throw IllegalArgumentException("Argument for broadcast must be a String or Component.")
            }
        })

        // broadcast with permission
        runtime.registerFunction("quetiapine:bukkit", "broadcastPermission", 2, NativeCallable { ctx: FunctionContext<Any?>? ->
            val message = when (val arg0 = ctx!!.getArgument(0)) {
                is String -> mmUtil.deserialize(arg0)
                is Component -> arg0
                else -> throw IllegalArgumentException("Argument 0 for broadcast must be a String or Component.")
            }
            val permission = ctx!!.getString(1)!!
            Bukkit.broadcast(message, permission)
        })

        // Deprecated broadcast methods
        runtime.registerFunction("quetiapine:bukkit", "broadcastMessage", 1, NativeCallable { ctx: FunctionContext<Any?>? ->
            // Note: This uses legacy string formatting.
            Bukkit.broadcastMessage(ctx!!.getString(0)!!)
        })

        // --- Whitelist ---

        runtime.registerFunction("quetiapine:bukkit", "hasWhitelist", 0, NativeCallable { ctx: FunctionContext<Any?>? -> Bukkit.hasWhitelist() })
        runtime.registerFunction("quetiapine:bukkit", "setWhitelist", 1, NativeCallable { ctx: FunctionContext<Any?>? -> Bukkit.setWhitelist(ctx!!.getBoolean(0)) })
        runtime.registerFunction("quetiapine:bukkit", "isWhitelistEnforced", 0, NativeCallable { ctx: FunctionContext<Any?>? -> Bukkit.isWhitelistEnforced() })
        runtime.registerFunction("quetiapine:bukkit", "setWhitelistEnforced", 1, NativeCallable { ctx: FunctionContext<Any?>? -> Bukkit.setWhitelistEnforced(ctx!!.getBoolean(0)) })
        runtime.registerFunction("quetiapine:bukkit", "getWhitelistedPlayers", 0, NativeCallable { ctx: FunctionContext<Any?>? -> Bukkit.getWhitelistedPlayers() })
        runtime.registerFunction("quetiapine:bukkit", "reloadWhitelist", 0, NativeCallable { ctx: FunctionContext<Any?>? -> Bukkit.reloadWhitelist() })

        // --- Plugin and Services ---

        runtime.registerFunction("quetiapine:bukkit", "getPluginManager", 0, NativeCallable { ctx: FunctionContext<Any?>? -> Bukkit.getPluginManager() })
        runtime.registerFunction("quetiapine:bukkit", "getScheduler", 0, NativeCallable { ctx: FunctionContext<Any?>? -> Bukkit.getScheduler() })
        runtime.registerFunction("quetiapine:bukkit", "getServicesManager", 0, NativeCallable { ctx: FunctionContext<Any?>? -> Bukkit.getServicesManager() })
        runtime.registerFunction("quetiapine:bukkit", "getMessenger", 0, NativeCallable { ctx: FunctionContext<Any?>? -> Bukkit.getMessenger() })
        runtime.registerFunction("quetiapine:bukkit", "getHelpMap", 0, NativeCallable { ctx: FunctionContext<Any?>? -> Bukkit.getHelpMap() })

        // --- Worlds ---

        runtime.registerFunction("quetiapine:bukkit", "getWorlds", 0, NativeCallable { ctx: FunctionContext<Any?>? -> Bukkit.getWorlds() })
        runtime.registerFunction("quetiapine:bukkit", "isTickingWorlds", 0, NativeCallable { ctx: FunctionContext<Any?>? -> Bukkit.isTickingWorlds() }) // Paper API
        runtime.registerFunction("quetiapine:bukkit", "createWorld", 1, NativeCallable { ctx: FunctionContext<Any?>? -> Bukkit.createWorld(ctx!!.getArgumentByType(0, WorldCreator::class.java)!!) })

        // unloadWorld (overloaded)
        runtime.registerFunction("quetiapine:bukkit", "unloadWorld", 2, NativeCallable { ctx: FunctionContext<Any?>? ->
            val save = ctx!!.getBoolean(1)
            when (val arg0 = ctx!!.getArgument(0)) {
                is String -> Bukkit.unloadWorld(arg0, save)
                is World -> Bukkit.unloadWorld(arg0, save)
                else -> throw IllegalArgumentException("Argument 0 for unloadWorld must be a String (name) or World object.")
            }
        })

        // getWorld (overloaded)
        runtime.registerFunction("quetiapine:bukkit", "getWorld", 1, NativeCallable { ctx: FunctionContext<Any?>? ->
            when (val arg = ctx!!.getArgument(0)) {
                is String -> Bukkit.getWorld(arg)
                is UUID -> Bukkit.getWorld(arg)
                is NamespacedKey -> Bukkit.getWorld(arg) // Paper API
                is net.kyori.adventure.key.Key -> Bukkit.getWorld(arg) // Paper API
                else -> throw IllegalArgumentException("Argument for getWorld must be a String, UUID, NamespacedKey, or Key.")
            }
        })

        runtime.registerFunction("quetiapine:bukkit", "getWorldContainer", 0, NativeCallable { ctx: FunctionContext<Any?>? -> Bukkit.getWorldContainer() })
        runtime.registerFunction("quetiapine:bukkit", "createWorldBorder", 0, NativeCallable { ctx: FunctionContext<Any?>? -> Bukkit.createWorldBorder() })

        // --- Commands ---

        runtime.registerFunction("quetiapine:bukkit", "getPluginCommand", 1, NativeCallable { ctx: FunctionContext<Any?>? -> Bukkit.getPluginCommand(ctx!!.getString(0)!!) })
        runtime.registerFunction("quetiapine:bukkit", "dispatchCommand", 2, NativeCallable { ctx: FunctionContext<Any?>? ->
            val sender = ctx!!.getArgumentByType(0, CommandSender::class.java)!!
            val commandLine = ctx!!.getString(1)!!
            Bukkit.dispatchCommand(sender, commandLine)
        })
        runtime.registerFunction("quetiapine:bukkit", "getCommandAliases", 0, NativeCallable { ctx: FunctionContext<Any?>? -> Bukkit.getCommandAliases() })
        runtime.registerFunction("quetiapine:bukkit", "getConsoleSender", 0, NativeCallable { ctx: FunctionContext<Any?>? -> Bukkit.getConsoleSender() })
        runtime.registerFunction("quetiapine:bukkit", "getCommandMap", 0, NativeCallable { ctx: FunctionContext<Any?>? -> Bukkit.getCommandMap() }) // Paper API

        // --- Recipes ---

        runtime.registerFunction("quetiapine:bukkit", "addRecipe", 1, NativeCallable { ctx: FunctionContext<Any?>? ->
            Bukkit.addRecipe(ctx!!.getArgumentByType(0, Recipe::class.java))
        })
        runtime.registerFunction("quetiapine:bukkit", "getRecipesFor", 1, NativeCallable { ctx: FunctionContext<Any?>? ->
            Bukkit.getRecipesFor(ctx!!.getArgumentByType(0, ItemStack::class.java)!!)
        })
        runtime.registerFunction("quetiapine:bukkit", "getRecipe", 1, NativeCallable { ctx: FunctionContext<Any?>? ->
            Bukkit.getRecipe(ctx!!.getArgumentByType(0, NamespacedKey::class.java)!!)
        })
        runtime.registerFunction("quetiapine:bukkit", "recipeIterator", 0, NativeCallable { ctx: FunctionContext<Any?>? -> Bukkit.recipeIterator() })
        runtime.registerFunction("quetiapine:bukkit", "clearRecipes", 0, NativeCallable { ctx: FunctionContext<Any?>? -> Bukkit.clearRecipes() })
        runtime.registerFunction("quetiapine:bukkit", "resetRecipes", 0, NativeCallable { ctx: FunctionContext<Any?>? -> Bukkit.resetRecipes() })
        runtime.registerFunction("quetiapine:bukkit", "removeRecipe", 1, NativeCallable { ctx: FunctionContext<Any?>? ->
            Bukkit.removeRecipe(ctx!!.getArgumentByType(0, NamespacedKey::class.java)!!)
        })

        // --- Banning ---

        runtime.registerFunction("quetiapine:bukkit", "getIPBans", 0, NativeCallable { ctx: FunctionContext<Any?>? -> Bukkit.getIPBans() })
        runtime.registerFunction("quetiapine:bukkit", "banIP", 1, NativeCallable { ctx: FunctionContext<Any?>? ->
            when (val arg = ctx!!.getArgument(0)) {
                is String -> Bukkit.banIP(arg)
                is InetAddress -> Bukkit.banIP(arg)
                else -> throw IllegalArgumentException("Argument for banIP must be a String or InetAddress.")
            }
        })
        runtime.registerFunction("quetiapine:bukkit", "unbanIP", 1, NativeCallable { ctx: FunctionContext<Any?>? ->
            when (val arg = ctx!!.getArgument(0)) {
                is String -> Bukkit.unbanIP(arg)
                is InetAddress -> Bukkit.unbanIP(arg)
                else -> throw IllegalArgumentException("Argument for unbanIP must be a String or InetAddress.")
            }
        })
        runtime.registerFunction("quetiapine:bukkit", "getBannedPlayers", 0, NativeCallable { ctx: FunctionContext<Any?>? -> Bukkit.getBannedPlayers() })
        runtime.registerFunction("quetiapine:bukkit", "getBanList", 1, NativeCallable { ctx: FunctionContext<Any?>? ->
            Bukkit.getBanList(ctx!!.getArgumentByType(0, BanList.Type::class.java)!!)
        })

        // --- Operators ---

        runtime.registerFunction("quetiapine:bukkit", "getOperators", 0, NativeCallable { ctx: FunctionContext<Any?>? -> Bukkit.getOperators() })

        // --- Offline Players ---

        runtime.registerFunction("quetiapine:bukkit", "getOfflinePlayer", 1, NativeCallable { ctx: FunctionContext<Any?>? ->
            when (val arg = ctx!!.getArgument(0)) {
                is String -> Bukkit.getOfflinePlayer(arg)
                is UUID -> Bukkit.getOfflinePlayer(arg)
                else -> throw IllegalArgumentException("Argument for getOfflinePlayer must be a String or UUID.")
            }
        })
        runtime.registerFunction("quetiapine:bukkit", "getOfflinePlayerIfCached", 1, NativeCallable { ctx: FunctionContext<Any?>? -> Bukkit.getOfflinePlayerIfCached(ctx!!.getString(0)!!) }) // Paper API
        runtime.registerFunction("quetiapine:bukkit", "getOfflinePlayers", 0, NativeCallable { ctx: FunctionContext<Any?>? -> Bukkit.getOfflinePlayers() })

        // --- Game Mode ---

        runtime.registerFunction("quetiapine:bukkit", "getDefaultGameMode", 0, NativeCallable { ctx: FunctionContext<Any?>? -> Bukkit.getDefaultGameMode() })
        runtime.registerFunction("quetiapine:bukkit", "setDefaultGameMode", 1, NativeCallable { ctx: FunctionContext<Any?>? ->
            Bukkit.setDefaultGameMode(ctx!!.getArgumentByType(0, GameMode::class.java)!!)
        })

        // --- Inventory and Items ---

        runtime.registerFunction("quetiapine:bukkit", "createInventory", -1, NativeCallable { ctx: FunctionContext<Any?>? ->
            val owner = ctx!!.getArgumentByType(0, InventoryHolder::class.java)
            when (ctx!!.argumentCount) {
                2 -> {
                    val arg1 = ctx!!.getArgument(1)!!
                    if (arg1 is InventoryType) {
                        Bukkit.createInventory(owner, arg1)
                    } else if (arg1 is Number) {
                        Bukkit.createInventory(owner, arg1.toInt())
                    } else {
                        throw IllegalArgumentException("With 2 arguments, second must be InventoryType or Integer size.")
                    }
                }
                3 -> {
                    val arg1 = ctx!!.getArgument(1)!!
                    val title = when (val arg2 = ctx!!.getArgument(2)) {
                        is String -> mmUtil.deserialize(arg2)
                        is Component -> arg2
                        else -> throw IllegalArgumentException("Title must be a String or Component.")
                    }
                    if (arg1 is InventoryType) {
                        Bukkit.createInventory(owner, arg1, title)
                    } else if (arg1 is Number) {
                        Bukkit.createInventory(owner, arg1.toInt(), title)
                    } else {
                        throw IllegalArgumentException("With 3 arguments, second must be InventoryType or Integer size.")
                    }
                }
                else -> throw IllegalArgumentException("createInventory requires 2 or 3 arguments.")
            }
        })

        runtime.registerFunction("quetiapine:bukkit", "createMerchant", -1, NativeCallable { ctx: FunctionContext<Any?>? ->
            if (ctx!!.argumentCount == 0) {
                Bukkit.createMerchant()
            } else {
                val title = when (val arg0 = ctx!!.getArgument(0)) {
                    is String -> mmUtil.deserialize(arg0)
                    is Component -> arg0
                    null -> null
                    else -> throw IllegalArgumentException("Title must be a String or Component.")
                }
                Bukkit.createMerchant(title)
            }
        })

        runtime.registerFunction("quetiapine:bukkit", "getItemFactory", 0, NativeCallable { ctx: FunctionContext<Any?>? -> Bukkit.getItemFactory() })

        // --- Server State & Reloading ---

        runtime.registerFunction("quetiapine:bukkit", "reload", 0, NativeCallable { ctx: FunctionContext<Any?>? -> Bukkit.reload() })
        runtime.registerFunction("quetiapine:bukkit", "reloadData", 0, NativeCallable { ctx: FunctionContext<Any?>? -> Bukkit.reloadData() })
        runtime.registerFunction("quetiapine:bukkit", "updateResources", 0, NativeCallable { ctx: FunctionContext<Any?>? -> Bukkit.updateResources() }) // Paper API
        runtime.registerFunction("quetiapine:bukkit", "savePlayers", 0, NativeCallable { ctx: FunctionContext<Any?>? -> Bukkit.savePlayers() })
        runtime.registerFunction("quetiapine:bukkit", "isPrimaryThread", 0, NativeCallable { ctx: FunctionContext<Any?>? -> Bukkit.isPrimaryThread() })
        runtime.registerFunction("quetiapine:bukkit", "isStopping", 0, NativeCallable { ctx: FunctionContext<Any?>? -> Bukkit.isStopping() }) // Paper API

        // --- MOTD and Server Icon ---

        runtime.registerFunction("quetiapine:bukkit", "getMotd", 0, NativeCallable { ctx: FunctionContext<Any?>? -> Bukkit.getMotd() }) // Deprecated
        runtime.registerFunction("quetiapine:bukkit", "setMotd", 1, NativeCallable { ctx: FunctionContext<Any?>? -> Bukkit.setMotd(ctx!!.getString(0)!!) }) // Deprecated
        runtime.registerFunction("quetiapine:bukkit", "motd", -1, NativeCallable { ctx: FunctionContext<Any?>? -> // Paper API
            if (ctx!!.argumentCount == 0) {
                Bukkit.motd()
            } else {
                val motd = when (val arg0 = ctx!!.getArgument(0)) {
                    is String -> mmUtil.deserialize(arg0)
                    is Component -> arg0
                    else -> throw IllegalArgumentException("MOTD must be a String or Component.")
                }
                Bukkit.motd(motd)
                null
            }
        })
        runtime.registerFunction("quetiapine:bukkit", "getShutdownMessage", 0, NativeCallable { ctx: FunctionContext<Any?>? -> Bukkit.getShutdownMessage() }) // Deprecated
        runtime.registerFunction("quetiapine:bukkit", "shutdownMessage", 0, NativeCallable { ctx: FunctionContext<Any?>? -> Bukkit.shutdownMessage() }) // Paper API
        runtime.registerFunction("quetiapine:bukkit", "getServerIcon", 0, NativeCallable { ctx: FunctionContext<Any?>? -> Bukkit.getServerIcon() })
        runtime.registerFunction("quetiapine:bukkit", "loadServerIcon", 1, NativeCallable { ctx: FunctionContext<Any?>? ->
            val file = ctx!!.getArgumentByType(0, File::class.java)!!
            Bukkit.loadServerIcon(file)
        })

        // --- Boss Bars ---

        runtime.registerFunction("quetiapine:bukkit", "createBossBar", -1, NativeCallable { ctx: FunctionContext<Any?>? ->
            when (ctx!!.argumentCount) {
                3, 4 -> { // (title, color, style, ...flags)
                    val title = ctx.getString(0)
                    val color = ctx!!.getArgumentByType(1, BarColor::class.java)!!
                    val style = ctx!!.getArgumentByType(2, BarStyle::class.java)!!
                    val flags = if (ctx!!.hasArgument(3)) ctx!!.getArgumentByType(3, Array<BarFlag>::class.java)!! else emptyArray()
                    Bukkit.createBossBar(title, color, style, *flags)
                }
                4, 5 -> { // (key, title, color, style, ...flags)
                    if (ctx!!.getArgument(0) is NamespacedKey) {
                        val key = ctx!!.getArgumentByType(0, NamespacedKey::class.java)!!
                        val title = ctx!!.getString(1)
                        val color = ctx!!.getArgumentByType(2, BarColor::class.java)!!
                        val style = ctx!!.getArgumentByType(3, BarStyle::class.java)!!
                        val flags = if (ctx!!.hasArgument(4)) ctx!!.getArgumentByType(4, Array<BarFlag>::class.java)!! else emptyArray()
                        Bukkit.createBossBar(key, title, color, style, *flags)
                    } else {
                        throw IllegalArgumentException("Invalid arguments for createBossBar.")
                    }
                }
                else -> throw IllegalArgumentException("Invalid argument count for createBossBar.")
            }
        })
        runtime.registerFunction("quetiapine:bukkit", "getBossBars", 0, NativeCallable { ctx: FunctionContext<Any?>? -> Bukkit.getBossBars() })
        runtime.registerFunction("quetiapine:bukkit", "getBossBar", 1, NativeCallable { ctx: FunctionContext<Any?>? ->
            Bukkit.getBossBar(ctx!!.getArgumentByType(0, NamespacedKey::class.java)!!)
        })
        runtime.registerFunction("quetiapine:bukkit", "removeBossBar", 1, NativeCallable { ctx: FunctionContext<Any?>? ->
            Bukkit.removeBossBar(ctx!!.getArgumentByType(0, NamespacedKey::class.java)!!)
        })

        // --- BlockData ---

        runtime.registerFunction("quetiapine:bukkit", "createBlockData", -1, NativeCallable { ctx: FunctionContext<Any?>? ->
            when(val arg0 = ctx!!.getArgument(0)) {
                is Material -> {
                    if (ctx!!.argumentCount == 1) {
                        Bukkit.createBlockData(arg0)
                    } else {
                        when(val arg1 = ctx!!.getArgument(1)) {
                            is Consumer<*> -> Bukkit.createBlockData(arg0, arg1 as Consumer<BlockData>)
                            is String -> Bukkit.createBlockData(arg0, arg1)
                            else -> throw IllegalArgumentException("Second argument for createBlockData must be a Consumer or String.")
                        }
                    }
                }
                is String -> Bukkit.createBlockData(arg0)
                else -> throw IllegalArgumentException("First argument for createBlockData must be Material or String.")
            }
        })

        // --- Entity and Advancements ---

        runtime.registerFunction("quetiapine:bukkit", "getEntity", 1, NativeCallable { ctx: FunctionContext<Any?>? ->
            Bukkit.getEntity(ctx!!.getArgumentByType(0, UUID::class.java)!!)
        })
        runtime.registerFunction("quetiapine:bukkit", "getAdvancement", 1, NativeCallable { ctx: FunctionContext<Any?>? ->
            Bukkit.getAdvancement(ctx!!.getArgumentByType(0, NamespacedKey::class.java)!!)
        })
        runtime.registerFunction("quetiapine:bukkit", "advancementIterator", 0, NativeCallable { ctx: FunctionContext<Any?>? -> Bukkit.advancementIterator() })
        runtime.registerFunction("quetiapine:bukkit", "selectEntities", 2, NativeCallable { ctx: FunctionContext<Any?>? ->
            val sender = ctx!!.getArgumentByType(0, CommandSender::class.java)!!
            val selector = ctx!!.getString(1)!!
            Bukkit.selectEntities(sender, selector)
        })

        // --- TPS and Ticks ---

        runtime.registerFunction("quetiapine:bukkit", "getTPS", 0, NativeCallable { ctx: FunctionContext<Any?>? -> Bukkit.getTPS() }) // Paper API
        runtime.registerFunction("quetiapine:bukkit", "getAverageTickTime", 0, NativeCallable { ctx: FunctionContext<Any?>? -> Bukkit.getAverageTickTime() }) // Paper API
        runtime.registerFunction("quetiapine:bukkit", "getCurrentTick", 0, NativeCallable { ctx: FunctionContext<Any?>? -> Bukkit.getCurrentTick() }) // Paper API

        // --- Miscellaneous ---

        runtime.registerFunction("quetiapine:bukkit", "getLogger", 0, NativeCallable { ctx: FunctionContext<Any?>? -> Bukkit.getLogger() })
        runtime.registerFunction("quetiapine:bukkit", "getScoreboardManager", 0, NativeCallable { ctx: FunctionContext<Any?>? -> Bukkit.getScoreboardManager() })
        runtime.registerFunction("quetiapine:bukkit", "getStructureManager", 0, NativeCallable { ctx: FunctionContext<Any?>? -> Bukkit.getStructureManager() })
        runtime.registerFunction("quetiapine:bukkit", "getLootTable", 1, NativeCallable { ctx: FunctionContext<Any?>? ->
            Bukkit.getLootTable(ctx!!.getArgumentByType(0, NamespacedKey::class.java)!!)
        })
        runtime.registerFunction("quetiapine:bukkit", "getWarningState", 0, NativeCallable { ctx: FunctionContext<Any?>? -> Bukkit.getWarningState() })
        // --- Folia Schedulers (Paper API) ---
        // These will only work on a Folia server. On other servers, they might throw ClassNotFoundException if not handled.
        // It's good practice to check for the server type before registering these.
        try {
            runtime.registerFunction("quetiapine:bukkit", "getRegionScheduler", 0, NativeCallable { ctx: FunctionContext<Any?>? -> Bukkit.getRegionScheduler() })
            runtime.registerFunction("quetiapine:bukkit", "getAsyncScheduler", 0, NativeCallable { ctx: FunctionContext<Any?>? -> Bukkit.getAsyncScheduler() })
            runtime.registerFunction("quetiapine:bukkit", "getGlobalRegionScheduler", 0, NativeCallable { ctx: FunctionContext<Any?>? -> Bukkit.getGlobalRegionScheduler() })

            // isOwnedByCurrentRegion (overloaded)
            runtime.registerFunction("quetiapine:bukkit", "isOwnedByCurrentRegion", -1, NativeCallable { ctx: FunctionContext<Any?>? ->
                when (val arg0 = ctx!!.getArgument(0)) {
                    is Location -> {
                        if (ctx!!.argumentCount == 1) Bukkit.isOwnedByCurrentRegion(arg0)
                        else Bukkit.isOwnedByCurrentRegion(arg0, ctx!!.getNumber(1).toInt())
                    }
                    is Entity -> Bukkit.isOwnedByCurrentRegion(arg0)
                    is org.bukkit.block.Block -> Bukkit.isOwnedByCurrentRegion(arg0)
                    is World -> {
                        val world = arg0
                        if (ctx!!.argumentCount == 3) {
                            Bukkit.isOwnedByCurrentRegion(world, ctx!!.getNumber(1).toInt(), ctx!!.getNumber(2).toInt())
                        } else if (ctx!!.argumentCount == 4) {
                            Bukkit.isOwnedByCurrentRegion(world, ctx!!.getNumber(1).toInt(), ctx!!.getNumber(2).toInt(), ctx!!.getNumber(3).toInt())
                        } else if (ctx!!.argumentCount == 5) {
                            Bukkit.isOwnedByCurrentRegion(world, ctx!!.getNumber(1).toInt(), ctx!!.getNumber(2).toInt(), ctx!!.getNumber(3).toInt(), ctx!!.getNumber(4).toInt())
                        } else {
                            throw IllegalArgumentException("Invalid argument count for isOwnedByCurrentRegion with World.")
                        }
                    }
                    // Could add io.papermc.paper.math.Position if needed
                    else -> throw IllegalArgumentException("Unsupported type for isOwnedByCurrentRegion: ${arg0?.javaClass?.name}")
                }
            })

            runtime.registerFunction("quetiapine:bukkit", "isGlobalTickThread", 0, NativeCallable { ctx: FunctionContext<Any?>? -> Bukkit.isGlobalTickThread() })
        } catch (e: NoClassDefFoundError) {
            // Not a Folia server, skipping Folia-specific functions.
            Bukkit.getLogger().info("[Fluxon] Folia API not found, skipping related function registration.")
        }


        // --- Deprecated or Unsafe Methods ---

        // Note: Use of these functions is highly discouraged.
        runtime.registerFunction("quetiapine:bukkit", "getUnsafe", 0, NativeCallable { ctx: FunctionContext<Any?>? -> Bukkit.getUnsafe() }) // Deprecated since 1.7.2

        // @Deprecated Spigot method
        // runtime.registerFunction("quetiapine:bukkit", "spigot", 0, NativeCallable { ctx: FunctionContext<Any?>? -> Bukkit.spigot() })

        // @Deprecated spawn limit related methods (since 1.18.1)
        runtime.registerFunction("quetiapine:bukkit", "getTicksPerAnimalSpawns", 0, NativeCallable { ctx: FunctionContext<Any?>? -> Bukkit.getTicksPerAnimalSpawns() })
        runtime.registerFunction("quetiapine:bukkit", "getTicksPerMonsterSpawns", 0, NativeCallable { ctx: FunctionContext<Any?>? -> Bukkit.getTicksPerMonsterSpawns() })
        runtime.registerFunction("quetiapine:bukkit", "getTicksPerWaterSpawns", 0, NativeCallable { ctx: FunctionContext<Any?>? -> Bukkit.getTicksPerWaterSpawns() })
        runtime.registerFunction("quetiapine:bukkit", "getTicksPerWaterAmbientSpawns", 0, NativeCallable { ctx: FunctionContext<Any?>? -> Bukkit.getTicksPerWaterAmbientSpawns() })
        runtime.registerFunction("quetiapine:bukkit", "getTicksPerAmbientSpawns", 0, NativeCallable { ctx: FunctionContext<Any?>? -> Bukkit.getTicksPerAmbientSpawns() })
        runtime.registerFunction("quetiapine:bukkit", "getMonsterSpawnLimit", 0, NativeCallable { ctx: FunctionContext<Any?>? -> Bukkit.getMonsterSpawnLimit() })
        runtime.registerFunction("quetiapine:bukkit", "getAnimalSpawnLimit", 0, NativeCallable { ctx: FunctionContext<Any?>? -> Bukkit.getAnimalSpawnLimit() })
        runtime.registerFunction("quetiapine:bukkit", "getWaterAnimalSpawnLimit", 0, NativeCallable { ctx: FunctionContext<Any?>? -> Bukkit.getWaterAnimalSpawnLimit() })
        runtime.registerFunction("quetiapine:bukkit", "getAmbientSpawnLimit", 0, NativeCallable { ctx: FunctionContext<Any?>? -> Bukkit.getAmbientSpawnLimit() })

        // @Deprecated createPlayerProfile (since 1.18.1)
        runtime.registerFunction("quetiapine:bukkit", "createPlayerProfile", -1, NativeCallable { ctx: FunctionContext<Any?>? ->
            when(ctx!!.argumentCount) {
                1 -> when(val arg0 = ctx!!.getArgument(0)) {
                    is UUID -> Bukkit.createPlayerProfile(arg0)
                    is String -> Bukkit.createPlayerProfile(arg0)
                    else -> throw IllegalArgumentException("Argument for createPlayerProfile must be UUID or String.")
                }
                2 -> {
                    val uuid = ctx!!.getArgumentByType(0, UUID::class.java)
                    val name = ctx!!.getString(1)
                    Bukkit.createPlayerProfile(uuid, name)
                }
                else -> throw IllegalArgumentException("Invalid argument count for createPlayerProfile.")
            }
        })

        // @Deprecated getRegistry (since 1.20.6)
        runtime.registerFunction("quetiapine:bukkit", "getRegistry", 1, NativeCallable { ctx: FunctionContext<Any?>? ->
            val clazz = ctx!!.getArgument(0) as Class<out Keyed>
            Bukkit.getRegistry(clazz)
        })

        // @Deprecated permissionMessage (Paper)
        runtime.registerFunction("quetiapine:bukkit", "getPermissionMessage", 0, NativeCallable { ctx: FunctionContext<Any?>? -> Bukkit.getPermissionMessage() })


        // --- Tags ---

        runtime.registerFunction("quetiapine:bukkit", "getTag", 3, NativeCallable { ctx: FunctionContext<Any?>? ->
            val registry = ctx!!.getString(0)!!
            val tagKey = ctx!!.getArgumentByType(1, NamespacedKey::class.java)!!
            val clazz = ctx!!.getArgument(2) as Class<out Keyed>
            Bukkit.getTag(registry, tagKey, clazz)
        })

        runtime.registerFunction("quetiapine:bukkit", "getTags", 2, NativeCallable { ctx: FunctionContext<Any?>? ->
            val registry = ctx!!.getString(0)!!
            val clazz = ctx!!.getArgument(1) as Class<out Keyed>
            Bukkit.getTags(registry, clazz)
        })

        // --- Paper-specific Profile Creation ---
        // These create com.destroystokyo.paper.profile.PlayerProfile, not org.bukkit.profile.PlayerProfile
        runtime.registerFunction("quetiapine:bukkit", "createProfile", -1, NativeCallable { ctx: FunctionContext<Any?>? ->
            when(ctx!!.argumentCount) {
                1 -> when(val arg0 = ctx!!.getArgument(0)) {
                    is UUID -> Bukkit.createProfile(arg0)
                    is String -> Bukkit.createProfile(arg0)
                    else -> throw IllegalArgumentException("Argument for createProfile must be UUID or String.")
                }
                2 -> {
                    val uuid = ctx!!.getArgumentByType(0, UUID::class.java)
                    val name = ctx!!.getString(1)
                    Bukkit.createProfile(uuid, name)
                }
                else -> throw IllegalArgumentException("Invalid argument count for createProfile.")
            }
        })
        runtime.registerFunction("quetiapine:bukkit", "createProfileExact", 2, NativeCallable { ctx: FunctionContext<Any?>? ->
            val uuid = ctx!!.getArgumentByType(0, UUID::class.java)
            val name = ctx!!.getString(1)
            Bukkit.createProfileExact(uuid, name)
        })

        // --- Remaining Paper API functions ---
        runtime.registerFunction("quetiapine:bukkit", "reloadPermissions", 0, NativeCallable { ctx: FunctionContext<Any?>? -> Bukkit.reloadPermissions() })
        runtime.registerFunction("quetiapine:bukkit", "reloadCommandAliases", 0, NativeCallable { ctx: FunctionContext<Any?>? -> Bukkit.reloadCommandAliases() })
        runtime.registerFunction("quetiapine:bukkit", "suggestPlayerNamesWhenNullTabCompletions", 0, NativeCallable { ctx: FunctionContext<Any?>? -> Bukkit.suggestPlayerNamesWhenNullTabCompletions() })
        runtime.registerFunction("quetiapine:bukkit", "permissionMessage", 0, NativeCallable { ctx: FunctionContext<Any?>? -> Bukkit.permissionMessage() })
        runtime.registerFunction("quetiapine:bukkit", "getMobGoals", 0, NativeCallable { ctx: FunctionContext<Any?>? -> Bukkit.getMobGoals() })
        runtime.registerFunction("quetiapine:bukkit", "getDatapackManager", 0, NativeCallable { ctx: FunctionContext<Any?>? -> Bukkit.getDatapackManager() })
        runtime.registerFunction("quetiapine:bukkit", "getPotionBrewer", 0, NativeCallable { ctx: FunctionContext<Any?>? -> Bukkit.getPotionBrewer() })

    }
}