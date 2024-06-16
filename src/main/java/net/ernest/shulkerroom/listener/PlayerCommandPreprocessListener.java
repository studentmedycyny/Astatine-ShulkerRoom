package net.ernest.shulkerroom.listener;

import net.ernest.shulkerroom.Main;
import net.ernest.shulkerroom.helper.ChatHelper;
import net.ernest.shulkerroom.room.ShulkerRoomFactory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;


public class PlayerCommandPreprocessListener implements Listener {
    private final Main plugin;
    public PlayerCommandPreprocessListener(Main plugin){
        this.plugin = plugin;
    }
    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        ShulkerRoomFactory shulkerRoomFactory = this.plugin.getShulkerRoomFactory();
        if(!shulkerRoomFactory.isPlayerInRoom(player)) return;
        String[] message = event.getMessage().split(" ");
        for (String otherstring : this.plugin.getPluginConfiguration().getConfig().getStringList("configuration.allowed-commands")) {
            if (otherstring.equalsIgnoreCase(message[0])) {
                return;
            }
        }
        if (!player.isOp()) {
            player.sendMessage(ChatHelper.fixColors(Main.getPlugin().getMessageConfiguration().get("command-not-allowed")));
            event.setCancelled(true);
            return;
        }
    }
}
