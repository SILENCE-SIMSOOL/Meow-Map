package silence.simsool.meowmap

import kotlinx.coroutines.CoroutineScope
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiScreen
import net.minecraftforge.client.ClientCommandHandler
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import silence.simsool.meowmap.commands.Help
import silence.simsool.meowmap.config.Config
import silence.simsool.meowmap.features.BoxWitherDoor
import silence.simsool.meowmap.features.Dungeon
import silence.simsool.meowmap.features.RunInformation
import silence.simsool.meowmap.ui.GuiRenderer
import silence.simsool.meowmap.utils.Location
import silence.simsool.meowmap.utils.RenderUtils
import kotlin.coroutines.EmptyCoroutineContext

@Mod(
    modid = MeowMap.MODID,
    name = MeowMap.NAME,
    version = MeowMap.VERSION,
    modLanguageAdapter = "silence.simsool.meowmap.kotlin.KotlinAdapter"
)
object MeowMap {
    const val MODID = "meowmap"
    const val NAME = "Meow Map"
    const val VERSION = "1.0.0"
    val CHAT_PREFIX: String
        get() = "§e[§fMeow§e]"

    val mc: Minecraft = Minecraft.getMinecraft()
    var display: GuiScreen? = null
    val scope = CoroutineScope(EmptyCoroutineContext)

    @Mod.EventHandler
    fun onInit(event: FMLInitializationEvent) {
        ClientCommandHandler.instance.registerCommand((Help()))
        listOf(
            this, Dungeon, GuiRenderer, Location, RunInformation, BoxWitherDoor
        ).forEach(MinecraftForge.EVENT_BUS::register)
        RenderUtils
    }

    @SubscribeEvent
    fun onTick(event: TickEvent.ClientTickEvent) {
        if (event.phase != TickEvent.Phase.START) return
        if (display != null) {
            mc.displayGuiScreen(display)
            display = null
        }

        Dungeon.onTick()
        GuiRenderer.onTick()
        Location.onTick()
    }

    fun openConfig() {
        display = Config.gui()
    }

}
