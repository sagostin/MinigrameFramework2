package cash.playmc.cashevents;

import cash.playmc.cashevents.commands.JoinEventCommand;
import cash.playmc.cashevents.commands.LeaveEventCommand;
import cash.playmc.cashevents.minigame.datatypes.Arena;
import cash.playmc.cashevents.minigame.datatypes.Game;
import cash.playmc.cashevents.minigame.handlers.GameHandler;
import cash.playmc.cashevents.minigame.listeners.PlayerListener;
import cash.playmc.cashevents.minigame.utils.PlayerStorageUtil;
import cash.playmc.cashevents.testgame.LastManStanding;
import com.grinderwolf.swm.api.SlimePlugin;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class CashEvents extends JavaPlugin implements Listener {

    private static CashEvents plugin;
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

    public static CashEvents getPlugin() {
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

        getCommand("joinevent").setExecutor(new JoinEventCommand());
        getCommand("leaveevent").setExecutor(new LeaveEventCommand());

        LastManStanding.createLMS();
    }
}
