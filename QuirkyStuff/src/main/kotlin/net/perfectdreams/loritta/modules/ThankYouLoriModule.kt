package net.perfectdreams.loritta.modules

import com.mrpowergamerbr.loritta.Loritta
import com.mrpowergamerbr.loritta.dao.Profile
import com.mrpowergamerbr.loritta.events.LorittaMessageEvent
import com.mrpowergamerbr.loritta.modules.MessageReceivedModule
import com.mrpowergamerbr.loritta.userdata.MongoServerConfig
import com.mrpowergamerbr.loritta.utils.LorittaUser
import com.mrpowergamerbr.loritta.utils.MiscUtils
import com.mrpowergamerbr.loritta.utils.locale.LegacyBaseLocale

class ThankYouLoriModule : MessageReceivedModule {
    override fun matches(event: LorittaMessageEvent, lorittaUser: LorittaUser, lorittaProfile: Profile, serverConfig: MongoServerConfig, locale: LegacyBaseLocale): Boolean {
        return event.channel.id == "529459242550296577"
    }

    override suspend fun handle(event: LorittaMessageEvent, lorittaUser: LorittaUser, lorittaProfile: Profile, serverConfig: MongoServerConfig, locale: LegacyBaseLocale): Boolean {
        if (event.message.contentRaw.length >= 8) {
            if (MiscUtils.hasInappropriateWords(event.message.contentRaw))
                return false

            val reactionRandom = Loritta.RANDOM.nextInt(0, 7)

            val message = event.message

            when (reactionRandom) {
                0 -> message.addReaction("a:lori_temmie:515330130495799307").queue()
                1 -> message.addReaction("lori_hearts:519901735666581514").queue()
                2 -> message.addReaction("a:lori_happy:521721811298156558").queue()
                3 -> message.addReaction("lori_pac:503600573741006863").queue()
                4 -> message.addReaction("lori_owo:417813932380520448").queue()
                5 -> message.addReaction("gesso:523233744656662548").queue()
                6 -> message.addReaction("a:owo_whats_this:515329346194636811").queue()
            }
        }

        return false
    }
}