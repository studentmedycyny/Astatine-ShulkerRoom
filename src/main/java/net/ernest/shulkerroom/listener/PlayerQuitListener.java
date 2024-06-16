package net.ernest.shulkerroom.listener;

import net.ernest.shulkerroom.Main;
import net.ernest.shulkerroom.room.ShulkerRoom;
import net.ernest.shulkerroom.room.ShulkerRoomFactory;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {
    private final Main plugin;
    public PlayerQuitListener(Main plugin){
        this.plugin = plugin;
    }
    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        Player player = event.getPlayer();
        ShulkerRoomFactory shulkerRoomFactory = this.plugin.getShulkerRoomFactory();
        if(shulkerRoomFactory.isPlayerInRoom(player)){
            shulkerRoomFactory.unclaimRoom(player);
        }
    }
}
