package me.refracdevelopment.simplegems.api.events.impl;

import lombok.Getter;
import me.refracdevelopment.simplegems.api.events.SimpleEvent;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

@Getter
public class GemsAddEvent extends SimpleEvent {

    private final double gems;
    private final Player target;

    public GemsAddEvent(@Nullable Player player, Player target, double amount) {
        super(player);

        this.target = target;
        this.gems = amount;
    }
}