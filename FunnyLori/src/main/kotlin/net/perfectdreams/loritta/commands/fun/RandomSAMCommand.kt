package net.perfectdreams.loritta.commands.`fun`

import com.mrpowergamerbr.loritta.Loritta
import com.mrpowergamerbr.loritta.utils.locale.BaseLocale
import net.perfectdreams.commands.annotation.Subcommand
import net.perfectdreams.loritta.api.commands.CommandCategory
import net.perfectdreams.loritta.api.commands.LorittaCommand
import net.perfectdreams.loritta.api.commands.LorittaCommandContext
import twitter4j.Query
import twitter4j.Status
import twitter4j.TwitterFactory
import twitter4j.conf.ConfigurationBuilder
import java.util.*

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
            val pastDaysAgo = Calendar.getInstance()
            val goBackDays = Loritta.RANDOM.nextInt(9, 365)
            pastDaysAgo.add(Calendar.DAY_OF_MONTH, -goBackDays)
            val futureDaysAgo = Calendar.getInstance()
            futureDaysAgo.add(Calendar.DAY_OF_MONTH, (-goBackDays + 9))

            val query = Query("from:SoutAmericMemes since:${pastDaysAgo.get(Calendar.YEAR)}-${pastDaysAgo.get(Calendar.MONTH) + 1}-${pastDaysAgo.get(Calendar.DAY_OF_MONTH)} until:${futureDaysAgo.get(Calendar.YEAR)}-${futureDaysAgo.get(Calendar.MONTH) + 1}-${futureDaysAgo.get(Calendar.DAY_OF_MONTH)}")
            val result = twitter.search(query)

            cachedMemes = result.tweets.filter { !it.isRetweeted && it.mediaEntities.isNotEmpty() }
            lastRequest = System.currentTimeMillis()
        }

        val randomMeme = cachedMemes.random()
        context.sendMessage("<:sam:383614103853203456> **|** " + context.getAsMention(true) + "Cópia não comédia! ${randomMeme.mediaEntities.first().mediaURLHttps}")
    }
}