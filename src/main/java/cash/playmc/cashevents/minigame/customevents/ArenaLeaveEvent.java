package cash.playmc.cashevents.minigame.customevents;

import cash.playmc.cashevents.minigame.datatypes.Arena;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ArenaLeaveEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private Arena arena;
    private Player player;

    public ArenaLeaveEvent(Arena arena, Player player) {
        this.arena = arena;
        this.player = player;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public Player getPlayer() {
        return player;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public Arena getArena() {
        return arena;
    }
}