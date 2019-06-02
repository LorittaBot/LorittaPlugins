package net.perfectdreams.loritta.commands.administration

import com.mrpowergamerbr.loritta.utils.Constants
import com.mrpowergamerbr.loritta.utils.extensions.await
import com.mrpowergamerbr.loritta.utils.locale.BaseLocale
import kotlinx.coroutines.delay
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.utils.MiscUtil
import net.perfectdreams.commands.annotation.Subcommand
import net.perfectdreams.loritta.api.commands.*
import net.perfectdreams.loritta.platform.discord.entities.DiscordCommandContext
import net.perfectdreams.loritta.platform.discord.entities.DiscordMessage

class LimparCommand : LorittaCommand(arrayOf("limpar", "clear", "clean", "bulkdelete"), CommandCategory.ADMIN) {
    override fun getDescription(locale: BaseLocale): String? {
        return locale["commands.moderation.clear.description"]
    }

    override fun getUsage(locale: BaseLocale): CommandArguments {
        return arguments {
            argument(ArgumentType.NUMBER) {}
        }
    }

    override val botPermissions: List<Permission> = listOf(Permission.MANAGE_CHANNEL, Permission.MESSAGE_HISTORY)
    override val discordPermissions: List<Permission> = listOf(Permission.MANAGE_CHANNEL)

    override fun getExamples(locale: BaseLocale): List<String> {
        return listOf(
                "20",
                "15 @Nightdavisao @MrPowerGamerBR"
        )
    }

    override val canUseInPrivateChannel: Boolean = false

    @Subcommand
    suspend fun root(context: DiscordCommandContext, locale: BaseLocale) {
        val toClear = context.args[0].toIntOrNull()
        if (context.args.isEmpty() || toClear == null) {
            context.explain()
            return
        }
        if (toClear !in 2..170) {
            context.reply(locale["commands.moderation.clear.invalidRange"], Constants.ERROR)
            return
        }

        // Deletar a mensagem do usuário caso não tenha nenhuma menção de usuário
        if (context.discordMessage.mentionedUsers.isEmpty()) context.message.delete()

        var hasOldMessages = false
        val messages = context.event.textChannel!!.history.retrievePast(toClear).await()
        val allowedMessages = messages.asSequence().filter {
            if (context.discordMessage.mentionedUsers.isNotEmpty()) {
                context.discordMessage.mentionedUsers.contains(it.author)
            } else {
                true
            }
        }.filter {
            val twoWeeksAgo = System.currentTimeMillis() - 14 * 24 * 60 * 60 * 1000 - Constants.DISCORD_EPOCH shl Constants.TIMESTAMP_OFFSET.toInt()
            hasOldMessages = it.timeCreated.toInstant().toEpochMilli() > twoWeeksAgo
            hasOldMessages
        }.toMutableList()

        if (allowedMessages.isEmpty())
            context.reply(locale["commands.moderation.clear.couldntFindMessages"])

        // it's time to delete
        context.event.textChannel!!.deleteMessages(allowedMessages)
        val broomEmoji = "<:broom:584827322197344267>"

        val sentMsg = if (!hasOldMessages) {
            context.reply(locale["commands.moderation.clear.success", allowedMessages.size], broomEmoji)
        } else {
            context.reply(locale["commands.moderation.clear.successIgnoredOld", allowedMessages.size, messages.size - allowedMessages.size], broomEmoji)
        }
        // Deletar mensagem da Lori depois de 5 segundos (se não ter nenhuma nehuma menção na mensagem)
        if (context.discordMessage.mentionedUsers.isEmpty()) {
            delay(5000)
            sentMsg.delete()
        }
    }
}