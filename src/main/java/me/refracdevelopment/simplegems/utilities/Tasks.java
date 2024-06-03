package me.refracdevelopment.simplegems.utilities;

import me.refracdevelopment.simplegems.SimpleGems;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class Tasks {

    public static void run(Runnable callable) {
        SimpleGems.getInstance().getPaperLib().scheduling().globalRegionalScheduler().run(callable);
    }

    public static void runAsync(Runnable callable) {
        SimpleGems.getInstance().getPaperLib().scheduling().asyncScheduler().run(callable);
    }

    public static void runAsyncTimer(Runnable callable, long delay) {
        SimpleGems.getInstance().getPaperLib().scheduling().asyncScheduler().runDelayed(callable, Duration.of(delay, TimeUnit.MICROSECONDS.toChronoUnit()));
    }

}