package io.github.johnfg10.handlers;

import sx.blah.discord.api.events.IListener;
import sx.blah.discord.handle.impl.events.NickNameChangeEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

/**
 * Created by johnfg10 on 18/03/2017.
 */
public class onNickNameChangeEvent implements IListener<NickNameChangeEvent> {
    @Override
    public void handle(NickNameChangeEvent event) {
        IGuild guild = event.getGuild();

        IChannel channel1 = guild.getChannelsByName("log").get(0);
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.withTitle("Deleted messages");
        embedBuilder.withAuthorName(event.getUser().getName());
        if(event.getUser().getAvatarURL() != null)
            embedBuilder.withAuthorIcon(event.getUser().getAvatarURL());

        embedBuilder.appendField("New nickname", event.getNewNickname().get(), false);
        embedBuilder.appendField("Old nickname", event.getOldNickname().get(), false);
        embedBuilder.appendField("Username", event.getUser().getName(), false);
        embedBuilder.appendField("User ID", event.getUser().getID(), false);

        try {
            channel1.sendMessage("", embedBuilder.build(), false);
        } catch (MissingPermissionsException | RateLimitException | DiscordException e) {
            e.printStackTrace();
        }
    }
}
