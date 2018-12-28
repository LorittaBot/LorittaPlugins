package net.perfectdreams.loritta

import com.mrpowergamerbr.loritta.plugin.LorittaPlugin
import net.perfectdreams.loritta.commands.images.AtendenteCommand
import net.perfectdreams.loritta.commands.images.TristeRealidadeCommand
import net.perfectdreams.loritta.commands.administration.DashboardCommand

class ArtsyJoyLori : LorittaPlugin() {
    override fun onEnable() {
        // ADMIN
        registerCommand(DashboardCommand())
        // IMAGES
        registerCommand(AtendenteCommand())
        registerCommand(TristeRealidadeCommand())
    }
}