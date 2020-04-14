package cash.playmc.cashevents.minigame.handlers;

import cash.playmc.cashevents.CashEvents;
import com.grinderwolf.swm.api.exceptions.CorruptedWorldException;
import com.grinderwolf.swm.api.exceptions.NewerFormatException;
import com.grinderwolf.swm.api.exceptions.UnknownWorldException;
import com.grinderwolf.swm.api.exceptions.WorldInUseException;
import com.grinderwolf.swm.api.loaders.SlimeLoader;
import com.grinderwolf.swm.api.world.SlimeWorld;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class WorldHandler {

    public static YamlConfiguration getConfigFile(String worldName) {
        File file = new File(
                CashEvents.getPlugin().getDataFolder() + File.separator + "guilds" + File.separator,
                worldName + ".yml");

        if (!(file.exists())) {
            try {
                YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
                configuration.set("worldname", worldName);
                configuration.set("worldauthor", worldName);
                configuration.set("minplayers", 2);
                configuration.set("maxplayers", 16);
                configuration.save(file);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return YamlConfiguration.loadConfiguration(file);
    }

    public static void loadSlimeWorld(String worldName) {
        SlimeLoader slimeLoader = CashEvents.getSlimePlugin().getLoader("file");
        SlimeWorld.SlimeProperties props = SlimeWorld.SlimeProperties.builder()
                .difficulty(0)
                .allowAnimals(false)
                .allowMonsters(false)
                .spawnX(0)
                .spawnY(50)
                .spawnZ(0)
                .pvp(true)
                .readOnly(true)
                .build();

        try {
            // Note that this method should be called asynchronously
            SlimeWorld world = CashEvents.getSlimePlugin().loadWorld(slimeLoader, worldName, props);

            // This method must be called synchronously
            CashEvents.getSlimePlugin().generateWorld(world);
        } catch (UnknownWorldException | IOException | CorruptedWorldException | NewerFormatException | WorldInUseException ex) {
            ex.printStackTrace();
        }
    }
}
