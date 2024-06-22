package gregtech.api.multitileentity;

import net.minecraft.world.World;

import gregtech.api.multitileentity.base.MultiTileEntity;
import gregtech.api.util.GT_Util;

public class MultiTileEntityContainer {

    public final MultiTileEntity tileEntity;
    public final MultiTileEntityBlock block;
    public final byte blockMetaData;

    public MultiTileEntityContainer(MultiTileEntity tileEntity, MultiTileEntityBlock block, byte blockMetaData) {
        this.blockMetaData = blockMetaData;
        this.tileEntity = tileEntity;
        this.block = block;
    }

    public void setMultiTile(World aWorld, int aX, int aY, int aZ) {
        GT_Util.setBlock(aWorld, aX, aY, aZ, block, blockMetaData, 0, false);
        GT_Util.setTileEntity(aWorld, aX, aY, aZ, tileEntity, true);
    }
}
