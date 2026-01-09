package io.github.zzzyyylllty.quetiapine.util

import org.bukkit.entity.Player
import org.serverct.ersha.AttributePlus

object AttributeHelper {
    fun updateClassAttribute(p: Player) {
        val data = AttributePlus.attributeManager.getAttributeData(p)
        data.takeApiAttribute("Quetiapine_ClassBuff")
    }
}