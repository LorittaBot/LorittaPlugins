package net.perfectdreams.loritta.commands.actions

import com.mrpowergamerbr.loritta.utils.locale.BaseLocale
import net.dv8tion.jda.core.entities.User
import java.awt.Color

class HugCommand : ActionCommand(arrayOf("hug", "abraço", "abraçar", "abraco", "abracar")) {
    override fun getEmbedColor(): Color {
        return Color(255, 235, 59)
    }

    override fun getDescription(locale: BaseLocale): String {
        return locale["commands.actions.hug.description"]
    }

    override fun getResponse(locale: BaseLocale, first: User, second: User): String {
        return locale["commands.actions.hug.response", first.asMention, second.asMention]
    }

    override fun getFolderName(): String {
        return "hug"
    }

    override fun getEmoji(): String {
        return "\uD83D\uDC99"
    }
}