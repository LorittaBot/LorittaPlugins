package net.perfectdreams.loritta.modules

import com.mrpowergamerbr.loritta.Loritta.Companion.RANDOM
import com.mrpowergamerbr.loritta.commands.vanilla.`fun`.TioDoPaveCommand
import com.mrpowergamerbr.loritta.dao.Profile
import com.mrpowergamerbr.loritta.events.LorittaMessageEvent
import com.mrpowergamerbr.loritta.modules.MessageReceivedModule
import com.mrpowergamerbr.loritta.userdata.MongoServerConfig
import com.mrpowergamerbr.loritta.utils.LorittaUser
import com.mrpowergamerbr.loritta.utils.chance
import com.mrpowergamerbr.loritta.utils.locale.LegacyBaseLocale
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.MessageType
import net.perfectdreams.loritta.QuirkyConfig

class QuirkyModule(val config: QuirkyConfig) : MessageReceivedModule {
    override fun matches(event: LorittaMessageEvent, lorittaUser: LorittaUser, lorittaProfile: Profile, serverConfig: MongoServerConfig, locale: LegacyBaseLocale): Boolean {
        return serverConfig.miscellaneousConfig.enableQuirky && event.guild?.selfMember?.hasPermission(event.textChannel!!, Permission.MESSAGE_ADD_REACTION, Permission.MESSAGE_EXT_EMOJI, Permission.MESSAGE_HISTORY, Permission.MESSAGE_WRITE) == true && event.message.type == MessageType.DEFAULT
    }

    override suspend fun handle(event: LorittaMessageEvent, lorittaUser: LorittaUser, lorittaProfile: Profile, serverConfig: MongoServerConfig, locale: LegacyBaseLocale): Boolean {
        // uwu u are sooo quirky
        val message = event.message

        val random = RANDOM.nextInt(0, 250)

        if (config.randomReactions.enabled) {
            val reactionRandom = RANDOM.nextInt(0, config.randomReactions.maxBound)

            config.randomReactions.reactions[reactionRandom]?.let {
                // Let = "Vamos apenas pegar se NÃO for nulo", ou seja:
                // Se o valor na randomReactions.reactions[reactionRandom] NÃO for nulo, nós iremos adicionar a reação
                // Caso seja nulo, nada irá acontecer.
                message.addReaction(it).queue()
            }

            for (contextAware in config.randomReactions.contextAwareReactions) {
                if (chance(contextAware.chanceOf)) {
                    if (event.message.contentRaw.matches(Regex(contextAware.match))) {
                        message.addReaction(contextAware.reactions.random()).queue()
                        break
                    }
                }
            }
        }

        if (config.tioDoPave.enabled && chance(config.tioDoPave.chance))
            event.channel.sendMessage("${event.author.asMention} ${TioDoPaveCommand.PIADAS.random()} <:lori_ok_hand:426183783008698391>").queue()

        if ((event.message.contentRaw.contains("esta é uma mensagem do criador", true) && event.message.contentRaw.contains("se tornou muito lenta", true) && event.message.contentRaw.contains("que não enviarem essa mensagem dentro de duas semanas", true)) || (event.message.contentRaw.contains("deve fechar", true) && event.message.contentRaw.contains("Vamos enviar esta mensagem para ver se os membros", true) && event.message.contentRaw.contains("isto é de acordo com o criador", true)))
            event.channel.sendMessage("${event.author.asMention} agora me diga... porque você acha que o Discord ia avisar algo importante assim com uma CORRENTE? Isso daí é fake, se isso fosse verdade, o Discord iria colocar um aviso nas redes sociais e ao iniciar o Discord, apenas ignore tais mensagens... e por favor, pare de espalhar \uD83D\uDD17 correntes \uD83D\uDD17, não quero que aqui vire igual ao WhatsApp. <:smol_lori_putassa:395010059157110785>").queue()

        if (event.message.contentRaw.contains("DDoSed", true) && event.message.contentRaw.contains("pedidos de amizade para usuários aleatórios", true) && event.message.contentRaw.contains("tornando uma vítima também", true))
            event.channel.sendMessage("${event.author.asMention} você acha mesmo que se um usuário tivesse fazendo isto no Discord, ele já não teria sido suspenso em todo o Discord? Antes de compartilhar \uD83D\uDD17 correntes \uD83D\uDD17, pense um pouco sobre elas antes de mandar isto para vários usuários, não quero que aqui vire igual ao WhatsApp. <:smol_lori_putassa:395010059157110785>").queue()

        return false
    }
}