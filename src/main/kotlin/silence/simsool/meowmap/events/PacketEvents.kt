package silence.simsool.meowmap.events

import net.minecraft.network.play.server.S02PacketChat
import net.minecraft.network.play.server.S38PacketPlayerListItem
import net.minecraft.network.play.server.S3EPacketTeams
import net.minecraftforge.fml.common.eventhandler.Event
import silence.simsool.meowmap.utils.Utils.removeFormatting

class ChatEvent(val packet: S02PacketChat) : Event() {
    val text: String by lazy { packet.chatComponent.unformattedText.removeFormatting() }
}

class TabListEvent(val packet: S38PacketPlayerListItem) : Event()

class ScoreboardEvent(val packet: S3EPacketTeams) : Event()
