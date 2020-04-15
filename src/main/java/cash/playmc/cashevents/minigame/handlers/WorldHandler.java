package cash.playmc.cashevents.minigame.handlers;

import cash.playmc.cashevents.CashEvents;
import cash.playmc.cashevents.minigame.datatypes.Arena;
import com.grinderwolf.swm.api.exceptions.CorruptedWorldException;
import com.grinderwolf.swm.api.exceptions.NewerFormatException;
import com.grinderwolf.swm.api.exceptions.UnknownWorldException;
import com.grinderwolf.swm.api.exceptions.WorldInUseException;
import com.grinderwolf.swm.api.loaders.SlimeLoader;
import com.grinderwolf.swm.api.world.SlimeWorld;
import com.grinderwolf.swm.api.world.properties.SlimeProperties;
import com.grinderwolf.swm.api.world.properties.SlimePropertyMap;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class WorldHandler {

    public static YamlConfiguration getWorldConfigFile(String gameName, String worldName) {
        File file = new File(
                CashEvents.getPlugin().getDataFolder() + File.separator + gameName + File.separator,
                worldName + ".yml");

        if (!(file.exists())) {
            try {
                YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
                configuration.set("mapname", worldName);
                configuration.set("author", worldName);
                configuration.set("minplayers", 2);
                configuration.set("maxplayers", 16);
                configuration.save(file);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return YamlConfiguration.loadConfiguration(file);
    }

    public static SlimeWorld loadSlimeWorld(String slimeWorld, Arena uuid) {
        SlimeLoader slimeLoader = CashEvents.getSlimePlugin().getLoader("file");

        SlimePropertyMap propMap = new SlimePropertyMap();
        propMap.setString(SlimeProperties.ENVIRONMENT, "NORMAL");
        propMap.setString(SlimeProperties.WORLD_TYPE, "DEFAULT");
        propMap.setString(SlimeProperties.DIFFICULTY, "PEACEFUL");
        propMap.setBoolean(SlimeProperties.ALLOW_ANIMALS, false);
        propMap.setBoolean(SlimeProperties.ALLOW_MONSTERS, false);
        propMap.setBoolean(SlimeProperties.PVP, true);
        propMap.setInt(SlimeProperties.SPAWN_Y, 65);
        propMap.setInt(SlimeProperties.SPAWN_X, 0);
        propMap.setInt(SlimeProperties.SPAWN_Z, 0);

        try {
            // Note that this method should be called asynchronously
            SlimeWorld world = CashEvents.getSlimePlugin().loadWorld(slimeLoader, slimeWorld, true, propMap);
            //world.clone

            // This method must be called synchronously
            CashEvents.getSlimePlugin().generateWorld(world);
        } catch (UnknownWorldException | IOException | CorruptedWorldException | NewerFormatException | WorldInUseException ex) {
            ex.printStackTrace();
        }

        return null;
    }
}
