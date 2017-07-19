package io.github.johnfg10.commands;

import de.btobastian.sdcf4j.Command;
import io.github.johnfg10.ExpeditConst;
import io.github.johnfg10.utils.RequestBufferHelper;
import org.json.JSONArray;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

import java.sql.SQLException;

import static io.github.johnfg10.utils.MessageUtil.hasPerm;

public class BannedWordCommandHandler {
    @Command(aliases = {"addtofilter"}, description = "Removes all messages in a channel")
    public void onCommandAddToFilter(IMessage message, IUser user, IGuild guild, IChannel channel, String command, String[] args) throws RateLimitException, DiscordException, MissingPermissionsException {
        try {
            String jsonRaw = ExpeditConst.databaseUtils.getSetting("", guild.getStringID());
            JSONArray jsonArray = new JSONArray(jsonRaw);
            jsonArray.put(message.getContent().replaceAll(command, ""));
            ExpeditConst.databaseUtils.excuteSql("UPDATE " + ExpeditConst.databaseUtils.getSchema() +
                    ".generalsettings(" +
                    "SET bannedwords = '" + jsonArray.toString() + "',"  +
                    "WHERE guildid = " + guild.getStringID() +
                    ")"
            );

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }
}
