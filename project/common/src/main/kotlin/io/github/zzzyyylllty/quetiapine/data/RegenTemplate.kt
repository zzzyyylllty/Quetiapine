package io.github.zzzyyylllty.quetiapine.data

import io.github.zzzyyylllty.quetiapine.Quetiapine.regenTemplates
import io.github.zzzyyylllty.quetiapine.function.kether.parseKether
import io.github.zzzyyylllty.quetiapine.listener.blockAdapters
import io.github.zzzyyylllty.quetiapine.util.loc
import net.momirealms.craftengine.bukkit.api.CraftEngineBlocks
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.block.data.BlockData
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

data class Regen(
    val templateID: String,
    val end: Long,
    val location: Location,
    val block: RegenBlock,
    val blockData: BlockData,
) {
    val template
    get() = regenTemplates[templateID]

    fun update() {
        blockAdapters[block.adapter]?.place(blockData, location, block.block)
    }
}

data class RegenTemplate(
    val id: String,
    val block: RegenBlock,
    val postCondition: Condition?,
    val condition: Condition?,
    val breakCondition: Condition?,
    val period: Double?,
    val tempBlock: RegenBlock?,
    val vanilla: Boolean,
    val items: List<RegenItem>,
    val agents: Agents?
)

data class RegenBlock(
    val adapter: String,
    val block: String,
)

data class RegenItem(
    val item: OmniItem,
    val amount: Any?,
    val condition: Condition?
) {
    fun build(player: Player): ItemStack? {
        val nAmount = if (amount is Int || amount is Double) amount.toInt() else amount.toString().parseKether(player)
        return if (condition?.validate(mapOf("item" to item, "amount" to nAmount), player) ?: true) item.build(player) else null
    }
}