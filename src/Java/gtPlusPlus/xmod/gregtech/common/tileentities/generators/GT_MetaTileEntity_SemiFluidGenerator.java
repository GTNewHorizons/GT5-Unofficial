package gtPlusPlus.xmod.gregtech.common.tileentities.generators;

import gregtech.api.GregTech_API;
import gregtech.api.enums.ConfigCategories;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.util.GT_Recipe;
import gregtech.common.tileentities.generators.GT_MetaTileEntity_DieselGenerator;

public class GT_MetaTileEntity_SemiFluidGenerator extends GT_MetaTileEntity_DieselGenerator{
	
	public GT_MetaTileEntity_SemiFluidGenerator(int aID, String aName, String aNameRegional, int aTier) {
		super(aID, aName, aNameRegional, aTier);
		onConfigLoad();
	}

	public GT_MetaTileEntity_SemiFluidGenerator(String aName, int aTier, String aDescription, ITexture[][][] aTextures) {
		super(aName, aTier, aDescription, aTextures);
		onConfigLoad();
	}

	public GT_MetaTileEntity_SemiFluidGenerator(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
		super(aName, aTier, aDescription, aTextures);
		onConfigLoad();
	}
	
	public int getPollution() {
		return (int) (2.0D * Math.pow(2.0D, this.mTier - 1));
	}
	
	public int getCapacity() {
		return 8000;
	}

	public void onConfigLoad() {
		this.mEfficiency = GregTech_API.sMachineFile.get(ConfigCategories.machineconfig,
				"SemiFluidGenerator.efficiency.tier." + this.mTier, 100 - (this.mTier * 10));
	}
	
	public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
		return new GT_MetaTileEntity_SemiFluidGenerator(this.mName, this.mTier, this.mDescriptionArray, this.mTextures);
	}

	public GT_Recipe.GT_Recipe_Map getRecipes() {
		return GT_Recipe.GT_Recipe_Map.sDenseLiquidFuels;
	}

}
