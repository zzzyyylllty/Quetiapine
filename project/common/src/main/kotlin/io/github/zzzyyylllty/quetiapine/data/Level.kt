package io.github.zzzyyylllty.quetiapine.data

data class LevelTemplate(
    val id: String,
    val level: Level
)

data class Level(
    val min: Long,
    val start: Long,
    val max: Long,
//    val display = Map<Int, LevelDisplay>
)