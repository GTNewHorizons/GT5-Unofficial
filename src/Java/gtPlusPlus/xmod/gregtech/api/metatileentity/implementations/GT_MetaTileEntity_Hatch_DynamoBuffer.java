package gtPlusPlus.xmod.gregtech.api.metatileentity.implementations;

import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Dynamo;

import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.enums.GT_Values;
import gregtech.api.interfaces.ITexture;

public class GT_MetaTileEntity_Hatch_DynamoBuffer extends GT_MetaTileEntity_Hatch_Dynamo {
	public GT_MetaTileEntity_Hatch_DynamoBuffer(final int aID, final String aName, final String aNameRegional,
			final int aTier) {
		super(aID, aName, aNameRegional, aTier);
	}

	public GT_MetaTileEntity_Hatch_DynamoBuffer(final String aName, final int aTier, final String aDescription,
			final ITexture[][][] aTextures) {
		super(aName, aTier, aDescription, aTextures);
	}

	public GT_MetaTileEntity_Hatch_DynamoBuffer(final String aName, final int aTier, final String[] aDescription,
			final ITexture[][][] aTextures) {
		super(aName, aTier, aDescription, aTextures);
	}

	public ITexture[] getTexturesActive(final ITexture aBaseTexture) {
		return new ITexture[]{aBaseTexture, TexturesGtBlock.OVERLAYS_ENERGY_OUT_MULTI_BUFFER[this.mTier]};
	}

	public ITexture[] getTexturesInactive(final ITexture aBaseTexture) {
		return new ITexture[]{aBaseTexture, TexturesGtBlock.OVERLAYS_ENERGY_OUT_MULTI_BUFFER[this.mTier]};
	}

	public long getMinimumStoredEU() {
		return 0L;
	}

	public long maxEUStore() {
		return 512L + GT_Values.V[this.mTier + 1] * 2048L;
	}

	public MetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
		return (MetaTileEntity) new GT_MetaTileEntity_Hatch_DynamoBuffer(this.mName, this.mTier, this.mDescriptionArray, this.mTextures);
	}

	@Override
	public String[] getDescription() {
		String[] g = new String[]{"Generating electric Energy from Multiblocks", "Stores "+maxEUStore()+"EU", "Puts out up to 4 Amps"};
		return g;
	}
}