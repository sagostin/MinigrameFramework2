package cash.playmc.cashevents.minigame.datatypes;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Arena {

    private List<GamePlayer> players;

    /*

    Arena Datatype:

    Arenas are the individual worlds/"arenas" that games will take place in.
    Games will be able to create more arenas by creating multiple copies of worlds.
    Those worlds will have configuration files attached to them (Min players, Arena name, Arena Author, etc.)
    It will also include spawns, etc.
    When creating arena, you need to specify;
    - Game
    - WorldName (config files names will be associated with the world name)

     */

    public Arena(String worldName) {
        players = new ArrayList<>();
    }

    public List<GamePlayer> getPlayers() {
        return players;
    }

    public GamePlayer getPlayer(UUID uuid) {
        for (GamePlayer gamePlayer : players) {
            if (gamePlayer.getUUID() == uuid) {
                return gamePlayer;
            }
        }
        return null;
    }
}
