package net.ernest.shulkerroom.command;

import net.ernest.shulkerroom.Main;
import net.ernest.shulkerroom.configuration.MessageConfiguration;
import net.ernest.shulkerroom.helper.ChatHelper;
import net.ernest.shulkerroom.room.ShulkerRoom;
import net.ernest.shulkerroom.room.ShulkerRoomFactory;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class ShulkerRoomCommand implements CommandExecutor {
    private final Main plugin;
    public ShulkerRoomCommand(Main plugin){
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments) {
        MessageConfiguration messageConfiguration = this.plugin.getMessageConfiguration();
        if(sender instanceof Player player){
            ShulkerRoomFactory shulkerRoomFactory = this.plugin.getShulkerRoomFactory();
            if(arguments.length == 1){
                if(arguments[0].equalsIgnoreCase("setspawn")){
                    this.plugin.getPluginConfiguration().setSpawnLocation(player.getLocation());
                    player.sendMessage(ChatHelper.fixColors(Main.getPlugin().getMessageConfiguration().get("spawn-set")));
                    return true;
                }
                if(arguments[0].equalsIgnoreCase("info")){
                    if(player.hasPermission("astatine.shulkerroom")) {
                        for(String line : messageConfiguration.getList("command-usage")){
                            player.sendMessage(ChatHelper.fixColors(line));
                        }
                        return true;
                    }
                }
                if(arguments[0].equalsIgnoreCase("reload")){
                    this.plugin.getPluginConfiguration().load();
                    this.plugin.getShulkerRoomConfiguration().load();
                    this.plugin.getMessageConfiguration().load();
                    player.sendMessage(ChatHelper.fixColors(Main.getPlugin().getMessageConfiguration().get("reload-config")));
                    return true;
                }
                if(arguments[0].equalsIgnoreCase("list")){
                    List<String> freeRooms = shulkerRoomFactory.getRooms().stream().map(ShulkerRoom::getId).toList();
                    List<String> occupiedRooms = shulkerRoomFactory.getOccupiedRooms().values().stream().map(ShulkerRoom::getId).toList();
                    player.sendMessage(
                            ChatHelper.fixColors(
                                    Main.getPlugin().getMessageConfiguration().get("free-rooms-list") + StringUtils.join(freeRooms, ", ")
                            )
                    );
                    player.sendMessage(
                            ChatHelper.fixColors(
                                    Main.getPlugin().getMessageConfiguration().get("occupied-rooms-list") + StringUtils.join(occupiedRooms, ", ")
                            )
                    );
                    return true;
                }
            }
            if(arguments.length == 2){
                if(arguments[0].equalsIgnoreCase("create")){
                    String room = arguments[1];
                    shulkerRoomFactory.createRoom(player, room, player.getLocation());
                    return true;
                }
                if(arguments[0].equalsIgnoreCase("remove")){
                    String room = arguments[1];
                    shulkerRoomFactory.removeRoom(player, room);
                    return true;
                }
                if(arguments[0].equalsIgnoreCase("exit")){
                    String room = arguments[1];
                    shulkerRoomFactory.setExitLocation(player, room);
                    return true;
                }
            }
            if(shulkerRoomFactory.isPlayerInRoom(player)){
                shulkerRoomFactory.unclaimRoom(player);
                return true;
            }
            shulkerRoomFactory.claimRoom(player);
        }else{
            sender.sendMessage(ChatHelper.fixColors(messageConfiguration.get("console-cannot-send")));
        }
        return false;
    }
}
