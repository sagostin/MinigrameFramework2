package cash.playmc.cashevents;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class CashEvents extends JavaPlugin {

    private static CashEvents plugin;
    private static PluginManager pluginManager;

    @Override
    public void onEnable(){
        plugin = this;
        pluginManager = Bukkit.getPluginManager();
    }

    @Override
    public void onDisable(){

    }

    public static CashEvents getPlugin(){
        return plugin;
    }
}
