package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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
	public String[] getTooltip() {
		return new String[]{
				"Controller Block for the Alloy Blast Smelter", //Outputs 144mb fluid for every inputStack.stackSize; Time to use those hot metals.
				"20% Faster than the Electric Blast Furnace",
				"Allows Complex GT++ alloys to be created",
				"Circuit for recipe goes in the Input Bus or GUI slot",
				"Size: 3x4x3 (Hollow)",
				"Blast Smelter Casings (10 at least!)",
				"Controller (front middle at bottom)",
				"16x Blast Smelter Heat Containment Coils (two middle Layers, hollow)",
				"1x Input bus",
				"1x Input Hatch (optional)",
				"1x Output Hatch",
				"1x Energy Hatch",
				};
	}

	@Override
	public String getSound() {
		return GregTech_API.sSoundList.get(Integer.valueOf(208));
	}

	@Override
	public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final byte aSide, final byte aFacing, final byte aColorIndex, final boolean aActive, final boolean aRedstone) {
		if (aSide == aFacing) {
			return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[TAE.GTPP_INDEX(15)], new GT_RenderedTexture(aActive ? TexturesGtBlock.Overlay_Machine_Controller_Advanced_Active : TexturesGtBlock.Overlay_Machine_Controller_Advanced)};
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
				if ((tRecipe != null) && (tRecipe.isRecipeInputEqual(true, tFluids, tInputs))) {
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
					List<ItemStack> tOutPutItems = new ArrayList<ItemStack>();
					for (ItemStack tOut : tRecipe.mOutputs) {
						if (ItemUtils.checkForInvalidItems(tOut)) {
							tOutPutItems.add(tOut);
						}	
					}
					if (tOutPutItems.size() > 0)
					this.mOutputItems = tOutPutItems.toArray(new ItemStack[tOutPutItems.size()]);
					this.updateSlots();
					return true;
				}
			}
		}
		Logger.WARNING("Failed to find some Valid Inputs or Clientside.");
		return false;
	}	
	
	@Override
	public int getMaxParallelRecipes() {
		return 1;
	}

	@Override
	public int getEuDiscountForParallelism() {
		return 0;
	}

	@Override
	public boolean checkMultiblock(final IGregTechTileEntity aBaseMetaTileEntity, final ItemStack aStack) {
		final int xDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetX;
		final int zDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetZ;

		this.mHeatingCapacity = 0;
		if (!aBaseMetaTileEntity.getAirOffset(xDir, 1, zDir)) {
			return false;
		}
		if (!aBaseMetaTileEntity.getAirOffset(xDir, 2, zDir)) {
			return false;
		}

		this.mHeatingCapacity = 20000;

		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				if ((i != 0) || (j != 0)) {
										
					//Coils 1
					if (!isValidBlockForStructure(null, TAE.GTPP_INDEX(1), false, aBaseMetaTileEntity.getBlockOffset(xDir + i, 1, zDir + j), (int) aBaseMetaTileEntity.getMetaIDOffset(xDir + i, 1, zDir + j), ModBlocks.blockCasingsMisc, 14)) {
						Logger.INFO("Heating Coils missing.");
						return false;
					}
					
					//Coils 2
					if (!isValidBlockForStructure(null, TAE.GTPP_INDEX(1), false, aBaseMetaTileEntity.getBlockOffset(xDir + i, 2, zDir + j), (int) aBaseMetaTileEntity.getMetaIDOffset(xDir + i, 2, zDir + j), ModBlocks.blockCasingsMisc, 14)) {
						Logger.INFO("Heating Coils missing.");
						return false;
					}	
				}
				
				//Top Layer
				final IGregTechTileEntity tTileEntity2 = aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir + i, 3, zDir + j);					
				if (!isValidBlockForStructure(tTileEntity2, TAE.GTPP_INDEX(15), true, aBaseMetaTileEntity.getBlockOffset(xDir + i, 3, zDir + j), (int) aBaseMetaTileEntity.getMetaIDOffset(xDir + i, 3, zDir + j), ModBlocks.blockCasingsMisc, 15)) {
					Logger.INFO("Top Layer missing.");
					return false;
				}
			}
		}
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				if (((xDir + i) != 0) || ((zDir + j) != 0)) {					
					//Bottom Layer
					final IGregTechTileEntity tTileEntity2 = aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir + i, 0, zDir + j);					
					if (!isValidBlockForStructure(tTileEntity2, TAE.GTPP_INDEX(15), true, aBaseMetaTileEntity.getBlockOffset(xDir + i, 0, zDir + j), (int) aBaseMetaTileEntity.getMetaIDOffset(xDir + i, 0, zDir + j), ModBlocks.blockCasingsMisc, 15)) {
						Logger.INFO("Bottom Layer missing.");
						return false;
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
