package me.refracdevelopment.simplegems.api.events;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
public abstract class SimpleEvent extends Event implements Cancellable {

    private final Player player;
    private final HandlerList HANDLERS = new HandlerList();
    private boolean isCancelled;

    public SimpleEvent(Player player) {
        this.player = player;
        this.isCancelled = false;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.isCancelled = cancelled;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }
}