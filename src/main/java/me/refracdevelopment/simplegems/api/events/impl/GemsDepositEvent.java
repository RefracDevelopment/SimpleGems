package me.refracdevelopment.simplegems.api.events.impl;

import lombok.Getter;
import me.refracdevelopment.simplegems.api.events.SimpleEvent;
import org.bukkit.entity.Player;

@Getter
public class GemsDepositEvent extends SimpleEvent {

    private final double gems;

    public GemsDepositEvent(Player target, double amount) {
        super(target);

        this.gems = amount;
    }
}