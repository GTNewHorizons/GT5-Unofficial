package gregtech.api.threads;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizon.gtnhlib.util.CoordinatePacker;

import gregtech.GTMod;
import gregtech.api.GregTechAPI;
import gregtech.api.interfaces.tileentity.IMachineBlockUpdateable;
import gregtech.api.util.GTLog;
import it.unimi.dsi.fastutil.longs.Long2IntMap;
import it.unimi.dsi.fastutil.longs.Long2IntOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongArrayFIFOQueue;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;

public class RunnableMachineUpdate implements Runnable {

    // used by runner thread
    protected final int initialX, initialY, initialZ;
    protected final World world;
    protected final LongSet visited = new LongOpenHashSet();
    protected final LongArrayFIFOQueue tQueue = new LongArrayFIFOQueue();

    // Threading
    private static final ThreadFactory THREAD_FACTORY = r -> {
        Thread thread = new Thread(r);
        thread.setName("GT_MachineBlockUpdate");
        return thread;
    };
    private static ExecutorService EXECUTOR_SERVICE;
    private static final Semaphore SEMAPHORE = new Semaphore(Integer.MAX_VALUE);
    private static final AtomicInteger TASK_COUNTER = new AtomicInteger(0);

    private static final HashMap<World, PerWorldData> perWorldData = new HashMap<>();
    static int requests = 0;

    // This class should never be initiated outside of this class!
    protected RunnableMachineUpdate(World aWorld, int posX, int posY, int posZ) {
        this.world = aWorld;
        this.initialX = posX;
        this.initialY = posY;
        this.initialZ = posZ;
        final long coords = CoordinatePacker.pack(posX, posY, posZ);
        tQueue.enqueue(coords);
        visited.add(coords);
    }

    public static void onBeforeTickLockLocked() {
        int numTasks = TASK_COUNTER.get();
        if (numTasks > 0) {
            try {
                SEMAPHORE.acquire(numTasks);
                TASK_COUNTER.addAndGet(-numTasks);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void onAfterTickLockReleased() {
        int total = 0;
        for (PerWorldData data : perWorldData.values()) {
            for (RunnableMachineUpdate unique : data.uniqueHandlers) {
                postTaskToRun(unique);
                total++;
            }

            data.clear();
        }
        if (requests > total) {
            GTLog.out.printf("merged %d requested tasks into %d final tasks\n", requests, total);
        }
        requests = 0;
    }

    public static boolean isEnabled() {
        return isEnabled;
    }

    public static void setEnabled() {
        RunnableMachineUpdate.isEnabled = true;
    }

    public static void setDisabled() {
        RunnableMachineUpdate.isEnabled = false;
    }

    public static void setEnabled(boolean isEnabled) {
        RunnableMachineUpdate.isEnabled = isEnabled;
    }

    public static boolean isCurrentThreadEnabled() {
        return perThreadEnable.get();
    }

    public static void setCurrentThreadEnabled(boolean perThreadEnable) {
        RunnableMachineUpdate.perThreadEnable.set(perThreadEnable);
    }

    protected static boolean isEnabled = true;
    protected static final ThreadLocal<Boolean> perThreadEnable = ThreadLocal.withInitial(() -> true);

    public static void setMachineUpdateValues(World aWorld, int posX, int posY, int posZ) {
        if (isEnabled() && isCurrentThreadEnabled()) {
            PerWorldData data = perWorldData.get(aWorld);
            if (data == null) {
                data = new PerWorldData(aWorld);
                perWorldData.put(aWorld, data);
            }
            data.check(posX, posY, posZ);
            requests++;
        }
    }

    protected static void postTaskToRun(Runnable runnable) {
        CompletableFuture<Void> f = CompletableFuture.runAsync(runnable, EXECUTOR_SERVICE);
        TASK_COUNTER.incrementAndGet();
        f.thenRun(SEMAPHORE::release);
    }

    public static void initExecutorService() {
        EXECUTOR_SERVICE = Executors.newFixedThreadPool(
            Math.max(
                1,
                (Runtime.getRuntime()
                    .availableProcessors() * 2
                    / 3)),
            THREAD_FACTORY);
    }

    public static void shutdownExecutorService() {
        try {
            GTMod.GT_FML_LOGGER.info("Shutting down Machine block update executor service");
            EXECUTOR_SERVICE.shutdown(); // Disable new tasks from being submitted
            // Wait a while for existing tasks to terminate
            if (!EXECUTOR_SERVICE.awaitTermination(60, TimeUnit.SECONDS)) {
                EXECUTOR_SERVICE.shutdownNow(); // Cancel currently executing tasks
                // Wait a while for tasks to respond to being cancelled
                if (!EXECUTOR_SERVICE.awaitTermination(60, TimeUnit.SECONDS)) {
                    GTMod.GT_FML_LOGGER
                        .error("Well this didn't terminated well... RunnableMachineUpdate.shutdownExecutorService");
                }
            }
        } catch (InterruptedException ie) {
            GTMod.GT_FML_LOGGER.error("Well this interruption got interrupted...", ie);
            // (Re-)Cancel if current thread also interrupted
            EXECUTOR_SERVICE.shutdownNow();
            // Preserve interrupt status
            Thread.currentThread()
                .interrupt();
        } catch (Exception e) {
            GTMod.GT_FML_LOGGER.error("Well this didn't terminated well...", e);
            // (Re-)Cancel in case
            EXECUTOR_SERVICE.shutdownNow();
        } finally {
            GTMod.GT_FML_LOGGER.info("Leaving... RunnableMachineUpdate.shutdownExecutorService");
        }
    }

    @Override
    public void run() {
        int posX, posY, posZ;
        try {
            int initialQueueSize = tQueue.size();
            int checked = 0;
            while (!tQueue.isEmpty()) {
                final long packedCoords = tQueue.dequeueLong();
                posX = CoordinatePacker.unpackX(packedCoords);
                posY = CoordinatePacker.unpackY(packedCoords);
                posZ = CoordinatePacker.unpackZ(packedCoords);

                final TileEntity tTileEntity;
                final boolean isMachineBlock;

                // This might load a chunk... which might load a TileEntity... which might get added to
                // `loadedTileEntityList`... which might be in the process
                // of being iterated over during `UpdateEntities()`... which might cause a
                // ConcurrentModificationException. So, lock that shit.
                GTMod.proxy.TICK_LOCK.lock();
                try {
                    tTileEntity = world.getTileEntity(posX, posY, posZ);
                    isMachineBlock = GregTechAPI
                        .isMachineBlock(world.getBlock(posX, posY, posZ), world.getBlockMetadata(posX, posY, posZ));
                } finally {
                    GTMod.proxy.TICK_LOCK.unlock();
                }

                // See if the block itself needs an update
                if (tTileEntity instanceof IMachineBlockUpdateable)
                    ((IMachineBlockUpdateable) tTileEntity).onMachineBlockUpdate();

                // Now see if we should add the nearby blocks to the queue:
                // 1) If we haven't visited the initial set
                // 2) If the tile says we should recursively updated (pipes don't, machine blocks do)
                // 3) If the block at the coordinates is marked as a machine block
                if (checked < initialQueueSize
                    || (tTileEntity instanceof IMachineBlockUpdateable
                        && ((IMachineBlockUpdateable) tTileEntity).isMachineBlockUpdateRecursive())
                    || isMachineBlock) {
                    checked++;
                    for (int i = 0; i < ForgeDirection.VALID_DIRECTIONS.length; i++) {
                        final ForgeDirection side = ForgeDirection.VALID_DIRECTIONS[i];
                        final long tCoords = CoordinatePacker
                            .pack(posX + side.offsetX, posY + side.offsetY, posZ + side.offsetZ);
                        if (visited.add(tCoords)) {
                            tQueue.enqueue(tCoords);
                        }
                    }
                }
            }
        } catch (Exception e) {
            GTMod.GT_FML_LOGGER.error(
                "Well this update was broken... " + initialX
                    + ", "
                    + initialY
                    + ", "
                    + initialZ
                    + ", mWorld={"
                    + world.getProviderName()
                    + " @dimId "
                    + world.provider.dimensionId
                    + "}",
                e);
        }
    }

    static class PerWorldData {

        public PerWorldData(World world) {
            this.world = world;
        }

        World world;
        ArrayList<RunnableMachineUpdate> indexToHandler = new ArrayList<>();
        Long2IntMap handlerForLocation = new Long2IntOpenHashMap();
        HashSet<RunnableMachineUpdate> uniqueHandlers = new HashSet<>();

        public void check(int posX, int posY, int posZ) {
            final long coords = CoordinatePacker.pack(posX, posY, posZ);
            if (handlerForLocation.containsKey(coords)) return; // Already covered by another check

            int currentHandler = -1;

            if (!indexToHandler.isEmpty()) {
                for (int i = 0; i < ForgeDirection.VALID_DIRECTIONS.length; i++) {
                    final ForgeDirection side = ForgeDirection.VALID_DIRECTIONS[i];
                    final long tCoords = CoordinatePacker
                        .pack(posX + side.offsetX, posY + side.offsetY, posZ + side.offsetZ);

                    if (handlerForLocation.containsKey(tCoords)) {
                        int handlerIndex = handlerForLocation.get(tCoords);

                        if (handlerIndex != currentHandler) {
                            if (currentHandler == -1) {
                                // First adjacent handler found, just merge into it
                                currentHandler = handlerIndex;
                                RunnableMachineUpdate handler = indexToHandler.get(currentHandler);
                                handler.tQueue.enqueue(coords);
                                handler.visited.add(coords);
                                handlerForLocation.put(coords, currentHandler);
                            } else {
                                // We already found an adjacent handler, and this is a different one, so merge them
                                RunnableMachineUpdate current = indexToHandler.get(currentHandler);
                                RunnableMachineUpdate other = indexToHandler.get(handlerIndex);
                                if (current != other) {
                                    if (current.tQueue.size() <= other.tQueue.size()) {
                                        mergeHandlers(other, current, handlerIndex);
                                    } else {
                                        mergeHandlers(current, other, currentHandler);
                                        currentHandler = handlerIndex;
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (currentHandler == -1) {
                // no existing handler found
                RunnableMachineUpdate handler = new RunnableMachineUpdate(world, posX, posY, posZ);
                int newIndex = indexToHandler.size();
                uniqueHandlers.add(handler);
                indexToHandler.add(handler);
                handlerForLocation.put(coords, newIndex);
            }
        }

        private void mergeHandlers(RunnableMachineUpdate mergedHandler, RunnableMachineUpdate remainingHandler,
            int indexOfMergedHandler) {
            while (!mergedHandler.tQueue.isEmpty()) {
                long tmpCoord = mergedHandler.tQueue.dequeueLong();
                remainingHandler.tQueue.enqueue(tmpCoord);
                remainingHandler.visited.add(tmpCoord);
            }

            indexToHandler.set(indexOfMergedHandler, remainingHandler);
            uniqueHandlers.remove(mergedHandler);
        }

        public void clear() {
            indexToHandler.clear();
            uniqueHandlers.clear();
            handlerForLocation.clear();
        }

    }
}
