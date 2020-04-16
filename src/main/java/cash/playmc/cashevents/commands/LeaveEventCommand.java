package cash.playmc.cashevents.commands;


import cash.playmc.cashevents.minigame.handlers.GameHandler;
import cash.playmc.cashevents.minigame.utils.PlayerStorageUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import java.util.Arrays;

public class LeaveEventCommand implements CommandExecutor {

    private static boolean inventoryEmpty(Player player) {
        return Arrays.asList(player.getInventory().getContents()).contains(null);
    }

    @EventHandler
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "You must be a player to use this command.");
            return true;
        }

        Player player = (Player) sender;

        if (!GameHandler.playerIsPlaying(player)) {
            player.sendMessage(ChatColor.RED + "You're not in an event.");
            return true;
        }

        GameHandler.getArenaFromPlayer(player).removePlayer(player);

        PlayerStorageUtil.$().restore(player);
        player.updateInventory();

        return true;
    }
}