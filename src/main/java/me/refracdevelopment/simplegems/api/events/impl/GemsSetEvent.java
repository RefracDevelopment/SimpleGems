package me.refracdevelopment.simplegems.api.events.impl;

import lombok.Getter;
import me.refracdevelopment.simplegems.api.events.SimpleEvent;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

@Getter
public class GemsSetEvent extends SimpleEvent {

    private final double gems;
    private final Player target;

    public GemsSetEvent(@Nullable Player player, Player target, double amount) {
        super(player != null ? player : target);

        this.target = target;
        this.gems = amount;
    }
}