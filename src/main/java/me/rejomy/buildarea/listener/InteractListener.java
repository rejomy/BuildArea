package me.rejomy.buildarea.listener;

import me.rejomy.buildarea.BuildArea;
import me.rejomy.buildarea.manager.LocationManager;
import me.rejomy.buildarea.util.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class InteractListener implements Listener {

    private final LocationManager locationManager = BuildArea.getInstance().getLocationManager();

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK && event.getAction() != Action.RIGHT_CLICK_AIR) {
            return;
        }

        Location place = event.getPlayer().getLocation();

        if (!locationManager.isArenaPosition(place)) {
            return;
        }

        ItemStack item = event.getItem();
        if (item == null) {
            return;
        }

        if (item.getType() == Material.ARMOR_STAND) {
            sendMessage(event.getPlayer());
            event.setCancelled(true);
            return;
        }

        String[] restrictedMaterials = {"WATER", "LAVA", "VINE"};
        if (StringUtil.contains(item.getType().name(), restrictedMaterials)) {
            sendMessage(event.getPlayer());
            event.setCancelled(true);
            return;
        }

        if (event.getClickedBlock() != null) {
            Material clickedBlockType = event.getClickedBlock().getType();

            String[] allowedInteractables = {"TABLE", "CHEST", "ANVIL", "FURNACE", "DROPPER", "DISPENSER", "HOPPER", "DOOR", "BUTTON", "LEVER", "TRAP_DOOR", "GATE", "BENCH", "DRAGON_EGG"};
            if (StringUtil.contains(clickedBlockType.name(), allowedInteractables)) {
                return;
            }

            if (item.getType() == Material.INK_SACK && item.getDurability() == 15) {
                sendMessage(event.getPlayer());
                event.setCancelled(true);
                return;
            }

            String[] restrictedTools = {"PAIN", "HOE", "SHOVEL", "BED"};
            if (StringUtil.contains(item.getType().name(), restrictedTools)) {
                sendMessage(event.getPlayer());
                event.setCancelled(true);
                return;
            }
        }

        if (!item.getType().isSolid() && item.getType() == Material.BUCKET) {
            sendMessage(event.getPlayer());
            event.setCancelled(true);
        }
    }

    private void sendMessage(Player player) {
        player.sendMessage(BuildArea.getInstance().getCustomConfig().getDenyMessage());
    }
}
