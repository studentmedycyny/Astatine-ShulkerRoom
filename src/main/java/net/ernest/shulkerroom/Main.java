package net.ernest.shulkerroom;

import lombok.Getter;
import net.ernest.shulkerroom.command.ShulkerRoomCommand;
import net.ernest.shulkerroom.command.tabcompleter.ShulkerRoomTabCompleter;
import net.ernest.shulkerroom.configuration.MenuConfiguration;
import net.ernest.shulkerroom.configuration.MessageConfiguration;
import net.ernest.shulkerroom.configuration.PluginConfiguration;
import net.ernest.shulkerroom.configuration.ShulkerRoomConfiguration;
import net.ernest.shulkerroom.listener.*;
import net.ernest.shulkerroom.room.ShulkerRoom;
import net.ernest.shulkerroom.room.ShulkerRoomFactory;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    @Getter private static Main plugin;
    @Getter private ShulkerRoomFactory shulkerRoomFactory;
    @Getter private ShulkerRoomConfiguration shulkerRoomConfiguration;
    @Getter private MessageConfiguration messageConfiguration;
    @Getter private PluginConfiguration pluginConfiguration;
    @Getter private MenuConfiguration menuConfiguration;

    @Override
    public void onEnable() {
        plugin = this;
        messageConfiguration = new MessageConfiguration(this, "messages.yml");
        messageConfiguration.create();
        shulkerRoomConfiguration = new ShulkerRoomConfiguration(this, "rooms.yml");
        shulkerRoomConfiguration.create();
        menuConfiguration = new MenuConfiguration(this, "menu.yml");
        menuConfiguration.create();
        pluginConfiguration = new PluginConfiguration(this, "config.yml");
        pluginConfiguration.create();
        shulkerRoomFactory = new ShulkerRoomFactory();
        registerListeners(
                new PlayerQuitListener(this),
                new BlockPlaceBreakListener(this),
                new PlayerInteractListener(this),
                new PlayerDeathListener(this),
                new PlayerCommandPreprocessListener(this));
        registerCommands();
    }

    @Override
    public void onDisable() {
        plugin = null;
        Bukkit.getScheduler().cancelTasks(this);
    }
    private void registerListeners(Listener... listeners){
        PluginManager pluginManager = Bukkit.getPluginManager();
        for(Listener listener : listeners){
            pluginManager.registerEvents(listener, this);
        }
    }
    private void registerCommands(){
        getCommand("shulkerroom").setExecutor(new ShulkerRoomCommand(this));
        getCommand("shulkerroom").setTabCompleter(new ShulkerRoomTabCompleter(this));
    }
}