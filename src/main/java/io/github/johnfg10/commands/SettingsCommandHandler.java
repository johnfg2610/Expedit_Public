package io.github.johnfg10.commands;

import de.btobastian.sdcf4j.Command;
import io.github.johnfg10.ExpeditConst;
import io.github.johnfg10.utils.RequestBufferHelper;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by johnfg10 on 02/04/2017.
 */
public class SettingsCommandHandler implements de.btobastian.sdcf4j.CommandExecutor {
    @Command(aliases = {"setsettings", "setsetting"})
    public void onCommandSetSettings(IMessage message, IUser user, IGuild guild, IChannel channel, String command, String[] args) throws RateLimitException, DiscordException, MissingPermissionsException {
        switch (args[0]){
            case "defaultprefix":
                if (args[1] != null){
                    try {
                        ExpeditConst.databaseUtils.excuteSql(
                                "\nUPDATE botdatabase.generalsettings" +
                                        "\nSET defaultprefix = '" +
                                        args[1] +
                                        "'" +
                                        "\nWHERE guildid = '" +
                                        guild.getID() +
                                        "';"
                        );
                        RequestBufferHelper.RequestBuffer(channel, "Updated!");
                    } catch (SQLException | InstantiationException | IllegalAccessException | ClassNotFoundException e) {
                        RequestBufferHelper.RequestBuffer(channel, e.getMessage());
                    }
                }else {
                    RequestBufferHelper.RequestBuffer(channel, "second argument can not be null");
                }
                break;
            case "modrole":
                if (args[1] != null){
                    try {
                        ExpeditConst.databaseUtils.excuteSql(
                                "\nUPDATE botdatabase.generalsettings" +
                                        "\nSET modrole = '" +
                                        args[1] +
                                        "'" +
                                        "\nWHERE guildid = '" +
                                        guild.getID() +
                                        "';"
                        );
                        RequestBufferHelper.RequestBuffer(channel, "Updated!");
                    } catch (SQLException | InstantiationException | IllegalAccessException | ClassNotFoundException e) {
                        RequestBufferHelper.RequestBuffer(channel, e.getMessage());
                    }
                }else {
                    RequestBufferHelper.RequestBuffer(channel, "second argument can not be null");
                }
                break;
                default:
                    RequestBufferHelper.RequestBuffer(channel, "Command " + args[0] + " not recognised");
                    break;
        }
    }

    @Command(aliases = {"getsettings", "getsetting"})
    public void onCommandGetSettings(IMessage message, IUser user, IGuild guild, IChannel channel, String command, String[] args){
        switch (args[0]){
            case "defaultprefix":
                try {
                    RequestBufferHelper.RequestBuffer(channel, ExpeditConst.databaseUtils.getSetting("defaultprefix", guild.getID()));
                } catch (SQLException | InstantiationException | IllegalAccessException | ClassNotFoundException e) {
                    RequestBufferHelper.RequestBuffer(channel, e.getMessage());
                }
                break;
            case "modrole":
                try {
                    RequestBufferHelper.RequestBuffer(channel, ExpeditConst.databaseUtils.getSetting("modrole", guild.getID()));
                } catch (SQLException | InstantiationException | IllegalAccessException | ClassNotFoundException e) {
                    RequestBufferHelper.RequestBuffer(channel, e.getMessage());
                }
                break;
            default:
                RequestBufferHelper.RequestBuffer(channel, "Command " + args[0] + " not recognised");
                break;
        }
    }
}
