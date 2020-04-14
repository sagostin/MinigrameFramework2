package cash.playmc.cashevents.minigame.countdowns;

public class RestartCountdown extends Countdown {

  @Override
  public void onEnd() {
    // remove arena && unload world
    // send player back to they last were before they joined
  }

  public void tick(int secs) {
    if ((secs % 5 == 0) || (secs < 5)) {
      // send message to players they will be sent back to where they were
    }
  }
}
