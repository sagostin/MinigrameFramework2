package cash.playmc.cashevents.minigame.listeners;

import cash.playmc.cashevents.minigame.handlers.GameHandler;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

public class PlayerListener implements Listener {

    @EventHandler
    public void onTeleport(PlayerTeleportEvent e) {
        Player player = e.getPlayer();
        if (GameHandler.playerIsPlaying(player)) {
            if (e.getCause() == PlayerTeleportEvent.TeleportCause.COMMAND) {
                player.sendMessage(ChatColor.RED + "You cannot teleport while in an event.");
                e.setCancelled(true);
            }
        }
    }

    //TODO handle player disconnect
}
