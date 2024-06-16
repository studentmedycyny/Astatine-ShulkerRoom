package net.ernest.shulkerroom.configuration;

import lombok.Getter;
import lombok.SneakyThrows;
import net.ernest.shulkerroom.Main;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public abstract class Configuration {
    private final Main plugin;
    private final String configName;
    private File configFile;
    @Getter private FileConfiguration config;
    public Configuration(Main plugin, String configName){
        this.plugin = plugin;
        this.configName = configName;
        this.configFile = new File(Bukkit.getServer().getWorldContainer().getAbsolutePath() + File.separator + "plugins" + File.separator + Main.getPlugin().getDescription().getName(), configName);
        this.config = new YamlConfiguration();
    }
    public void create() {
        if (!configFile.exists()) {
            configFile.getParentFile().mkdirs();
            this.plugin.saveResource(this.configName, false);
        }
        this.load();
    }
    @SneakyThrows
    public void save() {
        config.save(configFile);
    }

    @SneakyThrows
    public void load(){
        config.load(configFile);
    }
}
