package net.perfectdreams.loritta.commands.images

import com.mrpowergamerbr.loritta.utils.locale.BaseLocale
import net.perfectdreams.commands.annotation.Subcommand
import net.perfectdreams.loritta.api.commands.*
import net.perfectdreams.loritta.utils.ImageToAsciiConverter
import java.awt.image.BufferedImage
import kotlin.contracts.ExperimentalContracts


class AsciiCommand : LorittaCommand(arrayOf("ascii", "asciiart", "img2ascii", "img2asciiart", "image2ascii"), CommandCategory.IMAGES) {
    override val needsToUploadFiles: Boolean = true

    override fun getDescription(locale: BaseLocale): String? {
        return locale["commands.images.ascii.description"]
    }

    override fun getExamples(locale: BaseLocale): List<String> {
        return listOf(
                "@Loritta",
                "colorize",
                "dither",
                "@Loritta colorize dither"
        )
    }

    override fun getUsage(locale: BaseLocale): CommandArguments {
        return arguments {
            argument(ArgumentType.IMAGE) {}
        }
    }

    @ExperimentalContracts
    @Subcommand
    suspend fun root(context: LorittaCommandContext, locale: BaseLocale, image: BufferedImage?, args: Array<String>) {
        val img = notNullImage(image, context)
        val options = mutableSetOf<ImageToAsciiConverter.AsciiOptions>()
        for (arg in args) {
            try {
                options.add(ImageToAsciiConverter.AsciiOptions.valueOf(arg.toUpperCase()))
            } catch (e: IllegalArgumentException) {
            }
        }
        val converter = ImageToAsciiConverter(*options.toTypedArray())
        val newImage = converter.imgToAsciiImg(img)

        context.sendFile(newImage, "asciiart.png", context.getAsMention(true))
    }
}