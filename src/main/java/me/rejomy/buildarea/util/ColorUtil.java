package me.rejomy.buildarea.util;

import lombok.experimental.UtilityClass;
import org.bukkit.ChatColor;

@UtilityClass
public class ColorUtil {

    public String colorize(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }
}
