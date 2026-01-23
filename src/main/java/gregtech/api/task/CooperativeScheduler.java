package gregtech.api.task;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.Type;
import gregtech.GTMod;
import gregtech.api.util.GTUtility;
import gregtech.common.config.Gregtech;

/**
 * A simple cooperative scheduler that will run expensive tasks in the background without multithreading.
 * 
 * This cooperative scheduler differs from typical schedulers in that it will try to run the oldest tasks first, without
 * considering newer tasks.
 * This is to prevent it from becoming clogged if something's generating bad tasks.
 */
public final class CooperativeScheduler {

    public static final CooperativeScheduler INSTANCE = new CooperativeScheduler();

    private final LinkedHashMap<String, CoopFuture<?>> tasks = new LinkedHashMap<>();
    private final List<CoopFuture<?>> newTasks = new ArrayList<>();

    private long start, end;

    private CooperativeScheduler() {
        FMLCommonHandler.instance()
            .bus()
            .register(this);
    }

    @SubscribeEvent
    public void tick(TickEvent.ServerTickEvent event) {
        if (event.phase != Phase.END || event.type != Type.SERVER) return;

        if (tasks.isEmpty() && newTasks.isEmpty()) return;

        start = System.nanoTime();
        end = 0;

        // we intentionally don't return to the previous ran task here
        Iterator<CoopFuture<?>> iter = tasks.values()
            .iterator();

        while (iter.hasNext()) {
            CoopFuture<?> future = iter.next();

            long start2 = 0;

            if (Gregtech.general.schedulerProfileLevel >= 2) {
                start2 = System.nanoTime();
            }

            if (!future.running) {
                iter.remove();
                continue;
            }

            try {
                future.run();
            } catch (Exception t) {
                GTMod.GT_FML_LOGGER.error(
                    "Caught error while running task; it will be cancelled (" + future.name + " " + future.task + ")",
                    t);
                future.cancel(true);
            }

            if (!future.running) {
                iter.remove();
            }

            // poll the time if the task hasn't
            if (end == 0) end = System.nanoTime();

            if (Gregtech.general.schedulerProfileLevel >= 2) {
                GTMod.GT_FML_LOGGER.info(
                    "Task " + future.name
                        + " "
                        + future.task
                        + " took "
                        + formatNumber((end - start2) / 1e3)
                        + " microseconds");
            }

            if ((end - start) > Gregtech.general.schedulerDuration) {
                break;
            }
        }

        if (Gregtech.general.schedulerProfileLevel >= 1) {
            GTMod.GT_FML_LOGGER.info(
                "Task scheduler took " + formatNumber((System.nanoTime() - start) / 1e3) + " microseconds");
        }

        for (CoopFuture<?> future : newTasks) {
            future = tasks.put(future.name, future);

            if (future != null) {
                future.cancelled = true;
                future.running = false;
            }
        }

        newTasks.clear();
    }

    public <T> CoopFuture<T> schedule(CoopTask<T> task) {
        CoopFuture<T> future = new CoopFuture<>();

        future.name = UUID.randomUUID()
            .toString();
        future.task = task;

        newTasks.add(future);

        return future;
    }

    public <T> CoopFuture<T> schedule(String name, CoopTask<T> task) {
        CoopFuture<T> future = new CoopFuture<>();

        future.name = name;
        future.task = task;

        newTasks.add(future);

        return future;
    }

    public class CoopFuture<T> implements Future<T>, ICoopTaskContext<T> {

        public String name;
        public CoopTask<T> task;

        public boolean running = true, cancelled = false;
        public T value;
        public Consumer<T> callback;

        @Override
        public boolean cancel(boolean mayInterruptIfRunning) {
            if (running) {
                running = false;
                cancelled = true;
                return true;
            } else {
                return false;
            }
        }

        @Override
        public T get() {
            if (running) {
                throw new IllegalStateException("cannot call get() if the task hasn't finished");
            }

            return value;
        }

        @Override
        public T get(long timeout, TimeUnit unit) {
            if (running) {
                throw new IllegalStateException("cannot call get() if the task hasn't finished");
            }

            return value;
        }

        @Override
        public boolean isCancelled() {
            return cancelled;
        }

        @Override
        public boolean isDone() {
            return !running;
        }

        public void run() {
            task.run(this);
        }

        @Override
        public boolean shouldYield() {
            return (end = System.nanoTime()) - start
                > (Gregtech.general.schedulerDuration / Math.min(tasks.size(), Gregtech.general.maxTaskCount));
        }

        @Override
        public void stop(T value) {
            running = false;
            this.value = value;
            if (callback != null) {
                callback.accept(value);
            }
        }

        public CoopFuture<T> onFinished(Consumer<T> callback) {
            this.callback = callback;
            return this;
        }
    }
}
