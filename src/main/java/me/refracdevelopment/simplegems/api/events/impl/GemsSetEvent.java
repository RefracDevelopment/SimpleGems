package me.refracdevelopment.simplegems.api.events.impl;

import lombok.Getter;
import me.refracdevelopment.simplegems.api.events.SimpleEvent;
import org.bukkit.entity.Player;

@Getter
public class GemsSetEvent extends SimpleEvent {

    private final long gems;

    public GemsSetEvent(Player player, long amount) {
        super(player);
        this.gems = amount;
    }
}