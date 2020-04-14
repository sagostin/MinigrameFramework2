package cash.playmc.cashevents.minigame.datatypes;

import cash.playmc.cashevents.minigame.countdowns.CountdownHandler;
import cash.playmc.cashevents.minigame.countdowns.StartCountdown;
import cash.playmc.cashevents.minigame.customevents.ArenaEndEvent;
import cash.playmc.cashevents.minigame.customevents.ArenaStartEvent;
import cash.playmc.cashevents.minigame.enums.ArenaState;
import cash.playmc.cashevents.minigame.handlers.WorldHandler;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Arena {

    private List<GamePlayer> players;
    private ArenaState arenaState = ArenaState.WAITING;

    private int minPlayers;
    private int maxPlayers;
    private String author;
    private String worldNameFormatted;
    private UUID arenaID;

    public Arena(Game game, String worldName) {
        players = new ArrayList<>();
        this.worldName = worldName;
        this.arenaID = UUID.randomUUID();

        YamlConfiguration config = WorldHandler.getWorldConfigFile(game.getGameName(), worldName);
        this.worldNameFormatted = config.getString("worldname");
        author = config.getString("author");
        minPlayers = config.getInt("minplayers");
        maxPlayers = config.getInt("maxplayers");

        WorldHandler.loadSlimeWorld(worldName);
    }

    private String worldName;

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

    public String getWorldName() {
        return worldName;
    }

    public UUID getArenaID() {
        return arenaID;
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

    public void start() {
        ArenaStartEvent event = new ArenaStartEvent(this);
        Bukkit.getServer().getPluginManager().callEvent(event);
    }

    public ArenaState getArenaState() {
        return arenaState;
    }

    public int getMinPlayers() {
        return minPlayers;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public String getAuthor() {
        return author;
    }

    public String getWorldNameFormatted() {
        return worldNameFormatted;
    }

    public void end() {
        ArenaEndEvent event = new ArenaEndEvent(this);
        Bukkit.getServer().getPluginManager().callEvent(event);
    }

    public List<GamePlayer> getPlayersByMode(GamePlayer.Mode mode) {
        List<GamePlayer> players = new ArrayList<>();
        for (GamePlayer gamePlayer : this.players) {
            if (gamePlayer.getMode() == mode) {
                players.add(gamePlayer);
            }
        }

        return players;
    }

    public void addPlayer(Player player) {
        players.add(new GamePlayer(player.getUniqueId()));

        CountdownHandler.start(new StartCountdown(), this, 10);
    }

    public void removePlayer(Player player) {
        for (GamePlayer gamePlayer : players) {
            if (gamePlayer.getUUID() == player.getUniqueId()) {
                players.remove(gamePlayer);
                break;
            }
        }
    }
}
