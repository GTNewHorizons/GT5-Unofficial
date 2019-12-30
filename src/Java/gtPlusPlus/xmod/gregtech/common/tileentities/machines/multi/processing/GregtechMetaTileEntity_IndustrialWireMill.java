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
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public class GregtechMetaTileEntity_IndustrialWireMill
extends GregtechMeta_MultiBlockBase {
	public GregtechMetaTileEntity_IndustrialWireMill(final int aID, final String aName, final String aNameRegional) {
		super(aID, aName, aNameRegional);
	}

	public GregtechMetaTileEntity_IndustrialWireMill(final String aName) {
		super(aName);
	}

	@Override
	public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
		return new GregtechMetaTileEntity_IndustrialWireMill(this.mName);
	}

	@Override
	public String getMachineType() {
		return "Wiremill";
	}

	@Override
	public String[] getTooltip() {
		return new String[]{
				"Controller Block for the Industrial Wire Factory",
				"200% faster than using single block machines of the same voltage",
				"Only uses 75% of the eu/t normally required",
				"Processes four items per voltage tier",
				"Size: 3x3x5 [WxHxL] (Hollow)",
				"Wire Factory Casings (32 at least!)",
				"Controller (front centered)",
				"1x Input Bus",
				"1x Output Bus",
				"1x Energy Hatch",
				//"Maintenance Hatch must be at the back, centered",
		};
	}
	
	@Override
	public String getSound() {
		return GregTech_API.sSoundList.get(Integer.valueOf(204));
	}

	@Override
	public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final byte aSide, final byte aFacing, final byte aColorIndex, final boolean aActive, final boolean aRedstone) {
		if (aSide == aFacing) {
			return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[TAE.GTPP_INDEX(6)], new GT_RenderedTexture(aActive ? TexturesGtBlock.Overlay_Machine_Controller_Default_Active : TexturesGtBlock.Overlay_Machine_Controller_Default)};
		}
		return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[TAE.GTPP_INDEX(6)]};
	}

	@Override
	public boolean hasSlotInGUI() {
		return false;
	}

	@Override
	public String getCustomGUIResourceName() {
		return "IndustrialWireFactory";
	}

	@Override
	public GT_Recipe.GT_Recipe_Map getRecipeMap() {
		return GT_Recipe.GT_Recipe_Map.sWiremillRecipes;
	}

	@Override
	public boolean isFacingValid(final byte aFacing) {
		return aFacing > 1;
	}

	@Override
	public boolean checkRecipe(final ItemStack aStack) {
		return checkRecipeGeneric((4* GT_Utility.getTier(this.getMaxInputVoltage())), 75, 200);
	}
	
	@Override
	public int getMaxParallelRecipes() {
		return (4* GT_Utility.getTier(this.getMaxInputVoltage()));
	}

	@Override
	public int getEuDiscountForParallelism() {
		return 75;
	}

	@Override
	public boolean checkMultiblock(final IGregTechTileEntity aBaseMetaTileEntity, final ItemStack aStack) {
		final byte tSide = this.getBaseMetaTileEntity().getBackFacing();
		int aCasingCount = 0;
		if ((this.getBaseMetaTileEntity().getAirAtSideAndDistance(this.getBaseMetaTileEntity().getBackFacing(), 1)) && (this.getBaseMetaTileEntity().getAirAtSideAndDistance(this.getBaseMetaTileEntity().getBackFacing(), 2) && (this.getBaseMetaTileEntity().getAirAtSideAndDistance(this.getBaseMetaTileEntity().getBackFacing(), 3)))) {
			for (byte i = 2; i < 6; i = (byte) (i + 1)) {
				IGregTechTileEntity tTileEntity;
				if ((null != (tTileEntity = this.getBaseMetaTileEntity().getIGregTechTileEntityAtSideAndDistance(i, 2))) &&
						(tTileEntity.getFrontFacing() == this.getBaseMetaTileEntity().getFrontFacing()) && (tTileEntity.getMetaTileEntity() != null) &&
						((tTileEntity.getMetaTileEntity() instanceof GregtechMetaTileEntity_IndustrialWireMill))) {
					//Utils.LOG_INFO("False 1");
					return false;
				}
			}
			
			//Check Rear Middle
			{
				Block aBlock = this.getBaseMetaTileEntity()
						.getBlockAtSideAndDistance(this.getBaseMetaTileEntity().getBackFacing(), 4);
				int aMeta = this.getBaseMetaTileEntity()
						.getMetaIDAtSideAndDistance(this.getBaseMetaTileEntity().getBackFacing(), 4);
				IGregTechTileEntity aTile = this.getBaseMetaTileEntity()
						.getIGregTechTileEntityAtSideAndDistance(this.getBaseMetaTileEntity().getBackFacing(), 4);
				if (!isValidBlockForStructure(aTile, getCasingTextureIndex(), true, aBlock, aMeta, getCasingBlock(),
						getCasingMeta())) {
					log("Bad Casing on Wiremill.");
					return false;
				}
			}
			final int tX = this.getBaseMetaTileEntity().getXCoord();
			final int tY = this.getBaseMetaTileEntity().getYCoord();
			final int tZ = this.getBaseMetaTileEntity().getZCoord();
			for (byte i = -1; i < 2; i = (byte) (i + 1)) {
				for (byte j = -1; j < 2; j = (byte) (j + 1)) {
					if ((i != 0) || (j != 0)) {
						for (byte k = 0; k < 5; k = (byte) (k + 1)) {							
								Block aBlock = this.getBaseMetaTileEntity().getBlock(tX + (tSide == 5 ? k : tSide == 4 ? -k : i), tY + j, tZ + (tSide == 2 ? -k : tSide == 3 ? k : i));
								int aMeta = this.getBaseMetaTileEntity().getMetaID(tX + (tSide == 5 ? k : tSide == 4 ? -k : i), tY + j, tZ + (tSide == 2 ? -k : tSide == 3 ? k : i));
								IGregTechTileEntity aTile = this.getBaseMetaTileEntity().getIGregTechTileEntity(tX + (tSide == 5 ? k : tSide == 4 ? -k : i), tY + j, tZ + (tSide == 2 ? -k : tSide == 3 ? k : i));
								if (!isValidBlockForStructure(aTile, getCasingTextureIndex(), true, aBlock, aMeta, getCasingBlock(), getCasingMeta())) {
									log("Bad Casing on Wiremill.");
									return false;
								}	
								else {
									if (aTile == null) {
										aCasingCount++;
									}
								}
						}
					}
				}
			}
			if ((this.mOutputHatches.size() != 0) || (this.mInputHatches.size() != 0)) {
				log("Use Busses, Not Hatches for Input/Output.");
				return false;
			}
			if ((this.mInputBusses.size() == 0) || (this.mOutputBusses.size() == 0)) {
				log("Incorrect amount of Input & Output busses.");
				return false;
			}
			if ((this.mMaintenanceHatches.size() != 1)) {
				log("Incorrect amount of Maintenance or Energy hatches.");
				return false;
			}
		} else {
			log("False 5");
			return false;
		}
		log("True");
		return aCasingCount >= 32;
	}

	@Override
	public int getMaxEfficiency(final ItemStack aStack) {
		return 10000;
	}

	@Override
	public int getPollutionPerTick(final ItemStack aStack) {
		return 5;
	}

	@Override
	public int getAmountOfOutputs() {
		return 1;
	}

	@Override
	public boolean explodesOnComponentBreak(final ItemStack aStack) {
		return false;
	}

	public Block getCasingBlock() {
		return ModBlocks.blockCasingsMisc;
	}


	public byte getCasingMeta() {
		return 6;
	}


	public byte getCasingTextureIndex() {
		return (byte) TAE.GTPP_INDEX(6);
	}
}