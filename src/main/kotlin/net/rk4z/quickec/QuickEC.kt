package net.rk4z.quickec

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.plugin.java.JavaPlugin
import java.util.UUID

typealias TaskRunner = (JavaPlugin, Runnable) -> Unit

@Suppress("unused")
class QuickEC : JavaPlugin(), Listener {
    val runTask: TaskRunner = { plugin, runnable -> plugin.server.scheduler.runTask(plugin, runnable) }

    override fun onEnable() {
        server.pluginManager.registerEvents(this, this)
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        if (sender !is Player) {
            sender.sendMessage(LanguageManager.getSysMessage(MessageKey.THIS_COMMAND_IS_ONLY_FOR_PLAYERS))
            return true
        }

        when (command.name.lowercase()) {
            "ec" -> {
                if (args?.getOrNull(0) == "help") {
                    sendHelp(sender)
                    return true
                }
                runTask(this) {
                    if (!sender.hasPermission("quickec.open.ignore_inventory")) {
                        val hasEnderChest = sender.inventory.contains(Material.ENDER_CHEST)
                        if (!hasEnderChest) {
                            sender.sendMessage(LanguageManager.getSysMessage(MessageKey.NO_ENDER_CHEST_IN_INVENTORY))
                            return@runTask
                        }
                    }

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
                        if (args?.get(0)!!.isBlank() || args[0].isEmpty()) {
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

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<out String>?
    ): MutableList<String> {
        if (command.name.lowercase() == "uec") {
            if (args?.size == 1) {
                val players = Bukkit.getOnlinePlayers().map { it.name }
                return players.toMutableList()
            }
        } else if (command.name.lowercase() == "ec") {
            return mutableListOf("help")
        } else {
            return mutableListOf()
        }

        return mutableListOf()
    }

    @EventHandler
    fun onPlayerInteract(event: PlayerInteractEvent) {
        val player = event.player

        // If you're sneaking, you can put the ender chest.
        if (player.inventory.itemInMainHand.type == Material.ENDER_CHEST && !player.isSneaking && event.action.isRightClick) {
            if (player.hasPermission("quickec.open.click")) {
                event.isCancelled = true
                player.openInventory(player.enderChest)
            }
        }
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
