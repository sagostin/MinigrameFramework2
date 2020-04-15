package cash.playmc.cashevents.minigame.countdowns;

import cash.playmc.cashevents.minigame.datatypes.GamePlayer;
import cash.playmc.cashevents.minigame.enums.ArenaState;
import cash.playmc.cashevents.minigame.handlers.GameHandler;
import cash.playmc.cashevents.utils.PlayerStorageUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.Iterator;

public class EndCountdown extends Countdown {

  @Override
  public void onEnd() {
    arena.setArenaState(ArenaState.ENDING);

    Iterator<GamePlayer> gamePlayerIterator = arena.getPlayers().iterator();
    while (gamePlayerIterator.hasNext()) {
      GamePlayer gamePlayer = gamePlayerIterator.next();
      PlayerStorageUtil.$().restore(gamePlayer.getPlayer());

      gamePlayerIterator.remove();
    }

    Bukkit.unloadWorld(arena.getWorldName(), false);

    GameHandler.getArenaFromArenaUUID(arena.getArenaID()).getGame().removeArena(arena.getArenaID());
  }

  public void tick(int secs) {
    if ((secs % 5 == 0) || (secs < 5)) {
      for (GamePlayer gamePlayer : arena.getPlayersByMode(GamePlayer.Mode.PLAYER)) {
        Player player = gamePlayer.getPlayer();
        player.playSound(player.getLocation(), Sound.NOTE_PLING, 1.0F, 1.0F);

        player.sendTitle(ChatColor.RED + "Game ending in", ChatColor.DARK_RED + "" + secs);

        player.sendMessage(ChatColor.RED + "Game ending in " + ChatColor.DARK_RED + secs +
                ChatColor.RED + (secs > 1 ? " seconds!" : " second!"));
      }
    }
  }
}
