package io.github.johnfg10;

import de.btobastian.sdcf4j.CommandHandler;
import io.github.johnfg10.commands.GeneralCommandHandler;
import io.github.johnfg10.utils.AudioHelper;
import io.github.johnfg10.utils.storage.GeneralSettingsDatabaseUtils;
import sx.blah.discord.api.IDiscordClient;

import java.sql.Connection;

/**
 * Created by johnfg10 on 16/03/2017.
 */
public class ExpeditConst {
    public static String youtubeApi;

    private static ExpeditConst ourInstance = new ExpeditConst();

    public static ExpeditConst getInstance() {
        return ourInstance;
    }

    public static IDiscordClient iDiscordClient;
    public static CommandHandler commandHandler;
    public static Connection connection;
    public static AudioHelper audioHelper;
    public static GeneralSettingsDatabaseUtils databaseUtils;
}
