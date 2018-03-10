package io.github.johnfg10.command

import io.github.johnfg10.Expedit
import io.github.johnfg10.command.flags.CommandArgument
import io.github.johnfg10.deleteMessageReq
import io.github.johnfg10.roles.Role
import io.github.johnfg10.sendMessageReq
import io.github.johnfg10.user.User
import sx.blah.discord.handle.obj.IChannel
import sx.blah.discord.handle.obj.IGuild
import sx.blah.discord.handle.obj.IMessage
import sx.blah.discord.handle.obj.IUser
import sx.blah.discord.util.EmbedBuilder

@CommandController
class AdminCommands{
    @Command("addRole", ["addrole"])
    @Role("Admin")
    fun AddRole(
            @CommandArg(CommandArgumentType.Channel) channel: IChannel,
            @CommandArg(CommandArgumentType.Guild) guild: IGuild,
            @CommandArg(CommandArgumentType.Author) author: IUser,
            @CommandArg(CommandArgumentType.Mentions) mentions: List<IUser>,
            @CommandArg(CommandArgumentType.Arguments) msgArgs: List<CommandArgument>,
            @CommandArg(CommandArgumentType.Message) msg: IMessage
    ){
        val msgArg = msgArgs.firstOrNull { it.flag.flag == "role" }
        if (msgArg == null){
            channel.sendMessageReq("This command requires one argument of type long")
            return
        }

        val roleId = msgArg.value.toLongOrNull()
        if (roleId == null){
            channel.sendMessageReq("role must not be null")
            return
        }

        val role = guild.getRoleByID(roleId)
        if (role == null){
            channel.sendMessageReq("role could not be found")
            return
        }

        mentions.forEach {
            it.addRole(role)
        }

        msg.deleteMessageReq()
    }


    @Command("listRoles", ["listroles"])
    @Role("Admin")
    fun listRolesIds(
            @CommandArg(CommandArgumentType.Channel) channel: IChannel,
            @CommandArg(CommandArgumentType.Guild) guild: IGuild,
            @CommandArg(CommandArgumentType.Author) author: IUser,
            @CommandArg(CommandArgumentType.Arguments) args: List<CommandArgument>,
            @CommandArg(CommandArgumentType.Mentions) mentions: List<IUser>,
            @CommandArg(CommandArgumentType.Arguments) msgArgs: List<CommandArgument>,
            @CommandArg(CommandArgumentType.Message) msg: IMessage
    ){
        val builder = EmbedBuilder()
        guild.roles.forEach {
            builder.appendField(it.name, it.longID.toString(), false)
        }
        channel.sendMessageReq(builder.build())

        msg.deleteMessageReq()
    }


    @Command("addNick", ["addnick"])
    @Role("Mod")
    fun setNickname(
            @CommandArg(CommandArgumentType.Channel) channel: IChannel,
            @CommandArg(CommandArgumentType.Guild) guild: IGuild,
            @CommandArg(CommandArgumentType.Author) author: IUser,
            @CommandArg(CommandArgumentType.Arguments) args: List<CommandArgument>,
            @CommandArg(CommandArgumentType.Mentions) mentions: List<IUser>,
            @CommandArg(CommandArgumentType.Arguments) msgArgs: List<CommandArgument>,
            @CommandArg(CommandArgumentType.Message) msg: IMessage
    ){
        val nick = msgArgs.firstOrNull{it.flag.flag == "nickname" || it.flag.flag == "nick"}

        if (nick == null){
            channel.sendMessageReq("This command requires a nickname parameter")
            return
        }

        mentions.forEach {
            guild.setUserNickname(it, nick.value)
        }

        msg.deleteMessageReq()
    }

}