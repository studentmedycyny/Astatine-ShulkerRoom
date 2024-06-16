package net.ernest.shulkerroom.room;

import lombok.Data;
import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;

public @Data class ShulkerRoom {
    private String id;
    private String occupiedBy;
    private Location location;
    private Location exit;
    private List<Block> placedBlocks = new ArrayList<>();
    public ShulkerRoom(String id, Location location){
        this.id = id;
        this.occupiedBy = null;
        this.location = location;
        this.exit = null;
    }
    public ShulkerRoom(String id, Location location, Location exit){
        this.id = id;
        this.occupiedBy = null;
        this.location = location;
        this.exit = exit;
    }
    public void addPlacedBlocks(Block block){
        this.placedBlocks.add(block);
    }
    public void removePlacedBlocks(Block block){
        this.placedBlocks.remove(block);
    }
}
