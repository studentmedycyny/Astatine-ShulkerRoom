package net.ernest.shulkerroom.configuration;

import net.ernest.shulkerroom.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class ShulkerRoomConfiguration extends Configuration{
    public ShulkerRoomConfiguration(Main plugin, String configName) {
        super(plugin, configName);
    }
    public void createRoomInConfig(String roomId, Location location){
        this.getConfig().set("rooms." + roomId + ".location.world", location.getWorld().getName());
        this.getConfig().set("rooms." + roomId + ".location.x", location.getX());
        this.getConfig().set("rooms." + roomId + ".location.y", location.getY());
        this.getConfig().set("rooms." + roomId + ".location.z", location.getZ());
        this.getConfig().set("rooms." + roomId + ".location.yaw", location.getYaw());
        this.getConfig().set("rooms." + roomId + ".location.pitch", location.getPitch());
        Main.getPlugin().getShulkerRoomConfiguration().save();
    }
    public void removeRoomInConfig(String roomId){
        this.getConfig().set("rooms." + roomId, null);
        Main.getPlugin().getShulkerRoomConfiguration().save();
    }
    public Location getRoomLocation(String roomId){
        return new Location(
                Bukkit.getWorld(this.getConfig().getString("rooms." + roomId + ".location.world")),
                this.getConfig().getDouble("rooms." + roomId + ".location.x"),
                this.getConfig().getDouble("rooms." + roomId + ".location.y"),
                this.getConfig().getDouble("rooms." + roomId + ".location.z"),
                (float) this.getConfig().getDouble("rooms." + roomId + ".location.yaw"),
                (float) this.getConfig().getDouble("rooms." + roomId + ".location.pitch")
        );
    }
    public Location getExitLocation(String roomId){
        return new Location(
                Bukkit.getWorld(this.getConfig().getString("rooms." + roomId + ".exit.world")),
                this.getConfig().getDouble("rooms." + roomId + ".exit.x"),
                this.getConfig().getDouble("rooms." + roomId + ".exit.y"),
                this.getConfig().getDouble("rooms." + roomId + ".exit.z"),
                (float) this.getConfig().getDouble("rooms." + roomId + ".exit.yaw"),
                (float) this.getConfig().getDouble("rooms." + roomId + ".exit.pitch")
        );
    }
    public void setExitLocation(String roomId, Location location){
        this.getConfig().set("rooms." + roomId + ".exit.world", location.getWorld().getName());
        this.getConfig().set("rooms." + roomId + ".exit.x", location.getX());
        this.getConfig().set("rooms." + roomId + ".exit.y", location.getY());
        this.getConfig().set("rooms." + roomId + ".exit.z", location.getZ());
        this.getConfig().set("rooms." + roomId + ".exit.yaw", location.getYaw());
        this.getConfig().set("rooms." + roomId + ".exit.pitch", location.getPitch());
        Main.getPlugin().getShulkerRoomConfiguration().save();
    }
    public boolean isExitLocationExist(String roomId){
        return this.getConfig().get("rooms." + roomId + ".exit") != null;
    }
}
