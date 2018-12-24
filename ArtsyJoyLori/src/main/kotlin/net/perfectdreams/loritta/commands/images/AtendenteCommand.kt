package net.perfectdreams.loritta.commands.images

import com.mrpowergamerbr.loritta.utils.ImageUtils
import com.mrpowergamerbr.loritta.utils.LorittaImage
import com.mrpowergamerbr.loritta.utils.locale.BaseLocale
import com.mrpowergamerbr.loritta.utils.locale.LegacyBaseLocale
import net.perfectdreams.commands.annotation.Subcommand
import net.perfectdreams.loritta.api.commands.*
import java.awt.Color
import java.awt.Graphics2D
import java.awt.Rectangle
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

class AtendenteCommand : LorittaCommand(arrayOf("atendente"), CommandCategory.IMAGES) {
    override fun getDescription(locale: BaseLocale): String? {
        return locale["commands.images.atendente.description"]
    }

    override fun getUsage(locale: BaseLocale): CommandArguments {
        return arguments {
            argument(ArgumentType.TEXT) {}
        }
    }

    override fun getExamples(locale: BaseLocale): List<String> {
        return locale.getWithType("commands.images.atendente.examples")
    }

    @Subcommand
    suspend fun root(context: LorittaCommandContext, locale: BaseLocale, args: Array<String>) {
        if (args.isNotEmpty()) {
            val template = ImageIO.read(File(com.mrpowergamerbr.loritta.Loritta.ASSETS, "atendente.png"))

            val width = 214
            val height = 131

            val text = args.joinToString(" ")

            val templateGraphics = template.graphics

            val image = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
            val graphics = image.graphics as Graphics2D
            graphics.setRenderingHint(
                java.awt.RenderingHints.KEY_TEXT_ANTIALIASING,
                java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_ON)
            // graphics.color = Color.WHITE
            // graphics.fillRect(0, 0, width, height)
            val font = ArtsyJoyLoriConstants.KOMIKA.deriveFont(16f)

            graphics.font = font
            graphics.color = Color.BLACK
            val fontMetrics = graphics.fontMetrics

            // Para escrever uma imagem centralizada, nós precisamos primeiro saber algumas coisas sobre o texto

            // Lista contendo (texto, posição)
            val lines = mutableListOf<String>()

            // Se está centralizado verticalmente ou não, por enquanto não importa para a gente
            val split = text.split(" ")

            var x = 0
            var currentLine = StringBuilder()

            for (string in split) {
                val stringWidth = fontMetrics.stringWidth("$string ")
                val newX = x + stringWidth

                if (newX >= width) {
                    var endResult = currentLine.toString().trim()
                    if (endResult.isEmpty()) { // okay wtf
                        // Se o texto é grande demais e o conteúdo atual está vazio... bem... substitua o endResult pela string atual
                        endResult = string
                        lines.add(endResult)
                        x = 0
                        continue
                    }
                    lines.add(endResult)
                    currentLine = StringBuilder()
                    currentLine.append(' ')
                    currentLine.append(string)
                    x = fontMetrics.stringWidth("$string ")
                } else {
                    currentLine.append(' ')
                    currentLine.append(string)
                    x = newX
                }
            }
            lines.add(currentLine.toString().trim())

            // got it!!!
            // bem, supondo que cada fonte tem 22f de altura...

            // para centralizar é mais complicado
            val skipHeight = fontMetrics.ascent
            var y = (height / 2) - ((skipHeight - 10) * (lines.size - 1))
            for (line in lines) {
                ImageUtils.drawCenteredStringEmoji(graphics, line, Rectangle(0, y, width, 24), font)
                y += skipHeight
            }

            val loriImage = LorittaImage(image)
            loriImage.rotate(8.0)
            templateGraphics.drawImage(loriImage.bufferedImage, 46, -20, null)

            context.sendFile(template, "atendente.png", context.getAsMention(true))
        } else {
            context.explain()
        }
    }
}