package io.github.zzzyyylllty.quetiapine.command

import io.github.zzzyyylllty.embiancomponent.EmbianComponent.SafetyComponentSetter
import io.github.zzzyyylllty.embiancomponent.tools.getComponentsNMSFiltered
import io.github.zzzyyylllty.quetiapine.Quetiapine.blockRegenMap
import io.github.zzzyyylllty.quetiapine.Quetiapine.levels
import io.github.zzzyyylllty.quetiapine.Quetiapine.regenTemplates
import io.github.zzzyyylllty.quetiapine.Quetiapine.regenTemplatesByBlock
import io.github.zzzyyylllty.quetiapine.logger.infoS
import org.bukkit.Material
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import taboolib.common.platform.command.CommandBody
import taboolib.common.platform.command.CommandHeader
import taboolib.common.platform.command.PermissionDefault
import taboolib.common.platform.command.bool
import taboolib.common.platform.command.mainCommand
import taboolib.common.platform.command.player
import taboolib.common.platform.command.subCommand
import taboolib.common.platform.function.submitAsync
import taboolib.common.util.asList
import taboolib.expansion.setupDataContainer
import taboolib.module.nms.NMSItemTag.Companion.asNMSCopy
import taboolib.module.nms.getItemTag
import taboolib.platform.util.giveItem
import kotlin.text.get

@CommandHeader(
    name = "quetiapinedebug",
    aliases = ["rpgdebug"],
    permission = "quetiapine.command.debug",
    description = "DEBUG Command of Quetiapine.",
    permissionMessage = "",
    permissionDefault = PermissionDefault.OP,
    newParser = false,
)
object DebugCommand {

    @CommandBody
    val main = mainCommand {
        createModernHelper()
    }

    @CommandBody
    val help = subCommand {
        createModernHelper()
    }

    @CommandBody
    val getBlockRegenMap = subCommand {
        execute<CommandSender> { sender, context, argument ->
            var message = blockRegenMap.toString()
            sender.infoS(message, false)
        }
    }

    @CommandBody
    val getRegenTemplatesByBlock = subCommand {
        execute<CommandSender> { sender, context, argument ->
            var message = regenTemplatesByBlock.toString()
            sender.infoS(message, false)
        }
    }

    @CommandBody
    val getRegenTemplates = subCommand {
        execute<CommandSender> { sender, context, argument ->
            var message = regenTemplates.toString()
            sender.infoS(message, false)
        }
    }

    @CommandBody
    val getLevels = subCommand {
        execute<CommandSender> { sender, context, argument ->
            var message = levels.toString()
            sender.infoS(message, false)
        }
    }
}