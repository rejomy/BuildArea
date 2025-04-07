package me.rejomy.buildarea.util;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerBlockBreakAnimation;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;

@Getter
@UtilityClass
public class PEUtil {

    @Getter
    private boolean present;

    public void init() {
        try {
            PacketEvents.getAPI();
            present = true;
        } catch (NoClassDefFoundError e) {
            present = false;
        }
    }

    public void sendDestroyParticle(int entityId, Location place, byte destroyValue, List<? extends Player> recipients) {
        if (!PEUtil.isPresent())
            return;

        WrapperPlayServerBlockBreakAnimation packet = new WrapperPlayServerBlockBreakAnimation(entityId,
                new Vector3i(place.getBlockX(), place.getBlockY(), place.getBlockZ()), destroyValue);

        for (Player player : recipients) {
            PacketEvents.getAPI().getPlayerManager().sendPacket(player, packet);
        }
    }
}
