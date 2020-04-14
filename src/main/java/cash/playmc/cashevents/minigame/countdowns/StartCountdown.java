package cash.playmc.cashevents.minigame.countdowns;

import cash.playmc.cashevents.minigame.datatypes.GamePlayer;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class StartCountdown extends Countdown {

  @Override
  public void onEnd() {
    if (this.arena.getPlayersByMode(GamePlayer.Mode.PLAYER).size() < this.arena.getMinPlayers()) {
      for (GamePlayer gamePlayer : this.arena.getPlayers()) {
        gamePlayer.getPlayer().sendMessage(ChatColor.RED + "Unable to start the event... We need more players to join.");
      }
    } else {
      this.arena.start();
    }
  }

  @SuppressWarnings("deprecation")
  @Override
  public void tick(int secs) {
    if ((secs % 5 == 0) || (secs < 5)) {
      for (GamePlayer gamePlayer : this.arena.getPlayersByMode(GamePlayer.Mode.PLAYER)) {
        Player player = gamePlayer.getPlayer();
        player.playSound(player.getLocation(), Sound.NOTE_PLING, 1.0F, 1.0F);

        player.sendTitle(ChatColor.GREEN + "Event starts in", ChatColor.YELLOW + "" + secs);

        player.sendMessage(ChatColor.GREEN + "Event starting in " + (secs > 1 ? "seconds!" : "second!"));
      }
    }
  }
}
