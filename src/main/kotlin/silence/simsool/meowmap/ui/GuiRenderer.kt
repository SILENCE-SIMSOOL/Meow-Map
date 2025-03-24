package silence.simsool.meowmap.ui

import net.minecraft.client.gui.ScaledResolution
import net.minecraft.client.renderer.GlStateManager
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import silence.simsool.meowmap.MeowMap.mc
import silence.simsool.meowmap.utils.Location
import silence.simsool.meowmap.utils.RenderUtils

object GuiRenderer {
    val elements = mutableListOf(
        MapElement(),
        ScoreElement()
    )
    private var displayTitle = ""
    private var titleTicks = 0

    fun displayTitle(title: String, ticks: Int) {
        displayTitle = title
        titleTicks = ticks
    }

//    fun clearTitle() {
//        displayTitle = ""
//        titleTicks = 0
//    }

    @SubscribeEvent
    fun onOverlay(event: RenderGameOverlayEvent.Pre) {
        if (event.type != RenderGameOverlayEvent.ElementType.ALL || !Location.inDungeons) return
        if (mc.currentScreen is EditLocationGui) return

        mc.entityRenderer.setupOverlayRendering()

        elements.forEach {
            if (!it.shouldRender()) return@forEach
            GlStateManager.pushMatrix()
            GlStateManager.translate(it.x.toFloat(), it.y.toFloat(), 0f)
            GlStateManager.scale(it.scale, it.scale, 1f)
            it.render()
            GlStateManager.popMatrix()
        }

        if (titleTicks > 0) {
            val sr = ScaledResolution(mc)
            RenderUtils.drawText(
                text = displayTitle,
                x = sr.scaledWidth / 2f,
                y = sr.scaledHeight / 4f,
                scale = 4.0,
                color = 0xFF5555,
                center = true
            )
        }
    }

    fun onTick() {
        if (titleTicks > 0) titleTicks--
    }
}
