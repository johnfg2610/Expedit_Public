package io.github.johnfg10.commands;

import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import io.github.johnfg10.ExpeditConst;
import io.github.johnfg10.utils.RequestBufferHelper;
import io.github.johnfg10.utils.StringHelper;
import org.kohsuke.github.GHIssue;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;
import org.kohsuke.github.extras.OkHttpConnector;
import sx.blah.discord.handle.obj.*;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static com.sun.corba.se.impl.util.RepositoryId.cache;

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
        if (user.getID().equals("200989665304641536")) {
            for (IVoiceChannel iVoiceChannel : ExpeditConst.iDiscordClient.getVoiceChannels()) {
                iVoiceChannel.leave();
            }
        }
    }

    @Command(aliases = {"warnall"}, showInHelpPage = false)
    public void onCommand(IMessage message, IUser user, IGuild guild, IChannel channel, String command, String[] args) throws RateLimitException, DiscordException, MissingPermissionsException {
        //my id
        if (user.getID().equals("200989665304641536")) {
            for (IGuild g : ExpeditConst.iDiscordClient.getGuilds()) {
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



    @Command(aliases = {"reportbug"}, description = "Reports a bug!")
    public void onCommandReportBug(IMessage message, IUser user, IGuild guild, IChannel channel, String command, String[] args) throws RateLimitException, DiscordException, MissingPermissionsException {
        if (ExpeditConst.githubBlackListedUsers.contains(user.getID())) {
            RequestBufferHelper.RequestBuffer(channel, "you have been blacklisted from using this command if you belive this is in error please contact johnfg10");
        }else {
            try {
                GitHub github = GitHub.connectUsingPassword(ExpeditConst.configSettings.getNode().getNode("token", "githublogin").getString(), ExpeditConst.configSettings.getNode().getNode("token", "githubpass").getString());
                //System.out.println(github.createGist().description("test").file("test", "testing stuff").create().getUrl());
                GHIssue issue = github.getUser("johnfg10").getRepository("Expedit_Public").createIssue(user.getName() + "s Bug report").body(StringHelper.arrayToString(args) +
                        "\n" +
                        "additional information:" +
                        "\nGuild ID: " +
                        guild.getID() +
                        "\nChannel ID: " +
                        channel.getID() +
                        "\nUser ID:" +
                        user.getID()
                ).assignee("johnfg10").create();

                RequestBufferHelper.RequestBuffer(channel, "link: " + issue.getHtmlUrl() );
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
