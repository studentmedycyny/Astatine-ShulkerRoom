package net.ernest.shulkerroom.room.menu.item;

import net.ernest.shulkerroom.helper.ChatHelper;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import xyz.xenondevs.invui.gui.PagedGui;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.controlitem.PageItem;

import java.util.stream.Collectors;

public class ForwardItem extends PageItem {
    private final FileConfiguration menuConfig;

    public ForwardItem(FileConfiguration menuConfig) {
        super(true);
        this.menuConfig = menuConfig;
    }

    @Override
    public ItemProvider getItemProvider(PagedGui<?> gui) {
        ItemBuilder builder;
        if (!gui.hasNextPage()) {
            builder = new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE);
        } else {
            builder = new ItemBuilder(Material.valueOf(menuConfig.getString("menu.page-item.forward.material")))
                    .setDisplayName(ChatHelper.fixColors(menuConfig.getString("menu.page-item.forward.name")))
                    .addLegacyLoreLines(ChatHelper.fixColors(menuConfig.getStringList("menu.page-item.forward.lores"))
                            .stream()
                            .map(string -> string
                                    .replace("{NEXT_PAGE}", String.valueOf((gui.getCurrentPage() + 2)))
                                    .replace("{PAGES}", String.valueOf(gui.getPageAmount()))
                            )
                            .collect(Collectors.toList()));
        }
        return builder;
    }

}