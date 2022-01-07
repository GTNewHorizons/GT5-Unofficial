package gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base;

import static gregtech.api.enums.GT_Values.GT;

import gregtech.api.interfaces.ITexture;
import gregtech.api.metatileentity.MetaTileEntity;

import gtPlusPlus.core.lib.CORE;

public abstract class GregtechMetaTileEntity extends MetaTileEntity {
	/**
	 * Value between [0 - 9] to describe the Tier of this Machine.
	 */
	protected byte mTier;

	/**
	 * A simple Description.
	 */
	protected final String mDescription;

	/**
	 * Contains all Textures used by this Block.
	 */
	public final ITexture[][][] mTextures;

	public GregtechMetaTileEntity(final int aID, final String aName, final String aNameRegional, final int aTier,
			final int aInvSlotCount, final String aDescription, final ITexture... aTextures) {
		super(aID, aName, aNameRegional, aInvSlotCount);
		this.mTier = (byte) Math.max(0, Math.min(aTier, 9));
		this.mDescription = aDescription;

		// must always be the last call!
		if (GT.isClientSide()) {
			this.mTextures = this.getTextureSet(aTextures);
		} else {
			this.mTextures = null;
		}
	}

	public GregtechMetaTileEntity(final String aName, final int aTier, final int aInvSlotCount,
			final String aDescription, final ITexture[][][] aTextures) {
		super(aName, aInvSlotCount);
		this.mTier = (byte) aTier;
		this.mDescription = aDescription;
		this.mTextures = aTextures;

	}

	@Override
	public byte getTileEntityBaseType() {
		return (byte) (Math.min(3, this.mTier <= 0 ? 0 : 1 + ((this.mTier - 1) / 4)));
	}

	@Override
	public long getInputTier() {
		return this.mTier;
	}

	@Override
	public long getOutputTier() {
		return this.mTier;
	}

	@Override
	public String[] getDescription() {
		return new String[] { this.mDescription };
	}

	/**
	 * Used Client Side to get a Texture Set for this Block. Called after
	 * setting the Tier and the Description so that those two are accessible.
	 * 
	 * @param aTextures
	 *            is the optional Array you can give to the Constructor.
	 */
	public abstract ITexture[][][] getTextureSet(ITexture[] aTextures);
}