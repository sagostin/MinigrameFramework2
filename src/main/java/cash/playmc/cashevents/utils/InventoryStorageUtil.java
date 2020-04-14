package cash.playmc.cashevents.utils;

import lombok.*;
import lombok.Builder.Default;
import org.apache.commons.lang.Validate;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author kangarko
 */

/**
 * You can use this simple class to save and restore players' inventories.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class InventoryStorageUtil {

    /**
     * The instance of this singleton
     */
    private static final InventoryStorageUtil instance = new InventoryStorageUtil();
    /**
     * Stored inventories by player name
     */
    private final Map<UUID, StoredInventory> inventories = new HashMap<>();

    /**
     * Get the instance of this inventory storage.
     *
     * @return the {@link InventoryStorageUtil} instance
     */
    public static final InventoryStorageUtil $() {
        return instance;
    }

    /**
     * Save player experience and level
     *
     * @param player the player
     */
    public final void saveExperience(Player player) {
        inventories.put(player.getUniqueId(), StoredInventory.builder().justExperience(true).exp(player.getExp()).lvl(player.getLevel()).totalXp(player.getTotalExperience()).build());
    }

    /**
     * Restores player experience
     *
     * @param player the player
     * @throws IllegalArgumentException error if player has no stored inventory
     */
    public final void restoreExperience(Player player) {
        final StoredInventory s = getStored(player);
        Validate.isTrue(s != null, "Player " + player.getName() + " does not have a stored inventory!");

        player.setTotalExperience(s.getTotalXp());
        player.setLevel(s.getLvl());
        player.setExp(player.getExp());
    }

    /**
     * Saves player inventory in full
     *
     * @param player the player
     */
    public final void saveInventory(Player player) {
        final StoredInventory s = StoredInventory.builder()

                .gameMode(player.getGameMode())

                .armorContent(player.getInventory().getArmorContents())
                .content(player.getInventory().getContents())

                .healthScaled(player.isHealthScaled())

                .remainingAir(player.getRemainingAir())
                .maximumAir(player.getMaximumAir())
                .fallDistance(player.getFallDistance())
                .fireTicks(player.getFireTicks())

                .totalXp(player.getTotalExperience())
                .lvl(player.getLevel())
                .exp(player.getExp())

                .foodLevel(player.getFoodLevel())
                .exhaustion(player.getExhaustion())
                .saturation(player.getSaturation())

                .flySpeed(player.getFlySpeed())
                .walkSpeed(player.getWalkSpeed())

                .potionEffects(player.getActivePotionEffects())

                .build();

        inventories.put(player.getUniqueId(), s);
    }

    /**
     * Restores player inventory in full.
     *
     * @param player
     * @throws IllegalArgumentException error if player has no stored inventory
     */
    public final void restore(Player player) {
        final StoredInventory s = getStored(player);
        Validate.isTrue(s != null, "Player " + player.getName() + " does not have a stored inventory!");

        player.setTotalExperience(s.getTotalXp());
        player.setExp(s.getExp());
        player.setLevel(s.getLvl());

        if (s.isJustExperience())
            return;

        player.setGameMode(s.getGameMode());

        player.getInventory().setArmorContents(s.getArmorContent());
        player.getInventory().setContents(s.getContent());

        player.setMaxHealth(s.getMaxHealth());
        player.setHealth(s.getHealth());
        player.setHealthScaled(s.isHealthScaled());

        player.setRemainingAir(s.getRemainingAir());
        player.setMaximumAir(s.getMaximumAir());
        player.setFallDistance(s.getFallDistance());
        player.setFireTicks(s.getFireTicks());

        player.setFoodLevel(s.getFoodLevel());
        player.setExhaustion(s.getExhaustion());
        player.setSaturation(s.getSaturation());

        player.setFlySpeed(s.getFlySpeed());
        player.setWalkSpeed(s.getWalkSpeed());

        // Potions
        for (final PotionEffect ef : player.getActivePotionEffects())
            player.removePotionEffect(ef.getType());

        for (final PotionEffect ef : s.getPotionEffects())
            player.addPotionEffect(ef);

        if (!player.isOnline())
            player.saveData();

        inventories.remove(player.getName());
    }

    /**
     * Removes the stored inventory data for player
     *
     * @param player the player
     * @return true if the data were found and trashed
     */
    public final boolean removeStored(Player player) {
        if (hasStored(player)) {
            inventories.remove(player.getName());

            return true;
        }

        return false;
    }

    /**
     * Does the player have stored inventory or experience?
     *
     * @param player the player
     * @return true if the player has stored inventory or experience data?
     */
    public final boolean hasStored(Player player) {
        return getStored(player) != null;
    }

    // Return the stored inventory data for player, or null
    private final StoredInventory getStored(Player pl) {
        return inventories.get(pl.getName());
    }
}

/**
 * A helper class representing all data we store for players
 */
@Builder
@Getter(AccessLevel.PROTECTED)
@Setter
class StoredInventory {

    /**
     * Do these data only store player's experience?
     */
    @Default
    private boolean justExperience = false;

    private GameMode gameMode;

    private ItemStack[] content;
    private ItemStack[] armorContent;

    private double maxHealth;
    private double health;
    private boolean healthScaled;

    private int remainingAir;
    private int maximumAir;

    private float fallDistance;
    private int fireTicks;

    private int totalXp;
    private int lvl;
    private float exp;

    private int foodLevel;
    private float exhaustion;
    private float saturation;

    private float flySpeed;
    private float walkSpeed;

    private Collection<PotionEffect> potionEffects;
}