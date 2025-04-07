package me.rejomy.buildarea.manager;

import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@Getter
public class UserManager {

    private final List<Player> whitePlayerList = new ArrayList<>();
}
