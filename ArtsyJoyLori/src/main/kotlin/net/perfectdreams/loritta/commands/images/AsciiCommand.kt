package net.perfectdreams.loritta.commands.images

import com.mrpowergamerbr.loritta.utils.locale.BaseLocale
import net.perfectdreams.commands.annotation.Subcommand
import net.perfectdreams.loritta.api.commands.CommandCategory
import net.perfectdreams.loritta.api.commands.LorittaCommand
import net.perfectdreams.loritta.api.commands.LorittaCommandContext
import java.awt.Color

class AsciiCommand : LorittaCommand(arrayOf("ascii"), CommandCategory.IMAGES) {
    override fun getDescription(locale: BaseLocale): String? {
        return locale["commands.images.ascii.description"]
        // TODO: Locales
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
        var fMessage : String = ""
        for (h in 0 until image.getHeight()) {
            for (w in 0 until image.getWidth()) {
                val pixcol = Color(image.getRGB(h, w))
                var pixval : Double = 0.toDouble()
                pixval = ((((pixcol.getRed() * 0.30) + (pixcol.getBlue() * 0.59) + ((pixcol
                        .getGreen() * 0.11)))))
                fMessage += strChar(pixval)
            }
        }
        context.sendMessage("```${fMessage}```")

    }

}