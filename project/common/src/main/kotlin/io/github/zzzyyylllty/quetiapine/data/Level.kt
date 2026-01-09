package io.github.zzzyyylllty.quetiapine.data

import io.github.zzzyyylllty.quetiapine.function.fluxon.FluxonShell
import io.github.zzzyyylllty.quetiapine.function.kether.evalKetherValue
import io.github.zzzyyylllty.quetiapine.util.fixedCalculator
import org.bukkit.entity.Player
import org.tabooproject.fluxon.parser.expression.MapExpression

/**
 * Represents the top-level configuration for a single activity/level system.
 * Corresponds to the 'activity' block in the YAML.
 * @property id The unique identifier for this level system (e.g., "activity").
 * @property display A map of display names for the activity (e.g., "default", "legacy").
 * @property level The detailed configuration for levels within this activity.
 */
data class LevelTemplate(
    val id: String,
    val display: Map<String, String>, // Top-level activity display names
    val level: Level
)

/**
 * Represents the core level configuration for an activity.
 * Corresponds to the 'level' block in the YAML.
 * @property min The minimum possible level.
 * @property start The starting level for new players.
 * @property max The maximum possible level. Can be a number (as string) or a dynamic expression (as string), or null for unlimited.
 * @property display A map where keys are level thresholds and values define how the level is displayed.
 * @property expMap A linked hash map where keys are level thresholds and values define the experience required to reach that level.
 */
data class Level(
    val min: Long,
    val start: Long,
    val max: String?, // Can be a fixed number (as string), an expression, or null for unlimited
    val display: Map<Long, LevelDisplayConfig>, // LevelDisplayConfig allows for default and complex displays
    val expMap: LinkedHashMap<Long, Exp> // Preserves order for threshold processing
) {
    /**
     * Retrieves the required experience to level up from the current level.
     * Searches the expMap for the highest threshold less than or equal to the target level.
     * @param player The player context, can be null if not needed for calculation.
     * @param level The target level for which to get the required experience.
     * @return The required experience as a Double.
     * @throws IllegalStateException If no experience configuration is found for the given level.
     */
    fun getRequiredExp(player: Player?, level: Long): Double {
        var lastExpEntry: Map.Entry<Long, Exp>? = null
        for (expEntry in expMap.entries) {
            if (expEntry.key <= level) {
                lastExpEntry = expEntry
            } else {
                break
            }
        }

        val targetExp = lastExpEntry?.value
            ?: throw IllegalStateException("No experience configuration found for level $level. Please check the 'exp' section in your YAML.")

        return targetExp.getValue(player, level)
    }

    /**
     * Gets the display string for a specific level.
     * Prioritizes complex display if available, otherwise falls back to default.
     * @param level The level for which to get the display string.
     * @return The formatted display string for the level.
     */
    fun getLevelDisplay(level: Long): String {
        var lastDisplayConfig: Map.Entry<Long, LevelDisplayConfig>? = null
        for (displayEntry in display.entries) {
            if (displayEntry.key <= level) {
                lastDisplayConfig = displayEntry
            } else {
                break
            }
        }
        return lastDisplayConfig?.value?.render(level) ?: ""
    }
}

/**
 * Interface for all experience calculation types.
 * @property type The type of experience calculation (e.g., "calculator", "fixed").
 * @property value The raw string value used for the calculation.
 */
interface Exp {
    val type: String
    val value: String

    /**
     * Calculates the experience value.
     * @param player The player context (can be null if not needed for calculation).
     * @param level The current level.
     * @return The calculated experience as a Double.
     */
    fun getValue(player: Player?, level: Long): Double
}

/**
 * Experience calculation using a fixed formula string.
 */
data class CalculatorExp(override val type: String = "calculator", override val value: String) : Exp {
    override fun getValue(player: Player?, level: Long): Double {
        return fixedCalculator.evaluate(value.replace("%level%", level.toString()))
    }
}

/**
 * Experience calculation using a fixed numerical value.
 */
data class FixedExp(override val type: String = "fixed", override val value: String) : Exp {
    override fun getValue(player: Player?, level: Long): Double {
        return value.replace("%level%", level.toString()).toDoubleOrNull()
            ?: throw IllegalArgumentException("FixedExp value '$value' for level $level is not a valid number.")
    }
}

/**
 * Experience calculation using a JavaScript expression.
 */
data class JavascriptExp(override val type: String = "javascript", override val value: String) : Exp {
    override fun getValue(player: Player?, level: Long): Double {
        val localEngine = taboolib.common5.scriptEngineFactory.scriptEngine
        return localEngine.eval(value.replace("%level%", level.toString())).toString().toDouble()
    }
}

/**
 * Experience calculation using Kether expressions (TabooLib's expression engine).
 */
data class KetherExp(override val type: String = "kether", override val value: String) : Exp {
    override fun getValue(player: Player?, level: Long): Double {
        return value.replace("%level%", level.toString()).evalKetherValue(player).toString().toDouble()
    }
}

/**
 * Experience calculation using Fluxon expressions.
 */
data class FluxonExp(override val type: String = "fluxon", override val value: String) : Exp {
    override fun getValue(player: Player?, level: Long): Double {
        return FluxonShell.invoke(value.replace("%level%", level.toString())).toString().toDouble()
    }
}


/**
 * Represents the display configuration for a specific level threshold.
 * A level can have both a default fixed display and a complex symbol-based display.
 * Corresponds to an entry under 'level.display.X' in the YAML.
 * @property default The fixed display string for the level, can be null.
 * @property complex The complex (symbol-based) display configuration, can be null.
 */
data class LevelDisplayConfig(
    val default: String?,
    val complex: ComplexLevelDisplay?
) {
    /**
     * Renders the appropriate level display.
     * Prioritizes the complex display if available, otherwise falls back to the default fixed string.
     * @param level The current level to be displayed.
     * @return The formatted display string.
     */
    fun render(level: Long): String {
        return complex?.render(level) // Try complex display first
            ?: default?.replace("%level%", level.toString()) // Fallback to default, replacing %level%
            ?: "" // Empty string if neither is defined
    }
}

/**
 * Represents a complex (symbol-based) level display configuration.
 * Corresponds to the 'complex' block within a level display entry.
 * @property type The type of complex display, usually "symbol".
 * @property sort The sorting method for symbols, "greater" (descending threshold) or "smaller" (ascending threshold).
 * @property symbols A map where keys are symbol thresholds and values are the symbol definitions.
 */
data class ComplexLevelDisplay(
    val type: String = "symbol", // Should always be "symbol" for this display type
    val sort: String, // "greater" or "smaller"
    val symbols: Map<Long, SymbolEntry>
) {
    /**
     * Renders the complex symbol-based level display.
     * This method calculates which symbols to display based on the level and sort order.
     * @param level The current level to determine symbol composition.
     * @return The formatted symbol display string.
     */
    fun render(level: Long): String {
        val stringBuilder = StringBuilder()
        var currentLevel = level

        val sortedSymbols = if (sort == "greater") {
            // Process larger thresholds first (e.g., 256, 64, 16...)
            symbols.entries.sortedByDescending { it.key }
        } else {
            // Process smaller thresholds first (e.g., 1, 4, 16...) - less common for additive symbols
            symbols.entries.sortedBy { it.key }
        }

        for ((threshold, symbolEntry) in sortedSymbols) {
            if (threshold <= 0) continue // '0' threshold symbol is typically a default part, handled later or implicitly

            if (currentLevel >= threshold) {
                val count = (currentLevel / threshold).toInt()
                if (count > 0) {
                    stringBuilder.append(symbolEntry.start ?: symbolEntry.part)
                    repeat(count - 1) { // Append 'part' (count - 1) times as 'start' or first 'part' is already there
                        stringBuilder.append(symbolEntry.part)
                    }
                    currentLevel %= threshold // Update remaining level
                }
            }
        }

        // Handle the '0' threshold symbol for any remaining level or as a default filler.
        // This assumes '0' represents a basic unit or filler.
        val defaultSymbol = symbols[0L]
        if (currentLevel > 0 && defaultSymbol != null) {
            // If there's remaining level and a default symbol, append it
            stringBuilder.append(defaultSymbol.start ?: defaultSymbol.part)
            repeat((currentLevel - 1).toInt()) {
                stringBuilder.append(defaultSymbol.part)
            }
        } else if (stringBuilder.isEmpty() && defaultSymbol != null && level == 0L) {
            // If level is 0 and no other symbols were added, but a default exists, show it once
            stringBuilder.append(defaultSymbol.start ?: defaultSymbol.part)
        }

        return stringBuilder.toString()
    }
}

/**
 * Represents a single symbol definition for a complex level display.
 * Corresponds to an entry under 'symbols' in a ComplexLevelDisplay.
 * @property start An optional string to use for the first instance of this symbol.
 * @property part The string to use for subsequent instances of this symbol.
 */
data class SymbolEntry(
    val start: String?,
    val part: String
)