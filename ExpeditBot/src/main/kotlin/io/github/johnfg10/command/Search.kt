package io.github.johnfg10.command

import io.github.johnfg10.api.Search
import io.github.johnfg10.api.SearchImage
import io.github.johnfg10.api.SearchYoutube
import io.github.johnfg10.command.flags.CommandArgument
import io.github.johnfg10.command.flags.containsFlag
import io.github.johnfg10.sendMessageReq
import org.apache.commons.cli.Options
import sx.blah.discord.handle.impl.obj.Embed
import sx.blah.discord.handle.obj.*
import sx.blah.discord.util.EmbedBuilder
import java.awt.Color
import java.util.*

@CommandController
class Search {

    @Command(aliases = ["search"])
    fun search(
            @CommandArg(CommandArgumentType.Channel) channel: IChannel,
            @CommandArg(CommandArgumentType.Guild) guild: IGuild,
            @CommandArg(CommandArgumentType.Author) author: IUser,
            @CommandArg(CommandArgumentType.Arguments) args: List<CommandArgument>,
            @CommandArg(CommandArgumentType.Mentions) mentions: List<IUser>,
            @CommandArg(CommandArgumentType.Arguments) msgArgs: List<CommandArgument>,
            @CommandArg(CommandArgumentType.Message) msg: IMessage,
            @CommandArg(CommandArgumentType.RemainingMessage) remainingMessage: String
    ){
        if (remainingMessage.isBlank()){
            channel.sendMessage("Must include the search parse")
            return
        }

        val search = Search(remainingMessage)
        val results = search.getResults(5)

        val embedBuilder = EmbedBuilder()
        embedBuilder.withColor(Color.CYAN)

        results.forEach {
            embedBuilder.appendField(it.title, "Description: ${it.description} \nURL: ${it.url}", false)
        }
        channel.sendMessageReq(embedBuilder.build())
    }

    @Command(aliases = ["searchimage"])
    fun searchImage(
            @CommandArg(CommandArgumentType.Channel) channel: IChannel,
            @CommandArg(CommandArgumentType.Guild) guild: IGuild,
            @CommandArg(CommandArgumentType.Author) author: IUser,
            @CommandArg(CommandArgumentType.Arguments) args: List<CommandArgument>,
            @CommandArg(CommandArgumentType.Mentions) mentions: List<IUser>,
            @CommandArg(CommandArgumentType.Arguments) msgArgs: List<CommandArgument>,
            @CommandArg(CommandArgumentType.Message) msg: IMessage,
            @CommandArg(CommandArgumentType.RemainingMessage) remainingMessage: String
    ){
        if (remainingMessage.isBlank()){
            channel.sendMessage("Must include the search parse")
            return
        }

        val imgSearch = SearchImage(remainingMessage)
        val results = imgSearch.getResults(5)
        results.forEach {
            val embedBuilder = EmbedBuilder()
            embedBuilder.withColor(Random().nextInt(255), Random().nextInt(255), Random().nextInt(255))
            embedBuilder.appendField(it.description, "Citation: ${it.cite} ", false)
            embedBuilder.withImage(it.imageUrl)
            embedBuilder.withAuthorName(it.cite)
            channel.sendMessageReq(embedBuilder.build())
        }
    }

    @Command(aliases = ["searchyoutube"])
    fun searchYoutube(
            @CommandArg(CommandArgumentType.Channel) channel: IChannel,
            @CommandArg(CommandArgumentType.Guild) guild: IGuild,
            @CommandArg(CommandArgumentType.Author) author: IUser,
            @CommandArg(CommandArgumentType.Arguments) args: List<CommandArgument>,
            @CommandArg(CommandArgumentType.Mentions) mentions: List<IUser>,
            @CommandArg(CommandArgumentType.Arguments) msgArgs: List<CommandArgument>,
            @CommandArg(CommandArgumentType.Message) msg: IMessage,
            @CommandArg(CommandArgumentType.RemainingMessage) remainingMessage: String
    ){
        if (remainingMessage.isBlank()){
            channel.sendMessage("Must include the search parse")
            return
        }

        val ytSearch = SearchYoutube(remainingMessage)
        val results = ytSearch.getResults(5)
        results.forEach {
            val embedBuilder = EmbedBuilder()
            embedBuilder.withColor(Random().nextInt(255), Random().nextInt(255), Random().nextInt(255))
            embedBuilder.appendField(it.title, "${it.description}\n${it.url}", false)
            embedBuilder.withImage(it.thumbnailUrl)
            channel.sendMessageReq(embedBuilder.build())
        }
    }
}