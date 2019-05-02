package net.perfectdreams.loritta

import net.perfectdreams.loritta.modules.QuirkyModule
import net.perfectdreams.loritta.modules.ThankYouLoriModule
import net.perfectdreams.loritta.platform.discord.plugin.DiscordPlugin

class QuirkyStuff : DiscordPlugin() {
    override fun onEnable() {
        registerMessageReceivedModules(
                QuirkyModule(),
                ThankYouLoriModule()
        )
    }
}