package gtPlusPlus.xmod.gregtech.api.metatileentity.custom.power;

import static gregtech.api.enums.GT_Values.GT;

import gregtech.api.interfaces.ITexture;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.core.lib.CORE;

public abstract class GTPP_MTE_TieredMachineBlock extends MetaTileEntityCustomPower {
    /**
     * Value between [0 - 9] to describe the Tier of this Machine.
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

    public GTPP_MTE_TieredMachineBlock(int aID, String aName, String aNameRegional, int aTier, int aInvSlotCount, String aDescription, ITexture... aTextures) {
        super(aID, aName, aNameRegional, aInvSlotCount);
        mTier = (byte) Math.max(0, Math.min(aTier, 9));
        mDescriptionArray = aDescription == null ? new String[0] : new String[]{aDescription};
        mDescription = mDescriptionArray.length > 0 ? mDescriptionArray[0] : "";
        // must always be the last call!
        if (GT.isClientSide()) mTextures = getTextureSet(aTextures);
        else mTextures = null;
    }

    public GTPP_MTE_TieredMachineBlock(int aID, String aName, String aNameRegional, int aTier, int aInvSlotCount, String[] aDescription, ITexture... aTextures) {
        super(aID, aName, aNameRegional, aInvSlotCount);
        mTier = (byte) Math.max(0, Math.min(aTier, 9));
        mDescriptionArray = aDescription == null ? new String[0] : aDescription;
        mDescription = mDescriptionArray.length > 0 ? mDescriptionArray[0] : "";

        // must always be the last call!
        if (GT.isClientSide()) mTextures = getTextureSet(aTextures);
        else mTextures = null;
    }

    public GTPP_MTE_TieredMachineBlock(String aName, int aTier, int aInvSlotCount, String aDescription, ITexture[][][] aTextures) {
        super(aName, aInvSlotCount);
        mTier = (byte) aTier;
        mDescriptionArray = aDescription == null ? new String[0] : new String[]{aDescription};
        mDescription = mDescriptionArray.length > 0 ? mDescriptionArray[0] : "";
        mTextures = aTextures;
    }

    public GTPP_MTE_TieredMachineBlock(String aName, int aTier, int aInvSlotCount, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aInvSlotCount);
        mTier = (byte) aTier;
        mDescriptionArray = aDescription == null ? new String[0] : aDescription;
        mDescription = mDescriptionArray.length > 0 ? mDescriptionArray[0] : "";
        mTextures = aTextures;
    }

    @Override
    public byte getTileEntityBaseType() {
        return 12;
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

		AutoMap<String> aTooltip = new AutoMap<String>();
		String []s1 = null;
		s1 = new String[aTooltip.size()];
		int u = 0;
		for (String s : aTooltip) {
			s1[u] = s;
		}
		return s1;
    }

    /**
     * Used Client Side to get a Texture Set for this Block.
     * Called after setting the Tier and the Description so that those two are accessible.
     *
     * @param aTextures is the optional Array you can give to the Constructor.
     */
    public abstract ITexture[][][] getTextureSet(ITexture[] aTextures);
}