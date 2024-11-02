@file:Suppress("ClassName")

package net.rk4z.quickec

import net.rk4z.s1.pluginBase.MessageKey

open class Main : MessageKey {
    open class Command : Main() {
        object THIS_COMMAND_IS_ONLY_FOR_PLAYERS : Command()
        object NO_PLAYER_PROVIDED : Command()
        object PLAYER_NOT_FOUND : Command()
        object NO_ENDER_CHEST_IN_INVENTORY : Command()
    }

    open class Help : Main() {
        object HELP_HEADER : Help()

        object EC_COMMAND : Help()
        object UEC_COMMAND : Help()
    }

    open class Message : Main() {
        object NO_PERMISSION : Message()
        object INVALID_UUID : Message()
    }
}