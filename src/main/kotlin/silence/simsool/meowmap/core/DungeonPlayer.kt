package silence.simsool.meowmap.core

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EnumPlayerModelParts
import net.minecraft.util.ResourceLocation
import silence.simsool.meowmap.MeowMap.scope
import silence.simsool.meowmap.core.types.Room
import silence.simsool.meowmap.features.Dungeon
import silence.simsool.meowmap.utils.APIUtils
import silence.simsool.meowmap.utils.Location
import silence.simsool.meowmap.utils.MapUtils
import silence.simsool.meowmap.utils.Utils

data class DungeonPlayer(val skin: ResourceLocation) {

    var name = ""
    var colorPrefix = 'f'

    val formattedName: String
        get() = "§$colorPrefix$name"

    var mapX = 0
    var mapZ = 0
    var yaw = 0f

    var playerLoaded = false
    var icon = ""
    var renderHat = false
    var dead = false
    var uuid = ""
    var isPlayer = false

    var startingSecrets = 0
    var lastRoom = ""
    var lastTime = 0L
    var roomVisits: MutableList<Pair<Long, String>> = mutableListOf()

    fun setData(player: EntityPlayer) {
        renderHat = player.isWearing(EnumPlayerModelParts.HAT)
        uuid = player.uniqueID.toString()
        playerLoaded = true
        scope.launch(Dispatchers.IO) {
            val secrets = APIUtils.getSecrets(uuid)
            Utils.runMinecraftThread {
                startingSecrets = secrets
            }
        }
    }

    fun getCurrentRoom(): String {
        if (dead) return "Dead"
        if (Location.inBoss) return "Boss"
        val roomSizeWithConnector = MapUtils.roomSize + MapUtils.connectorSize
        val x = (mapX - MapUtils.startCorner.first) / roomSizeWithConnector
        val z = (mapZ - MapUtils.startCorner.second) / roomSizeWithConnector
        return (Dungeon.Info.dungeonList.getOrNull(x * 2 + z * 22) as? Room)?.data?.name ?: "Error"
    }

}
