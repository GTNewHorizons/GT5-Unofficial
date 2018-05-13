package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing;

import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.TAE;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;

import net.minecraft.item.ItemStack;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.Recipe_GT;
import gregtech.api.gui.GT_GUIContainer_MultiMachine;

import static gregtech.api.util.Recipe_GT.Gregtech_Recipe_Map.*;

import net.minecraft.entity.player.InventoryPlayer;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;

public class GregtechMetaTileEntity_IndustrialVacuumFreezer extends GregtechMeta_MultiBlockBase {

	public static int CASING_TEXTURE_ID;
	public static String mCryoFuelName = "Gelid Cryotheum";
	public static String mCasingName = "Advanced Cryogenic Casing";
	public static FluidStack mFuelStack;

	public GregtechMetaTileEntity_IndustrialVacuumFreezer(final int aID, final String aName, final String aNameRegional) {
		super(aID, aName, aNameRegional);
		mFuelStack = FluidUtils.getFluidStack("cryotheum", 1);
		CASING_TEXTURE_ID = TAE.getIndexFromPage(2, 10);
		mCryoFuelName = mFuelStack.getLocalizedName();
		mCasingName = ItemUtils.getLocalizedNameOfBlock(ModBlocks.blockCasings3Misc, 10);
	}

	public GregtechMetaTileEntity_IndustrialVacuumFreezer(final String aName) {
		super(aName);
		mFuelStack = FluidUtils.getFluidStack("cryotheum", 1);
		CASING_TEXTURE_ID = TAE.getIndexFromPage(2, 10);
		mCryoFuelName = mFuelStack.getLocalizedName();
		mCasingName = ItemUtils.getLocalizedNameOfBlock(ModBlocks.blockCasings3Misc, 10);
	}

	public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
		return (IMetaTileEntity) new GregtechMetaTileEntity_IndustrialVacuumFreezer(this.mName);
	}

	public String[] getDescription() {
		return new String[]{
				"Controller Block for the Advanced Cryogenic Freezer",
				"Super cools hot ingots and cells",
				"Processes four Vacuum Freezer Recipes at double speed",
				"Consumes 1L of "+mCryoFuelName+"/t during operation",
				"Size(WxHxD): 3x3x3 (Hollow), Controller (Front centered)",
				"1x Input Bus (Any casing)",
				"1x Output Bus (Any casing)", 
				"1x Input Hatch (Any casing, optional)",
				"1x Output Hatch (Any casing, optional)", 
				"1x Maintenance Hatch (Any casing)", 
				"1x Energy Hatch (Any casing)",
				mCasingName+"s for the rest (10 at least!)"};
	}

	public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final byte aSide, final byte aFacing,
			final byte aColorIndex, final boolean aActive, final boolean aRedstone) {
		if (aSide == aFacing) {
			return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[CASING_TEXTURE_ID],
					new GT_RenderedTexture((IIconContainer) (aActive
							? Textures.BlockIcons.OVERLAY_FRONT_VACUUM_FREEZER_ACTIVE
									: Textures.BlockIcons.OVERLAY_FRONT_VACUUM_FREEZER))};
		}
		return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[CASING_TEXTURE_ID]};
	}

	public Object getClientGUI(final int aID, final InventoryPlayer aPlayerInventory,
			final IGregTechTileEntity aBaseMetaTileEntity) {
		return new GT_GUIContainer_MultiMachine(aPlayerInventory, aBaseMetaTileEntity, this.getLocalName(),
				"VacuumFreezer.png");
	}

	public GT_Recipe.GT_Recipe_Map getRecipeMap() {		
		if (sAdvFreezerRecipes_GT.mRecipeList.size() < 1) {
			for (GT_Recipe a : sAdvFreezerRecipes.mRecipeList) {
				sAdvFreezerRecipes_GT.add(a);
			}
		}		
		return Recipe_GT.Gregtech_Recipe_Map.sAdvFreezerRecipes_GT;
	}

	public boolean isCorrectMachinePart(final ItemStack aStack) {
		return true;
	}

	public boolean isFacingValid(final byte aFacing) {
		return aFacing > 1;
	}

	public boolean checkRecipe(final ItemStack aStack) {
		return this.checkRecipeGeneric(4, 100, 100);
	}

	public boolean checkMachine(final IGregTechTileEntity aBaseMetaTileEntity, final ItemStack aStack) {
		final int xDir = ForgeDirection.getOrientation((int) aBaseMetaTileEntity.getBackFacing()).offsetX;
		final int zDir = ForgeDirection.getOrientation((int) aBaseMetaTileEntity.getBackFacing()).offsetZ;
		if (!aBaseMetaTileEntity.getAirOffset(xDir, 0, zDir)) {
			return false;
		}
		int tAmount = 0;
		for (int i = -1; i < 2; ++i) {
			for (int j = -1; j < 2; ++j) {
				for (int h = -1; h < 2; ++h) {
					if (h != 0 || ((xDir + i != 0 || zDir + j != 0) && (i != 0 || j != 0))) {
						final IGregTechTileEntity tTileEntity = aBaseMetaTileEntity
								.getIGregTechTileEntityOffset(xDir + i, h, zDir + j);
						if (!this.addToMachineList(tTileEntity, CASING_TEXTURE_ID)) {
							if (aBaseMetaTileEntity.getBlockOffset(xDir + i, h,	zDir + j) != ModBlocks.blockCasings3Misc) {
								return false;
							}
							if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j) != 10) {
								return false;
							}
							++tAmount;
						}
					}
				}
			}
		}
		return tAmount >= 10;
	}

	public int getMaxEfficiency(final ItemStack aStack) {
		return 10000;
	}

	public int getPollutionPerTick(final ItemStack aStack) {
		return 50;
	}

	public int getDamageToComponent(final ItemStack aStack) {
		return 0;
	}

	public boolean explodesOnComponentBreak(final ItemStack aStack) {
		return false;
	}

	@Override
	public boolean hasSlotInGUI() {
		return true;
	}

	@Override
	public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
		/*if (this.getBaseMetaTileEntity().isActive()) {
			if (!this.depleteInput(mFuelStack.copy())) {
				this.getBaseMetaTileEntity().setActive(false);
			}
		}	*/
		super.onPostTick(aBaseMetaTileEntity, aTick);
	}
}