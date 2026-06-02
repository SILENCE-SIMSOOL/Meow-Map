package silence.simsool.meowmap.features

import net.minecraft.client.renderer.GlStateManager
import net.minecraft.util.AxisAlignedBB
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import silence.simsool.meowmap.config.Config
import silence.simsool.meowmap.core.types.RoomState
import silence.simsool.meowmap.utils.Location.inBoss
import silence.simsool.meowmap.utils.Location.inDungeons
import silence.simsool.meowmap.utils.RenderUtils
import silenceaddon.events.WorldRenderEvent

object BoxWitherDoor {

    @SubscribeEvent
    fun onRender(event: WorldRenderEvent) {
        if (!inDungeons || inBoss || !Config.witherDoorESP) return

        val hasKeys = Dungeon.Info.keys > 0
        val keyColor = if (hasKeys) Config.witherDoorKeyColor else Config.witherDoorNoKeyColor
        val outlineWidth = Config.witherDoorOutlineWidth
        val showOutline = Config.witherDoorOutline
        val showFill = Config.witherDoorFill

        GlStateManager.pushMatrix()
        GlStateManager.translate(-event.renderX, -event.renderY, -event.renderZ)
        GlStateManager.alphaFunc(516, 0.01F)
        RenderUtils.preDraw()
        for (door in Dungeon.espDoors) {
            if (door.state != RoomState.UNDISCOVERED) {
                val aabb = AxisAlignedBB(
                    door.x - 1.0, 69.0, door.z - 1.0,
                    door.x + 2.0, 73.0, door.z + 2.0
                )
                RenderUtils.drawBox(aabb, keyColor, outlineWidth, showOutline, showFill, true)
            }
        }
        RenderUtils.postDraw()
        GlStateManager.popMatrix()
    }

}