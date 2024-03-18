package me.refracdevelopment.simplegems.api.events.impl;

import lombok.Getter;
import me.refracdevelopment.simplegems.api.events.SimpleEvent;
import org.bukkit.entity.Player;

@Getter
public class GemsWithdrawEvent extends SimpleEvent {

    private final long gems;

    public GemsWithdrawEvent(Player target, long amount) {
        super(target);
        this.gems = amount;
    }
}