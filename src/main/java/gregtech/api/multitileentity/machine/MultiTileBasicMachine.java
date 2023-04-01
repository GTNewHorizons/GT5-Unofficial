package gregtech.api.multitileentity.machine;

import static com.google.common.primitives.Ints.saturatedCast;
import static gregtech.api.enums.GT_Values.emptyIconContainerArray;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;

import com.gtnewhorizons.modularui.api.forge.IItemHandlerModifiable;
import com.gtnewhorizons.modularui.api.forge.ItemStackHandler;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.GT_Values.NBT;
import gregtech.api.enums.Textures;
import gregtech.api.fluid.FluidTankGT;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.multitileentity.MultiTileEntityRegistry;
import gregtech.api.multitileentity.base.BaseTickableMultiTileEntity;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Util;
import gregtech.api.util.GT_Utility;

public class MultiTileBasicMachine extends BaseTickableMultiTileEntity {

    protected static final IItemHandlerModifiable EMPTY_INVENTORY = new ItemStackHandler(0);

    private static final String TEXTURE_LOCATION = "multitileentity/machines/";
    public IIconContainer[] mTexturesInactive = emptyIconContainerArray;
    public IIconContainer[] mTexturesActive = emptyIconContainerArray;

    protected int mParallel = 1;
    protected boolean mActive = false;
    protected long mStoredEnergy = 0;
    protected FluidTankGT[] mTanksInput = GT_Values.emptyFluidTankGT, mTanksOutput = GT_Values.emptyFluidTankGT;
    protected FluidStack[] mOutputFluids = GT_Values.emptyFluidStack;

    protected IItemHandlerModifiable mInputInventory = EMPTY_INVENTORY;
    protected IItemHandlerModifiable mOutputInventory = EMPTY_INVENTORY;
    protected boolean mOutputInventoryChanged = false;

    @Override
    public String getTileEntityName() {
        return "gt.multitileentity.machine.basic";
    }

    @Override
    public void writeMultiTileNBT(NBTTagCompound aNBT) {
        super.writeMultiTileNBT(aNBT);
        if (mParallel > 0) aNBT.setInteger(NBT.PARALLEL, mParallel);
        if (mActive) aNBT.setBoolean(NBT.ACTIVE, mActive);
        if (mInputInventory != null && mInputInventory.getSlots() > 0)
            writeInventory(aNBT, mInputInventory, NBT.INV_INPUT_LIST);
        if (mOutputInventory != null && mOutputInventory.getSlots() > 0)
            writeInventory(aNBT, mOutputInventory, NBT.INV_OUTPUT_LIST);
        for (int i = 0; i < mTanksInput.length; i++) mTanksInput[i].writeToNBT(aNBT, NBT.TANK_IN + i);
        for (int i = 0; i < mTanksOutput.length; i++) mTanksOutput[i].writeToNBT(aNBT, NBT.TANK_OUT + i);
        if (mOutputFluids != null && mOutputFluids.length > 0) writeFluids(aNBT, mOutputFluids, NBT.FLUID_OUT);
    }

    protected void writeFluids(NBTTagCompound aNBT, FluidStack[] fluids, String fluidListTag) {
        if (fluids != null && fluids.length > 0) {
            final NBTTagList tList = new NBTTagList();
            for (final FluidStack tFluid : fluids) {
                if (tFluid != null) {
                    final NBTTagCompound tag = new NBTTagCompound();
                    tFluid.writeToNBT(tag);
                    tList.appendTag(tag);
                }
            }
            aNBT.setTag(fluidListTag, tList);
        }
    }

    protected void writeInventory(NBTTagCompound aNBT, IItemHandlerModifiable inv, String invListTag) {
        if (inv != null && inv.getSlots() > 0) {
            final NBTTagList tList = new NBTTagList();
            for (int tSlot = 0; tSlot < inv.getSlots(); tSlot++) {
                final ItemStack tStack = inv.getStackInSlot(tSlot);
                if (tStack != null) {
                    final NBTTagCompound tag = new NBTTagCompound();
                    tag.setByte("s", (byte) tSlot);
                    tStack.writeToNBT(tag);
                    tList.appendTag(tag);
                }
            }
            aNBT.setTag(invListTag, tList);
        }
    }

    @Override
    public void readMultiTileNBT(NBTTagCompound aNBT) {
        super.readMultiTileNBT(aNBT);
        if (aNBT.hasKey(NBT.PARALLEL)) mParallel = Math.max(1, aNBT.getInteger(NBT.PARALLEL));
        if (aNBT.hasKey(NBT.ACTIVE)) mActive = aNBT.getBoolean(NBT.ACTIVE);

        /* Inventories */
        mInputInventory = new ItemStackHandler(Math.max(aNBT.getInteger(NBT.INV_INPUT_SIZE), 0));
        mOutputInventory = new ItemStackHandler(Math.max(aNBT.getInteger(NBT.INV_OUTPUT_SIZE), 0));
        loadInventory(aNBT, mInputInventory, NBT.INV_INPUT_LIST);
        loadInventory(aNBT, mOutputInventory, NBT.INV_OUTPUT_LIST);

        /* Tanks */
        long tCapacity = 1000;
        if (aNBT.hasKey(NBT.TANK_CAPACITY)) tCapacity = saturatedCast(aNBT.getLong(NBT.TANK_CAPACITY));

        mTanksInput = new FluidTankGT[getFluidInputCount()];
        mTanksOutput = new FluidTankGT[getFluidOutputCount()];
        mOutputFluids = new FluidStack[getFluidOutputCount()];

        // TODO: See if we need the adjustable map here `.setCapacity(mRecipes, mParallel * 2L)` in place of the
        // `setCapacityMultiplier`
        for (int i = 0; i < mTanksInput.length; i++)
            mTanksInput[i] = new FluidTankGT(tCapacity).setCapacityMultiplier(mParallel * 2L)
                                                       .readFromNBT(aNBT, NBT.TANK_IN + i);
        for (int i = 0; i < mTanksOutput.length; i++)
            mTanksOutput[i] = new FluidTankGT().readFromNBT(aNBT, NBT.TANK_OUT + i);
        for (int i = 0; i < mOutputFluids.length; i++)
            mOutputFluids[i] = FluidStack.loadFluidStackFromNBT(aNBT.getCompoundTag(NBT.FLUID_OUT + "." + i));
    }

    protected void loadInventory(NBTTagCompound aNBT, IItemHandlerModifiable inv, String invListTag) {
        final NBTTagList tList = aNBT.getTagList(invListTag, 10);
        for (int i = 0; i < tList.tagCount(); i++) {
            final NBTTagCompound tNBT = tList.getCompoundTagAt(i);
            final int tSlot = tNBT.getShort("s");
            if (tSlot >= 0 && tSlot < inv.getSlots()) inv.setStackInSlot(tSlot, GT_Utility.loadItem(tNBT));
        }
    }

    @Override
    public void loadTextureNBT(NBTTagCompound aNBT) {
        // Loading the registry
        final String textureName = aNBT.getString(NBT.TEXTURE);
        mTextures = new IIconContainer[] {
                new Textures.BlockIcons.CustomIcon(TEXTURE_LOCATION + textureName + "/bottom"),
                new Textures.BlockIcons.CustomIcon(TEXTURE_LOCATION + textureName + "/top"),
                new Textures.BlockIcons.CustomIcon(TEXTURE_LOCATION + textureName + "/left"),
                new Textures.BlockIcons.CustomIcon(TEXTURE_LOCATION + textureName + "/front"),
                new Textures.BlockIcons.CustomIcon(TEXTURE_LOCATION + textureName + "/right"),
                new Textures.BlockIcons.CustomIcon(TEXTURE_LOCATION + textureName + "/side") };
        mTexturesInactive = new IIconContainer[] {
                new Textures.BlockIcons.CustomIcon(TEXTURE_LOCATION + textureName + "/overlay/inactive/bottom"),
                new Textures.BlockIcons.CustomIcon(TEXTURE_LOCATION + textureName + "/overlay/inactive/top"),
                new Textures.BlockIcons.CustomIcon(TEXTURE_LOCATION + textureName + "/overlay/inactive/left"),
                new Textures.BlockIcons.CustomIcon(TEXTURE_LOCATION + textureName + "/overlay/inactive/front"),
                new Textures.BlockIcons.CustomIcon(TEXTURE_LOCATION + textureName + "/overlay/inactive/right"),
                new Textures.BlockIcons.CustomIcon(TEXTURE_LOCATION + textureName + "/overlay/inactive/back") };
        mTexturesActive = new IIconContainer[] {
                new Textures.BlockIcons.CustomIcon(TEXTURE_LOCATION + textureName + "/overlay/active/bottom"),
                new Textures.BlockIcons.CustomIcon(TEXTURE_LOCATION + textureName + "/overlay/active/top"),
                new Textures.BlockIcons.CustomIcon(TEXTURE_LOCATION + textureName + "/overlay/active/left"),
                new Textures.BlockIcons.CustomIcon(TEXTURE_LOCATION + textureName + "/overlay/active/front"),
                new Textures.BlockIcons.CustomIcon(TEXTURE_LOCATION + textureName + "/overlay/active/right"),
                new Textures.BlockIcons.CustomIcon(TEXTURE_LOCATION + textureName + "/overlay/active/back") };
    }

    @Override
    public void copyTextures() {
        // Loading an instance
        final TileEntity tCanonicalTileEntity = MultiTileEntityRegistry.getCanonicalTileEntity(
                getMultiTileEntityRegistryID(),
                getMultiTileEntityID());
        if (tCanonicalTileEntity instanceof MultiTileBasicMachine) {
            mTextures = ((MultiTileBasicMachine) tCanonicalTileEntity).mTextures;
            mTexturesInactive = ((MultiTileBasicMachine) tCanonicalTileEntity).mTexturesInactive;
            mTexturesActive = ((MultiTileBasicMachine) tCanonicalTileEntity).mTexturesActive;
        } else {
            mTextures = mTexturesInactive = mTexturesActive = emptyIconContainerArray;
        }
    }

    @Override
    public ITexture[] getTexture(Block aBlock, byte aSide, boolean isActive, int aRenderPass) {
        return new ITexture[] {
                TextureFactory.of(mTextures[GT_Values.FACING_ROTATIONS[mFacing][aSide]], GT_Util.getRGBaArray(mRGBa)),
                TextureFactory.of(
                        (mActive ? mTexturesActive : mTexturesInactive)[GT_Values.FACING_ROTATIONS[mFacing][aSide]]) };
    }

    /*
     * Fluids
     */

    /**
     * The number of fluid (input) slots available for this machine
     */
    public int getFluidInputCount() {
        return 7;
    }

    /**
     * The number of fluid (output) slots available for this machine
     */
    public int getFluidOutputCount() {
        return 3;
    }

    @Override
    public void setLightValue(byte aLightValue) {}

    @Override
    public String getInventoryName() {
        final String name = getCustomName();
        if (name != null) return name;
        final MultiTileEntityRegistry tRegistry = MultiTileEntityRegistry.getRegistry(getMultiTileEntityRegistryID());
        return tRegistry == null ? getClass().getName() : tRegistry.getLocal(getMultiTileEntityID());
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer aPlayer) {
        return playerOwnsThis(aPlayer, false) && mTickTimer > 40
                && getTileEntityOffset(0, 0, 0) == this
                && aPlayer.getDistanceSq(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5) < 64
                && allowInteraction(aPlayer);
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

        if (fluidInput && fluidOutput) {
            final IFluidTank[] rTanks = new IFluidTank[mTanksInput.length + mTanksOutput.length];
            System.arraycopy(mTanksInput, 0, rTanks, 0, mTanksInput.length);
            System.arraycopy(mTanksOutput, 0, rTanks, mTanksInput.length, mTanksOutput.length);
            return rTanks;
        } else if (fluidInput) {
            return mTanksInput;
        } else if (fluidOutput) {
            return mTanksOutput;
        }
        return GT_Values.emptyFluidTank;
    }

    @Override
    public IFluidTank getFluidTankFillable(byte aSide, FluidStack aFluidToFill) {
        if (!isLiquidInput(aSide)) return null;
        for (FluidTankGT tankGT : mTanksInput) if (tankGT.contains(aFluidToFill)) return tankGT;
        // if (!mRecipes.containsInput(aFluidToFill, this, slot(mRecipes.mInputItemsCount +
        // mRecipes.mOutputItemsCount))) return null;
        for (FluidTankGT fluidTankGT : mTanksInput) if (fluidTankGT.isEmpty()) return fluidTankGT;
        return null;
    }

    @Override
    protected IFluidTank getFluidTankDrainable(byte aSide, FluidStack aFluidToDrain) {
        if (!isLiquidOutput(aSide)) return null;
        for (FluidTankGT fluidTankGT : mTanksOutput)
            if (aFluidToDrain == null ? fluidTankGT.has() : fluidTankGT.contains(aFluidToDrain)) return fluidTankGT;

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
            if (isInvalid()) return false;
            if (!getCoverInfoAtSide(aSide).letsEnergyIn()) return false;
            if (isEnetInput()) return isEnergyInputSide(aSide);
        }
        return false;
    }

    @Override
    public boolean outputsEnergyTo(byte aSide) {
        if (aSide == GT_Values.SIDE_UNKNOWN) return true;
        if (aSide >= 0 && aSide < 6) {
            if (isInvalid()) return false;
            if (!getCoverInfoAtSide(aSide).letsEnergyOut()) return false;
            if (isEnetOutput()) return isEnergyOutputSide(aSide);
        }
        return false;
    }

    /*
     * Inventory
     */

    @Override
    public boolean hasInventoryBeenModified() {
        // True if the input inventory has changed
        return mInventoryChanged;
    }

    public void markOutputInventoryBeenModified() {
        mOutputInventoryChanged = true;
    }

    public boolean hasOutputInventoryBeenModified() {
        // True if the output inventory has changed
        return mOutputInventoryChanged;
    }

    @Override
    public boolean isItemValidForSlot(int aSlot, ItemStack aStack) {
        return false;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }
}
