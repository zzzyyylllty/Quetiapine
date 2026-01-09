package io.github.zzzyyylllty.quetiapine.data.load


// import org.yaml.snakeyaml.Yaml
import io.github.zzzyyylllty.quetiapine.Quetiapine.config
import io.github.zzzyyylllty.quetiapine.data.*
import io.github.zzzyyylllty.quetiapine.data.LevelTemplate
import io.github.zzzyyylllty.quetiapine.logger.infoL
import io.github.zzzyyylllty.quetiapine.logger.severeS
import io.github.zzzyyylllty.quetiapine.logger.warningL
import io.github.zzzyyylllty.quetiapine.util.ExternalItemHelper
import io.github.zzzyyylllty.quetiapine.util.devLog
import io.github.zzzyyylllty.quetiapine.util.toBooleanTolerance
import taboolib.common.platform.function.getDataFolder
import taboolib.common.platform.function.releaseResourceFile
import taboolib.common5.compileJS
import java.io.File
import java.util.LinkedHashMap
import kotlin.String
import kotlin.collections.forEach
import kotlin.collections.set

fun loadLevelFiles() {
    infoL("LevelBlockLoad")
    if (!File(getDataFolder(), "Level").exists()) {
        warningL("LevelBlockLevel")
        releaseResourceFile("Level/iron.yml")
    }
    val files = File(getDataFolder(), "Level").listFiles()
    for (file in files) {
        // If directory load file in it...
        if (file.isDirectory) file.listFiles()?.forEach {
            loadLevelFile(it)
        }
        else loadLevelFile(file)
    }
}
fun loadLevelFile(file: File) {
    devLog("Loading file ${file.name}")

    if (file.isDirectory) file.listFiles()?.forEach {
        loadLevelFile(it)
    } else {
        if (!checkRegexMatch(file.name, (config["file-load.Level"] ?: ".*").toString())) {
            devLog("${file.name} not match regex, skipping...")
            return
        }
        val map = multiExtensionLoader(file)
        if (map != null) for (it in map.entries) {
            val key = it.key
            val value = map[key] ?: continue
            (value as Map<String, Any?>?)?.let { arg -> loadLevel(key, arg) }
        } else {
            devLog("Map is null, skipping.")
        }
    }
}
fun loadLevel(key: String, arg: Map<String, Any?>): LevelTemplate? {

    val displayMap = arg["display"] as? Map<String, String> ?: run {
        severeS("Issue found in Level '$key': 'display' section is missing or malformed. Using an empty map.")
        emptyMap()
    }

    val rawLevelMap = arg["level"] as? Map<String, Any> ?: run {
        severeS("Issue found in Level '$key': 'level' section is missing or malformed. Cannot create LevelTemplate.")
        return null // 'level' is core configuration, cannot proceed without it
    }

    // --- Parsing Level's basic properties ---
    val minLevel = (rawLevelMap["min"] as? Number)?.toLong() ?: 0L // Default to 0
    val startLevel = (rawLevelMap["start"] as? Number)?.toLong() ?: 0L // Default to 0
    val maxLevel = rawLevelMap["max"] as? String // max can be null

    // --- Parsing level.display (Map<Long, LevelDisplayConfig>) ---
    val rawLevelDisplayMap = rawLevelMap["display"] as? Map<String, Any> ?: emptyMap()
    val levelDisplayConfigMap = rawLevelDisplayMap.entries.mapNotNull { (thresholdStr, displayConfigRaw) ->
        val threshold = thresholdStr.toLongOrNull() ?: run {
            severeS("Issue found in Level '$key': Invalid level display threshold '$thresholdStr'. Skipping entry.")
            return@mapNotNull null
        }

        val displayConfigData = displayConfigRaw as? Map<String, Any> ?: run {
            severeS("Issue found in Level '$key': Malformed level display configuration for threshold '$thresholdStr'. Skipping entry.")
            return@mapNotNull null
        }

        val defaultDisplay = displayConfigData["default"] as? String
        val rawComplexDisplay = displayConfigData["complex"] as? Map<String, Any>

        val complexLevelDisplay = if (rawComplexDisplay != null) {
            val complexType = rawComplexDisplay["type"] as? String ?: "symbol" // Default type
            val complexSort = rawComplexDisplay["sort"] as? String ?: "greater" // Default sort order
            val rawSymbols = rawComplexDisplay["symbols"] as? Map<String, Any> ?: emptyMap()

            val symbolEntries = rawSymbols.entries.mapNotNull { (symbolThresholdStr, symbolEntryRaw) ->
                val symbolThreshold = symbolThresholdStr.toLongOrNull() ?: run {
                    severeS("Issue found in Level '$key', complex display: Invalid symbol threshold '$symbolThresholdStr'. Skipping symbol entry.")
                    return@mapNotNull null
                }
                val symbolEntryData = symbolEntryRaw as? Map<String, String> ?: run {
                    severeS("Issue found in Level '$key', complex display: Malformed symbol entry for threshold '$symbolThresholdStr'. Skipping symbol entry.")
                    return@mapNotNull null
                }
                val symbolStart = symbolEntryData["start"]
                val symbolPart = symbolEntryData["part"] ?: run {
                    severeS("Issue found in Level '$key', complex display: Missing 'part' field for symbol threshold '$symbolThresholdStr'. Skipping symbol entry.")
                    return@mapNotNull null // 'part' is mandatory
                }
                symbolThreshold to SymbolEntry(symbolStart, symbolPart)
            }.toMap() // Convert List<Pair> to Map

            ComplexLevelDisplay(complexType, complexSort, symbolEntries)
        } else {
            null
        }

        threshold to LevelDisplayConfig(defaultDisplay, complexLevelDisplay)
    }.toMap() // Convert List<Pair> to Map

    // --- Parsing level.expMap (LinkedHashMap<Long, Exp>) ---
    val rawExpMap = rawLevelMap["exp"] as? Map<String, Any> ?: emptyMap()
    val expConfigMap = LinkedHashMap<Long, Exp>()

    for ((thresholdStr, expConfigRaw) in rawExpMap.entries) {
        val threshold = thresholdStr.toLongOrNull() ?: run {
            severeS("Issue found in Level '$key': Invalid experience threshold '$thresholdStr'. Skipping experience entry.")
            continue
        }

        val expConfigData = expConfigRaw as? Map<String, String> ?: run {
            severeS("Issue found in Level '$key': Malformed experience configuration for threshold '$thresholdStr'. Skipping experience entry.")
            continue
        }

        val type = expConfigData["type"] ?: run {
            severeS("Issue found in Level '$key': Missing experience type for threshold '$thresholdStr'. Skipping experience entry.")
            continue
        }
        val value = expConfigData["value"] ?: run {
            severeS("Issue found in Level '$key': Missing experience value for threshold '$thresholdStr'. Skipping experience entry.")
            continue
        }

        val expInstance = when (type.lowercase()) {
            "calculator" -> CalculatorExp(value = value)
            "fixed" -> FixedExp(value = value)
            "javascript" -> JavascriptExp(value = value)
            "kether" -> KetherExp(value = value)
            "fluxon" -> FluxonExp(value = value)
            else -> {
                severeS("Issue found in Level '$key': Unknown experience type '$type' for threshold '$thresholdStr'. Skipping experience entry.")
                null
            }
        }
        expInstance?.let {
            expConfigMap[threshold] = it
        }
    }

    // Create Level instance
    val levelInstance = Level(
        min = minLevel,
        start = startLevel,
        max = maxLevel,
        display = levelDisplayConfigMap,
        expMap = expConfigMap
    )

    // Finally, create LevelTemplate instance
    return LevelTemplate(
        id = key,
        display = displayMap,
        level = levelInstance
    )
}