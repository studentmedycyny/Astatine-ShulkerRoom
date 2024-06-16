package net.ernest.shulkerroom.configuration;


import net.ernest.shulkerroom.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class PluginConfiguration extends Configuration{
    public PluginConfiguration(Main plugin, String configName) {
        super(plugin, configName);
    }
    public Location getSpawnLocation(){
        return new Location(
                Bukkit.getWorld(this.getConfig().getString("configuration.spawn-location.world")),
                this.getConfig().getDouble("configuration.spawn-location.x"),
                this.getConfig().getDouble("configuration.spawn-location.y"),
                this.getConfig().getDouble("configuration.spawn-location.z"),
                (float) this.getConfig().getDouble("configuration.spawn-location.yaw"),
                (float) this.getConfig().getDouble("configuration.spawn-location.pitch")
        );
    }
    public void setSpawnLocation(Location location){
        this.getConfig().set("configuration.spawn-location.world", location.getWorld().getName());
        this.getConfig().set("configuration.spawn-location.x", location.getX());
        this.getConfig().set("configuration.spawn-location.y", location.getY());
        this.getConfig().set("configuration.spawn-location.z", location.getZ());
        this.getConfig().set("configuration.spawn-location.yaw", location.getYaw());
        this.getConfig().set("configuration.spawn-location.pitch", location.getPitch());
        this.save();
    }
}
