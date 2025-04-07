package me.rejomy.buildarea.manager;

import lombok.Getter;
import me.rejomy.buildarea.BuildArea;
import me.rejomy.buildarea.task.PlaceResetTask;
import me.rejomy.buildarea.util.BlockData;
import me.rejomy.buildarea.util.DoubleBlockUtil;
import me.rejomy.buildarea.util.WGUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.BlockState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

@Getter
public class LocationManager {

    private final HashMap<Location, BlockData> operations = new HashMap<>();
    private final ArrayList<Location> placedBlocks = new ArrayList<>();

    public void handle(BlockState originBlockState) {
        if (originBlockState == null) {
            throw new IllegalArgumentException("originBlockState cannot be null");
        }

        Location location = originBlockState.getLocation();
        Object[] objectArray = new Object[]{
                originBlockState.getType().getId(),
                location.getX(),
                location.getY(),
                location.getZ()
        };

        int breakIndex = Objects.hash(objectArray);
        BlockData actionData = new BlockData(originBlockState, breakIndex);
        PlaceResetTask placeResetTask = new PlaceResetTask(actionData);
        BlockState doubleBlockBottom = DoubleBlockUtil.getDoubleBlockBottom(originBlockState);

        if (!placedBlocks.contains(location)) {
            placedBlocks.add(location);
        }

        if (operations.containsKey(location)) {
            BlockData arenaActionData = operations.get(location);
            if (arenaActionData != null) {
                arenaActionData.setLiveCycle(0);
            }

            return;
        }

        if (doubleBlockBottom != null) {
            actionData.setBottomPart(doubleBlockBottom.getRawData());
        }

        int taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(
                BuildArea.getInstance(),
                placeResetTask,
                20L,
                20L
        );
        placeResetTask.setScheduleTaskId(taskId);
        operations.put(location, actionData);
    }

    public boolean isArenaPosition(Location place) {
        if (place == null || place.getWorld() == null) {
            return false;
        }

        return isValidWorld(place) || WGUtil.isLocationInValidRegion(place);
    }

    private boolean isValidWorld(Location location) {
        return BuildArea.getInstance().getCustomConfig().getWorldNames().contains(location.getWorld().getName());
    }
}