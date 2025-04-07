package me.rejomy.buildarea;

import lombok.Getter;
import me.rejomy.buildarea.command.BuildAreaCommand;
import me.rejomy.buildarea.config.Config;
import me.rejomy.buildarea.listener.ActionListener;
import me.rejomy.buildarea.listener.BlockListener;
import me.rejomy.buildarea.listener.ConnectionListener;
import me.rejomy.buildarea.listener.InteractListener;
import me.rejomy.buildarea.manager.LocationManager;
import me.rejomy.buildarea.manager.UserManager;
import me.rejomy.buildarea.util.PEUtil;
import me.rejomy.buildarea.util.WGUtil;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class BuildArea extends JavaPlugin {

    @Getter
    private static BuildArea instance;

    private LocationManager locationManager;
    private UserManager userManager;
    private Config customConfig;

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();
        customConfig = new Config();
        customConfig.load(getConfig());

        getCommand("buildarea").setExecutor(new BuildAreaCommand());

        WGUtil.init();
        PEUtil.init();

        initManagers();
        initListeners();
    }

    private void initListeners() {
        registerListener(new ConnectionListener());
        registerListener(new BlockListener());
        registerListener(new ActionListener());
        registerListener(new InteractListener());
    }

    private void initManagers() {
        locationManager = new LocationManager();
        userManager = new UserManager();
    }

    private void registerListener(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, this);
    }
}
