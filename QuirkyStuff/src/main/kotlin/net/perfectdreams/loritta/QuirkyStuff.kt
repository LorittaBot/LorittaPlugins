package net.perfectdreams.loritta

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.TreeNode
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier
import com.fasterxml.jackson.databind.type.ArrayType
import com.fasterxml.jackson.databind.type.CollectionLikeType
import com.fasterxml.jackson.databind.type.MapType
import com.fasterxml.jackson.module.kotlin.readValue
import com.jasonclawson.jackson.dataformat.hocon.HoconFactory
import com.mrpowergamerbr.loritta.utils.Constants
import com.typesafe.config.Config
import net.perfectdreams.loritta.modules.QuirkyModule
import net.perfectdreams.loritta.modules.ThankYouLoriModule
import net.perfectdreams.loritta.platform.discord.plugin.DiscordPlugin
import java.io.File
import com.typesafe.config.ConfigFactory

class QuirkyStuff : DiscordPlugin() {
    override fun onEnable() {
        val config = Constants.HOCON_MAPPER.readValue<QuirkyConfig>(File(dataFolder, "config.conf"))

        registerMessageReceivedModules(
                QuirkyModule(config),
                ThankYouLoriModule(config)
        )
    }
}