package cash.playmc.cashevents.minigame.datatypes;

import cash.playmc.cashevents.minigame.countdowns.CountdownHandler;
import cash.playmc.cashevents.minigame.countdowns.EndCountdown;
import cash.playmc.cashevents.minigame.countdowns.StartCountdown;
import cash.playmc.cashevents.minigame.customevents.ArenaEndEvent;
import cash.playmc.cashevents.minigame.customevents.ArenaJoinEvent;
import cash.playmc.cashevents.minigame.customevents.ArenaLeaveEvent;
import cash.playmc.cashevents.minigame.customevents.ArenaStartEvent;
import cash.playmc.cashevents.minigame.enums.ArenaState;
import cash.playmc.cashevents.minigame.handlers.WorldHandler;
import cash.playmc.cashevents.utils.PlayerStorageUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
    private String mapName;
    private UUID arenaID;
    private Game game;

    public Arena(Game game, String worldName) {
        players = new ArrayList<>();
        this.worldName = worldName;
        this.arenaID = UUID.randomUUID();
        this.game = game;

        YamlConfiguration config = WorldHandler.getWorldConfigFile(game.getGameName(), worldName);
        this.mapName = config.getString("mapname");
        author = config.getString("author");
        minPlayers = config.getInt("minplayers");
        maxPlayers = config.getInt("maxplayers");

        WorldHandler.loadWorldClone(this);
    }

    public Game getGame() {
        return game;
    }

    private String worldName;

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
        setArenaState(ArenaState.INGAME);
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

    public String getMapName() {
        return mapName;
    }

    public void end() {
        ArenaEndEvent event = new ArenaEndEvent(this);
        Bukkit.getServer().getPluginManager().callEvent(event);

        if (arenaState != ArenaState.ENDING) {
            CountdownHandler.start(new EndCountdown(), this, 10);
        }
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

    public void setArenaState(ArenaState arenaState) {
        this.arenaState = arenaState;
    }

    public void addPlayer(Player player) {
        players.add(new GamePlayer(player.getUniqueId()));

        PlayerStorageUtil.$().saveInventory(player);
        player.getInventory().clear();
        player.updateInventory();

        ArenaJoinEvent event = new ArenaJoinEvent(this, player);
        Bukkit.getServer().getPluginManager().callEvent(event);

        for (GamePlayer gamePlayer : players) {
            gamePlayer.getPlayer().sendMessage(ChatColor.YELLOW + player.getName() + " has joined the event! " +
                    ChatColor.GRAY + "(" + ChatColor.WHITE + players.size() + ChatColor.DARK_GRAY + "/" +
                    ChatColor.WHITE + maxPlayers + ChatColor.GRAY + ")");
        }

        if (players.size() >= minPlayers && arenaState != ArenaState.STARTING) {
            arenaState = ArenaState.STARTING;
            CountdownHandler.start(new StartCountdown(), this, 10);
        }
    }

    public void removePlayer(Player player) {
        for (GamePlayer gamePlayer : players) {
            if (gamePlayer.getUUID() == player.getUniqueId()) {
                players.remove(gamePlayer);

                PlayerStorageUtil.$().restore(player);

                ArenaLeaveEvent event = new ArenaLeaveEvent(this, player);
                Bukkit.getServer().getPluginManager().callEvent(event);
                break;
            }
        }

        if (arenaState != ArenaState.ENDING) {
            for (GamePlayer gamePlayer : players) {
                gamePlayer.getPlayer().sendMessage(ChatColor.YELLOW + player.getName() + " has left the event! " +
                        ChatColor.GRAY + "(" + ChatColor.WHITE + (players.size()) + ChatColor.DARK_GRAY + "/" +
                        ChatColor.WHITE + maxPlayers + ChatColor.GRAY + ")");
            }
        }

        if (players.size() <= 1) {
            for (GamePlayer gamePlayer : players) {
                gamePlayer.getPlayer().sendMessage(ChatColor.RED + "Not enough players to continue the event!");
            }
            end();
        }
    }

    public String getSlimeWorldName() {
        return arenaID.toString();
    }


}
