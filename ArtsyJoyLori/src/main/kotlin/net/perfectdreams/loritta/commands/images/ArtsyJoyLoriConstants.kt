package net.perfectdreams.loritta.commands.images

import com.mrpowergamerbr.loritta.Loritta
import java.awt.Font
import java.io.File
import java.io.FileInputStream

object ArtsyJoyLoriConstants {
    val BEBAS_NEUE by lazy {
        FileInputStream(File(Loritta.ASSETS + "BebasNeue.otf")).use {
            Font.createFont(Font.TRUETYPE_FONT, it)
        }
    }

    val KOMIKA by lazy {
        FileInputStream(File(Loritta.ASSETS + "komika.ttf")).use {
            Font.createFont(Font.TRUETYPE_FONT, it)
        }
    }
}