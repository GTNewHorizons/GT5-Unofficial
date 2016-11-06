package gtPlusPlus.xmod.gregtech.api.metatileentity.implementations;

import gregtech.api.enums.ItemList;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicTank;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.xmod.gregtech.api.gui.CONTAINER_DeluxeTank;
import gtPlusPlus.xmod.gregtech.api.gui.GUI_DeluxeTank;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;

/**
 * NEVER INCLUDE THIS FILE IN YOUR MOD!!!
 * <p/>
 * This is the main construct for my generic Tanks. Filling and emptying behavior have to be implemented manually
 */
public abstract class GT_MetaTileEntity_DeluxeTank extends GT_MetaTileEntity_BasicTank {

	public FluidStack mFluid;
	public FluidStack mFluid2;

	/**
	 * @param aInvSlotCount should be 3
	 */
	public GT_MetaTileEntity_DeluxeTank(int aID, String aName, String aNameRegional, int aTier, int aInvSlotCount, String aDescription, ITexture... aTextures) {
		super(aID, aName, aNameRegional, aTier, aInvSlotCount, aDescription, aTextures);
	}

	public GT_MetaTileEntity_DeluxeTank(String aName, int aTier, int aInvSlotCount, String aDescription, ITexture[][][] aTextures) {
		super(aName, aTier, aInvSlotCount, aDescription, aTextures);
	}

	@Override
	public boolean isSimpleMachine() {
		return false;
	}

	@Override
	public boolean isValidSlot(int aIndex) {
		return aIndex != getStackDisplaySlot();
	}

	@Override
	public void saveNBTData(NBTTagCompound aNBT) {
		if (mFluid != null) aNBT.setTag("mFluid", mFluid.writeToNBT(new NBTTagCompound()));
		if (mFluid2 != null) aNBT.setTag("mFluid2", mFluid2.writeToNBT(new NBTTagCompound()));
	}

	@Override
	public void loadNBTData(NBTTagCompound aNBT) {
		mFluid = FluidStack.loadFluidStackFromNBT(aNBT.getCompoundTag("mFluid"));
		mFluid2 = FluidStack.loadFluidStackFromNBT(aNBT.getCompoundTag("mFluid2"));
	}

	@Override
	public abstract boolean doesFillContainers();

	@Override
	public abstract boolean doesEmptyContainers();

	@Override
	public abstract boolean canTankBeFilled();

	@Override
	public abstract boolean canTankBeEmptied();

	@Override
	public abstract boolean displaysItemStack();

	@Override
	public abstract boolean displaysStackSize();

	@Override
	public int getInputSlot() {
		return 0;
	}

	@Override
	public int getOutputSlot() {
		return 1;
	}

	@Override
	public int getStackDisplaySlot() {
		return 2;
	}

	public int getStackDisplaySlot2() {
		return 3;
	}

	@Override
	public boolean isFluidInputAllowed(FluidStack aFluid) {
		return true;
	}

	@Override
	public boolean isFluidChangingAllowed() {
		return true;
	}

	@Override
	public FluidStack getFillableStack() {
		return getFillableStackEx(1);
	}

	public FluidStack getFillableStackEx(int stackID) {
		if (stackID <= 1){
			return mFluid;
		}
		return mFluid2;
	}

	@Override
	public FluidStack setFillableStack(FluidStack aFluid) {
		mFluid = aFluid;
		return mFluid;
	}
	
	public FluidStack setFillableStack2(FluidStack aFluid) {
		mFluid2 = aFluid;
		return mFluid2;
	}

	@Override
	public FluidStack getDrainableStack() {
		return getDrainableStackEx(1);
	}

	public FluidStack getDrainableStackEx(int stackID) {
		if (stackID <= 1){
			return mFluid;
		}
		return mFluid2;
	}

	@Override
	public FluidStack setDrainableStack(FluidStack aFluid) {
		mFluid = aFluid;
		return mFluid;
	}

	@Override
	public FluidStack getDisplayedFluid() {
		return getDrainableStack();
	}

	@Override
	public Object getServerGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
		return new CONTAINER_DeluxeTank(aPlayerInventory, aBaseMetaTileEntity);
	}

	@Override
	public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
		return new GUI_DeluxeTank(aPlayerInventory, aBaseMetaTileEntity, getLocalName());
	}

	@Override
	public void onPreTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
		if (aBaseMetaTileEntity.isServerSide()) {
			if (isFluidChangingAllowed() && getFillableStack() != null && getFillableStack().amount <= 0)
				setFillableStack(null);

			if (displaysItemStack() && getStackDisplaySlot() >= 0 && getStackDisplaySlot() < mInventory.length) {
				if (getDisplayedFluid() == null) {
					if (ItemList.Display_Fluid.isStackEqual(mInventory[getStackDisplaySlot()], true, true))
						mInventory[getStackDisplaySlot()] = null;
				} else {
					mInventory[getStackDisplaySlot()] = GT_Utility.getFluidDisplayStack(getDisplayedFluid(), displaysStackSize());
				}
			}

			if (displaysItemStack() && getStackDisplaySlot2() >= 0 && getStackDisplaySlot2() < mInventory.length) {
				if (getDrainableStackEx(2) == null) {
					if (ItemList.Display_Fluid.isStackEqual(mInventory[getStackDisplaySlot2()], true, true))
						mInventory[getStackDisplaySlot2()] = null;
				} else {
					mInventory[getStackDisplaySlot2()] = GT_Utility.getFluidDisplayStack(getDrainableStackEx(2), displaysStackSize());
				}
			}

			if (doesEmptyContainers()) {
				FluidStack tFluid = GT_Utility.getFluidForFilledItem(mInventory[getInputSlot()], true);
				if (tFluid != null && isFluidInputAllowed(tFluid)) {

					if (tFluid.isFluidEqual(getDrainableStackEx(1)) || getDrainableStackEx(1) == null){
						if (getFillableStackEx(1) == null) {
							if (isFluidInputAllowed(tFluid) && tFluid.amount <= getCapacity()) {
								if (aBaseMetaTileEntity.addStackToSlot(getOutputSlot(), GT_Utility.getContainerItem(mInventory[getInputSlot()], true), 1)) {
									setFillableStack(tFluid.copy());
									aBaseMetaTileEntity.decrStackSize(getInputSlot(), 1);
								}
							}
						} else {
							if (tFluid.isFluidEqual(getFillableStack()) && tFluid.amount + getFillableStack().amount <= getCapacity()) {
								if (aBaseMetaTileEntity.addStackToSlot(getOutputSlot(), GT_Utility.getContainerItem(mInventory[getInputSlot()], true), 1)) {
									getFillableStack().amount += tFluid.amount;
									aBaseMetaTileEntity.decrStackSize(getInputSlot(), 1);
								}
							}
						}
					}
					else if (tFluid.isFluidEqual(getDrainableStackEx(2)) || (getDrainableStackEx(2) == null)){
						if (getFillableStackEx(2) == null) {
							if (isFluidInputAllowed(tFluid) && tFluid.amount <= getCapacity()) {
								if (aBaseMetaTileEntity.addStackToSlot(getOutputSlot(), GT_Utility.getContainerItem(mInventory[getInputSlot()], true), 1)) {
									setFillableStack2(tFluid.copy());
									aBaseMetaTileEntity.decrStackSize(getInputSlot(), 1);
								}
							}
						} else {
							if (tFluid.isFluidEqual(getFillableStackEx(2)) && tFluid.amount + getFillableStackEx(2).amount <= getCapacity()) {
								if (aBaseMetaTileEntity.addStackToSlot(getOutputSlot(), GT_Utility.getContainerItem(mInventory[getInputSlot()], true), 1)) {
									getFillableStackEx(2).amount += tFluid.amount;
									aBaseMetaTileEntity.decrStackSize(getInputSlot(), 1);
								}
							}
						}
					}
					else {
						Utils.LOG_INFO("Something broke when trying to empty cells between two fluid tank areas.");
					}
				}
			}

			if (doesFillContainers()) {
				ItemStack tOutput = GT_Utility.fillFluidContainer(getDrainableStack(), mInventory[getInputSlot()], false, true);
				if (tOutput != null && aBaseMetaTileEntity.addStackToSlot(getOutputSlot(), tOutput, 1)) {
					FluidStack tFluid = GT_Utility.getFluidForFilledItem(tOutput, true);
					aBaseMetaTileEntity.decrStackSize(getInputSlot(), 1);
					if (tFluid != null) getDrainableStack().amount -= tFluid.amount;
					if (getDrainableStack().amount <= 0 && isFluidChangingAllowed()) setDrainableStack(null);
				}
			}
		}
	}

	@Override
	public FluidStack getFluid() {
		return getDrainableStack();
	}

	@Override
	public int getFluidAmount() {
		return getDrainableStack() != null ? getDrainableStack().amount : 0;
	}

	@Override
	public int fill(FluidStack aFluid, boolean doFill) {
		if (aFluid == null || aFluid.getFluid().getID() <= 0 || aFluid.amount <= 0 || !canTankBeFilled() || !isFluidInputAllowed(aFluid))
			return 0;

		if (getFillableStack() == null || getFillableStack().getFluid().getID() <= 0) {
			if (aFluid.amount <= getCapacity()) {
				if (doFill) {
					setFillableStack(aFluid.copy());
					getBaseMetaTileEntity().markDirty();
				}
				return aFluid.amount;
			}
			if (doFill) {
				setFillableStack(aFluid.copy());
				getFillableStack().amount = getCapacity();
				getBaseMetaTileEntity().markDirty();
			}
			return getCapacity();
		}

		if (!getFillableStack().isFluidEqual(aFluid))
			return 0;

		int space = getCapacity() - getFillableStack().amount;
		if (aFluid.amount <= space) {
			if (doFill) {
				getFillableStack().amount += aFluid.amount;
				getBaseMetaTileEntity().markDirty();
			}
			return aFluid.amount;
		}
		if (doFill)
			getFillableStack().amount = getCapacity();
		return space;
	}

	@Override
	public FluidStack drain(int maxDrain, boolean doDrain) {
		if (getDrainableStack() == null || !canTankBeEmptied()) return null;
		if (getDrainableStack().amount <= 0 && isFluidChangingAllowed()) {
			setDrainableStack(null);
			getBaseMetaTileEntity().markDirty();
			return null;
		}

		int used = maxDrain;
		if (getDrainableStack().amount < used)
			used = getDrainableStack().amount;

		if (doDrain) {
			getDrainableStack().amount -= used;
			getBaseMetaTileEntity().markDirty();
		}

		FluidStack drained = getDrainableStack().copy();
		drained.amount = used;

		if (getDrainableStack().amount <= 0 && isFluidChangingAllowed()) {
			setDrainableStack(null);
			getBaseMetaTileEntity().markDirty();
		}

		return drained;
	}

	@Override
	public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
		return aIndex == getOutputSlot();
	}

	@Override
	public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
		return aIndex == getInputSlot();
	}
}