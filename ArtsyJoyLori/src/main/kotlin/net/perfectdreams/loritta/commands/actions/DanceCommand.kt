package net.perfectdreams.loritta.commands.actions

import com.mrpowergamerbr.loritta.utils.locale.BaseLocale
import net.dv8tion.jda.api.entities.User
import java.awt.Color

class DanceCommand : ActionCommand(arrayOf("dance", "dançar")) {
    override fun getEmbedColor(): Color {
        return Color(255, 152, 0)
    }

    override fun getDescription(locale: BaseLocale): String {
        return locale["commands.actions.dance.description"]
    }

    override fun getResponse(locale: BaseLocale, first: User, second: User): String {
        return locale["commands.actions.dance.response", first.asMention, second.asMention]
    }

    override fun getFolderName(): String {
        return "dance"
    }

    override fun getEmoji(): String {
        return "\uD83D\uDD7A"
    }
}