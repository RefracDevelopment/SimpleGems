package me.refracdevelopment.simplegems.api.events.impl;

import lombok.Getter;
import me.refracdevelopment.simplegems.api.events.SimpleEvent;
import org.bukkit.entity.Player;

@Getter
public class GemsSetEvent extends SimpleEvent {

    private final double setGems;

    public GemsSetEvent(Player player, double amount) {
        super(player);
        this.setGems = amount;
    }
}