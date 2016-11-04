package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi;

import java.util.ArrayList;
import java.util.Arrays;

import gregtech.api.enums.Textures;
import gregtech.api.gui.GT_GUIContainer_MultiMachine;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Input;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.array.Pair;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

public class GregtechMetaTileEntity_MultiTank extends GregtechMeta_MultiBlockBase {
	private long		fluidStored				= 0;

	private short		multiblockCasingCount	= 0;
	private short		storageMultiplier		= this.getStorageMultiplier();
	private long		maximumFluidStorage		= this.getMaximumTankStorage();
	private FluidStack	internalStorageTank		= null;
	public GregtechMetaTileEntity_MultiTank(final int aID, final String aName, final String aNameRegional) {
		super(aID, aName, aNameRegional);
	}

	public GregtechMetaTileEntity_MultiTank(final String aName) {
		super(aName);
	}

	@Override
	public boolean checkMachine(final IGregTechTileEntity aBaseMetaTileEntity, final ItemStack aStack) {
		final int xDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetX;
		final int zDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetZ;
		if (!aBaseMetaTileEntity.getAirOffset(xDir, 0, zDir)) {
			Utils.LOG_INFO("Must be hollow.");
			return false;
		}
		int tAmount = 0;
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				for (int h = -1; h < 19; h++) {
					if (h != 0 || (xDir + i != 0 || zDir + j != 0) && (i != 0 || j != 0)) {
						final IGregTechTileEntity tTileEntity = aBaseMetaTileEntity
								.getIGregTechTileEntityOffset(xDir + i, h, zDir + j);
						if (!this.addMaintenanceToMachineList(tTileEntity, 68)
								&& !this.addInputToMachineList(tTileEntity, 68)
								&& !this.addOutputToMachineList(tTileEntity, 68)
								&& !this.addEnergyInputToMachineList(tTileEntity, 68)) {
							if (aBaseMetaTileEntity.getBlockOffset(xDir + i, h,
									zDir + j) != ModBlocks.blockCasingsMisc) {
								if (h < 3) {
									Utils.LOG_INFO("Casing Expected.");
									return false;
								}
								else if (h >= 3) {
									// Utils.LOG_WARNING("Your Multitank can be
									// 20 blocks tall.");
								}
							}
							if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j) != 11) {
								if (h < 3) {
									Utils.LOG_INFO("Wrong Meta.");
									return false;
								}
								else if (h >= 3) {
									// Utils.LOG_WARNING("Your Multitank can be
									// 20 blocks tall.");
								}
							}
							if (h < 3) {
								tAmount++;
							}
							else if (h >= 3) {
								if (aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j) == Blocks.air
										|| aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j)
												.getUnlocalizedName().contains("residual")) {
									Utils.LOG_INFO("Found air");
								}
								else {
									Utils.LOG_INFO("Layer " + (h + 2) + " is complete. Adding " + 64000 * 9
											+ "L storage to the tank.");
									tAmount++;
								}
							}
						}
					}
				}
			}
		}
		this.multiblockCasingCount = (short) tAmount;
		Utils.LOG_INFO("Your Multitank can be 20 blocks tall.");
		Utils.LOG_INFO("Casings Count: " + tAmount + " Valid Multiblock: " + (tAmount >= 16) + " Tank Storage Capacity:"
				+ this.getMaximumTankStorage() + "L");
		return tAmount >= 16;
	}

	@Override
	public boolean checkRecipe(final ItemStack aStack) {
		Utils.LOG_INFO("Okay");

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
		final FluidStack[] tFluids = Arrays.copyOfRange(tFluidList.toArray(new FluidStack[1]), 0, 1);

		if (tFluids.length >= 2) {
			Utils.LOG_INFO("Bad");
			return false;
		}

		final ArrayList<Pair<GT_MetaTileEntity_Hatch_Input, Boolean>> rList = new ArrayList<Pair<GT_MetaTileEntity_Hatch_Input, Boolean>>();
		int slotInputCount = 0;
		for (final GT_MetaTileEntity_Hatch_Input tHatch : this.mInputHatches) {
			boolean containsFluid = false;
			if (GregtechMeta_MultiBlockBase.isValidMetaTileEntity(tHatch)) {
				slotInputCount++;
				for (int i = 0; i < tHatch.getBaseMetaTileEntity().getSizeInventory(); i++) {
					if (tHatch.canTankBeEmptied()) {
						containsFluid = true;
					}
				}
				rList.add(new Pair<GT_MetaTileEntity_Hatch_Input, Boolean>(tHatch, containsFluid));
			}
		}
		if (tFluids.length <= 0 || slotInputCount > 1) {
			Utils.LOG_INFO("Bad");
			return false;
		}

		Utils.LOG_INFO("Okay - 2");
		if (this.internalStorageTank == null) {
			if (rList.get(0).getKey().mFluid != null && rList.get(0).getKey().mFluid.amount > 0) {
				Utils.LOG_INFO(
						"Okay - 1" + " rList.get(0).getKey().mFluid.amount: " + rList.get(0).getKey().mFluid.amount
								+ " internalStorageTank:" + this.internalStorageTank.amount);
				this.internalStorageTank = rList.get(0).getKey().mFluid;
				this.internalStorageTank.amount = rList.get(0).getKey().mFluid.amount;
				rList.get(0).getKey().mFluid.amount = 0;
				Utils.LOG_INFO(
						"Okay - 1.1" + " rList.get(0).getKey().mFluid.amount: " + rList.get(0).getKey().mFluid.amount
								+ " internalStorageTank:" + this.internalStorageTank.amount);
				return true;
			}
			Utils.LOG_INFO("No Fluid in hatch.");
			return false;
		}
		else if (this.internalStorageTank.isFluidEqual(rList.get(0).getKey().mFluid)) {
			Utils.LOG_INFO("Storing " + rList.get(0).getKey().mFluid.amount + "L");
			Utils.LOG_INFO("Contains " + this.internalStorageTank.amount + "L");

			int tempAdd = 0;
			tempAdd = rList.get(0).getKey().getFluidAmount();
			rList.get(0).getKey().mFluid = null;
			Utils.LOG_INFO("adding " + tempAdd);
			this.internalStorageTank.amount = this.internalStorageTank.amount + tempAdd;
			Utils.LOG_INFO("Tank now Contains " + this.internalStorageTank.amount + "L of "
					+ this.internalStorageTank.getFluid().getName() + ".");

			if (this.mOutputHatches.get(0).mFluid == null || this.mOutputHatches.isEmpty()) {
				Utils.LOG_INFO("Okay - 3");
				final int tempCurrentStored = this.internalStorageTank.amount;
				int tempSubtract = 0;
				int tempResult = 0;
				final int tempHatchSize = this.mOutputHatches.get(0).getCapacity();
				final FluidStack tempOutputFluid = this.internalStorageTank;
				if (tempHatchSize > tempCurrentStored) {
					Utils.LOG_INFO("Okay - 3.1.1" + " hatchCapacity: " + tempHatchSize + " tempCurrentStored:"
							+ tempCurrentStored);
					tempOutputFluid.amount = tempHatchSize;
					tempSubtract = tempHatchSize;
					tempResult = tempCurrentStored - tempSubtract;
					Utils.LOG_INFO(
							"Okay - 3.1.2" + " result: " + tempResult + " tempCurrentStored:" + tempCurrentStored);
					this.mOutputHatches.get(0).mFluid = tempOutputFluid;
					this.internalStorageTank.amount = tempResult;
				}
				else if (tempCurrentStored >= 5000) {
					Utils.LOG_INFO("Okay - 3.2");
					tempOutputFluid.amount = tempCurrentStored;
					tempSubtract = tempOutputFluid.amount;
					tempResult = tempCurrentStored - tempSubtract;
					this.mOutputHatches.get(0).mFluid = tempOutputFluid;
					this.internalStorageTank.amount = tempResult;
				}
				Utils.LOG_INFO("Tank");
				return true;
			}
			else if (this.mOutputHatches.get(0).mFluid.isFluidEqual(this.internalStorageTank)) {
				Utils.LOG_INFO("Okay - 4");
				final int tempCurrentStored = this.internalStorageTank.amount;
				int tempSubtract = 0;
				int tempResult = 0;
				final int tempHatchSize = this.mOutputHatches.get(0).getCapacity();
				final FluidStack tempOutputFluid = this.internalStorageTank;
				if (tempHatchSize > tempCurrentStored) {
					tempOutputFluid.amount = tempHatchSize;
					tempSubtract = tempOutputFluid.amount;
					tempResult = tempCurrentStored - tempSubtract;
					this.mOutputHatches.get(0).mFluid = tempOutputFluid;
					this.internalStorageTank.amount = tempResult;
				}
				else if (tempCurrentStored >= 5000) {
					tempOutputFluid.amount = tempCurrentStored;
					tempSubtract = tempOutputFluid.amount;
					tempResult = tempCurrentStored - tempSubtract;
					this.mOutputHatches.get(0).mFluid = tempOutputFluid;
					this.internalStorageTank.amount = tempResult;
				}
				Utils.LOG_INFO("Tank");
				return true;
			}
			Utils.LOG_INFO("Tank");
			return true;
		}
		else {
			Utils.LOG_INFO("Tank Contains " + this.internalStorageTank.amount + "L of "
					+ this.internalStorageTank.getFluid().getName() + ".");
		}
		// this.getBaseMetaTileEntity().(tFluids[0].amount, true);
		Utils.LOG_INFO("Tank");
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
		return new GT_GUIContainer_MultiMachine(aPlayerInventory, aBaseMetaTileEntity, this.getLocalName(),
				"VacuumFreezer.png");
	}

	@Override
	public String[] getDescription() {
		return new String[] {
				"Controller Block for the Multitank", "Size: 3xHx3 (Block behind controller must be air)",
				"Structure must be at least 4 blocks tall, maximum 20.",
				"Each casing within the structure adds 96000L storage.", "Controller (front centered)",
				"1x Input hatch (anywhere)", "1x Output hatch (anywhere)", "1x Energy Hatch (anywhere)",
				"1x Maintenance Hatch (anywhere)", "Multitank Exterior Casings for the rest (16 at least!)",
				"Stored Fluid: " + this.fluidStored
		};
	}

	@Override
	public int getMaxEfficiency(final ItemStack aStack) {
		return 10000;
	}

	private long getMaximumTankStorage() {
		final int multiplier = this.getStorageMultiplier();
		Utils.LOG_WARNING("x = " + multiplier + " * 96000");
		final long tempTankStorageMax = 96000 * multiplier;
		Utils.LOG_WARNING("x = " + tempTankStorageMax);
		if (tempTankStorageMax <= 0) {
			return 96000;
		}
		return tempTankStorageMax;
	}

	@Override
	public int getPollutionPerTick(final ItemStack aStack) {
		return 0;
	}

	@Override
	public GT_Recipe.GT_Recipe_Map getRecipeMap() {
		return null;
	}

	private short getStorageMultiplier() {
		final int tempstorageMultiplier = 1 * this.multiblockCasingCount;
		if (tempstorageMultiplier <= 0) {
			return 1;
		}
		return (short) tempstorageMultiplier;
	}

	@Override
	public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final byte aSide, final byte aFacing,
			final byte aColorIndex, final boolean aActive, final boolean aRedstone) {
		if (aSide == aFacing) {
			return new ITexture[] {
					Textures.BlockIcons.CASING_BLOCKS[68], new GT_RenderedTexture(aActive
							? TexturesGtBlock.Overlay_Machine_Screen_Logo : TexturesGtBlock.Overlay_Machine_Screen_Logo)
			};
		}
		return new ITexture[] {
				Textures.BlockIcons.CASING_BLOCKS[68]
		};
	}

	@Override
	public boolean isFacingValid(final byte aFacing) {
		return aFacing > 1;
	}

	@Override
	public void loadNBTData(final NBTTagCompound aNBT) {
		this.mEUt = aNBT.getInteger("mEUt");
		this.mProgresstime = aNBT.getInteger("mProgresstime");
		this.mMaxProgresstime = aNBT.getInteger("mMaxProgresstime");
		if (this.mMaxProgresstime > 0) {
			this.mRunningOnLoad = true;
		}
		this.mEfficiencyIncrease = aNBT.getInteger("mEfficiencyIncrease");
		this.mEfficiency = aNBT.getInteger("mEfficiency");
		this.mPollution = aNBT.getInteger("mPollution");
		this.mRuntime = aNBT.getInteger("mRuntime");
		this.fluidStored = aNBT.getLong("mFluidStored");
		this.storageMultiplier = aNBT.getShort("mStorageMultiplier");
		this.maximumFluidStorage = aNBT.getLong("mMaxFluidStored");
		this.multiblockCasingCount = aNBT.getShort("mCasingCount");
		this.mOutputItems = new ItemStack[this.getAmountOfOutputs()];
		for (int i = 0; i < this.mOutputItems.length; i++) {
			this.mOutputItems[i] = GT_Utility.loadItem(aNBT, "mOutputItem" + i);
		}
		this.mOutputFluids = new FluidStack[this.getAmountOfOutputs()];
		for (int i = 0; i < this.mOutputFluids.length; i++) {
			this.mOutputFluids[i] = GT_Utility.loadFluid(aNBT, "mOutputFluids" + i);
		}
		this.mWrench = aNBT.getBoolean("mWrench");
		this.mScrewdriver = aNBT.getBoolean("mScrewdriver");
		this.mSoftHammer = aNBT.getBoolean("mSoftHammer");
		this.mHardHammer = aNBT.getBoolean("mHardHammer");
		this.mSolderingTool = aNBT.getBoolean("mSolderingTool");
		this.mCrowbar = aNBT.getBoolean("mCrowbar");
	}

	@Override
	public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
		return new GregtechMetaTileEntity_MultiTank(this.mName);
	}

	@Override
	public void saveNBTData(final NBTTagCompound aNBT) {
		aNBT.setInteger("mEUt", this.mEUt);
		aNBT.setInteger("mProgresstime", this.mProgresstime);
		aNBT.setInteger("mMaxProgresstime", this.mMaxProgresstime);
		aNBT.setInteger("mEfficiencyIncrease", this.mEfficiencyIncrease);
		aNBT.setInteger("mEfficiency", this.mEfficiency);
		aNBT.setInteger("mPollution", this.mPollution);
		aNBT.setInteger("mRuntime", this.mRuntime);
		aNBT.setLong("mFluidStored", this.fluidStored);
		aNBT.setShort("mStorageMultiplier", this.storageMultiplier);
		aNBT.setLong("mMaxFluidStored", this.maximumFluidStorage);
		aNBT.setShort("mCasingCount", this.multiblockCasingCount);

		if (this.mOutputItems != null) {
			for (int i = 0; i < this.mOutputItems.length; i++) {
				if (this.mOutputItems[i] != null) {
					final NBTTagCompound tNBT = new NBTTagCompound();
					this.mOutputItems[i].writeToNBT(tNBT);
					aNBT.setTag("mOutputItem" + i, tNBT);
				}
			}
		}
		if (this.mOutputFluids != null) {
			for (int i = 0; i < this.mOutputFluids.length; i++) {
				if (this.mOutputFluids[i] != null) {
					final NBTTagCompound tNBT = new NBTTagCompound();
					this.mOutputFluids[i].writeToNBT(tNBT);
					aNBT.setTag("mOutputFluids" + i, tNBT);
				}
			}
		}

		aNBT.setBoolean("mWrench", this.mWrench);
		aNBT.setBoolean("mScrewdriver", this.mScrewdriver);
		aNBT.setBoolean("mSoftHammer", this.mSoftHammer);
		aNBT.setBoolean("mHardHammer", this.mHardHammer);
		aNBT.setBoolean("mSolderingTool", this.mSolderingTool);
		aNBT.setBoolean("mCrowbar", this.mCrowbar);
	}
}
