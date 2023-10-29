package me.refracdevelopment.simplegems.api.events;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Getter
public abstract class SimpleEvent extends Event {

    private final Player player;
    private final HandlerList HANDLERS = new HandlerList();

    public SimpleEvent(Player player) {
        this.player = player;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }
}