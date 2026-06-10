package me.refracdevelopment.simplegems.utilities;

import me.refracdevelopment.simplegems.SimpleGems;
import org.bukkit.Bukkit;

public class Tasks {

    public static void runAsync(Runnable runnable) {
        Bukkit.getScheduler().runTaskAsynchronously(SimpleGems.getInstance(), runnable);
    }

    public static void runAsyncTimer(Runnable runnable, long delay, long period) {
        Bukkit.getScheduler().runTaskTimerAsynchronously(SimpleGems.getInstance(), runnable, delay, period);
    }
}