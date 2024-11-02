package net.rk4z.quickec

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import net.rk4z.s1.pluginBase.LanguageManager
import net.rk4z.s1.pluginBase.PluginEntry
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import net.rk4z.s1.pluginBase.Executor
import java.util.UUID

@Suppress("unused")
class QuickEC : PluginEntry(
    "quickec",
    "net.rk4z.quickec",
    false,
    false,
    null,
    true,
    "BKtFPESp",
    listOf("ja", "en", "zh", "fr", "de", "it", "es", "ko", "zh"),
    true
), Listener {
    override fun onEnablePost() {
        server.pluginManager.registerEvents(this, this)
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage(LanguageManager.getSysMessage(Main.Command.THIS_COMMAND_IS_ONLY_FOR_PLAYERS))
            return true
        }

        when (command.name.lowercase()) {
            "ec" -> {
                if (args.getOrNull(0) == "help") {
                    sendHelp(sender)
                    return true
                }
                Executor.execute {
                    if (!sender.hasPermission("quickec.open.ignore_inventory")) {
                        val hasEnderChest = sender.inventory.contains(Material.ENDER_CHEST)
                        if (!hasEnderChest) {
                            sender.sendMessage(LanguageManager.getSysMessage(Main.Command.NO_ENDER_CHEST_IN_INVENTORY))
                            return@execute
                        }
                    }

                    if (sender.hasPermission("quickec.open.self")) {
                        sender.openInventory(sender.enderChest)
                    } else {
                        sender.sendMessage(LanguageManager.getSysMessage(Main.Message.NO_PERMISSION))
                    }
                }
            }

            "uec" -> {
                if (args.getOrNull(0) == "help") {
                    sendHelp(sender)
                    return true
                }

                if (sender.hasPermission("quickec.open.others")) {
                    Executor.execute {
                        if (args[0].isBlank() || args[0].isEmpty()) {
                            sender.sendMessage(LanguageManager.getSysMessage(Main.Command.NO_PLAYER_PROVIDED))
                            return@execute
                        }

                        val target: Player? = try {
                            if (args[0].matches(Regex("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$"))) {
                                Bukkit.getPlayer(UUID.fromString(args[0]))
                            } else {
                                Bukkit.getPlayer(args[0])
                            }
                        } catch (e: IllegalArgumentException) {
                            sender.sendMessage(LanguageManager.getSysMessage(Main.Message.INVALID_UUID))
                            return@execute
                        }

                        if (target == null) {
                            sender.sendMessage(LanguageManager.getSysMessage(Main.Command.PLAYER_NOT_FOUND))
                            return@execute
                        }

                        sender.openInventory(target.enderChest)
                    }
                } else {
                    sender.sendMessage(LanguageManager.getSysMessage(Main.Message.NO_PERMISSION))
                }
            }
        }
        return true
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<out String>
    ): MutableList<String> {
        if (command.name.lowercase() == "uec") {
            if (args.size == 1) {
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
        val headerComponent = LanguageManager.getMessage(player, Main.Help.HELP_HEADER)
            .color(NamedTextColor.GOLD)
            .decorate(TextDecoration.BOLD)

        val hStartComponent = Component.text("=======").color(NamedTextColor.GOLD)
        val hEndComponent = Component.text("=======").color(NamedTextColor.GOLD)

        val commands = listOf(
            "ec" to Main.Help.EC_COMMAND,
            "uec" to Main.Help.UEC_COMMAND
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
