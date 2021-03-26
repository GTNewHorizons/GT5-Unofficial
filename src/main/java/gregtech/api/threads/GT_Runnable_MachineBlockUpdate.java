package gregtech.api.threads;

import gregtech.GT_Mod;
import gregtech.api.GregTech_API;
import gregtech.api.interfaces.tileentity.IMachineBlockUpdateable;
import gregtech.common.GT_Proxy;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class GT_Runnable_MachineBlockUpdate implements Runnable {
    // used by runner thread
    private final ChunkCoordinates mCoords;
    private final World world;
    private final Set<ChunkCoordinates> visited = new HashSet<>(80);
    private final Queue<ChunkCoordinates> tQueue = new LinkedList<>();

    // This class should never be initiated outside of this class!
    private GT_Runnable_MachineBlockUpdate(World aWorld, ChunkCoordinates aCoords) {
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

    private static boolean isEnabled = true;

    public static void setMachineUpdateValues(World aWorld, ChunkCoordinates aCoords) {
        if (!isEnabled) {
            return;
        }
        aWorld.getTileEntity(aCoords.posX, aCoords.posY, aCoords.posZ);
        GT_Threads.getExecutorServiceMap()
                .get(GT_Runnable_MachineBlockUpdate.class)
                .submit(new GT_Runnable_MachineBlockUpdate(aWorld, aCoords));
    }

    @Override
    public void run() {
        try {
            while (!tQueue.isEmpty()) {
                final ChunkCoordinates aCoords = tQueue.poll();
                final TileEntity tTileEntity;
                final boolean isMachineBlock;
                
                // This might load a chunk... which might load a TileEntity... which might get added to `loadedTileEntityList`... which might be in the process
                // of being iterated over during `UpdateEntities()`... which might cause a ConcurrentModificationException.  So, lock that shit.
                GT_Proxy.TICK_LOCK.lock();
                try {
                    tTileEntity = world.getTileEntity(aCoords.posX, aCoords.posY, aCoords.posZ);
                    isMachineBlock = GregTech_API.isMachineBlock(world.getBlock(aCoords.posX, aCoords.posY, aCoords.posZ), world.getBlockMetadata(aCoords.posX, aCoords.posY, aCoords.posZ));
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
                    || (tTileEntity instanceof IMachineBlockUpdateable && ((IMachineBlockUpdateable) tTileEntity).isMachineBlockUpdateRecursive()) 
                    || isMachineBlock) 
                {
                    ChunkCoordinates tCoords;
                    
                    if (visited.add(tCoords = new ChunkCoordinates(aCoords.posX + 1, aCoords.posY, aCoords.posZ))) tQueue.add(tCoords);
                    if (visited.add(tCoords = new ChunkCoordinates(aCoords.posX - 1, aCoords.posY, aCoords.posZ))) tQueue.add(tCoords);
                    if (visited.add(tCoords = new ChunkCoordinates(aCoords.posX, aCoords.posY + 1, aCoords.posZ))) tQueue.add(tCoords);
                    if (visited.add(tCoords = new ChunkCoordinates(aCoords.posX, aCoords.posY - 1, aCoords.posZ))) tQueue.add(tCoords);
                    if (visited.add(tCoords = new ChunkCoordinates(aCoords.posX, aCoords.posY, aCoords.posZ + 1))) tQueue.add(tCoords);
                    if (visited.add(tCoords = new ChunkCoordinates(aCoords.posX, aCoords.posY, aCoords.posZ - 1))) tQueue.add(tCoords);
                }
            }
        } catch (Exception e) {
            GT_Mod.GT_FML_LOGGER.error(
                "Well this update was broken... " + mCoords + ", mWorld={" + world.getProviderName() + " @dimId " + world.provider.dimensionId + "}", e);
        }
    }

}
