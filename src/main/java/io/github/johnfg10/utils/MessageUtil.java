package io.github.johnfg10.utils;

import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.Permissions;

import java.util.Objects;

/**
 * Created by johnfg10 on 21/03/2017.
 */
public class MessageUtil {
    public static boolean hasPerm(IUser user, IGuild guild, String perm){
        if(user.getPermissionsForGuild(guild).contains(Permissions.ADMINISTRATOR))
            return true;
        //overrides if the users is me
        if (Objects.equals(user.getStringID(), "200989665304641536"))
            return true;

        for (IRole role:user.getRolesForGuild(guild)) {
            if(role.getName().equals(perm)){
                return true;
            }
        }
        return false;
    }
}
