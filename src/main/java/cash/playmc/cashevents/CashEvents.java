package cash.playmc.cashevents;

import cash.playmc.cashevents.commands.JoinEventCommand;
import cash.playmc.cashevents.commands.LeaveEventCommand;
import cash.playmc.cashevents.minigame.datatypes.Arena;
import cash.playmc.cashevents.minigame.datatypes.Game;
import cash.playmc.cashevents.minigame.handlers.GameHandler;
import cash.playmc.cashevents.testgame.FFA;
import co.aikar.commands.BukkitCommandManager;
import com.grinderwolf.swm.api.SlimePlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class CashEvents extends JavaPlugin implements Listener {

    private static CashEvents plugin;
    private static PluginManager pluginManager;
    private static SlimePlugin slimePlugin;
    private static BukkitCommandManager commandManager;

    /*
    - Save player inventory for when they join
    - Give them their inventory back when the game finished
    - Add protection against disconnecting etc
     */


    @Override
    public void onDisable() {
        for (Game game : GameHandler.getGames()) {
            for (Arena arena : game.getArenas()) {
                Bukkit.unloadWorld(arena.getWorldName(), true);
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

        pluginManager.registerEvents(this, this);
        pluginManager.registerEvents(new FFA(), this);

        getCommand("joinevent").setExecutor(new JoinEventCommand());
        getCommand("leaveevent").setExecutor(new LeaveEventCommand());

        FFA.createFFA();
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();

        Bukkit.broadcastMessage(ChatColor.GREEN + GameHandler.getArenaIDFromPlayer(player).toString());
    }
}
