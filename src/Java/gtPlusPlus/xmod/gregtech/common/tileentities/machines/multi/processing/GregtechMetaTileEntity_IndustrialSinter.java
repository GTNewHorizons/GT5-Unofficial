package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

import gregtech.api.enums.TAE;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Maintenance;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_MultiBlockBase;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.xmod.gregtech.api.gui.GUI_MultiMachine;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

public class GregtechMetaTileEntity_IndustrialSinter
extends GT_MetaTileEntity_MultiBlockBase {


	RenderBlocks asdasd = RenderBlocks.getInstance();

	public GregtechMetaTileEntity_IndustrialSinter(final int aID, final String aName, final String aNameRegional) {
		super(aID, aName, aNameRegional);
	}

	public GregtechMetaTileEntity_IndustrialSinter(final String aName) {
		super(aName);
	}

	@Override
	public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
		return new GregtechMetaTileEntity_IndustrialSinter(this.mName);
	}

	@Override
	public String[] getDescription() {
		return new String[]{
				"Controller Block for the Industrial Sinter Furnace",
				"Size: 3x5x3 [WxLxH] (Hollow)", "Controller (front centered)",
				"2x Input Bus (side centered)",
				"2x Output Bus (side centered)",
				"1x Energy Hatch (top or bottom centered)",
				"1x Maintenance Hatch (back centered)",
				"Sinter Furnace Casings for the rest (32 at least!)",
				"Causes " + 20 * this.getPollutionPerTick(null) + " Pollution per second",
				CORE.GT_Tooltip
		};
	}

	@Override
	public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final byte aSide, final byte aFacing, final byte aColorIndex, final boolean aActive, final boolean aRedstone) {
		if (aSide == aFacing) {
			return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[TAE.GTPP_INDEX(7)], new GT_RenderedTexture(aActive ? TexturesGtBlock.Overlay_Machine_Controller_Default_Active : TexturesGtBlock.Overlay_Machine_Controller_Default)};
		}
		return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[TAE.GTPP_INDEX(7)]};
	}

	@Override
	public Object getClientGUI(final int aID, final InventoryPlayer aPlayerInventory, final IGregTechTileEntity aBaseMetaTileEntity) {
		return new GUI_MultiMachine(aPlayerInventory, aBaseMetaTileEntity, this.getLocalName(), "WireFactory.png");
	}

	@Override
	public GT_Recipe.GT_Recipe_Map getRecipeMap() {
		return GT_Recipe.GT_Recipe_Map.sWiremillRecipes;
	}

	@Override
	public boolean isCorrectMachinePart(final ItemStack aStack) {
		return true;
	}

	@Override
	public boolean isFacingValid(final byte aFacing) {
		return aFacing > 1;
	}

	@Override
	public boolean checkRecipe(final ItemStack aStack) {
		final ArrayList<ItemStack> tInputList = this.getStoredInputs();
		for (final ItemStack tInput : tInputList) {
			final long tVoltage = this.getMaxInputVoltage();
			final byte tTier = (byte) Math.max(1, GT_Utility.getTier(tVoltage));

			final GT_Recipe tRecipe = GT_Recipe.GT_Recipe_Map.sWiremillRecipes.findRecipe(this.getBaseMetaTileEntity(), false, gregtech.api.enums.GT_Values.V[tTier], null, new ItemStack[]{tInput});
			if (tRecipe != null) {
				if (tRecipe.isRecipeInputEqual(true, null, new ItemStack[]{tInput})) {
					this.mEfficiency = (10000 - ((this.getIdealStatus() - this.getRepairStatus()) * 1000));
					this.mEfficiencyIncrease = 10000;
					if (tRecipe.mEUt <= 16) {
						this.mEUt = (tRecipe.mEUt * (1 << (tTier - 1)) * (1 << (tTier - 1)));
						this.mMaxProgresstime = (tRecipe.mDuration / (1 << (tTier - 1)));
					} else {
						this.mEUt = tRecipe.mEUt;
						this.mMaxProgresstime = tRecipe.mDuration;
						while (this.mEUt <= gregtech.api.enums.GT_Values.V[(tTier - 1)]) {
							this.mEUt *= 4;
							this.mMaxProgresstime /= 2;
						}
					}
					if (this.mEUt > 0) {
						this.mEUt = (-this.mEUt);
					}
					this.mMaxProgresstime = Math.max(1, this.mMaxProgresstime);
					this.mOutputItems = new ItemStack[]{tRecipe.getOutput(0)};
					this.updateSlots();
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean checkMachine(final IGregTechTileEntity aBaseMetaTileEntity, final ItemStack aStack) {
		final int controllerX = aBaseMetaTileEntity.getXCoord();
		final int controllerY = aBaseMetaTileEntity.getYCoord();
		final int controllerZ = aBaseMetaTileEntity.getZCoord();

		final byte tSide = this.getBaseMetaTileEntity().getBackFacing();
		if ((this.getBaseMetaTileEntity().getAirAtSideAndDistance(this.getBaseMetaTileEntity().getBackFacing(), 1)) && (this.getBaseMetaTileEntity().getAirAtSideAndDistance(this.getBaseMetaTileEntity().getBackFacing(), 2) && (this.getBaseMetaTileEntity().getAirAtSideAndDistance(this.getBaseMetaTileEntity().getBackFacing(), 3)))) {
			int tAirCount = 0;
			for (byte i = -1; i < 2; i = (byte) (i + 1)) {
				for (byte j = -1; j < 2; j = (byte) (j + 1)) {
					for (byte k = -1; k < 2; k = (byte) (k + 1)) {
						if (this.getBaseMetaTileEntity().getAirOffset(i, j, k)) {
							Logger.INFO("Found Air at: "+(controllerX+i)+" "+(controllerY+k)+" "+(controllerZ+k));
							//if (aBaseMetaTileEntity.getWorld().isRemote){
							//asdasd.renderStandardBlock(ModBlocks.MatterFabricatorEffectBlock, (controllerX+i), (controllerY+k), (controllerZ+k));
							//UtilsRendering.drawBlockInWorld((controllerX+i), (controllerY+k), (controllerZ+k), Color.YELLOW_GREEN);
							//}
							tAirCount++;
						}
					}
				}
			}
			if (tAirCount != 10) {
				Logger.INFO("False. Air != 10. Air == "+tAirCount);
				//return false;
			}
			for (byte i = 2; i < 6; i = (byte) (i + 1)) {
				//UtilsRendering.drawBlockInWorld((controllerX+i), (controllerY), (controllerZ), Color.LIME_GREEN);
				IGregTechTileEntity tTileEntity;
				if ((null != (tTileEntity = this.getBaseMetaTileEntity().getIGregTechTileEntityAtSideAndDistance(i, 2))) &&
						(tTileEntity.getFrontFacing() == this.getBaseMetaTileEntity().getFrontFacing()) && (tTileEntity.getMetaTileEntity() != null) &&
						((tTileEntity.getMetaTileEntity() instanceof GregtechMetaTileEntity_IndustrialSinter))) {
					//Utils.LOG_INFO("False 1");
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
							//UtilsRendering.drawBlockInWorld((controllerX+i), (controllerY+k), (controllerZ+k), Color.ORANGE);
							if (((i == 0) || (j == 0)) && ((k == 1) || (k == 2) || (k == 3))) {
								//UtilsRendering.drawBlockInWorld((controllerX+i), (controllerY+k), (controllerZ+k), Color.TOMATO);
								if ((this.getBaseMetaTileEntity().getBlock(tX + (tSide == 5 ? k : tSide == 4 ? -k : i), tY + j, tZ + (tSide == 2 ? -k : tSide == 3 ? k : i)) == this.getCasingBlock()) && (this.getBaseMetaTileEntity().getMetaID(tX + (tSide == 5 ? k : tSide == 4 ? -k : i), tY + j, tZ + (tSide == 2 ? -k : tSide == 3 ? k : i)) == this.getCasingMeta())) {
								}
								else if (!this.addToMachineList(this.getBaseMetaTileEntity().getIGregTechTileEntity(tX + (tSide == 5 ? k : tSide == 4 ? -k : i), tY + j, tZ + (tSide == 2 ? -k : tSide == 3 ? k : i))) && (!this.addEnergyInputToMachineList(this.getBaseMetaTileEntity().getIGregTechTileEntity(tX + (tSide == 5 ? k : tSide == 4 ? -k : i), tY + j, tZ + (tSide == 2 ? -k : tSide == 3 ? k : i))))) {
									Logger.INFO("False 2");
									return false;
								}
							}
							else if ((this.getBaseMetaTileEntity().getBlock(tX + (tSide == 5 ? k : tSide == 4 ? -k : i), tY + j, tZ + (tSide == 2 ? -k : tSide == 3 ? k : i)) == this.getCasingBlock()) && (this.getBaseMetaTileEntity().getMetaID(tX + (tSide == 5 ? k : tSide == 4 ? -k : i), tY + j, tZ + (tSide == 2 ? -k : tSide == 3 ? k : i)) == this.getCasingMeta())) {
							}
							else {
								Logger.INFO("False 3");
								return false;
							}
						}
					}
				}
			}
			if ((this.mOutputHatches.size() != 0) || (this.mInputHatches.size() != 0)) {
				Logger.INFO("Use Busses, Not Hatches for Input/Output.");
				return false;
			}
			if ((this.mInputBusses.size() != 2) || (this.mOutputBusses.size() != 2)) {
				Logger.INFO("Incorrect amount of Input & Output busses.");
				return false;
			}
			this.mMaintenanceHatches.clear();
			final IGregTechTileEntity tTileEntity = this.getBaseMetaTileEntity().getIGregTechTileEntityAtSideAndDistance(this.getBaseMetaTileEntity().getBackFacing(), 4);
			if ((tTileEntity != null) && (tTileEntity.getMetaTileEntity() != null)) {
				if ((tTileEntity.getMetaTileEntity() instanceof GT_MetaTileEntity_Hatch_Maintenance)) {
					this.mMaintenanceHatches.add((GT_MetaTileEntity_Hatch_Maintenance) tTileEntity.getMetaTileEntity());
					((GT_MetaTileEntity_Hatch) tTileEntity.getMetaTileEntity()).mMachineBlock = this.getCasingTextureIndex();
				} else {
					Logger.INFO("Maintenance hatch must be in the middle block on the back.");
					return false;
				}
			}
			if ((this.mMaintenanceHatches.size() != 1) || (this.mEnergyHatches.size() != 1)) {
				Logger.INFO("Incorrect amount of Maintenance or Energy hatches.");
				return false;
			}
		} else {
			Logger.INFO("False 5");
			return false;
		}
		Logger.INFO("True");
		return true;
	}

	@Override
	public int getMaxEfficiency(final ItemStack aStack) {
		return 10000;
	}

	@Override
	public int getPollutionPerTick(final ItemStack aStack) {
		return 0;
	}

	@Override
	public int getDamageToComponent(final ItemStack aStack) {
		return 0;
	}

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
		return 1; //TODO
	}

	private boolean addToMachineList(final IGregTechTileEntity tTileEntity) {
		return ((this.addMaintenanceToMachineList(tTileEntity, this.getCasingTextureIndex())) || (this.addInputToMachineList(tTileEntity, this.getCasingTextureIndex())) || (this.addOutputToMachineList(tTileEntity, this.getCasingTextureIndex())) || (this.addMufflerToMachineList(tTileEntity, this.getCasingTextureIndex())));
	}

	private boolean addEnergyInputToMachineList(final IGregTechTileEntity tTileEntity) {
		return ((this.addEnergyInputToMachineList(tTileEntity, this.getCasingTextureIndex())));
	}
}
