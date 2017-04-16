package io.github.johnfg10.handlers;

import io.github.johnfg10.Expedit;
import io.github.johnfg10.ExpeditConst;
import sx.blah.discord.api.events.IListener;
import sx.blah.discord.handle.impl.events.UserVoiceChannelJoinEvent;
import sx.blah.discord.util.MissingPermissionsException;

import java.sql.SQLException;
import java.util.Objects;

/**
 * Created by johnfg10 on 30/03/2017.
 */
public class onUserVoiceChannelJoinEvent implements IListener<UserVoiceChannelJoinEvent> {
    @Override
    public void handle(UserVoiceChannelJoinEvent event) {
        try {
            if (event.getChannel().getName().matches(ExpeditConst.databaseUtils.getSetting("musicVoice", event.getChannel().getGuild().getID()))){
                try {
                    event.getChannel().join();
                    ExpeditConst.audioHelper.getGuildAudioPlayer(event.getChannel().getGuild()).player.setPaused(false);
                } catch (MissingPermissionsException e) {
                    e.printStackTrace();
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
