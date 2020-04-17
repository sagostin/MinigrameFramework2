package cash.playmc.cashevents.minigame.listeners;

import cash.playmc.cashevents.minigame.datatypes.Arena;
import cash.playmc.cashevents.minigame.handlers.GameHandler;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
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

    @EventHandler
    public void onLobbyDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            Player player = (Player) e.getEntity();

            if (GameHandler.playerIsPlaying(player)) {
                Arena arena = GameHandler.getArenaFromPlayer(player);
                if (arena.getState() == Arena.State.WAITING ||
                        arena.getState() == Arena.State.STARTING ||
                        arena.getState() == Arena.State.ENDING) {

                    e.setCancelled(true);

                }
            }
        }
    }

    @EventHandler
    public void hungerChange(FoodLevelChangeEvent e) {
        if (e.getEntity() instanceof Player) {
            Player player = (Player) e.getEntity();

            if (GameHandler.playerIsPlaying(player)) {
                Arena arena = GameHandler.getArenaFromPlayer(player);
                if (arena.getState() == Arena.State.WAITING ||
                        arena.getState() == Arena.State.STARTING ||
                        arena.getState() == Arena.State.ENDING) {

                    e.setCancelled(true);
                }
            }
        }
    }
}
