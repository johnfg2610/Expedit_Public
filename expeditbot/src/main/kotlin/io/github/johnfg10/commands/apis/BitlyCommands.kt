package io.github.johnfg10.commands.apis

import com.github.kvnxiao.discord.meirei.annotations.Command
import com.github.kvnxiao.discord.meirei.command.CommandContext
import io.github.johnfg10.BotHelper
import io.github.johnfg10.Expedit
import io.github.johnfg10.TokenConfig
import io.github.johnfg10.apis.bitly.BitlyApi
import org.apache.commons.validator.routines.UrlValidator
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent
import java.lang.reflect.InvocationTargetException

class BitlyCommands{
    @Command(id = "shorten", aliases = ["shorten"], prefix = Expedit.prefix)
    fun shortenLink(context: CommandContext, event: MessageReceivedEvent){
        if (context.args == null){
            BotHelper.SendMessage(event.channel, "This command requires additional arguments, specifically the long URL")
        }

        val urlValidator = UrlValidator()
        if (!urlValidator.isValid(context.args!!)){
            BotHelper.SendMessage(event.channel, "The given URL was not valid")
            return
        }

        try {
            val shortenResult = BitlyApi().shortenUrl(context.args!!)
            if (shortenResult != null){
                if (shortenResult.statusCode == 200){
                    BotHelper.SendMessage(event.channel, "Shortened URL: ${shortenResult.data.url}")
                } else {
                    // a error occured
                    BotHelper.SendMessage(event.channel, "A error occurred fetching this result,  error code: ${shortenResult.statusCode}. Error text: ${shortenResult.statusText}")
                }
            }else{
                BotHelper.SendMessage(event.channel, "The result was null")
            }
        }catch (e: InvocationTargetException){
            BotHelper.SendMessage(event.channel, "A error has occurred, commonly a invalide URI")
        }
    }
}