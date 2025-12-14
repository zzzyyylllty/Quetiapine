package io.github.zzzyyylllty.quetiapine.util

import io.github.zzzyyylllty.quetiapine.logger.debugS
import io.github.zzzyyylllty.quetiapine.Quetiapine.devMode

fun devLog(input: String) {
    if (devMode) debugS(input)
}

fun devMode(b: Boolean) {
    devMode = b
}