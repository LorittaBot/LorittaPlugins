package net.perfectdreams.loritta

import com.mrpowergamerbr.loritta.plugin.LorittaPlugin
import net.perfectdreams.loritta.commands.`fun`.RandomSAMCommand

class FunnyLori : LorittaPlugin() {
    override fun onEnable() {
        registerCommand(RandomSAMCommand())
    }
}