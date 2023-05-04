package me.refracdevelopment.simplegems.api.events.impl;

import lombok.Getter;
import me.refracdevelopment.simplegems.api.events.SimpleEvent;
import org.bukkit.entity.Player;

@Getter
public class GemsPayEvent extends SimpleEvent {

    private final double paidGems;
    private final Player target;

    public GemsPayEvent(Player player, Player target, double amount) {
        super(player);
        this.target = target;
        this.paidGems = amount;
    }
}