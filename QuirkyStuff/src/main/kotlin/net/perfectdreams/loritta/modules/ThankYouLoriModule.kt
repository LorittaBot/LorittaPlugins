package net.perfectdreams.loritta.modules

import com.mrpowergamerbr.loritta.Loritta
import com.mrpowergamerbr.loritta.dao.Profile
import com.mrpowergamerbr.loritta.events.LorittaMessageEvent
import com.mrpowergamerbr.loritta.modules.MessageReceivedModule
import com.mrpowergamerbr.loritta.userdata.MongoServerConfig
import com.mrpowergamerbr.loritta.utils.LorittaUser
import com.mrpowergamerbr.loritta.utils.MiscUtils
import com.mrpowergamerbr.loritta.utils.locale.LegacyBaseLocale
import net.perfectdreams.loritta.QuirkyConfig

class ThankYouLoriModule(val config: QuirkyConfig) : MessageReceivedModule {
    override fun matches(event: LorittaMessageEvent, lorittaUser: LorittaUser, lorittaProfile: Profile, serverConfig: MongoServerConfig, locale: LegacyBaseLocale): Boolean {
        return config.thankYouLori.enabled && event.channel.idLong == config.thankYouLori.channelId
    }

    override suspend fun handle(event: LorittaMessageEvent, lorittaUser: LorittaUser, lorittaProfile: Profile, serverConfig: MongoServerConfig, locale: LegacyBaseLocale): Boolean {
        if (event.message.contentRaw.length >= 8) {
            if (MiscUtils.hasInappropriateWords(event.message.contentRaw))
                return false

            event.message.addReaction(config.thankYouLori.reactions.random()).queue()
        }

        return false
    }
}