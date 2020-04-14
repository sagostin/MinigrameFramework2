package cash.playmc.cashevents.minigame.datatypes;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private String gameName;
    private List<Arena> arenaList;
    private List<String> worldNames;

    /*
    Game Datatype:

    You can use this to define games.
    Example: FFA, TDM, etc.

    In here you will be able to check which Arenas are running etc.
    You can stop all running arenas, or view their state.

     */

    public Game(String gameName) {
        this.gameName = gameName;
        this.arenaList = new ArrayList<>();
        this.worldNames = new ArrayList<>();
    }

    public void addWorld(String worldName) {
        worldNames.add(worldName);
    }

    public void createArena(String worldName) {

    }

    public void removeArena(Arena arena) {

    }

    public List<Arena> getArenas() {
        return arenaList;
    }

}
