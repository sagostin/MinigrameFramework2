package cash.playmc.game.datatypes;

import cash.playmc.game.countdowns.CountdownHandler;
import cash.playmc.game.countdowns.EndCountdown;
import cash.playmc.game.countdowns.StartCountdown;
import cash.playmc.game.customevents.ArenaEndEvent;
import cash.playmc.game.customevents.ArenaJoinEvent;
import cash.playmc.game.customevents.ArenaLeaveEvent;
import cash.playmc.game.customevents.ArenaStartEvent;
import cash.playmc.game.handlers.WorldHandler;
import cash.playmc.game.utils.PlayerStorageUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class Arena {

    private List<GamePlayer> players;
    private State arenaState = State.WAITING;

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
        setState(Arena.State.INGAME);
    }

    public Arena.State getState() {
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

    public void setState(Arena.State arenaState) {
        this.arenaState = arenaState;
    }

    public void end() {
        ArenaEndEvent event = new ArenaEndEvent(this);
        Bukkit.getServer().getPluginManager().callEvent(event);

        if (arenaState != Arena.State.ENDING) {
            CountdownHandler.start(new EndCountdown(), this, 15);
        }
    }

    public List<GamePlayer> getPlayersByMode(GamePlayer.Mode mode) {
        List<GamePlayer> playersByMode = new ArrayList<>();
        players.forEach(gp -> {
            if (gp.getMode() == mode) {
                playersByMode.add(gp);
            }

        });

        return playersByMode;
    }

    public void addPlayer(Player player) {
        players.add(new GamePlayer(player.getUniqueId()));

        PlayerStorageUtil.$().saveInventory(player);
        player.getInventory().clear();
        player.updateInventory();

        ArenaJoinEvent event = new ArenaJoinEvent(this, player);
        Bukkit.getServer().getPluginManager().callEvent(event);

        players.forEach(gp -> gp.getPlayer().sendMessage(ChatColor.YELLOW + player.getName() + " has joined the game! " +
                ChatColor.GRAY + "(" + ChatColor.WHITE + players.size() + ChatColor.DARK_GRAY + "/" +
                ChatColor.WHITE + maxPlayers + ChatColor.GRAY + ")"));

        if (players.size() >= minPlayers && arenaState != Arena.State.STARTING) {
            arenaState = Arena.State.STARTING;
            CountdownHandler.start(new StartCountdown(), this, 15);
        }
    }

    public void removePlayer(Player player) {
        Iterator<GamePlayer> gamePlayerIterator = players.iterator();
        while (gamePlayerIterator.hasNext()) {
            GamePlayer gp = gamePlayerIterator.next();
            if (gp.getUUID() == player.getUniqueId()) {
                players.remove(gp);

                PlayerStorageUtil.$().restore(player);

                ArenaLeaveEvent event = new ArenaLeaveEvent(this, player);
                Bukkit.getServer().getPluginManager().callEvent(event);
                break;
            }
        }

        if (arenaState != Arena.State.ENDING) {
            if (players.size() <= 1) {
                players.forEach(gp -> gp.getPlayer().sendMessage(
                        ChatColor.RED + "Not enough players to continue the game!"));
                end();
            } else {
                for (GamePlayer gamePlayer : players) {
                    gamePlayer.getPlayer().sendMessage(ChatColor.YELLOW + player.getName() + " has left the game! " +
                            ChatColor.GRAY + "(" + ChatColor.WHITE + (players.size()) + ChatColor.DARK_GRAY + "/" +
                            ChatColor.WHITE + maxPlayers + ChatColor.GRAY + ")");
                }
            }
        }
    }

    public String getSlimeWorldName() {
        return arenaID.toString();
    }

    public enum State {
        WAITING, STARTING, INGAME, ENDING
    }
}
