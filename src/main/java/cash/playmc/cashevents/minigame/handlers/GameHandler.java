package cash.playmc.cashevents.minigame.handlers;

import cash.playmc.cashevents.minigame.datatypes.Arena;
import cash.playmc.cashevents.minigame.datatypes.Game;
import cash.playmc.cashevents.minigame.datatypes.GamePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class GameHandler {
    private static List<Game> gameList = new ArrayList<>();

    public static boolean playerIsPlaying(Player player) {
        for (Game game : gameList) {
            for (Map.Entry<UUID, Arena> arenaEntry : game.getArenas().entrySet()) {
                for (GamePlayer gamePlayer : arenaEntry.getValue().getPlayers()) {
                    if (gamePlayer.getUUID() == player.getUniqueId()) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public static UUID getArenaIDFromPlayer(Player player) {
        for (Game game : gameList) {
            for (Map.Entry<UUID, Arena> arenaEntry : game.getArenas().entrySet()) {
                for (GamePlayer gamePlayer : arenaEntry.getValue().getPlayers()) {
                    if (gamePlayer.getUUID() == player.getUniqueId()) {
                        return arenaEntry.getKey();
                    }
                }
            }
        }

        return null;
    }

    public static Arena getArenaFromPlayer(Player player) {
        for (Game game : gameList) {
            for (Map.Entry<UUID, Arena> arenaEntry : game.getArenas().entrySet()) {
                for (GamePlayer gamePlayer : arenaEntry.getValue().getPlayers()) {
                    if (gamePlayer.getUUID() == player.getUniqueId()) {
                        return arenaEntry.getValue();
                    }
                }
            }
        }

        return null;
    }

    public static Arena getArenaFromArenaUUID(UUID arenaID) {
        for (Game game : gameList) {
            for (Map.Entry<UUID, Arena> arenaEntry : game.getArenas().entrySet()) {
                if (arenaEntry.getKey() == arenaID) {
                    return arenaEntry.getValue();
                }
            }
        }

        return null;
    }

    public static Game getGameByName(String gameName) {
        for (Game game : gameList) {
            if (game.getGameName().equalsIgnoreCase(gameName)) {
                return game;
            }
        }

        return null;
    }

    public static void addGame(Game game) {
        gameList.add(game);
    }
}
