package gtPlusPlus.xmod.gregtech.api.metatileentity.implementations;

import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Dynamo;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
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

	/*public GT_MetaTileEntity_Hatch_DynamoBuffer(final String aName, final int aTier, final String[] aDescription,
			final ITexture[][][] aTextures) {
		super(aName, aTier, aDescription, aTextures);
	}*/

	public ITexture[] getTexturesActive(final ITexture aBaseTexture) {
		return new ITexture[]{aBaseTexture, TexturesGtBlock.OVERLAYS_ENERGY_OUT_MULTI_BUFFER[this.mTier]};
	}

	public ITexture[] getTexturesInactive(final ITexture aBaseTexture) {
		return new ITexture[]{aBaseTexture, TexturesGtBlock.OVERLAYS_ENERGY_OUT_MULTI_BUFFER[this.mTier]};
	}

	@Override
	public long getMinimumStoredEU() {
		return 0L;
	}

	@Override
	public long maxEUStore() {
		return 512L + GT_Values.V[this.mTier + 1] * 2048L;
	}

	@Override
	public MetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
		return (MetaTileEntity) new GT_MetaTileEntity_Hatch_DynamoBuffer(this.mName, this.mTier, this.mDescription, this.mTextures);
	}

	@Override
	public String[] getDescription() {
		String[] g;
		if (CORE.GTNH || (CORE.MAIN_GREGTECH_5U_EXPERIMENTAL_FORK && Utils.getGregtechVersionAsInt() >= 50932)) {
			g = new String[]{"Dynamo with internal storage and additional Amp capacity", "Capacity: "+maxEUStore()+"EU", "Voltage: "+this.maxEUOutput(), "Amperage In: 4", "Amperage Out: 4"};
			
		}
		else {
			g = new String[]{"Dynamo with internal storage and additional Amp capacity", "Stores "+maxEUStore()+"EU", "Amperage In: 4", "Amperage Out: 4", "Does not accept more than "+this.maxEUOutput()+"EU/t as input", "Large Turbines only supply 1A to this, other Multiblocks can inject more amps"};
		}		
		return g;
	}

	@Override
	public long maxAmperesIn() {
		return 4;
	}

	@Override
	public long maxAmperesOut() {
		return 4;
	}
}