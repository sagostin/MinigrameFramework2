package cash.playmc.cashevents.minigame.handlers;

import cash.playmc.cashevents.minigame.datatypes.Arena;
import cash.playmc.cashevents.minigame.datatypes.Game;

import java.util.ArrayList;
import java.util.List;

public class GameHandler {
    private static List<Game> gameList = new ArrayList<>();

    public static void playerIsPlaying() {

    }

    public static void getArenaFromPlayer() {
        for (Game game : gameList) {
            for (Arena arena : game.getArenas()) {

            }
        }
    }

    public void addGame() {

    }
}
