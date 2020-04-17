package cash.playmc.cashevents.testgame;

import cash.playmc.cashevents.CashEvents;
import cash.playmc.cashevents.minigame.customevents.ArenaJoinEvent;
import cash.playmc.cashevents.minigame.customevents.ArenaStartEvent;
import cash.playmc.cashevents.minigame.datatypes.Arena;
import cash.playmc.cashevents.minigame.datatypes.Game;
import cash.playmc.cashevents.minigame.datatypes.GamePlayer;
import cash.playmc.cashevents.minigame.handlers.GameHandler;
import cash.playmc.cashevents.minigame.handlers.WorldHandler;
import cash.playmc.cashevents.utils.FireworkUtil;
import org.bukkit.*;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;

public class LastManStanding implements Listener {

    private static String gameName = "Last Man Standing";

    /*
    TODO

    - make players invincible during lobby time
    - no hunger/damage
    - no pvp in lobby
    - disable block breaking

     */

    public static void createLMS() {
        Game game = new Game(gameName);
        game.addWorld("world-arena-lastmanstanding-1");
        GameHandler.addGame(game);
        WorldHandler.loadWorldsForGame(game);

        Arena arena = new Arena(game, "world-arena-lastmanstanding-1");
        game.addArena(arena);
    }

    private static BukkitTask fireworks = null;

    @EventHandler
    public void onArenaJoin(ArenaJoinEvent e) {
        if (e.getArena().getGame().getGameName() == "Last Man Standing") {
            YamlConfiguration yamlConfiguration = WorldHandler.getWorldConfigFile(gameName, e.getArena().getWorldName());
            Location lobby = new Location(
                    Bukkit.getWorld(e.getArena().getSlimeWorldName()),
                    yamlConfiguration.getDouble("lobby.x"),
                    yamlConfiguration.getDouble("lobby.y"),
                    yamlConfiguration.getDouble("lobby.z"));
            e.getPlayer().teleport(lobby);
        }
    }

    private static int fireworksCountMax = 5;

    private static void givePlayerKit(Player player) {
        //prot1 iron armor with some splash potions, bow and arrow, iron sword sharp 1
        List<ItemStack> armor = new ArrayList<>();
        armor.add(new ItemStack(Material.IRON_HELMET, 1));
        armor.add(new ItemStack(Material.IRON_CHESTPLATE, 1));
        armor.add(new ItemStack(Material.IRON_LEGGINGS, 1));
        armor.add(new ItemStack(Material.IRON_BOOTS, 1));

        armor.forEach(itemStack -> {
            itemStack.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        });

        player.getEquipment().setHelmet(armor.get(0));
        player.getEquipment().setChestplate(armor.get(1));
        player.getEquipment().setLeggings(armor.get(2));
        player.getEquipment().setBoots(armor.get(3));

        Inventory inventory = player.getInventory();

        List<ItemStack> kitItems = new ArrayList<>();
        kitItems.add(new ItemStack(Material.IRON_SWORD, 1));
        kitItems.add(new ItemStack(Material.BOW, 1));
        kitItems.add(new ItemStack(new Potion(PotionType.INSTANT_DAMAGE, 1, true)
                .toItemStack(2)));
        kitItems.add(new ItemStack(new Potion(PotionType.INSTANT_HEAL, 1, false)
                .toItemStack(2)));
        kitItems.add(new ItemStack(new Potion(PotionType.SPEED, 1, false)
                .toItemStack(1)));
        kitItems.add(new ItemStack(Material.ARROW, 1));

        kitItems.get(0).addEnchantment(Enchantment.DAMAGE_ALL, 1);
        kitItems.get(1).addEnchantment(Enchantment.ARROW_INFINITE, 1);

        kitItems.forEach(kitItem -> {
            inventory.addItem(kitItem);
        });

        player.updateInventory();

    }

    @EventHandler
    public void onGameStart(ArenaStartEvent e) {
        Arena arena = e.getArena();

        arena.getPlayers().forEach(gp -> {
            Player player = gp.getPlayer();
            player.teleport(new Location(Bukkit.getWorld(arena.getSlimeWorldName()), 0, 65.5, 0));

            player.sendMessage(ChatColor.GREEN + " ----- $$$ CashEvents $$$ ----- ");
            player.sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "Game: " +
                    ChatColor.YELLOW + arena.getGame().getGameName());
            player.sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "Map: " +
                    ChatColor.YELLOW + arena.getMapName());
            player.sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "Map Author: " +
                    ChatColor.YELLOW + arena.getAuthor());
            player.sendMessage(ChatColor.GREEN + " ----- $$$ CashEvents $$$ ----- ");

            // set inventory & gamemode
            player.setGameMode(GameMode.SURVIVAL);

            // items simply for testing
            givePlayerKit(player);
        });
    }

    @EventHandler
    public void onDeath(EntityDamageByEntityEvent e) {
        Player player = (Player) e.getEntity();
        Player damager = (Player) e.getDamager();

        Arena arena = GameHandler.getArenaFromPlayer(player);
        if (arena.getState() == Arena.State.INGAME) {
            if (e.getDamage() >= player.getHealth()) {
                e.setCancelled(true);
                player.sendTitle(ChatColor.RED + "You died!", ChatColor.GRAY + "You are now a spectator");
                arena.getPlayer(player.getUniqueId()).setMode(GamePlayer.Mode.SPECTATOR);
            }

            if (arena.getPlayersByMode(GamePlayer.Mode.PLAYER).size() < 2) {
                GamePlayer winner = arena.getPlayersByMode(GamePlayer.Mode.PLAYER).get(0);

                arena.getPlayers().forEach(gp ->
                        gp.getPlayer().sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD +
                                winner.getPlayer().getName() + ChatColor.GREEN + " has won the event!"));

                fireworks = Bukkit.getScheduler().runTaskTimer(CashEvents.getPlugin(), () -> {
                    if (fireworksCountMax >= 1) {
                        fireworksCountMax--;
                        FireworkUtil.spawnFirework(winner.getPlayer().getLocation());
                    } else {
                        fireworks.cancel();
                    }
                }, 20, 1 * 20);

                //TODO give player reward money for event
                arena.end();
            }
        }
    }

}
