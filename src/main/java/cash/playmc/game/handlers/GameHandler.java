package cash.playmc.game.handlers;

import cash.playmc.game.datatypes.Arena;
import cash.playmc.game.datatypes.Game;
import cash.playmc.game.datatypes.GamePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GameHandler {
    private static List<Game> gameList = new ArrayList<>();

    public static boolean playerIsPlaying(Player player) {
        for (Game game : gameList) {
            for (Arena arena : game.getArenas()) {
                for (GamePlayer gamePlayer : arena.getPlayers()) {
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
            for (Arena arena : game.getArenas()) {
                for (GamePlayer gamePlayer : arena.getPlayers()) {
                    if (gamePlayer.getUUID() == player.getUniqueId()) {
                        return arena.getArenaID();
                    }
                }
            }
        }

        return null;
    }

    public static Arena getArenaFromPlayer(Player player) {
        for (Game game : gameList) {
            for (Arena arena : game.getArenas()) {
                for (GamePlayer gamePlayer : arena.getPlayers()) {
                    if (gamePlayer.getUUID() == player.getUniqueId()) {
                        return arena;
                    }
                }
            }
        }

        return null;
    }

    public static Arena getArenaFromArenaUUID(UUID arenaID) {
        for (Game game : gameList) {
            for (Arena arena : game.getArenas()) {
                if (arena.getArenaID() == arenaID) {
                    return arena;
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

    public static List<Game> getGames() {
        return gameList;
    }

    public static void addGame(Game game) {
        gameList.add(game);
    }
}
