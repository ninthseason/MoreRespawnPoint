package pers.ninthseason.morerespawnpoint.command;

import com.google.gson.Gson;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pers.ninthseason.morerespawnpoint.Main;
import pers.ninthseason.morerespawnpoint.container.RangeLocation;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class RespawnPoint implements CommandExecutor {
    private final Main plugin;
    private final Gson gson = new Gson();

    public RespawnPoint(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!player.hasPermission("morerespawnpoint.setpoint")) {
                player.sendMessage(Color.RED + "You do not have the permission to run the command.");
                return true;
            }
        }
        if (args.length == 0) {
            return false;
        }
        String method = args[0];
        args = (String[]) ArrayUtils.subarray(args, 1, args.length);

        if (method.equalsIgnoreCase("add")) {
            if (args.length != 6) {
                return false;
            }
            RangeLocation entry = new RangeLocation();

            try {
                entry.setX1(Integer.parseInt(args[0]));
                entry.setY1(Integer.parseInt(args[1]));
                entry.setZ1(Integer.parseInt(args[2]));
                entry.setX2(Integer.parseInt(args[3]));
                entry.setY2(Integer.parseInt(args[4]));
                entry.setZ2(Integer.parseInt(args[5]));
            } catch (NumberFormatException e) {
                return false;
            }

            List<String> respawn_regions = (List<String>) plugin.getConfig().getList("respawn_regions");
            if (respawn_regions == null) {
                respawn_regions = new ArrayList<>();
            }
            String s = gson.toJson(entry);
            respawn_regions.add(s);
            plugin.getConfig().set("respawn_regions", respawn_regions);
            plugin.saveConfig();
            sender.sendMessage("Location saved. size: " + respawn_regions.size());

        } else if (method.equalsIgnoreCase("del")) {
            if (args.length != 1) {
                return false;
            }
            try {
                List<String> respawn_regions = (List<String>) plugin.getConfig().getList("respawn_regions");
                if (respawn_regions == null) {
                    respawn_regions = new ArrayList<>();
                }
                int index = Integer.parseInt(args[0]);
                if (index >= respawn_regions.size()) {
                    sender.sendMessage("Array out of bounds. index " + index + ", size " + respawn_regions.size());
                    return true;
                }
                respawn_regions.remove(index);
                plugin.getConfig().set("respawn_regions", respawn_regions);
                plugin.saveConfig();
                sender.sendMessage("Location deleted. size: " + respawn_regions.size());
            } catch (NumberFormatException e) {
                return false;
            }


        } else if (method.equalsIgnoreCase("list")) {
            if (args.length != 0) {
                return false;
            }
            List<String> respawn_regions = (List<String>) plugin.getConfig().getList("respawn_regions");
            if (respawn_regions == null) {
                respawn_regions = new ArrayList<>();
            }
            if (respawn_regions.size() == 0) {
                sender.sendMessage("There are no respawn points yet.");
                return true;
            }

            StringBuilder message = new StringBuilder("Current respawn points:\n");
            for (int i = 0; i < respawn_regions.size(); i++) {
                System.out.println(respawn_regions.get(i));
                RangeLocation loc = gson.fromJson(respawn_regions.get(i), RangeLocation.class);
                message.append("    [").append(i).append("] ").append(loc.getX1()).append(",").append(loc.getY1()).append(",").append(loc.getZ1()).append(" -> ").append(loc.getX2()).append(",").append(loc.getY2()).append(",").append(loc.getZ2()).append("\n");
            }
            sender.sendMessage(message.toString());

        } else {
            return false;
        }
        return true;
    }
}
