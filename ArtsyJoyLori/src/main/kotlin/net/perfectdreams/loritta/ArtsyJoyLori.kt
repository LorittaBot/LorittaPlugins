package net.perfectdreams.loritta

import com.mrpowergamerbr.loritta.plugin.LorittaPlugin
import net.perfectdreams.loritta.commands.images.AtendenteCommand
import net.perfectdreams.loritta.commands.images.TristeRealidadeCommand
import net.perfectdreams.loritta.commands.administration.DashboardCommand
import net.perfectdreams.loritta.commands.discord.RenameEmojiCommand
import net.perfectdreams.loritta.commands.discord.RoleInfoCommand
import net.perfectdreams.loritta.commands.images.AsciiCommand

class ArtsyJoyLori : LorittaPlugin() {
    override fun onEnable() {
        // ADMIN
        registerCommand(DashboardCommand())
        // DISCORD
        registerCommand(RoleInfoCommand())
        registerCommand(RenameEmojiCommand())
        // IMAGES
        registerCommand(AtendenteCommand())
        registerCommand(TristeRealidadeCommand())
        // UTILS
        registerCommand(AsciiCommand())
    }
}