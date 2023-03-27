package gregtech.api.multitileentity;

import static gregtech.api.enums.GT_Values.NBT;

import java.lang.ref.WeakReference;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Tuple;

import gregtech.api.enums.Materials;
import gregtech.api.multitileentity.base.MultiTileEntity;
import gregtech.api.multitileentity.multiblock.casing.FunctionalCasing;
import gregtech.api.multitileentity.multiblock.casing.UpgradeCasing;
import gregtech.api.util.GT_Util;
import gregtech.common.tileentities.casings.upgrade.InventoryUpgrade;

public class MultiTileEntityClassContainer {

    private final WeakReference<MultiTileEntityRegistry> mRegistry;
    private String mLocalized;
    private String mCategoryName;

    public final short mID;
    public Class<? extends MultiTileEntity> mClass;
    public MultiTileEntityBlock mBlock;
    public MultiTileEntity mCanonicalTileEntity;
    public NBTTagCompound mParameters;

    // These have defaults
    public byte mBlockMetaData = 1;
    public byte mStackSize = 64;
    public boolean mHidden = false;

    public MultiTileEntityClassContainer(MultiTileEntityRegistry aRegistry, int aID,
            Class<? extends MultiTileEntity> aClass) {
        /* Start the Builder */
        mRegistry = new WeakReference<>(aRegistry);
        mID = (short) aID;
        mClass = aClass;
        mParameters = new NBTTagCompound();
    }

    public boolean register() {
        /* End and register the Builder with the registry */
        final MultiTileEntityRegistry registry = mRegistry.get();

        if (mParameters.hasKey(NBT.MATERIAL) && !mParameters.hasKey(NBT.COLOR)) mParameters
                .setInteger(NBT.COLOR, GT_Util.getRGBInt(Materials.get(mParameters.getString(NBT.MATERIAL)).getRGBA()));

        try {
            mCanonicalTileEntity = mClass.newInstance();
        } catch (Throwable e) {
            throw new IllegalArgumentException(e);
        }
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

    // Need a base texture for the MTE machine, and then a separate texture set for the machine/active overlays

    public MultiTileEntityClassContainer material(Materials aMaterial) {
        // Sets the material, and the color from the material, if not already set
        mParameters.setString(NBT.MATERIAL, aMaterial.toString());
        if (!mParameters.hasKey(NBT.COLOR)) mParameters.setInteger(NBT.COLOR, GT_Util.getRGBInt(aMaterial.getRGBA()));
        return this;
    }

    public MultiTileEntityClassContainer color(int aRPG) {
        mParameters.setInteger(NBT.COLOR, aRPG);
        return this;
    }

    public MultiTileEntityClassContainer color(short[] aRPGA) {
        mParameters.setInteger(NBT.COLOR, GT_Util.getRGBInt(aRPGA));
        return this;
    }

    public MultiTileEntityClassContainer texture(String aTexture) {
        mParameters.setString(NBT.TEXTURE, aTexture);
        return this;
    }

    public MultiTileEntityClassContainer inputInventorySize(int aSize) {
        mParameters.setInteger(NBT.INV_INPUT_SIZE, aSize);
        return this;
    }

    public MultiTileEntityClassContainer outputInventorySize(int aSize) {
        mParameters.setInteger(NBT.INV_OUTPUT_SIZE, aSize);
        return this;
    }

    public MultiTileEntityClassContainer tankCapacity(Long aCapacity) {
        mParameters.setLong(NBT.TANK_CAPACITY, aCapacity);
        return this;
    }

    public MultiTileEntityClassContainer tier(int aTier) {
        verifyDescendentOfMultiple(true, UpgradeCasing.class, FunctionalCasing.class);
        mParameters.setInteger(NBT.TIER, aTier);
        return this;
    }

    public MultiTileEntityClassContainer upgradeInventorySize(int aSize) {
        verifyDescendentOf(InventoryUpgrade.class);

        mParameters.setInteger(NBT.UPGRADE_INVENTORY_SIZE, aSize);
        return this;
    }

    @SuppressWarnings("unused")
    public MultiTileEntityClassContainer setNBT(String key, Object val) {
        return setNBT(new Tuple(key, val));
    }

    public MultiTileEntityClassContainer setNBT(Tuple... aTags) {
        /*
         * Merge in arbitrary NBT tuples of (key, value). Useful for anything for which a custom method has not yet been
         * exposed
         */
        mParameters = GT_Util.fuseNBT(mParameters, GT_Util.makeNBT(aTags));
        return this;
    }

    private void verifyDescendentOf(Class<?> cls) {
        // Check if cls is extended by mClass
        if (!cls.isAssignableFrom(mClass)) {
            throw new IllegalArgumentException(
                    "Expected a descendent of " + cls.getName() + " got " + mClass.getName() + " instead.");
        }
    }

    private void verifyDescendentOfMultiple(boolean onlyOne, Class<?>... classes) {
        boolean atLeastOne = false;
        String classNames = "";
        for (Class<?> cls : classes) {
            classNames += cls.getName() + " ";
            if (!onlyOne) {
                verifyDescendentOf(cls);
                atLeastOne = true;
            } else {
                if (cls.isAssignableFrom(mClass)) {
                    atLeastOne = true;
                }
            }
        }

        if (!atLeastOne) {
            throw new IllegalArgumentException(
                    "Expected a descendent of any of these " + classNames + " got " + mClass.getName() + " instead.");
        }
    }
}
