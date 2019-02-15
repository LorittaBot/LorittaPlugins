package net.perfectdreams.loritta.commands.images

import com.mrpowergamerbr.loritta.Loritta
import com.mrpowergamerbr.loritta.utils.ImageUtils
import com.mrpowergamerbr.loritta.utils.locale.BaseLocale
import com.mrpowergamerbr.loritta.utils.substringIfNeeded
import net.perfectdreams.commands.annotation.Subcommand
import net.perfectdreams.loritta.api.commands.*
import java.awt.Color
import java.awt.FontMetrics
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

class DrawnWordCommand : LorittaCommand(arrayOf("drawnword"), CommandCategory.IMAGES) {
    override val needsToUploadFiles = true

    override fun getDescription(locale: BaseLocale): String? {
        return locale["commands.images.drawnword.description"]
    }

    override fun getUsage(locale: BaseLocale): CommandArguments {
        return arguments {
            argument(ArgumentType.TEXT) {}
        }
    }

    override fun getExamples(locale: BaseLocale): List<String> {
        return locale.getWithType("commands.images.drawnword.examples")
    }

    @Subcommand
    suspend fun root(context: LorittaCommandContext, locale: BaseLocale, args: Array<String>) {
        if (args.isNotEmpty()) {
            val text = args.joinToString(" ").substringIfNeeded(0..800)

            fun getTextWrapSpacesRequiredHeight(text: String, startX: Int, startY: Int, endX: Int, endY: Int, fontMetrics: FontMetrics, graphics: Graphics): Int {
                val lineHeight = fontMetrics.height // Aqui é a altura da nossa fonte

                var currentX = startX // X atual
                var currentY = startY // Y atual

                val split = text.split("((?<= )|(?= ))".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray() // Nós precisamos deixar os espaços entre os splits!
                for (str in split) {
                    var width = fontMetrics.stringWidth(str) // Width do texto que nós queremos colocar
                    if (currentX + width > endX) { // Se o currentX é maior que o endX... (Nós usamos currentX + width para verificar "ahead of time")
                        currentX = startX // Nós iremos fazer wrapping do texto
                        currentY += lineHeight
                    }
                    var idx = 0
                    for (c in str.toCharArray()) { // E agora nós iremos printar todos os chars
                        idx++
                        if (c == '\n') {
                            currentX = startX // Nós iremos fazer wrapping do texto
                            currentY += lineHeight
                            continue
                        }
                        width = fontMetrics.charWidth(c)
                        if (!graphics.font.canDisplay(c)) {
                            // Talvez seja um emoji!
                            val emoteImage = ImageUtils.getTwitterEmoji(str, idx)
                            if (emoteImage != null) {
                                // graphics.drawImage(emoteImage.getScaledInstance(width, width, BufferedImage.SCALE_SMOOTH), currentX, currentY - width, null)
                                currentX += width
                            }

                            continue
                        }
                        // graphics.drawString(c.toString(), currentX, currentY) // Escreva o char na imagem
                        currentX += width // E adicione o width no nosso currentX
                    }
                }
                return currentY
            }

            val drawnMaskWordImage  = ImageIO.read(File(Loritta.ASSETS, "drawn_mask_word.png"))
            val drawnMaskWordBottomImage  = ImageIO.read(File(Loritta.ASSETS, "drawn_mask_word_bottom.png"))
            val babyMaskChairImage = ImageIO.read(File(Loritta.ASSETS, "baby_mask_chair.png"))

            var wordScreenHeight = drawnMaskWordImage.height

            val width = 468

            val graphics = drawnMaskWordImage.graphics as Graphics2D
            graphics.setRenderingHint(
                    java.awt.RenderingHints.KEY_TEXT_ANTIALIASING,
                    java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_ON
            )

            val font2 = graphics.font.deriveFont(24f)
            graphics.font = font2
            val fontMetrics = graphics.fontMetrics

            val lineHeight = fontMetrics.height // Aqui é a altura da nossa fonte

            val startY = 90

            val currentY = getTextWrapSpacesRequiredHeight(
                    text,
                    54,
                    90,
                    drawnMaskWordImage.width,
                    99999,
                    fontMetrics,
                    graphics
            )

            val currentJumps = (currentY - startY) / lineHeight

            val pixelsNeeded = currentY - startY

            if (currentJumps > 4) {
                val overflownPixels = (pixelsNeeded - (lineHeight * 3)) + lineHeight + lineHeight // Esse + lineHeight é por causa que os pixels da primeira linha overflow não são considerados
                val requiredPastes = (overflownPixels / 53)

                wordScreenHeight += (53 * requiredPastes) - 27
                // wordScreenHeight += 13
            }

            val wordScreen = BufferedImage(drawnMaskWordImage.width, wordScreenHeight, BufferedImage.TYPE_INT_ARGB)
            val wordScreenGraphics = wordScreen.graphics as Graphics2D
            wordScreenGraphics.setRenderingHint(
                    java.awt.RenderingHints.KEY_TEXT_ANTIALIASING,
                    java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_ON
            )

            wordScreenGraphics.drawImage(drawnMaskWordImage, 0, 0, null)


            wordScreenGraphics.color = Color.BLACK
            val font = wordScreenGraphics.font.deriveFont(24f)


            wordScreenGraphics.font = font2
            val fontMetrics2 = wordScreenGraphics.fontMetrics

            if (currentJumps > 4) {
                val overflownPixels = (pixelsNeeded - (lineHeight * 3)) + lineHeight + lineHeight // + lineHeight + lineHeight // Esse + lineHeight é por causa que os pixels da primeira linha overflow não são considerados
                val requiredPastes = (overflownPixels / 53)

                var currentY = (drawnMaskWordImage.height - 40)

                repeat(requiredPastes) {
                    wordScreenGraphics.drawImage(drawnMaskWordBottomImage, 0, currentY, null)
                    // wordScreenGraphics.drawLine(0, currentY, 250, currentY)

                    currentY += 53
                }
            }

            ImageUtils.drawTextWrapSpaces(
                    text,
                    54,
                    90,
                    wordScreen.width,
                    99999,
                    fontMetrics2,
                    wordScreenGraphics
            )

            val image = BufferedImage(width, wordScreen.height + 202, BufferedImage.TYPE_INT_ARGB)
            val imageGraphics = image.graphics

            imageGraphics.fillRect(0, 0, 468, wordScreen.height + 202)
            imageGraphics.drawImage(wordScreen, 218, 0, null)
            imageGraphics.drawImage(babyMaskChairImage, 0, image.height - babyMaskChairImage.height, null)

            context.sendFile(image, "drawn_word.png", context.getAsMention(true))
        } else {
            context.explain()
        }
    }
}