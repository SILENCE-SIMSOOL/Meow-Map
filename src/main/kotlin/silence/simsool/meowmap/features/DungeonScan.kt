package silence.simsool.meowmap.features

import net.minecraft.init.Blocks
import net.minecraft.util.BlockPos
import silence.simsool.meowmap.MeowMap.mc
import silence.simsool.meowmap.core.types.*
import silence.simsool.meowmap.utils.Location.dungeonFloor
import silence.simsool.meowmap.utils.Utils.equalsOneOf

object DungeonScan {

    const val roomSize = 32
    const val startX = -185
    const val startZ = -185

    private var lastScanTime = 0L
    var isScanning = false
    var hasScanned = false

    val shouldScan: Boolean
        get() = !isScanning && !hasScanned && System.currentTimeMillis() - lastScanTime >= 250 && dungeonFloor != -1

    fun scan() {
        isScanning = true
        var allChunksLoaded = true

        for (x in 0..10) {
            for (z in 0..10) {
                val xPos = startX + x * (roomSize shr 1)
                val zPos = startZ + z * (roomSize shr 1)

                if (!mc.theWorld.getChunkFromChunkCoords(xPos shr 4, zPos shr 4).isLoaded) {
                    allChunksLoaded = false
                    continue
                }

                if (Dungeon.Info.dungeonList[x + z * 11].run {
                        this !is Unknown && (this as? Room)?.data?.name != "Unknown"
                    }) continue

                scanRoom(xPos, zPos, z, x)?.let {
                    val prev = Dungeon.Info.dungeonList[z * 11 + x]
                    if (it is Room) {
                        if ((prev as? Room)?.uniqueRoom != null) prev.uniqueRoom?.addTile(x, z, it)
                        else if (Dungeon.Info.uniqueRooms.none { unique -> unique.name == it.data.name }) UniqueRoom(x, z, it)
                        MapUpdate.roomAdded = true
                    }
                    Dungeon.Info.dungeonList[z * 11 + x] = it
                }
            }
        }

        if (MapUpdate.roomAdded) MapUpdate.updateUniques()

        if (allChunksLoaded) {
            Dungeon.Info.roomCount = Dungeon.Info.dungeonList.filter { it is Room && !it.isSeparator }.size
            hasScanned = true
        }

        lastScanTime = System.currentTimeMillis()
        isScanning = false
    }

    private fun scanRoom(x: Int, z: Int, row: Int, column: Int): Tile? {
        val height = mc.theWorld.getChunkFromChunkCoords(x shr 4, z shr 4).getHeightValue(x and 15, z and 15)
        if (height == 0) return null

        val rowEven = row and 1 == 0
        val columnEven = column and 1 == 0

        return when {
            rowEven && columnEven -> {
                val roomCore = ScanUtils.getCore(x, z)
                Room(x, z, ScanUtils.getRoomData(roomCore) ?: return null).apply {
                    core = roomCore
                }
            }

            !rowEven && !columnEven -> {
                Dungeon.Info.dungeonList[column - 1 + (row - 1) * 11].let {
                    if (it is Room) {
                        Room(x, z, it.data).apply {
                            isSeparator = true
                        }
                    } else null
                }
            }

            height.equalsOneOf(74, 82) -> {
                Door(
                    x, z,
                    type = when (mc.theWorld.getBlockState(BlockPos(x, 69, z)).block) {
                        Blocks.coal_block -> {
                            Dungeon.Info.witherDoors++
                            DoorType.WITHER
                        }

                        Blocks.monster_egg -> DoorType.ENTRANCE
                        Blocks.stained_hardened_clay -> DoorType.BLOOD
                        else -> DoorType.NORMAL
                    }
                )
            }

            else -> {
                Dungeon.Info.dungeonList[if (rowEven) row * 11 + column - 1 else (row - 1) * 11 + column].let {
                    if (it !is Room) null
                    else if (it.data.type == RoomType.ENTRANCE) Door(x, z, DoorType.ENTRANCE)
                    else {
                        Room(x, z, it.data).apply {
                            isSeparator = true
                        }
                    }
                }
            }
        }
    }
}
