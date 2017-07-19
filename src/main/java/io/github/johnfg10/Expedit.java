package io.github.johnfg10;

import com.wrapper.spotify.Api;
import de.btobastian.sdcf4j.handler.Discord4JHandler;
import io.github.johnfg10.commands.*;
import io.github.johnfg10.handlers.*;
import io.github.johnfg10.utils.AudioHelper;
import io.github.johnfg10.utils.storage.ConfigSettings;
import io.github.johnfg10.utils.storage.GeneralSettingsDatabaseUtils;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventDispatcher;
import sx.blah.discord.util.DiscordException;

import java.io.IOException;
import java.sql.Connection;
import java.util.Timer;

/**
 * Created by johnfg10 on 16/03/2017.
 */
public class Expedit {

    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        ConfigSettings configSettings = null;
        Connection conn = null;
        try {
            configSettings = new ConfigSettings("./expeditconfig.hocon");
        } catch (IOException e) {
            e.printStackTrace();
        }
        boolean bool = configSettings.getNode().getNode("mysql", "ismysqlenabled").getBoolean();
        if(bool){
            ExpeditConst.databaseUtils = new GeneralSettingsDatabaseUtils(configSettings.getNode().getNode("mysql", "hostname").getString(), configSettings.getNode().getNode("mysql", "port").getInt(), configSettings.getNode().getNode("mysql", "username").getString(), configSettings.getNode().getNode("mysql", "password").getString(),  configSettings.getNode().getNode("mysql", "schema").getString());
        } else{
            //ToDo add fall back??
        }

        if (configSettings.getNode().getNode("token", "clienttoken").getString() == "" || configSettings.getNode().getNode("token", "youtubeapi").getString() == "" || configSettings.getNode().getNode("token", "defaultprefix").getString() == ""){
            System.out.println("All tokens need to be filled in!");
            return;
        }

        Timer timer = new Timer("oneMin");
        timer.schedule(new onMinuteTimer(), 1, 60000);

        ExpeditConst.iDiscordClient = createClient(configSettings.getNode().getNode("token", "clienttoken").getString(), true);
        ExpeditConst.youtubeApi = configSettings.getNode().getNode("token", "youtubeapi").getString();
        ExpeditConst.audioHelper = new AudioHelper();
        ExpeditConst.commandHandler = new Discord4JHandler(ExpeditConst.iDiscordClient);
        ExpeditConst.commandHandler.setDefaultPrefix("^");
        ExpeditConst.configSettings = configSettings;
        ExpeditConst.SPOTIFYAPI = Api.builder()
                .clientId(configSettings.getNode().getNode("token", "spotifyclientid").getString())
                .clientSecret(configSettings.getNode().getNode("token", "spotifyclientsecret").getString())
                .build();

        ExpeditConst.commandHandler.registerCommand(new GeneralCommandHandler());
        ExpeditConst.commandHandler.registerCommand(new AudioCommandHandler());
        ExpeditConst.commandHandler.registerCommand(new SettingsCommandHandler());
        ExpeditConst.commandHandler.registerCommand(new DevelopmentCommandHandler());
        ExpeditConst.commandHandler.registerCommand(new SpotifyCommandHandlers());
        ExpeditConst.commandHandler.registerCommand(new BannedWordCommandHandler());

        EventDispatcher eventDispatcher = ExpeditConst.iDiscordClient.getDispatcher();

        eventDispatcher.registerListener(new onUserJoinEvent());
        eventDispatcher.registerListener(new onMessageDeleteEvent());
        eventDispatcher.registerListener(new onNickNameChangeEvent());
        eventDispatcher.registerListener(new onReadyEvent());
        eventDispatcher.registerListener(new onUserLeaveEvent());
        eventDispatcher.registerListener(new onUserBanEvent());
        eventDispatcher.registerListener(new onUserVoiceChannelJoinEvent());
        eventDispatcher.registerListener(new onUserVoiceChannelLeaveEvent());
        eventDispatcher.registerListener(new onUserVoiceChannelMoveEvent());

        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                System.out.println("Shutting down essential core processes");
                ExpeditConst.iDiscordClient.logout();
            }
        });
    }

    public static IDiscordClient createClient(String token, boolean login) { // Returns a new instance of the Discord client
        ClientBuilder clientBuilder = new ClientBuilder(); // Creates the ClientBuilder instance
        clientBuilder.withToken(token); // Adds the login info to the builder
        try {
            if (login) {
                return clientBuilder.login(); // Creates the client instance and logs the client in
            } else {
                return clientBuilder.build(); // Creates the client instance but it doesn't log the client in yet, you would have to call client.login() yourself
            }
        } catch (DiscordException e) { // This is thrown if there was a problem building the client
            e.printStackTrace();
            return null;
        }
    }



}
