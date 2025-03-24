package silence.simsool.meowmap.commands

import net.minecraft.command.CommandBase
import net.minecraft.command.ICommandSender
import silence.simsool.meowmap.MeowMap

class Help : CommandBase() {

    override fun getCommandName(): String = "meow"

    override fun getCommandAliases(): List<String> = listOf("mm", "map")

    override fun getCommandUsage(sender: ICommandSender): String = "/$commandName"

    override fun getRequiredPermissionLevel(): Int = 0

    override fun processCommand(sender: ICommandSender, args: Array<String>) {
        if (args.isEmpty()) {
            MeowMap.openConfig()
            return
        }
    }
}
