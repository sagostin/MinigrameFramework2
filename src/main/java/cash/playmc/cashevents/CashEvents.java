package cash.playmc.cashevents;

import cash.playmc.cashevents.minigame.datatypes.Arena;
import cash.playmc.cashevents.minigame.handlers.GameHandler;
import cash.playmc.cashevents.testgame.FFA;
import com.grinderwolf.swm.api.SlimePlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;
import java.util.UUID;

public class CashEvents extends JavaPlugin implements Listener {

    private static CashEvents plugin;
    private static PluginManager pluginManager;
    private static SlimePlugin slimePlugin;


    @Override
    public void onDisable() {

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

        FFA.createFFA();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();

        for (Map.Entry<UUID, Arena> arenaEntry : GameHandler.getGameByName("test").getArenas().entrySet()) {
            arenaEntry.getValue().addPlayer(player);
            Bukkit.broadcastMessage(ChatColor.RED + "Player has been added to the arena with ID: " + arenaEntry.getKey());
            break;
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();

        Bukkit.broadcastMessage(ChatColor.GREEN + GameHandler.getArenaIDFromPlayer(player).toString());
    }
}
