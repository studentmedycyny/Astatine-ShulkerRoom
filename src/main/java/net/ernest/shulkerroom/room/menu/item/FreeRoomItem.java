package net.ernest.shulkerroom.room.menu.item;

import net.ernest.shulkerroom.Main;
import net.ernest.shulkerroom.helper.ChatHelper;
import net.ernest.shulkerroom.room.ShulkerRoom;
import net.ernest.shulkerroom.room.ShulkerRoomFactory;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.AbstractItem;

public class FreeRoomItem extends AbstractItem {
    private final ShulkerRoomFactory shulkerRoomFactory;
    private final ShulkerRoom shulkerRoom;
    private final FileConfiguration menuConfig;
    public FreeRoomItem(ShulkerRoomFactory shulkerRoomFactory, ShulkerRoom shulkerRoom, FileConfiguration menuConfig){
        this.shulkerRoomFactory = shulkerRoomFactory;
        this.shulkerRoom = shulkerRoom;
        this.menuConfig = menuConfig;
    }
    @Override
    public ItemProvider getItemProvider() {
        return new ItemBuilder(Material.valueOf(menuConfig.getString("menu.free-room-item.material")))
                .setDisplayName(ChatHelper.fixColors(menuConfig.getString("menu.free-room-item.name").replace("{ROOM_ID}", shulkerRoom.getId())))
                .addLegacyLoreLines(ChatHelper.fixColors(menuConfig.getStringList("menu.free-room-item.lores")));
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent inventoryClickEvent) {
        if(shulkerRoomFactory.isPlayerInRoom(player)){
            ChatHelper.sendMessage(player, Main.getPlugin().getMessageConfiguration().get("player-has-already-room"));
            return;
        }
        shulkerRoomFactory.claimRoom(player, this.shulkerRoom);
    }
}
