package io.github.johnfg10.commands

import com.github.kvnxiao.discord.meirei.annotations.Command
import com.github.kvnxiao.discord.meirei.annotations.CommandGroup
import com.github.kvnxiao.discord.meirei.command.CommandContext
import io.github.johnfg10.BotHelper
import io.github.johnfg10.Expedit
import io.github.johnfg10.TypeHelper
import kotlinx.coroutines.experimental.*
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent
import sx.blah.discord.handle.obj.IUser
import sx.blah.discord.util.RequestBuffer
import sx.blah.discord.util.RequestBuilder
import java.time.LocalDateTime
import java.util.concurrent.CancellationException
import java.util.concurrent.TimeUnit

@CommandGroup("admin.cleaning")
class CleaningCommands {

    private val SECONDS_PER_DAY = 86400

    private val nukeMap = mutableMapOf<Long, Job>()

    @Command(prefix = Expedit.Companion.prefix,aliases = ["nuke"], description = "", id = "nuke")
    fun nuke(context: CommandContext, event: MessageReceivedEvent) {
        if (nukeMap.containsKey(event.messageID)) {
            BotHelper.SendMessage(event.channel, "A Nuke task is already active in this channel")
            return
        }

        val job = launch {
            try {
                val initLocalDateTime = LocalDateTime.now()
                fun clearLastTwoWeeksMsges() {
                    val messageHistory = event.channel.getMessageHistoryTo(initLocalDateTime.minusWeeks(2), 99)
                    if (messageHistory.isEmpty())
                        return

                    if (!isActive)
                        throw CancellationException("A cancelation has been requested, as such the task could not finnish and will now exit!")

                    RequestBuffer.request {
                        messageHistory.bulkDelete()
                    }
                }
                clearLastTwoWeeksMsges()

                var msgHistory = event.channel.getMessageHistory(50)

                while (isActive && msgHistory.isNotEmpty()){
                    delay(10, TimeUnit.SECONDS)
                    for (msg in msgHistory){
                        delay(300)

                        if (!msg.isPinned && !msg.isDeleted){
                            RequestBuffer.request {
                                msg.delete()
                            }
                        }
                    }
                    msgHistory = event.channel.getMessageHistory(50)
                    if (RequestBuffer.getIncompleteRequestCount() > 50)
                        delay(30, TimeUnit.SECONDS)
                }

                event.channel.sendMessage("nuke complete")
            }catch (e: CancellationException){
                println("canceled nuke")
            }finally {
                println("nuke finnished")
            }
        }


        nukeMap[event.channel.longID] = job
    }

    @Command(prefix = Expedit.Companion.prefix, aliases = ["cancelnuke"], description = "", id = "cancelnuke")
    fun cancelNuke(context: CommandContext, event: MessageReceivedEvent){
        val nukeChannel= nukeMap[event.channel.longID]
        if (nukeChannel != null) {
            if (nukeChannel.isCompleted) {
                BotHelper.SendMessage(event.channel, "It would appear this job has already completed!")
                Expedit.logger.debug("job done")
            }

            if (nukeChannel.isCancelled) {
                BotHelper.SendMessage(event.channel, "It would appear this nuke has already been canceled!")
                Expedit.logger.debug("already canceled")
            }

            Expedit.logger.debug("Canceling nuke")
            BotHelper.SendMessage(event.channel, "Canceling nuke and waiting for it to join with main thread....")

            runBlocking {
                RequestBuffer.killAllRequests()
                nukeChannel.cancelAndJoin()
            }

            BotHelper.SendMessage(event.channel, "Nuke has been successfully cancelled!")
        }else{
            BotHelper.SendMessage(event.channel, "It would appear this channel does not currently have a nuke in progress, as such it can not be cancelled!")
            return
        }
    }

    @Command(prefix = Expedit.Companion.prefix, aliases = ["purge"], description = "", id = "purge")
    fun purge(context: CommandContext, event: MessageReceivedEvent){
        if (context.args == null){
            event.channel.sendMessage("This command requires arguments!")
            return
        }

        val args = context.args!!.split(" ")

        if (!TypeHelper.IsInt(args[0])){
            event.channel.sendMessage("The first arugment must be numerical [0-9]!")
            return
        }

        val eventMentions = event.message.mentions
        BotHelper.DeleteMessage(event.message)

        val deleteAmount = args[0].toInt()

        val msgList = event.channel.getMessageHistory(deleteAmount)



        if (eventMentions.isNotEmpty()){
            //only delete messages of certain users
            BotHelper.DeleteMessages(msgList, eventMentions.toTypedArray())
        }else{
            BotHelper.DeleteMessages(msgList)
        }
    }

}