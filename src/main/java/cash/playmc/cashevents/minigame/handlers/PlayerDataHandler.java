package cash.playmc.cashevents.minigame.handlers;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class PlayerDataHandler {

    private static HashMap<UUID, PlayerData> playerData = new HashMap<>();

    public static void savePlayer(Player player) {
        if (!playerData.containsKey(player.getUniqueId())) {
            playerData.put(player.getUniqueId(), new PlayerData(player.getUniqueId(), player.getLevel(), player.getExp(), player.getLocation().clone()));
        }
    }

    public static void givePlayer(Player player) {
        if (playerData.containsKey(player.getUniqueId())) {
            PlayerData data = playerData.get(player.getUniqueId());
            player.teleport(data.getLocation());
            player.setExp(data.getXp());
            player.setLevel(data.getLevel());

            playerData.remove(player.getUniqueId());
        }
    }

    public static class PlayerData {
        private UUID uuid;
        private int level;
        private float xp;
        private Location location;

        public PlayerData(UUID uuid, int level, float xp, Location location) {
            this.uuid = uuid;
            this.level = level;
            this.xp = xp;
            this.location = location;
        }

        public float getXp() {
            return xp;
        }

        public int getLevel() {
            return level;
        }

        public Location getLocation() {
            return location;
        }
    }
}
