package gtPlusPlus.xmod.gregtech.common.tileentities.machines.basic;

import static gregtech.api.enums.GT_Values.E;

import java.util.HashSet;
import java.util.List;

import cpw.mods.fml.common.registry.LanguageRegistry;
import gregtech.GT_Mod;
import gregtech.api.GregTech_API;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicMachine;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Recipe.GT_Recipe_Map;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.Recipe_GT;
import gtPlusPlus.api.objects.random.XSTR;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.slots.SlotChemicalPlantInput;
import gtPlusPlus.xmod.gregtech.api.gui.fluidreactor.Container_FluidReactor;
import gtPlusPlus.xmod.gregtech.api.gui.fluidreactor.GUI_FluidReactor;
import gtPlusPlus.xmod.gregtech.common.Meta_GT_Proxy;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

public class GregtechMetaTileEntity_ChemicalReactor extends GT_MetaTileEntity_BasicMachine {

	public boolean mFluidTransfer_1 = false;
	public boolean mFluidTransfer_2 = false;

	public FluidStack[] mInputFluids = new FluidStack[4];
	public FluidStack[] mOutputFluids = new FluidStack[2];

	private static final GT_Recipe_Map mFluidChemicalReactorRecipes = new GT_Recipe_Map(
			new HashSet<GT_Recipe>(100),
			"gt.recipe.fluidchemicaleactor",
			"Chemical Plant",
			null,
			CORE.MODID+":textures/gui/FluidReactor",
			0,
			0,
			0,
			2,
			1,
			"Tier: ",
			1,
			E,
			true,
			false);

	public GregtechMetaTileEntity_ChemicalReactor(int aID, String aName, String aNameRegional, int aTier) {
		super(aID, aName, aNameRegional, aTier, 1, 
				"For mixing fluids", 4, 4, "ChemicalReactor.png", "",
				new ITexture[]{
						new GT_RenderedTexture(TexturesGtBlock.Overlay_FluidReactor_Side_Active),
						new GT_RenderedTexture(TexturesGtBlock.Overlay_FluidReactor_Side),
						new GT_RenderedTexture(TexturesGtBlock.Overlay_FluidReactor_Front_Active),
						new GT_RenderedTexture(TexturesGtBlock.Overlay_FluidReactor_Front),
						new GT_RenderedTexture(TexturesGtBlock.Overlay_FluidReactor_Top_Active),
						new GT_RenderedTexture(TexturesGtBlock.Overlay_FluidReactor_Top),
						new GT_RenderedTexture(TexturesGtBlock.Overlay_FluidReactor_Top_Active),
						new GT_RenderedTexture(TexturesGtBlock.Overlay_FluidReactor_Top)
		}
				);
	}

	public GregtechMetaTileEntity_ChemicalReactor(String aName, int aTier, String aDescription, ITexture[][][] aTextures, String aGUIName, String aNEIName) {
		super(aName, aTier, 1, aDescription, aTextures, 1, 1, aGUIName, aNEIName);
	}

	/*public GregtechMetaTileEntity_BasicWasher(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures, String aGUIName, String aNEIName) {
		super(aName, aTier, 1, aDescription, aTextures, 1, 1, aGUIName, aNEIName);
	}*/

	@Override
	public String[] getDescription() {
		return new String[]{this.mDescription, "Because why not?", };
	}

	@Override
	public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
		try {
			GregtechMetaTileEntity_ChemicalReactor y = new GregtechMetaTileEntity_ChemicalReactor(this.mName, this.mTier, this.mDescription, this.mTextures, this.mGUIName, this.mNEIName);
			return y;
		}
		catch(Throwable t) {
			t.printStackTrace();
		}
		return null;
	}

	@Override
	public Object getServerGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
		try {
			Container_FluidReactor y = new Container_FluidReactor(aPlayerInventory, aBaseMetaTileEntity);
			return y;
		}
		catch(Throwable t) {
			t.printStackTrace();
		}
		return null;
	}

	@Override
	public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
		return new GUI_FluidReactor(aPlayerInventory, aBaseMetaTileEntity, this.getLocalName(),
				this.mGUIName,
				GT_Utility.isStringValid(this.mNEIName)
				? this.mNEIName
						: (this.getRecipeList() != null ? this.getRecipeList().mUnlocalizedName : ""));
	}

	@Override
	public GT_Recipe.GT_Recipe_Map getRecipeList() {
		if (mFluidChemicalReactorRecipes.mRecipeList.isEmpty()) {
			for (Recipe_GT i :Recipe_GT.Gregtech_Recipe_Map.sFluidChemicalReactorRecipes.mRecipeList) {
				mFluidChemicalReactorRecipes.add(i);
			}
		}
		return mFluidChemicalReactorRecipes;
	}

	@Override
	public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
		return (super.allowPutStack(aBaseMetaTileEntity, aIndex, aSide, aStack)) && (getRecipeList().containsInput(aStack));
	}

	@Override
	public boolean isFluidInputAllowed(FluidStack aFluid) {
		return (super.isFluidInputAllowed(aFluid));
	}

	@Override
	public int getCapacity() {
		return 32000 * Math.max(1, this.mTier) / 4;
	}

	@Override
	public int getInputSlot() {
		return 3;
	}

	@Override
	public boolean isLiquidInput(byte aSide) {
		return aSide > 1;
	}

	@Override
	public boolean isLiquidOutput(byte aSide) {
		return aSide < 2;
	}

	@Override
	public int getOutputSlot() {
		return super.getOutputSlot();
	}

	@Override
	public int getStackDisplaySlot() {
		return super.getStackDisplaySlot();
	}

	@Override
	public boolean doesEmptyContainers() {
		return true;
	}

	@Override
	public boolean canTankBeFilled() {
		return super.canTankBeFilled();
	}

	@Override
	public boolean canTankBeEmptied() {
		return super.canTankBeEmptied();
	}

	@Override
	public FluidStack getDisplayedFluid() {
		return super.getDisplayedFluid();
	}

	@Override
	public void saveNBTData(NBTTagCompound aNBT) {
		super.saveNBTData(aNBT);		
		aNBT.setBoolean("mFluidTransfer_1", mFluidTransfer_1);
		aNBT.setBoolean("mFluidTransfer_2", mFluidTransfer_2);
		for (int i=0;i<4;i++) {
			if (this.mInputFluids[i] != null) {
				aNBT.setTag("mInputFluid"+i, this.mInputFluids[i].writeToNBT(new NBTTagCompound()));
			}
		}	
		for (int i=0;i<2;i++) {
			if (this.mOutputFluids[i] != null) {
				aNBT.setTag("mOutputFluid"+i, this.mOutputFluids[i].writeToNBT(new NBTTagCompound()));
			}
		}
	}

	@Override
	public void loadNBTData(NBTTagCompound aNBT) {
		super.loadNBTData(aNBT);
		mFluidTransfer_1 = aNBT.getBoolean("mFluidTransfer_1");
		mFluidTransfer_2 = aNBT.getBoolean("mFluidTransfer_2");
		for (int i=0;i<4;i++) {
			if (this.mInputFluids[i] == null) {
				this.mInputFluids[i] = FluidStack.loadFluidStackFromNBT(aNBT.getCompoundTag("mInputFluid"+i));
			}
		}	
		for (int i=0;i<2;i++) {
			if (this.mOutputFluids[i] == null) {
				this.mOutputFluids[i] = FluidStack.loadFluidStackFromNBT(aNBT.getCompoundTag("mOutputFluid"+i));
			}
		}
	}

	/*
	 * Custom Fluid Handling - TODO
	 */

	public FluidStack getFillableStack() {
		return this.mFluid;
	}

	public FluidStack setFillableStack(FluidStack aFluid) {
		this.mFluid = aFluid;
		return this.mFluid;
	}

	public FluidStack getDrainableStack() {
		return this.mFluid;
	}

	public FluidStack setDrainableStack(FluidStack aFluid) {
		this.mFluid = aFluid;
		return this.mFluid;
	}








	@Override
	public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
		// Re-implement basic machine logic from the ground up.=
		
		if (aBaseMetaTileEntity.isServerSide()) {
			this.mCharge = aBaseMetaTileEntity.getStoredEU() / 2L > aBaseMetaTileEntity.getEUCapacity() / 3L;
			this.mDecharge = aBaseMetaTileEntity.getStoredEU() < aBaseMetaTileEntity.getEUCapacity() / 3L;
			this.doDisplayThings();
			boolean tSucceeded = false;
			int i;
			if (this.mMaxProgresstime > 0 && (this.mProgresstime >= 0 || aBaseMetaTileEntity.isAllowedToWork())) {
				aBaseMetaTileEntity.setActive(true);
				if (this.mProgresstime >= 0 && !this.drainEnergyForProcess((long) this.mEUt)) {
					if (!this.mStuttering) {
						this.stutterProcess();
						if (this.canHaveInsufficientEnergy()) {
							this.mProgresstime = -100;
						}

						this.mStuttering = true;
					}
				} else {
					if (++this.mProgresstime >= this.mMaxProgresstime) {
						for (i = 0; i < this.mOutputItems.length; ++i) {
							for (i = 0; i < this.mOutputItems.length && !aBaseMetaTileEntity.addStackToSlot(
									this.getOutputSlot() + (i + i) % this.mOutputItems.length,
									this.mOutputItems[i]); ++i) {
								;
							}
						}

						if (this.mOutputFluid != null) {
							if (this.getDrainableStack() == null) {
								this.setDrainableStack(this.mOutputFluid.copy());
							} else if (this.mOutputFluid.isFluidEqual(this.getDrainableStack())) {
								FluidStack var10000 = this.getDrainableStack();
								var10000.amount += this.mOutputFluid.amount;
							}
						}

						for (i = 0; i < this.mOutputItems.length; ++i) {
							this.mOutputItems[i] = null;
						}

						this.mOutputFluid = null;
						this.mEUt = 0;
						this.mProgresstime = 0;
						this.mMaxProgresstime = 0;
						this.mStuttering = false;
						tSucceeded = true;
						this.endProcess();
					}

					if (this.mProgresstime > 5) {
						this.mStuttering = false;
					}
				}
			} else {
				aBaseMetaTileEntity.setActive(false);
			}

			boolean tRemovedOutputFluid = false;
			if (this.doesAutoOutputFluids() && this.getDrainableStack() != null
					&& aBaseMetaTileEntity.getFrontFacing() != this.mMainFacing && (tSucceeded || aTick % 20L == 0L)) {
				IFluidHandler tTank = aBaseMetaTileEntity.getITankContainerAtSide(aBaseMetaTileEntity.getFrontFacing());
				if (tTank != null) {
					FluidStack tDrained = this.drain(1000, false);
					if (tDrained != null) {
						int tFilledAmount = tTank.fill(
								ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()), tDrained, false);
						if (tFilledAmount > 0) {
							tTank.fill(ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()),
									this.drain(tFilledAmount, true), true);
						}
					}
				}

				if (this.getDrainableStack() == null) {
					tRemovedOutputFluid = true;
				}
			}

			int j;
			if (this.doesAutoOutput() && !this.isOutputEmpty()
					&& aBaseMetaTileEntity.getFrontFacing() != this.mMainFacing
					&& (tSucceeded || this.mOutputBlocked % 300 == 1 || aBaseMetaTileEntity.hasInventoryBeenModified()
							|| aTick % 600L == 0L)) {
				TileEntity tTileEntity2 = aBaseMetaTileEntity.getTileEntityAtSide(aBaseMetaTileEntity.getFrontFacing());
				j = 0;

				for (byte tCosts = 1; j < this.mOutputItems.length && tCosts > 0
						&& aBaseMetaTileEntity.isUniversalEnergyStored(128L); ++j) {
					tCosts = GT_Utility.moveOneItemStack(aBaseMetaTileEntity, tTileEntity2,
							aBaseMetaTileEntity.getFrontFacing(), aBaseMetaTileEntity.getBackFacing(), (List) null,
							false, (byte) 64, (byte) 1, (byte) 64, (byte) 1);
					if (tCosts > 0) {
						aBaseMetaTileEntity.decreaseStoredEnergyUnits((long) tCosts, true);
					}
				}
			}

			if (this.mOutputBlocked != 0) {
				if (this.isOutputEmpty()) {
					this.mOutputBlocked = 0;
				} else {
					++this.mOutputBlocked;
				}
			}

			if (this.allowToCheckRecipe()) {
				if (this.mMaxProgresstime <= 0 && aBaseMetaTileEntity.isAllowedToWork()
						&& (tRemovedOutputFluid || tSucceeded || aBaseMetaTileEntity.hasInventoryBeenModified()
								|| aTick % 600L == 0L || aBaseMetaTileEntity.hasWorkJustBeenEnabled())
						&& this.hasEnoughEnergyToCheckRecipe()) {
					if (this.checkRecipe() == 2) {
						if (this.mInventory[3] != null && this.mInventory[3].stackSize <= 0) {
							this.mInventory[3] = null;
						}

						i = this.getInputSlot();

						for (j = i + this.mInputSlotCount; i < j; ++i) {
							if (this.mInventory[i] != null && this.mInventory[i].stackSize <= 0) {
								this.mInventory[i] = null;
							}
						}

						for (i = 0; i < this.mOutputItems.length; ++i) {
							this.mOutputItems[i] = GT_Utility.copy(new Object[]{this.mOutputItems[i]});
							if (this.mOutputItems[i] != null && this.mOutputItems[i].stackSize > 64) {
								this.mOutputItems[i].stackSize = 64;
							}

							this.mOutputItems[i] = GT_OreDictUnificator.get(true, this.mOutputItems[i]);
						}

						if (this.mFluid != null && this.mFluid.amount <= 0) {
							this.mFluid = null;
						}

						this.mMaxProgresstime = Math.max(1, this.mMaxProgresstime);
						if (GT_Utility.isDebugItem(this.mInventory[this.dechargerSlotStartIndex()])) {
							this.mEUt = this.mMaxProgresstime = 1;
						}

						this.startProcess();
					} else {
						this.mMaxProgresstime = 0;

						for (i = 0; i < this.mOutputItems.length; ++i) {
							this.mOutputItems[i] = null;
						}

						this.mOutputFluid = null;
					}
				}
			} else if (!this.mStuttering) {
				this.stutterProcess();
				this.mStuttering = true;
			}
		}

	
		
	}

	@Override
	protected void doDisplayThings() {
		// TODO Auto-generated method stub
		super.doDisplayThings();
	}

	@Override
	protected ItemStack getSpecialSlot() {
		// TODO Auto-generated method stub
		return super.getSpecialSlot();
	}

	@Override
	protected ItemStack getOutputAt(int aIndex) {
		// TODO Auto-generated method stub
		return super.getOutputAt(aIndex);
	}

	@Override
	protected ItemStack[] getAllOutputs() {
		// TODO Auto-generated method stub
		return super.getAllOutputs();
	}

	@Override
	protected ItemStack getInputAt(int aIndex) {
		// TODO Auto-generated method stub
		return super.getInputAt(aIndex);
	}

	@Override
	protected ItemStack[] getAllInputs() {
		// TODO Auto-generated method stub
		return super.getAllInputs();
	}

	@Override
	protected boolean displaysInputFluid() {
		return true;
	}

	@Override
	protected boolean displaysOutputFluid() {
		return true;
	}

	@Override
	public boolean doesAutoOutput() {
		return super.doesAutoOutput();
	}

	@Override
	public boolean doesAutoOutputFluids() {
		return this.mFluidTransfer_1 && this.mFluidTransfer_2;
	}

	@Override
	public void startProcess() {
		super.startProcess();
	}

	@Override
	public void endProcess() {
		super.endProcess();
	}

	@Override
	public String[] getInfoData() {
		return super.getInfoData();
	}

	@Override
	public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
		return super.allowPullStack(aBaseMetaTileEntity, aIndex, aSide, aStack);
	}

	@Override
	public int checkRecipe() {
		return super.checkRecipe();
	}

	@Override
	public void onPreTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
		super.onPreTick(aBaseMetaTileEntity, aTick);

		/*		if (aBaseMetaTileEntity != null) {
					if (!aBaseMetaTileEntity.getWorld().isRemote) {
						itemslots : for (int i=3;i<7;i++) {
							ItemStack aStack = aBaseMetaTileEntity.getStackInSlot(i);
							if (aStack != null) {
								if (FluidContainerRegistry.isContainer(aStack)) {
									if (this.isItemValidForSlot(i, aStack)) {
										// Add Fluid
										FluidStack aContainerFluid = FluidContainerRegistry.getFluidForFilledItem(aStack);
										if (aContainerFluid != null) {
											fluidslots : for (FluidStack u : mInputFluids) {
												if (u != null && u.isFluidEqual(aContainerFluid)) {
													if (u.amount <= (this.getCapacity() - aContainerFluid.amount)) {

														// Matching, not full, let's fill, then continue
														u.amount += aContainerFluid.amount;		

														// Update stack size
														if (aStack.stackSize > 1) {
															aStack.stackSize--;
														}
														else {
															aStack = null;
														}

														// Add Output container
														// TODO

														continue itemslots;
													}
													else {
														// Too full
														break fluidslots;
													}
												}
												else {
													if (u == null ) {

														// Empty, let's fill, then continue
														u = aContainerFluid.copy();

														// Update stack size
														if (aStack.stackSize > 1) {
															aStack.stackSize--;
														}
														else {
															aStack = null;
														}

														// Add Output container
														// TODO

														continue itemslots;

													}
													else {
														// Not empty, doesn't match, check next slot.
														continue fluidslots;
													}
												}
											}							



										}
										// Eat Input

										// Add Container to Output
									}
								}
							}
						}
					}
				}*/		

	}

	@Override
	public FluidStack getFluid() {
		return super.getFluid();
	}

	@Override
	public int getFluidAmount() {
		return super.getFluidAmount();
	}

	@Override
	public int fill(FluidStack aFluid, boolean doFill) {
		return super.fill(aFluid, doFill);
	}

	@Override
	public FluidStack drain(int maxDrain, boolean doDrain) {
		return super.drain(maxDrain, doDrain);
	}

	@Override
	public void setItemNBT(NBTTagCompound aNBT) {
		super.setItemNBT(aNBT);
	}

	@Override
	public boolean isItemValidForSlot(int aIndex, ItemStack aStack) {
		if (aIndex >= 3 && aIndex <= 6) {
			return SlotChemicalPlantInput.isItemValidForChemicalPlantSlot(aStack);			
		}
		return false;
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int aSide) {
		return super.getAccessibleSlotsFromSide(aSide);
	}

	@Override
	public boolean canInsertItem(int aIndex, ItemStack aStack, int aSide) {
		if (aIndex >= 3 && aIndex <= 6) {
			return super.canInsertItem(aIndex, aStack, aSide);	
		}
		return false;
	}

	@Override
	public boolean canExtractItem(int aIndex, ItemStack aStack, int aSide) {
		if (aIndex >= 7 && aIndex <= 11) {
			return super.canExtractItem(aIndex, aStack, aSide);
		}
		return false;
	}

	@Override
	public boolean canFill(ForgeDirection aSide, Fluid aFluid) {
		// TODO Auto-generated method stub
		return super.canFill(aSide, aFluid);
	}

	@Override
	public boolean canDrain(ForgeDirection aSide, Fluid aFluid) {
		// TODO Auto-generated method stub
		return super.canDrain(aSide, aFluid);
	}

	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection aSide) {
		// TODO Auto-generated method stub
		return super.getTankInfo(aSide);
	}

	@Override
	public int fill_default(
			ForgeDirection aSide, FluidStack aFluid, boolean doFill
			) {
		// TODO Auto-generated method stub
		return super.fill_default(aSide, aFluid, doFill);
	}

	@Override
	public int fill(ForgeDirection aSide, FluidStack aFluid, boolean doFill) {
		// TODO Auto-generated method stub
		return super.fill(aSide, aFluid, doFill);
	}

	@Override
	public FluidStack drain(
			ForgeDirection aSide, FluidStack aFluid, boolean doDrain
			) {
		// TODO Auto-generated method stub
		return super.drain(aSide, aFluid, doDrain);
	}

	@Override
	public FluidStack drain(
			ForgeDirection aSide, int maxDrain, boolean doDrain
			) {
		// TODO Auto-generated method stub
		return super.drain(aSide, maxDrain, doDrain);
	}

}
