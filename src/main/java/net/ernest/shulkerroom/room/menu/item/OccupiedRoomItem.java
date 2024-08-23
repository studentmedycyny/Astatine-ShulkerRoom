package net.ernest.shulkerroom.room.menu.item;

import net.ernest.shulkerroom.helper.ChatHelper;
import net.ernest.shulkerroom.room.ShulkerRoom;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.AbstractItem;

public class OccupiedRoomItem extends AbstractItem {
    private final ShulkerRoom shulkerRoom;
    private final FileConfiguration menuConfig;
    public OccupiedRoomItem(ShulkerRoom shulkerRoom, FileConfiguration menuConfig){
        this.shulkerRoom = shulkerRoom;
        this.menuConfig = menuConfig;

    }
    @Override
    public ItemProvider getItemProvider() {
        return new ItemBuilder(Material.valueOf(menuConfig.getString("menu.occupied-room-item.material")))
                .setDisplayName(ChatHelper.fixColors(menuConfig.getString("menu.occupied-room-item.name").replace("{ROOM_ID}", shulkerRoom.getId())))
                .addLegacyLoreLines(ChatHelper.fixColors(menuConfig.getStringList("menu.occupied-room-item.lores")));
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent inventoryClickEvent) {

    }
}
