package gregtech.api.threads;

import java.util.LinkedList;
import java.util.List;
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
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IMachineBlockUpdateable;
import gregtech.api.metatileentity.BaseMetaPipeEntity;
import gregtech.api.metatileentity.MetaPipeEntity;
import gregtech.api.metatileentity.implementations.MTEFrame;
import gregtech.common.config.Gregtech;
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

    // This class should never be initiated outside of this class!
    protected RunnableMachineUpdate(World aWorld, int posX, int posY, int posZ) {
        this.world = aWorld;
        this.initialX = posX;
        this.initialY = posY;
        this.initialZ = posZ;
        final long coords = CoordinatePacker.pack(posX, posY, posZ);
        visited.add(coords);
        tQueue.enqueue(coords);
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
            postTaskToRun(new RunnableMachineUpdate(aWorld, posX, posY, posZ));
        }
    }

    protected static void postTaskToRun(Runnable runnable) {
        initExecutorService();
        CompletableFuture<Void> f = CompletableFuture.runAsync(runnable, EXECUTOR_SERVICE);
        TASK_COUNTER.incrementAndGet();
        f.thenRun(SEMAPHORE::release);
    }

    private static void initExecutorService() {
        if (EXECUTOR_SERVICE != null) {
            return;
        }
        EXECUTOR_SERVICE = Executors.newFixedThreadPool(
            Math.max(
                1,
                (Runtime.getRuntime()
                    .availableProcessors() * 2
                    / 3)),
            THREAD_FACTORY);
    }

    public static void shutdownExecutorService() {
        if (EXECUTOR_SERVICE == null) {
            return;
        }
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
            EXECUTOR_SERVICE = null;
        }
    }

    @Override
    public void run() {
        int posX, posY, posZ;
        List<BMPEdata> adjacentCableList = new LinkedList<>();
        LongSet adjacentCableKeys = new LongOpenHashSet();
        try {
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

                // Skip propagation through pipes\cables\etc, but save some BaseMetaPipeEntity data for later
                if (Gregtech.features.speedupMachineUpdateThread) {
                    final boolean isBaseMetaPipeEntity = tTileEntity instanceof BaseMetaPipeEntity;
                    if (isBaseMetaPipeEntity) {
                        final BMPEdata bmpeData = new BMPEdata((BaseMetaPipeEntity) tTileEntity, posX, posY, posZ);
                        adjacentCableKeys.add(packedCoords);
                        adjacentCableList.add(bmpeData);
                        if (!bmpeData.isPotentialStructureBlock) continue;
                    }
                }

                // Now see if we should add the nearby blocks to the queue:
                // 1) If we've visited less than 5 blocks, then yes
                // 2) If the tile says we should recursively updated (pipes don't, machine blocks do)
                // 3) If the block at the coordinates is marked as a machine block
                if (visited.size() < 5
                    || (tTileEntity instanceof IMachineBlockUpdateable
                        && ((IMachineBlockUpdateable) tTileEntity).isMachineBlockUpdateRecursive())
                    || isMachineBlock) {
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

            // Check for connected cables-likes and create RunnableCableUpdate for them.
            // Requires a full list of visited machines to avoid missing not-yet-visited connections
            // and a full list of BMPEs to avoid false-positives (e.g. cable runs)
            for (var cable : adjacentCableList) {
                if (!cable.isMetaPipe) continue;
                for (int i = 0; i < ForgeDirection.VALID_DIRECTIONS.length; i++) {
                    final ForgeDirection side = ForgeDirection.VALID_DIRECTIONS[i];
                    if (!cable.metaPipe.isConnectedAtSide(side)) continue;
                    final long targetCoords = CoordinatePacker
                        .pack(cable.x + side.offsetX, cable.y + side.offsetY, cable.z + side.offsetZ);
                    if (adjacentCableKeys.contains(targetCoords)) continue;
                    if (visited.contains(targetCoords)) {
                        RunnableCableUpdate.setCableUpdateValues(world, cable.x, cable.y, cable.z);
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

    private static class BMPEdata {

        final int x, y, z;
        final BaseMetaPipeEntity baseMetaPipe;
        final IMetaTileEntity metaTile;
        final MetaPipeEntity metaPipe;
        final boolean isMetaPipe;
        final boolean isPotentialStructureBlock;

        BMPEdata(BaseMetaPipeEntity bmpe, int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
            baseMetaPipe = bmpe;
            metaTile = baseMetaPipe.getMetaTileEntity();
            isMetaPipe = metaTile instanceof MetaPipeEntity;
            metaPipe = isMetaPipe ? (MetaPipeEntity) metaTile : null;
            isPotentialStructureBlock = metaPipe instanceof MTEFrame;
        }
    }
}
