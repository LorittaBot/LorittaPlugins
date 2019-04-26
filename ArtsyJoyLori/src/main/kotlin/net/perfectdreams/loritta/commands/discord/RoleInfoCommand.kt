/* owo e uwu */
package net.perfectdreams.loritta.commands.discord

import com.mrpowergamerbr.loritta.utils.Constants
import com.mrpowergamerbr.loritta.utils.DateUtils
import com.mrpowergamerbr.loritta.utils.extensions.localized
import com.mrpowergamerbr.loritta.utils.isValidSnowflake
import com.mrpowergamerbr.loritta.utils.locale.BaseLocale
import com.mrpowergamerbr.loritta.utils.locale.LegacyBaseLocale
import net.dv8tion.jda.api.EmbedBuilder
import net.perfectdreams.commands.annotation.Subcommand
import net.perfectdreams.loritta.api.commands.*
import net.perfectdreams.loritta.platform.discord.entities.DiscordCommandContext

class RoleInfoCommand : LorittaCommand(arrayOf("roleinfo", "taginfo"), CommandCategory.DISCORD) {
    override fun getDescription(locale: BaseLocale): String? {
        return locale["commands.discord.roleinfo.description"]
    }

    override val canUseInPrivateChannel: Boolean = false

    override fun getUsage(locale: BaseLocale): CommandArguments {
        return arguments {
            argument(ArgumentType.TEXT) {}
        }
    }

    @Subcommand
    suspend fun root(context: DiscordCommandContext, locale: BaseLocale, legacyLocale: LegacyBaseLocale, argument: String? = null) {
        if (argument == null) {
            context.explain()
            return
        }

        val role = if (context.discordMessage.mentionedRoles.firstOrNull() != null) {
            context.discordMessage.mentionedRoles.firstOrNull()
        } else if (context.args.isNotEmpty() && argument.isValidSnowflake() && context.event.guild!!.getRoleById(argument) != null) {
            context.event.guild!!.getRoleById(argument)
        } else if (context.event.guild!!.getRolesByName(argument, true).isNotEmpty()) {
            context.event.guild!!.getRolesByName(argument, true)[0]
        } else if (context.event.guild!!.roles.filter { it.name.contains(argument, true) }.isNotEmpty()) {
            context.event.guild!!.roles.filter { it.name.contains(argument, true) }.first()
        } else {
            context.reply(locale["commands.discord.roleinfo.roleNotFound"], Constants.ERROR)
            return
        }

        if (role != null) {
            val embed = EmbedBuilder()

            val isHoisted = if (role.isHoisted) {
                locale["loritta.fancyBoolean.true"]
            } else {
                locale["loritta.fancyBoolean.false"]
            }
            val isMentionable = if (role.isMentionable) {
                locale["loritta.fancyBoolean.true"]
            } else {
                locale["loritta.fancyBoolean.false"]
            }
            val isIntegrationBot = if (role.isManaged) {
                locale["loritta.fancyBoolean.true"]
            } else {
                locale["loritta.fancyBoolean.false"]
            }
            val permissions = role.permissions.joinToString(", ", transform = { "`${it.localized(locale)}`" })

            embed.setTitle("\uD83D\uDCBC ${role.name}")
            if (role.color != null)
                embed.setColor(role.color)
            embed.addField("\uD83D\uDC40 ${locale["commands.discord.roleinfo.roleMention"]}", "`${role.asMention}`", true)
            embed.addField("\uD83D\uDCC5 ${locale["commands.discord.roleinfo.roleCreated"]}", DateUtils.formatDateDiff(role.timeCreated.toInstant().toEpochMilli(), context.legacyLocale), true)
            embed.addField("\uD83D\uDCBB ${locale["commands.discord.roleinfo.roleID"]}", "`${role.id}`", true)
            embed.addField(locale["commands.discord.roleinfo.roleHoisted"], isHoisted, true)
            embed.addField("<:bot:516314838541008906> ${locale["commands.discord.roleinfo.roleIntegration"]}", isIntegrationBot, true)
            embed.addField("\uD83D\uDC40 ${locale["commands.discord.roleinfo.roleMentionable"]}", isMentionable, true)
            embed.addField("\uD83D\uDC65 ${locale["commands.discord.roleinfo.roleMembers"]}", context.discordGuild!!.getMembersWithRoles(role).size.toString(),true)
            if (role.color != null)
                embed.addField("🎨 ${locale["commands.discord.roleinfo.roleColor"]}", "`#${Integer.toHexString(role.color!!.rgb).substring(2).toUpperCase()}`", true)
            embed.addField("\uD83D\uDEE1 ${locale["commands.discord.roleinfo.rolePermissions"]}", permissions, true)

            context.sendMessage(context.getAsMention(true), embed.build())
        }
    }
}