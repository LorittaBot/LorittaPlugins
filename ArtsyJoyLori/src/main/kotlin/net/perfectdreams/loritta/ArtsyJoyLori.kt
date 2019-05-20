package net.perfectdreams.loritta

import com.mrpowergamerbr.loritta.plugin.LorittaPlugin
import net.perfectdreams.loritta.commands.actions.*
import net.perfectdreams.loritta.commands.administration.DashboardCommand
import net.perfectdreams.loritta.commands.administration.RenameChannelCommand
import net.perfectdreams.loritta.commands.discord.RenameEmojiCommand
import net.perfectdreams.loritta.commands.discord.RoleInfoCommand
import net.perfectdreams.loritta.commands.images.*

class ArtsyJoyLori : LorittaPlugin() {
    override fun onEnable() {
        // ADMIN
        registerCommand(DashboardCommand())
        registerCommand(RenameChannelCommand())

        // DISCORD
        registerCommand(RoleInfoCommand())
        registerCommand(RenameEmojiCommand())
        // IMAGES

        registerCommand(AtendenteCommand())
        registerCommand(TristeRealidadeCommand())
        registerCommand(AsciiCommand())
        registerCommand(DrawnWordCommand())
        registerCommand(BriggsCapaCommand())

        // ACTIONS
        registerCommand(AttackCommand())
        registerCommand(DanceCommand())
        registerCommand(HugCommand())
        registerCommand(KissCommand())
        registerCommand(SlapCommand())
        registerCommand(HeadPatCommand())
    }
}