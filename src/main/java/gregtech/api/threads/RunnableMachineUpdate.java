package gregtech.api.threads;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizon.gtnhlib.util.CoordinatePacker;

import gregtech.GTMod;
import gregtech.api.GregTechAPI;
import gregtech.api.interfaces.tileentity.IMachineBlockUpdateable;
import gregtech.common.GTProxy;
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
    protected static ExecutorService EXECUTOR_SERVICE;

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
            EXECUTOR_SERVICE.submit(new RunnableMachineUpdate(aWorld, posX, posY, posZ));
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
            GTMod.GT_FML_LOGGER.info("Shutting down Machine block update executor service");
            EXECUTOR_SERVICE.shutdown(); // Disable new tasks from being submitted
            // Wait a while for existing tasks to terminate
            if (!EXECUTOR_SERVICE.awaitTermination(60, TimeUnit.SECONDS)) {
                EXECUTOR_SERVICE.shutdownNow(); // Cancel currently executing tasks
                // Wait a while for tasks to respond to being cancelled
                if (!EXECUTOR_SERVICE.awaitTermination(60, TimeUnit.SECONDS)) {
                    GTMod.GT_FML_LOGGER.error(
                        "Well this didn't terminated well... GT_Runnable_MachineBlockUpdate.shutdownExecutorService");
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
            GTMod.GT_FML_LOGGER.info("Leaving... GT_Runnable_MachineBlockUpdate.shutdownExecutorService");
        }
    }

    @Override
    public void run() {
        int posX, posY, posZ;
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
                GTProxy.TICK_LOCK.lock();
                try {
                    tTileEntity = world.getTileEntity(posX, posY, posZ);
                    isMachineBlock = GregTechAPI
                        .isMachineBlock(world.getBlock(posX, posY, posZ), world.getBlockMetadata(posX, posY, posZ));
                } finally {
                    GTProxy.TICK_LOCK.unlock();
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
}
