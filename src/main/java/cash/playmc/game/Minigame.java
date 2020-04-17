package cash.playmc.game;

import cash.playmc.game.datatypes.Arena;
import cash.playmc.game.datatypes.Game;
import cash.playmc.game.handlers.GameHandler;
import cash.playmc.game.listeners.PlayerListener;
import cash.playmc.game.utils.PlayerStorageUtil;
import cash.playmc.testgame.LastManStanding;
import com.grinderwolf.swm.api.SlimePlugin;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Minigame extends JavaPlugin implements Listener {

    private static Minigame plugin;
    private static PluginManager pluginManager;
    private static SlimePlugin slimePlugin;

    @Override
    public void onDisable() {
        for (Game game : GameHandler.getGames()) {
            for (Arena arena : game.getArenas()) {
                arena.getPlayers().forEach(gp -> {
                    PlayerStorageUtil.$().restore(gp.getPlayer());
                });

                Bukkit.unloadWorld(arena.getSlimeWorldName(), false);
            }

            for (String masterWorlds : game.getWorldNames()) {
                Bukkit.unloadWorld(masterWorlds, false);
            }
        }
    }

    public static Minigame getPlugin() {
        return plugin;
    }

    public static SlimePlugin getSlimePlugin() {
        return slimePlugin;
    }

    @Override
    public void onEnable() {
        plugin = this;
        pluginManager = Bukkit.getPluginManager();

        slimePlugin = (SlimePlugin) Bukkit.getPluginManager().getPlugin("SlimeWorldManager");

        pluginManager.registerEvents(new PlayerListener(), this);
        pluginManager.registerEvents(new LastManStanding(), this);

        LastManStanding.createLMS();
    }
}
