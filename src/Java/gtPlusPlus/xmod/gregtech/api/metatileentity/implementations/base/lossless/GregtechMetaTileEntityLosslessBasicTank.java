package gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.lossless;

import gregtech.api.enums.ItemList;
import gregtech.api.gui.GT_Container_BasicTank;
import gregtech.api.gui.GT_GUIContainer_BasicTank;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GT_Utility;
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
public abstract class GregtechMetaTileEntityLosslessBasicTank extends GregtechMetaTileEntityLosslessTieredMachineBlock {

	public FluidStack mFluid;

	/**
	 * @param aInvSlotCount
	 *            should be 3
	 */
	public GregtechMetaTileEntityLosslessBasicTank(final int aID, final String aName, final String aNameRegional,
			final int aTier, final int aInvSlotCount, final String aDescription, final ITexture... aTextures) {
		super(aID, aName, aNameRegional, aTier, aInvSlotCount, aDescription, aTextures);
	}

	public GregtechMetaTileEntityLosslessBasicTank(final String aName, final int aTier, final int aInvSlotCount,
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

	public abstract boolean canTankBeEmptied();

	public abstract boolean canTankBeFilled();

	public abstract boolean displaysItemStack();

	public abstract boolean displaysStackSize();

	public abstract boolean doesEmptyContainers();

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
		return new GT_GUIContainer_BasicTank(aPlayerInventory, aBaseMetaTileEntity, this.getLocalName());
	}

	public FluidStack getDisplayedFluid() {
		return this.getDrainableStack();
	}

	public FluidStack getDrainableStack() {
		return this.mFluid;
	}

	public FluidStack getFillableStack() {
		return this.mFluid;
	}

	@Override
	public FluidStack getFluid() {
		return this.getDrainableStack();
	}

	@Override
	public int getFluidAmount() {
		return this.getDrainableStack() != null ? this.getDrainableStack().amount : 0;
	}

	public int getInputSlot() {
		return 0;
	}

	public int getOutputSlot() {
		return 1;
	}

	@Override
	public Object getServerGUI(final int aID, final InventoryPlayer aPlayerInventory,
			final IGregTechTileEntity aBaseMetaTileEntity) {
		return new GT_Container_BasicTank(aPlayerInventory, aBaseMetaTileEntity);
	}

	public int getStackDisplaySlot() {
		return 2;
	}

	public boolean isFluidChangingAllowed() {
		return true;
	}

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

			if (this.doesEmptyContainers()) {
				final FluidStack tFluid = GT_Utility.getFluidForFilledItem(this.mInventory[this.getInputSlot()], true);
				if (tFluid != null && this.isFluidInputAllowed(tFluid)) {
					if (this.getFillableStack() == null) {
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
	}

	public FluidStack setDrainableStack(final FluidStack aFluid) {
		this.mFluid = aFluid;
		return this.mFluid;
	}

	public FluidStack setFillableStack(final FluidStack aFluid) {
		this.mFluid = aFluid;
		return this.mFluid;
	}
}