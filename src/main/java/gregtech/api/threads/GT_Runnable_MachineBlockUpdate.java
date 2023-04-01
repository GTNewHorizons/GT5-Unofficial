package gregtech.api.threads;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;

import gregtech.GT_Mod;
import gregtech.api.GregTech_API;
import gregtech.api.interfaces.tileentity.IMachineBlockUpdateable;
import gregtech.common.GT_Proxy;

public class GT_Runnable_MachineBlockUpdate implements Runnable {

    // used by runner thread
    protected final ChunkCoordinates mCoords;
    protected final World world;
    protected final Set<ChunkCoordinates> visited = new HashSet<>(80);
    protected final Queue<ChunkCoordinates> tQueue = new LinkedList<>();

    // Threading
    private static final ThreadFactory THREAD_FACTORY = r -> {
        Thread thread = new Thread(r);
        thread.setName("GT_MachineBlockUpdate");
        return thread;
    };
    protected static ExecutorService EXECUTOR_SERVICE;

    // This class should never be initiated outside of this class!
    protected GT_Runnable_MachineBlockUpdate(World aWorld, ChunkCoordinates aCoords) {
        this.world = aWorld;
        this.mCoords = aCoords;
        visited.add(aCoords);
        tQueue.add(aCoords);
    }

    public static boolean isEnabled() {
        return isEnabled;
    }

    public static void setEnabled() {
        GT_Runnable_MachineBlockUpdate.isEnabled = true;
    }

    public static void setDisabled() {
        GT_Runnable_MachineBlockUpdate.isEnabled = false;
    }

    public static void setEnabled(boolean isEnabled) {
        GT_Runnable_MachineBlockUpdate.isEnabled = isEnabled;
    }

    public static boolean isCurrentThreadEnabled() {
        return perThreadEnable.get();
    }

    public static void setCurrentThreadEnabled(boolean perThreadEnable) {
        GT_Runnable_MachineBlockUpdate.perThreadEnable.set(perThreadEnable);
    }

    protected static boolean isEnabled = true;
    protected static final ThreadLocal<Boolean> perThreadEnable = ThreadLocal.withInitial(() -> true);

    public static void setMachineUpdateValues(World aWorld, ChunkCoordinates aCoords) {
        if (isEnabled() && isCurrentThreadEnabled()) {
            EXECUTOR_SERVICE.submit(new GT_Runnable_MachineBlockUpdate(aWorld, aCoords));
        }
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
            GT_Mod.GT_FML_LOGGER.info("Shutting down Machine block update executor service");
            EXECUTOR_SERVICE.shutdown(); // Disable new tasks from being submitted
            // Wait a while for existing tasks to terminate
            if (!EXECUTOR_SERVICE.awaitTermination(60, TimeUnit.SECONDS)) {
                EXECUTOR_SERVICE.shutdownNow(); // Cancel currently executing tasks
                // Wait a while for tasks to respond to being cancelled
                if (!EXECUTOR_SERVICE.awaitTermination(60, TimeUnit.SECONDS)) {
                    GT_Mod.GT_FML_LOGGER.error(
                            "Well this didn't terminated well... GT_Runnable_MachineBlockUpdate.shutdownExecutorService");
                }
            }
        } catch (InterruptedException ie) {
            GT_Mod.GT_FML_LOGGER.error("Well this interruption got interrupted...", ie);
            // (Re-)Cancel if current thread also interrupted
            EXECUTOR_SERVICE.shutdownNow();
            // Preserve interrupt status
            Thread.currentThread()
                  .interrupt();
        } catch (Exception e) {
            GT_Mod.GT_FML_LOGGER.error("Well this didn't terminated well...", e);
            // (Re-)Cancel in case
            EXECUTOR_SERVICE.shutdownNow();
        } finally {
            GT_Mod.GT_FML_LOGGER.info("Leaving... GT_Runnable_MachineBlockUpdate.shutdownExecutorService");
        }
    }

    @Override
    public void run() {
        try {
            while (!tQueue.isEmpty()) {
                final ChunkCoordinates aCoords = tQueue.poll();
                final TileEntity tTileEntity;
                final boolean isMachineBlock;

                // This might load a chunk... which might load a TileEntity... which might get added to
                // `loadedTileEntityList`... which might be in the process
                // of being iterated over during `UpdateEntities()`... which might cause a
                // ConcurrentModificationException. So, lock that shit.
                GT_Proxy.TICK_LOCK.lock();
                try {
                    tTileEntity = world.getTileEntity(aCoords.posX, aCoords.posY, aCoords.posZ);
                    isMachineBlock = GregTech_API.isMachineBlock(
                            world.getBlock(aCoords.posX, aCoords.posY, aCoords.posZ),
                            world.getBlockMetadata(aCoords.posX, aCoords.posY, aCoords.posZ));
                } finally {
                    GT_Proxy.TICK_LOCK.unlock();
                }

                // See if the block itself needs an update
                if (tTileEntity instanceof IMachineBlockUpdateable)
                    ((IMachineBlockUpdateable) tTileEntity).onMachineBlockUpdate();

                // Now see if we should add the nearby blocks to the queue:
                // 1) If we've visited less than 5 blocks, then yes
                // 2) If the tile says we should recursively updated (pipes don't, machine blocks do)
                // 3) If the block at the coordinates is marked as a machine block
                if (visited.size() < 5
                        || (tTileEntity instanceof IMachineBlockUpdateable
                                && ((IMachineBlockUpdateable) tTileEntity).isMachineBlockUpdateRecursive())
                        || isMachineBlock) {
                    ChunkCoordinates tCoords;

                    if (visited.add(tCoords = new ChunkCoordinates(aCoords.posX + 1, aCoords.posY, aCoords.posZ)))
                        tQueue.add(tCoords);
                    if (visited.add(tCoords = new ChunkCoordinates(aCoords.posX - 1, aCoords.posY, aCoords.posZ)))
                        tQueue.add(tCoords);
                    if (visited.add(tCoords = new ChunkCoordinates(aCoords.posX, aCoords.posY + 1, aCoords.posZ)))
                        tQueue.add(tCoords);
                    if (visited.add(tCoords = new ChunkCoordinates(aCoords.posX, aCoords.posY - 1, aCoords.posZ)))
                        tQueue.add(tCoords);
                    if (visited.add(tCoords = new ChunkCoordinates(aCoords.posX, aCoords.posY, aCoords.posZ + 1)))
                        tQueue.add(tCoords);
                    if (visited.add(tCoords = new ChunkCoordinates(aCoords.posX, aCoords.posY, aCoords.posZ - 1)))
                        tQueue.add(tCoords);
                }
            }
        } catch (Exception e) {
            GT_Mod.GT_FML_LOGGER.error(
                    "Well this update was broken... " + mCoords
                            + ", mWorld={"
                            + world.getProviderName()
                            + " @dimId "
                            + world.provider.dimensionId
                            + "}",
                    e);
        }
    }
}
