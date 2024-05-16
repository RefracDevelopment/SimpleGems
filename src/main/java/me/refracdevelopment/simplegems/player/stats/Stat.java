package me.refracdevelopment.simplegems.player.stats;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Stat {

    private double amount;

    public void incrementAmount(double amount) {
        this.amount += amount;
    }

    public void decrementAmount(double amount) {
        this.amount -= amount;
    }

    public boolean hasAmount(double amount) {
        return this.amount >= amount;
    }
}