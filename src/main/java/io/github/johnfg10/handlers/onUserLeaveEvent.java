package io.github.johnfg10.handlers;

import sx.blah.discord.api.events.IListener;
import sx.blah.discord.handle.impl.events.guild.member.UserLeaveEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

/**
 * Created by johnfg10 on 18/03/2017.
 */
public class onUserLeaveEvent implements IListener<UserLeaveEvent> {
    @Override
    public void handle(UserLeaveEvent event) {
        IGuild guild = event.getGuild();

        IChannel channel1 = guild.getChannelsByName("log").get(0);
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.withTitle("User left");
        embedBuilder.withAuthorName(event.getUser().getName());
        if(event.getUser().getAvatarURL() != null)
            embedBuilder.withAuthorIcon(event.getUser().getAvatarURL());

        embedBuilder.appendField("Username", event.getUser().getName(), false);
        embedBuilder.appendField("ID", event.getUser().getStringID(), false);

        try {
            channel1.sendMessage("", embedBuilder.build(), false);
        } catch (MissingPermissionsException | RateLimitException | DiscordException e) {
            e.printStackTrace();
        }
    }
}
