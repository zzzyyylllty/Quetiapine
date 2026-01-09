package io.github.zzzyyylllty.quetiapine.event

import taboolib.platform.type.BukkitProxyEvent

class quetiapineReloadEvent() : BukkitProxyEvent()

/**
 * Modify [defaultData] you can register
 * your custom utils.
 * DO NOT USE clear, re-set or directly modify it, OR OTHER SENSITIVE FUNCTIONS.
 * */
class QuetiapineCustomScriptDataLoadEvent(
    var defaultData: LinkedHashMap<String, Any?>
) : BukkitProxyEvent()
