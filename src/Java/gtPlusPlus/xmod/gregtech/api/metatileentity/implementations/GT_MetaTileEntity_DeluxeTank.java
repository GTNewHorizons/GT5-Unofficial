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
 * This is the main construct for my generic Tanks. Filling and emptying
 * behavior have to be implemented manually
 */
public abstract class GT_MetaTileEntity_DeluxeTank extends GT_MetaTileEntity_BasicTank {

	public FluidStack	mFluid;
	public FluidStack	mFluid2;

	/**
	 * @param aInvSlotCount
	 *            should be 3
	 */
	public GT_MetaTileEntity_DeluxeTank(final int aID, final String aName, final String aNameRegional, final int aTier,
			final int aInvSlotCount, final String aDescription, final ITexture... aTextures) {
		super(aID, aName, aNameRegional, aTier, aInvSlotCount, aDescription, aTextures);
	}

	public GT_MetaTileEntity_DeluxeTank(final String aName, final int aTier, final int aInvSlotCount,
			final String aDescription, final ITexture[][][] aTextures) {
		super(aName, aTier, aInvSlotCount, aDescription, aTextures);
	}

	@Override
	public boolean allowPullStack(final IGregTechTileEntity aBaseMetaTileEntity, final int aIndex, final byte aSide,
			final ItemStack aStack) {
		return aIndex == this.getOutputSlot();
	}

	@Override
	public boolean allowPutStack(final IGregTechTileEntity aBaseMetaTileEntity, final int aIndex, final byte aSide,
			final ItemStack aStack) {
		return aIndex == this.getInputSlot();
	}

	@Override
	public abstract boolean canTankBeEmptied();

	@Override
	public abstract boolean canTankBeFilled();

	@Override
	public abstract boolean displaysItemStack();

	@Override
	public abstract boolean displaysStackSize();

	@Override
	public abstract boolean doesEmptyContainers();

	@Override
	public abstract boolean doesFillContainers();

	@Override
	public FluidStack drain(final int maxDrain, final boolean doDrain) {
		if (this.getDrainableStack() == null || !this.canTankBeEmptied()) {
			return null;
		}
		if (this.getDrainableStack().amount <= 0 && this.isFluidChangingAllowed()) {
			this.setDrainableStack(null);
			this.getBaseMetaTileEntity().markDirty();
			return null;
		}

		int used = maxDrain;
		if (this.getDrainableStack().amount < used) {
			used = this.getDrainableStack().amount;
		}

		if (doDrain) {
			this.getDrainableStack().amount -= used;
			this.getBaseMetaTileEntity().markDirty();
		}

		final FluidStack drained = this.getDrainableStack().copy();
		drained.amount = used;

		if (this.getDrainableStack().amount <= 0 && this.isFluidChangingAllowed()) {
			this.setDrainableStack(null);
			this.getBaseMetaTileEntity().markDirty();
		}

		return drained;
	}

	@Override
	public int fill(final FluidStack aFluid, final boolean doFill) {
		if (aFluid == null || aFluid.getFluid().getID() <= 0 || aFluid.amount <= 0 || !this.canTankBeFilled()
				|| !this.isFluidInputAllowed(aFluid)) {
			return 0;
		}

		if (this.getFillableStack() == null || this.getFillableStack().getFluid().getID() <= 0) {
			if (aFluid.amount <= this.getCapacity()) {
				if (doFill) {
					this.setFillableStack(aFluid.copy());
					this.getBaseMetaTileEntity().markDirty();
				}
				return aFluid.amount;
			}
			if (doFill) {
				this.setFillableStack(aFluid.copy());
				this.getFillableStack().amount = this.getCapacity();
				this.getBaseMetaTileEntity().markDirty();
			}
			return this.getCapacity();
		}

		if (!this.getFillableStack().isFluidEqual(aFluid)) {
			return 0;
		}

		final int space = this.getCapacity() - this.getFillableStack().amount;
		if (aFluid.amount <= space) {
			if (doFill) {
				this.getFillableStack().amount += aFluid.amount;
				this.getBaseMetaTileEntity().markDirty();
			}
			return aFluid.amount;
		}
		if (doFill) {
			this.getFillableStack().amount = this.getCapacity();
		}
		return space;
	}

	@Override
	public Object getClientGUI(final int aID, final InventoryPlayer aPlayerInventory,
			final IGregTechTileEntity aBaseMetaTileEntity) {
		return new GUI_DeluxeTank(aPlayerInventory, aBaseMetaTileEntity, this.getLocalName());
	}

	@Override
	public FluidStack getDisplayedFluid() {
		return this.getDrainableStack();
	}

	@Override
	public FluidStack getDrainableStack() {
		return this.getDrainableStackEx(1);
	}

	public FluidStack getDrainableStackEx(final int stackID) {
		if (stackID <= 1) {
			return this.mFluid;
		}
		return this.mFluid2;
	}

	@Override
	public FluidStack getFillableStack() {
		return this.getFillableStackEx(1);
	}

	public FluidStack getFillableStackEx(final int stackID) {
		if (stackID <= 1) {
			return this.mFluid;
		}
		return this.mFluid2;
	}

	@Override
	public FluidStack getFluid() {
		return this.getDrainableStack();
	}

	@Override
	public int getFluidAmount() {
		return this.getDrainableStack() != null ? this.getDrainableStack().amount : 0;
	}

	@Override
	public int getInputSlot() {
		return 0;
	}

	@Override
	public int getOutputSlot() {
		return 1;
	}

	@Override
	public Object getServerGUI(final int aID, final InventoryPlayer aPlayerInventory,
			final IGregTechTileEntity aBaseMetaTileEntity) {
		return new CONTAINER_DeluxeTank(aPlayerInventory, aBaseMetaTileEntity);
	}

	@Override
	public int getStackDisplaySlot() {
		return 2;
	}

	public int getStackDisplaySlot2() {
		return 3;
	}

	@Override
	public boolean isFluidChangingAllowed() {
		return true;
	}

	@Override
	public boolean isFluidInputAllowed(final FluidStack aFluid) {
		return true;
	}

	@Override
	public boolean isSimpleMachine() {
		return false;
	}

	@Override
	public boolean isValidSlot(final int aIndex) {
		return aIndex != this.getStackDisplaySlot();
	}

	@Override
	public void loadNBTData(final NBTTagCompound aNBT) {
		this.mFluid = FluidStack.loadFluidStackFromNBT(aNBT.getCompoundTag("mFluid"));
		this.mFluid2 = FluidStack.loadFluidStackFromNBT(aNBT.getCompoundTag("mFluid2"));
	}

	@Override
	public void onPreTick(final IGregTechTileEntity aBaseMetaTileEntity, final long aTick) {
		if (aBaseMetaTileEntity.isServerSide()) {
			if (this.isFluidChangingAllowed() && this.getFillableStack() != null
					&& this.getFillableStack().amount <= 0) {
				this.setFillableStack(null);
			}

			if (this.displaysItemStack() && this.getStackDisplaySlot() >= 0
					&& this.getStackDisplaySlot() < this.mInventory.length) {
				if (this.getDisplayedFluid() == null) {
					if (ItemList.Display_Fluid.isStackEqual(this.mInventory[this.getStackDisplaySlot()], true, true)) {
						this.mInventory[this.getStackDisplaySlot()] = null;
					}
				}
				else {
					this.mInventory[this.getStackDisplaySlot()] = GT_Utility
							.getFluidDisplayStack(this.getDisplayedFluid(), this.displaysStackSize());
				}
			}

			if (this.displaysItemStack() && this.getStackDisplaySlot2() >= 0
					&& this.getStackDisplaySlot2() < this.mInventory.length) {
				if (this.getDrainableStackEx(2) == null) {
					if (ItemList.Display_Fluid.isStackEqual(this.mInventory[this.getStackDisplaySlot2()], true, true)) {
						this.mInventory[this.getStackDisplaySlot2()] = null;
					}
				}
				else {
					this.mInventory[this.getStackDisplaySlot2()] = GT_Utility
							.getFluidDisplayStack(this.getDrainableStackEx(2), this.displaysStackSize());
				}
			}

			if (this.doesEmptyContainers()) {
				final FluidStack tFluid = GT_Utility.getFluidForFilledItem(this.mInventory[this.getInputSlot()], true);
				if (tFluid != null && this.isFluidInputAllowed(tFluid)) {

					if (tFluid.isFluidEqual(this.getDrainableStackEx(1)) || this.getDrainableStackEx(1) == null) {
						if (this.getFillableStackEx(1) == null) {
							if (this.isFluidInputAllowed(tFluid) && tFluid.amount <= this.getCapacity()) {
								if (aBaseMetaTileEntity.addStackToSlot(this.getOutputSlot(),
										GT_Utility.getContainerItem(this.mInventory[this.getInputSlot()], true), 1)) {
									this.setFillableStack(tFluid.copy());
									aBaseMetaTileEntity.decrStackSize(this.getInputSlot(), 1);
								}
							}
						}
						else {
							if (tFluid.isFluidEqual(this.getFillableStack())
									&& tFluid.amount + this.getFillableStack().amount <= this.getCapacity()) {
								if (aBaseMetaTileEntity.addStackToSlot(this.getOutputSlot(),
										GT_Utility.getContainerItem(this.mInventory[this.getInputSlot()], true), 1)) {
									this.getFillableStack().amount += tFluid.amount;
									aBaseMetaTileEntity.decrStackSize(this.getInputSlot(), 1);
								}
							}
						}
					}
					else if (tFluid.isFluidEqual(this.getDrainableStackEx(2)) || this.getDrainableStackEx(2) == null) {
						if (this.getFillableStackEx(2) == null) {
							if (this.isFluidInputAllowed(tFluid) && tFluid.amount <= this.getCapacity()) {
								if (aBaseMetaTileEntity.addStackToSlot(this.getOutputSlot(),
										GT_Utility.getContainerItem(this.mInventory[this.getInputSlot()], true), 1)) {
									this.setFillableStack2(tFluid.copy());
									aBaseMetaTileEntity.decrStackSize(this.getInputSlot(), 1);
								}
							}
						}
						else {
							if (tFluid.isFluidEqual(this.getFillableStackEx(2))
									&& tFluid.amount + this.getFillableStackEx(2).amount <= this.getCapacity()) {
								if (aBaseMetaTileEntity.addStackToSlot(this.getOutputSlot(),
										GT_Utility.getContainerItem(this.mInventory[this.getInputSlot()], true), 1)) {
									this.getFillableStackEx(2).amount += tFluid.amount;
									aBaseMetaTileEntity.decrStackSize(this.getInputSlot(), 1);
								}
							}
						}
					}
					else {
						Utils.LOG_INFO("Something broke when trying to empty cells between two fluid tank areas.");
					}
				}
			}

			if (this.doesFillContainers()) {
				final ItemStack tOutput = GT_Utility.fillFluidContainer(this.getDrainableStack(),
						this.mInventory[this.getInputSlot()], false, true);
				if (tOutput != null && aBaseMetaTileEntity.addStackToSlot(this.getOutputSlot(), tOutput, 1)) {
					final FluidStack tFluid = GT_Utility.getFluidForFilledItem(tOutput, true);
					aBaseMetaTileEntity.decrStackSize(this.getInputSlot(), 1);
					if (tFluid != null) {
						this.getDrainableStack().amount -= tFluid.amount;
					}
					if (this.getDrainableStack().amount <= 0 && this.isFluidChangingAllowed()) {
						this.setDrainableStack(null);
					}
				}
			}
		}
	}

	@Override
	public void saveNBTData(final NBTTagCompound aNBT) {
		if (this.mFluid != null) {
			aNBT.setTag("mFluid", this.mFluid.writeToNBT(new NBTTagCompound()));
		}
		if (this.mFluid2 != null) {
			aNBT.setTag("mFluid2", this.mFluid2.writeToNBT(new NBTTagCompound()));
		}
	}

	@Override
	public FluidStack setDrainableStack(final FluidStack aFluid) {
		this.mFluid = aFluid;
		return this.mFluid;
	}

	@Override
	public FluidStack setFillableStack(final FluidStack aFluid) {
		this.mFluid = aFluid;
		return this.mFluid;
	}

	public FluidStack setFillableStack2(final FluidStack aFluid) {
		this.mFluid2 = aFluid;
		return this.mFluid2;
	}
}