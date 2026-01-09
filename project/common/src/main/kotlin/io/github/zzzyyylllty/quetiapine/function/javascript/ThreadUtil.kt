package io.github.zzzyyylllty.quetiapine.function.javascript

import com.github.retrooper.packetevents.protocol.dialog.input.Input
import io.github.zzzyyylllty.quetiapine.item.adapter.transferBooleanToByte
import io.github.zzzyyylllty.quetiapine.item.rebuildLore
import io.github.zzzyyylllty.quetiapine.logger.severeS
import org.bukkit.entity.Player
import org.bukkit.event.Cancellable
import org.bukkit.event.Event
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import taboolib.module.nms.ItemTag
import taboolib.module.nms.getItemTag
import taboolib.module.nms.setItemTag

object ThreadUtil {
    fun sleep(time: Long) {
        Thread.sleep(time)
    }
}