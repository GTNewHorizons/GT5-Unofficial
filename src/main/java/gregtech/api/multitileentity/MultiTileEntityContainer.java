package gregtech.api.multitileentity;

import static gregtech.api.util.GT_Util.setTileEntity;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import gregtech.api.multitileentity.interfaces.IMultiTileEntity;

public class MultiTileEntityContainer {

    public final TileEntity mTileEntity;
    public final MultiTileEntityBlock mBlock;
    public final byte mBlockMetaData;

    public MultiTileEntityContainer(TileEntity aTileEntity, MultiTileEntityBlock aBlock, byte aBlockMetaData) {
        mBlockMetaData = aBlockMetaData;
        mTileEntity = aTileEntity;
        mBlock = aBlock;
    }

    public void setMultiTile(World aWorld, int aX, int aY, int aZ) {
        // This is some complicated Bullshit Greg had to do to make his MTEs work right.
        ((IMultiTileEntity) mTileEntity).setShouldRefresh(false);
        setTileEntity(aWorld, aX, aY, aZ, mTileEntity, false);
        setTileEntity(aWorld, aX, aY, aZ, mBlock, mBlockMetaData, 0, false);
        ((IMultiTileEntity) mTileEntity).setShouldRefresh(true);
        setTileEntity(aWorld, aX, aY, aZ, mTileEntity, true);
    }
}
