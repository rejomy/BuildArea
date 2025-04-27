package me.rejomy.buildarea.listener;

import me.rejomy.buildarea.BuildArea;
import me.rejomy.buildarea.manager.LocationManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.SpongeAbsorbEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;

/**
 * Collect small events here.
 */
public class ActionListener implements Listener {

    private final LocationManager locationManager = BuildArea.getInstance().getLocationManager();

    @EventHandler
    public void onBucketFill(PlayerBucketFillEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlockClicked();
        Material type = block.getType();

        if (type == Material.WATER || type == Material.LAVA) {
            sendMessage(player);
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onWaterOrLavaPlaceEvent(PlayerBucketEmptyEvent event) {
        Location location = event.getBlock().getLocation();

        if (locationManager.isArenaPosition(location)) {
            sendMessage(event.getPlayer());
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onSpongeAbsorb(SpongeAbsorbEvent event) {
        Block block = event.getBlock();
        Location location = block.getLocation();

        if (!locationManager.isArenaPosition(location)) {
            return;
        }

        event.setCancelled(true);
    }

    @EventHandler
    public void onPistonExtend(BlockPistonExtendEvent event) {
        if (locationManager.getPlacedBlocks().contains(event.getBlock().getLocation())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onExplode(EntityExplodeEvent event) {
        Location place = event.getLocation();

        if (!locationManager.isArenaPosition(place)) {
            return;
        }

        event.blockList().removeIf(block -> !locationManager.getPlacedBlocks().contains(block.getLocation()));
    }

    private void sendMessage(Player player) {
        player.sendMessage(BuildArea.getInstance().getCustomConfig().getDenyMessage());
    }
}
