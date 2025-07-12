package gregtech.api.threads;

import java.util.HashMap;

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

    private final static HashMap<World, RunnableCableUpdate> perWorldHandler = new HashMap<>();

    public static void setCableUpdateValues(World aWorld, int posX, int posY, int posZ) {
        if (isEnabled) {
            RunnableCableUpdate handler = perWorldHandler.get(aWorld);
            if (handler == null) {
                handler = new RunnableCableUpdate(aWorld, posX, posY, posZ);
                perWorldHandler.put(aWorld, handler);
            } else {
                final long coords = CoordinatePacker.pack(posX, posY, posZ);
                handler.tQueue.enqueue(coords);
                handler.visited.add(coords);
            }
        }
    }

    public static void endTick() {
        for (RunnableCableUpdate handler : perWorldHandler.values()) handler.run();
        perWorldHandler.clear();
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

                // we don't want to go over cables that are in unloaded chunks
                // keeping the lock just to make sure no CME happens
                if (world.blockExists(posX, posY, posZ)) {
                    tTileEntity = world.getTileEntity(posX, posY, posZ);
                } else {
                    tTileEntity = null;
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
