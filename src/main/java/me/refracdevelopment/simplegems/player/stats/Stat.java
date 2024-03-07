package me.refracdevelopment.simplegems.player.stats;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Stat {

    private long amount;

    public void incrementAmount(long amount) {
        this.amount += amount;
    }

    public void decrementAmount(long amount) {
        this.amount -= amount;
    }

    public boolean hasAmount(long amount) {
        return this.amount >= amount;
    }
}