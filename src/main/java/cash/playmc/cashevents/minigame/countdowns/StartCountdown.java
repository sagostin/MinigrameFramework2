package cash.playmc.cashevents.minigame.countdowns;

import cash.playmc.cashevents.minigame.datatypes.GamePlayer;
import cash.playmc.cashevents.minigame.enums.ArenaState;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class StartCountdown extends Countdown {

  @Override
  public void onEnd() {
    if (arena.getPlayersByMode(GamePlayer.Mode.PLAYER).size() < arena.getMinPlayers()) {
      for (GamePlayer gamePlayer : this.arena.getPlayers()) {
        gamePlayer.getPlayer().sendMessage(ChatColor.RED + "Unable to start the game... We need more players to join.");
        arena.setArenaState(ArenaState.WAITING);
      }
    } else {
      for (GamePlayer gamePlayer : this.arena.getPlayers()) {
        Player player = gamePlayer.getPlayer();
        player.sendMessage(ChatColor.GREEN + " ----- $$$ CashEvents $$$ ----- ");
        player.sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "Game: " +
                ChatColor.YELLOW + arena.getGame().getGameName());
        player.sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "Map: " +
                ChatColor.YELLOW + arena.getMapName());
        player.sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "Map Author: " +
                ChatColor.YELLOW + arena.getAuthor());
        player.sendMessage(ChatColor.GREEN + " ----- $$$ CashEvents $$$ ----- ");
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
