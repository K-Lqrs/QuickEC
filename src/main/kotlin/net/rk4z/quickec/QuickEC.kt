package net.rk4z.quickec

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import java.util.UUID

typealias TaskRunner = (JavaPlugin, Runnable) -> Unit

@Suppress("unused")
class QuickEC : JavaPlugin() {
    val runTask: TaskRunner = { plugin, runnable -> plugin.server.scheduler.runTask(plugin, runnable) }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        if (sender !is Player) {
            sender.sendMessage(LanguageManager.getSysMessage(MessageKey.THIS_COMMAND_IS_ONLY_FOR_PLAYERS))
            return true
        }

        if (args.isNullOrEmpty()) {
            sender.sendMessage(LanguageManager.getSysMessage(MessageKey.NO_COMMAND_PROVIDED))
            sendHelp(sender)
            return true
        }

        when (command.name.lowercase()) {
            "ec" -> {
                runTask(this) {
                    // Normally, everyone has this permission.
                    // But some server owners might want to restrict it.
                    // So, we'll check for it.
                    if (sender.hasPermission("quickec.open.self")) {
                        sender.openInventory(sender.enderChest)
                    } else {
                        sender.sendMessage(LanguageManager.getSysMessage(MessageKey.NO_PERMISSION))
                    }
                }
            }

            "uec" -> {
                if (sender.hasPermission("quickec.open.others")) {
                    runTask(this) {
                        if (args.isEmpty()) {
                            sender.sendMessage(LanguageManager.getSysMessage(MessageKey.NO_PLAYER_PROVIDED))
                            return@runTask
                        }

                        val target: Player? = try {
                            if (args[0].matches(Regex("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$"))) {
                                Bukkit.getPlayer(UUID.fromString(args[0]))
                            } else {
                                Bukkit.getPlayer(args[0])
                            }
                        } catch (e: IllegalArgumentException) {
                            sender.sendMessage(LanguageManager.getSysMessage(MessageKey.INVALID_UUID))
                            return@runTask
                        }

                        if (target == null) {
                            sender.sendMessage(LanguageManager.getSysMessage(MessageKey.PLAYER_NOT_FOUND))
                            return@runTask
                        }

                        sender.openInventory(target.enderChest)
                    }
                } else {
                    sender.sendMessage(LanguageManager.getSysMessage(MessageKey.NO_PERMISSION))
                }
            }
        }
        return true
    }

    private fun sendHelp(player: Player) {
        val headerComponent = LanguageManager.getMessage(player, MessageKey.HELP_HEADER)
            .color(NamedTextColor.GOLD)
            .decorate(TextDecoration.BOLD)

        val hStartComponent = Component.text("=======").color(NamedTextColor.GOLD)
        val hEndComponent = Component.text("=======").color(NamedTextColor.GOLD)

        val commands = listOf(
            "ec" to MessageKey.EC_COMMAND,
            "uec" to MessageKey.UEC_COMMAND
        )

        player.sendMessage(hStartComponent.append(headerComponent).append(hEndComponent))

        commands.forEach { (command, key) ->
            player.sendMessage(
                Component.text("$command - ").append(LanguageManager.getMessage(player, key))
                    .color(NamedTextColor.GREEN)
            )
        }

        player.sendMessage(Component.text("=======================").color(NamedTextColor.GOLD))
    }
}
