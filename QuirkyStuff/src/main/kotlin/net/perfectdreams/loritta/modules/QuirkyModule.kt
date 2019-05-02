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

class QuirkyModule : MessageReceivedModule {
    override fun matches(event: LorittaMessageEvent, lorittaUser: LorittaUser, lorittaProfile: Profile, serverConfig: MongoServerConfig, locale: LegacyBaseLocale): Boolean {
        return serverConfig.miscellaneousConfig.enableQuirky && event.guild?.selfMember?.hasPermission(event.textChannel!!, Permission.MESSAGE_ADD_REACTION, Permission.MESSAGE_EXT_EMOJI, Permission.MESSAGE_HISTORY, Permission.MESSAGE_WRITE) == true && event.message.type == MessageType.DEFAULT
    }

    override suspend fun handle(event: LorittaMessageEvent, lorittaUser: LorittaUser, lorittaProfile: Profile, serverConfig: MongoServerConfig, locale: LegacyBaseLocale): Boolean {
        // uwu u are sooo quirky
        val message = event.message

        val reactionRandom = RANDOM.nextInt(0, 500)
        val random = RANDOM.nextInt(0, 250)

        when (reactionRandom) {
            0 -> message.addReaction("ata:339904769139146755").queue()
            1 -> message.addReaction("daora:375321168632086529").queue()
            2 -> message.addReaction("wow:432531424671694849").queue()
            3 -> message.addReaction("rip:473621981619552267").queue()
            4 -> message.addReaction("gesso:523233744656662548").queue()
            5 -> message.addReaction("a:revolving_think:417382964364836864").queue()
            6 -> message.addReaction("thonk:413425726369431552").queue()
            7 -> message.addReaction("lori_morre_diabo:540656812836519936").queue()
            8 -> message.addReaction("a:lori_rage:541715482986938379").queue()
            9 -> message.addReaction("lori_ok_hand:426183783008698391").queue()
            10 -> message.addReaction("demencia:302228166314033152").queue()
            11 -> message.addReaction("eu_te_moido:366047906689581085").queue()
            12 -> message.addReaction("lori_wow:540944393692119040").queue()
            13 -> message.addReaction("hyper_NOSA:450476856303419432").queue()
            14 -> message.addReaction("idai:334709223588102144").queue()
            15 -> message.addReaction("a:lori_happy:521721811298156558").queue()
            16 -> message.addReaction("a:ralseinite:508811387175436291").queue()
        }

        if (chance(0.10)) {
            event.channel.sendMessage("${event.author.asMention} ${TioDoPaveCommand.PIADAS.random()} <:lori_ok_hand:426183783008698391>").queue()
        }

        if ((event.message.contentRaw.contains("esta é uma mensagem do criador", true) && event.message.contentRaw.contains("se tornou muito lenta", true) && event.message.contentRaw.contains("que não enviarem essa mensagem dentro de duas semanas", true)) || (event.message.contentRaw.contains("deve fechar", true) && event.message.contentRaw.contains("Vamos enviar esta mensagem para ver se os membros", true) && event.message.contentRaw.contains("isto é de acordo com o criador", true)))
            event.channel.sendMessage("${event.author.asMention} agora me diga... porque você acha que o Discord ia avisar algo importante assim com uma CORRENTE? Isso daí é fake, se isso fosse verdade, o Discord iria colocar um aviso nas redes sociais e ao iniciar o Discord, apenas ignore tais mensagens... e por favor, pare de espalhar \uD83D\uDD17 correntes \uD83D\uDD17, não quero que aqui vire igual ao WhatsApp. <:smol_lori_putassa:395010059157110785>").queue()

        if (event.message.contentRaw.contains("DDoSed", true) && event.message.contentRaw.contains("pedidos de amizade para usuários aleatórios", true) && event.message.contentRaw.contains("tornando uma vítima também", true))
            event.channel.sendMessage("${event.author.asMention} você acha mesmo que se um usuário tivesse fazendo isto no Discord, ele já não teria sido suspenso em todo o Discord? Antes de compartilhar \uD83D\uDD17 correntes \uD83D\uDD17, pense um pouco sobre elas antes de mandar isto para vários usuários, não quero que aqui vire igual ao WhatsApp. <:smol_lori_putassa:395010059157110785>").queue()

        return false
    }
}