package me.refracdevelopment.simplegems.api.events.impl;

import lombok.Getter;
import me.refracdevelopment.simplegems.api.events.SimpleEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

@Getter
public class GemsSetEvent extends SimpleEvent implements Cancellable {

    private final long setGems;
    private boolean isCancelled;

    public GemsSetEvent(Player player, long amount) {
        super(player);
        this.setGems = amount;
        this.isCancelled = false;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean isCancelled) {
        this.isCancelled = isCancelled;
    }
}