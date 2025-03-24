package silence.simsool.meowmap.core.types

import java.awt.Color

interface Tile {
    val x: Int
    val z: Int
    var state: RoomState
    val color: Color
}
