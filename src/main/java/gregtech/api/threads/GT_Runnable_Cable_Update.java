package gregtech.api.threads;

import gregtech.GT_Mod;
import gregtech.api.interfaces.tileentity.IMachineBlockUpdateable;
import gregtech.api.metatileentity.BaseMetaPipeEntity;
import gregtech.api.metatileentity.implementations.GT_MetaPipeEntity_Cable;
import gregtech.common.GT_Proxy;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class GT_Runnable_Cable_Update extends GT_Runnable_MachineBlockUpdate {
    protected GT_Runnable_Cable_Update(World aWorld, ChunkCoordinates aCoords) {
        super(aWorld, aCoords);
    }

    public static void setCableUpdateValues(World aWorld, ChunkCoordinates aCoords) {
        if (isEnabled) {
            EXECUTOR_SERVICE.submit(new GT_Runnable_Cable_Update(aWorld, aCoords));
        }
    }

    @Override
    public void run() {
        try {
            while (!tQueue.isEmpty()) {
                final ChunkCoordinates aCoords = tQueue.poll();
                final TileEntity tTileEntity;

                GT_Proxy.TICK_LOCK.lock();
                try {
                    //we dont want to go over cables that are in unloaded chunks
                    //keeping the lock just to make sure no CME happens
                    if (world.blockExists(aCoords.posX, aCoords.posY, aCoords.posZ)) {
                        tTileEntity = world.getTileEntity(aCoords.posX, aCoords.posY, aCoords.posZ);
                    } else {
                        tTileEntity = null;
                    }
                } finally {
                    GT_Proxy.TICK_LOCK.unlock();
                }

                // See if the block itself needs an update
                if (tTileEntity instanceof IMachineBlockUpdateable)
                    ((IMachineBlockUpdateable) tTileEntity).onMachineBlockUpdate();

                // Now see if we should add the nearby blocks to the queue:
                // only add blocks the cable is connected to
                if (tTileEntity instanceof BaseMetaPipeEntity &&
                        ((BaseMetaPipeEntity) tTileEntity).getMetaTileEntity() instanceof GT_MetaPipeEntity_Cable)
                {
                    ChunkCoordinates tCoords;
                    for (byte i = 0;i<6;i++) {
                        if (((GT_MetaPipeEntity_Cable) ((BaseMetaPipeEntity) tTileEntity).getMetaTileEntity()).isConnectedAtSide(i)) {
                            ForgeDirection offset = ForgeDirection.getOrientation(i);
                            if (visited.add(tCoords = new ChunkCoordinates(aCoords.posX + offset.offsetX,
                                    aCoords.posY + offset.offsetY, aCoords.posZ + offset.offsetZ)))
                                tQueue.add(tCoords);
                        }
                    }
                }
            }
        } catch (Exception e) {
            GT_Mod.GT_FML_LOGGER.error(
                    "Well this update was broken... " + mCoords + ", mWorld={" + world.getProviderName() + " @dimId " + world.provider.dimensionId + "}", e);
        }
    }
}
