package me.refracdevelopment.simplegems.utilities;

import com.tcoded.folialib.wrapper.task.WrappedTask;
import me.refracdevelopment.simplegems.SimpleGems;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class Tasks {

    public static void run(Consumer<WrappedTask> callable) {
        SimpleGems.getInstance().getFoliaLib().getImpl().runNextTick(callable);
    }

    public static void runAsync(Consumer<WrappedTask> callable) {
        SimpleGems.getInstance().getFoliaLib().getImpl().runAsync(callable);
    }

    public static void runLater(Consumer<WrappedTask> callable, long delay, TimeUnit timeUnit) {
        SimpleGems.getInstance().getFoliaLib().getImpl().runLater(callable, delay, timeUnit);
    }

    public static void runAsyncLater(Consumer<WrappedTask> callable, long delay, TimeUnit timeUnit) {
        SimpleGems.getInstance().getFoliaLib().getImpl().runLaterAsync(callable, delay, timeUnit);
    }

    public static void runTimer(Consumer<WrappedTask> callable, long delay, long interval, TimeUnit timeUnit) {
        SimpleGems.getInstance().getFoliaLib().getImpl().runTimer(callable, delay, interval, timeUnit);
    }

    public static void runAsyncTimer(Consumer<WrappedTask> callable, long delay, long interval, TimeUnit timeUnit) {
        SimpleGems.getInstance().getFoliaLib().getImpl().runTimerAsync(callable, delay, interval, timeUnit);
    }
}