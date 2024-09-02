package gregtech.api.threads;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizon.gtnhlib.util.CoordinatePacker;

import gregtech.GTMod;
import gregtech.api.interfaces.tileentity.IMachineBlockUpdateable;
import gregtech.api.metatileentity.BaseMetaPipeEntity;
import gregtech.api.metatileentity.implementations.MTECable;
import gregtech.common.GTProxy;

public class RunnableCableUpdate extends RunnableMachineUpdate {

    protected RunnableCableUpdate(World aWorld, int posX, int posY, int posZ) {
        super(aWorld, posX, posY, posZ);
    }

    public static void setCableUpdateValues(World aWorld, int posX, int posY, int posZ) {
        if (isEnabled) {
            EXECUTOR_SERVICE.submit(new RunnableCableUpdate(aWorld, posX, posY, posZ));
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

                GTProxy.TICK_LOCK.lock();
                try {
                    // we dont want to go over cables that are in unloaded chunks
                    // keeping the lock just to make sure no CME happens
                    if (world.blockExists(posX, posY, posZ)) {
                        tTileEntity = world.getTileEntity(posX, posY, posZ);
                    } else {
                        tTileEntity = null;
                    }
                } finally {
                    GTProxy.TICK_LOCK.unlock();
                }

                // See if the block itself needs an update
                if (tTileEntity instanceof IMachineBlockUpdateable)
                    ((IMachineBlockUpdateable) tTileEntity).onMachineBlockUpdate();

                // Now see if we should add the nearby blocks to the queue:
                // only add blocks the cable is connected to
                if (tTileEntity instanceof BaseMetaPipeEntity metaPipe
                    && metaPipe.getMetaTileEntity() instanceof MTECable cable) {
                    for (int i = 0; i < ForgeDirection.VALID_DIRECTIONS.length; i++) {
                        final ForgeDirection side = ForgeDirection.VALID_DIRECTIONS[i];
                        if (cable.isConnectedAtSide(side)) {
                            final long tCoords = CoordinatePacker
                                .pack(posX + side.offsetX, posY + side.offsetY, posZ + side.offsetZ);
                            if (visited.add(tCoords)) {
                                tQueue.enqueue(tCoords);
                            }
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
