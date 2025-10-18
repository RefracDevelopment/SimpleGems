package me.refracdevelopment.simplegems.api.events.impl;

import lombok.Getter;
import me.refracdevelopment.simplegems.api.events.SimpleEvent;
import org.bukkit.entity.Player;

@Getter
public class GemsAddEvent extends SimpleEvent {

    private final double gems;
    private final Player target;

    public GemsAddEvent(Player player, Player target, double amount) {
        super(player != null ? player : target);

        this.target = target;
        this.gems = amount;
    }
}