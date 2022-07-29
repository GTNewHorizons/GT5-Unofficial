package gregtech.api.multitileentity.machine;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.GT_Values.NBT;
import gregtech.api.fluid.FluidTankGT;
import gregtech.api.interfaces.ITexture;
import gregtech.api.multitileentity.MultiTileEntityRegistry;
import gregtech.api.multitileentity.base.BaseTickableMultiTileEntity;
import gregtech.api.util.GT_Utility;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;

import static com.google.common.primitives.Ints.saturatedCast;

public class MultiTileBasicMachine extends BaseTickableMultiTileEntity  {
    protected int mParallel = 1;
    protected long mStoredEnergy = 0;
    protected FluidTankGT[] mTanksInput = GT_Values.emptyFluidTankGT, mTanksOutput = GT_Values.emptyFluidTankGT;
    protected ItemStack[] mOutputItems = GT_Values.emptyItemStackArray;
    protected FluidStack[] mOutputFluids = GT_Values.emptyFluidStack;

    protected ItemStack[] mInventory = GT_Values.emptyItemStackArray;

    @Override
    public String getTileEntityName() {
        return "gt.multitileentity.machine.basic";
    }


    @Override
    public void writeMultiTileNBT(NBTTagCompound aNBT) {
        super.writeMultiTileNBT(aNBT);

    }

    @Override
    public void readMultiTileNBT(NBTTagCompound aNBT) {
        super.readMultiTileNBT(aNBT);
        if (aNBT.hasKey(NBT.PARALLEL)) mParallel = Math.max(1, aNBT.getInteger(NBT.PARALLEL));

        mInventory = getDefaultInventory(aNBT);
        if(mInventory != null) {
            final NBTTagList tList = aNBT.getTagList(NBT.INV_LIST, 10);
            for (int i = 0; i < tList.tagCount(); i++) {
                final NBTTagCompound tNBT = tList.getCompoundTagAt(i);
                final int tSlot = tNBT.getShort("s");
                if (tSlot >= 0 && tSlot < mInventory.length) mInventory[tSlot] = GT_Utility.loadItem(tNBT);
            }
        }
        /* Tanks */
        long tCapacity = 1000;
        if (aNBT.hasKey(NBT.TANK_CAPACITY)) tCapacity = saturatedCast(aNBT.getLong(NBT.TANK_CAPACITY));

        mTanksInput = new FluidTankGT[getFluidInputCount()];
        mTanksOutput = new FluidTankGT[getFluidOutputCount()];
        mOutputFluids = new FluidStack[getFluidOutputCount()];
        mOutputItems = new ItemStack[getItemOutputCount()];

        // TODO: See if we need the adjustable map here `.setCapacity(mRecipes, mParallel * 2L)` in place of the `setCapacityMultiplier`
        for (int i = 0; i < mTanksInput.length; i++) mTanksInput[i] = new FluidTankGT(tCapacity).setCapacityMultiplier(mParallel * 2L).readFromNBT(aNBT, NBT.TANK_IN + i);
        for (int i = 0; i < mTanksOutput.length; i++) mTanksOutput[i] = new FluidTankGT().readFromNBT(aNBT, NBT.TANK_OUT + i);
        for (int i = 0; i < mOutputFluids.length; i++) mOutputFluids[i] = FluidStack.loadFluidStackFromNBT(aNBT.getCompoundTag(NBT.FLUID_OUT + "." + i));
        for (int i = 0; i < mOutputItems.length; i++) mOutputItems[i] = ItemStack.loadItemStackFromNBT(aNBT.getCompoundTag(NBT.INV_OUT + "." + i));

    }

    /*
     * Fluids
     */


    /**
     * The number of fluid (input) slots available for this machine
     */
    public int getFluidInputCount() {
        return 2;
    }

    /**
     * The number of fluid (output) slots available for this machine
     */
    public int getFluidOutputCount() {
        return 2;
    }

    public ItemStack[] getDefaultInventory(NBTTagCompound aNBT) {
        final int tSize = Math.max(0, aNBT.getShort(NBT.INV_SIZE));
        return tSize > 0 ? new ItemStack[tSize] : GT_Values.emptyItemStackArray;
    }

    @Override
    public ITexture[] getTexture(Block aBlock, byte aSide, boolean isActive, int aRenderPass) {
        // TODO: MTE(Texture)
        return new ITexture[0];
    }

    @Override
    public void setLightValue(byte aLightValue) {

    }

    @Override
    public String getInventoryName() {
        final String name = getCustomName();
        if(name != null) return name;
        final MultiTileEntityRegistry tRegistry = MultiTileEntityRegistry.getRegistry(getMultiTileEntityRegistryID());
        return tRegistry == null ? getClass().getName() : tRegistry.getLocal(getMultiTileEntityID());
    }


    @Override
    public boolean isUseableByPlayer(EntityPlayer aPlayer) {
        return playerOwnsThis(aPlayer, false) && mTickTimer > 40 &&
            getTileEntityOffset(0, 0, 0) == this &&
            aPlayer.getDistanceSq(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5) < 64 && allowInteraction(aPlayer);
    }


    @Override
    public boolean isLiquidInput(byte aSide) {
        return aSide != mFacing;
    }

    @Override
    public boolean isLiquidOutput(byte aSide) {
        return aSide != mFacing;
    }


    @Override
    protected IFluidTank[] getFluidTanks(byte aSide) {
        final boolean fluidInput = isLiquidInput(aSide);
        final boolean fluidOutput = isLiquidOutput(aSide);

        if(fluidInput && fluidOutput) {
            final IFluidTank[] rTanks = new IFluidTank[ mTanksInput.length + mTanksOutput.length];
            System.arraycopy(mTanksInput, 0, rTanks, 0, mTanksInput.length);
            System.arraycopy(mTanksOutput, 0, rTanks, mTanksInput.length, mTanksOutput.length);
            return rTanks;
        } else if(fluidInput) {
            return mTanksInput;
        } else if(fluidOutput) {
            return mTanksOutput;
        }
        return GT_Values.emptyFluidTank;
    }

    @Override
    public IFluidTank getFluidTankFillable(byte aSide, FluidStack aFluidToFill) {
        if(!isLiquidInput(aSide)) return null;
        for (FluidTankGT tankGT : mTanksInput) if (tankGT.contains(aFluidToFill)) return tankGT;
//        if (!mRecipes.containsInput(aFluidToFill, this, slot(mRecipes.mInputItemsCount + mRecipes.mOutputItemsCount))) return null;
        for (FluidTankGT fluidTankGT : mTanksInput) if (fluidTankGT.isEmpty()) return fluidTankGT;
        return null;
    }

    @Override
    protected IFluidTank getFluidTankDrainable(byte aSide, FluidStack aFluidToDrain) {
        if(!isLiquidOutput(aSide)) return null;
        for (FluidTankGT fluidTankGT : mTanksOutput)
            if (aFluidToDrain == null ? fluidTankGT.has() : fluidTankGT.contains(aFluidToDrain))
                return fluidTankGT;

        return null;
    }

    /*
     * Energy
     */

    @Override
    public boolean isEnetInput() {
        return true;
    }

    @Override
    public boolean isEnetOutput() {
        return true;
    }

    @Override
    public boolean isUniversalEnergyStored(long aEnergyAmount) {
        return getUniversalEnergyStored() >= aEnergyAmount;
    }

    @Override
    public long getUniversalEnergyStored() {
        return mStoredEnergy;
    }

    @Override
    public long getUniversalEnergyCapacity() {
        return 0;
    }

    @Override
    public long getOutputAmperage() {
        return 1;
    }

    @Override
    public long getOutputVoltage() {
        return 1;
    }

    @Override
    public long getInputAmperage() {
        return 1;
    }

    @Override
    public long getInputVoltage() {
        return 1;
    }


    public boolean isEnergyInputSide(byte aSide) {
        return true;
    }

    public boolean isEnergyOutputSide(byte aSide) {
        return true;
    }

    @Override
    public boolean inputEnergyFrom(byte aSide) {
        if (aSide == GT_Values.SIDE_UNKNOWN) return true;
        if (aSide >= 0 && aSide < 6) {
            if(isInvalid()) return false;
            if (!getCoverBehaviorAtSideNew(aSide).letsEnergyIn(aSide, getCoverIDAtSide(aSide), getComplexCoverDataAtSide(aSide), this)) return false;
            if (isEnetInput()) return isEnergyInputSide(aSide);
        }
        return false;
    }

    @Override
    public boolean outputsEnergyTo(byte aSide) {
        if (aSide == GT_Values.SIDE_UNKNOWN) return true;
        if (aSide >= 0 && aSide < 6) {
            if (isInvalid()) return false;
            if (!getCoverBehaviorAtSideNew(aSide).letsEnergyOut(aSide, getCoverIDAtSide(aSide), getComplexCoverDataAtSide(aSide), this)) return false;
            if (isEnetOutput()) return isEnergyOutputSide(aSide);
        }
        return false;
    }

    /*
     * Inventory
     */

    @Override public boolean hasInventoryBeenModified() {
        return mInventoryChanged;
    }

    @Override
    public boolean isItemValidForSlot(int aSlot, ItemStack aStack) {
        return false;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    /**
     * The number of item (input) slots available for this machine
     */
    public int getItemInputCount() {
        return 2;
    }

    /**
     * The number of item (output) slots available for this machine
     */
    public int getItemOutputCount() {
        return 2;
    }

}
