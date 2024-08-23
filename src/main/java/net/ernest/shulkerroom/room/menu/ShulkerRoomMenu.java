package net.ernest.shulkerroom.room.menu;

import net.ernest.shulkerroom.Main;
import net.ernest.shulkerroom.configuration.PluginConfiguration;
import net.ernest.shulkerroom.helper.ChatHelper;
import net.ernest.shulkerroom.room.ShulkerRoomFactory;
import net.ernest.shulkerroom.room.menu.item.BackItem;
import net.ernest.shulkerroom.room.menu.item.ForwardItem;
import net.ernest.shulkerroom.room.menu.item.FreeRoomItem;
import net.ernest.shulkerroom.room.menu.item.OccupiedRoomItem;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.gui.PagedGui;
import xyz.xenondevs.invui.gui.structure.Markers;
import xyz.xenondevs.invui.item.Item;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.SimpleItem;
import xyz.xenondevs.invui.window.Window;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class ShulkerRoomMenu {
    private final Main plugin;
    private Gui gui;
    public ShulkerRoomMenu(Main plugin){
        this.plugin = plugin;
        FileConfiguration menuConfig = this.plugin.getMenuConfiguration().getConfig();
        ShulkerRoomFactory shulkerRoomFactory = Main.getPlugin().getShulkerRoomFactory();
        List<Item> freeRooms = shulkerRoomFactory.getRooms().stream()
                .map(room -> new FreeRoomItem(shulkerRoomFactory, room, menuConfig))
                .collect(Collectors.toList());
        List<Item> occupiedRooms = shulkerRoomFactory.getOccupiedRooms().values().stream()
                .map(room -> new OccupiedRoomItem(room, menuConfig))
                .collect(Collectors.toList());
        List<Item> allRooms = new ArrayList<>(freeRooms);
        allRooms.addAll(occupiedRooms);
        PagedGui.Builder guiCreator = PagedGui.items()
                .setStructure(menuConfig.getStringList("menu.structure").toArray(new String[0]))
                .addIngredient(menuConfig.getString("menu.content-ingredient").charAt(0), Markers.CONTENT_LIST_SLOT_HORIZONTAL)
                .addIngredient(menuConfig.getString("menu.page-item.back.ingredient").charAt(0), new BackItem(menuConfig))
                .addIngredient(menuConfig.getString("menu.page-item.forward.ingredient").charAt(0), new ForwardItem(menuConfig));
        for(String ingredient : menuConfig.getConfigurationSection("menu.ingredients").getKeys(false)){
            String displayName = menuConfig.getString("menu.ingredients." + ingredient + ".name");
            List<String> lores = menuConfig.getStringList("menu.ingredients." + ingredient + ".lores");
            ItemBuilder itemBuilder = new ItemBuilder(Material.valueOf(menuConfig.getString("menu.ingredients." + ingredient + ".material")));
            if(displayName != null) itemBuilder.setDisplayName(ChatHelper.fixColors(displayName));
            if(lores != null) itemBuilder.setLegacyLore(ChatHelper.fixColors(lores));
            guiCreator.addIngredient(ingredient.charAt(0),
                    new SimpleItem(itemBuilder));
        }
        gui = guiCreator
                .setContent(allRooms)
                .build();
    }
    public void openInventory(Player player){
        FileConfiguration menuConfig = this.plugin.getMenuConfiguration().getConfig();
        Window window = Window.single()
                .setViewer(player)
                .setGui(gui)
                .setTitle(menuConfig.getString("menu.title"))
                .build();
        window.open();
    }
}
