package gregtech.api.multitileentity;

import static gregtech.api.enums.GT_Values.NBT;

import gregtech.api.enums.Materials;
import gregtech.api.multitileentity.interfaces.IMultiTileEntity;
import gregtech.api.util.GT_Util;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class MultiTileEntityClassContainer {
    public final Class<? extends TileEntity> mClass;
    public final MultiTileEntityBlock mBlock;
    public final TileEntity mCanonicalTileEntity;
    public final NBTTagCompound mParameters;
    public final byte mBlockMetaData, mStackSize;
    public final short mID;
    public final boolean mHidden;

    public MultiTileEntityClassContainer(
            int aID,
            Class<? extends TileEntity> aClass,
            int aBlockMetaData,
            int aStackSize,
            MultiTileEntityBlock aBlock,
            NBTTagCompound aParameters) {
        if (!IMultiTileEntity.class.isAssignableFrom(aClass))
            throw new IllegalArgumentException("MultiTileEntities must implement the Interface IMultiTileEntity!");
        mBlockMetaData = (byte) aBlockMetaData;
        mStackSize = (byte) aStackSize;
        mParameters = aParameters == null ? new NBTTagCompound() : aParameters;
        mHidden = mParameters.getBoolean(NBT.HIDDEN);
        mID = (short) aID;
        mBlock = aBlock;
        mClass = aClass;
        if (mParameters.hasKey(NBT.MATERIAL) && !mParameters.hasKey(NBT.COLOR))
            mParameters.setInteger(
                    NBT.COLOR,
                    GT_Util.getRGBInt(
                            Materials.get(mParameters.getString(NBT.MATERIAL)).getRGBA()));
        try {
            mCanonicalTileEntity = aClass.newInstance();
        } catch (Throwable e) {
            throw new IllegalArgumentException(e);
        }
        if (mCanonicalTileEntity instanceof IMultiTileEntity)
            ((IMultiTileEntity) mCanonicalTileEntity).initFromNBT(mParameters, mID, (short) -1);
    }
}
