package gtPlusPlus.core.util.debug;

import static gregtech.api.enums.GT_Values.V;

import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import gregtech.GT_Mod;
import gregtech.api.GregTech_API;
import gregtech.api.enums.ConfigCategories;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.gui.GT_Container_MultiMachine;
import gregtech.api.gui.GT_GUIContainer_MultiMachine;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.items.GT_MetaGenerated_Tool;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.*;
import gregtech.api.objects.GT_ItemStack;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Recipe.GT_Recipe_Map;
import gregtech.api.util.GT_Utility;
import gregtech.common.items.GT_MetaGenerated_Tool_01;

import net.minecraftforge.fluids.FluidStack;

public abstract class DEBUG_MULTIBLOCK_ShapeSpawner extends MetaTileEntity {

	public static boolean disableMaintenance;
	public boolean mMachine = false, mWrench = false, mScrewdriver = false, mSoftHammer = false, mHardHammer = false, mSolderingTool = false, mCrowbar = false, mRunningOnLoad = false;
	public int mPollution = 0, mProgresstime = 0, mMaxProgresstime = 0, mEUt = 0, mEfficiencyIncrease = 0, mUpdate = 0, mStartUpCheck = 100, mRuntime = 0, mEfficiency = 0;
	public ItemStack[] mOutputItems = null;
	public FluidStack[] mOutputFluids = null;
	public ArrayList<GT_MetaTileEntity_Hatch_Input> mInputHatches = new ArrayList<>();
	public ArrayList<GT_MetaTileEntity_Hatch_Output> mOutputHatches = new ArrayList<>();
	public ArrayList<GT_MetaTileEntity_Hatch_InputBus> mInputBusses = new ArrayList<>();
	public ArrayList<GT_MetaTileEntity_Hatch_OutputBus> mOutputBusses = new ArrayList<>();
	public ArrayList<GT_MetaTileEntity_Hatch_Dynamo> mDynamoHatches = new ArrayList<>();
	public ArrayList<GT_MetaTileEntity_Hatch_Muffler> mMufflerHatches = new ArrayList<>();
	public ArrayList<GT_MetaTileEntity_Hatch_Energy> mEnergyHatches = new ArrayList<>();
	public ArrayList<GT_MetaTileEntity_Hatch_Maintenance> mMaintenanceHatches = new ArrayList<>();

	public DEBUG_MULTIBLOCK_ShapeSpawner(final int aID, final String aName, final String aNameRegional) {
		super(aID, aName, aNameRegional, 2);
		DEBUG_MULTIBLOCK_ShapeSpawner.disableMaintenance = GregTech_API.sMachineFile.get(ConfigCategories.machineconfig, "MultiBlockMachines.disableMaintenance", false);
	}

	public DEBUG_MULTIBLOCK_ShapeSpawner(final String aName) {
		super(aName, 2);
		DEBUG_MULTIBLOCK_ShapeSpawner.disableMaintenance = GregTech_API.sMachineFile.get(ConfigCategories.machineconfig, "MultiBlockMachines.disableMaintenance", false);
	}

	public static boolean isValidMetaTileEntity(final MetaTileEntity aMetaTileEntity) {
		return (aMetaTileEntity.getBaseMetaTileEntity() != null) && (aMetaTileEntity.getBaseMetaTileEntity().getMetaTileEntity() == aMetaTileEntity) && !aMetaTileEntity.getBaseMetaTileEntity().isDead();
	}

	@Override
	public boolean allowCoverOnSide(final byte aSide, final GT_ItemStack aCoverID) {
		return aSide != this.getBaseMetaTileEntity().getFrontFacing();
	}

	@Override
	public boolean isSimpleMachine() {
		return false;
	}

	@Override
	public boolean isFacingValid(final byte aFacing) {
		return true;
	}

	@Override
	public boolean isAccessAllowed(final EntityPlayer aPlayer) {
		return true;
	}

	@Override
	public boolean isValidSlot(final int aIndex) {
		return aIndex > 0;
	}

	@Override
	public int getProgresstime() {
		return this.mProgresstime;
	}

	@Override
	public int maxProgresstime() {
		return this.mMaxProgresstime;
	}

	@Override
	public int increaseProgress(final int aProgress) {
		return aProgress;
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
	public boolean onRightclick(final IGregTechTileEntity aBaseMetaTileEntity, final EntityPlayer aPlayer) {
		if (aBaseMetaTileEntity.isClientSide()) {
			return true;
		}
		aBaseMetaTileEntity.openGUI(aPlayer);
		return true;
	}

	@Override
	public Object getServerGUI(final int aID, final InventoryPlayer aPlayerInventory, final IGregTechTileEntity aBaseMetaTileEntity) {
		return new GT_Container_MultiMachine(aPlayerInventory, aBaseMetaTileEntity);
	}

	@Override
	public Object getClientGUI(final int aID, final InventoryPlayer aPlayerInventory, final IGregTechTileEntity aBaseMetaTileEntity) {
		return new GT_GUIContainer_MultiMachine(aPlayerInventory, aBaseMetaTileEntity, this.getLocalName(), "MultiblockDisplay.png");
	}

	@Override
	public byte getTileEntityBaseType() {
		return 2;
	}

	@Override
	public void onMachineBlockUpdate() {
		this.mUpdate = 50;
	}

	@Override
	public void onPostTick(final IGregTechTileEntity aBaseMetaTileEntity, final long aTick) {
		if (aBaseMetaTileEntity.isServerSide()) {
			if (this.mEfficiency < 0) {
				this.mEfficiency = 0;
			}
			if ((--this.mUpdate == 0) || (--this.mStartUpCheck == 0)) {
				this.mInputHatches.clear();
				this.mInputBusses.clear();
				this.mOutputHatches.clear();
				this.mOutputBusses.clear();
				this.mDynamoHatches.clear();
				this.mEnergyHatches.clear();
				this.mMufflerHatches.clear();
				this.mMaintenanceHatches.clear();
				this.mMachine = this.checkMachine(aBaseMetaTileEntity, this.mInventory[1]);
			}
			if (this.mStartUpCheck < 0) {
				if (this.mMachine) {
					for (final GT_MetaTileEntity_Hatch_Maintenance tHatch : this.mMaintenanceHatches) {
						if (isValidMetaTileEntity(tHatch)) {
							if (!DEBUG_MULTIBLOCK_ShapeSpawner.disableMaintenance) {
								if (tHatch.mWrench) {
									this.mWrench = true;
								}
								if (tHatch.mScrewdriver) {
									this.mScrewdriver = true;
								}
								if (tHatch.mSoftHammer) {
									this.mSoftHammer = true;
								}
								if (tHatch.mHardHammer) {
									this.mHardHammer = true;
								}
								if (tHatch.mSolderingTool) {
									this.mSolderingTool = true;
								}
								if (tHatch.mCrowbar) {
									this.mCrowbar = true;
								}
							} else {
								this.mWrench = true;
								this.mScrewdriver = true;
								this.mSoftHammer = true;
								this.mHardHammer = true;
								this.mSolderingTool = true;
								this.mCrowbar = true;
							}

							tHatch.mWrench = false;
							tHatch.mScrewdriver = false;
							tHatch.mSoftHammer = false;
							tHatch.mHardHammer = false;
							tHatch.mSolderingTool = false;
							tHatch.mCrowbar = false;
						}
					}
					if (this.getRepairStatus() > 0) {
						if ((this.mMaxProgresstime > 0) && this.doRandomMaintenanceDamage()) {
							if (this.onRunningTick(this.mInventory[1])) {
								if (!this.polluteEnvironment(this.getPollutionPerTick(this.mInventory[1]))) {
									this.stopMachine();
								}
								if ((this.mMaxProgresstime > 0) && (++this.mProgresstime >= this.mMaxProgresstime)) {
									if (this.mOutputItems != null) {
										for (final ItemStack tStack : this.mOutputItems) {
											if (tStack != null) {
												try {
													GT_Mod.achievements.issueAchivementHatch(aBaseMetaTileEntity.getWorld().getPlayerEntityByName(aBaseMetaTileEntity.getOwnerName()), tStack);
												} catch (final Exception e) {
												}
												this.addOutput(tStack);
											}
										}
									}
									if ((this.mOutputFluids != null) && (this.mOutputFluids.length == 1)) {
										for (final FluidStack tStack : this.mOutputFluids) {
											if (tStack != null) {
												this.addOutput(tStack);
											}
										}
									} else if ((this.mOutputFluids != null) && (this.mOutputFluids.length > 1)) {
										this.addFluidOutputs(this.mOutputFluids);
									}
									this.mEfficiency = Math.max(0, Math.min(this.mEfficiency + this.mEfficiencyIncrease, this.getMaxEfficiency(this.mInventory[1]) - ((this.getIdealStatus() - this.getRepairStatus()) * 1000)));
									this.mOutputItems = null;
									this.mProgresstime = 0;
									this.mMaxProgresstime = 0;
									this.mEfficiencyIncrease = 0;
									if (aBaseMetaTileEntity.isAllowedToWork()) {
										this.checkRecipe(this.mInventory[1]);
									}
									if ((this.mOutputFluids != null) && (this.mOutputFluids.length > 0)) {
										if (this.mOutputFluids.length > 1) {
											GT_Mod.achievements.issueAchievement(aBaseMetaTileEntity.getWorld().getPlayerEntityByName(aBaseMetaTileEntity.getOwnerName()), "oilplant");
										}
									}
								}
							}
						} else {
							if (((aTick % 100) == 0) || aBaseMetaTileEntity.hasWorkJustBeenEnabled() || aBaseMetaTileEntity.hasInventoryBeenModified()) {

								if (aBaseMetaTileEntity.isAllowedToWork()) {
									this.checkRecipe(this.mInventory[1]);
								}
								if (this.mMaxProgresstime <= 0) {
									this.mEfficiency = Math.max(0, this.mEfficiency - 1000);
								}
							}
						}
					} else {
						this.stopMachine();
					}
				} else {
					this.stopMachine();
				}
			}
			aBaseMetaTileEntity.setErrorDisplayID((aBaseMetaTileEntity.getErrorDisplayID() & ~127) | (this.mWrench ? 0 : 1) | (this.mScrewdriver ? 0 : 2) | (this.mSoftHammer ? 0 : 4) | (this.mHardHammer ? 0 : 8) | (this.mSolderingTool ? 0 : 16) | (this.mCrowbar ? 0 : 32) | (this.mMachine ? 0 : 64));
			aBaseMetaTileEntity.setActive(this.mMaxProgresstime > 0);
		}
	}

	public boolean polluteEnvironment(final int aPollutionLevel) {
		this.mPollution += aPollutionLevel;
		for (final GT_MetaTileEntity_Hatch_Muffler tHatch : this.mMufflerHatches) {
			if (isValidMetaTileEntity(tHatch)) {
				if (this.mPollution >= 10000) {
					if (tHatch.polluteEnvironment()) {
						this.mPollution -= 10000;
					}
				} else {
					break;
				}
			}
		}
		return this.mPollution < 10000;
	}

	/**
	 * Called every tick the Machine runs
	 */
	public boolean onRunningTick(final ItemStack aStack) {
		if (this.mEUt > 0) {
			this.addEnergyOutput(((long) this.mEUt * this.mEfficiency) / 10000);
			return true;
		}
		if (this.mEUt < 0) {
			if (!this.drainEnergyInput(((long) -this.mEUt * 10000) / Math.max(1000, this.mEfficiency))) {
				this.stopMachine();
				return false;
			}
		}
		return true;
	}

	/**
	 * Checks if this is a Correct Machine Part for this kind of Machine (Turbine Rotor for example)
	 */
	public abstract boolean isCorrectMachinePart(ItemStack aStack);

	/**
	 * Checks the Recipe
	 */
	public abstract boolean checkRecipe(ItemStack aStack);

	/**
	 * Checks the Machine. You have to assign the MetaTileEntities for the Hatches here.
	 */
	public abstract boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack);

	/**
	 * Gets the maximum Efficiency that spare Part can get (0 - 10000)
	 */
	public abstract int getMaxEfficiency(ItemStack aStack);

	/**
	 * Gets the pollution this Device outputs to a Muffler per tick (10000 = one Pullution Block)
	 */
	public abstract int getPollutionPerTick(ItemStack aStack);

	/**
	 * Gets the damage to the ItemStack, usually 0 or 1.
	 */
	public abstract int getDamageToComponent(ItemStack aStack);

	/**
	 * Gets the Amount of possibly outputted Items for loading the Output Stack Array from NBT.
	 * This should be the largest Amount that can ever happen legitimately.
	 */
	public abstract int getAmountOfOutputs();

	/**
	 * If it explodes when the Component has to be replaced.
	 */
	public abstract boolean explodesOnComponentBreak(ItemStack aStack);

	public void stopMachine() {
		this.mOutputItems = null;
		this.mEUt = 0;
		this.mEfficiency = 0;
		this.mProgresstime = 0;
		this.mMaxProgresstime = 0;
		this.mEfficiencyIncrease = 0;
		this.getBaseMetaTileEntity().disableWorking();
	}

	public int getRepairStatus() {
		return (this.mWrench ? 1 : 0) + (this.mScrewdriver ? 1 : 0) + (this.mSoftHammer ? 1 : 0) + (this.mHardHammer ? 1 : 0) + (this.mSolderingTool ? 1 : 0) + (this.mCrowbar ? 1 : 0);
	}

	public int getIdealStatus() {
		return 6;
	}

	public boolean doRandomMaintenanceDamage() {
		if (!this.isCorrectMachinePart(this.mInventory[1]) || (this.getRepairStatus() == 0)) {
			this.stopMachine();
			return false;
		}
		if (this.mRuntime++ > 1000) {
			this.mRuntime = 0;
			if (this.getBaseMetaTileEntity().getRandomNumber(6000) == 0) {
				switch (this.getBaseMetaTileEntity().getRandomNumber(6)) {
				case 0:
					this.mWrench = false;
					break;
				case 1:
					this.mScrewdriver = false;
					break;
				case 2:
					this.mSoftHammer = false;
					break;
				case 3:
					this.mHardHammer = false;
					break;
				case 4:
					this.mSolderingTool = false;
					break;
				case 5:
					this.mCrowbar = false;
					break;
				}
			}
			if ((this.mInventory[1] != null) && (this.getBaseMetaTileEntity().getRandomNumber(2) == 0) && !this.mInventory[1].getUnlocalizedName().startsWith("gt.blockmachines.basicmachine.")) {
				if (this.mInventory[1].getItem() instanceof GT_MetaGenerated_Tool_01) {
					final NBTTagCompound tNBT = this.mInventory[1].getTagCompound();
					if (tNBT != null) {
						NBTTagCompound tNBT2 = tNBT.getCompoundTag("GT.CraftingComponents");
						if (!tNBT.getBoolean("mDis")) {
							tNBT2 = new NBTTagCompound();
							final Materials tMaterial = GT_MetaGenerated_Tool.getPrimaryMaterial(this.mInventory[1]);
							final ItemStack tTurbine = GT_OreDictUnificator.get(OrePrefixes.turbineBlade, tMaterial, 1);
							final int i = this.mInventory[1].getItemDamage();
							if (i == 170) {
								ItemStack tStack = GT_Utility.copyAmount(1, tTurbine);
								tNBT2.setTag("Ingredient.0", tStack.writeToNBT(new NBTTagCompound()));
								tNBT2.setTag("Ingredient.1", tStack.writeToNBT(new NBTTagCompound()));
								tNBT2.setTag("Ingredient.2", tStack.writeToNBT(new NBTTagCompound()));
								tNBT2.setTag("Ingredient.3", tStack.writeToNBT(new NBTTagCompound()));
								tStack = GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.Magnalium, 1);
								tNBT2.setTag("Ingredient.4", tStack.writeToNBT(new NBTTagCompound()));
							} else if (i == 172) {
								ItemStack tStack = GT_Utility.copyAmount(1, tTurbine);
								tNBT2.setTag("Ingredient.0", tStack.writeToNBT(new NBTTagCompound()));
								tNBT2.setTag("Ingredient.1", tStack.writeToNBT(new NBTTagCompound()));
								tNBT2.setTag("Ingredient.2", tStack.writeToNBT(new NBTTagCompound()));
								tNBT2.setTag("Ingredient.3", tStack.writeToNBT(new NBTTagCompound()));
								tNBT2.setTag("Ingredient.5", tStack.writeToNBT(new NBTTagCompound()));
								tNBT2.setTag("Ingredient.6", tStack.writeToNBT(new NBTTagCompound()));
								tNBT2.setTag("Ingredient.7", tStack.writeToNBT(new NBTTagCompound()));
								tNBT2.setTag("Ingredient.8", tStack.writeToNBT(new NBTTagCompound()));
								tStack = GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.Titanium, 1);
								tNBT2.setTag("Ingredient.4", tStack.writeToNBT(new NBTTagCompound()));
							} else if (i == 174) {
								ItemStack tStack = GT_Utility.copyAmount(2, tTurbine);
								tNBT2.setTag("Ingredient.0", tStack.writeToNBT(new NBTTagCompound()));
								tNBT2.setTag("Ingredient.1", tStack.writeToNBT(new NBTTagCompound()));
								tNBT2.setTag("Ingredient.2", tStack.writeToNBT(new NBTTagCompound()));
								tNBT2.setTag("Ingredient.3", tStack.writeToNBT(new NBTTagCompound()));
								tNBT2.setTag("Ingredient.5", tStack.writeToNBT(new NBTTagCompound()));
								tNBT2.setTag("Ingredient.6", tStack.writeToNBT(new NBTTagCompound()));
								tStack = GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.TungstenSteel, 1);
								tNBT2.setTag("Ingredient.4", tStack.writeToNBT(new NBTTagCompound()));
							} else if (i == 176) {
								ItemStack tStack = GT_Utility.copyAmount(2, tTurbine);
								tNBT2.setTag("Ingredient.0", tStack.writeToNBT(new NBTTagCompound()));
								tNBT2.setTag("Ingredient.1", tStack.writeToNBT(new NBTTagCompound()));
								tNBT2.setTag("Ingredient.2", tStack.writeToNBT(new NBTTagCompound()));
								tNBT2.setTag("Ingredient.3", tStack.writeToNBT(new NBTTagCompound()));
								tNBT2.setTag("Ingredient.5", tStack.writeToNBT(new NBTTagCompound()));
								tNBT2.setTag("Ingredient.6", tStack.writeToNBT(new NBTTagCompound()));
								tNBT2.setTag("Ingredient.7", tStack.writeToNBT(new NBTTagCompound()));
								tNBT2.setTag("Ingredient.8", tStack.writeToNBT(new NBTTagCompound()));
								tStack = GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.Americium, 1);
								tNBT2.setTag("Ingredient.4", tStack.writeToNBT(new NBTTagCompound()));
							}
							tNBT.setTag("GT.CraftingComponents", tNBT2);
							tNBT.setBoolean("mDis", true);
							this.mInventory[1].setTagCompound(tNBT);

						}
					}

					((GT_MetaGenerated_Tool) this.mInventory[1].getItem()).doDamage(this.mInventory[1], (long) Math.min(this.mEUt / 5, Math.pow(this.mEUt, 0.7)));
					if (this.mInventory[1].stackSize == 0) {
						this.mInventory[1] = null;
					}
				}
			}
		}
		return true;
	}

	public void explodeMultiblock() {
		this.mInventory[1] = null;
		for (final MetaTileEntity tTileEntity : this.mInputBusses) {
			tTileEntity.getBaseMetaTileEntity().doExplosion(V[8]);
		}
		for (final MetaTileEntity tTileEntity : this.mOutputBusses) {
			tTileEntity.getBaseMetaTileEntity().doExplosion(V[8]);
		}
		for (final MetaTileEntity tTileEntity : this.mInputHatches) {
			tTileEntity.getBaseMetaTileEntity().doExplosion(V[8]);
		}
		for (final MetaTileEntity tTileEntity : this.mOutputHatches) {
			tTileEntity.getBaseMetaTileEntity().doExplosion(V[8]);
		}
		for (final MetaTileEntity tTileEntity : this.mDynamoHatches) {
			tTileEntity.getBaseMetaTileEntity().doExplosion(V[8]);
		}
		for (final MetaTileEntity tTileEntity : this.mMufflerHatches) {
			tTileEntity.getBaseMetaTileEntity().doExplosion(V[8]);
		}
		for (final MetaTileEntity tTileEntity : this.mEnergyHatches) {
			tTileEntity.getBaseMetaTileEntity().doExplosion(V[8]);
		}
		for (final MetaTileEntity tTileEntity : this.mMaintenanceHatches) {
			tTileEntity.getBaseMetaTileEntity().doExplosion(V[8]);
		}
		this.getBaseMetaTileEntity().doExplosion(V[8]);
	}

	public boolean addEnergyOutput(final long aEU) {
		if (aEU <= 0) {
			return true;
		}
		for (final GT_MetaTileEntity_Hatch_Dynamo tHatch : this.mDynamoHatches) {
			if (isValidMetaTileEntity(tHatch)) {
				if (tHatch.getBaseMetaTileEntity().increaseStoredEnergyUnits(aEU, false)) {
					return true;
				}
			}
		}
		return false;
	}

	public long getMaxInputVoltage() {
		long rVoltage = 0;
		for (final GT_MetaTileEntity_Hatch_Energy tHatch : this.mEnergyHatches) {
			if (isValidMetaTileEntity(tHatch)) {
				rVoltage += tHatch.getBaseMetaTileEntity().getInputVoltage();
			}
		}
		return rVoltage;
	}

	public boolean drainEnergyInput(final long aEU) {
		if (aEU <= 0) {
			return true;
		}
		for (final GT_MetaTileEntity_Hatch_Energy tHatch : this.mEnergyHatches) {
			if (isValidMetaTileEntity(tHatch)) {
				if (tHatch.getBaseMetaTileEntity().decreaseStoredEnergyUnits(aEU, false)) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean addOutput(final FluidStack aLiquid) {
		if (aLiquid == null) {
			return false;
		}
		final FluidStack tLiquid = aLiquid.copy();
		for (final GT_MetaTileEntity_Hatch_Output tHatch : this.mOutputHatches) {
			if (isValidMetaTileEntity(tHatch) && GT_ModHandler.isSteam(aLiquid) ? tHatch.outputsSteam() : tHatch.outputsLiquids()) {
				final int tAmount = tHatch.fill(tLiquid, false);
				if (tAmount >= tLiquid.amount) {
					return tHatch.fill(tLiquid, true) >= tLiquid.amount;
				} else if (tAmount > 0) {
					tLiquid.amount = tLiquid.amount - tHatch.fill(tLiquid, true);
				}
			}
		}
		return false;
	}

	private void addFluidOutputs(final FluidStack[] mOutputFluids2) {
		for (int i = 0; i < mOutputFluids2.length; i++) {
			if ((this.mOutputHatches.size() > i) && (this.mOutputHatches.get(i) != null) && (mOutputFluids2[i] != null) && isValidMetaTileEntity(this.mOutputHatches.get(i))) {
				this.mOutputHatches.get(i).fill(mOutputFluids2[i], true);
			}
		}

	}

	public boolean depleteInput(final FluidStack aLiquid) {
		if (aLiquid == null) {
			return false;
		}
		for (final GT_MetaTileEntity_Hatch_Input tHatch : this.mInputHatches) {
			tHatch.mRecipeMap = this.getRecipeMap();
			if (isValidMetaTileEntity(tHatch)) {
				FluidStack tLiquid = tHatch.getFluid();
				if ((tLiquid != null) && tLiquid.isFluidEqual(aLiquid)) {
					tLiquid = tHatch.drain(aLiquid.amount, false);
					if ((tLiquid != null) && (tLiquid.amount >= aLiquid.amount)) {
						tLiquid = tHatch.drain(aLiquid.amount, true);
						return (tLiquid != null) && (tLiquid.amount >= aLiquid.amount);
					}
				}
			}
		}
		return false;
	}

	public boolean addOutput(ItemStack aStack) {
		if (GT_Utility.isStackInvalid(aStack)) {
			return false;
		}
		aStack = GT_Utility.copy(aStack);
		//		FluidStack aLiquid = GT_Utility.getFluidForFilledItem(aStack, true);
		//		if (aLiquid == null) {
		for (final GT_MetaTileEntity_Hatch_OutputBus tHatch : this.mOutputBusses) {
			if (isValidMetaTileEntity(tHatch)) {
				for (int i = tHatch.getSizeInventory() - 1; i >= 0; i--) {
					if (tHatch.getBaseMetaTileEntity().addStackToSlot(i, aStack)) {
						return true;
					}
				}
			}
		}
		for (final GT_MetaTileEntity_Hatch_Output tHatch : this.mOutputHatches) {
			if (isValidMetaTileEntity(tHatch) && tHatch.outputsItems()) {
				if (tHatch.getBaseMetaTileEntity().addStackToSlot(1, aStack)) {
					return true;
				}
			}
		}
		//		}else {
		//			for (GT_MetaTileEntity_Hatch_Output tHatch : mOutputHatches) {
		//				if (isValidMetaTileEntity(tHatch) && GT_ModHandler.isSteam(aLiquid)?tHatch.outputsSteam():tHatch.outputsLiquids()) {
		//					int tAmount = tHatch.fill(aLiquid, false);
		//					if (tAmount >= aLiquid.amount) {
		//						return tHatch.fill(aLiquid, true) >= aLiquid.amount;
		//					}
		//				}
		//			}
		//		}
		return false;
	}

	public boolean depleteInput(final ItemStack aStack) {
		if (GT_Utility.isStackInvalid(aStack)) {
			return false;
		}
		final FluidStack aLiquid = GT_Utility.getFluidForFilledItem(aStack, true);
		if (aLiquid != null) {
			return this.depleteInput(aLiquid);
		}
		for (final GT_MetaTileEntity_Hatch_Input tHatch : this.mInputHatches) {
			tHatch.mRecipeMap = this.getRecipeMap();
			if (isValidMetaTileEntity(tHatch)) {
				if (GT_Utility.areStacksEqual(aStack, tHatch.getBaseMetaTileEntity().getStackInSlot(0))) {
					if (tHatch.getBaseMetaTileEntity().getStackInSlot(0).stackSize >= aStack.stackSize) {
						tHatch.getBaseMetaTileEntity().decrStackSize(0, aStack.stackSize);
						return true;
					}
				}
			}
		}
		for (final GT_MetaTileEntity_Hatch_InputBus tHatch : this.mInputBusses) {
			tHatch.mRecipeMap = this.getRecipeMap();
			if (isValidMetaTileEntity(tHatch)) {
				for (int i = tHatch.getBaseMetaTileEntity().getSizeInventory() - 1; i >= 0; i--) {
					if (GT_Utility.areStacksEqual(aStack, tHatch.getBaseMetaTileEntity().getStackInSlot(i))) {
						if (tHatch.getBaseMetaTileEntity().getStackInSlot(0).stackSize >= aStack.stackSize) {
							tHatch.getBaseMetaTileEntity().decrStackSize(0, aStack.stackSize);
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	public ArrayList<ItemStack> getStoredOutputs() {
		final ArrayList<ItemStack> rList = new ArrayList<>();
		for (final GT_MetaTileEntity_Hatch_Output tHatch : this.mOutputHatches) {
			if (isValidMetaTileEntity(tHatch)) {
				rList.add(tHatch.getBaseMetaTileEntity().getStackInSlot(1));
			}
		}
		for (final GT_MetaTileEntity_Hatch_OutputBus tHatch : this.mOutputBusses) {
			if (isValidMetaTileEntity(tHatch)) {
				for (int i = tHatch.getBaseMetaTileEntity().getSizeInventory() - 1; i >= 0; i--) {
					rList.add(tHatch.getBaseMetaTileEntity().getStackInSlot(i));
				}
			}
		}
		return rList;
	}

	public ArrayList<FluidStack> getStoredFluids() {
		final ArrayList<FluidStack> rList = new ArrayList<>();
		for (final GT_MetaTileEntity_Hatch_Input tHatch : this.mInputHatches) {
			tHatch.mRecipeMap = this.getRecipeMap();
			if (isValidMetaTileEntity(tHatch) && (tHatch.getFillableStack() != null)) {
				rList.add(tHatch.getFillableStack());
			}
		}
		return rList;
	}

	public ArrayList<ItemStack> getStoredInputs() {
		final ArrayList<ItemStack> rList = new ArrayList<>();
		for (final GT_MetaTileEntity_Hatch_Input tHatch : this.mInputHatches) {
			tHatch.mRecipeMap = this.getRecipeMap();
			if (isValidMetaTileEntity(tHatch) && (tHatch.getBaseMetaTileEntity().getStackInSlot(0) != null)) {
				rList.add(tHatch.getBaseMetaTileEntity().getStackInSlot(0));
			}
		}
		for (final GT_MetaTileEntity_Hatch_InputBus tHatch : this.mInputBusses) {
			tHatch.mRecipeMap = this.getRecipeMap();
			if (isValidMetaTileEntity(tHatch)) {
				for (int i = tHatch.getBaseMetaTileEntity().getSizeInventory() - 1; i >= 0; i--) {
					if (tHatch.getBaseMetaTileEntity().getStackInSlot(i) != null) {
						rList.add(tHatch.getBaseMetaTileEntity().getStackInSlot(i));
					}
				}
			}
		}
		return rList;
	}

	public GT_Recipe_Map getRecipeMap() {
		return null;
	}

	public void updateSlots() {
		for (final GT_MetaTileEntity_Hatch_Input tHatch : this.mInputHatches) {
			if (isValidMetaTileEntity(tHatch)) {
				tHatch.updateSlots();
			}
		}
		for (final GT_MetaTileEntity_Hatch_InputBus tHatch : this.mInputBusses) {
			if (isValidMetaTileEntity(tHatch)) {
				tHatch.updateSlots();
			}
		}
	}

	public boolean addToMachineList(final IGregTechTileEntity aTileEntity, final int aBaseCasingIndex) {
		if (aTileEntity == null) {
			return false;
		}
		final IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
		if (aMetaTileEntity == null) {
			return false;
		}
		if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch) {
			((GT_MetaTileEntity_Hatch) aMetaTileEntity).mMachineBlock = (byte) aBaseCasingIndex;
		}
		if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Input) {
			return this.mInputHatches.add((GT_MetaTileEntity_Hatch_Input) aMetaTileEntity);
		}
		if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_InputBus) {
			return this.mInputBusses.add((GT_MetaTileEntity_Hatch_InputBus) aMetaTileEntity);
		}
		if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Output) {
			return this.mOutputHatches.add((GT_MetaTileEntity_Hatch_Output) aMetaTileEntity);
		}
		if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_OutputBus) {
			return this.mOutputBusses.add((GT_MetaTileEntity_Hatch_OutputBus) aMetaTileEntity);
		}
		if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Energy) {
			return this.mEnergyHatches.add((GT_MetaTileEntity_Hatch_Energy) aMetaTileEntity);
		}
		if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Dynamo) {
			return this.mDynamoHatches.add((GT_MetaTileEntity_Hatch_Dynamo) aMetaTileEntity);
		}
		if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Maintenance) {
			return this.mMaintenanceHatches.add((GT_MetaTileEntity_Hatch_Maintenance) aMetaTileEntity);
		}
		if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Muffler) {
			return this.mMufflerHatches.add((GT_MetaTileEntity_Hatch_Muffler) aMetaTileEntity);
		}
		return false;
	}

	public boolean addMaintenanceToMachineList(final IGregTechTileEntity aTileEntity, final int aBaseCasingIndex) {
		if (aTileEntity == null) {
			return false;
		}
		final IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
		if (aMetaTileEntity == null) {
			return false;
		}
		if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Maintenance) {
			((GT_MetaTileEntity_Hatch) aMetaTileEntity).mMachineBlock = (byte) aBaseCasingIndex;
			return this.mMaintenanceHatches.add((GT_MetaTileEntity_Hatch_Maintenance) aMetaTileEntity);
		}
		return false;
	}

	public boolean addEnergyInputToMachineList(final IGregTechTileEntity aTileEntity, final int aBaseCasingIndex) {
		if (aTileEntity == null) {
			return false;
		}
		final IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
		if (aMetaTileEntity == null) {
			return false;
		}
		if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Energy) {
			((GT_MetaTileEntity_Hatch) aMetaTileEntity).mMachineBlock = (byte) aBaseCasingIndex;
			return this.mEnergyHatches.add((GT_MetaTileEntity_Hatch_Energy) aMetaTileEntity);
		}
		return false;
	}

	public boolean addDynamoToMachineList(final IGregTechTileEntity aTileEntity, final int aBaseCasingIndex) {
		if (aTileEntity == null) {
			return false;
		}
		final IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
		if (aMetaTileEntity == null) {
			return false;
		}
		if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Dynamo) {
			((GT_MetaTileEntity_Hatch) aMetaTileEntity).mMachineBlock = (byte) aBaseCasingIndex;
			return this.mDynamoHatches.add((GT_MetaTileEntity_Hatch_Dynamo) aMetaTileEntity);
		}
		return false;
	}

	public boolean addMufflerToMachineList(final IGregTechTileEntity aTileEntity, final int aBaseCasingIndex) {
		if (aTileEntity == null) {
			return false;
		}
		final IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
		if (aMetaTileEntity == null) {
			return false;
		}
		if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Muffler) {
			((GT_MetaTileEntity_Hatch) aMetaTileEntity).mMachineBlock = (byte) aBaseCasingIndex;
			return this.mMufflerHatches.add((GT_MetaTileEntity_Hatch_Muffler) aMetaTileEntity);
		}
		return false;
	}

	public boolean addInputToMachineList(final IGregTechTileEntity aTileEntity, final int aBaseCasingIndex) {
		if (aTileEntity == null) {
			return false;
		}
		final IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
		if (aMetaTileEntity == null) {
			return false;
		}
		if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Input) {
			((GT_MetaTileEntity_Hatch) aMetaTileEntity).mMachineBlock = (byte) aBaseCasingIndex;
			((GT_MetaTileEntity_Hatch_Input) aMetaTileEntity).mRecipeMap = this.getRecipeMap();
			return this.mInputHatches.add((GT_MetaTileEntity_Hatch_Input) aMetaTileEntity);
		}
		if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_InputBus) {
			((GT_MetaTileEntity_Hatch) aMetaTileEntity).mMachineBlock = (byte) aBaseCasingIndex;
			((GT_MetaTileEntity_Hatch_InputBus) aMetaTileEntity).mRecipeMap = this.getRecipeMap();
			return this.mInputBusses.add((GT_MetaTileEntity_Hatch_InputBus) aMetaTileEntity);
		}
		return false;
	}

	public boolean addOutputToMachineList(final IGregTechTileEntity aTileEntity, final int aBaseCasingIndex) {
		if (aTileEntity == null) {
			return false;
		}
		final IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
		if (aMetaTileEntity == null) {
			return false;
		}
		if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Output) {
			((GT_MetaTileEntity_Hatch) aMetaTileEntity).mMachineBlock = (byte) aBaseCasingIndex;
			return this.mOutputHatches.add((GT_MetaTileEntity_Hatch_Output) aMetaTileEntity);
		}
		if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_OutputBus) {
			((GT_MetaTileEntity_Hatch) aMetaTileEntity).mMachineBlock = (byte) aBaseCasingIndex;
			return this.mOutputBusses.add((GT_MetaTileEntity_Hatch_OutputBus) aMetaTileEntity);
		}
		return false;
	}

	@Override
	public String[] getInfoData() {
		return new String[]{"Progress:", (this.mProgresstime / 20) + "secs", (this.mMaxProgresstime / 20) + "secs", "Efficiency:", (this.mEfficiency / 100.0F) + "%", "Problems:", "" + (this.getIdealStatus() - this.getRepairStatus())};
	}

	@Override
	public boolean isGivingInformation() {
		return true;
	}

	@Override
	public boolean allowPullStack(final IGregTechTileEntity aBaseMetaTileEntity, final int aIndex, final byte aSide, final ItemStack aStack) {
		return false;
	}

	@Override
	public boolean allowPutStack(final IGregTechTileEntity aBaseMetaTileEntity, final int aIndex, final byte aSide, final ItemStack aStack) {
		return false;
	}
}
