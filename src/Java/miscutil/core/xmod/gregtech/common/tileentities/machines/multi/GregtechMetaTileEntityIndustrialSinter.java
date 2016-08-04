package miscutil.core.xmod.gregtech.common.tileentities.machines.multi;

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

import java.util.ArrayList;

import miscutil.core.block.ModBlocks;
import miscutil.core.lib.CORE;
import miscutil.core.util.Utils;
import miscutil.core.xmod.gregtech.api.gui.GUI_MultiMachine;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

public class GregtechMetaTileEntityIndustrialSinter
extends GT_MetaTileEntity_MultiBlockBase {
	

	RenderBlocks asdasd = RenderBlocks.getInstance();
	
	public GregtechMetaTileEntityIndustrialSinter(int aID, String aName, String aNameRegional) {
		super(aID, aName, aNameRegional);
	}

	public GregtechMetaTileEntityIndustrialSinter(String aName) {
		super(aName);
	}

	@Override
	public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
		return new GregtechMetaTileEntityIndustrialSinter(this.mName);
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
				CORE.GT_Tooltip
		};
	}

	@Override
	public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
		if (aSide == aFacing) {
			return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[63], new GT_RenderedTexture(aActive ? Textures.BlockIcons.OVERLAY_FRONT_VACUUM_FREEZER_ACTIVE : Textures.BlockIcons.OVERLAY_FRONT_VACUUM_FREEZER)};
		}
		return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[63]};
	}

	@Override
	public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
		return new GUI_MultiMachine(aPlayerInventory, aBaseMetaTileEntity, getLocalName(), "WireFactory.png");
	}

	@Override
	public GT_Recipe.GT_Recipe_Map getRecipeMap() {
		return GT_Recipe.GT_Recipe_Map.sWiremillRecipes;
	}

	@Override
	public boolean isCorrectMachinePart(ItemStack aStack) {
		return true;
	}

	@Override
	public boolean isFacingValid(byte aFacing) {
		return aFacing > 1;
	}

	@Override
	public boolean checkRecipe(ItemStack aStack) {
		ArrayList<ItemStack> tInputList = getStoredInputs();
		for (ItemStack tInput : tInputList) {
			long tVoltage = getMaxInputVoltage();
			byte tTier = (byte) Math.max(1, GT_Utility.getTier(tVoltage));

			GT_Recipe tRecipe = GT_Recipe.GT_Recipe_Map.sWiremillRecipes.findRecipe(getBaseMetaTileEntity(), false, gregtech.api.enums.GT_Values.V[tTier], null, new ItemStack[]{tInput});
			if (tRecipe != null) {
				if (tRecipe.isRecipeInputEqual(true, null, new ItemStack[]{tInput})) {
					this.mEfficiency = (10000 - (getIdealStatus() - getRepairStatus()) * 1000);
					this.mEfficiencyIncrease = 10000;
					if (tRecipe.mEUt <= 16) {
						this.mEUt = (tRecipe.mEUt * (1 << tTier - 1) * (1 << tTier - 1));
						this.mMaxProgresstime = (tRecipe.mDuration / (1 << tTier - 1));
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
					updateSlots();
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
		int controllerX = aBaseMetaTileEntity.getXCoord();
		int controllerY = aBaseMetaTileEntity.getYCoord();
		int controllerZ = aBaseMetaTileEntity.getZCoord();

		byte tSide = getBaseMetaTileEntity().getBackFacing();
		if ((getBaseMetaTileEntity().getAirAtSideAndDistance(getBaseMetaTileEntity().getBackFacing(), 1)) && (getBaseMetaTileEntity().getAirAtSideAndDistance(getBaseMetaTileEntity().getBackFacing(), 2) && (getBaseMetaTileEntity().getAirAtSideAndDistance(getBaseMetaTileEntity().getBackFacing(), 3)))) {
			int tAirCount = 0;
			for (byte i = -1; i < 2; i = (byte) (i + 1)) {
				for (byte j = -1; j < 2; j = (byte) (j + 1)) {
					for (byte k = -1; k < 2; k = (byte) (k + 1)) {
						if (getBaseMetaTileEntity().getAirOffset(i, j, k)) {
							Utils.LOG_INFO("Found Air at: "+(controllerX+i)+" "+(controllerY+k)+" "+(controllerZ+k));
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
				Utils.LOG_INFO("False. Air != 10. Air == "+tAirCount);
				//return false;
			}
			for (byte i = 2; i < 6; i = (byte) (i + 1)) {
				//UtilsRendering.drawBlockInWorld((controllerX+i), (controllerY), (controllerZ), Color.LIME_GREEN);
				IGregTechTileEntity tTileEntity;
				if ((null != (tTileEntity = getBaseMetaTileEntity().getIGregTechTileEntityAtSideAndDistance(i, 2))) &&
						(tTileEntity.getFrontFacing() == getBaseMetaTileEntity().getFrontFacing()) && (tTileEntity.getMetaTileEntity() != null) &&
						((tTileEntity.getMetaTileEntity() instanceof GregtechMetaTileEntityIndustrialSinter))) {
					//Utils.LOG_INFO("False 1");
					return false;
				}
			}
			int tX = getBaseMetaTileEntity().getXCoord();
			int tY = getBaseMetaTileEntity().getYCoord();
			int tZ = getBaseMetaTileEntity().getZCoord();
			for (byte i = -1; i < 2; i = (byte) (i + 1)) {
				for (byte j = -1; j < 2; j = (byte) (j + 1)) {
					if ((i != 0) || (j != 0)) {
						for (byte k = 0; k < 5; k = (byte) (k + 1)) {
							//UtilsRendering.drawBlockInWorld((controllerX+i), (controllerY+k), (controllerZ+k), Color.ORANGE);
							if (((i == 0) || (j == 0)) && ((k == 1) || (k == 2) || (k == 3))) {
								//UtilsRendering.drawBlockInWorld((controllerX+i), (controllerY+k), (controllerZ+k), Color.TOMATO);
								if (getBaseMetaTileEntity().getBlock(tX + (tSide == 5 ? k : tSide == 4 ? -k : i), tY + j, tZ + (tSide == 2 ? -k : tSide == 3 ? k : i)) == getCasingBlock() && getBaseMetaTileEntity().getMetaID(tX + (tSide == 5 ? k : tSide == 4 ? -k : i), tY + j, tZ + (tSide == 2 ? -k : tSide == 3 ? k : i)) == getCasingMeta()) {
								} 
								else if (!addToMachineList(getBaseMetaTileEntity().getIGregTechTileEntity(tX + (tSide == 5 ? k : tSide == 4 ? -k : i), tY + j, tZ + (tSide == 2 ? -k : tSide == 3 ? k : i))) && (!addEnergyInputToMachineList(getBaseMetaTileEntity().getIGregTechTileEntity(tX + (tSide == 5 ? k : tSide == 4 ? -k : i), tY + j, tZ + (tSide == 2 ? -k : tSide == 3 ? k : i))))) {
									Utils.LOG_INFO("False 2");
									return false;
								}
							} 
							else if (getBaseMetaTileEntity().getBlock(tX + (tSide == 5 ? k : tSide == 4 ? -k : i), tY + j, tZ + (tSide == 2 ? -k : tSide == 3 ? k : i)) == getCasingBlock() && getBaseMetaTileEntity().getMetaID(tX + (tSide == 5 ? k : tSide == 4 ? -k : i), tY + j, tZ + (tSide == 2 ? -k : tSide == 3 ? k : i)) == getCasingMeta()) {
							} 
							else {
								Utils.LOG_INFO("False 3");
								return false;
							}
						}
					}
				}
			}
			if (this.mOutputHatches.size() != 0 || this.mInputHatches.size() != 0) {
				Utils.LOG_INFO("Use Busses, Not Hatches for Input/Output.");
				return false;
			}
			if (this.mInputBusses.size() != 2 || this.mOutputBusses.size() != 2) {
				Utils.LOG_INFO("Incorrect amount of Input & Output busses.");
				return false;
			}			
			this.mMaintenanceHatches.clear();
			IGregTechTileEntity tTileEntity = getBaseMetaTileEntity().getIGregTechTileEntityAtSideAndDistance(getBaseMetaTileEntity().getBackFacing(), 4);
			if ((tTileEntity != null) && (tTileEntity.getMetaTileEntity() != null)) {
				if ((tTileEntity.getMetaTileEntity() instanceof GT_MetaTileEntity_Hatch_Maintenance)) {
					this.mMaintenanceHatches.add((GT_MetaTileEntity_Hatch_Maintenance) tTileEntity.getMetaTileEntity());
					((GT_MetaTileEntity_Hatch) tTileEntity.getMetaTileEntity()).mMachineBlock = getCasingTextureIndex();
				} else {
					Utils.LOG_INFO("Maintenance hatch must be in the middle block on the back.");
					return false;
				}
			}
			if (this.mMaintenanceHatches.size() != 1 || this.mEnergyHatches.size() != 1) {
				Utils.LOG_INFO("Incorrect amount of Maintenance or Energy hatches.");
				return false;
			}
		} else {
			Utils.LOG_INFO("False 5");
			return false;
		}
		Utils.LOG_INFO("True");
		return true;
	}

	@Override
	public int getMaxEfficiency(ItemStack aStack) {
		return 10000;
	}

	@Override
	public int getPollutionPerTick(ItemStack aStack) {
		return 0;
	}

	@Override
	public int getDamageToComponent(ItemStack aStack) {
		return 0;
	}

	@Override
	public int getAmountOfOutputs() {
		return 1;
	}

	@Override
	public boolean explodesOnComponentBreak(ItemStack aStack) {
		return false;
	}

	public Block getCasingBlock() {
		return ModBlocks.blockCasingsMisc;
	}


	public byte getCasingMeta() {
		return 6;
	}


	public byte getCasingTextureIndex() {
		return 63;
	}

	private boolean addToMachineList(IGregTechTileEntity tTileEntity) {
		return ((addMaintenanceToMachineList(tTileEntity, getCasingTextureIndex())) || (addInputToMachineList(tTileEntity, getCasingTextureIndex())) || (addOutputToMachineList(tTileEntity, getCasingTextureIndex())) || (addMufflerToMachineList(tTileEntity, getCasingTextureIndex())));
	}

	private boolean addEnergyInputToMachineList(IGregTechTileEntity tTileEntity) {
		return ((addEnergyInputToMachineList(tTileEntity, getCasingTextureIndex())));
	}
}
