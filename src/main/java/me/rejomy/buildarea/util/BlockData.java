package me.rejomy.buildarea.util;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.block.BlockState;

@Getter
@Setter
public class BlockData {

    private final BlockState originBlockState;
    private final int breakIndex;
    private int liveCycle;
    private final Location location;
    private int bottomPart; // ← FIXED type

    public BlockData(BlockState originBlockState, int breakIndex) {
        this.originBlockState = originBlockState;
        this.breakIndex = breakIndex;
        this.location = originBlockState.getLocation();
        this.bottomPart = 1; // default value
    }
}
