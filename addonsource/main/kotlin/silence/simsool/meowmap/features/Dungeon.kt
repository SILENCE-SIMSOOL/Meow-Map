package silence.simsool.meowmap.features

import net.minecraft.event.ClickEvent
import net.minecraftforge.event.world.WorldEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import silence.simsool.meowmap.core.DungeonPlayer
import silence.simsool.meowmap.core.types.*
import silence.simsool.meowmap.events.ChatEvent
import silence.simsool.meowmap.features.Dungeon.Info.ended
import silence.simsool.meowmap.utils.Location
import silence.simsool.meowmap.utils.Location.inDungeons
import silence.simsool.meowmap.MeowMap.mc
import silence.simsool.meowmap.utils.MapUtils
import silence.simsool.meowmap.utils.TabList
import silence.simsool.meowmap.utils.Utils.equalsOneOf
import silence.simsool.meowmap.config.Config
import kotlin.math.roundToInt

object Dungeon {

    val dungeonTeammates = mutableMapOf<String, DungeonPlayer>()
    val espDoors = mutableListOf<Door>()

    private val keyGainRegex = listOf(
        Regex(".+ §r§ehas obtained §r§a§r§.+ Key§r§e!§r"),
        Regex("§r§eA §r§a§r§.+ Key§r§e was picked up!§r")
    )
    private val keyUseRegex = listOf(
        Regex("§r§cThe §r§c§lBLOOD DOOR§r§c has been opened!§r"),
        Regex("§r§a.+§r§a opened a §r§8§lWITHER §r§adoor!§r"),
    )

    fun onTick() {
        if (!inDungeons) return

        if (!MapUtils.calibrated) MapUtils.calibrated = MapUtils.calibrateMap()

        if (MapUtils.mapDataUpdated) {
            MapUpdate.updateRooms()
            MapUtils.mapDataUpdated = false
        }

        if (Location.dungeonFloor.equalsOneOf(6, 7)) MimicDetector.checkMimicDead()
        ScoreCalculation.updateScore()

        TabList.getDungeonTabList()?.let {
            MapUpdate.updatePlayers(it)
            RunInformation.updatePuzzleCount(it)
        }

        if (DungeonScan.shouldScan) {
            DungeonScan.scan()
        }

        if (Info.startTime == 0L) {
            val playerPosX = mc.thePlayer.posX.toInt()
            val playerPosZ = mc.thePlayer.posZ.toInt()

            // 플레이어의 위치를 기반으로 11x11 격자 인덱스 계산
            val xIndex = ((playerPosX - DungeonScan.startX) / 32f).roundToInt() * 2
            val zIndex = ((playerPosZ - DungeonScan.startZ) / 32f).roundToInt() * 2

            if (xIndex in 0..10 && zIndex in 0..10) {
                val index = zIndex * 11 + xIndex
                val tile = Info.dungeonList[index]

                if (tile is Room && tile.uniqueRoom != null) {
                    // UniqueRoom을 찾았으므로 해당 방의 모든 타일 상태 변경
                    tile.uniqueRoom!!.tiles.forEach { (uniqueTile, _) ->
                        if (uniqueTile.state == RoomState.UNDISCOVERED) {
                            uniqueTile.state = RoomState.DISCOVERED
                        }
                    }

                    // 방 주변의 문 상태 변경
                    val neighbors = listOf(
                        Info.dungeonList.getOrNull(index + 1), // Right
                        Info.dungeonList.getOrNull(index - 1), // Left
                        Info.dungeonList.getOrNull(index + 11),// Down
                        Info.dungeonList.getOrNull(index - 11) // Up
                    )

                    neighbors.forEach { neighbor ->
                        if (neighbor is Door && neighbor.state == RoomState.UNDISCOVERED) {
                            neighbor.state = RoomState.DISCOVERED
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    fun onChatPacket(event: ChatEvent) {
        if (!inDungeons) return
        if (event.packet.chatComponent.siblings.any {
                it.chatStyle?.chatClickEvent?.run {
                    action == ClickEvent.Action.RUN_COMMAND && value == "/showextrastats"
                } == true
            }) {
            ended = true
            if (Config.teamInfo) PlayerTracker.onDungeonEnd()
        }

        if (keyGainRegex.any { it.matches(event.packet.chatComponent.formattedText) }) Info.keys++

        if (keyUseRegex.any { it.matches(event.packet.chatComponent.formattedText) }) Info.keys--

        when (event.text) {
            "Starting in 4 seconds." -> MapUpdate.preloadHeads()
            "[NPC] Mort: Here, I found this map when I first entered the dungeon." -> {
                MapUpdate.getPlayers()
                Info.startTime = System.currentTimeMillis()
            }
        }
    }

    @SubscribeEvent
    fun onWorldLoad(event: WorldEvent.Unload) {
        reset()
    }

    fun reset() {
        Info.reset()
        dungeonTeammates.clear()
        espDoors.clear()
        PlayerTracker.roomClears.clear()
        MapUtils.calibrated = false
        MapUtils.mapData = null
        DungeonScan.hasScanned = false
        RunInformation.reset()
    }

    object Info {
        val dungeonList = Array<Tile>(121) { Unknown(0, 0) }
        val uniqueRooms = mutableSetOf<UniqueRoom>()
        var roomCount = 0
        val puzzles = mutableMapOf<Puzzle, Boolean>()

        var trapType = ""
        var witherDoors = 0
        var cryptCount = 0
        var secretCount = 0
        var mimicFound = false

        var startTime = 0L
        var ended = false
        var keys = 0
        fun reset() {
            dungeonList.fill(Unknown(0, 0))
            uniqueRooms.clear()
            roomCount = 0
            puzzles.clear()

            trapType = ""
            witherDoors = 0
            cryptCount = 0
            secretCount = 0
            mimicFound = false

            startTime = 0L
            ended = false
            keys = 0
        }
    }

}