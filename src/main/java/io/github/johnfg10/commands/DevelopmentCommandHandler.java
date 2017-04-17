package io.github.johnfg10.commands;

import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import io.github.johnfg10.ExpeditConst;
import io.github.johnfg10.utils.RequestBufferHelper;
import io.github.johnfg10.utils.StringHelper;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.obj.*;
import sx.blah.discord.util.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;

/**
 * Created by johnfg10 on 17/04/2017.
 */
public class DevelopmentCommandHandler implements CommandExecutor {
    @Command(aliases = {"debuginfo"})
    public void onCommandMyUserID(IMessage message, IUser user, IGuild guild, IChannel channel, String command, String[] args) throws RateLimitException, DiscordException, MissingPermissionsException {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.appendField("Guild ID", guild.getID(), false);
        embedBuilder.appendField("Channel ID", channel.getID(), false);
        embedBuilder.appendField("User ID", user.getID(), false);

        RequestBufferHelper.RequestBuffer(channel, "", embedBuilder.build(), false);
    }

    @Command(aliases = {"leaveall"}, showInHelpPage = false)
    public void onCommandLeaveAllConnectedVoiceChannels(IMessage message, IUser user, IGuild guild, IChannel channel, String command, String[] args) throws RateLimitException, DiscordException, MissingPermissionsException {
        //my id
        if (user.getID().equals("200989665304641536")){
            for (IVoiceChannel iVoiceChannel:ExpeditConst.iDiscordClient.getVoiceChannels()) {
                iVoiceChannel.leave();
            }
        }
    }

    @Command(aliases = {"warnall"}, showInHelpPage = false)
    public void onCommand(IMessage message, IUser user, IGuild guild, IChannel channel, String command, String[] args) throws RateLimitException, DiscordException, MissingPermissionsException {
        //my id
        if (user.getID().equals("200989665304641536")){
            for (IGuild g:ExpeditConst.iDiscordClient.getGuilds()) {
                RequestBufferHelper.RequestBuffer(g.getChannels().get(0), StringHelper.arrayToString(args));
            }
        }
    }

    @Command(aliases = {"ping"}, description = "Pong!", async = true)
    public void onCommandPing(IMessage message, IUser user, IGuild guild, IChannel channel, String command, String[] args) throws RateLimitException, DiscordException, MissingPermissionsException {
        IMessage msg = channel.sendMessage("Pong!");
        LocalDateTime firstTime = message.getTimestamp();
        LocalDateTime secondTime = msg.getTimestamp();
        RequestBufferHelper.RequestBuffer(channel, "That took " + ChronoUnit.MICROS.between(firstTime, secondTime) + " micro seconds");
    }
}
