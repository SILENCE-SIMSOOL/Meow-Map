package silence.simsool.meowmap

import kotlinx.coroutines.CoroutineScope
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiScreen
import net.minecraftforge.client.ClientCommandHandler
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import silence.simsool.meowmap.commands.Help
import silence.simsool.meowmap.config.Config
import silence.simsool.meowmap.features.BoxWitherDoor
import silence.simsool.meowmap.features.Dungeon
import silence.simsool.meowmap.features.RunInformation
import silence.simsool.meowmap.ui.GuiRenderer
import silence.simsool.meowmap.utils.Location
import kotlin.coroutines.EmptyCoroutineContext
import silenceaddon.events.SilenceTickEvent
import net.minecraft.network.play.server.S02PacketChat
import net.minecraft.network.play.server.S34PacketMaps
import net.minecraft.network.play.server.S38PacketPlayerListItem
import net.minecraft.network.play.server.S3EPacketTeams
import silenceaddon.events.PacketEvent
import silence.simsool.meowmap.events.ChatEvent
import silence.simsool.meowmap.events.ScoreboardEvent
import silence.simsool.meowmap.events.TabListEvent
import silence.simsool.meowmap.features.MimicDetector
import silence.simsool.meowmap.utils.MapUtils

@Mod(
    modid = MeowMap.MODID,
    name = MeowMap.NAME,
    version = MeowMap.VERSION,
    modLanguageAdapter = "silence.simsool.meowmap.kotlin.KotlinAdapter",
    dependencies = "required-after:silenceutils"
)
object MeowMap {
    const val MODID = "meowmap"
    const val NAME = "Meow Map"
    const val VERSION = "1.0.2"
    val CHAT_PREFIX: String
        get() = "§e[§fMeow§e]"

    val mc: Minecraft = Minecraft.getMinecraft()
    var display: GuiScreen? = null
    val scope = CoroutineScope(EmptyCoroutineContext)

    @Mod.EventHandler
    fun onInit(event: FMLInitializationEvent) {
        ClientCommandHandler.instance.registerCommand((Help()))
        listOf(
            this, Dungeon, GuiRenderer, Location, RunInformation, BoxWitherDoor, MimicDetector
        ).forEach(MinecraftForge.EVENT_BUS::register)
    }

    @SubscribeEvent
    fun onTick(event: SilenceTickEvent) {
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

    @SubscribeEvent
    fun onPacketReceive(event: PacketEvent.ReceiveEvent) {
        when (val packet = event.packet) {
            is S02PacketChat -> {
                if (packet.type.toInt() != 2) MinecraftForge.EVENT_BUS.post(ChatEvent(packet))
            }
            is S3EPacketTeams -> {
                MinecraftForge.EVENT_BUS.post(ScoreboardEvent(packet))
            }
            is S38PacketPlayerListItem -> {
                MinecraftForge.EVENT_BUS.post(TabListEvent(packet))
            }
            is S34PacketMaps -> {
                MapUtils.updateMapData(packet)
            }
        }
    }
}