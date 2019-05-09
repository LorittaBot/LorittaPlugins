package net.perfectdreams.loritta.utils

import com.mrpowergamerbr.loritta.utils.loritta
import java.awt.Color
import java.awt.Font
import java.awt.RenderingHints
import java.awt.image.BufferedImage
import java.io.File
import kotlin.math.roundToInt

class ImageToAsciiConverter(private vararg val options: AsciiOptions) {
    enum class AsciiOptions {
        COLORIZE, DITHER
    }

    fun imgToAsciiImg(oldImage: BufferedImage): BufferedImage {
        val resizedImage = resizeImg(oldImage, (oldImage.width / 6.5).roundToInt(), (oldImage.height / 6.5).roundToInt())
        val asciiArt = imgToAscii(resizedImage)

        val asciiSplit = asciiArt.split("\n")
        // Largura é o tamanho da maior string * 10 (tamanho da fonte!)
        val width = (asciiSplit.maxBy { it.length }?.length ?: 1) * 10
        // A altura é a quantidade de linhas * 11 (o tamanho da fonte é 18, mas a gente subtrai para ficar mais "compacto")
        val height = asciiSplit.size * 11
        val newImage = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
        val newImageGraph = newImage.graphics

        // Colocar um background cinza (cor do tema escuro do Discord)
        newImageGraph.color = Color(54, 57, 63)
        newImageGraph.fillRect(0, 0, width, height)
        newImageGraph.color = Color.BLACK
        newImageGraph.font = Font.createFont(Font.PLAIN, File(loritta.config.loritta.folders.assets, "MorePerfectDOSVGA.ttf")).deriveFont(18f)

        val fontMetrics = newImageGraph.fontMetrics
        var x = 0
        var y = 0
        val fontHeight = fontMetrics.height - 8
        for (string in asciiSplit) {
            y += fontHeight
            for (char in string) {
                if (AsciiOptions.COLORIZE in options) {
                    val queryX = x * (resizedImage.width / width.toDouble())
                    val queryY = (y - fontHeight) * (resizedImage.height / height.toDouble())

                    newImageGraph.color = Color(resizedImage.getRGB(queryX.toInt(), queryY.toInt()))
                }

                newImageGraph.drawString(char.toString(), x, y)
                x += newImageGraph.fontMetrics.charWidth(char)
            }
            x = 0
        }

        newImageGraph.dispose()
        return newImage
    }

    private fun imgToAscii(img: BufferedImage): String {
        val sb = StringBuilder((img.width + 1) * img.height)

        for (y in 0 until img.height) {
            if (sb.isNotEmpty()) sb.append("\n")
            for (x in 0 until img.width) {
                val pixelColor = Color(img.getRGB(x, y), true)

                val s = when {
                    // Transparent Pixel
                    pixelColor.alpha == 0 -> ' '

                    AsciiOptions.DITHER in options -> {
                        val gValue = pixelColor.red.toDouble() * 0.2989 + pixelColor.blue.toDouble() * 0.5870 + pixelColor.green.toDouble() * 0.1140
                        strCharDither(gValue)
                    }

                    AsciiOptions.COLORIZE in options -> '@'

                    else -> {
                        val gValue = pixelColor.red.toDouble() * 0.2989 + pixelColor.blue.toDouble() * 0.5870 + pixelColor.green.toDouble() * 0.1140
                        strChar(gValue)
                    }
                }

                sb.append(s)
            }
        }

        return sb.toString()
    }

    private fun resizeImg(img: BufferedImage, width: Int, height: Int): BufferedImage {
        val resizedImg = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
        val graphics = resizedImg.createGraphics()
        graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR)
        graphics.drawImage(img, 0, 0, width, height, null)
        graphics.dispose()
        return resizedImg
    }

    private fun strChar(g: Double): Char {
        return when {
            g >= 230.0 -> ' '
            g >= 200.0 -> '.'
            g >= 180.0 -> '*'
            g >= 160.0 -> ':'
            g >= 130.0 -> 'o'
            g >= 100.0 -> '&'
            g >= 70.0 -> '8'
            g >= 50.0 -> '#'
            else -> '@'
        }
    }

    private fun strCharDither(g: Double): Char {
        return when {
            g >= 230.0 -> ' '
            g >= 200.0 -> ' '
            g >= 180.0 -> '░'
            g >= 160.0 -> '▒'
            g >= 130.0 -> '▒'
            g >= 100.0 -> '▓'
            g >= 70.0 -> '▓'
            g >= 50.0 -> '█'
            else -> '█'
        }
    }
}