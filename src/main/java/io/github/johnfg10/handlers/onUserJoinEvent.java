package io.github.johnfg10.handlers;

import io.github.johnfg10.ExpeditConst;
import sx.blah.discord.api.events.IListener;
import sx.blah.discord.handle.impl.events.guild.member.UserJoinEvent;

import java.sql.SQLException;

/**
 * Created by johnfg10 on 17/03/2017.
 */
public class onUserJoinEvent implements IListener<UserJoinEvent> {
    @Override
    public void handle(UserJoinEvent event) {
/*        IRole role = event.getGuild().getRolesByName("Member").get(0);
        if(role != null){
            try {
                event.getUser().addRole(role);
            } catch (MissingPermissionsException | DiscordException | RateLimitException e) {
                e.printStackTrace();
            }
        }

        IGuild guild = event.getGuild();

        IChannel channel1 = guild.getChannelsByName("log").get(0);
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.withTitle("User Joined");
        embedBuilder.withAuthorName(event.getUser().getName());
        if(event.getUser().getAvatarURL() != null)
            embedBuilder.withAuthorIcon(event.getUser().getAvatarURL());

        embedBuilder.appendField("Username", event.getUser().getName(), false);
        embedBuilder.appendField("ID", event.getUser().getID(), false);

        try {
            channel1.sendMessage("", embedBuilder.build(), false);
        } catch (MissingPermissionsException | RateLimitException | DiscordException e) {
            e.printStackTrace();
        }*/

        if (event.getUser().equals(ExpeditConst.iDiscordClient.getOurUser())){
            try {
                ExpeditConst.databaseUtils.setupDefaultGuildEntry(event.getGuild().getStringID());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
