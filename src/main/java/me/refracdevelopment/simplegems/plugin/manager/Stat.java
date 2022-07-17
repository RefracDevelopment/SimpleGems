package me.refracdevelopment.simplegems.plugin.manager;

/**
 * Author:  Zachary (Refrac) Baldwin <refracplaysmc@gmail.com>
 * Created: 2022-6-17
 */
public class Stat {

    private double stat;

    public double getStat() {
        return this.stat;
    }

    public void setStat(double stat) {
        this.stat = stat;
    }

    public void incrementStat(double stat) {
        this.stat += stat;
    }

    public void decrementStat(double stat) {
        this.stat -= stat;
    }

    public boolean hasStat(double stat) {
        return this.stat >= stat;
    }
}
