package cash.playmc.cashevents.minigame.datatypes;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Game {
    private String gameName;
    private List<Arena> arenaList;
    private List<String> worldNames;

    public Game(String gameName) {
        this.gameName = gameName;
        this.arenaList = new ArrayList<>();
        this.worldNames = new ArrayList<>();
    }

    public String getGameName() {
        return gameName;
    }

    public List<Arena> getArenas() {
        return arenaList;
    }

    /*
    Game Datatype:

    You can use this to define games.
    Example: FFA, TDM, etc.

    In here you will be able to check which Arenas are running etc.
    You can stop all running arenas, or view their state.

     */

    public List<String> getWorldNames() {
        return worldNames;
    }

    public void addWorld(String worldName) {
        worldNames.add(worldName);
    }

    public void addArena(Arena arena) {
        arenaList.add(arena);
    }

    public void removeArena(UUID uuid) {
        for (Arena arena : getArenas()) {
            if (arena.getArenaID() == uuid) {
                arena.end();
                getArenas().remove(uuid);
            }
        }
    }

    public void addPlayerToBestArena(Player player) {
        //calculate the best arena based on state && min/max players && current players
    }

}
