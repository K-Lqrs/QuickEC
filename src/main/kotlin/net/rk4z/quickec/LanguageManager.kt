package net.rk4z.quickec

import net.kyori.adventure.text.Component
import org.bukkit.entity.Player
import java.util.Locale

object LanguageManager {
    private val message: Map<String, Map<MessageKey, String>> = mapOf(
        "ja" to mapOf(
            MessageKey.THIS_COMMAND_IS_ONLY_FOR_PLAYERS to "このコマンドはプレイヤーのみ使用できます",
            MessageKey.NO_COMMAND_PROVIDED to "コマンドが指定されていません。",
            MessageKey.NO_PLAYER_PROVIDED to "対象のプレイヤーが指定されていません。",
            MessageKey.PLAYER_NOT_FOUND to "指定されたプレイヤーが見つかりません。",
            MessageKey.NO_PERMISSION to "このコマンドを実行する権限がありません。",
            MessageKey.INVALID_UUID to "無効なUUIDです。",
            MessageKey.HELP_HEADER to "クイックエンダーチェスト ヘルプ",
            MessageKey.EC_COMMAND to "自分のエンダーチェストを開く。",
            MessageKey.UEC_COMMAND to "他のプレイヤーのエンダーチェストを開く。"
        ),
        "en" to mapOf(
            MessageKey.THIS_COMMAND_IS_ONLY_FOR_PLAYERS to "Only players can use this command",
            MessageKey.NO_COMMAND_PROVIDED to "No command provided.",
            MessageKey.NO_PLAYER_PROVIDED to "No player specified.",
            MessageKey.PLAYER_NOT_FOUND to "Player not found.",
            MessageKey.NO_PERMISSION to "You don't have permission to execute this command.",
            MessageKey.INVALID_UUID to "Invalid UUID.",
            MessageKey.HELP_HEADER to "QuickEnderChest Help",
            MessageKey.EC_COMMAND to "Opens your own ender chest.",
            MessageKey.UEC_COMMAND to "Opens another player's ender chest."
        ),
        "zh" to mapOf(
            MessageKey.THIS_COMMAND_IS_ONLY_FOR_PLAYERS to "此命令只能由玩家使用。",
            MessageKey.NO_COMMAND_PROVIDED to "未指定命令。",
            MessageKey.NO_PLAYER_PROVIDED to "未指定目标玩家。",
            MessageKey.PLAYER_NOT_FOUND to "未找到指定的玩家。",
            MessageKey.NO_PERMISSION to "您没有权限执行此命令。",
            MessageKey.INVALID_UUID to "无效的UUID。",
            MessageKey.HELP_HEADER to "快速末影箱帮助",
            MessageKey.EC_COMMAND to "打开自己的末影箱。",
            MessageKey.UEC_COMMAND to "打开其他玩家的末影箱。"
        ),
        "fr" to mapOf(
            MessageKey.THIS_COMMAND_IS_ONLY_FOR_PLAYERS to "Seuls les joueurs peuvent utiliser cette commande.",
            MessageKey.NO_COMMAND_PROVIDED to "Aucune commande spécifiée.",
            MessageKey.NO_PLAYER_PROVIDED to "Aucun joueur spécifié.",
            MessageKey.PLAYER_NOT_FOUND to "Joueur introuvable.",
            MessageKey.NO_PERMISSION to "Vous n'avez pas la permission d'exécuter cette commande.",
            MessageKey.INVALID_UUID to "UUID invalide.",
            MessageKey.HELP_HEADER to "Aide de QuickEnderChest",
            MessageKey.EC_COMMAND to "Ouvre votre propre coffre de l'End.",
            MessageKey.UEC_COMMAND to "Ouvre le coffre de l'End d'un autre joueur."
        ),
        "de" to mapOf(
            MessageKey.THIS_COMMAND_IS_ONLY_FOR_PLAYERS to "Nur Spieler können diesen Befehl verwenden.",
            MessageKey.NO_COMMAND_PROVIDED to "Kein Befehl angegeben.",
            MessageKey.NO_PLAYER_PROVIDED to "Kein Spieler angegeben.",
            MessageKey.PLAYER_NOT_FOUND to "Spieler nicht gefunden.",
            MessageKey.NO_PERMISSION to "Sie haben keine Berechtigung, diesen Befehl auszuführen.",
            MessageKey.INVALID_UUID to "Ungültige UUID.",
            MessageKey.HELP_HEADER to "QuickEnderChest Hilfe",
            MessageKey.EC_COMMAND to "Öffnet deine eigene Endertruhe.",
            MessageKey.UEC_COMMAND to "Öffnet die Endertruhe eines anderen Spielers."
        ),
        "es" to mapOf(
            MessageKey.THIS_COMMAND_IS_ONLY_FOR_PLAYERS to "Solo los jugadores pueden usar este comando.",
            MessageKey.NO_COMMAND_PROVIDED to "No se ha proporcionado ningún comando.",
            MessageKey.NO_PLAYER_PROVIDED to "No se ha especificado ningún jugador.",
            MessageKey.PLAYER_NOT_FOUND to "Jugador no encontrado.",
            MessageKey.NO_PERMISSION to "No tienes permiso para ejecutar este comando.",
            MessageKey.INVALID_UUID to "UUID inválida.",
            MessageKey.HELP_HEADER to "Ayuda de QuickEnderChest",
            MessageKey.EC_COMMAND to "Abre tu propio cofre del End.",
            MessageKey.UEC_COMMAND to "Abre el cofre del End de otro jugador."
        ),
        "it" to mapOf(
            MessageKey.THIS_COMMAND_IS_ONLY_FOR_PLAYERS to "Solo i giocatori possono usare questo comando.",
            MessageKey.NO_COMMAND_PROVIDED to "Nessun comando specificato.",
            MessageKey.NO_PLAYER_PROVIDED to "Nessun giocatore specificato.",
            MessageKey.PLAYER_NOT_FOUND to "Giocatore non trovato.",
            MessageKey.NO_PERMISSION to "Non hai il permesso per eseguire questo comando.",
            MessageKey.INVALID_UUID to "UUID non valida.",
            MessageKey.HELP_HEADER to "Guida di QuickEnderChest",
            MessageKey.EC_COMMAND to "Apre il tuo Ender Chest.",
            MessageKey.UEC_COMMAND to "Apre l'Ender Chest di un altro giocatore."
        ),
        "ko" to mapOf(
            MessageKey.THIS_COMMAND_IS_ONLY_FOR_PLAYERS to "이 명령어는 플레이어만 사용할 수 있습니다.",
            MessageKey.NO_COMMAND_PROVIDED to "명령어가 지정되지 않았습니다.",
            MessageKey.NO_PLAYER_PROVIDED to "대상 플레이어가 지정되지 않았습니다.",
            MessageKey.PLAYER_NOT_FOUND to "지정된 플레이어를 찾을 수 없습니다.",
            MessageKey.NO_PERMISSION to "이 명령어를 실행할 권한이 없습니다.",
            MessageKey.INVALID_UUID to "잘못된 UUID입니다.",
            MessageKey.HELP_HEADER to "QuickEnderChest 도움말",
            MessageKey.EC_COMMAND to "자신의 엔더 상자를 엽니다.",
            MessageKey.UEC_COMMAND to "다른 플레이어의 엔더 상자를 엽니다."
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
}