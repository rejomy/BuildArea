package me.rejomy.buildarea.config;

import lombok.Getter;
import me.rejomy.buildarea.util.ColorUtil;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

@Getter
public class Config {

    private String denyMessage;
    private int blockDestroyTicks;
    private List<String> regions;
    private List<String> worldNames;

    public void load(FileConfiguration config) {
        worldNames = config.getStringList("worlds");
        regions = config.getStringList("regions");
        denyMessage = ColorUtil.colorize(config.getString("deny-message"));
        blockDestroyTicks = config.getInt("block-destroy-ticks");
    }
}
