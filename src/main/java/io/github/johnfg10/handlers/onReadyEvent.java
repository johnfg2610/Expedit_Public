package io.github.johnfg10.handlers;

import io.github.johnfg10.ExpeditConst;
import sx.blah.discord.api.events.IListener;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by johnfg10 on 18/03/2017.
 */
public class onReadyEvent implements IListener<ReadyEvent> {
    @Override
    public void handle(ReadyEvent event) {
        List<IGuild> guilds = ExpeditConst.iDiscordClient.getGuilds();

        for (IGuild guild:guilds) {
            try {
                ExpeditConst.databaseUtils.excuteSql(
                        "INSERT INTO botdatabase.generalsettings (guildid, defaultprefix, modrole) " +
                                "VALUES (" +
                                guild.getID() +
                                ", '^', 'expeditmod'" +
                                ")"
                );
            } catch (SQLException e) {
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
            }
        }
    }
}
