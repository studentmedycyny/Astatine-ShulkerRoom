package net.ernest.shulkerroom.listener;

import net.ernest.shulkerroom.Main;
import net.ernest.shulkerroom.room.ShulkerRoom;
import net.ernest.shulkerroom.room.ShulkerRoomFactory;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlaceBreakListener implements Listener {
    private final Main plugin;
    public BlockPlaceBreakListener(Main plugin){
        this.plugin = plugin;
    }
    @EventHandler
    public void onPlace(BlockPlaceEvent event){
        Player player = event.getPlayer();
        ShulkerRoomFactory shulkerRoomFactory = this.plugin.getShulkerRoomFactory();
        if(shulkerRoomFactory.isPlayerInRoom(player)){
            if(event.getBlockAgainst().getType() != Material.BEDROCK){
                event.setCancelled(true);
                return;
            }
            ShulkerRoom room =  shulkerRoomFactory.findRoomByUniqueId(player.getUniqueId());
            Block block = event.getBlock();
            room.addPlacedBlocks(block);
        }
    }
    @EventHandler
    public void onBreak(BlockBreakEvent event){
        Player player = event.getPlayer();
        ShulkerRoomFactory shulkerRoomFactory = this.plugin.getShulkerRoomFactory();
        if(shulkerRoomFactory.isPlayerInRoom(player)){
            ShulkerRoom room =  shulkerRoomFactory.findRoomByUniqueId(player.getUniqueId());
            Block block = event.getBlock();
            if(!room.getPlacedBlocks().contains(block)){
                event.setCancelled(true);
                return;
            }
            room.removePlacedBlocks(block);
        }
    }
}
