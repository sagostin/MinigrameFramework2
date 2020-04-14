package cash.playmc.cashevents.testgame;

import cash.playmc.cashevents.minigame.customevents.ArenaStartEvent;
import cash.playmc.cashevents.minigame.datatypes.Arena;
import cash.playmc.cashevents.minigame.datatypes.Game;
import cash.playmc.cashevents.minigame.datatypes.GamePlayer;
import cash.playmc.cashevents.minigame.handlers.GameHandler;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class FFA implements Listener {

    public static void createFFA() {
        Game game = new Game("Last Man Standing");
        Arena arena = new Arena("world-arena-lastmanstanding-1");
        game.addArena(arena);
        GameHandler.addGame(game);
    }

    @EventHandler
    public void onGameStart(ArenaStartEvent e) {
        Arena arena = e.getArena();

        for (GamePlayer gamePlayer : arena.getPlayers()) {
            Player player = gamePlayer.getPlayer();
            player.teleport(new Location(Bukkit.getWorld(arena.getWorldName()), 0, 69, 0));
        }
    }


}
