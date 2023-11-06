package me.refracdevelopment.simplegems.api.events.impl;

import lombok.Getter;
import me.refracdevelopment.simplegems.api.events.SimpleEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

@Getter
public class GemsAddEvent extends SimpleEvent implements Cancellable {

    private final long newGems;
    private boolean isCancelled;

    public GemsAddEvent(Player player, long amount) {
        super(player);
        this.newGems = amount;
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