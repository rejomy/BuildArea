package me.rejomy.buildarea.util;

import lombok.experimental.UtilityClass;
import me.rejomy.buildarea.BuildArea;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;

import java.util.Map;

@UtilityClass
public final class DoubleBlockUtil {

    public boolean handleDoubleBlock(BlockData blockData) {
        BlockState originBlockState = blockData.getOriginBlockState();
        Location originLocation = originBlockState.getLocation();
        if (blockData.getBottomPart() != 1) {
            byte rawData = originBlockState.getRawData();
            boolean isTopPart = (rawData & 8) != 0;
            Location otherBlockLocation = isTopPart ? originLocation.clone().subtract(0.0, 1.0, 0.0) : originLocation.clone().add(0.0, 1.0, 0.0);
            if (BuildArea.getInstance().getLocationManager().getOperations().containsKey(otherBlockLocation)) {
                return false;
            }
            Block otherBlock = otherBlockLocation.getBlock();
            otherBlock.setType(originBlockState.getType());
            BlockState state = otherBlock.getState();
            state.setRawData(isTopPart ? blockData.getBottomPart() : (byte)(rawData & 7 | 8));
            state.update();
            BuildArea.getInstance().getLocationManager().getPlacedBlocks().remove(originLocation);
        }
        return true;
    }

    public final boolean isDoubleBlock(Location location) {
        BlockState blockState = location.getBlock().getState();
        return getDoubleBlockBottom(blockState) != null;
    }

    public BlockState getDoubleBlockBottom(BlockState blockState) {
        boolean isDoubleBlock;
        Block block = blockState.getBlock();
        byte rawData = blockState.getRawData();
        boolean isTop = (rawData & 8) != 0;
        Block otherBlock = isTop ? block.getRelative(BlockFace.DOWN) : block.getRelative(BlockFace.UP);
        BlockState otherBlockState = otherBlock.getState();
        boolean otherBlockIsTop = (otherBlockState.getRawData() & 8) != 0;
        boolean bl = isDoubleBlock = otherBlockIsTop != isTop && otherBlock.getType() == blockState.getType();
        return isDoubleBlock ? (!isTop ? blockState : otherBlockState) : null;
    }
}

