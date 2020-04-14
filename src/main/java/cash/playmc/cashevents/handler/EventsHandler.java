package cash.playmc.cashevents.handler;

import cash.playmc.cashevents.minigame.datatypes.Game;
import cash.playmc.cashevents.minigame.handlers.GameHandler;

import java.util.Random;

public class EventsHandler {

    private static Game game;

    public static void selectRandomGame() {
        Random random = new Random();
        int randomNumber = random.nextInt(GameHandler.getGames().size());
        game = GameHandler.getGames().get(randomNumber);
    }

    public static Game getGame() {
        return game;
    }
}
