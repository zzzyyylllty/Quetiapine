package io.github.zzzyyylllty.quetiapine.listener

import com.sk89q.worldguard.util.Locations
import io.github.zzzyyylllty.quetiapine.Quetiapine.blockRegenMap
import io.github.zzzyyylllty.quetiapine.Quetiapine.regenTemplates
import io.github.zzzyyylllty.quetiapine.Quetiapine.regenTemplatesByBlock
import io.github.zzzyyylllty.quetiapine.data.Regen
import io.github.zzzyyylllty.quetiapine.data.RegenBlock
import io.github.zzzyyylllty.quetiapine.data.RegenTemplate
import io.github.zzzyyylllty.quetiapine.util.DependencyHelper
import io.github.zzzyyylllty.quetiapine.util.devLog
import net.momirealms.craftengine.bukkit.api.CraftEngineBlocks
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.block.data.BlockData
import org.bukkit.event.block.BlockBreakEvent
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common.platform.function.submitAsync
import java.util.LinkedList
import kotlin.math.roundToLong
import kotlin.time.Clock

val blockAdapters = LinkedHashMap<String, BlockAdapter>()

@SubscribeEvent
fun onRegenBlockBreak(e: BlockBreakEvent) {
    val player = e.player
    if (player.gameMode == GameMode.CREATIVE) return

    val regen = blockRegenMap[e.block.location]

    // 如果没有刷新直接返回
    if ((regen?.end ?: 0) > System.currentTimeMillis()) return

    // 获取方块ID，出于性能考虑
    val id = getBlockID(e.block)
    devLog("ID $id")
    val templates = regenTemplatesByBlock[id] ?: return
    devLog("templates $templates")

    var template: RegenTemplate? = null

    for (templateID in templates) {
        val cTemplate = regenTemplates[templateID] ?: continue
        devLog("template $cTemplate")

        // Post条件
        if (!(cTemplate.postCondition?.validate(mapOf("event" to e, "block" to e.block), player) ?: true)) {
            devLog("Post-Condition not met, continue")
            continue
        }
        // 条件
        if (cTemplate.condition?.validate(mapOf("event" to e, "block" to e.block), player) ?: true) {
            devLog("Condition met,break")
            template = cTemplate
            break
        }
    }

    if (template == null) {
        devLog("template not found")
        return
    }

    if (!(template.breakCondition?.validate(mapOf("event" to e, "block" to e.block), player) ?: true)) {
        template.agents?.runAgent("onMineDeny", mapOf("event" to e, "block" to e.block), player)
        devLog("Condition not met, return")
        return
    }

    if (!template.vanilla) {
        e.isDropItems = false
    }

    template.items.forEach {
        val item = it.build(player)
        item?.let { e.block.world.dropItemNaturally(e.block.location, it) }
    }

    val data = e.block.blockData.clone()

    submitAsync {
        val time = System.currentTimeMillis() + ((template.period ?: 10.0) * 1000.0).roundToLong()
        blockRegenMap[e.block.location] = Regen(template.id, time, e.block.location, template.block, data)
    }

}

fun getBlockID(block: Block): RegenBlock {
    if (blockAdapters.isNotEmpty()) {
        for (a in blockAdapters) {
            a.value.getID(block)?.let { return it }
        }
    }
    return MCAdapter.getID(block)
}

public interface BlockAdapter {
    fun getID(block: Block): RegenBlock?
    fun place(block: BlockData, location: Location, id: String)
}

object CEAdapter : BlockAdapter {
    override fun getID(block: Block): RegenBlock? {
        return CraftEngineBlocks.getCustomBlockState(block)?.customBlockState()?.ownerId()?.asString()?.let { RegenBlock("craftengine", it) }
    }
    override fun place(block: BlockData, location: Location, id: String) {
        CraftEngineBlocks.getCustomBlockState(block)?.let { CraftEngineBlocks.place(location, it, false) }
    }

}
object MCAdapter : BlockAdapter {
    override fun getID(block: Block): RegenBlock {
        return RegenBlock("minecraft", block.type.name)
    }

    override fun place(block: BlockData, location: Location, id: String) {
        location.world.setBlockData(location, block)
    }
}

@Awake(LifeCycle.ENABLE)
fun registerNativeBlockAdapters() {
    if (DependencyHelper.ce) blockAdapters["craftengine"] = CEAdapter
}


@Awake(LifeCycle.ACTIVE)
fun refreshRegenBlocks() {
    submitAsync(period = 20) {
        for (entry in blockRegenMap) {
            // 如果已经结束
            if (entry.value.end <= System.currentTimeMillis()) {
                devLog("Updating $entry")
                entry.value.update()
                blockRegenMap.remove(entry.key)
            }
        }
    }
}



@Awake(LifeCycle.DISABLE)
fun releaseRegenBlocks() {
    for (entry in blockRegenMap) {
        // 如果已经结束
        devLog("Releasing $entry")
        entry.value.update()
        blockRegenMap.remove(entry.key)
    }
}

