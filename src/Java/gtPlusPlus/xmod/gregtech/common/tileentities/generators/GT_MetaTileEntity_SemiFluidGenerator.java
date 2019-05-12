package gtPlusPlus.xmod.gregtech.common.tileentities.generators;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.GregTech_API;
import gregtech.api.enums.ConfigCategories;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.objects.GT_ItemStack;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.Recipe_GT.Gregtech_Recipe_Map;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.xmod.gregtech.api.metatileentity.custom.power.GTPP_MTE_BasicLosslessGenerator;
import net.minecraft.item.ItemStack;

public class GT_MetaTileEntity_SemiFluidGenerator extends GTPP_MTE_BasicLosslessGenerator{

	public static final int BASE_POLLUTION = 2;
	public int mEfficiency;

	/*public GT_MetaTileEntity_SemiFluidGenerator(int aID, String aName, String aNameRegional, int aTier) {
		super(aID, aName, aNameRegional, aTier);
		onConfigLoad();
	}*/

	public GT_MetaTileEntity_SemiFluidGenerator(int aID, String aName, String aNameRegional, int aTier) {
		super(aID, aName, aNameRegional, aTier,
				"Requires semi-fluid Fuel",
				new ITexture[0]);
		onConfigLoad();
	}

	public GT_MetaTileEntity_SemiFluidGenerator(String aName, int aTier, String aDescription, ITexture[][][] aTextures) {
		super(aName, aTier, aDescription, aTextures);
		onConfigLoad();
	}

	public int getPollution() {
		return (int) (2.0D * Math.pow(2.0D, this.mTier));
	}

	@Override
	public int getCapacity() {
		return 4000 * this.mTier;
	}

	public void onConfigLoad() {
		this.mEfficiency = GregTech_API.sMachineFile.get(ConfigCategories.machineconfig,
				"SemiFluidGenerator.efficiency.tier." + this.mTier, 100 - (this.mTier * 5));
	}

	@Override
	public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
		return new GT_MetaTileEntity_SemiFluidGenerator(this.mName, this.mTier, this.mDescription, this.mTextures);
	}

	@Override
	public GT_Recipe.GT_Recipe_Map getRecipes() {
		//Logger.WARNING("Fuel Count: "+Gregtech_Recipe_Map.sSemiFluidLiquidFuels.mRecipeList.size());
		return Gregtech_Recipe_Map.sSemiFluidLiquidFuels;
	}
	
	@Override
	public String[] getDescription() {
		return new String[]{this.mDescription, "Produces "+(this.getPollution()*20)+" pollution/sec", "Fuel Efficiency: "+this.getEfficiency() + "%"};
	}

	@Override
	public int getEfficiency() {
		return this.mEfficiency;
	}
	
	@Override
	public boolean isOutputFacing(byte aSide) {
		return (aSide == getBaseMetaTileEntity().getFrontFacing());
	}
	
	@Override
	public boolean allowCoverOnSide(byte aSide, GT_ItemStack aCover) {
		if (aSide != this.getBaseMetaTileEntity().getFrontFacing()) {
			return true;
		}
		return super.allowCoverOnSide(aSide, aCover);
	}
	
	@Override
	public int getFuelValue(ItemStack aStack) {
		if ((GT_Utility.isStackInvalid(aStack)) || (getRecipes() == null)) {
			Logger.WARNING("Bad Fuel?");
			return 0;
		}
		int rValue = Math.max(GT_ModHandler.getFuelCanValue(aStack) * 6 / 5, super.getFuelValue(aStack));
		if (ItemList.Fuel_Can_Plastic_Filled.isStackEqual(aStack, false, true)) {
			rValue = Math.max(rValue, GameRegistry.getFuelValue(aStack) * 3);
		}
		Logger.WARNING("Good Fuel: "+rValue);
		return rValue;
	}

	@Override
	public ITexture[] getFront(byte aColor) {
		return new ITexture[] { super.getFront(aColor)[0],
				new GT_RenderedTexture(Textures.BlockIcons.DIESEL_GENERATOR_FRONT),
				Textures.BlockIcons.OVERLAYS_ENERGY_OUT[this.mTier] };
	}

	@Override
	public ITexture[] getBack(byte aColor) {
		return new ITexture[] { super.getBack(aColor)[0],
				new GT_RenderedTexture(Textures.BlockIcons.DIESEL_GENERATOR_TOP) };
	}

	@Override
	public ITexture[] getBottom(byte aColor) {
		return new ITexture[] { super.getBottom(aColor)[0],
				new GT_RenderedTexture(Textures.BlockIcons.DIESEL_GENERATOR_BOTTOM) };
	}

	@Override
	public ITexture[] getTop(byte aColor) {
		return new ITexture[] { super.getTop(aColor)[0],
				new GT_RenderedTexture(Textures.BlockIcons.DIESEL_GENERATOR_SIDE) };
	}

	@Override
	public ITexture[] getSides(byte aColor) {
		return new ITexture[] { super.getSides(aColor)[0],
				new GT_RenderedTexture(Textures.BlockIcons.DIESEL_GENERATOR_TOP) };
	}

	@Override
	public ITexture[] getFrontActive(byte aColor) {
		return new ITexture[] { super.getFrontActive(aColor)[0],
				new GT_RenderedTexture(Textures.BlockIcons.DIESEL_GENERATOR_FRONT_ACTIVE),
				Textures.BlockIcons.OVERLAYS_ENERGY_OUT[this.mTier] };
	}

	@Override
	public ITexture[] getBackActive(byte aColor) {
		return new ITexture[] { super.getBackActive(aColor)[0],
				new GT_RenderedTexture(Textures.BlockIcons.DIESEL_GENERATOR_TOP_ACTIVE) };
	}

	@Override
	public ITexture[] getBottomActive(byte aColor) {
		return new ITexture[] { super.getBottomActive(aColor)[0],
				new GT_RenderedTexture(Textures.BlockIcons.DIESEL_GENERATOR_BOTTOM_ACTIVE) };
	}

	@Override
	public ITexture[] getTopActive(byte aColor) {
		return new ITexture[] { super.getTopActive(aColor)[0],
				new GT_RenderedTexture(Textures.BlockIcons.DIESEL_GENERATOR_SIDE_ACTIVE) };
	}

	@Override
	public ITexture[] getSidesActive(byte aColor) {
		return new ITexture[] { super.getSidesActive(aColor)[0],
				new GT_RenderedTexture(Textures.BlockIcons.DIESEL_GENERATOR_TOP_ACTIVE) };
	}

}
