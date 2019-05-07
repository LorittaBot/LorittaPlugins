package net.perfectdreams.loritta.commands.images

import com.mrpowergamerbr.loritta.utils.locale.BaseLocale
import net.perfectdreams.commands.annotation.Subcommand
import net.perfectdreams.loritta.api.commands.CommandCategory
import net.perfectdreams.loritta.api.commands.LorittaCommand
import net.perfectdreams.loritta.api.commands.LorittaCommandContext
import java.awt.*
import java.awt.image.BufferedImage
import java.util.*
import kotlin.math.roundToInt


class AsciiCommand : LorittaCommand(arrayOf("ascii"), CommandCategory.IMAGES) {
    override fun getDescription(locale: BaseLocale): String? {
        return locale["commands.images.ascii.description"]
        // TODO: Locales
    }
    fun imgtoasciiimg(oldImage: BufferedImage): BufferedImage {
        val asciiArt =
            imgtoascii(resizeimg(oldImage, (oldImage.width / 5.0).roundToInt(), (oldImage.height / 5.0).roundToInt()))
        val width = (oldImage.width)
        val height = (oldImage.height)
        val newImage = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
        val newImageGraph = newImage.graphics
        newImageGraph.color = Color.LIGHT_GRAY
        newImageGraph.fillRect(0, 0, width, height)
        newImageGraph.color = Color.BLACK
        newImageGraph.font = Font(Font.MONOSPACED, Font.BOLD, 9)
        val asciisplit = asciiArt.split("\n").toTypedArray()
        var t: Int = 0
        for (a in 0 until asciisplit.size) {
            t = t + 5
            newImageGraph.drawString(asciisplit[a], oldImage.minX / 2, oldImage.minY + t)
        }
        newImageGraph.dispose()
        return newImage
    }
 
    fun imgtoascii(img: BufferedImage): String {
        val sb = StringBuilder((img.width + 1) * img.height)
        for (y in 0 until img.height) {
            if (sb.isNotEmpty()) sb.append("\n")
            for (x in 0 until img.width) {
                val pixelColor = Color(img.getRGB(x, y))
                val gValue =
                    pixelColor.red.toDouble() * 0.2989 + pixelColor.blue.toDouble() * 0.5870 + pixelColor.green.toDouble() * 0.1140
                val s = strChar(gValue)
                sb.append(s)
            }
        }
        return sb.toString()
    }

    fun resizeimg(img: BufferedImage, width: Int, height: Int): BufferedImage {
        val resizedImg = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
        val graphics = resizedImg.createGraphics()
        graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR)
        graphics.drawImage(img, 0, 0, width, height, null)
        graphics.dispose()
        return resizedImg
    }
 
 
fun strChar(g: Double): Char {
    val str: Char
    if (g >= 230.0) {
        str = ' '
    } else if (g >= 200.0) {
        str = '.'
    } else if (g >= 180.0) {
        str = '*'
    } else if (g >= 160.0) {
        str = ':'
    } else if (g >= 130.0) {
        str = 'o'
    } else if (g >= 100.0) {
        str = '&'
    } else if (g >= 70.0) {
        str = '8'
    } else if (g >= 50.0) {
        str = '#'
    } else {
        str = '@'
    }
    return str
}
    @Subcommand
    suspend fun root(context: LorittaCommandContext, locale: BaseLocale, args: Array<String>) {
        val image = context.getImageAt(0)
        if (image == null) {
            context.explain()
            return
        }
        // Mensagem final
        
        // Vamos criar uma imagem e escrever o texto 
        val newImage = imgtoasciiimg(image)

        context.sendFile(newImage, "ascii.png", context.getAsMention(true))

    }

}
