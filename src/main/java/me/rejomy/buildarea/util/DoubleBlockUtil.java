package me.rejomy.buildarea.util;

import lombok.experimental.UtilityClass;
import me.rejomy.buildarea.BuildArea;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.BlockData;

@UtilityClass
public final class DoubleBlockUtil {

    public boolean handleDoubleBlock(me.rejomy.buildarea.util.BlockData blockData) {
        BlockState originBlockState = blockData.getOriginBlockState();
        Location originLocation = originBlockState.getLocation();

        // Skip if the block is a full double block already
        if (blockData.getBottomPart() != 1) {

            BlockData blockDataFromState = originBlockState.getBlockData();

            // Ensure this block is bisected (top/bottom blocks like doors, tall flowers)
            if (!(blockDataFromState instanceof Bisected)) {
                return true;
            }

            Bisected bisected = (Bisected) blockDataFromState;
            Bisected.Half currentHalf = bisected.getHalf();

            Location otherBlockLocation = currentHalf == Bisected.Half.TOP
                    ? originLocation.clone().subtract(0, 1, 0)
                    : originLocation.clone().add(0, 1, 0);

            if (BuildArea.getInstance().getLocationManager().getOperations().containsKey(otherBlockLocation)) {
                return false;
            }

            Block otherBlock = otherBlockLocation.getBlock();
            otherBlock.setType(originBlockState.getType());

            // Set the correct half (TOP or BOTTOM) to the other block
            BlockState otherState = otherBlock.getState();
            BlockData newBlockData = otherBlock.getBlockData();

            if (newBlockData instanceof Bisected) {
                Bisected otherBisected = (Bisected) newBlockData;
                otherBisected.setHalf(currentHalf == Bisected.Half.TOP
                        ? Bisected.Half.BOTTOM
                        : Bisected.Half.TOP);
                otherState.setBlockData(otherBisected);
                otherState.update();
            }

            // Remove original block from placed list
            BuildArea.getInstance().getLocationManager().getPlacedBlocks().remove(originLocation);
        }

        return true;
    }

    public boolean isDoubleBlock(Location location) {
        BlockState state = location.getBlock().getState();
        return getDoubleBlockBottom(state) != null;
    }

    public BlockState getDoubleBlockBottom(BlockState blockState) {
        Block block = blockState.getBlock();
        BlockData data = blockState.getBlockData();

        if (!(data instanceof Bisected)) {
            return null;
        }

        Bisected bisected = (Bisected) data;
        boolean isTop = bisected.getHalf() == Bisected.Half.TOP;

        Block otherBlock = isTop
                ? block.getRelative(BlockFace.DOWN)
                : block.getRelative(BlockFace.UP);

        BlockState otherState = otherBlock.getState();
        BlockData otherData = otherState.getBlockData();

        if (!(otherData instanceof Bisected)) {
            return null;
        }

        Bisected otherBisected = (Bisected) otherData;
        boolean isOtherTop = otherBisected.getHalf() == Bisected.Half.TOP;

        boolean matches = isTop != isOtherTop
                && otherBlock.getType() == blockState.getType();

        return matches ? (!isTop ? blockState : otherState) : null;
    }
}
