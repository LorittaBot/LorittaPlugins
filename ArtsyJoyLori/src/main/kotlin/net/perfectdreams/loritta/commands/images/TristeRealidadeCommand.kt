package net.perfectdreams.loritta.commands.images

import com.mrpowergamerbr.loritta.Loritta
import com.mrpowergamerbr.loritta.commands.CommandContext
import com.mrpowergamerbr.loritta.dao.Profile
import com.mrpowergamerbr.loritta.dao.ProfileSettings
import com.mrpowergamerbr.loritta.network.Databases
import com.mrpowergamerbr.loritta.utils.Constants
import com.mrpowergamerbr.loritta.utils.ImageUtils
import com.mrpowergamerbr.loritta.utils.LorittaUtils
import com.mrpowergamerbr.loritta.utils.locale.BaseLocale
import com.mrpowergamerbr.loritta.utils.locale.Gender
import com.mrpowergamerbr.loritta.utils.locale.PersonalPronoun
import com.mrpowergamerbr.loritta.utils.loritta
import kotlinx.html.InputType
import net.dv8tion.jda.core.OnlineStatus
import net.dv8tion.jda.core.entities.User
import net.perfectdreams.commands.annotation.Subcommand
import net.perfectdreams.loritta.api.commands.CommandCategory
import net.perfectdreams.loritta.api.commands.LorittaCommand
import net.perfectdreams.loritta.api.impl.DiscordCommandContext
import org.jetbrains.exposed.sql.transactions.transaction
import java.awt.*
import java.awt.image.BufferedImage
import java.io.File
import java.util.ArrayList
import javax.imageio.ImageIO

class TristeRealidadeCommand : LorittaCommand(arrayOf("sadreality", "tristerealidade"), CommandCategory.IMAGES) {
    override val needsToUploadFiles = true

    @Subcommand
    suspend fun root(context: DiscordCommandContext, locale: BaseLocale) {
        var x = 0
        var y = 0

        val base = BufferedImage(384, 256, BufferedImage.TYPE_INT_ARGB) // Iremos criar uma imagem 384x256 (tamanho do template)
        val baseGraph = base.graphics as Graphics2D
        baseGraph.setRenderingHint(
            java.awt.RenderingHints.KEY_TEXT_ANTIALIASING,
            java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_ON)

        val users = ArrayList<User>()
        val members = context.guild.members.filter { it.onlineStatus != OnlineStatus.OFFLINE && it.user.avatarUrl != null && !it.user.isBot }.toMutableList()

        users.addAll(context.discordMessage.mentionedUsers)

        while (6 > users.size) {
            val member = if (members.isEmpty()) {
                // omg
                context.guild.members[Loritta.RANDOM.nextInt(context.guild.members.size)]
            } else {
                members[Loritta.RANDOM.nextInt(members.size)]
            }

            users.add(member.user)
            members.remove(member)
        }

        var lovedGender = transaction(Databases.loritta) {
            val profile = loritta.getOrCreateLorittaProfile(users[0].idLong)
            profile.settings.gender
        }

        if (lovedGender == Gender.UNKNOWN)
            lovedGender = Gender.FEMALE

        var aux = 0
        while (6 > aux) {
            val member = users[0]

            val avatarImg = LorittaUtils.downloadImage(member.effectiveAvatarUrl)!!.getScaledInstance(128, 128, Image.SCALE_SMOOTH)
            baseGraph.drawImage(avatarImg, x, y, null)

            baseGraph.font = Constants.MINECRAFTIA.deriveFont(Font.PLAIN, 8f)
            baseGraph.color = Color.BLACK
            baseGraph.drawString(member.name + "#" + member.discriminator, x + 1, y + 12)
            baseGraph.drawString(member.name + "#" + member.discriminator, x + 1, y + 14)
            baseGraph.drawString(member.name + "#" + member.discriminator, x, y + 13)
            baseGraph.drawString(member.name + "#" + member.discriminator, x + 2, y + 13)
            baseGraph.color = Color.WHITE
            baseGraph.drawString(member.name + "#" + member.discriminator, x + 1, y + 13)

            baseGraph.font = ArtsyJoyLoriConstants.BEBAS_NEUE.deriveFont(22f)
            var gender = transaction(Databases.loritta) {
                val profile = loritta.getOrCreateLorittaProfile(member.idLong)
                profile.settings.gender
            }
            if (gender == Gender.UNKNOWN)
                gender = Gender.MALE
            if (aux == 0)
                gender = lovedGender

            drawCentralizedTextOutlined(
                baseGraph,
                locale["commands.images.tristerealidade.slot.$aux.${gender.name}", lovedGender.getPossessivePronoun(locale, PersonalPronoun.THIRD_PERSON, member.name)],
                Rectangle(x, y + 80, 128, 42),
                Color.WHITE,
                Color.BLACK,
                2
            )

            x += 128
            if (x > 256) {
                x = 0
                y = 128
            }
            aux++
            users.removeAt(0)
        }

        context.sendFile(base, "sad_reality.png", context.getAsMention(true))
    }

    fun drawCentralizedTextOutlined(graphics: Graphics, text: String, rectangle: Rectangle, fontColor: Color, strokeColor: Color, strokeSize: Int) {
        val font = graphics.font
        graphics.font = font
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

            if (newX >= rectangle.width) {
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
        var y = (rectangle.height / 2) - ((skipHeight - 4) * (lines.size - 1))
        for (line in lines) {
            graphics.color = strokeColor
            for (strokeX in rectangle.x - strokeSize .. rectangle.x + strokeSize) {
                for (strokeY in rectangle.y + y - strokeSize .. rectangle.y + y + strokeSize) {
                    ImageUtils.drawCenteredStringEmoji(graphics, line, Rectangle(strokeX, strokeY, rectangle.width, 24), font)
                }
            }
            graphics.color = fontColor
            ImageUtils.drawCenteredStringEmoji(graphics, line, Rectangle(rectangle.x, rectangle.y + y, rectangle.width, 24), font)
            y += skipHeight
        }
    }
}