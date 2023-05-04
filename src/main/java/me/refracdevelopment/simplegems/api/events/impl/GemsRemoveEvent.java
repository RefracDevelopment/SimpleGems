package me.refracdevelopment.simplegems.api.events.impl;

import lombok.Getter;
import me.refracdevelopment.simplegems.api.events.SimpleEvent;
import org.bukkit.entity.Player;

@Getter
public class GemsRemoveEvent extends SimpleEvent {

    private final double lossGems;

    public GemsRemoveEvent(Player player, double amount) {
        super(player);
        this.lossGems = amount;
    }
}