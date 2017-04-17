package io.github.johnfg10.utils.storage;

import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;

import java.io.File;
import java.io.IOException;

/**
 * Created by johnfg10 on 30/03/2017.
 */
public class ConfigSettings {

    private ConfigurationLoader<CommentedConfigurationNode> loader;
    private CommentedConfigurationNode node;

    public ConfigSettings(String fileLoc) throws IOException {
        File file = new File(fileLoc);

        if (!file.exists()){
            file.createNewFile();

            loader = HoconConfigurationLoader.builder().setFile(file).build(); // Create the loader
            node = loader.createEmptyNode();
            node.getNode("config", "version").setValue("1.0.0").setComment("DO NOT CHANGE THIS EVER!!! LIKE I MEAN IT NEVER CHANGE IT!");

            node.getNode("mysql", "ismysqlenabled").setValue(false).setComment("the hostname without port");
            node.getNode("mysql", "hostname").setValue("").setComment("the hostname without port");
            node.getNode("mysql", "port").setValue(3306).setComment("the port");
            node.getNode("mysql", "username").setValue("").setComment("the username");
            node.getNode("mysql", "password").setValue("").setComment("the password");
            node.getNode("mysql", "schema").setValue("").setComment("the schema");

            node.getNode("token", "youtubeapi").setValue("").setComment("your youtube api key");
            node.getNode("token", "clienttoken").setValue("").setComment("the discord client token");
            node.getNode("token", "githublogin").setValue("").setComment("the youtube login");
            node.getNode("token", "githubpass").setValue("").setComment("the youtube password");
            loader.save(node);
        }
        loader = HoconConfigurationLoader.builder().setFile(file).build(); // Create the loader
        node = loader.load();
    }

    public CommentedConfigurationNode getNode() {
        return node;
    }
}
