package io.github.johnfg10.command

import io.github.johnfg10.command.flags.CommandArgument
import io.github.johnfg10.deleteMessageReq
import io.github.johnfg10.random
import io.github.johnfg10.roles.Role
import io.github.johnfg10.sendMessageReq
import sx.blah.discord.handle.obj.IChannel
import sx.blah.discord.handle.obj.IGuild
import sx.blah.discord.handle.obj.IMessage
import sx.blah.discord.handle.obj.IUser
import kotlin.system.measureNanoTime
import kotlin.system.measureTimeMillis

@CommandController
class UtitilyCommands {
    @Command("ping",  ["ping"], "")
    fun PingCommand(@CommandArg(CommandArgumentType.Mentions) mentions: List<IUser>,
                    @CommandArg(CommandArgumentType.Guild) guild: IGuild,
                    @CommandArg(CommandArgumentType.Author) author: IUser,
                    @CommandArg(CommandArgumentType.Channel) channel: IChannel,
                    @CommandArg(CommandArgumentType.Message) message: IMessage
    ){
        var msg: IMessage? = null
        val nanoTime = measureTimeMillis {
            msg = channel.sendMessage("Pong!")
        }
        msg?.edit("Pong : $nanoTime ms!")
    }

    @Command("clear", ["clear", "purge", "clean"], "-clear --amount=10 @examplebot", "Clears the amount requested out of the channel(using --amount=1 argument) with optional tagging of users to filter removals")
    @Role("Mod")
    fun ClearChat(@CommandArg(CommandArgumentType.Channel) channel: IChannel,
                  @CommandArg(CommandArgumentType.Arguments) msgArgs: List<CommandArgument>,
                  @CommandArg(CommandArgumentType.Mentions) mentions: List<IUser>
    ){
        if(!msgArgs.any { it.flag.flag == "amount" }){
            channel.sendMessageReq("This command requires the amount argument. example: --amount=10")
            return
        }

        var argument = msgArgs.first{ it.flag.flag == "amount" }.value.toIntOrNull()

        if (argument == null){
            channel.sendMessageReq("The amount argument must be of type int. example: --amount=102")
            return
        }

        if (argument > 1000){
            channel.sendMessageReq("There is a maximum of 1000 messages at a time(until discord decide to sort out the long term deletion method)")
            return
        }

        argument += 1

        val messageHistory = channel.getMessageHistory(argument)

        messageHistory.forEach {
            if (mentions.isEmpty()){
                if (!it.isDeleted && !it.isPinned){
                    it.deleteMessageReq()
                }
                it.deleteMessageReq()
            }else{
                if (mentions.contains(it.author) && (!it.isDeleted && !it.isPinned)){
                    it.delete()
                }else{

                }
            }
        }



    }
}