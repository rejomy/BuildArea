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
    private byte bottomPart;

    public BlockData(BlockState originBlockState, int breakIndex) {
        this.originBlockState = originBlockState;
        this.breakIndex = breakIndex;
        Location location = this.originBlockState.getLocation();
        this.location = location;
        this.bottomPart = 1;
    }
}
