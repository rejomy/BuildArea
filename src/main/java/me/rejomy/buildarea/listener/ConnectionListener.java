package me.rejomy.buildarea.listener;

import me.rejomy.buildarea.BuildArea;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class ConnectionListener implements Listener {

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        // Remove player if exists for prevent memory leaks.
        BuildArea.getInstance().getUserManager().getWhitePlayerList().remove(player);
    }
}
