package cash.playmc.game.customevents;

import cash.playmc.game.datatypes.Arena;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ArenaStartEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private Arena arena;

    public ArenaStartEvent(Arena arena) {
        this.arena = arena;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public Arena getArena() {
        return arena;
    }
}