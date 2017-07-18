package io.github.johnfg10.handlers;

import io.github.johnfg10.ExpeditConst;
import sx.blah.discord.api.events.IListener;
import sx.blah.discord.handle.impl.events.UserVoiceChannelMoveEvent;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.MissingPermissionsException;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by johnfg10 on 30/03/2017.
 */
public class onUserVoiceChannelMoveEvent implements IListener<UserVoiceChannelMoveEvent> {
    @Override
    public void handle(UserVoiceChannelMoveEvent event) {
        try {
            if (event.getNewChannel().getName().matches(ExpeditConst.databaseUtils.getSetting("musicVoice", event.getOldChannel().getGuild().getID()))){
                if (!event.getNewChannel().isConnected()){
                    try {
                        event.getNewChannel().join();
                        ExpeditConst.audioHelper.getGuildAudioPlayer(event.getNewChannel().getGuild()).player.setPaused(false);
                    } catch (MissingPermissionsException e) {
                        e.printStackTrace();
                    }
                }
            }else if(event.getOldChannel().getName().matches(ExpeditConst.databaseUtils.getSetting("musicVoice", event.getOldChannel().getGuild().getID()))){
                if (event.getOldChannel().getGuild().getVoiceChannelByID(event.getOldChannel().getID()).getConnectedUsers().size() <=  1){
                    ExpeditConst.audioHelper.getGuildAudioPlayer(event.getOldChannel().getGuild()).player.setPaused(true);
                    event.getOldChannel().leave();
                }
            }
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | SQLException e) {
            e.printStackTrace();
        }
    }
}
