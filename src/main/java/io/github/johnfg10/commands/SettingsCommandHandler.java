package io.github.johnfg10.commands;

import de.btobastian.sdcf4j.Command;
import io.github.johnfg10.Expedit;
import io.github.johnfg10.ExpeditConst;
import io.github.johnfg10.utils.RequestBufferHelper;
import sx.blah.discord.handle.obj.*;
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
        try {
            if (hasPerm(user, guild, ExpeditConst.databaseUtils.getSetting("modrole", guild.getID()))) {
                switch (args[0]) {
                    case "defaultprefix":
                        if (args[1] != null) {
                            try {
                                ExpeditConst.databaseUtils.excuteSql(
                                        String.format(
                                                "INSERT INTO %1s.generalsettings (guildid, defaultprefix) \n" +
                                                        "VALUES (%2s,'%3s')\n" +
                                                        "ON DUPLICATE KEY UPDATE defaultprefix = '%4s';",
                                                ExpeditConst.databaseUtils.getSchema(),
                                                guild.getID(),
                                                args[1].replaceAll(" ", ""),
                                                args[1].replaceAll(" ", "")
                                        )
                                );
                                RequestBufferHelper.RequestBuffer(channel, "Updated!");
                            } catch (SQLException | InstantiationException | IllegalAccessException | ClassNotFoundException e) {
                                RequestBufferHelper.RequestBuffer(channel, e.getMessage());
                            }
                        } else {
                            RequestBufferHelper.RequestBuffer(channel, "second argument can not be null");
                        }
                        break;
                    case "modrole":
                        if (args[1] != null) {
                            try {

                                ExpeditConst.databaseUtils.excuteSql(
                                        String.format(
                                                "INSERT INTO %1s.generalsettings (guildid, modrole) \n" +
                                                        "VALUES (%2s,'%3s')\n" +
                                                        "ON DUPLICATE KEY UPDATE modrole = '%4s';",
                                                ExpeditConst.databaseUtils.getSchema(),
                                                guild.getID(),
                                                args[1].replaceAll(" ", ""),
                                                args[1].replaceAll(" ", "")
                                        )
                                );
                                RequestBufferHelper.RequestBuffer(channel, "Updated!");
                            } catch (SQLException | InstantiationException | IllegalAccessException | ClassNotFoundException e) {
                                RequestBufferHelper.RequestBuffer(channel, e.getMessage());
                            }
                        } else {
                            RequestBufferHelper.RequestBuffer(channel, "second argument can not be null");
                        }
                        break;
                    case "musictext":
                        if (args[1] != null) {
                            try {

                                ExpeditConst.databaseUtils.excuteSql(
                                        String.format(
                                                "INSERT INTO %1s.generalsettings (guildid, musicText) \n" +
                                                        "VALUES (%2s,'%3s')\n" +
                                                        "ON DUPLICATE KEY UPDATE musicText = '%4s';",
                                                ExpeditConst.databaseUtils.getSchema(),
                                                guild.getID(),
                                                args[1].replaceAll(" ", ""),
                                                args[1].replaceAll(" ", "")
                                        )
                                );
                                RequestBufferHelper.RequestBuffer(channel, "Updated!");
                            } catch (SQLException | InstantiationException | IllegalAccessException | ClassNotFoundException e) {
                                RequestBufferHelper.RequestBuffer(channel, e.getMessage());
                            }
                        } else {
                            RequestBufferHelper.RequestBuffer(channel, "second argument can not be null");
                        }
                        break;
                    case "musicvoice":
                        if (args[1] != null) {
                            try {

                                ExpeditConst.databaseUtils.excuteSql(
                                        String.format(
                                                "INSERT INTO %1s.generalsettings (guildid, musicVoice) \n" +
                                                        "VALUES (%2s,'%3s')\n" +
                                                        "ON DUPLICATE KEY UPDATE musicVoice = '%4s';",
                                                ExpeditConst.databaseUtils.getSchema(),
                                                guild.getID(),
                                                args[1].replaceAll(" ", ""),
                                                args[1].replaceAll(" ", "")
                                        )
                                );
                                RequestBufferHelper.RequestBuffer(channel, "Updated!");
                            } catch (SQLException | InstantiationException | IllegalAccessException | ClassNotFoundException e) {
                                RequestBufferHelper.RequestBuffer(channel, e.getMessage());
                            }
                        } else {
                            RequestBufferHelper.RequestBuffer(channel, "second argument can not be null");
                        }
                        break;
                    default:
                        RequestBufferHelper.RequestBuffer(channel, "Command " + args[0] + " not recognised");
                        break;
                }
            }
        } catch (ClassNotFoundException | IllegalAccessException | SQLException | InstantiationException e) {
            RequestBufferHelper.RequestBuffer(channel, e.getMessage());
        }
    }
/*
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
    }*/

    public boolean hasPerm(IUser user, IGuild guild, String perm){
        if(user.getPermissionsForGuild(guild).contains(Permissions.ADMINISTRATOR)){
            return true;
        }
        for (IRole role:user.getRolesForGuild(guild)) {
            if(role.getName().equals(perm)){
                return true;
            }
        }
        return false;
    }
}
