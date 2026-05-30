package gregtech.api.threads;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizon.gtnhlib.util.CoordinatePacker;

import gregtech.GTMod;
import gregtech.api.interfaces.tileentity.IMachineBlockUpdateable;
import gregtech.api.metatileentity.BaseMetaPipeEntity;
import gregtech.api.metatileentity.implementations.MTECable;

public class RunnableCableUpdate extends RunnableMachineUpdate {

    protected RunnableCableUpdate(World aWorld, int posX, int posY, int posZ) {
        super(aWorld, posX, posY, posZ);
    }

    public static void setCableUpdateValues(World aWorld, int posX, int posY, int posZ) {
        if (isEnabled) {
            postTaskToRun(new RunnableCableUpdate(aWorld, posX, posY, posZ));
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

                // Check chunk availability without holding TICK_LOCK so the server thread can
                // create the chunk map snapshot if needed (same fix as RunnableMachineUpdate)
                if (!world.blockExists(posX, posY, posZ)) {
                    continue;
                }

                GTMod.proxy.TICK_LOCK.lock();
                try {
                    tTileEntity = world.getTileEntity(posX, posY, posZ);
                } finally {
                    GTMod.proxy.TICK_LOCK.unlock();
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
