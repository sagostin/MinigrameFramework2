package cash.playmc.cashevents.minigame.handlers;

import cash.playmc.cashevents.CashEvents;
import cash.playmc.cashevents.minigame.datatypes.Arena;
import cash.playmc.cashevents.minigame.datatypes.Game;
import com.grinderwolf.swm.api.exceptions.*;
import com.grinderwolf.swm.api.loaders.SlimeLoader;
import com.grinderwolf.swm.api.world.SlimeWorld;
import com.grinderwolf.swm.api.world.properties.SlimeProperties;
import com.grinderwolf.swm.api.world.properties.SlimePropertyMap;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorldHandler {

    private static HashMap<String, List<SlimeWorld>> slimeWorlds = new HashMap<>();

    public static void loadWorldsForGame(Game game) {
        List<SlimeWorld> worlds = new ArrayList<>();
        for (String worldName : game.getWorldNames()) {
            worlds.add(loadSlimeWorld(worldName));
        }

        slimeWorlds.put(game.getGameName(), worlds);
    }

    public static void loadWorldClone(Arena arena) {
        for (Map.Entry<String, List<SlimeWorld>> gameWorldMaster : slimeWorlds.entrySet()) {
            if (gameWorldMaster.getKey().equalsIgnoreCase(arena.getGame().getGameName())) {
                for (SlimeWorld slimeWorld : gameWorldMaster.getValue()) {
                    if (slimeWorld.getName().equalsIgnoreCase(arena.getWorldName())) {
                        try {
                            // Note that this method should be called asynchronously
                            SlimeWorld world = slimeWorld.clone(arena.getArenaID().toString(), null);
                            // This method must be called synchronously
                            CashEvents.getSlimePlugin().generateWorld(world);
                        } catch (IOException | WorldAlreadyExistsException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
                break;
            }
        }
    }

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

    private static SlimeWorld loadSlimeWorld(String slimeWorld) {
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

        SlimeWorld world = null;

        try {
            // Note that this method should be called asynchronously
            world = CashEvents.getSlimePlugin().loadWorld(slimeLoader, slimeWorld, true, propMap);

            // This method must be called synchronously
            CashEvents.getSlimePlugin().generateWorld(world);
        } catch (UnknownWorldException | IOException | CorruptedWorldException | NewerFormatException | WorldInUseException ex) {
            ex.printStackTrace();
        }

        return world;
    }
}
