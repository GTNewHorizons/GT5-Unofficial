package gregtech.api.multitileentity;

import gregtech.api.enums.Materials;
import gregtech.api.multitileentity.base.BaseMultiTileEntity;
import gregtech.api.util.GT_Util;
import net.minecraft.block.material.Material;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Tuple;

import java.lang.ref.WeakReference;

import static gregtech.api.enums.GT_Values.NBT;

public class MultiTileEntityClassContainer {
    private final WeakReference<MultiTileEntityRegistry> mRegistry;
    private String mLocalized;
    private String mCategoryName;

    public final short mID;
    public Class<? extends BaseMultiTileEntity> mClass;
    public MultiTileEntityBlock mBlock;
    public BaseMultiTileEntity mCanonicalTileEntity;
    public NBTTagCompound mParameters;

    // These have defaults
    public byte mBlockMetaData = 1;
    public byte mStackSize = 64;
    public boolean mHidden = false;


    public MultiTileEntityClassContainer(MultiTileEntityRegistry aRegistry, int aID, Class<? extends BaseMultiTileEntity> aClass) {
        /* Start the Builder */
        mRegistry = new WeakReference<>(aRegistry);
        mID = (short) aID;
        mClass = aClass;
        mParameters = new NBTTagCompound();
    }

    public boolean register() {
        /* End and register the Builder with the registry */
        final MultiTileEntityRegistry registry = mRegistry.get();

        if (mParameters.hasKey(NBT.MATERIAL) && !mParameters.hasKey(NBT.COLOR))
            mParameters.setInteger(NBT.COLOR, GT_Util.getRGBInt(Materials.get(mParameters.getString(NBT.MATERIAL)).getRGBA()));

        try {mCanonicalTileEntity = mClass.newInstance();} catch (Throwable e) {throw new IllegalArgumentException(e);}
        mCanonicalTileEntity.initFromNBT(mParameters, mID, (short) -1);

        return registry != null && registry.add(this.mLocalized, this.mCategoryName, this) != null;
    }

    public MultiTileEntityClassContainer name(String aName) {
        mLocalized = aName;
        return this;
    }

    public MultiTileEntityClassContainer category(String aCategoryName) {
        mCategoryName = aCategoryName;
        return this;
    }

    public MultiTileEntityClassContainer meta(int aMeta) {
        mBlockMetaData = (byte) aMeta;
        return this;
    }

    public MultiTileEntityClassContainer stackSize(int aStackSize) {
        mStackSize = (byte) aStackSize;
        return this;
    }

    public MultiTileEntityClassContainer hide() {
        mHidden = true;
        return this;
    }

    public MultiTileEntityClassContainer setBlock(MultiTileEntityBlock aBlock) {
        mBlock = aBlock;
        return this;
    }

    /* These methods are builder methods for commonly used NBT tags */

    public MultiTileEntityClassContainer material(Material aMaterial) {
        mParameters.setString(NBT.MATERIAL, aMaterial.toString());
        return this;
    }

    public MultiTileEntityClassContainer texture(String aTexture) {
        mParameters.setString(NBT.TEXTURE, aTexture);
        return this;
    }

    public MultiTileEntityClassContainer tankCapacity(Long aCapacity) {
        mParameters.setLong(NBT.TANK_CAPACITY, aCapacity);
        return this;
    }

    public MultiTileEntityClassContainer setNBT(Tuple... aTags) {
        /* Merge in arbitrary NBT tuples of (key, value).  Useful for anything for which a custom method has not yet been exposed */
        mParameters = GT_Util.fuseNBT(mParameters, GT_Util.makeNBT(aTags));
        return this;
    }

}
