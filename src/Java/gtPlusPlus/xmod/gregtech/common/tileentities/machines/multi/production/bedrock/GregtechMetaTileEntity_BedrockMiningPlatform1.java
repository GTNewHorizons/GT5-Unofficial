package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.bedrock;

import gregtech.api.enums.TAE;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;

import gtPlusPlus.core.material.ALLOY;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;

public class GregtechMetaTileEntity_BedrockMiningPlatform1 extends GregtechMetaTileEntity_BedrockMiningPlatformBase {
	public GregtechMetaTileEntity_BedrockMiningPlatform1(final int aID, final String aName, final String aNameRegional) {
		super(aID, aName, aNameRegional);
	}

	public GregtechMetaTileEntity_BedrockMiningPlatform1(final String aName) {
		super(aName);
	}

	public String[] getDescription() {
		return this.getDescriptionInternal("I");
	}

	public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
		return (IMetaTileEntity) new GregtechMetaTileEntity_BedrockMiningPlatform1(this.mName);
	}

	protected Material getFrameMaterial() {
		return ALLOY.INCONEL_690;
	}

	protected int getCasingTextureIndex() {
		return TAE.getIndexFromPage(0, 14);
	}

	protected int getRadiusInChunks() {
		return 9;
	}

	protected int getMinTier() {
		return 5;
	}

	protected int getBaseProgressTime() {
		return (int) (420*(this.mProductionModifier/100));
	}
}