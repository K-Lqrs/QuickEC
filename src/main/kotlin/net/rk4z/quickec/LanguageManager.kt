package net.rk4z.quickec

import net.kyori.adventure.text.Component
import org.bukkit.entity.Player
import java.util.Locale

object LanguageManager {
    private val message: Map<String, Map<MessageKey, String>> = mapOf(
        "ja" to mapOf(
            MessageKey.THIS_COMMAND_IS_ONLY_FOR_PLAYERS to "このコマンドはプレイヤーのみ使用できます",
            MessageKey.NO_COMMAND_PROVIDED to "コマンドが指定されていません。"
        ),

        "en" to mapOf(
            MessageKey.THIS_COMMAND_IS_ONLY_FOR_PLAYERS to "Only players can use this command",
            MessageKey.NO_COMMAND_PROVIDED to "No command provided."
        )
    )

    private fun Player.getLanguage(): String {
        return this.locale().language ?: "en"
    }

    fun getMessage(player: Player, key: MessageKey): Component {
        val lang = player.getLanguage()
        val message: String = message[lang]?.get(key) ?: key.name

        return Component.text(message)
    }

    fun getSysMessage(key: MessageKey): Component {
        val lang = Locale.getDefault().language
        val message: String = message[lang]?.get(key) ?: key.name

        return Component.text(message)
    }
}

enum class MessageKey {
    THIS_COMMAND_IS_ONLY_FOR_PLAYERS,
    NO_COMMAND_PROVIDED,
    NO_PLAYER_PROVIDED,
    PLAYER_NOT_FOUND,

    HELP_HEADER,

    EC_COMMAND,
    UEC_COMMAND,
    NO_PERMISSION,
    INVALID_UUID
    ;

    fun toComponent(): Component {
        return Component.text(this.name)
    }
}