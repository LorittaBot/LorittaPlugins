package net.perfectdreams.loritta.commands.`fun`

import com.github.kevinsawicki.http.HttpRequest
import com.mrpowergamerbr.loritta.Loritta
import com.mrpowergamerbr.loritta.Loritta.Companion.RANDOM
import com.mrpowergamerbr.loritta.utils.Constants
import com.mrpowergamerbr.loritta.utils.extensions.getRandom
import com.mrpowergamerbr.loritta.utils.jsonParser
import com.mrpowergamerbr.loritta.utils.locale.BaseLocale
import net.perfectdreams.commands.annotation.Subcommand
import net.perfectdreams.loritta.api.commands.CommandArguments
import net.perfectdreams.loritta.api.commands.CommandCategory
import net.perfectdreams.loritta.api.commands.LorittaCommand
import net.perfectdreams.loritta.api.commands.LorittaCommandContext
import org.jsoup.Jsoup
import twitter4j.Query
import twitter4j.Status
import twitter4j.TwitterFactory
import twitter4j.conf.ConfigurationBuilder
import java.awt.Color
import java.awt.Font
import java.awt.Graphics
import java.awt.Rectangle
import java.io.File
import java.util.*
import javax.imageio.ImageIO

class RandomSAMCommand : LorittaCommand(arrayOf("randomsam", "randomsouthamericamemes", "rsam", "rsouthamericamemes"), category = CommandCategory.IMAGES) {
    companion object {
        var lastRequest = 0L
        var cachedMemes = listOf<Status>()
    }

    override fun getDescription(locale: BaseLocale): String? {
        return locale["commands.fun.randomsam.description"]
    }

    @Subcommand
    suspend fun root(context: LorittaCommandContext) {
        val cb = ConfigurationBuilder()
        cb.setDebugEnabled(true)
            .setOAuthConsumerKey(Loritta.config.twitterConfig.oAuthConsumerKey)
            .setOAuthConsumerSecret(Loritta.config.twitterConfig.oAuthConsumerSecret)
            .setOAuthAccessToken(Loritta.config.twitterConfig.oAuthAccessToken)
            .setOAuthAccessTokenSecret(Loritta.config.twitterConfig.oAuthAccessTokenSecret)

        if (System.currentTimeMillis() >= (lastRequest + 60_000)) {
            val tf = TwitterFactory(cb.build())
            val twitter = tf.instance
            // Twitter só permite procurar, no máximo, 9 dias atrás (mais que isso não retorna nada!)
            val now = Calendar.getInstance()
            val nineDaysAgo = Calendar.getInstance()
            nineDaysAgo.add(Calendar.DAY_OF_MONTH, -(Loritta.RANDOM.nextInt(1, 10)))
            val query = Query("from:SoutAmericMemes since:${nineDaysAgo.get(Calendar.YEAR)}-${nineDaysAgo.get(Calendar.MONTH) + 1}-${nineDaysAgo.get(Calendar.DAY_OF_MONTH)} until:${now.get(Calendar.YEAR)}-${now.get(Calendar.MONTH) + 1}-${now.get(Calendar.DAY_OF_MONTH)}")
            val result = twitter.search(query)
            cachedMemes = result.tweets.filter { !it.isRetweeted && it.mediaEntities.isNotEmpty() }
            lastRequest = System.currentTimeMillis()
        }

        val randomMeme = cachedMemes.random()
        context.sendMessage("<:sam:383614103853203456> **|** " + context.getAsMention(true) + "Cópia não comédia! ${randomMeme.mediaEntities.first().mediaURLHttps}")
    }
}