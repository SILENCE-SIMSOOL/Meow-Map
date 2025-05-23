package silence.simsool.meowmap.features

import net.minecraft.client.gui.ScaledResolution
import net.minecraft.client.renderer.GlStateManager
import org.lwjgl.opengl.GL11
import silence.simsool.meowmap.MeowMap.mc
import silence.simsool.meowmap.config.Config
import silence.simsool.meowmap.core.DungeonPlayer
import silence.simsool.meowmap.core.types.*
import silence.simsool.meowmap.ui.ScoreElement
import silence.simsool.meowmap.utils.Location.inBoss
import silence.simsool.meowmap.utils.MapUtils
import silence.simsool.meowmap.utils.MapUtils.connectorSize
import silence.simsool.meowmap.utils.MapUtils.halfRoomSize
import silence.simsool.meowmap.utils.MapUtils.roomSize
import silence.simsool.meowmap.utils.RenderUtils
import silence.simsool.meowmap.utils.Utils.equalsOneOf
import java.awt.Color

object MapRender {
    var dynamicRotation = 0f
        set(value) {
            field = value
        }
    fun renderMap() {
        RenderUtils.renderRect(
            0.0, 0.0, 128.0, if (Config.mapShowRunInformation) 142.0 else 128.0, Config.mapBackground
        )

        RenderUtils.renderRectBorder(
            0.0,
            0.0,
            128.0,
            if (Config.mapShowRunInformation) 142.0 else 128.0,
            Config.mapBorderWidth.toDouble(),
            Config.mapBorder
        )

        if (Config.mapRotate) {
            GlStateManager.pushMatrix()
            setupRotate()
        } else if (Config.mapDynamicRotate) {
            GlStateManager.translate(64.0, 64.0, 0.0)
            GlStateManager.rotate(dynamicRotation, 0f, 0f, 1f)
            GlStateManager.translate(-64.0, -64.0, 0.0)
        }

        renderRooms()
        renderText()
        if (!inBoss) renderPlayerHeads()

        if (Config.mapRotate) {
            GL11.glDisable(GL11.GL_SCISSOR_TEST)
            GlStateManager.popMatrix()
        } else if (Config.mapDynamicRotate) {
            GlStateManager.translate(64.0, 64.0, 0.0)
            GlStateManager.rotate(-dynamicRotation, 0f, 0f, 1f)
            GlStateManager.translate(-64.0, -64.0, 0.0)
        }

        if (Config.mapShowRunInformation) renderRunInformation()
    }

    fun setupRotate() {
        val scale = ScaledResolution(mc).scaleFactor
        GL11.glEnable(GL11.GL_SCISSOR_TEST)
        GL11.glScissor(
            (Config.mapX * scale),
            (mc.displayHeight - Config.mapY * scale - 128 * scale * Config.mapScale).toInt(),
            (128 * scale * Config.mapScale).toInt(),
            (128 * scale * Config.mapScale).toInt()
        )
        GlStateManager.translate(64.0, 64.0, 0.0)
        GlStateManager.rotate(-mc.thePlayer.rotationYaw + 180f, 0f, 0f, 1f)

        if (Config.mapCenter) {
            GlStateManager.translate(
                -((mc.thePlayer.posX - DungeonScan.startX + 15) * MapUtils.coordMultiplier + MapUtils.startCorner.first - 2),
                -((mc.thePlayer.posZ - DungeonScan.startZ + 15) * MapUtils.coordMultiplier + MapUtils.startCorner.second - 2),
                0.0
            )
        } else {
            GlStateManager.translate(-64.0, -64.0, 0.0)
        }
    }

    private fun renderRooms() {
        GlStateManager.pushMatrix()
        GlStateManager.translate(MapUtils.startCorner.first.toFloat(), MapUtils.startCorner.second.toFloat(), 0f)

        for (y in 0..10) {
            for (x in 0..10) {
                val tile = Dungeon.Info.dungeonList[y * 11 + x]
                if (tile is Unknown) continue
                if (tile.state == RoomState.UNDISCOVERED) continue

                val xOffset = (x shr 1) * (roomSize + connectorSize)
                val yOffset = (y shr 1) * (roomSize + connectorSize)

                val xEven = x and 1 == 0
                val yEven = y and 1 == 0

                var color = tile.color

                when {
                    xEven && yEven -> if (tile is Room) {
                        RenderUtils.renderRect(
                            xOffset,
                            yOffset,
                            roomSize,
                            roomSize,
                            color
                        )

                        if (tile.state == RoomState.UNOPENED) {
                            RenderUtils.drawCheckmark(xOffset.toFloat(), yOffset.toFloat(), tile.state)
                        }
                    }

                    !xEven && !yEven -> {
                        RenderUtils.renderRect(
                            xOffset,
                            yOffset,
                            (roomSize + connectorSize),
                            (roomSize + connectorSize),
                            color
                        )
                    }

                    else -> drawRoomConnector(
                        xOffset, yOffset, connectorSize, tile is Door, !xEven, color
                    )
                }
            }
        }
        GlStateManager.popMatrix()
    }

    private fun renderText() {
        GlStateManager.pushMatrix()
        GlStateManager.translate(MapUtils.startCorner.first.toFloat(), MapUtils.startCorner.second.toFloat(), 0f)

        Dungeon.Info.uniqueRooms.forEach { unique ->
            val room = unique.mainRoom
            if (room.state.equalsOneOf(RoomState.UNDISCOVERED, RoomState.UNOPENED)) return@forEach
            val checkPos = unique.getCheckmarkPosition()
            val namePos = unique.getNamePosition()
            val xOffsetCheck = (checkPos.first / 2f) * (roomSize + connectorSize)
            val yOffsetCheck = (checkPos.second / 2f) * (roomSize + connectorSize)
            val xOffsetName = (namePos.first / 2f) * (roomSize + connectorSize)
            val yOffsetName = (namePos.second / 2f) * (roomSize + connectorSize)

            if (Config.mapCheckmark != 0 && Config.mapRoomSecrets != 2) RenderUtils.drawCheckmark(xOffsetCheck, yOffsetCheck, room.state)

            val color = (if (Config.mapColorText) when (room.state) {
                RoomState.GREEN -> Config.colorTextGreen
                RoomState.CLEARED -> Config.colorTextCleared
                RoomState.FAILED -> Config.colorTextFailed
                else -> Config.colorTextUncleared
            } else Config.colorTextCleared).rgb

            if (Config.mapRoomSecrets == 2) {
                GlStateManager.pushMatrix()
                GlStateManager.translate(xOffsetCheck + halfRoomSize, yOffsetCheck + 2 + halfRoomSize, 0f)
                GlStateManager.scale(2f, 2f, 1f)
                RenderUtils.renderCenteredText(listOf(room.data.secrets.toString()), 0, 0, color)
                GlStateManager.popMatrix()
            }

            val name = mutableListOf<String>()

            if (Config.mapRoomNames != 0 && room.data.type.equalsOneOf(RoomType.PUZZLE, RoomType.TRAP) || Config.mapRoomNames == 2 && room.data.type.equalsOneOf(RoomType.NORMAL, RoomType.RARE, RoomType.CHAMPION))
                name.addAll(room.data.name.split(" "))
            if (room.data.type == RoomType.NORMAL && Config.mapRoomSecrets == 1) name.add(room.data.secrets.toString())

            RenderUtils.renderCenteredText(name, xOffsetName.toInt() + halfRoomSize, yOffsetName.toInt() + halfRoomSize, color)
        }
        GlStateManager.popMatrix()
    }

    fun renderPlayerHeads() {
        try {
            if (Dungeon.dungeonTeammates.isEmpty()) {
                RenderUtils.drawPlayerHead(mc.thePlayer.name, DungeonPlayer(mc.thePlayer.locationSkin).apply {
                    yaw = mc.thePlayer.rotationYaw
                })
            } else {
                Dungeon.dungeonTeammates.forEach { (name, teammate) ->
                    if (!teammate.dead) RenderUtils.drawPlayerHead(name, teammate)
                }
            }
        } catch (_: ConcurrentModificationException) {
        }
    }

    private fun drawRoomConnector(
        x: Int,
        y: Int,
        doorWidth: Int,
        doorway: Boolean,
        vertical: Boolean,
        color: Color,
    ) {
        val doorwayOffset = if (roomSize == 16) 5 else 6
        val width = if (doorway) 6 else roomSize
        var x1 = if (vertical) x + roomSize else x
        var y1 = if (vertical) y else y + roomSize
        if (doorway) if (vertical) y1 += doorwayOffset else x1 += doorwayOffset
        RenderUtils.renderRect(x1, y1, if (vertical) doorWidth else width, if (vertical) width else doorWidth, color)
    }

    fun renderRunInformation() {
        GlStateManager.pushMatrix()
        GlStateManager.translate(64f, 128f, 0f)
        GlStateManager.scale(2.0 / 3.0, 2.0 / 3.0, 1.0)
        val lines = ScoreElement.runInformationLines()

        val lineOne = lines.takeWhile { it != "split" }.joinToString(separator = "    ")
        val lineTwo = lines.takeLastWhile { it != "split" }.joinToString(separator = "    ")

        mc.fontRendererObj.drawString(lineOne, -mc.fontRendererObj.getStringWidth(lineOne) / 2f, 0f, 0xffffff, true)
        mc.fontRendererObj.drawString(lineTwo, -mc.fontRendererObj.getStringWidth(lineTwo) / 2f, 9f, 0xffffff, true)

        GlStateManager.popMatrix()
    }
}
