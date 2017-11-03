package io.github.johnfg10.handlers;


import sx.blah.discord.api.events.IListener;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageDeleteEvent;

/**
 * Created by johnfg10 on 18/03/2017.
 */
public class onMessageDeleteEvent implements IListener<MessageDeleteEvent> {
    @Override
    public void handle(MessageDeleteEvent event) {
/*        IMessage message = event.getMessage();
        IGuild guild = event.getMessage().getGuild();

        IChannel channel1 = guild.getChannelsByName("log").get(0);
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.withTitle("Deleted messages");
        embedBuilder.withAuthorName(message.getAuthor().getName());
        if(message.getAuthor().getAvatarURL() != null)
        embedBuilder.withAuthorIcon(message.getAuthor().getAvatarURL());

        embedBuilder.appendField("Old message", message.getFormattedContent(), false);
        embedBuilder.appendField("Message ID", message.getID(), false);
        embedBuilder.appendField("Channel name", message.getChannel().getName(), false);
        embedBuilder.appendField("Channel ID", message.getChannel().getID(), false);

        try {
            channel1.sendMessage("", embedBuilder.build(), false);
        } catch (MissingPermissionsException | RateLimitException | DiscordException e) {
        }*/
    }
}
