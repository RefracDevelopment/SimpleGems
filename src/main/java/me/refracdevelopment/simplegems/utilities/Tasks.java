package me.refracdevelopment.simplegems.utilities;

import me.refracdevelopment.simplegems.SimpleGems;
import org.bukkit.entity.Entity;
import space.arim.morepaperlib.scheduling.ScheduledTask;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class Tasks {

    public static void executeAtEntity(Entity entity, Runnable runnable) {
        Consumer<ScheduledTask> consumer = task -> runnable.run();
        SimpleGems.getInstance().getPaperLib().scheduling().entitySpecificScheduler(entity).run(consumer, runnable);
    }

    public static void runAtEntityDelayed(Entity entity, Runnable runnable, long delay) {
        Consumer<ScheduledTask> consumer = task -> runnable.run();
        SimpleGems.getInstance().getPaperLib().scheduling().entitySpecificScheduler(entity).runDelayed(consumer, runnable, delay);
    }

    public static void runAsync(Runnable runnable) {
        SimpleGems.getInstance().getPaperLib().scheduling().asyncScheduler().run(runnable);
    }

    public static void runAsyncDelayed(Runnable runnable, long delay, TimeUnit unit) {
        SimpleGems.getInstance().getPaperLib().scheduling().asyncScheduler().runDelayed(runnable, Duration.of(delay, unit.toChronoUnit()));
    }

    public static void runAsyncRate(Runnable runnable, long delay, long period, TimeUnit unit) {
        SimpleGems.getInstance().getPaperLib().scheduling().asyncScheduler().runAtFixedRate(runnable, Duration.of(delay, unit.toChronoUnit()), Duration.of(period, unit.toChronoUnit()));
    }
}