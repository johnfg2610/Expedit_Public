package io.github.johnfg10.utils;

import ninja.leaping.configurate.ConfigurationOptions;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;

import java.io.File;
import java.io.IOException;

/**
 * Created by johnfg10 on 19/03/2017.
 */
public class ConfigUtils {
     private ConfigurationLoader<CommentedConfigurationNode> loader;

     private CommentedConfigurationNode node;

     private File configFile = new File(System.getProperty("user.dir") + File.separator + "dbinfo.hocon");

     public ConfigUtils() throws IOException {
         if(!configFile.exists()){
             configFile.createNewFile();
             loader = HoconConfigurationLoader.builder().setFile(configFile).build(); // Create the loader
             node = loader.createEmptyNode(); // Load the configuration into memory

             node.getNode("MYSQL", "hostname").setValue("localhost");
             node.getNode("MYSQL", "port").setValue(3306);
             node.getNode("MYSQL", "username").setValue("root");
             node.getNode("MYSQL", "password").setValue("");
             node.getNode("MYSQL", "ismysqlenabled").setValue(false).setComment("if false the default h.2 implementation will be used");

             loader.save(node);
         }else{
             loader = HoconConfigurationLoader.builder().setFile(configFile).build(); // Create the loader
             node = loader.load(); // Load the configuration into memory
         }
     }

    public CommentedConfigurationNode getNode() {
        return node;
    }

    public ConfigurationLoader<CommentedConfigurationNode> getLoader() {
        return loader;
    }

    public File getConfigFile() {
        return configFile;
    }

    public void setConfigFile(File configFile) {
        this.configFile = configFile;
    }

    public void setLoader(ConfigurationLoader<CommentedConfigurationNode> loader) {
        this.loader = loader;
    }

    public void setNode(CommentedConfigurationNode node) {
        this.node = node;
    }
}
