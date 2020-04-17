package cash.playmc.game.countdowns;

import cash.playmc.game.Minigame;
import cash.playmc.game.datatypes.Arena;
import cash.playmc.game.datatypes.GamePlayer;
import cash.playmc.game.handlers.GameHandler;
import cash.playmc.game.utils.PlayerStorageUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.Iterator;

public class EndCountdown extends Countdown {

  @Override
  public void onEnd() {
    arena.setState(Arena.State.ENDING);

    Iterator<GamePlayer> gamePlayerIterator = arena.getPlayers().iterator();
    while (gamePlayerIterator.hasNext()) {
      GamePlayer gamePlayer = gamePlayerIterator.next();
      PlayerStorageUtil.$().restore(gamePlayer.getPlayer());

      gamePlayerIterator.remove();
    }

    Bukkit.getScheduler().scheduleSyncDelayedTask(Minigame.getPlugin(),
            () -> Bukkit.unloadWorld(arena.getSlimeWorldName(), false), 20 * 2);

    GameHandler.getGameByName(arena.getGame().getGameName()).removeArena(arena.getArenaID());
  }

  public void tick(int secs) {
    if ((secs % 5 == 0) || (secs < 5)) {
      for (GamePlayer gamePlayer : arena.getPlayers()) {
        Player player = gamePlayer.getPlayer();
        player.playSound(player.getLocation(), Sound.NOTE_PLING, 1.0F, 1.0F);

        //player.sendTitle(ChatColor.RED + "Game ending in", ChatColor.DARK_RED + "" + secs);

        player.sendMessage(ChatColor.RED + "Game ending in " + ChatColor.DARK_RED + secs +
                ChatColor.RED + (secs > 1 ? " seconds!" : " second!"));
      }
    }
  }
}
