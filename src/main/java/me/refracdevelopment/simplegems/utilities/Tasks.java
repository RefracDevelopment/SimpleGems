package me.refracdevelopment.simplegems.utilities;

import com.tcoded.folialib.wrapper.task.WrappedTask;
import me.refracdevelopment.simplegems.SimpleGems;
import org.bukkit.entity.Entity;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class Tasks {

    public static void executeConsole(Runnable runnable) {
        Consumer<WrappedTask> consumer = task -> runnable.run();
        SimpleGems.getInstance().getFoliaLib().getScheduler().runNextTick(consumer);
    }

    public static void executeConsoleDelayed(Runnable runnable, long delay) {
        Consumer<WrappedTask> consumer = task -> runnable.run();
        SimpleGems.getInstance().getFoliaLib().getScheduler().runLater(consumer, delay);
    }

    public static void executeAtEntity(Entity entity, Runnable runnable) {
        Consumer<WrappedTask> consumer = task -> runnable.run();
        SimpleGems.getInstance().getFoliaLib().getScheduler().runAtEntity(entity, consumer);
    }

    public static void executeAtEntityDelayed(Entity entity, Runnable runnable, long delay) {
        Consumer<WrappedTask> consumer = task -> runnable.run();
        SimpleGems.getInstance().getFoliaLib().getScheduler().runAtEntityLater(entity, consumer, runnable, delay);
    }

    public static void runAsync(Runnable runnable) {
        Consumer<WrappedTask> consumer = task -> runnable.run();
        SimpleGems.getInstance().getFoliaLib().getScheduler().runAsync(consumer);
    }

    public static void runTimer(Runnable runnable, long delay, long period, TimeUnit unit) {
        Consumer<WrappedTask> consumer = task -> runnable.run();
        SimpleGems.getInstance().getFoliaLib().getScheduler().runTimer(consumer, delay, period, unit);
    }

    public static void runAsyncTimer(Runnable runnable, long delay, long period, TimeUnit unit) {
        Consumer<WrappedTask> consumer = task -> runnable.run();
        SimpleGems.getInstance().getFoliaLib().getScheduler().runTimerAsync(consumer, delay, period, unit);
    }
}