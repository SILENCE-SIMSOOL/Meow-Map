package silence.simsool.meowmap.features

import gg.essential.universal.UChat
import net.minecraft.event.HoverEvent
import net.minecraft.util.ChatComponentText
import net.minecraft.util.ChatStyle
import net.minecraft.util.IChatComponent
import silence.simsool.meowmap.core.DungeonPlayer
import silence.simsool.meowmap.core.RoomData
import silence.simsool.meowmap.core.types.Room
import silence.simsool.meowmap.core.types.RoomState
import silence.simsool.meowmap.core.types.RoomType
import silence.simsool.meowmap.core.types.Tile
import silence.simsool.meowmap.utils.Utils.equalsOneOf
import silence.simsool.meowmap.utils.APIUtils
import silence.simsool.meowmap.MeowMap.scope
import silence.simsool.meowmap.MeowMap.mc
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

object PlayerTracker {

    val roomClears: MutableMap<RoomData, Set<String>> = mutableMapOf()

    fun roomStateChange(room: Tile, state: RoomState, newState: RoomState) {
        if (room !is Room) return
        if (newState.equalsOneOf(RoomState.CLEARED, RoomState.GREEN) && state != RoomState.CLEARED) {
            val currentRooms =  Dungeon.dungeonTeammates.map { Pair(it.value.formattedName, it.value.getCurrentRoom()) }
            roomClears[room.data] =  currentRooms.filter { it.first != "" && it.second == room.data.name }.map { it.first }.toSet()
        }
    }

    fun onDungeonEnd() {
        val time = System.currentTimeMillis() - Dungeon.Info.startTime
        Dungeon.dungeonTeammates.forEach {
            it.value.roomVisits.add(Pair(time - it.value.lastTime, it.value.lastRoom))
        }

        scope.launch {
            val results = Dungeon.dungeonTeammates.map { (_, player) ->
                async(Dispatchers.IO) {
                    Triple(
                        player.formattedName,
                        player,
                        APIUtils.getSecrets(player.uuid)
                    )
                }
            }.map {
                val (name, player, secrets) = it.await()
                getStatMessage(name, player, secrets)
            }
            UChat.chat("\n §7§lTeam Info >")
            results.forEach {
                mc.thePlayer.addChatMessage(it)
            }
            UChat.chat("")
        }
    }

    fun getStatMessage(name: String, player: DungeonPlayer, secrets: Int): IChatComponent {
        val secretsComponent = ChatComponentText("§7found §e${secrets - player.startingSecrets} §fSecrets ")

        val allClearedRooms = roomClears.filter { it.value.contains(name) }
        val soloClearedRooms = allClearedRooms.filter { it.value.size == 1 }
        val max = allClearedRooms.size
        val min = soloClearedRooms.size

        val roomComponent = ChatComponentText("§7and cleared §e${if (soloClearedRooms.size != allClearedRooms.size) "$min-$max" else max} §fRooms!")
            .apply {
            chatStyle =
                ChatStyle().setChatHoverEvent(HoverEvent(HoverEvent.Action.SHOW_TEXT, ChatComponentText(
                    roomClears.entries.filter {
                        !it.key.type.equalsOneOf(RoomType.BLOOD, RoomType.ENTRANCE, RoomType.FAIRY) && it.value.contains(name)
                    }.joinToString(
                        separator = "\n",
                        prefix = "$name's §eRooms Cleared:\n"
                    ) { (room, players) ->
                        if (players.size == 1) {
                            "§6${room.name}"
                        } else {
                            "§6${room.name} §7with ${
                                players.filter { it != name }.joinToString(separator = "§r, ")
                            }"
                        }
                    }
                )))
        }

        return ChatComponentText(" §8- §e$name ")
            .appendSibling(secretsComponent)
            .appendSibling(roomComponent)
    }
}