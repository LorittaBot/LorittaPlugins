package net.perfectdreams.loritta.commands.discord

import com.mrpowergamerbr.loritta.utils.Constants
import com.mrpowergamerbr.loritta.utils.isValidSnowflake
import com.mrpowergamerbr.loritta.utils.locale.BaseLocale
import net.dv8tion.jda.core.Permission
import net.perfectdreams.commands.annotation.Subcommand
import net.perfectdreams.loritta.api.commands.*
import net.perfectdreams.loritta.platform.discord.entities.DiscordCommandContext
import java.util.regex.Pattern

class RenameEmojiCommand : LorittaCommand(arrayOf("renameemoji", "renomearemoji", "remoji"), CommandCategory.DISCORD) {
    override fun getDescription(locale: BaseLocale): String? {
        return locale["commands.discord.renameemoji.description"]
    }

    override fun getUsage(locale: BaseLocale): CommandArguments {
        return arguments {
            argument(ArgumentType.EMOTE) {}
            argument(ArgumentType.TEXT) {}
        }
    }

    override fun getExamples(locale: BaseLocale): List<String> {
        return listOf(":gesso: gessy", "524938593475756042 sad_gesso", "gesso_cat sad_gesso")
    }

    override val canUseInPrivateChannel: Boolean = false

    override val botPermissions: List<Permission> = listOf(Permission.MANAGE_EMOTES)
    override val discordPermissions: List<Permission> = listOf(Permission.MANAGE_EMOTES)

    @Subcommand
    suspend fun root(context: DiscordCommandContext, locale: BaseLocale) {
        if (context.args.isNotEmpty()) {
            val argumentEmote = context.args[0]
            val argumentChangeName = context.args[1]
            val firstEmote = context.discordMessage.emotes.firstOrNull()

            // Verificar emojis na mensagem
            val emote = if (firstEmote != null) {
                firstEmote
            } else if (argumentEmote.isValidSnowflake() && context.discordGuild!!.getEmoteById(argumentEmote) != null) {
                context.discordGuild!!.getEmoteById(argumentEmote)
            } else if (context.discordGuild!!.getEmotesByName(argumentEmote, true).isNotEmpty()) {
                context.discordGuild!!.getEmotesByName(argumentEmote, true).first()
            } else {
                context.reply(locale["commands.discord.renameemoji.emoteNotFound"], Constants.ERROR)
                return
            }
            // Verificar nome para renomear o emoji
            val regexPattern = Pattern.compile("[A-z0-9_]+")
            val regexMatch = regexPattern.matcher(argumentChangeName)
            val emoteName = if (argumentChangeName.length >= 32) {
                context.reply(locale["commands.discord.renameemoji.emoteNameLength32Error"], Constants.ERROR)
                return
            } else if (2 >= argumentChangeName.length) {
                context.reply(locale["commands.discord.renameemoji.emoteNameLength2Error"], Constants.ERROR)
                return
            } else if (!regexMatch.matches()) {
                context.reply(locale["commands.discord.renameemoji.emoteNameSpecialChar"], Constants.ERROR)
                return
            } else {
                argumentChangeName
            }

            // Finalmente renomear emoji!
            if (emote != null && emoteName != null && emote.canInteract(context.discordGuild!!.selfMember)) {
                emote.manager.setName(emoteName).queue()
                context.reply(locale["commands.discord.renameemoji.renameSucess"], emote.asMention)
            }
        } else {
            context.explain()
        }
    }
}
