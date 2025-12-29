package me.refracdevelopment.simplegems.utilities;

import me.refracdevelopment.simplegems.SimpleGems;
import org.bukkit.entity.Entity;

import java.util.concurrent.TimeUnit;

public class Tasks {

    public static void run(Runnable runnable) {
        SimpleGems.getInstance().getFoliaLib().getScheduler().runNextTick(t -> runnable.run());
    }

    public static void runDelayed(Runnable runnable, long delay) {
        SimpleGems.getInstance().getFoliaLib().getScheduler().runLater(t -> runnable.run(), delay);
    }

    public static void runAtEntity(Entity entity, Runnable runnable) {
        SimpleGems.getInstance().getFoliaLib().getScheduler().runAtEntity(entity, t -> runnable.run());
    }

    public static void runAtEntityDelayed(Entity entity, Runnable runnable, long delay) {
        SimpleGems.getInstance().getFoliaLib().getScheduler().runAtEntityLater(entity, t -> runnable.run(), delay);
    }

    public static void runAsync(Runnable runnable) {
        SimpleGems.getInstance().getFoliaLib().getScheduler().runAsync(t -> runnable.run());
    }

    public static void runTimer(Runnable runnable, long delay, long period, TimeUnit unit) {
        SimpleGems.getInstance().getFoliaLib().getScheduler().runTimer(t -> runnable.run(), delay, period, unit);
    }

    public static void runAsyncTimer(Runnable runnable, long delay, long period, TimeUnit unit) {
        SimpleGems.getInstance().getFoliaLib().getScheduler().runTimerAsync(t -> runnable.run(), delay, period, unit);
    }
}