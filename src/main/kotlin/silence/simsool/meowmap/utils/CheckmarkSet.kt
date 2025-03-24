package silence.simsool.meowmap.utils

import net.minecraft.client.renderer.texture.SimpleTexture
import net.minecraft.util.ResourceLocation
import silence.simsool.meowmap.MeowMap.mc
import silence.simsool.meowmap.core.types.RoomState

class CheckmarkSet(val size: Int, location: String) {
    private val crossResource = ResourceLocation("meowmap", "$location/cross.png")
    private val greenResource = ResourceLocation("meowmap", "$location/green_check.png")
    private val questionResource = ResourceLocation("meowmap", "$location/question.png")
    private val whiteResource = ResourceLocation("meowmap", "$location/white_check.png")

    init {
        listOf(crossResource, greenResource, questionResource, whiteResource).forEach {
            mc.textureManager.loadTexture(it, SimpleTexture(it))
        }
    }

    fun getCheckmark(state: RoomState): ResourceLocation? {
        return when (state) {
            RoomState.CLEARED -> whiteResource
            RoomState.GREEN -> greenResource
            RoomState.FAILED -> crossResource
            RoomState.UNOPENED -> questionResource
            else -> null
        }
    }
}
