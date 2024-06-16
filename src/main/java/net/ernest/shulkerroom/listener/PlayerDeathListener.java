package net.ernest.shulkerroom.listener;

import net.ernest.shulkerroom.Main;
import net.ernest.shulkerroom.room.ShulkerRoomFactory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerDeathListener implements Listener {
    private final Main plugin;
    public PlayerDeathListener(Main plugin){
        this.plugin = plugin;
    }
    @EventHandler
    public void onDeath(PlayerDeathEvent event){
        Player player = event.getEntity();
        ShulkerRoomFactory shulkerRoomFactory = this.plugin.getShulkerRoomFactory();
        if(shulkerRoomFactory.isPlayerInRoom(player)){
            shulkerRoomFactory.unclaimRoom(player);
        }
    }
}
