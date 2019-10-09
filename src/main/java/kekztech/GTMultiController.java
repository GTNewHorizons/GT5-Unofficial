package kekztech;

import java.util.ArrayList;

import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Dynamo;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Energy;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Input;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_InputBus;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Maintenance;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Muffler;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Output;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_OutputBus;
import gregtech.api.util.GT_Utility;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

/**
 * Only purpose of this class is to make me learn about GT multis :)
 * @author Kekzdealer
 *
 */
public abstract class GTMultiController {
	
	private final GTRecipe GT_RECIPE = new GTRecipe();
	
	private boolean running = false;
	private boolean structureValid = false;
	
	public final ArrayList<GT_MetaTileEntity_Hatch_Dynamo> mDynamoHatches = new ArrayList<>();
	public final ArrayList<GT_MetaTileEntity_Hatch_Energy> mEnergyHatches = new ArrayList<>();
	public final ArrayList<GT_MetaTileEntity_Hatch_Input> mInputHatches = new ArrayList<>();
	public final ArrayList<GT_MetaTileEntity_Hatch_Output> mOutputHatches = new ArrayList<>();
	public final ArrayList<GT_MetaTileEntity_Hatch_InputBus> mInputBusses = new ArrayList<>();
	public final ArrayList<GT_MetaTileEntity_Hatch_OutputBus> mOutputBusses = new ArrayList<>();
	public final ArrayList<GT_MetaTileEntity_Hatch_Muffler> mMufflerHatches = new ArrayList<>();
	public final ArrayList<GT_MetaTileEntity_Hatch_Maintenance> mMaintenanceHatches = new ArrayList<>();
	
	protected GTRecipe getGT_RECIPE() {
		return GT_RECIPE;
	}
	
	protected abstract boolean checkStructure();
	protected abstract String[] getDescription();
	
	protected long getMaxInputVoltage() {
		long voltage = 0L;
		for(GT_MetaTileEntity_Hatch_Energy energyHatch : mEnergyHatches) {
			voltage += energyHatch.getBaseMetaTileEntity().getInputVoltage();
		}
		return voltage;
	}
	
	private void updateDynamoHatches() {
		if(GT_RECIPE.getEuPerTick() > 0) {
			long remOutput = GT_RECIPE.getEuPerTick();
			for(GT_MetaTileEntity_Hatch_Dynamo dynamoHatch : mDynamoHatches) {
				final long deltaCapacity = dynamoHatch.getBaseMetaTileEntity().getEUCapacity()
						- dynamoHatch.getBaseMetaTileEntity().getStoredEU();
				final long toOutput = Math.min(deltaCapacity, GT_RECIPE.getEuPerTick());
				dynamoHatch.getBaseMetaTileEntity().increaseStoredEnergyUnits(toOutput, false);
				remOutput -= toOutput;
			}
		}
	}
	
	private void updateEnergyHatches() {
		if(GT_RECIPE.getEuPerTick() < 0) {
			long remConsumption = GT_RECIPE.getEuPerTick();
			for(GT_MetaTileEntity_Hatch_Energy energyHatch : mEnergyHatches) {
				if(remConsumption > 0) {
					final long toConsume = Math.min(remConsumption, energyHatch.getBaseMetaTileEntity().getStoredEU());
					energyHatch.getBaseMetaTileEntity().decreaseStoredEnergyUnits(toConsume, false);
					remConsumption -= toConsume;
				}
			}
		}
	}
	
	private void updateInputBusses() {
		if(GT_RECIPE.getInputItems() != null) {
			// Check for each input stack...
			for(ItemStack inputStack : GT_RECIPE.getInputItems()) {
				int remainingRequired = inputStack.stackSize;
				// ...Each slot in each input bus...
				for(GT_MetaTileEntity_Hatch_InputBus inputBus : mInputBusses) {
					for(int slot = 0; slot <= inputBus.getBaseMetaTileEntity().getSizeInventory(); slot++) {
						
						final ItemStack slotStack = inputBus.getBaseMetaTileEntity().getStackInSlot(slot);
						if(GT_Utility.isStackValid(slotStack)) {
							if(GT_Utility.areStacksEqual(inputStack, slotStack)) {
								// Found correct slot
								final int toConsume = Math.min(slotStack.stackSize, inputStack.stackSize);
								final ItemStack retrievedStack = inputBus.getBaseMetaTileEntity().decrStackSize(slot, toConsume);						
							}
						}
					}
				}
			}
		}
	}
	
	private void updateInputHatches() {
		if(GT_RECIPE.getInputFluids() != null) {
			// Check for each input stack
			for(FluidStack inputStack : GT_RECIPE.getInputFluids()) {
				int remainingRequired = inputStack.amount;
				// ...In each input hatch...
				for(GT_MetaTileEntity_Hatch_Input inputHatch : mInputHatches) {
					
					FluidStack slotStack = inputHatch.getFluid();
					if(slotStack.amount > 0 && slotStack.isFluidEqual(inputStack)) {
						// Found correct hatch
						final int toConsume = Math.min(slotStack.amount, remainingRequired);
						inputHatch.drain(toConsume, true);
						remainingRequired -= toConsume;
					}
				}
			}
		}
	}
	
	private void updateOutputBusses() {
		if(GT_RECIPE.getOutputItems() != null) {
			
		}
	}
	
	private void updateOutputHatches() {
		if(GT_RECIPE.getOutputFluids() != null) {
			// Find for each output stack...
			for(FluidStack outputStack : GT_RECIPE.getOutputFluids()) {
				// ...an output hatch that can accept the stack
				for(GT_MetaTileEntity_Hatch_Output outputHatch : mOutputHatches) {
					
				}
			}
		}
	}
	
	protected boolean tryAddHatch(IGregTechTileEntity hatch, int aBaseCasingIndex) {
		if (hatch == null) {
			return false;
		} else {
			final IMetaTileEntity aMetaTileEntity = hatch.getMetaTileEntity();
			if (aMetaTileEntity == null) {
				return false;
			} else {
				if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch) {
					((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
				}

				if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Input) {
					return this.mInputHatches.add((GT_MetaTileEntity_Hatch_Input) aMetaTileEntity);
				} else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_InputBus) {
					return this.mInputBusses.add((GT_MetaTileEntity_Hatch_InputBus) aMetaTileEntity);
				} else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Output) {
					return this.mOutputHatches.add((GT_MetaTileEntity_Hatch_Output) aMetaTileEntity);
				} else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_OutputBus) {
					return this.mOutputBusses.add((GT_MetaTileEntity_Hatch_OutputBus) aMetaTileEntity);
				} else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Energy) {
					return this.mEnergyHatches.add((GT_MetaTileEntity_Hatch_Energy) aMetaTileEntity);
				} else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Dynamo) {
					return this.mDynamoHatches.add((GT_MetaTileEntity_Hatch_Dynamo) aMetaTileEntity);
				} else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Maintenance) {
					return this.mMaintenanceHatches.add((GT_MetaTileEntity_Hatch_Maintenance) aMetaTileEntity);
				} else {
					return aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Muffler
							? this.mMufflerHatches.add((GT_MetaTileEntity_Hatch_Muffler) aMetaTileEntity)
							: false;
				}
			}
		}
	}
	
	protected boolean tryAddMaintenanceHatch(IGregTechTileEntity hatch, int aBaseCasingIndex) {
		if (hatch == null) {
			return false;
		} else {
			IMetaTileEntity aMetaTileEntity = hatch.getMetaTileEntity();
			if (aMetaTileEntity == null) {
				return false;
			} else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Maintenance) {
				((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
				return this.mMaintenanceHatches.add((GT_MetaTileEntity_Hatch_Maintenance) aMetaTileEntity);
			} else {
				return false;
			}
		}
	}
	
	protected boolean tryAddEnergyHatch(IGregTechTileEntity hatch, int aBaseCasingIndex) {
		if (hatch == null) {
			return false;
		} else {
			IMetaTileEntity aMetaTileEntity = hatch.getMetaTileEntity();
			if (aMetaTileEntity == null) {
				return false;
			} else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Energy) {
				((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
				return this.mEnergyHatches.add((GT_MetaTileEntity_Hatch_Energy) aMetaTileEntity);
			} else {
				return false;
			}
		}
	}

	protected boolean tryAddDynamoHatch(IGregTechTileEntity hatch, int aBaseCasingIndex) {
		if (hatch == null) {
			return false;
		} else {
			IMetaTileEntity aMetaTileEntity = hatch.getMetaTileEntity();
			if (aMetaTileEntity == null) {
				return false;
			} else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Dynamo) {
				((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
				return this.mDynamoHatches.add((GT_MetaTileEntity_Hatch_Dynamo) aMetaTileEntity);
			} else {
				return false;
			}
		}
	}

	protected boolean tryAddMufflerHatch(IGregTechTileEntity hatch, int aBaseCasingIndex) {
		if (hatch == null) {
			return false;
		} else {
			IMetaTileEntity aMetaTileEntity = hatch.getMetaTileEntity();
			if (aMetaTileEntity == null) {
				return false;
			} else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Muffler) {
				((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
				return this.mMufflerHatches.add((GT_MetaTileEntity_Hatch_Muffler) aMetaTileEntity);
			} else {
				return false;
			}
		}
	}

	protected boolean tryAddInputBus(IGregTechTileEntity hatch, int aBaseCasingIndex) {
		if (hatch == null) {
			return false;
		} else {
			IMetaTileEntity aMetaTileEntity = hatch.getMetaTileEntity();
			if (aMetaTileEntity == null) {
				return false;
			} else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_InputBus) {
				((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
				return this.mInputBusses.add((GT_MetaTileEntity_Hatch_InputBus) aMetaTileEntity);
			} else {
				return false;
			}
		}
	}
	
	protected boolean tryAddInputHatch(IGregTechTileEntity hatch, int aBaseCasingIndex) {
		if (hatch == null) {
			return false;
		} else {
			IMetaTileEntity aMetaTileEntity = hatch.getMetaTileEntity();
			if (aMetaTileEntity == null) {
				return false;
			} else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Input) {
				((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
				return this.mInputHatches.add((GT_MetaTileEntity_Hatch_Input) aMetaTileEntity);
			} else {
				return false;
			}
		}
	}

	protected boolean tryAddOutputBus(IGregTechTileEntity hatch, int aBaseCasingIndex) {
		if (hatch == null) {
			return false;
		} else {
			IMetaTileEntity aMetaTileEntity = hatch.getMetaTileEntity();
			if (aMetaTileEntity == null) {
				return false;
			} else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_OutputBus) {
				((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
				return this.mOutputBusses.add((GT_MetaTileEntity_Hatch_OutputBus) aMetaTileEntity);
			} else {
				return false;
			}
		}
	}
	
	protected boolean tryAddOutputHatch(IGregTechTileEntity hatch, int aBaseCasingIndex) {
		if (hatch == null) {
			return false;
		} else {
			IMetaTileEntity aMetaTileEntity = hatch.getMetaTileEntity();
			if (aMetaTileEntity == null) {
				return false;
			} else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Output) {
				((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
				return this.mOutputHatches.add((GT_MetaTileEntity_Hatch_Output) aMetaTileEntity);
			} else {
				return false;
			}
		}
	}
}
