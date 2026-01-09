package io.github.zzzyyylllty.quetiapine.data

import com.sk89q.worldedit.entity.Player
import taboolib.expansion.DataContainer

/**
 * level.
 * */
class PlayerDataManager(val data: DataContainer) {
    fun getLevel(name: String): Long? {
        return data["level.$name"]?.toLong()
    }
    fun getLevelOrDef(name: String): Long? {
        return data["level.$name"]?.toLong() // ?: def
    }
}