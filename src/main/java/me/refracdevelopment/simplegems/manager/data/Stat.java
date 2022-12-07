package me.refracdevelopment.simplegems.manager.data;

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