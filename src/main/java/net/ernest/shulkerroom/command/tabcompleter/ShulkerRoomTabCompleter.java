package net.ernest.shulkerroom.command.tabcompleter;

import net.ernest.shulkerroom.Main;
import net.ernest.shulkerroom.command.ShulkerRoomCommand;
import net.ernest.shulkerroom.room.ShulkerRoom;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ShulkerRoomTabCompleter implements TabCompleter {
    private final Main plugin;
    public ShulkerRoomTabCompleter(Main plugin){
        this.plugin = plugin;
    }
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] arguments) {
        List<String> autoCompletes = new ArrayList<>();
        if (sender.hasPermission("astatine.shulkeroom")) {
            if (arguments.length == 1) {
                autoCompletes.add("create");
                autoCompletes.add("exit");
                autoCompletes.add("remove");
                autoCompletes.add("setspawn");
                autoCompletes.add("list");
                autoCompletes.add("reload");
                autoCompletes.add("help");
                return autoCompletes;
            }
            if (arguments.length == 2) {
                if(arguments[0].equalsIgnoreCase("create")
                        || arguments[0].equalsIgnoreCase("exit")
                        || arguments[0].equalsIgnoreCase("remove")) {
                    for (ShulkerRoom shulkerRoom : this.plugin.getShulkerRoomFactory().getRooms()) {
                        autoCompletes.add(shulkerRoom.getId());
                    }
                    return autoCompletes;
                }
            }
            return autoCompletes;
        }
        return autoCompletes;
    }
}