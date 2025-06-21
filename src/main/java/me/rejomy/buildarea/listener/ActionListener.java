package me.rejomy.buildarea.listener;

import me.rejomy.buildarea.BuildArea;
import me.rejomy.buildarea.manager.LocationManager;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.SpongeAbsorbEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

/**
 * Bunch of small events here.
 */
public class ActionListener implements Listener {

    private final LocationManager locationManager = BuildArea.getInstance().getLocationManager();

    @EventHandler
    public void onSpongeAbsorb(SpongeAbsorbEvent event) {
        Block block = event.getBlock();
        Location location = block.getLocation();
        event.setCancelled(locationManager.isArenaPosition(location));
    }

    @EventHandler
    public void onPistonExtend(BlockPistonExtendEvent event) {
        if (locationManager.getPlacedBlocks().contains(event.getBlock().getLocation()))
            event.setCancelled(true);
    }

    @EventHandler
    public void onExplode(EntityExplodeEvent event) {
        Location place = event.getLocation();

        if (locationManager.isArenaPosition(place))
            event.blockList().removeIf(block ->
                    !locationManager.getPlacedBlocks().contains(block.getLocation()));
    }
}
