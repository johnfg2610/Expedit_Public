package io.github.johnfg10.handlers;

import io.github.johnfg10.ExpeditConst;
import sx.blah.discord.api.events.IListener;
import sx.blah.discord.handle.impl.events.UserVoiceChannelLeaveEvent;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.MissingPermissionsException;

import java.util.List;

/**
 * Created by johnfg10 on 30/03/2017.
 */
public class onUserVoiceChannelLeaveEvent implements IListener<UserVoiceChannelLeaveEvent> {
    @Override
    public void handle(UserVoiceChannelLeaveEvent event) {
        if (event.getChannel().getName().matches("Music_Channel") && event.getChannel().isConnected()) {
            if (event.getChannel().getGuild().getVoiceChannelByID(event.getChannel().getID()).getConnectedUsers().size() <=  2){
               ExpeditConst.audioHelper.getGuildAudioPlayer(event.getChannel().getGuild()).player.setPaused(true);
               event.getChannel().leave();
            }
        }
    }
}
