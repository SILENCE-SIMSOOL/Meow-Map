package silence.simsool.meowmap.ui

import silence.simsool.meowmap.config.Config
import silence.simsool.meowmap.features.MapRender
import silence.simsool.meowmap.utils.Location

class MapElement : MovableGuiElement() {
    override var x: Int by Config::mapX
    override var y: Int by Config::mapY
    override val h: Int
        get() = if (Config.mapShowRunInformation) 142 else 128
    override val w: Int
        get() = 128
    override var scale: Float by Config::mapScale
    override var x2: Int = (x + w * scale).toInt()
    override var y2: Int = (y + h * scale).toInt()

    override fun render() {
        MapRender.renderMap()
    }

    override fun shouldRender(): Boolean {
        if (!Config.mapEnabled) return false
        if (Config.mapHideInBoss && Location.inBoss) return false
        return super.shouldRender()
    }
}
