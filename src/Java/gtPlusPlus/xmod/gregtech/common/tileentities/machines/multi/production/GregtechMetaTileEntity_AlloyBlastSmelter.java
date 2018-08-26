package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production;

import java.util.ArrayList;
import java.util.Arrays;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

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
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

public class GregtechMetaTileEntity_AlloyBlastSmelter
extends GregtechMeta_MultiBlockBase {

	private int mHeatingCapacity = 0;
	private int mMode = 0;
	private boolean isUsingControllerCircuit = false;
	private static final Item circuit = CI.getNumberedCircuit(0).getItem();

	public GregtechMetaTileEntity_AlloyBlastSmelter(final int aID, final String aName, final String aNameRegional) {
		super(aID, aName, aNameRegional);
	}

	public GregtechMetaTileEntity_AlloyBlastSmelter(final String aName) {
		super(aName);
	}

	@Override
	public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
		return new GregtechMetaTileEntity_AlloyBlastSmelter(this.mName);
	}

	@Override
	public String getMachineType() {
		return "Alloy Smelter";
	}

	@Override
	public String[] getDescription() {
		return new String[]{
				"Controller Block for the Alloy Blast Smelter", //Outputs 144mb fluid for every inputStack.stackSize; Time to use those hot metals.
				"20% Faster than the Electric Blast Furnace",
				"Allows Complex GT++ alloys to be created",
				"Circuit for recipe goes in the Input Bus or GUI slot",
				"Size: 3x4x3 (Hollow)",
				"Controller (front middle at bottom)",
				"16x Blast Smelter Heat Containment Coils (two middle Layers, hollow)",
				"1x Input bus (one of bottom)",
				"1x Output Hatch (one of bottom)",
				"1x Energy Hatch (one of bottom)",
				"1x Maintenance Hatch (one of bottom)",
				"1x Muffler Hatch (top middle)",
				"1x Fluid Input Hatch (optional, top layer)",
				"Blast Smelter Casings for the rest",
				getPollutionTooltip(),
				getMachineTooltip(),
				CORE.GT_Tooltip};
	}

	@Override
	public String getSound() {
		return GregTech_API.sSoundList.get(Integer.valueOf(208));
	}

	@Override
	public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final byte aSide, final byte aFacing, final byte aColorIndex, final boolean aActive, final boolean aRedstone) {
		if (aSide == aFacing) {
			return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[TAE.GTPP_INDEX(15)], new GT_RenderedTexture(aActive ? Textures.BlockIcons.OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE_ACTIVE : Textures.BlockIcons.OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE)};
		}
		return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[TAE.GTPP_INDEX(15)]};
	}

	@Override
	public boolean hasSlotInGUI() {
		return true;
	}	

	@Override
	public boolean requiresVanillaGtGUI() {
		return true;
	}

	@Override
	public String getCustomGUIResourceName() {
		return "ElectricBlastFurnace";
	}	

	@Override
	public GT_Recipe.GT_Recipe_Map getRecipeMap() {
		return Recipe_GT.Gregtech_Recipe_Map.sAlloyBlastSmelterRecipes;
	}

	@Override
	public boolean isCorrectMachinePart(final ItemStack aStack) {
		if (this.getBaseMetaTileEntity().isServerSide()) {
			//Get Controller Circuit
			if (aStack != null && aStack.getItem() == circuit) {
				this.mMode = aStack.getItemDamage();	
				return this.isUsingControllerCircuit = true;
			}
			else {
				if (aStack == null) {
					this.isUsingControllerCircuit = false;
					return true; //Allowed empty
				}
				Logger.WARNING("Not circuit in GUI inputs.");
				return this.isUsingControllerCircuit = false;
			}
		}
		Logger.WARNING("No Circuit, clientside.");
		return this.isUsingControllerCircuit = false;
	}

	@Override
	public boolean isFacingValid(final byte aFacing) {
		return aFacing > 1;
	}

	@Override
	public boolean checkRecipe(final ItemStack aStack) {

		if (this.getBaseMetaTileEntity().isServerSide()) {
			//Get Controller Circuit
			this.isUsingControllerCircuit = isCorrectMachinePart(aStack);

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

			//Validity check
			if ((isUsingControllerCircuit && tInputList.size() < 1) || (!isUsingControllerCircuit && tInputList.size() < 2)) {
				Logger.WARNING("Not enough inputs.");
				return false;
			}
			else if (isUsingControllerCircuit  && tInputList.size() >= 1) {
				tInputList.add(CI.getNumberedCircuit(this.mMode));
			}


			final ItemStack[] tInputs = Arrays.copyOfRange(tInputList.toArray(new ItemStack[tInputList.size()]), 0, tInputList.size());

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
			if (tInputList.size() > 1) {
				final long tVoltage = this.getMaxInputVoltage();
				final byte tTier = (byte) Math.max(1, GT_Utility.getTier(tVoltage));
				final GT_Recipe tRecipe = Recipe_GT.Gregtech_Recipe_Map.sAlloyBlastSmelterRecipes.findRecipe(this.getBaseMetaTileEntity(), false, gregtech.api.enums.GT_Values.V[tTier], tFluids, tInputs);
				if ((tRecipe != null) && (this.mHeatingCapacity >= tRecipe.mSpecialValue) && (tRecipe.isRecipeInputEqual(true, tFluids, tInputs))) {
					Logger.WARNING("Found some Valid Inputs.");
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
					this.mOutputFluids = new FluidStack[]{tRecipe.getFluidOutput(0)};
					this.updateSlots();
					return true;
				}
			}
		}
		Logger.WARNING("Failed to find some Valid Inputs or Clientside.");
		return false;
	}

	@Override
	public boolean checkMachine(final IGregTechTileEntity aBaseMetaTileEntity, final ItemStack aStack) {
		final int xDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetX;
		final int zDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetZ;

		//this.mHeatingCapacity = 0;
		if (!aBaseMetaTileEntity.getAirOffset(xDir, 1, zDir)) {
			return false;
		}
		if (!aBaseMetaTileEntity.getAirOffset(xDir, 2, zDir)) {
			return false;
		}
		this.addMufflerToMachineList(aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir, 3, zDir), TAE.GTPP_INDEX(15));

		this.mHeatingCapacity = 20000;

		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				if ((i != 0) || (j != 0)) {
					if (aBaseMetaTileEntity.getBlockOffset(xDir + i, 2, zDir + j) != ModBlocks.blockCasingsMisc) {
						return false;
					}
					if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, 2, zDir + j) != 14) {
						return false;
					}
					if (aBaseMetaTileEntity.getBlockOffset(xDir + i, 1, zDir + j) != ModBlocks.blockCasingsMisc) {
						return false;
					}
					if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, 1, zDir + j) != 14) {
						return false;
					}
					if (!this.addFluidInputToMachineList(aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir + i, 3, zDir + j), TAE.GTPP_INDEX(15))) {
						if (aBaseMetaTileEntity.getBlockOffset(xDir + i, 3, zDir + j) != ModBlocks.blockCasingsMisc) {
							return false;
						}
						if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, 3, zDir + j) != 15) {
							return false;
						}
					}
				}
			}
		}
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				if (((xDir + i) != 0) || ((zDir + j) != 0)) {
					final IGregTechTileEntity tTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir + i, 0, zDir + j);
					if ((!this.addMaintenanceToMachineList(tTileEntity, TAE.GTPP_INDEX(15))) && (!this.addInputToMachineList(tTileEntity, TAE.GTPP_INDEX(15))) && (!this.addOutputToMachineList(tTileEntity, TAE.GTPP_INDEX(15))) && (!this.addEnergyInputToMachineList(tTileEntity, TAE.GTPP_INDEX(15)))) {
						if (aBaseMetaTileEntity.getBlockOffset(xDir + i, 0, zDir + j) != ModBlocks.blockCasingsMisc) {
							return false;
						}
						if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, 0, zDir + j) != 15) {
							return false;
						}
					}
				}
			}
		}
		this.mHeatingCapacity += 100 * (GT_Utility.getTier(this.getMaxInputVoltage()) - 2);

		if (	(this.mMaintenanceHatches.size() != 1) ||
				(this.mMufflerHatches.size() != 1) ||
				(this.mInputBusses.size() < 1) ||
				(this.mOutputHatches.size() < 1) ||
				(this.mEnergyHatches.size() != 1) )  {
			return false;
		}

		return true;
	}

	@Override
	public int getMaxEfficiency(final ItemStack aStack) {
		return 10000;
	}

	@Override
	public int getPollutionPerTick(final ItemStack aStack) {
		return 10;
	}

	@Override
	public int getDamageToComponent(final ItemStack aStack) {
		return 0;
	}

	@Override
	public int getAmountOfOutputs() {
		return 2;
	}

	@Override
	public boolean explodesOnComponentBreak(final ItemStack aStack) {
		return false;
	}
}
