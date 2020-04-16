package cash.playmc.cashevents.testgame;

import cash.playmc.cashevents.CashEvents;
import cash.playmc.cashevents.minigame.customevents.ArenaJoinEvent;
import cash.playmc.cashevents.minigame.customevents.ArenaStartEvent;
import cash.playmc.cashevents.minigame.datatypes.Arena;
import cash.playmc.cashevents.minigame.datatypes.Game;
import cash.playmc.cashevents.minigame.datatypes.GamePlayer;
import cash.playmc.cashevents.minigame.handlers.GameHandler;
import cash.playmc.cashevents.minigame.handlers.WorldHandler;
import cash.playmc.cashevents.utils.FireworkUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitTask;

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

    private static BukkitTask fireworks = null;

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

    private static int fireworksCountMax = 5;

    @EventHandler
    public void onGameStart(ArenaStartEvent e) {
        Arena arena = e.getArena();

        arena.getPlayers().forEach(gp -> {
            Player player = gp.getPlayer();
            player.teleport(new Location(Bukkit.getWorld(arena.getSlimeWorldName()), 0, 65.5, 0));

            player.sendMessage(ChatColor.GREEN + " ----- $$$ CashEvents $$$ ----- ");
            player.sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "Game: " +
                    ChatColor.YELLOW + arena.getGame().getGameName());
            player.sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "Map: " +
                    ChatColor.YELLOW + arena.getMapName());
            player.sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "Map Author: " +
                    ChatColor.YELLOW + arena.getAuthor());
            player.sendMessage(ChatColor.GREEN + " ----- $$$ CashEvents $$$ ----- ");
        });
    }

    @EventHandler
    public void onDeath(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
            Player player = (Player) e.getEntity();
            Player damager = (Player) e.getDamager();

            if (GameHandler.playerIsPlaying(player) && GameHandler.playerIsPlaying(damager)) {
                if (GameHandler.getArenaFromPlayer(player).getArenaID() == GameHandler.getArenaFromPlayer(damager).getArenaID()) {
                    Arena arena = GameHandler.getArenaFromPlayer(player);
                    if (arena.getState() == Arena.State.INGAME) {
                        if (e.getDamage() >= player.getHealth()) {
                            e.setCancelled(true);
                            arena.getPlayer(player.getUniqueId()).setMode(GamePlayer.Mode.SPECTATOR);
                        }

                        if (arena.getPlayersByMode(GamePlayer.Mode.PLAYER).size() < 2) {
                            GamePlayer winner = arena.getPlayersByMode(GamePlayer.Mode.PLAYER).get(0);

                            arena.getPlayers().forEach(gp ->
                                    gp.getPlayer().sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD +
                                            winner.getPlayer().getName() + ChatColor.GREEN + " has won the event!"));

                            fireworks = Bukkit.getScheduler().runTaskTimer(CashEvents.getPlugin(), () -> {
                                if (fireworksCountMax >= 1) {
                                    fireworksCountMax--;
                                    FireworkUtil.spawnFirework(winner.getPlayer().getLocation());
                                } else {
                                    fireworks.cancel();
                                }
                            }, 20, 2 * 20);

                            //TODO give player reward money for event
                            arena.end();
                        }
                    }
                }
            }
        }

    }


}
