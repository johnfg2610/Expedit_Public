package io.github.johnfg10.handlers;

import io.github.johnfg10.Expedit;
import io.github.johnfg10.ExpeditConst;
import sx.blah.discord.api.events.IListener;
import sx.blah.discord.handle.impl.events.UserVoiceChannelJoinEvent;
import sx.blah.discord.util.MissingPermissionsException;

import java.util.Objects;

/**
 * Created by johnfg10 on 30/03/2017.
 */
public class onUserVoiceChannelJoinEvent implements IListener<UserVoiceChannelJoinEvent> {
    @Override
    public void handle(UserVoiceChannelJoinEvent event) {
            //ToDo make this configurable
            if (event.getChannel().getName().matches("Music_Channel")){
                try {
                    event.getChannel().join();
                    ExpeditConst.audioHelper.getGuildAudioPlayer(event.getChannel().getGuild()).player.setPaused(false);
                } catch (MissingPermissionsException e) {
                    e.printStackTrace();
                }
            }
    }
}
