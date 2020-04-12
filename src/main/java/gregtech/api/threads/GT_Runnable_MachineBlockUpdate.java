package gregtech.api.threads;

import gregtech.api.GregTech_API;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.interfaces.tileentity.IMachineBlockUpdateable;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaPipeEntity_Cable;
import gregtech.api.metatileentity.implementations.GT_MetaPipeEntity_Fluid;
import gregtech.api.metatileentity.implementations.GT_MetaPipeEntity_Item;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;

import java.util.Set;
import java.util.HashSet;

public class GT_Runnable_MachineBlockUpdate implements Runnable {
    private final int mX, mY, mZ;
    private final World mWorld;
    private final Set<ChunkPosition> mVisited;

    public GT_Runnable_MachineBlockUpdate(World aWorld, int aX, int aY, int aZ) {
        mWorld = aWorld;
        mX = aX;
        mY = aY;
        mZ = aZ;
        mVisited = new HashSet<ChunkPosition>(80);
    }

    private boolean shouldRecurse(TileEntity aTileEntity, int aX, int aY, int aZ) {
        if (aTileEntity instanceof IGregTechTileEntity) {
            // Stop recursion on GregTech cables, item pipes, and fluid pipes
            IMetaTileEntity tMetaTileEntity = ((IGregTechTileEntity) aTileEntity).getMetaTileEntity();
            if ((tMetaTileEntity instanceof GT_MetaPipeEntity_Cable) ||
                (tMetaTileEntity instanceof GT_MetaPipeEntity_Fluid) ||
                (tMetaTileEntity instanceof GT_MetaPipeEntity_Item))
                return false;
        }

        return (aTileEntity instanceof IMachineBlockUpdateable) ||
            GregTech_API.isMachineBlock(mWorld.getBlock(aX, aY, aZ), mWorld.getBlockMetadata(aX, aY, aZ));
    }

    private void stepToUpdateMachine(int aX, int aY, int aZ) {
        if (!mVisited.add(new ChunkPosition(aX, aY, aZ)))
            return;

        TileEntity tTileEntity = mWorld.getTileEntity(aX, aY, aZ);
        if (tTileEntity instanceof IMachineBlockUpdateable)
            ((IMachineBlockUpdateable) tTileEntity).onMachineBlockUpdate();

        if (mVisited.size() < 5 || shouldRecurse(tTileEntity, aX, aY, aZ)) {
            stepToUpdateMachine(aX + 1, aY, aZ);
            stepToUpdateMachine(aX - 1, aY, aZ);
            stepToUpdateMachine(aX, aY + 1, aZ);
            stepToUpdateMachine(aX, aY - 1, aZ);
            stepToUpdateMachine(aX, aY, aZ + 1);
            stepToUpdateMachine(aX, aY, aZ - 1);
        }
    }

    @Override
    public void run() {
        try {
            stepToUpdateMachine(mX, mY, mZ);
        } catch (Throwable e) {/**/}
    }
}
