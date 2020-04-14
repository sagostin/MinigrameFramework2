package cash.playmc.cashevents.minigame.datatypes;

import org.bukkit.entity.Player;

import java.util.*;

public class Game {
    private String gameName;
    private HashMap<UUID, Arena> arenaHashMap;
    private List<String> worldNames;

    public Game(String gameName) {
        this.gameName = gameName;
        this.arenaHashMap = new HashMap<>();
        this.worldNames = new ArrayList<>();
    }

    public String getGameName() {
        return gameName;
    }

    public HashMap<UUID, Arena> getArenaHashMap() {
        return arenaHashMap;
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
        arenaHashMap.put(UUID.randomUUID(), arena);
    }

    public void removeArena(UUID uuid) {
        if (arenaHashMap.containsKey(uuid)) {
            arenaHashMap.remove(uuid);
        }
    }

    public HashMap<UUID, Arena> getArenas() {
        return arenaHashMap;
    }

    public void addPlayerToBestArena(Player player) {
        for (Map.Entry<UUID, Arena> arenaEntry : getArenas().entrySet()) {
            Arena arena = arenaEntry.getValue();

            //calculate the best arena based on state && min/max players && current players
        }
    }

}
