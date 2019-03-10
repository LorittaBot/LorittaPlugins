package net.perfectdreams.loritta.commands.actions

import com.mrpowergamerbr.loritta.Loritta
import com.mrpowergamerbr.loritta.utils.locale.BaseLocale
import net.dv8tion.jda.core.entities.User
import java.awt.Color

class AttackCommand : ActionCommand(arrayOf("attack", "atacar")) {
    override fun getEmbedColor(): Color {
        return Color(244, 67, 54)
    }

    override fun getDescription(locale: BaseLocale): String {
        return locale["commands.actions.attack.description"]
    }

    override fun getResponse(locale: BaseLocale, first: User, second: User): String {
        return if (second.id != Loritta.config.clientId) {
            locale["commands.actions.attack.response", first.asMention, second.asMention]
        } else {
            locale["commands.actions.attack.responseAntiIdiot", second.asMention, first.asMention]
        }
    }

    override fun getFolderName(): String {
        return "attack"
    }

    override fun getEmoji(): String {
        return "\uD83E\uDD4A"
    }
}