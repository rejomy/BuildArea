package me.rejomy.buildarea.listener;

import me.rejomy.buildarea.BuildArea;
import me.rejomy.buildarea.manager.LocationManager;
import me.rejomy.buildarea.manager.UserManager;
import me.rejomy.buildarea.util.BlockData;
import me.rejomy.buildarea.util.PEUtil;
import me.rejomy.buildarea.util.StringUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;

import java.util.List;

public class BlockListener implements Listener {

    private final LocationManager locationManager = BuildArea.getInstance().getLocationManager();
    private final UserManager userManager = BuildArea.getInstance().getUserManager();

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        Location blockLocation = block.getLocation();

        if (!locationManager.isArenaPosition(blockLocation) || userManager.isWhiteListed(player))
            return;

        if (locationManager.getPlacedBlocks().contains(blockLocation)) {
            if (locationManager.getOperations().containsKey(blockLocation)) {
                BlockData BlockData = locationManager.getOperations().get(blockLocation);
                BlockData.setLiveCycle(5);
                BlockData BlockData2 = locationManager.getOperations().get(blockLocation);
                int n = BlockData2.getBreakIndex();
                List<Player> list = player.getWorld().getPlayers();
                PEUtil.sendDestroyParticle(n, blockLocation, (byte) -1, list);
            }
            locationManager.getPlacedBlocks().remove(blockLocation);
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        Location place = block.getLocation();

        if (!locationManager.isArenaPosition(place) || userManager.isWhiteListed(player))
            return;

        if (!event.getBlock().getType().isSolid()) {
            String[] standTypes = {"STAND"};
            if (StringUtil.contains(event.getBlock().getType().name(), standTypes)) {
                event.getBlock().setType(Material.DIRT);
            }
        } else if (event.getBlock().getType() == Material.TNT) {
            TNTPrimed tnt = event.getBlockPlaced().getLocation().getWorld().spawn(event.getBlockPlaced().getLocation(), TNTPrimed.class);
            tnt.setFireTicks(40);
            event.getBlockPlaced().getLocation().getBlock().setType(Material.AIR);
        }

        locationManager.handle(event.getBlockReplacedState());
    }

    @EventHandler
    public void onEntityFall(EntityChangeBlockEvent event) {
        Location location = event.getEntity().getLocation();
        if (locationManager.isArenaPosition(location) && event.getEntityType() == EntityType.FALLING_BLOCK && event.getTo() == Material.AIR) {
            event.setCancelled(true);
            event.getBlock().getState().update(false, false);
        }
    }

    @EventHandler
    public void onBlockFade(BlockFadeEvent event) {
        Block block = event.getBlock();
        Location location = block.getLocation();

        if (!locationManager.isArenaPosition(location))
            return;

        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockPhysics(BlockPhysicsEvent event) {
        Block block = event.getBlock();
        Location place = block.getLocation();
        String lastBlockName = event.getBlock().getType().name().toLowerCase();

        if (!locationManager.isArenaPosition(place))
            return;

        if (block.getType() == Material.CACTUS) {
            event.setCancelled(true);
        } else if (lastBlockName.contains("water") || lastBlockName.contains("lava")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onGrowth(BlockFadeEvent event) {
        Block block = event.getBlock();
        Location location = block.getLocation();

        if (locationManager.isArenaPosition(location))
            event.setCancelled(true);
    }
}
