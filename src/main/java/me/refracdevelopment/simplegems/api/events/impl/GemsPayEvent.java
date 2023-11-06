package me.refracdevelopment.simplegems.api.events.impl;

import lombok.Getter;
import me.refracdevelopment.simplegems.api.events.SimpleEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

@Getter
public class GemsPayEvent extends SimpleEvent implements Cancellable {

    private final long paidGems;
    private final Player target;
    private boolean isCancelled;

    public GemsPayEvent(Player player, Player target, long amount) {
        super(player);
        this.target = target;
        this.paidGems = amount;
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