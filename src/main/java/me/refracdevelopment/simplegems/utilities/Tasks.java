package me.refracdevelopment.simplegems.utilities;

import org.bukkit.plugin.java.JavaPlugin;

public class Tasks {

    public static void run(JavaPlugin plugin, Runnable callable) {
        plugin.getServer().getScheduler().runTask(plugin, callable);
    }

    public static void runAsync(JavaPlugin plugin, Runnable callable) {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, callable);
    }

    public static void runLater(JavaPlugin plugin, Runnable callable, long delay) {
        plugin.getServer().getScheduler().runTaskLater(plugin, callable, delay);
    }

    public static void runAsyncLater(JavaPlugin plugin, Runnable callable, long delay) {
        plugin.getServer().getScheduler().runTaskLaterAsynchronously(plugin, callable, delay);
    }

    public static void runTimer(JavaPlugin plugin, Runnable callable, long delay, long interval) {
        plugin.getServer().getScheduler().runTaskTimer(plugin, callable, delay, interval);
    }

    public static void runAsyncTimer(JavaPlugin plugin, Runnable callable, long delay, long interval) {
        plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, callable, delay, interval);
    }
}