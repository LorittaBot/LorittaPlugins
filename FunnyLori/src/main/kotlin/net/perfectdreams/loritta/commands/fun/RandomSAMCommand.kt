package net.perfectdreams.loritta.commands.`fun`

import com.github.kevinsawicki.http.HttpRequest
import com.github.salomonbrys.kotson.array
import com.github.salomonbrys.kotson.get
import com.github.salomonbrys.kotson.obj
import com.github.salomonbrys.kotson.string
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.mrpowergamerbr.loritta.Loritta
import com.mrpowergamerbr.loritta.utils.jsonParser
import com.mrpowergamerbr.loritta.utils.locale.BaseLocale
import net.perfectdreams.commands.annotation.Subcommand
import net.perfectdreams.loritta.api.commands.CommandCategory
import net.perfectdreams.loritta.api.commands.LorittaCommand
import net.perfectdreams.loritta.api.commands.LorittaCommandContext
import org.jsoup.Jsoup
import twitter4j.Query
import twitter4j.Status
import twitter4j.TwitterFactory
import twitter4j.conf.ConfigurationBuilder
import java.util.*

class RandomSAMCommand : LorittaCommand(arrayOf("randomsam", "randomsouthamericamemes", "rsam", "rsouthamericamemes"), category = CommandCategory.IMAGES) {
    companion object {
        var lastRequest = 0L
        var cachedMemes = listOf<JsonElement>()
    }

    override fun getDescription(locale: BaseLocale): String? {
        return locale["commands.fun.randomsam.description"]
    }

    @Subcommand
    suspend fun root(context: LorittaCommandContext) {
        if (System.currentTimeMillis() >= (lastRequest + 60_000)) {
            val scripts = Jsoup.connect("https://www.instagram.com/southamericamemes/?hl=pt-br")
                    .get()
                    .getElementsByTag("script")

            val instagramData = scripts.first { it.html().startsWith("window._sharedData = ") }.html().substring(21).let { it.substring(0, it.length - 1) }

            println(instagramData)

            val json = jsonParser.parse(instagramData).obj

            cachedMemes = json["entry_data"]["ProfilePage"].array[0]["graphql"]["user"]["edge_owner_to_timeline_media"]["edges"].array.toMutableList()
            lastRequest = System.currentTimeMillis()
        }

        val randomMeme = cachedMemes.random()
        context.sendMessage("<:sam:383614103853203456> **|** " + context.getAsMention(true) + "Cópia não comédia! ${randomMeme["node"]["display_url"].string}")
    }
}