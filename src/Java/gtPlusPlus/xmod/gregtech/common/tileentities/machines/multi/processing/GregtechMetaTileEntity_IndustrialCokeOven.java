package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing;

import gregtech.api.GregTech_API;
import gregtech.api.enums.TAE;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.Recipe_GT;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

public class GregtechMetaTileEntity_IndustrialCokeOven
extends GregtechMeta_MultiBlockBase {
	private int mLevel = 0;

	public GregtechMetaTileEntity_IndustrialCokeOven(final int aID, final String aName, final String aNameRegional) {
		super(aID, aName, aNameRegional);
	}

	public GregtechMetaTileEntity_IndustrialCokeOven(final String aName) {
		super(aName);
	}

	@Override
	public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
		return new GregtechMetaTileEntity_IndustrialCokeOven(this.mName);
	}

	@Override
	public String getMachineType() {
		return "Coke Oven";
	}

	@Override
	public String[] getTooltip() {
		return new String[]{"Processes Logs and Coal into Charcoal and Coal Coke.",
				"Controller Block for the Industrial Coke Oven",
				"Gain 4% speed bonus per voltage tier increased",
				"Process 12x materials with Heat Resistant Casings",
				"Or 24x materials with Heat Proof Casings",
				"Size: 3x3x3 (Hollow)",
				"Structural Coke Oven Casings (8 at least!)",
				"Controller (front middle at bottom)",
				"8x Heat Resistant/Proof Coke Oven Casings (middle Layer, hollow)",
				"1x Input Hatch",
				"1x Output Hatch",
				"1x Input Bus",
				"1x Output Bus",
				"1x Energy Hatch"
				};
	}

	@Override
	public String getSound() {
		return GregTech_API.sSoundList.get(Integer.valueOf(207));
	}

	@Override
	public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final byte aSide, final byte aFacing, final byte aColorIndex, final boolean aActive, final boolean aRedstone) {
		if (aSide == aFacing) {
			return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[TAE.GTPP_INDEX(1)], new GT_RenderedTexture(aActive ? TexturesGtBlock.Overlay_Machine_Controller_Advanced_Active : TexturesGtBlock.Overlay_Machine_Controller_Advanced)};
		}
		return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[TAE.GTPP_INDEX(1)]};
	}

	@Override
	public boolean hasSlotInGUI() {
		return false;
	}

	@Override
	public String getCustomGUIResourceName() {
		return "CokeOven";
	}	

	@Override
	public GT_Recipe.GT_Recipe_Map getRecipeMap() {
		return Recipe_GT.Gregtech_Recipe_Map.sCokeOvenRecipes;

	}

	/* @Override
	public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }*/

	@Override
	public boolean isFacingValid(final byte aFacing) {
		return aFacing > 1;
	}

	@Override
	public boolean checkRecipe(final ItemStack aStack) {
		return checkRecipeGeneric(getMaxParallelRecipes(), getEuDiscountForParallelism(), 0);
	}
	
	@Override
	public int getMaxParallelRecipes() {
		return this.mLevel * 12;
	}

	@Override
	public int getEuDiscountForParallelism() {
		return (100-(GT_Utility.getTier(this.getMaxInputVoltage())*4));
	}

	@Override
	public boolean checkMultiblock(final IGregTechTileEntity aBaseMetaTileEntity, final ItemStack aStack) {
		final int xDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetX;
		final int zDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetZ;
		this.mLevel = 0;
		if (!aBaseMetaTileEntity.getAirOffset(xDir, 1, zDir)) {
			return false;
		}

		final byte tUsedMeta = aBaseMetaTileEntity.getMetaIDOffset(xDir + 1, 1, zDir);
		switch (tUsedMeta) {
		case 2:
			this.mLevel = 1;
			break;
		case 3:
			this.mLevel = 2;
			break;
		default:
			return false;
		}
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				if ((i != 0) || (j != 0)) {
					final IGregTechTileEntity tTileEntity2 = aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir + i, 2, zDir + j);					
					if (!isValidBlockForStructure(null, TAE.GTPP_INDEX(1), false, aBaseMetaTileEntity.getBlockOffset(xDir + i, 1, zDir + j), (int) aBaseMetaTileEntity.getMetaIDOffset(xDir + i, 1, zDir + j), ModBlocks.blockCasingsMisc, tUsedMeta)) {
						Logger.INFO("Heating Coils missing.");
						return false;
					}
					
					if (!isValidBlockForStructure(tTileEntity2, TAE.GTPP_INDEX(1), true, aBaseMetaTileEntity.getBlockOffset(xDir + i, 2, zDir + j), (int) aBaseMetaTileEntity.getMetaIDOffset(xDir + i, 2, zDir + j), ModBlocks.blockCasingsMisc, 1)) {
						Logger.INFO("Casings missing from top layer of coke oven.");
						return false;
					}
				}
			}
		}
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				if (((xDir + i) != 0) || ((zDir + j) != 0)) {
					final IGregTechTileEntity tTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir + i, 0, zDir + j);
					if (!isValidBlockForStructure(tTileEntity, TAE.GTPP_INDEX(1), true, aBaseMetaTileEntity.getBlockOffset(xDir + i, 0, zDir + j), (int) aBaseMetaTileEntity.getMetaIDOffset(xDir + i, 0, zDir + j), ModBlocks.blockCasingsMisc, 1)) {
						Logger.INFO("Casings missing from bottom layer of coke oven.");
						return false;
					}
				}
			}
		}
		return true;
	}

	@Override
	public int getMaxEfficiency(final ItemStack aStack) {
		return 10000;
	}

	@Override
	public int getPollutionPerTick(final ItemStack aStack) {
		return 4;
	}

	/* @Override
	public int getDamageToComponent(ItemStack aStack) {
        return 0;
    }*/

	@Override
	public int getAmountOfOutputs() {
		return 24;
	}

	@Override
	public boolean explodesOnComponentBreak(final ItemStack aStack) {
		return false;
	}
}
