package me.rejomy.buildarea.util;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerBlockBreakAnimation;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import me.rejomy.buildarea.BuildArea;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Set;

@UtilityClass
public class WGUtil {

    @Getter
    private boolean present;

    public void init() {
        try {
            WorldGuardPlugin.inst();
            present = true;
        } catch (NoClassDefFoundError e) {
            present = false;
        }
    }

    public boolean isLocationInValidRegion(Location location) {
        if (!present)
            return false;

        // Get the WorldGuard plugin instance
        WorldGuardPlugin wg = WorldGuardPlugin.inst();

        // Get the region manager for the world the location is in
        RegionManager regionManager = wg.getRegionManager(location.getWorld());

        if (regionManager == null) {
            return false;
        }

        // Iterate through the list of region IDs
        for (String regionName : BuildArea.getInstance().getCustomConfig().getRegions()) {
            // Get the specific region by ID
            ProtectedRegion region = regionManager.getRegion(regionName);
            if (region != null && region.contains(location.getBlockX(), location.getBlockY(), location.getBlockZ())) {
                // The location is inside the region
                return true;
            }
        }
        // Location is not inside any of the regions
        return false;
    }
}
