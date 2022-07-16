package pers.ninthseason.morerespawnpoint;

import org.bukkit.plugin.java.JavaPlugin;
import pers.ninthseason.morerespawnpoint.command.RespawnPoint;
import pers.ninthseason.morerespawnpoint.listener.RespawnEventExecutor;

public final class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();

        this.getCommand("RespawnPoint").setExecutor(new RespawnPoint(this));
        getServer().getPluginManager().registerEvents(new RespawnEventExecutor(this), this);
    }

}
