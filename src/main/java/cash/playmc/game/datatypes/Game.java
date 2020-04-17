package cash.playmc.game.datatypes;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Iterator;
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
        Iterator<Arena> arenaIterator = arenaList.iterator();
        while (arenaIterator.hasNext()) {
            if (arenaIterator.next().getArenaID() == uuid) {
                arenaIterator.remove();
                break;
            }
        }
    }

    public void addWorldName(String worldName) {
        worldNames.add(worldName);
    }

    public void addPlayerToBestArena(Player player) {
        //calculate the best arena based on state && min/max players && current players
    }

}
