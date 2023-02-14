package me.refracdevelopment.simplegems.data;

public class Stat {

    private double amount;

    public double getAmount() {
        return this.amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

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