package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi;

import static gtPlusPlus.xmod.gregtech.common.blocks.GregtechMetaCasingBlocks.GTID;

import java.util.ArrayList;
import java.util.Arrays;

import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.*;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.xmod.gregtech.api.gui.GUI_MultiMachine;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

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
	public String[] getDescription() {
		return new String[]{"Processes Logs and Coal into Charcoal and Coal Coke.",
				"Controller Block for the Industrial Coke Oven",
				"Size: 3x3x3 (Hollow)",
				"Controller (front middle at bottom)",
				"8x Heat Resistant/Proof Coke Oven Casings (middle Layer, hollow)",
				"1x Input Hatch (one of bottom)",
				"1x Output Hatch (one of bottom)",
				"1x Input Bus (one of bottom)",
				"1x Output Bus (one of bottom)",
				"1x Energy Hatch (one of bottom)",
				"1x Maintenance Hatch (one of bottom)",
				"1x Muffler Hatch (top middle)",
				"Structural Coke Oven Casings for the rest",
				CORE.GT_Tooltip};
	}

	@Override
	public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final byte aSide, final byte aFacing, final byte aColorIndex, final boolean aActive, final boolean aRedstone) {
		if (aSide == aFacing) {
			return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[GTID+1], new GT_RenderedTexture(aActive ? Textures.BlockIcons.OVERLAY_FRONT_MULTI_SMELTER_ACTIVE : Textures.BlockIcons.OVERLAY_FRONT_MULTI_SMELTER)};
		}
		return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[GTID+1]};
	}

	@Override
	public Object getClientGUI(final int aID, final InventoryPlayer aPlayerInventory, final IGregTechTileEntity aBaseMetaTileEntity) {
		return new GUI_MultiMachine(aPlayerInventory, aBaseMetaTileEntity, this.getLocalName(), "CokeOven.png");
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
		final ArrayList<ItemStack> tInputList = this.getStoredInputs();
		for (int i = 0; i < (tInputList.size() - 1); i++) {
			for (int j = i + 1; j < tInputList.size(); j++) {
				if (GT_Utility.areStacksEqual(tInputList.get(i), tInputList.get(j))) {
					if (tInputList.get(i).stackSize >= tInputList.get(j).stackSize) {
						tInputList.remove(j--);
					} else {
						tInputList.remove(i--);
						break;
					}
				}
			}
		}
		final ItemStack[] tInputs = Arrays.copyOfRange(tInputList.toArray(new ItemStack[tInputList.size()]), 0, 2);

		final ArrayList<FluidStack> tFluidList = this.getStoredFluids();
		for (int i = 0; i < (tFluidList.size() - 1); i++) {
			for (int j = i + 1; j < tFluidList.size(); j++) {
				if (GT_Utility.areFluidsEqual(tFluidList.get(i), tFluidList.get(j))) {
					if (tFluidList.get(i).amount >= tFluidList.get(j).amount) {
						tFluidList.remove(j--);
					} else {
						tFluidList.remove(i--);
						break;
					}
				}
			}
		}
		final FluidStack[] tFluids = Arrays.copyOfRange(tFluidList.toArray(new FluidStack[tInputList.size()]), 0, 1);

		final int tValidOutputSlots = this.getValidOutputSlots(this.getRecipeMap(), tInputs);
		Utils.LOG_WARNING("Valid Output Slots: "+tValidOutputSlots);

		//More than or one input
		if ((tInputList.size() > 0) && (tValidOutputSlots >= 1)) {
			final long tVoltage = this.getMaxInputVoltage();
			final byte tTier = (byte) Math.max(1, GT_Utility.getTier(tVoltage));
			final GT_Recipe tRecipe = Recipe_GT.Gregtech_Recipe_Map.sCokeOvenRecipes.findRecipe(this.getBaseMetaTileEntity(), false, gregtech.api.enums.GT_Values.V[tTier], tFluids, tInputs);
			if ((tRecipe != null) && (this.mLevel >= tRecipe.mSpecialValue) && (tRecipe.isRecipeInputEqual(true, tFluids, tInputs))) {
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
				this.mOutputFluids = new FluidStack[]{tRecipe.getFluidOutput(0)};
				this.updateSlots();
				//Utils.LOG_INFO("Coke oven: True");
				return true;
			}
		}
		//Utils.LOG_INFO("Coke oven: False");
		return false;
	}
	/*public boolean checkRecipe(ItemStack aStack) {
        ArrayList<ItemStack> tInputList = getStoredInputs();
        if (!tInputList.isEmpty()) {
            byte tTier = (byte) Math.max(1, GT_Utility.getTier(getMaxInputVoltage()));

            int j = 0;
            this.mOutputItems = new ItemStack[12 * this.mLevel];
            for (int i = 0; (i < 100) && (j < this.mOutputItems.length); i++) {
                if (null != (this.mOutputItems[j] = GT_ModHandler.getSmeltingOutput((ItemStack) tInputList.get(i % tInputList.size()), true, null))) {
                    j++;
                }
            }
            if (j > 0) {
                this.mEfficiency = (10000 - (getIdealStatus() - getRepairStatus()) * 1000);
                this.mEfficiencyIncrease = 10000;

                this.mEUt = (-4 * (1 << tTier - 1) * (1 << tTier - 1) * this.mLevel);
                this.mMaxProgresstime = Math.max(1, 512 / (1 << tTier - 1));
            }
            updateSlots();
            return true;
        }
        return false;
    }*/

	@Override
	public boolean checkMachine(final IGregTechTileEntity aBaseMetaTileEntity, final ItemStack aStack) {
		final int xDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetX;
		final int zDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetZ;
		int xr = aBaseMetaTileEntity.getXCoord();
		int yr = aBaseMetaTileEntity.getYCoord();
		int zr = aBaseMetaTileEntity.getZCoord();
		this.mLevel = 0;
		if (!aBaseMetaTileEntity.getAirOffset(xDir, 1, zDir)) {
			return false;
		}
		this.addMufflerToMachineList(aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir, 2, zDir), GTID+1);

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
		this.mOutputItems = new ItemStack[12 * this.mLevel];
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				if ((i != 0) || (j != 0)) {
					if (aBaseMetaTileEntity.getBlockOffset(xDir + i, 1, zDir + j) != ModBlocks.blockCasingsMisc) {
						return false;
					}
					if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, 1, zDir + j) != tUsedMeta) {
						return false;
					}
					if (aBaseMetaTileEntity.getBlockOffset(xDir + i, 2, zDir + j) != ModBlocks.blockCasingsMisc) {
						return false;
					}
					if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, 2, zDir + j) != 1) {
						return false;
					}
				}
			}
		}
		for (int i = -1; i < 2; i++) {
			xr = aBaseMetaTileEntity.getXCoord();
			yr = aBaseMetaTileEntity.getYCoord();
			zr = aBaseMetaTileEntity.getZCoord();
			//Utils.LOG_WARNING("STEP 1 - x ["+xr+"]  y ["+yr+"]  z ["+zr+"]");
			for (int j = -1; j < 2; j++) {
				xr = aBaseMetaTileEntity.getXCoord();
				yr = aBaseMetaTileEntity.getYCoord();
				zr = aBaseMetaTileEntity.getZCoord();
				//Utils.LOG_WARNING("STEP 2 - x ["+xr+"]  y ["+yr+"]  z ["+zr+"]");
				if (((xDir + i) != 0) || ((zDir + j) != 0)) {
					xr = aBaseMetaTileEntity.getXCoord();
					yr = aBaseMetaTileEntity.getYCoord();
					zr = aBaseMetaTileEntity.getZCoord();
					//Utils.LOG_WARNING("STEP 3 - x ["+xr+"]  y ["+yr+"]  z ["+zr+"]");
					final IGregTechTileEntity tTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir + i, 0, zDir + j);
					if ((!this.addMaintenanceToMachineList(tTileEntity, GTID+1)) && (!this.addInputToMachineList(tTileEntity, GTID+1)) && (!this.addOutputToMachineList(tTileEntity, GTID+1)) && (!this.addEnergyInputToMachineList(tTileEntity, GTID+1))) {
						if (aBaseMetaTileEntity.getBlockOffset(xDir + i, 0, zDir + j) != ModBlocks.blockCasingsMisc) {
							return false;
						}
						if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, 0, zDir + j) != 1) {
							return false;
						}
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
		return 0;
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
