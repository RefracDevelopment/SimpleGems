package me.refracdevelopment.simplegems.data;

public class Stat {

    private long amount;

    public long getAmount() {
        return this.amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

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