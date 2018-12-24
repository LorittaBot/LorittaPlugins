package net.perfectdreams.loritta

import com.mrpowergamerbr.loritta.plugin.LorittaPlugin
import net.perfectdreams.loritta.commands.images.AtendenteCommand

class ArtsyJoyLori : LorittaPlugin() {
    override fun onEnable() {
        registerCommand(AtendenteCommand())
    }
}