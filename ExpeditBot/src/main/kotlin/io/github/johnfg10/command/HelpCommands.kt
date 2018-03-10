package io.github.johnfg10.command

import io.github.johnfg10.Expedit
import io.github.johnfg10.deleteMessageReq
import io.github.johnfg10.roles.Role
import io.github.johnfg10.sendMessageReq
import sx.blah.discord.handle.obj.IMessage
import sx.blah.discord.handle.obj.IUser
import sx.blah.discord.util.EmbedBuilder

@CommandController
class HelpCommands{

    @Command("help", ["help"])
    fun Help(@CommandArg(CommandArgumentType.Author) author: IUser, @CommandArg(CommandArgumentType.Message) msg: IMessage) {
        if (Expedit.kdiscord == null){
            author.orCreatePMChannel.sendMessage("unfortunately i am unable to access the command index at this moment please try later, and if this reoccurs infor the developer")
            return
        }
        val embededBuilder = EmbedBuilder()

        Expedit.kdiscord!!.getCommandMapp().forEach { t, u ->
            embededBuilder.appendField(t.aliases.joinToString(", "),
                    "Description: ${if (!t.description.isBlank()) t.description else "No description provided"} \n" +
                            "Roles: ${Expedit.kdiscord!!.getRoleMapp()[u]?.toRoleString()?.joinToString(", ") ?: "No roles required"} \n" +
                            "Example: ${if (t.commandExample.isNotBlank()){t.commandExample}else{"No command example provided"} } \n"
                    , false)
        }

        author.orCreatePMChannel.sendMessageReq(embededBuilder.build())
        msg.deleteMessageReq()
    }

    fun List<Role>.toRoleString(): MutableList<String> {
        val roles = mutableListOf<String>()

        this.forEach {
            roles.add(it.role)
        }

        return roles
    }
}