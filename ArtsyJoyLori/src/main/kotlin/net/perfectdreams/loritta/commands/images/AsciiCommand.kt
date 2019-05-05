package net.perfectdreams.loritta.commands.images

import com.mrpowergamerbr.loritta.utils.locale.BaseLocale
import net.perfectdreams.commands.annotation.Subcommand
import net.perfectdreams.loritta.api.commands.CommandCategory
import net.perfectdreams.loritta.api.commands.LorittaCommand
import net.perfectdreams.loritta.api.commands.LorittaCommandContext
import java.awt.*
import java.awt.image.BufferedImage
import java.util.*

class AsciiCommand : LorittaCommand(arrayOf("ascii"), CommandCategory.IMAGES) {
    override fun getDescription(locale: BaseLocale): String? {
        return locale["commands.images.ascii.description"]
        // TODO: Locales
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
   
    fun strChar(g:Double):String {
        var str = " "
        if (g >= 240)
        {
            str = " "
        }
        else if (g >= 210)
        {
            str = "."
        }
        else if (g >= 190)
        {
            str = "*"
        }
        else if (g >= 170)
        {
            str = "+"
        }
        else if (g >= 120)
        {
            str = "^"
        }
        else if (g >= 110)
        {
            str = "&"
        }
        else if (g >= 80)
        {
            str = "8"
        }
        else if (g >= 60)
        {
            str = "#"
        }
        else
        {
            str = "@"
        }
        return str
    }

    @Subcommand
    suspend fun root(context: LorittaCommandContext, locale: BaseLocale, args: Array<String>) {
        val image = context.getImageAt(0, 0, 0)
        if (image == null) {
            context.explain()
            return
        }
        // Mensagem final
        var fMessage : String = imgtoascii(image)
        
        // Vamos criar uma imagem e escrever o texto 
        val newImage = BufferedImage(image.height, image.width, BufferedImage.TYPE_INT_ARGB)
        val newImageGraph = newImage.graphics as Graphics2D
        newImageGraph.drawString(fMessage, 0, 0)

        context.sendFile(newImage, "ascii.png", context.getAsMention(true))

    }

}
