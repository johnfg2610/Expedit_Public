package io.github.johnfg10.handlers;

import io.github.johnfg10.ExpeditConst;
import sx.blah.discord.api.events.IListener;
import sx.blah.discord.handle.impl.events.UserVoiceChannelMoveEvent;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.MissingPermissionsException;

import java.util.List;

/**
 * Created by johnfg10 on 30/03/2017.
 */
public class onUserVoiceChannelMoveEvent implements IListener<UserVoiceChannelMoveEvent> {
    @Override
    public void handle(UserVoiceChannelMoveEvent event) {
        if (event.getNewChannel().getName().matches("Music_Channel")){
            if (!event.getNewChannel().isConnected()){
                try {
                    event.getNewChannel().join();
                    ExpeditConst.audioHelper.getGuildAudioPlayer(event.getNewChannel().getGuild()).player.setPaused(false);
                } catch (MissingPermissionsException e) {
                    e.printStackTrace();
                }
            }
        }else if(event.getOldChannel().getName().matches("Music_Channel")){
            if (event.getOldChannel().getGuild().getVoiceChannelByID(event.getOldChannel().getID()).getConnectedUsers().size() <=  2){
                ExpeditConst.audioHelper.getGuildAudioPlayer(event.getOldChannel().getGuild()).player.setPaused(true);
                event.getOldChannel().leave();
            }
        }
    }
}
