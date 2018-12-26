package net.perfectdreams.loritta

import com.mrpowergamerbr.loritta.plugin.LorittaPlugin
import net.perfectdreams.loritta.commands.minecraft.McSignCommand

class MinecraftStuff : LorittaPlugin() {
    override fun onEnable() {
        registerCommand(McSignCommand())
    }
}