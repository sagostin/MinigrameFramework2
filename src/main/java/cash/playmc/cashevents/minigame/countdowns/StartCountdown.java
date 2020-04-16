package cash.playmc.cashevents.minigame.countdowns;

import cash.playmc.cashevents.minigame.datatypes.Arena;
import cash.playmc.cashevents.minigame.datatypes.GamePlayer;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class StartCountdown extends Countdown {

  @Override
  public void onEnd() {
    if (arena.getPlayersByMode(GamePlayer.Mode.PLAYER).size() < arena.getMinPlayers()) {
      for (GamePlayer gamePlayer : this.arena.getPlayers()) {
        gamePlayer.getPlayer().sendMessage(ChatColor.RED + "Unable to start the game... We need more players to join.");
      }

      arena.setState(Arena.State.WAITING);
    } else {
      for (GamePlayer gamePlayer : this.arena.getPlayers()) {
        Player player = gamePlayer.getPlayer();
        player.sendTitle(ChatColor.GREEN + "The game has started!", "");
      }
      this.arena.start();
    }
  }

  @SuppressWarnings("deprecation")
  @Override
  public void tick(int secs) {
    if ((secs % 5 == 0) || (secs < 5)) {
      for (GamePlayer gamePlayer : arena.getPlayersByMode(GamePlayer.Mode.PLAYER)) {
        Player player = gamePlayer.getPlayer();
        player.playSound(player.getLocation(), Sound.NOTE_PLING, 1.0F, 1.0F);

        player.sendTitle(ChatColor.GREEN + "Game starts in", ChatColor.YELLOW + "" + secs);

        player.sendMessage(ChatColor.GREEN + "Game starting in " + ChatColor.YELLOW + secs +
                ChatColor.GREEN + (secs > 1 ? " seconds!" : " second!"));
      }
    }
  }
}
