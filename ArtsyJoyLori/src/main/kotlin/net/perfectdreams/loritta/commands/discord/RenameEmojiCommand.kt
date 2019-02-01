package net.perfectdreams.loritta.commands.discord

import com.mrpowergamerbr.loritta.utils.Constants
import com.mrpowergamerbr.loritta.utils.isValidSnowflake
import com.mrpowergamerbr.loritta.utils.locale.BaseLocale
import net.dv8tion.jda.core.Permission
import net.perfectdreams.commands.annotation.Subcommand
import net.perfectdreams.loritta.api.commands.*
import net.perfectdreams.loritta.platform.discord.entities.DiscordCommandContext

class RenameEmojiCommand : LorittaCommand(arrayOf("renameemoji", "renomearemoji"), CommandCategory.DISCORD) {
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
        return listOf("renameemoji :gesso: gessy")
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

            val emote = if (argumentEmote == firstEmote?.asMention) {
                firstEmote
            } else if (argumentEmote.isValidSnowflake() && context.discordGuild!!.getEmoteById(argumentEmote) != null) {
                context.discordGuild!!.getEmoteById(argumentEmote)
            } else if (context.discordGuild!!.getEmotesByName(argumentEmote, true).isNotEmpty()) {
                context.discordGuild!!.getEmotesByName(argumentEmote, true).first()
            } else {
                context.reply(locale["commands.discord.renameemoji.emoteNotFound"], Constants.ERROR)
                return
            }
            if (emote != null && argumentChangeName != null && emote.canInteract(context.discordGuild!!.selfMember)) {
                emote.manager.setName(argumentChangeName).queue()
                context.reply(locale["commands.discord.renameemoji.renameSucess"], emote.asMention)
            }
        } else {
            context.explain()
        }
    }
}