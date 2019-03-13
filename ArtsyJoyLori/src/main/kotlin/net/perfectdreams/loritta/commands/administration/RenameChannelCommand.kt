package net.perfectdreams.loritta.commands.administration

import com.mrpowergamerbr.loritta.utils.Constants
import com.mrpowergamerbr.loritta.utils.isValidSnowflake
import com.mrpowergamerbr.loritta.utils.locale.BaseLocale
import net.dv8tion.jda.core.Permission
import net.perfectdreams.loritta.api.commands.*
import net.perfectdreams.loritta.platform.discord.entities.DiscordCommandContext

class RenameChannelCommand: LorittaCommand(arrayOf("renamechannel", "renomearcanal"), CommandCategory.ADMIN) {
    override fun getDescription(locale: BaseLocale): String? {
        return locale["commands.moderation.renamechannel.description"]
    }

    override fun getUsage(locale: BaseLocale): CommandArguments {
        return arguments {
            argument(ArgumentType.TEXT) {}
            argument(ArgumentType.TEXT) {}
        }
    }

    override val botPermissions: List<Permission> = listOf(Permission.MANAGE_CHANNEL)
    override val discordPermissions: List<Permission> = listOf(Permission.MANAGE_CHANNEL)

    override fun getExamples(locale: BaseLocale): List<String> {
        return listOf(
                "#lori-é-fofis lori é fofis",
                "297732013006389252 bate papo",
                "bate-papo bate papo"
        )
    }

    override val canUseInPrivateChannel: Boolean = false

    @Subcommand
    suspend fun root(context: DiscordCommandContext, locale: BaseLocale) {
        if (context.args.isEmpty() || context.args.size < 2) {
            context.explain()
            return
        }
            val channel = if (context.discordMessage.mentionedChannels.firstOrNull() != null) {
                context.discordMessage.mentionedChannels.firstOrNull()
            } else if (context.args[0].isValidSnowflake() && context.event.guild!!.getTextChannelById(context.args[0]) != null) {
                context.event.guild!!.getTextChannelById(context.args[0])
            } else if (context.event.guild!!.getTextChannelsByName(context.args[0], true) != null) {
                context.event.guild!!.getTextChannelsByName(context.args[0], true).first()
            } else {
                context.reply(locale["commands.moderation.renamechannel.channelnotfound"], Constants.ERROR)
                return
            }
            val toRename = context.args.joinToString(" ")
                    .slice(context.args[0].length..context.args.joinToString(" ").length)
                    .trim()
                    .replace('|', '│')
                    .replace("/[\\s]/g".toRegex(), "\u2005")

            channel!!.manager.setName(toRename)
            context.reply(locale["commands.moderation.renamechannel.successfullyrenamed"], "\uD83C\uDF89")
    }
}
