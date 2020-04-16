package cash.playmc.cashevents.minigame.listeners;

import cash.playmc.cashevents.minigame.handlers.GameHandler;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class PlayerListener implements Listener {

    @EventHandler
    public void onTeleport(PlayerTeleportEvent e) {
        Player player = e.getPlayer();
        if (GameHandler.playerIsPlaying(player)) {
            if (e.getCause() == PlayerTeleportEvent.TeleportCause.COMMAND) {
                player.sendMessage(ChatColor.RED + "You cannot teleport while in a game.");
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onDisconnect(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        if (GameHandler.playerIsPlaying(player)) {
            GameHandler.getArenaFromPlayer(player).removePlayer(player);
        }
    }

    //TODO handle player disconnect
}
