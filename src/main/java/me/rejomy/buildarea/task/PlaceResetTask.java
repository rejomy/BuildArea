package me.rejomy.buildarea.task;

import lombok.Getter;
import lombok.Setter;
import me.rejomy.buildarea.BuildArea;
import me.rejomy.buildarea.manager.LocationManager;
import me.rejomy.buildarea.util.BlockData;
import me.rejomy.buildarea.util.DoubleBlockUtil;
import me.rejomy.buildarea.util.PEUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.List;

@Getter
@Setter
public final class PlaceResetTask implements Runnable {

    private final BlockData blockData;
    private BukkitTask scheduleTask;
    private final LocationManager locationManager = BuildArea.getInstance().getLocationManager();

    public PlaceResetTask(BlockData blockData) {
        this.blockData = blockData;
    }

    public void run() {
        BlockState blockState = this.blockData.getOriginBlockState();
        Location location = blockState.getLocation();
        int liveCycle = blockData.getLiveCycle();
        blockData.setLiveCycle(liveCycle + 1);

        if (liveCycle < BuildArea.getInstance().getCustomConfig().getBlockDestroyTicks()) {
            if (PEUtil.isPresent() && locationManager.getPlacedBlocks().contains(location)) {
                int n2 = this.blockData.getBreakIndex();
                byte by = (byte)(this.blockData.getLiveCycle() * 2);
                List<Player> list = blockState.getWorld().getPlayers();
                PEUtil.sendDestroyParticle(n2, location, by, list);
            }

            return;
        }

        if (PEUtil.isPresent()) {
            List<Player> recipients = location.getWorld().getPlayers();
            PEUtil.sendDestroyParticle(blockData.getBreakIndex(), location, (byte)-1, recipients);
        }

        locationManager.getPlacedBlocks().remove(location);
        location.getBlock().setType(Material.AIR);
        if (DoubleBlockUtil.handleDoubleBlock(this.blockData)) {
            location.getBlock().setType(blockState.getType());
            BlockState state = location.getBlock().getState();
            state.setData(blockState.getData());
            state.update();
            locationManager.getOperations().remove(location);
            scheduleTask.cancel();
        }
    }
}

