package me.refracdevelopment.simplegems.api.events.impl;

import lombok.Getter;
import me.refracdevelopment.simplegems.api.events.SimpleEvent;
import org.bukkit.entity.Player;

@Getter
public class GemsAddEvent extends SimpleEvent {

    private final long newGems;

    public GemsAddEvent(Player player, long amount) {
        super(player);
        this.newGems = amount;
    }
}