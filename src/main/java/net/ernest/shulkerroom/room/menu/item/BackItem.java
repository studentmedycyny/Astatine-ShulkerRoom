package net.ernest.shulkerroom.room.menu.item;

import net.ernest.shulkerroom.configuration.MenuConfiguration;
import net.ernest.shulkerroom.helper.ChatHelper;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import xyz.xenondevs.invui.gui.PagedGui;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.controlitem.PageItem;

import java.util.stream.Collectors;

public class BackItem extends PageItem {
    private final FileConfiguration menuConfig;

    public BackItem(FileConfiguration menuConfig) {
        super(false);
        this.menuConfig = menuConfig;
    }

    @Override
    public ItemProvider getItemProvider(PagedGui<?> gui) {
        ItemBuilder builder;
        if (!gui.hasPreviousPage()) {
            builder = new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE);
        } else {
            builder = new ItemBuilder(Material.valueOf(menuConfig.getString("menu.page-item.back.material")))
                    .setDisplayName(ChatHelper.fixColors(menuConfig.getString("menu.page-item.back.name")))
                    .addLegacyLoreLines(ChatHelper.fixColors(menuConfig.getStringList("menu.page-item.back.lores"))
                            .stream()
                            .map(string -> string
                                    .replace("{PREVIOUS_PAGE}", String.valueOf(gui.getCurrentPage()))
                                    .replace("{PAGES}", String.valueOf(gui.getPageAmount()))
                            )
                            .collect(Collectors.toList()));
        }
        return builder;
    }
}