package silence.simsool.meowmap.utils

import gg.essential.universal.UChat
import net.minecraft.item.ItemStack
import net.minecraft.util.StringUtils
import silence.simsool.meowmap.MeowMap.CHAT_PREFIX
import silence.simsool.meowmap.MeowMap.mc


object Utils {
    fun Any?.equalsOneOf(vararg other: Any): Boolean = other.any { this == it }

    fun runMinecraftThread(run: () -> Unit) {
        if (!mc.isCallingFromMinecraftThread) mc.addScheduledTask(run)
        else run()
    }

    fun String.removeFormatting(): String = StringUtils.stripControlCodes(this)

    val ItemStack.itemID: String
        get() = this.getSubCompound("ExtraAttributes", false)?.getString("id") ?: ""
}
