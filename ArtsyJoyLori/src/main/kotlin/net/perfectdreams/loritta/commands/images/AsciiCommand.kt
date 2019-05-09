package net.perfectdreams.loritta.commands.images

import com.mrpowergamerbr.loritta.utils.locale.BaseLocale
import net.perfectdreams.commands.annotation.Subcommand
import net.perfectdreams.loritta.api.commands.*
import net.perfectdreams.loritta.utils.ImageToAsciiConverter
import java.awt.*
import java.awt.image.BufferedImage
import java.io.File
import java.net.URL
import javax.imageio.ImageIO
import kotlin.contracts.ExperimentalContracts
import kotlin.math.roundToInt


class AsciiCommand : LorittaCommand(arrayOf("ascii"), CommandCategory.IMAGES) {
    override fun getDescription(locale: BaseLocale): String? {
        return locale["commands.images.ascii.description"]
    }

    override fun getUsage(locale: BaseLocale): CommandArguments {
        return arguments {
            argument(ArgumentType.IMAGE) {}
        }
    }

    @ExperimentalContracts
    @Subcommand
    suspend fun root(context: LorittaCommandContext, locale: BaseLocale, args: Array<String>) {
        val image = notNullImage(context.getImageAt(0), context)
        val converter = ImageToAsciiConverter()

        // Vamos criar uma imagem e escrever o texto 
        val newImage = converter.imgToAsciiImg(image)

        context.sendFile(newImage, "ascii.png", context.getAsMention(true))
    }
}