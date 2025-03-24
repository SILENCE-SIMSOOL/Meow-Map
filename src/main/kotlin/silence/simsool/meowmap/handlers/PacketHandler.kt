package silence.simsool.meowmap.handlers

import net.minecraft.network.Packet
import net.minecraft.network.play.server.S02PacketChat
import net.minecraft.network.play.server.S34PacketMaps
import net.minecraft.network.play.server.S38PacketPlayerListItem
import net.minecraft.network.play.server.S3EPacketTeams
import net.minecraftforge.common.MinecraftForge
import silence.simsool.meowmap.events.ChatEvent
import silence.simsool.meowmap.events.ScoreboardEvent
import silence.simsool.meowmap.events.TabListEvent
import silence.simsool.meowmap.utils.MapUtils

object PacketHandler {
    fun processPacket(packet: Packet<*>) {
        when (packet) {
            is S02PacketChat -> {
                if (packet.type.toInt() != 2) {
                    MinecraftForge.EVENT_BUS.post(ChatEvent(packet))
                }
            }

            is S3EPacketTeams -> {
                MinecraftForge.EVENT_BUS.post(ScoreboardEvent(packet))
            }

            is S38PacketPlayerListItem -> {
                MinecraftForge.EVENT_BUS.post(TabListEvent(packet))
            }

            is S34PacketMaps -> {
				MapUtils.updateMapData(packet)
            }
        }
    }
}
