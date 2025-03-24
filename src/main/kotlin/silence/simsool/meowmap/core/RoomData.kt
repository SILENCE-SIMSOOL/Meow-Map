package silence.simsool.meowmap.core

import silence.simsool.meowmap.core.types.RoomType

data class RoomData(
    val name: String,
    var type: RoomType,
    val cores: List<Int>,
    val crypts: Int,
    val secrets: Int,
    val trappedChests: Int,
) {
    companion object {
        fun createUnknown(type: RoomType) = RoomData("Unknown", type, emptyList(), 0, 0, 0)
    }
}
