package me.rejomy.buildarea.util;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import me.rejomy.buildarea.BuildArea;
import org.bukkit.Location;

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
        RegionContainer container = WorldGuard.getInstance()
                .getPlatform()
                .getRegionContainer();

        RegionManager regionManager = container.get(BukkitAdapter.adapt(location.getWorld()));

        if (regionManager == null) {
            return false;
        }

        RegionQuery query = container.createQuery();

        ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(location));

        for (String regionId : BuildArea.getInstance()
                .getCustomConfig()
                .getRegions()) {

            if (set.getRegions().stream()
                    .anyMatch(r -> r.getId().equalsIgnoreCase(regionId))) {
                return true;
            }
        }

        return false;
    }
}
