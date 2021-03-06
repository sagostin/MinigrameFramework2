package cash.playmc.game.customevents;

import cash.playmc.game.datatypes.Arena;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ArenaEndEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private Arena arena;

    public ArenaEndEvent(Arena arena) {
        this.arena = arena;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public Arena getArena() {
        return arena;
    }

    public HandlerList getHandlers() {
        return handlers;
    }
}