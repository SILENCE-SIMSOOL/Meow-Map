package silence.simsool.meowmap.core.types

import silence.simsool.meowmap.config.Config
import silence.simsool.meowmap.core.RoomData
import java.awt.Color

class Room(override val x: Int, override val z: Int, var data: RoomData) : Tile {
    var core = 0
    var isSeparator = false
    var uniqueRoom: UniqueRoom? = null
    override var state: RoomState = RoomState.UNDISCOVERED
    override val color: Color
        get() = if (state == RoomState.UNOPENED) Config.colorUnopened
        else when (data.type) {
            RoomType.BLOOD -> Config.colorBlood
            RoomType.CHAMPION -> Config.colorMiniboss
            RoomType.ENTRANCE -> Config.colorEntrance
            RoomType.FAIRY -> Config.colorFairy
            RoomType.PUZZLE -> Config.colorPuzzle
            RoomType.RARE -> Config.colorRare
            RoomType.TRAP -> Config.colorTrap
            else -> Config.colorRoom
        }
}
