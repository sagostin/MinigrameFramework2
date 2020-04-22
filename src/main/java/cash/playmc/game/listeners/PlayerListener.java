package cash.playmc.game.listeners;

import cash.playmc.game.Minigame;
import cash.playmc.game.datatypes.Arena;
import cash.playmc.game.handlers.GameHandler;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
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
    public void cancelIngameTeleport(PlayerCommandPreprocessEvent e) {
        Player player = e.getPlayer();
        // switch over to list in main class

        if (e.getMessage().startsWith("/")) {
            String[] args = e.getMessage().split(" ");
            if (Minigame.getBlockedCommands().contains(args[0])) {
                player.sendMessage(ChatColor.RED + "You cannot use this command while in a game.");
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

    @EventHandler
    public void inventoryInteract(InventoryInteractEvent e) {
        if (e.getWhoClicked() instanceof Player) {
            Player player = (Player) e.getWhoClicked();

            if (GameHandler.playerIsPlaying(player)) {
                if (GameHandler.getArenaFromPlayer(player).getState() == Arena.State.STARTING ||
                        GameHandler.getArenaFromPlayer(player).getState() == Arena.State.ENDING ||
                        GameHandler.getArenaFromPlayer(player).getState() == Arena.State.WAITING) {
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void cancelBreak(BlockBreakEvent e) {
        Player player = e.getPlayer();

        if (GameHandler.playerIsPlaying(player)) {
            if (GameHandler.getArenaFromPlayer(player).getState() == Arena.State.STARTING ||
                    GameHandler.getArenaFromPlayer(player).getState() == Arena.State.ENDING ||
                    GameHandler.getArenaFromPlayer(player).getState() == Arena.State.WAITING) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void cancelPlace(BlockPlaceEvent e) {
        Player player = e.getPlayer();

        if (GameHandler.playerIsPlaying(player)) {
            if (GameHandler.getArenaFromPlayer(player).getState() == Arena.State.STARTING ||
                    GameHandler.getArenaFromPlayer(player).getState() == Arena.State.ENDING ||
                    GameHandler.getArenaFromPlayer(player).getState() == Arena.State.WAITING) {
                e.setCancelled(true);
            }
        }
    }
}
