package cash.playmc.cashevents.testgame;

import cash.playmc.cashevents.minigame.customevents.ArenaJoinEvent;
import cash.playmc.cashevents.minigame.customevents.ArenaStartEvent;
import cash.playmc.cashevents.minigame.datatypes.Arena;
import cash.playmc.cashevents.minigame.datatypes.Game;
import cash.playmc.cashevents.minigame.datatypes.GamePlayer;
import cash.playmc.cashevents.minigame.enums.ArenaState;
import cash.playmc.cashevents.minigame.handlers.GameHandler;
import cash.playmc.cashevents.minigame.handlers.WorldHandler;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class LastManStanding implements Listener {

    private static String gameName = "Last Man Standing";

    public static void createLMS() {
        Game game = new Game(gameName);
        game.addWorld("world-arena-lastmanstanding-1");
        GameHandler.addGame(game);
        WorldHandler.loadWorldsForGame(game);

        Arena arena = new Arena(game, "world-arena-lastmanstanding-1");
        game.addArena(arena);
    }

    @EventHandler
    public void onGameStart(ArenaStartEvent e) {
        Arena arena = e.getArena();

        for (GamePlayer gamePlayer : arena.getPlayers()) {
            Player player = gamePlayer.getPlayer();
            player.teleport(new Location(Bukkit.getWorld(arena.getSlimeWorldName()), 0, 65.5, 0));
        }
    }

    @EventHandler
    public void onArenaJoin(ArenaJoinEvent e) {
        if (e.getArena().getGame().getGameName() == "Last Man Standing") {
            YamlConfiguration yamlConfiguration = WorldHandler.getWorldConfigFile(gameName, e.getArena().getWorldName());
            Location lobby = new Location(
                    Bukkit.getWorld(e.getArena().getSlimeWorldName()),
                    yamlConfiguration.getDouble("lobby.x"),
                    yamlConfiguration.getDouble("lobby.y"),
                    yamlConfiguration.getDouble("lobby.z"));
            e.getPlayer().teleport(lobby);
        }
    }

    @EventHandler
    public void onDeath(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
            Player player = (Player) e.getEntity();
            Player damager = (Player) e.getDamager();

            if (GameHandler.playerIsPlaying(player) && GameHandler.playerIsPlaying(damager)) {
                if (GameHandler.getArenaFromPlayer(player).getArenaID() == GameHandler.getArenaFromPlayer(damager).getArenaID()) {
                    Arena arena = GameHandler.getArenaFromPlayer(player);
                    if (arena.getArenaState() == ArenaState.INGAME) {
                        if (e.getDamage() >= player.getHealth()) {
                            e.setCancelled(true);
                            arena.getPlayer(player.getUniqueId()).setMode(GamePlayer.Mode.SPECTATOR);
                        }

                        if (arena.getPlayersByMode(GamePlayer.Mode.PLAYER).size() < 2) {
                            GamePlayer winner = arena.getPlayersByMode(GamePlayer.Mode.PLAYER).get(0);
                            Bukkit.broadcastMessage(winner.getPlayer().getName() + " has one " + arena.getGame().getGameName());
                            arena.end();
                        }
                    }
                }
            }
        }

    }


}
