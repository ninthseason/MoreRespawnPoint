package pers.ninthseason.morerespawnpoint.listener;

import com.google.gson.Gson;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.java.JavaPlugin;
import pers.ninthseason.morerespawnpoint.container.RangeLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RespawnEventExecutor implements Listener {
    private final JavaPlugin plugin;
    private final Random rand = new Random();
    private final Gson gson = new Gson();

    public RespawnEventExecutor(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        List<String> regions = (List<String>) plugin.getConfig().get("respawn_regions");
        if (regions == null) {
            regions = new ArrayList<>();
        }
        if (event.isBedSpawn() || regions.size() == 0) {
            return;
        }
        String selectedRegion = regions.get(rand.nextInt(regions.size()));
        RangeLocation loc = gson.fromJson(selectedRegion, RangeLocation.class);

        int x = rand.nextInt(Math.min(loc.getX1(), loc.getX2()), Math.max(loc.getX1(), loc.getX2()) + 1);
        int y = rand.nextInt(Math.min(loc.getY1(), loc.getY2()), Math.max(loc.getY1(), loc.getY2()) + 1);
        int z = rand.nextInt(Math.min(loc.getZ1(), loc.getZ2()), Math.max(loc.getZ1(), loc.getZ2()) + 1);

        Location location = new Location(event.getRespawnLocation().getWorld(), x + 0.5, y, z + 0.5);
        event.setRespawnLocation(location);

    }
}
