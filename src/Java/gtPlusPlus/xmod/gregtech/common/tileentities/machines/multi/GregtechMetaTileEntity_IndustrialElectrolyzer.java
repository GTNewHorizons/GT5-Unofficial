package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi;

import java.util.*;

import org.apache.commons.lang3.ArrayUtils;

import gregtech.api.GregTech_API;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.xmod.gregtech.api.gui.GUI_MultiMachine;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import gtPlusPlus.xmod.gregtech.common.blocks.GregtechMetaCasingBlocks;
import net.minecraft.block.Block;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

public class GregtechMetaTileEntity_IndustrialElectrolyzer extends GregtechMeta_MultiBlockBase {
	ArrayList<ItemStack>	tInputList	= this.getStoredInputs();

	GT_Recipe				mLastRecipe;

	public GregtechMetaTileEntity_IndustrialElectrolyzer(final int aID, final String aName,
			final String aNameRegional) {
		super(aID, aName, aNameRegional);
	}

	public GregtechMetaTileEntity_IndustrialElectrolyzer(final String aName) {
		super(aName);
	}

	@Override
	public boolean checkMachine(final IGregTechTileEntity aBaseMetaTileEntity, final ItemStack aStack) {
		final int xDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetX;
		final int zDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetZ;
		if (!aBaseMetaTileEntity.getAirOffset(xDir, 0, zDir)) {
			return false;
		}
		int tAmount = 0;
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				for (int h = -1; h < 2; h++) {
					if (h != 0 || (xDir + i != 0 || zDir + j != 0) && (i != 0 || j != 0)) {
						final IGregTechTileEntity tTileEntity = aBaseMetaTileEntity
								.getIGregTechTileEntityOffset(xDir + i, h, zDir + j);
						if (!this.addMaintenanceToMachineList(tTileEntity, 62)
								&& !this.addMufflerToMachineList(tTileEntity, 62)
								&& !this.addInputToMachineList(tTileEntity, 62)
								&& !this.addOutputToMachineList(tTileEntity, 62)
								&& !this.addEnergyInputToMachineList(tTileEntity, 62)) {
							final Block tBlock = aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j);
							final byte tMeta = aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j);
							if (tBlock != ModBlocks.blockCasingsMisc || tMeta != 5) {
								return false;
							}
							tAmount++;
						}
					}
				}
			}
		}
		return tAmount >= 16;
	}

	@Override
	public boolean checkRecipe(final ItemStack aStack) { // TODO - Add Check to
															// make sure Fluid
															// output isn't full
		final ArrayList<ItemStack> tInputList = this.getStoredInputs();
		for (int i = 0; i < tInputList.size() - 1; i++) {
			for (int j = i + 1; j < tInputList.size(); j++) {
				if (GT_Utility.areStacksEqual(tInputList.get(i), tInputList.get(j))) {
					if (tInputList.get(i).stackSize >= tInputList.get(j).stackSize) {
						tInputList.remove(j--);
					}
					else {
						tInputList.remove(i--);
						break;
					}
				}
			}
		}
		final ItemStack[] tInputs = Arrays.copyOfRange(tInputList.toArray(new ItemStack[tInputList.size()]), 0, 2);

		final ArrayList<FluidStack> tFluidList = this.getStoredFluids();
		for (int i = 0; i < tFluidList.size() - 1; i++) {
			for (int j = i + 1; j < tFluidList.size(); j++) {
				if (GT_Utility.areFluidsEqual(tFluidList.get(i), tFluidList.get(j))) {
					if (tFluidList.get(i).amount >= tFluidList.get(j).amount) {
						tFluidList.remove(j--);
					}
					else {
						tFluidList.remove(i--);
						break;
					}
				}
			}
		}
		final FluidStack[] tFluids = Arrays.copyOfRange(tFluidList.toArray(new FluidStack[tInputList.size()]), 0, 1);

		final int tValidOutputSlots = this.getValidOutputSlots(this.getRecipeMap(), tInputs);
		Utils.LOG_WARNING("Valid Output Slots: " + tValidOutputSlots);

		// More than or one input
		if (tInputList.size() > 0 && tValidOutputSlots >= 1) {
			final long tVoltage = this.getMaxInputVoltage();
			final byte tTier = (byte) Math.max(1, GT_Utility.getTier(tVoltage));
			final GT_Recipe tRecipe = GT_Recipe.GT_Recipe_Map.sElectrolyzerRecipes.findRecipe(
					this.getBaseMetaTileEntity(), false, gregtech.api.enums.GT_Values.V[tTier], tFluids, tInputs);
			if (tRecipe != null && 7500 >= tRecipe.mSpecialValue
					&& tRecipe.isRecipeInputEqual(true, tFluids, tInputs)) {
				this.mEfficiency = 10000 - (this.getIdealStatus() - this.getRepairStatus()) * 1000;
				this.mEfficiencyIncrease = 10000;
				if (tRecipe.mEUt <= 16) {
					this.mEUt = tRecipe.mEUt * (1 << tTier - 1) * (1 << tTier - 1);
					this.mMaxProgresstime = tRecipe.mDuration / (1 << tTier - 1);
				}
				else {
					this.mEUt = tRecipe.mEUt;
					this.mMaxProgresstime = tRecipe.mDuration;
					while (this.mEUt <= gregtech.api.enums.GT_Values.V[tTier - 1]) {
						this.mEUt *= 4;
						this.mMaxProgresstime /= 2;
					}
				}
				if (this.mEUt > 0) {
					this.mEUt = -this.mEUt;
				}
				this.mMaxProgresstime = Math.max(1, this.mMaxProgresstime);

				ItemStack[] tOut = new ItemStack[tRecipe.mOutputs.length];
				for (int h = 0; h < tRecipe.mOutputs.length; h++) {
					tOut[h] = tRecipe.getOutput(h).copy();
					tOut[h].stackSize = 0;
				}
				FluidStack tFOut = null;
				if (tRecipe.getFluidOutput(0) != null) {
					tFOut = tRecipe.getFluidOutput(0).copy();
				}
				for (int f = 0; f < tOut.length; f++) {
					if (tRecipe.mOutputs[f] != null && tOut[f] != null) {
						for (int g = 0; g < 1; g++) {
							if (this.getBaseMetaTileEntity().getRandomNumber(10000) < tRecipe.getOutputChance(f)) {
								tOut[f].stackSize += tRecipe.mOutputs[f].stackSize;
							}
						}
					}
				}
				if (tFOut != null) {
					final int tSize = tFOut.amount;
					tFOut.amount = tSize * 1;
				}

				final List<ItemStack> overStacks = new ArrayList<ItemStack>();
				for (int f = 0; f < tOut.length; f++) {
					if (tOut[f].getMaxStackSize() < tOut[f].stackSize) {
						while (tOut[f].getMaxStackSize() < tOut[f].stackSize) {
							final ItemStack tmp = tOut[f].copy();
							tmp.stackSize = tmp.getMaxStackSize();
							tOut[f].stackSize = tOut[f].stackSize - tOut[f].getMaxStackSize();
							overStacks.add(tmp);
						}
					}
				}
				if (overStacks.size() > 0) {
					ItemStack[] tmp = new ItemStack[overStacks.size()];
					tmp = overStacks.toArray(tmp);
					tOut = ArrayUtils.addAll(tOut, tmp);
				}
				final List<ItemStack> tSList = new ArrayList<ItemStack>();
				for (final ItemStack tS : tOut) {
					if (tS.stackSize > 0) {
						tSList.add(tS);
					}
				}
				tOut = tSList.toArray(new ItemStack[tSList.size()]);
				this.mOutputItems = tOut;
				this.mOutputFluids = new FluidStack[] {
						tFOut
				};
				this.updateSlots();

				/*
				 * this.mOutputItems = new ItemStack[]{tRecipe.getOutput(0),
				 * tRecipe.getOutput(1)}; updateSlots();
				 */
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean explodesOnComponentBreak(final ItemStack aStack) {
		return false;
	}

	@Override
	public int getAmountOfOutputs() {
		return 1;
	}

	@Override
	public Object getClientGUI(final int aID, final InventoryPlayer aPlayerInventory,
			final IGregTechTileEntity aBaseMetaTileEntity) {
		return new GUI_MultiMachine(aPlayerInventory, aBaseMetaTileEntity, this.getLocalName(),
				"IndustrialElectrolyzer.png");
	}
	@Override
	public String[] getDescription() {
		return new String[] {
				"Controller Block for the Industrial Electrolyzer", "Size: 3x3x3 (Hollow)",
				"Controller (front centered)", "1x Input Bus (anywhere)", "1x Output Bus (anywhere)",
				"1x Input Hatch (anywhere)", "1x Output Hatch (anywhere)", "1x Energy Hatch (anywhere)",
				"1x Maintenance Hatch (anywhere)", "1x Muffler (anywhere)",
				"Electrolyzer Casings for the rest (16 at least!)", CORE.GT_Tooltip
		};
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
	public GT_Recipe.GT_Recipe_Map getRecipeMap() {
		return GT_Recipe.GT_Recipe_Map.sElectrolyzerRecipes;
	}

	@Override
	public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final byte aSide, final byte aFacing,
			final byte aColorIndex, final boolean aActive, final boolean aRedstone) {
		if (aSide == aFacing) {
			return new ITexture[] {
					Textures.BlockIcons.CASING_BLOCKS[GregtechMetaCasingBlocks.GTID + 5], new GT_RenderedTexture(aActive
							? Textures.BlockIcons.STEAM_TURBINE_SIDE_ACTIVE : Textures.BlockIcons.STEAM_TURBINE_SIDE)
			};
		}
		return new ITexture[] {
				Textures.BlockIcons.CASING_BLOCKS[GregtechMetaCasingBlocks.GTID + 5]
		};
	}

	@Override
	public boolean isFacingValid(final byte aFacing) {
		return aFacing > 1;
	}

	@Override
	public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
		return new GregtechMetaTileEntity_IndustrialElectrolyzer(this.mName);
	}

	@Override
	public void startSoundLoop(final byte aIndex, final double aX, final double aY, final double aZ) {
		super.startSoundLoop(aIndex, aX, aY, aZ);
		if (aIndex == 20) {
			GT_Utility.doSoundAtClient(GregTech_API.sSoundList.get(Integer.valueOf(5)), 10, 1.0F, aX, aY, aZ);
		}
	}
}
