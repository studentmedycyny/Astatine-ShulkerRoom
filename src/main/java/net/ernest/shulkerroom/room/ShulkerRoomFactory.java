package net.ernest.shulkerroom.room;

import lombok.Getter;
import net.ernest.shulkerroom.Main;
import net.ernest.shulkerroom.helper.ChatHelper;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.*;

public class ShulkerRoomFactory {
    @Getter private List<ShulkerRoom> rooms = new ArrayList<>();
    @Getter private HashMap<UUID, ShulkerRoom> occupiedRooms = new HashMap<>();
    public ShulkerRoomFactory(){
        FileConfiguration shulkerRoomConfig = Main.getPlugin().getShulkerRoomConfiguration().getConfig();
        ConfigurationSection configurationSection = shulkerRoomConfig.getConfigurationSection("rooms");
        if(configurationSection == null) return;
        for(String id : configurationSection.getKeys(false)){
            Location location = new Location(
                    Bukkit.getWorld(shulkerRoomConfig.getString("rooms." + id + ".location.world")),
                    shulkerRoomConfig.getDouble("rooms." + id + ".location.x"),
                    shulkerRoomConfig.getDouble("rooms." + id + ".location.y"),
                    shulkerRoomConfig.getDouble("rooms." + id + ".location.z"),
                    (float) shulkerRoomConfig.getDouble("rooms." + id + ".location.yaw"),
                    (float) shulkerRoomConfig.getDouble("rooms." + id + ".location.pitch")
            );
            if(shulkerRoomConfig.get("rooms." + id + ".exit") != null) {
                Location exit = new Location(
                        Bukkit.getWorld(shulkerRoomConfig.getString("rooms." + id + ".exit.world")),
                        shulkerRoomConfig.getDouble("rooms." + id + ".exit.x"),
                        shulkerRoomConfig.getDouble("rooms." + id + ".exit.y"),
                        shulkerRoomConfig.getDouble("rooms." + id + ".exit.z"),
                        (float) shulkerRoomConfig.getDouble("rooms." + id + ".exit.yaw"),
                        (float) shulkerRoomConfig.getDouble("rooms." + id + ".exit.pitch")
                );
                this.rooms.add(new ShulkerRoom(id, location, exit));
                continue;
            }
            this.rooms.add(new ShulkerRoom(id, location));
        }
    }
    public void createRoom(Player player, String roomId, Location location){
        if(this.rooms.stream().anyMatch(filter -> filter.getId().equals(roomId))){
            player.sendMessage(ChatHelper.fixColors(Main.getPlugin().getMessageConfiguration().get("room-exist")));
            return;
        }
        this.rooms.add(new ShulkerRoom(roomId, location));
        FileConfiguration shulkerRoomConfig = Main.getPlugin().getShulkerRoomConfiguration().getConfig();
        shulkerRoomConfig.set("rooms." + roomId + ".location.world", location.getWorld().getName());
        shulkerRoomConfig.set("rooms." + roomId + ".location.x", location.getX());
        shulkerRoomConfig.set("rooms." + roomId + ".location.y", location.getY());
        shulkerRoomConfig.set("rooms." + roomId + ".location.z", location.getZ());
        shulkerRoomConfig.set("rooms." + roomId + ".location.yaw", location.getYaw());
        shulkerRoomConfig.set("rooms." + roomId + ".location.pitch", location.getPitch());
        Main.getPlugin().getShulkerRoomConfiguration().save();
        Main.getPlugin().getShulkerRoomConfiguration().load();
        player.sendMessage(ChatHelper.fixColors(Main.getPlugin().getMessageConfiguration().get("room-created").replace("{ID}", roomId)));
    }
    public void removeRoom(Player player, String roomId){
        ShulkerRoom room = this.findRoomById(roomId);
        if(room == null){
            player.sendMessage(ChatHelper.fixColors(Main.getPlugin().getMessageConfiguration().get("room-not-found")));
            return;
        }
        this.rooms.remove(room);
        FileConfiguration shulkerRoomConfig = Main.getPlugin().getShulkerRoomConfiguration().getConfig();
        shulkerRoomConfig.set("rooms." + roomId, null);
        Main.getPlugin().getShulkerRoomConfiguration().save();
        player.sendMessage(ChatHelper.fixColors(Main.getPlugin().getMessageConfiguration().get("room-removed").replace("{ID}", roomId)));

    }
    public boolean isPlayerInRoom(Player player){
        return this.occupiedRooms.get(player.getUniqueId()) != null;
    }
    public void setExitLocation(Player player, String roomId){
        Block targetBlockExact = player.getTargetBlockExact(5);
        if(targetBlockExact == null){
            player.sendMessage(ChatHelper.fixColors(Main.getPlugin().getMessageConfiguration().get("block-to-exit-not-found")));
            return;
        }
        Location location = targetBlockExact.getLocation();
        ShulkerRoom room = this.findRoomById(roomId);
        if(room == null){
            player.sendMessage(ChatHelper.fixColors(Main.getPlugin().getMessageConfiguration().get("room-not-found")));
            return;
        }
        room.setExit(location);
        FileConfiguration shulkerRoomConfig = Main.getPlugin().getShulkerRoomConfiguration().getConfig();
        shulkerRoomConfig.set("rooms." + roomId + ".exit.world", location.getWorld().getName());
        shulkerRoomConfig.set("rooms." + roomId + ".exit.x", location.getX());
        shulkerRoomConfig.set("rooms." + roomId + ".exit.y", location.getY());
        shulkerRoomConfig.set("rooms." + roomId + ".exit.z", location.getZ());
        shulkerRoomConfig.set("rooms." + roomId + ".exit.yaw", location.getYaw());
        shulkerRoomConfig.set("rooms." + roomId + ".exit.pitch", location.getPitch());
        Main.getPlugin().getShulkerRoomConfiguration().save();
        Main.getPlugin().getShulkerRoomConfiguration().load();
        player.sendMessage(ChatHelper.fixColors(Main.getPlugin().getMessageConfiguration().get("room-exit-created").replace("{ID}", roomId)));
    }
    public ShulkerRoom findRoomById(String id){
        for(ShulkerRoom room : this.rooms){
            if(room.getId().equals(id)) return room;
        }
        return null;
    }
    public ShulkerRoom findRoomByUniqueId(UUID uniqueId){
        return this.occupiedRooms.get(uniqueId);
    }
    public void claimRoom(Player player){
        if(this.rooms.isEmpty()){
            player.sendMessage(ChatHelper.fixColors(Main.getPlugin().getMessageConfiguration().get("empty-rooms")));
            return;
        }
        ShulkerRoom shulkerRoom = this.rooms.remove(0);
        shulkerRoom.setOccupiedBy(player.getName());
        this.occupiedRooms.put(player.getUniqueId(), shulkerRoom);
        player.teleport(shulkerRoom.getLocation());
        player.sendMessage(ChatHelper.fixColors(Main.getPlugin().getMessageConfiguration().get("join-room")));
        player.sendTitle(
                ChatHelper.fixColors(Main.getPlugin().getMessageConfiguration().get("join-room-title")),
                ChatHelper.fixColors(Main.getPlugin().getMessageConfiguration().get("join-room-subtitle"))
        );
    }
    public void unclaimRoom(Player player){
        ShulkerRoom shulkerRoom = this.occupiedRooms.remove(player.getUniqueId());
        shulkerRoom.setOccupiedBy(null);
        this.rooms.add(shulkerRoom);
        FileConfiguration configuration = Main.getPlugin().getPluginConfiguration().getConfig();
        Location location = new Location(
                Bukkit.getWorld(configuration.getString("configuration.spawn-location.world")),
                configuration.getDouble("configuration.spawn-location.x"),
                configuration.getDouble("configuration.spawn-location.y"),
                configuration.getDouble("configuration.spawn-location.z"),
                (float) configuration.getDouble("configuration.spawn-location.yaw"),
                (float) configuration.getDouble("configuration.spawn-location.pitch")
        );
        for(Block block : shulkerRoom.getPlacedBlocks()){
            block.setType(Material.AIR);
        }
        player.teleport(location);
        player.sendMessage(ChatHelper.fixColors(Main.getPlugin().getMessageConfiguration().get("leave-room")));
        player.sendTitle(
                ChatHelper.fixColors(Main.getPlugin().getMessageConfiguration().get("leave-room-title")),
                ChatHelper.fixColors(Main.getPlugin().getMessageConfiguration().get("leave-room-subtitle"))
        );
    }
}
