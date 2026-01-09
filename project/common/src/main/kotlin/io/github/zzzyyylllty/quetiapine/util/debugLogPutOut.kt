package io.github.zzzyyylllty.quetiapine.util

import io.github.zzzyyylllty.quetiapine.logger.debugS
import io.github.zzzyyylllty.quetiapine.Quetiapine.devMode
import taboolib.common.platform.function.submitAsync

fun devLog(input: String) {
    submitAsync { if (devMode) debugS(input) }
}

fun devMode(b: Boolean) {
    devMode = b
}