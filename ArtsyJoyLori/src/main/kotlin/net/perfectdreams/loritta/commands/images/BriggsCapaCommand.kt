package net.perfectdreams.loritta.commands.images

import com.mrpowergamerbr.loritta.utils.Constants
import com.mrpowergamerbr.loritta.utils.LoriReply
import com.mrpowergamerbr.loritta.utils.LorittaImage
import com.mrpowergamerbr.loritta.utils.locale.BaseLocale
import net.perfectdreams.commands.annotation.Subcommand
import net.perfectdreams.loritta.api.commands.*
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import kotlin.contracts.ExperimentalContracts

class BriggsCapaCommand : LorittaCommand(arrayOf("briggscover", "coverbriggs", "capabriggs", "briggscapa"), CommandCategory.IMAGES) {
    companion object {
        val TEMPLATE by lazy { ImageIO.read(File(Constants.ASSETS_FOLDER, "briggs_capa.png")) }
    }

    override val needsToUploadFiles = true

    override fun getDescription(locale: BaseLocale): String? {
        return locale["commands.images.briggscover.description"]
    }

    override fun getUsage(locale: BaseLocale): CommandArguments {
        return arguments {
            argument(ArgumentType.IMAGE) {}
        }
    }

    override fun getExamples(locale: BaseLocale): List<String> {
        return locale.getWithType("commands.images.briggscover.examples")
    }

    @ExperimentalContracts
    @Subcommand
    suspend fun root(context: LorittaCommandContext, locale: BaseLocale) {
        val image = context.getImageAt(0) ?: run {
            context.reply(
                    LoriReply(
                            context.legacyLocale["NO_VALID_IMAGE"],
                            Constants.ERROR
                    )
            )
            return
        }

        val base = BufferedImage(400, 300, BufferedImage.TYPE_INT_ARGB)
        val skewed = LorittaImage(image)

        skewed.resize(400, 300)

        // skew image
        skewed.setCorners(
                // keep the upper left corner as it is
                242F,67F, // UL

                381F,88F, // UR

                366F,266F, // LR

                218F, 248F) // LL


        val graphics = base.graphics
        graphics.drawImage(skewed.bufferedImage, 0, 0, null)
        graphics.drawImage(TEMPLATE, 0, 0, null)

        context.sendFile(base, "briggs_capa.png", context.getAsMention(true))
    }
}