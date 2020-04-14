package cash.playmc.cashevents.commands;


import cash.playmc.cashevents.handler.EventsHandler;
import cash.playmc.cashevents.minigame.handlers.GameHandler;
import cash.playmc.cashevents.utils.InventoryStorageUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class JoinEventCommand implements CommandExecutor {

    @EventHandler
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "You must be a player to use this command.");
            return true;
        }

        Player player = (Player) sender;

        if (GameHandler.playerIsPlaying(player)) {
            player.sendMessage(ChatColor.RED + "You're already in an event.");
            return true;
        }

        InventoryStorageUtil.$().saveInventory(player);
        player.getInventory().clear();
        player.updateInventory();

        EventsHandler.selectRandomGame();

        EventsHandler.getGame().getArenas().get(0).addPlayer(player);

        Bukkit.broadcastMessage(GameHandler.getArenaFromPlayer(player).getArenaID().toString());

        return true;
    }
}