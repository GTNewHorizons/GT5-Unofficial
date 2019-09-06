package gregtech.api.metatileentity.implementations;

import gregtech.api.interfaces.ITexture;
import gregtech.api.metatileentity.MetaTileEntity;

import static gregtech.api.enums.GT_Values.GT;

public abstract class GT_MetaTileEntity_TieredMachineBlock extends MetaTileEntity {
    /**
     * Value between [0 - 9] to describe the Tier of this Machine.
     * PLZ [0-15] works - READ! GT_Values class.
     */
    public final byte mTier;

    @Deprecated
    public final String mDescription;

    /**
     * A simple Description.
     */
    public final String[] mDescriptionArray;

    /**
     * Contains all Textures used by this Block.
     */
    public final ITexture[][][] mTextures;

    public GT_MetaTileEntity_TieredMachineBlock(int aID, String aName, String aNameRegional, int aTier, int aInvSlotCount, String aDescription, ITexture... aTextures) {
        super(aID, aName, aNameRegional, aInvSlotCount);
        mTier = (byte) Math.max(0, Math.min(aTier, 15));
        mDescriptionArray = aDescription == null ? new String[0] : new String[]{aDescription};
        mDescription = mDescriptionArray.length > 0 ? mDescriptionArray[0] : "";
        // must always be the last call!
        if (GT.isClientSide()) mTextures = getTextureSet(aTextures);
        else mTextures = null;
    }

    public GT_MetaTileEntity_TieredMachineBlock(int aID, String aName, String aNameRegional, int aTier, int aInvSlotCount, String[] aDescription, ITexture... aTextures) {
        super(aID, aName, aNameRegional, aInvSlotCount);
        mTier = (byte) Math.max(0, Math.min(aTier, 15));
        mDescriptionArray = aDescription == null ? new String[0] : aDescription;
        mDescription = mDescriptionArray.length > 0 ? mDescriptionArray[0] : "";

        // must always be the last call!
        if (GT.isClientSide()) mTextures = getTextureSet(aTextures);
        else mTextures = null;
    }

    public GT_MetaTileEntity_TieredMachineBlock(String aName, int aTier, int aInvSlotCount, String aDescription, ITexture[][][] aTextures) {
        super(aName, aInvSlotCount);
        mTier = (byte) aTier;
        mDescriptionArray = aDescription == null ? new String[0] : new String[]{aDescription};
        mDescription = mDescriptionArray.length > 0 ? mDescriptionArray[0] : "";
        mTextures = aTextures;
    }

    public GT_MetaTileEntity_TieredMachineBlock(String aName, int aTier, int aInvSlotCount, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aInvSlotCount);
        mTier = (byte) aTier;
        mDescriptionArray = aDescription == null ? new String[0] : aDescription;
        mDescription = mDescriptionArray.length > 0 ? mDescriptionArray[0] : "";
        mTextures = aTextures;
    }

    @Override
    public byte getTileEntityBaseType() {
        return (byte) (Math.min(3, mTier <= 0 ? 0 : 1 + ((mTier - 1) / 4)));
    }

    @Override
    public long getInputTier() {
        return mTier;
    }

    @Override
    public long getOutputTier() {
        return mTier;
    }

    @Override
    public String[] getDescription() {
        return mDescriptionArray;
    }

    /**
     * Used Client Side to get a Texture Set for this Block.
     * Called after setting the Tier and the Description so that those two are accessible.
     *
     * @param aTextures is the optional Array you can give to the Constructor.
     */
    public abstract ITexture[][][] getTextureSet(ITexture[] aTextures);
}
