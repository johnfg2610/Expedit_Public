package io.github.johnfg10.command

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import io.github.johnfg10.command.flags.CommandArgument
import io.github.johnfg10.deleteMessageReq
import io.github.johnfg10.random
import io.github.johnfg10.sendMessageReq
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import org.apache.commons.logging.LogFactory
import sx.blah.discord.handle.obj.IChannel
import sx.blah.discord.handle.obj.IGuild
import sx.blah.discord.handle.obj.IMessage
import sx.blah.discord.handle.obj.IUser
import java.security.SecureRandom
import java.util.*
import kotlin.concurrent.timer

@CommandController
class FunCommands() {
    @Command("insults",  ["insult"], "")
    fun Insult(@CommandArg(CommandArgumentType.Mentions) mentions: List<IUser>,
              @CommandArg(CommandArgumentType.Guild) guild: IGuild,
              @CommandArg(CommandArgumentType.Author) author: IUser,
              @CommandArg(CommandArgumentType.Message) message: IMessage,
              @CommandArg(CommandArgumentType.Channel) channel: IChannel
    ) {
        val insults = listOf<String>(
                "If laughter is the best medicine, your face must be curing the world.",
                "You're so ugly, you scared the crap out of the toilet.",
                "Your family tree must be a cactus because everybody on it is a prick.",
                "No I'm not insulting you, I'm describing you.",
                "It's better to let someone think you are an Idiot than to open your mouth and prove it.",
                "If I had a face like yours, I'd sue my parents.",
                "Your birth certificate is an apology letter from the condom factory.",
                "The only way you'll ever get laid is if you crawl up a chicken's ass and wait.",
                "You're so fake, Barbie is jealous.",
                "I’m jealous of people that don’t know you!",
                "You're so ugly, when your mom dropped you off at school she got a fine for littering.",
                "If I wanted to kill myself I'd climb your ego and jump to your IQ.",
                "You must have been born on a highway because that's where most accidents happen.",
                "Brains aren't everything. In your case they're nothing.",
                "I don't know what makes you so stupid, but it really works.",
                "I can explain it to you, but I can’t understand it for you.",
                "Roses are red violets are blue, God made me pretty, what happened to you?",
                "Calling you an idiot would be an insult to all the stupid people.",
                "You, sir, are an oxygen thief!",
                "Some babies were dropped on their heads but you were clearly thrown at a wall.",
                "Don't like my sarcasm, well I don't like your stupid.",
                "Please shut your mouth when you’re talking to me.",
                "I'd slap you, but that would be animal abuse.",
                "They say opposites attract. I hope you meet someone who is good-looking, intelligent, and cultured.",
                "Stop trying to be a smart ass, you're just an ass.",
                "The last time I saw something like you, I flushed it.",
                "I'm busy now. Can I ignore you some other time?",
                "You have Diarrhea of the mouth; constipation of the ideas.",
                "If ugly were a crime, you'd get a life sentence.",
                "I can lose weight, but you’ll always be ugly.",
                "Why don't you slip into something more comfortable... like a coma.",
                "Shock me, say something intelligent.",
                "Keep rolling your eyes, perhaps you'll find a brain back there.",
                "You are not as bad as people say, you are much, much worse.",
                "I don't know what your problem is, but I'll bet it's hard to pronounce.",
                "You get ten times more girls than me? ten times zero is zero...",
                "There is no vaccine against stupidity.",
                "You're the reason the gene pool needs a lifeguard.",
                "Sure, I've seen people like you before - but I had to pay an admission.",
                "How old are you? - Wait I shouldn't ask, you can't count that high.",
                "Have you been shopping lately? They're selling lives, you should go get one.",
                "You're like Monday mornings, nobody likes you.",
                "Of course I talk like an idiot, how else would you understand me?",
                "All day I thought of you... I was at the zoo.",
                "To make you laugh on Saturday, I need to tell you a joke on Wednesday.",
                "I'd like to see things from your point of view but I can't seem to get my head that far up my ass.",
                "Don't you need a license to be that ugly?",
                "My friend thinks he is smart. He told me an onion is the only food that makes you cry, so I threw a coconut at his face.",
                "Your house is so dirty you have to wipe your feet before you go outside.",
                "If you really spoke your mind, you'd be speechless.",
                "Stupidity is not a crime so you are free to go.",
                "If I told you that I have a piece of dirt in my eye, would you move?",
                "You so dumb, you think Cheerios are doughnut seeds.",
                "So, a thought crossed your mind? Must have been a long and lonely journey.",
                "Every time I'm next to you, I get a fierce desire to be alone.",
                "You're so dumb that you got hit by a parked car.",
                "Keep talking, someday you'll say something intelligent!",
                "How did you get here? Did someone leave your cage open?",
                "Don't you have a terribly empty feeling - in your skull?",
                "As an outsider, what do you think of the human race?",
                "We can always tell when you are lying. Your lips move."
                )

        channel.sendMessageReq("Hey ${mentions.firstOrNull()?: "You"}, " + insults.random())
        message.deleteMessageReq()
    }

    @Command("numtrivia",  ["numtrivia"], "")
    fun NumTrivia(@CommandArg(CommandArgumentType.Mentions) mentions: List<IUser>,
               @CommandArg(CommandArgumentType.Guild) guild: IGuild,
               @CommandArg(CommandArgumentType.Author) author: IUser,
               @CommandArg(CommandArgumentType.Message) message: IMessage,
               @CommandArg(CommandArgumentType.Channel) channel: IChannel
    ) {
        "http://numbersapi.com/random/trivia".httpGet().responseString {
            request, response, result ->
            when(result){
                is Result.Success -> {
                    channel.sendMessage(result.value)
                }
                is Result.Failure -> {
                    logger.error("response: ${result.error.response} error: ${result.error.message}")
                }
            }

        }
    }

    @Command("joke",  ["joke"], "")
    fun Joke(@CommandArg(CommandArgumentType.Mentions) mentions: List<IUser>,
                  @CommandArg(CommandArgumentType.Guild) guild: IGuild,
                  @CommandArg(CommandArgumentType.Author) author: IUser,
                  @CommandArg(CommandArgumentType.Message) message: IMessage,
                  @CommandArg(CommandArgumentType.Channel) channel: IChannel
    ) {
        "http://numbersapi.com/random/trivia".httpGet().responseString {
            request, response, result ->
            when(result){
                is Result.Success -> {
                    channel.sendMessage(result.value)
                }
                is Result.Failure -> {
                    logger.error("response: ${result.error.response} error: ${result.error.message}")
                }
            }

        }
    }

    companion object {
        val logger = LogFactory.getLog(FunCommands::class.java)
    }

/*    val randTimerMap = mutableMapOf<Long, Job>()

    @Command("randomvoice",  ["rndvoice","randomvoice"], "")
    @Permission("funcommands.randomvoice")
    fun RandomVoice(@CommandArg(CommandArgumentType.Mentions) mentions: List<IUser>,
                    @CommandArg(CommandArgumentType.Guild) guild: IGuild,
                    @CommandArg(CommandArgumentType.Author) author: IUser,
                    @CommandArg(CommandArgumentType.Message) message: IMessage,
                    @CommandArg(CommandArgumentType.Channel) channel: IChannel,
                    voiceChannelIgnore: IChannel? = null
                    ){


        if (author.longID == 232904677912608768 && mentions.size > 1){
            return
        }

        for (mention in mentions){
            if(mentions.contains(guild.getUserByID(200989665304641536)) || mention == guild.owner){
                channel.sendMessageReq("I don't think so do you?")
                val channel1 = author.getVoiceStateForGuild(guild).channel
                if (channel1 != null){
                    if (Random().nextBoolean()){
                        channel.sendMessageReq("Better luck next time ;)")
                        val mentionList = mentions as MutableList
                        mentionList.remove(mention)
                        mentionList.add(author)
                        RandomVoice(mentionList, guild, author, message, channel)
                    }else{
                        channel.sendMessageReq("You got lucky this time ;)")
                    }
                }
                return
            }

            val voiceChannel = mention.getVoiceStateForGuild(guild).channel
            if (voiceChannel != null){
                val voiceChannels = guild.voiceChannels
                if (voiceChannelIgnore != null)
                    voiceChannels.remove(voiceChannelIgnore)
                voiceChannels.remove(voiceChannel)
                val voiceChannel1 = voiceChannels[(0 .. voiceChannels.size).random()]
                mention.moveToVoiceChannel(voiceChannel1)
            }
        }

        message.deleteMessageReq()
    }

    @Command("randomvoicetimer", ["rndtimer"], "")
    @Permission("funcommands.randomtimer")
    fun RandomVoiceTimer(@CommandArg(CommandArgumentType.Mentions) mentions: List<IUser>,
                         @CommandArg(CommandArgumentType.Guild) guild: IGuild,
                         @CommandArg(CommandArgumentType.Author) author: IUser,
                         @CommandArg(CommandArgumentType.Message) message: IMessage,
                         @CommandArg(CommandArgumentType.Arguments) msgArgs: List<CommandArgument>,
                         @CommandArg(CommandArgumentType.Channel) channel: IChannel
    ){

        val mentionss = mutableListOf(*mentions.toTypedArray())
        if (author.longID == 232904677912608768){
            RandomVoice(mutableListOf(author), guild, author, message, channel)
            return
        }

        message.content.split(" ").forEach {
            if (it.toLongOrNull() != null){
                val id = it.toLong()
                println(id)
                val user = guild.getUserByID(id)
                if (user != null){
                    mentionss.add(user)
                }
            }
        }

        for (mention in mentionss){
            if(mentions.contains(guild.getUserByID(200989665304641536)) || mention == guild.owner || author.longID == 232904677912608768){
                channel.sendMessageReq("I don't think so do you?")
                val channel1 = author.getVoiceStateForGuild(guild).channel
                if (channel1 != null){
                    if (Random().nextBoolean() || (author.longID == 232904677912608768)){
                        channel.sendMessageReq("Better luck next time ;)")
                        mentionss.remove(mention)
                        mentionss.add(author)
                        RandomVoice(mentionss, guild, author, message, channel)
                    }else{
                        channel.sendMessageReq("You got lucky this time ;)")
                    }
                }
                return
            }

            if (msgArgs.find { it.flag.flag == "amount" } == null){
                author.orCreatePMChannel.sendMessageReq("hey this command requires the --amount=10(or what ever number) argument")
                return
            }

            val adelay = msgArgs.find { it.flag.flag == "delay" }?.value?.toInt() ?: 10000

            val delaysec = adelay * 1000

            val job = launch {
                repeat(msgArgs.find { it.flag.flag == "amount" }!!.value.toIntOrNull() ?: 0) {
                    if (!this.isActive)
                        return@repeat

                    val channel1 = author.getVoiceStateForGuild(guild).channel

                    RandomVoice(mentionss, guild, author, message, channel, channel1)
                    delay(delaysec)
                }
            }
            val id = Random().nextLong()

            randTimerMap[id] = job
            author.orCreatePMChannel.sendMessageReq("Hey, the id for that is: $id")
        }

        message.deleteMessageReq()
    }

    @Command("cancelrandomvoicetimer", ["cancelrndtimer"], "")
    @Permission("funcommands.cancelrandomtimer")
    fun CancelRandomVoiceTimer(@CommandArg(CommandArgumentType.Arguments) msgArgs: List<CommandArgument>) {
        if (msgArgs.find { it.flag.flag == "id" } == null){
            val id = msgArgs.find { it.flag.flag == "id" }?.value?.toLong() ?: -1L
            if (id == -1L){
                if (randTimerMap.contains(id)){
                    randTimerMap[id]?.cancel()
                }
            }
        }
    }

    @Command("debugPrint", ["debugPrint"], "")
    @Permission("funcommands.debugprintj")
    fun printDebugInfo(@CommandArg(CommandArgumentType.Channel) channel: IChannel){
        randTimerMap.forEach { t, u ->
            channel.sendMessageReq("id: $t, Job Info: $u")
        }
    }*/
}