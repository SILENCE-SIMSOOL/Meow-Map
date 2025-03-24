package silence.simsool.meowmap.utils

import net.minecraft.scoreboard.ScorePlayerTeam
import silence.simsool.meowmap.MeowMap.mc
import silence.simsool.meowmap.utils.Utils.removeFormatting

object ScoreboardUtils {
    fun cleanLine(scoreboard: String): String = scoreboard.removeFormatting().filter { it.code in 32..126 }

    fun getLines(): List<String> {
        return mc.theWorld?.scoreboard?.run {
            getSortedScores(getObjectiveInDisplaySlot(1) ?: return emptyList())
                .filter { it?.playerName?.startsWith("#") == false }
                .let { if (it.size > 15) it.drop(15) else it }
                .map { ScorePlayerTeam.formatPlayerName(getPlayersTeam(it.playerName), it.playerName) }
        } ?: emptyList()
    }
}
