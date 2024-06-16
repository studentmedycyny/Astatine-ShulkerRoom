package net.ernest.shulkerroom.configuration;


import net.ernest.shulkerroom.Main;

import java.util.List;

public class MessageConfiguration extends Configuration{
    public MessageConfiguration(Main plugin, String configName) {
        super(plugin, configName);
    }
    public String get(String message){
        return this.getConfig().getString("messages." + message);
    }
    public List<String> getList(String message){
        return this.getConfig().getStringList("messages." + message);
    }
}
