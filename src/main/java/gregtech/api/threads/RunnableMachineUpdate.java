package gregtech.api.threads;

import java.util.HashMap;
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
import it.unimi.dsi.fastutil.longs.LongArrayFIFOQueue;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;

public class RunnableMachineUpdate implements Runnable {

    // used by runner thread
    protected final int initialX, initialY, initialZ;
    protected final World world;
    protected final LongSet visited = new LongOpenHashSet();
    protected final LongArrayFIFOQueue tQueue = new LongArrayFIFOQueue();
    private final static HashMap<World, RunnableMachineUpdate> perWorldHandler = new HashMap<>();

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
            RunnableMachineUpdate handler = perWorldHandler.get(aWorld);
            if(handler == null){
                handler = new RunnableMachineUpdate(aWorld, posX, posY, posZ);
                perWorldHandler.put(aWorld, handler);
            }
            else {
                final long coords = CoordinatePacker.pack(posX, posY, posZ);
                handler.tQueue.enqueue(coords);
                handler.visited.add(coords);
            }
        }
    }

    public static void endTick() {
        for(RunnableMachineUpdate handler : perWorldHandler.values())
            handler.run();
        perWorldHandler.clear();
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

                tTileEntity = world.getTileEntity(posX, posY, posZ);
                isMachineBlock = GregTechAPI
                    .isMachineBlock(world.getBlock(posX, posY, posZ), world.getBlockMetadata(posX, posY, posZ));


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
}
