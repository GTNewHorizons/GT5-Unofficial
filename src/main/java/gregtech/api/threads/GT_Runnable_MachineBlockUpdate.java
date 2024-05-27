package gregtech.api.threads;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import gregtech.GT_Mod;
import gregtech.api.GregTech_API;
import gregtech.api.interfaces.tileentity.IMachineBlockUpdateable;
import gregtech.common.GT_Proxy;
import it.unimi.dsi.fastutil.longs.LongArrayFIFOQueue;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class GT_Runnable_MachineBlockUpdate implements Runnable {

    // Borrowed from Angelica until moving to GTNHLib or something
    final static int SIZE_BITS_X = 26; // 1 + log2(MathHelper.roundUpToPowerOfTwo(30000000), RoundingMode.UNNECESSARY);
    final static int SIZE_BITS_Z = SIZE_BITS_X;
    final static int SIZE_BITS_Y = 64 - SIZE_BITS_X - SIZE_BITS_Z;
    final static long BITS_X = (1L << SIZE_BITS_X) - 1L;
    final static long BITS_Y = (1L << SIZE_BITS_Y) - 1L;
    final static long BITS_Z = (1L << SIZE_BITS_Z) - 1L;
    final static int BIT_SHIFT_Z = SIZE_BITS_Y;
    final static int BIT_SHIFT_X = SIZE_BITS_Y + SIZE_BITS_Z;

    static long asLong(int x, int y, int z) {
        long l = 0L;
        l |= ((long) x & BITS_X) << BIT_SHIFT_X;
        l |= ((long) y & BITS_Y) << 0;
        l |= ((long) z & BITS_Z) << BIT_SHIFT_Z;
        return l;
    }

    public static int unpackLongX(long packedPos) {
        return (int) (packedPos << 64 - BIT_SHIFT_X - SIZE_BITS_X >> 64 - SIZE_BITS_X);
    }

    public static int unpackLongY(long packedPos) {
        return (int) (packedPos << 64 - SIZE_BITS_Y >> 64 - SIZE_BITS_Y);
    }

    public static int unpackLongZ(long packedPos) {
        return (int) (packedPos << 64 - BIT_SHIFT_Z - SIZE_BITS_Z >> 64 - SIZE_BITS_Z);
    }

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
    protected GT_Runnable_MachineBlockUpdate(World aWorld, int posX, int posY, int posZ) {
        this.world = aWorld;
        this.initialX = posX;
        this.initialY = posY;
        this.initialZ = posZ;
        final long coords = asLong(posX, posY, posZ);
        visited.add(coords);
        tQueue.enqueue(coords);
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

    public static void setMachineUpdateValues(World aWorld, int posX, int posY, int posZ) {
        if (isEnabled() && isCurrentThreadEnabled()) {
            EXECUTOR_SERVICE.submit(new GT_Runnable_MachineBlockUpdate(aWorld, posX, posY, posZ));
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
        int posX, posY, posZ;
        try {
            while (!tQueue.isEmpty()) {
                final long packedCoords = tQueue.dequeueLong();
                posX = unpackLongX(packedCoords);
                posY = unpackLongY(packedCoords);
                posZ = unpackLongZ(packedCoords);

                final TileEntity tTileEntity;
                final boolean isMachineBlock;

                // This might load a chunk... which might load a TileEntity... which might get added to
                // `loadedTileEntityList`... which might be in the process
                // of being iterated over during `UpdateEntities()`... which might cause a
                // ConcurrentModificationException. So, lock that shit.
                GT_Proxy.TICK_LOCK.lock();
                try {
                    tTileEntity = world.getTileEntity(posX, posY, posZ);
                    isMachineBlock = GregTech_API
                        .isMachineBlock(world.getBlock(posX, posY, posZ), world.getBlockMetadata(posX, posY, posZ));
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
                    for (int i = 0; i < ForgeDirection.VALID_DIRECTIONS.length; i++) {
                        final ForgeDirection side = ForgeDirection.VALID_DIRECTIONS[i];
                        final long tCoords = asLong(posX + side.offsetX, posY + side.offsetY, posZ + side.offsetZ);
                        if (visited.add(tCoords)) {
                            tQueue.enqueue(tCoords);
                        }
                    }
                }
            }
        } catch (Exception e) {
            GT_Mod.GT_FML_LOGGER.error(
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
