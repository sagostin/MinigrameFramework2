package cash.playmc.cashevents;

import com.grinderwolf.swm.api.SlimePlugin;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class CashEvents extends JavaPlugin {

    private static CashEvents plugin;
    private static PluginManager pluginManager;
    private static SlimePlugin slimePlugin;

    public static SlimePlugin getSlimePlugin() {
        return slimePlugin;
    }

    @Override
    public void onDisable() {

    }

    public static CashEvents getPlugin() {
        return plugin;
    }

    @Override
    public void onEnable() {
        plugin = this;
        pluginManager = Bukkit.getPluginManager();

        slimePlugin = (SlimePlugin) Bukkit.getPluginManager().getPlugin("SlimeWorldManager");
    }
}
