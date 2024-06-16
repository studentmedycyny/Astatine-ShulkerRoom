package net.ernest.shulkerroom.listener;

import net.ernest.shulkerroom.Main;
import net.ernest.shulkerroom.room.ShulkerRoom;
import net.ernest.shulkerroom.room.ShulkerRoomFactory;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteractListener implements Listener {
    private final Main plugin;
    public PlayerInteractListener(Main plugin){
        this.plugin = plugin;
    }
    @EventHandler
    public void onInteract(PlayerInteractEvent event){
        Player player = event.getPlayer();
        if(event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        Block block = event.getClickedBlock();
        if(block == null) return;
        ShulkerRoomFactory shulkerRoomFactory = this.plugin.getShulkerRoomFactory();
        if(shulkerRoomFactory.isPlayerInRoom(player)){
            ShulkerRoom room =  shulkerRoomFactory.findRoomByUniqueId(player.getUniqueId());
            Location roomExit = room.getExit();
            Location blockLocation = block.getLocation();
            if(roomExit.equals(blockLocation)) {
                shulkerRoomFactory.unclaimRoom(player);
            }
        }
    }
}
