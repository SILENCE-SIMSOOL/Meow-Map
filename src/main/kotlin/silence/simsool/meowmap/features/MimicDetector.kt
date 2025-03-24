package silence.simsool.meowmap.features

import gg.essential.universal.UChat
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.Entity
import net.minecraft.entity.monster.EntityZombie
import net.minecraft.init.Blocks
import net.minecraft.util.BlockPos
import silence.simsool.meowmap.MeowMap.mc
import silence.simsool.meowmap.config.Config

object MimicDetector {

    var mimicOpenTime = 0L
    var mimicPos: BlockPos? = null

    fun onBlockChange(pos: BlockPos, old: IBlockState, new: IBlockState) {
        if (old.block == Blocks.trapped_chest && new.block == Blocks.air) {
            mimicOpenTime = System.currentTimeMillis()
            mimicPos = pos
        }
    }

    fun checkMimicDead() {
        if (RunInformation.mimicKilled) return
        if (mimicOpenTime == 0L) return
        if (System.currentTimeMillis() - mimicOpenTime < 750) return
        if (mc.thePlayer.getDistanceSq(mimicPos) < 400) {
            if (mc.theWorld.loadedEntityList.none {
                    it is EntityZombie && it.isChild && it.getCurrentArmor(3)?.getSubCompound("SkullOwner", false)
                        ?.getString("Id") == "bcb486a4-0cb5-35db-93f0-039fbdde03f0"
                }) {
                setMimicKilled()
            }
        }
    }

    fun setMimicKilled() {
        RunInformation.mimicKilled = true
        if (Config.mimicMessageEnabled) UChat.say("/pc ${Config.mimicMessage}")
    }

    fun isMimic(entity: Entity): Boolean {
        if (entity is EntityZombie && entity.isChild) {
            for (i in 0..3) {
                if (entity.getCurrentArmor(i) != null) return false
            }
            return true
        }
        return false
    }
}
