package gregtech.api.threads;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.GT_Mod;
import gregtech.api.interfaces.tileentity.IMachineBlockUpdateable;
import gregtech.api.metatileentity.BaseMetaPipeEntity;
import gregtech.api.metatileentity.implementations.GT_MetaPipeEntity_Cable;
import gregtech.common.GT_Proxy;

public class GT_Runnable_Cable_Update extends GT_Runnable_MachineBlockUpdate {

    protected GT_Runnable_Cable_Update(World aWorld, int posX, int posY, int posZ) {
        super(aWorld, posX, posY, posZ);
    }

    public static void setCableUpdateValues(World aWorld, int posX, int posY, int posZ) {
        if (isEnabled) {
            EXECUTOR_SERVICE.submit(new GT_Runnable_Cable_Update(aWorld, posX, posY, posZ));
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

                GT_Proxy.TICK_LOCK.lock();
                try {
                    // we dont want to go over cables that are in unloaded chunks
                    // keeping the lock just to make sure no CME happens
                    if (world.blockExists(posX, posY, posZ)) {
                        tTileEntity = world.getTileEntity(posX, posY, posZ);
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
                if (tTileEntity instanceof BaseMetaPipeEntity metaPipe
                    && metaPipe.getMetaTileEntity() instanceof GT_MetaPipeEntity_Cable cable) {
                    for (int i = 0; i < ForgeDirection.VALID_DIRECTIONS.length; i++) {
                        final ForgeDirection side = ForgeDirection.VALID_DIRECTIONS[i];
                        if (cable.isConnectedAtSide(side)) {
                            final long tCoords = asLong(posX + side.offsetX, posY + side.offsetY, posZ + side.offsetZ);
                            if (visited.add(tCoords)) {
                                tQueue.enqueue(tCoords);
                            }
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
