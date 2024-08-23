package net.ernest.shulkerroom.room;

import lombok.Getter;
import net.ernest.shulkerroom.Main;
import net.ernest.shulkerroom.configuration.PluginConfiguration;
import net.ernest.shulkerroom.configuration.ShulkerRoomConfiguration;
import net.ernest.shulkerroom.helper.ChatHelper;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.*;

public class ShulkerRoomFactory {
    @Getter private List<ShulkerRoom> rooms = new ArrayList<>();
    @Getter private HashMap<UUID, ShulkerRoom> occupiedRooms = new HashMap<>();
    public ShulkerRoomFactory(){
        ShulkerRoomConfiguration shulkerRoomConfig = Main.getPlugin().getShulkerRoomConfiguration();
        ConfigurationSection configurationSection = shulkerRoomConfig.getConfig().getConfigurationSection("rooms");
        if(configurationSection == null) return;
        for(String id : configurationSection.getKeys(false)){
            Location location = shulkerRoomConfig.getRoomLocation(id);
            if(shulkerRoomConfig.isExitLocationExist(id)) {
                Location exit = shulkerRoomConfig.getExitLocation(id);
                this.rooms.add(new ShulkerRoom(id, location, exit));
                continue;
            }
            this.rooms.add(new ShulkerRoom(id, location));
        }
    }
    public void createRoom(Player player, String roomId, Location location){
        if(this.rooms.stream().anyMatch(filter -> filter.getId().equals(roomId))){
            ChatHelper.sendMessage(player, Main.getPlugin().getMessageConfiguration().get("room-exist"));
            return;
        }
        this.rooms.add(new ShulkerRoom(roomId, location));
        ShulkerRoomConfiguration shulkerRoomConfig = Main.getPlugin().getShulkerRoomConfiguration();
        shulkerRoomConfig.createRoomInConfig(roomId, location);
        ChatHelper.sendMessage(player, Main.getPlugin().getMessageConfiguration().get("room-created").replace("{ID}", roomId));
    }
    public void removeRoom(Player player, String roomId){
        ShulkerRoom room = this.findRoomById(roomId);
        if(room == null){
            ChatHelper.sendMessage(player, Main.getPlugin().getMessageConfiguration().get("room-not-found"));
            return;
        }
        this.rooms.remove(room);
        ShulkerRoomConfiguration shulkerRoomConfig = Main.getPlugin().getShulkerRoomConfiguration();
        shulkerRoomConfig.removeRoomInConfig(roomId);
        ChatHelper.sendMessage(player, Main.getPlugin().getMessageConfiguration().get("room-removed").replace("{ID}", roomId));

    }
    public boolean isPlayerInRoom(Player player){
        return this.occupiedRooms.get(player.getUniqueId()) != null;
    }
    public void setExitLocation(Player player, String roomId){
        Block targetBlockExact = player.getTargetBlockExact(5);
        if(targetBlockExact == null){
            ChatHelper.sendMessage(player, Main.getPlugin().getMessageConfiguration().get("block-to-exit-not-found"));
            return;
        }
        Location location = targetBlockExact.getLocation();
        ShulkerRoom room = this.findRoomById(roomId);
        if(room == null){
            ChatHelper.sendMessage(player, Main.getPlugin().getMessageConfiguration().get("room-not-found"));
            return;
        }
        room.setExit(location);
        ShulkerRoomConfiguration shulkerRoomConfig = Main.getPlugin().getShulkerRoomConfiguration();
        shulkerRoomConfig.setExitLocation(roomId, location);
        ChatHelper.sendMessage(player, Main.getPlugin().getMessageConfiguration().get("room-exit-created").replace("{ID}", roomId));
    }
    public ShulkerRoom findRoomById(String roomId){
        for(ShulkerRoom room : this.rooms){
            if(room.getId().equals(roomId)) return room;
        }
        return null;
    }
    public ShulkerRoom findRoomByUniqueId(UUID uniqueId){
        return this.occupiedRooms.get(uniqueId);
    }
    public void claimRoom(Player player){
        if(this.rooms.isEmpty()){
            ChatHelper.sendMessage(player, Main.getPlugin().getMessageConfiguration().get("empty-rooms"));
            return;
        }
        ShulkerRoom shulkerRoom = this.rooms.remove(0);
        shulkerRoom.setOccupiedBy(player.getName());
        this.occupiedRooms.put(player.getUniqueId(), shulkerRoom);
        player.teleport(shulkerRoom.getLocation());
        ChatHelper.sendMessage(player, Main.getPlugin().getMessageConfiguration().get("join-room"));
        player.sendTitle(
                ChatHelper.fixColors(Main.getPlugin().getMessageConfiguration().get("join-room-title")),
                ChatHelper.fixColors(Main.getPlugin().getMessageConfiguration().get("join-room-subtitle"))
        );
    }
    public void claimRoom(Player player, ShulkerRoom shulkerRoom){
        if(this.rooms.isEmpty()){
            ChatHelper.sendMessage(player, Main.getPlugin().getMessageConfiguration().get("empty-rooms"));
            return;
        }
        this.rooms.remove(shulkerRoom);
        shulkerRoom.setOccupiedBy(player.getName());
        this.occupiedRooms.put(player.getUniqueId(), shulkerRoom);
        player.teleport(shulkerRoom.getLocation());
        ChatHelper.sendMessage(player, Main.getPlugin().getMessageConfiguration().get("join-room"));
        player.sendTitle(
                ChatHelper.fixColors(Main.getPlugin().getMessageConfiguration().get("join-room-title")),
                ChatHelper.fixColors(Main.getPlugin().getMessageConfiguration().get("join-room-subtitle"))
        );
    }
    public void unclaimRoom(Player player){
        ShulkerRoom shulkerRoom = this.occupiedRooms.remove(player.getUniqueId());
        shulkerRoom.setOccupiedBy(null);
        this.rooms.add(shulkerRoom);
        PluginConfiguration configuration = Main.getPlugin().getPluginConfiguration();
        for(Block block : shulkerRoom.getPlacedBlocks()){
            block.setType(Material.AIR);
        }
        player.teleport(configuration.getSpawnLocation());
        ChatHelper.sendMessage(player, Main.getPlugin().getMessageConfiguration().get("leave-room"));
        player.sendTitle(
                ChatHelper.fixColors(Main.getPlugin().getMessageConfiguration().get("leave-room-title")),
                ChatHelper.fixColors(Main.getPlugin().getMessageConfiguration().get("leave-room-subtitle"))
        );
    }
}
