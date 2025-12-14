package io.github.zzzyyylllty.quetiapine

import io.github.zzzyyylllty.quetiapine.logger.*
import io.github.zzzyyylllty.quetiapine.util.QuetiapineLocalDependencyHelper
import io.github.zzzyyylllty.quetiapine.util.dependencies
import io.github.zzzyyylllty.quetiapine.util.devLog
import org.bukkit.command.CommandSender
import org.graalvm.polyglot.Source
import org.tabooproject.fluxon.runtime.FluxonRuntime
import taboolib.common.LifeCycle
import taboolib.common.env.RuntimeEnv
import taboolib.common.platform.Awake
import taboolib.common.platform.Plugin
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common.platform.function.console
import taboolib.common.platform.function.submit
import taboolib.expansion.JexlCompiledScript
import taboolib.expansion.setupPlayerDatabase
import taboolib.module.configuration.Config
import taboolib.module.configuration.Configuration
import taboolib.module.database.getHost
import taboolib.module.kether.KetherShell
import taboolib.module.lang.Language
import taboolib.module.lang.event.PlayerSelectLocaleEvent
import taboolib.module.lang.event.SystemSelectLocaleEvent
import java.io.File
import java.time.format.DateTimeFormatter
import javax.script.CompiledScript


class RuntimeEnv


object Quetiapine : Plugin() {


    @Config("config.yml")
    lateinit var config: Configuration

    val plugin by lazy { this }
    val dataFolder by lazy { nativeDataFolder() }
    val console by lazy { console() }
    val consoleSender by lazy { console.castSafely<CommandSender>()!! }
    val host by lazy { config.getHost("database") }
    val dataSource by lazy { host.createDataSource() }
    var fluxonInst: FluxonRuntime? = null

    var mappings = LinkedHashMap<String, List<String>?>()
    val dateTimeFormatter: DateTimeFormatter by lazy { DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss") }
    var devMode = true
    val ketherScriptCache by lazy { LinkedHashMap<String, KetherShell.Cache?>() }
    val jsScriptCache by lazy { LinkedHashMap<String, CompiledScript?>() }
    val gjsScriptCache by lazy { LinkedHashMap<String, Source?>() }
    val jexlScriptCache by lazy { LinkedHashMap<String, JexlCompiledScript?>() }
    val itemCache by lazy { LinkedHashMap<String, Map<String, Any?>?>() }

    override fun onLoad() {
        fluxonInst = FluxonRuntime.getInstance()
        if (config.getBoolean("database.enable", false)) {
            setupPlayerDatabase(config.getConfigurationSection("database")!!)
        } else {
            setupPlayerDatabase(File("${config.getString("database.filename") ?: "data"}.db"))
        }
    }

    override fun onEnable() {

        infoL("Enable")
        Language.enableSimpleComponent = true
        reloadCustomConfig()

    }

    override fun onDisable() {
        infoL("Disable")
    }
    /*
    fun compat() {
        if (Bukkit.getPluginManager().getPlugin("Chemdah") != null) {
            connectChemdah()
        }
    }*/

    fun reloadCustomConfig(async: Boolean = true) {
        submit(async) {

            config.reload()
            devMode = config.getBoolean("debug",false)

            mappings.clear()

            ketherScriptCache.clear()
            jsScriptCache.clear()
            jexlScriptCache.clear()
            gjsScriptCache.clear()
        }
    }
//
//
//    fun createCustomConfig() {
//        infoL("INTERNAL_INFO_CREATING_CONFIG")
//        try {
//            Configuration.loadFromFile(newFile(getDataFolder(), "placeholders.yml", create = true), Type.YAML)
//            Configuration.loadFromFile(newFile(getDataFolder(), "config.yml", create = true), Type.YAML)
//            infoL("INTERNAL_INFO_CREATED_CONFIG")
//        } catch (e: Exception) {
//            severeL("INTERNAL_SEVERE_CREATE_CONFIG_ERROR")
//            e.printStackTrace()
//        }
//    }


    @SubscribeEvent
    fun lang(event: PlayerSelectLocaleEvent) {
        event.locale = config.getString("lang", "zh_CN")!!
    }

    @SubscribeEvent
    fun lang(event: SystemSelectLocaleEvent) {
        event.locale = config.getString("lang", "zh_CN")!!
    }


    @Awake(LifeCycle.INIT)
    fun initDependenciesInit() {
        solveDependencies(dependencies)
    }


    fun solveDependencies(dependencies: List<String>,useTaboo: Boolean = false) {
        devLog("Starting loading dependencies...")
        for (name in dependencies) {
            try {
                devLog("Trying to load dependencies from file $name")
                val resource = Quetiapine::class.java.classLoader.getResource("META-INF/dependencies/$name.json")
                if (resource == null) {
                    severeS("Resource META-INF/dependencies/$name.json not found!")
                    continue // 跳过这个依赖文件
                }

                if (useTaboo) RuntimeEnv.ENV_DEPENDENCY.loadFromLocalFile(resource) else QuetiapineLocalDependencyHelper().loadFromLocalFile(resource)

                devLog("Trying to load dependencies from file $name ... DONE.")
            } catch (e: Exception) {
                severeS("Trying to load dependencies from file $name FAILED.")
                severeS("Exception: $e")
                e.printStackTrace()
            }
        }
    }


}
