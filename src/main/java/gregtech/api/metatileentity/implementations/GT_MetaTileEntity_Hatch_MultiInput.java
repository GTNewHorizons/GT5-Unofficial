package gregtech.api.metatileentity.implementations;

import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_INPUT_HATCH_2x2;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;

import com.gtnewhorizons.modularui.api.ModularUITextures;
import com.gtnewhorizons.modularui.api.math.Pos2d;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.internal.network.NetworkUtils;

import gregtech.api.enums.ItemList;
import gregtech.api.interfaces.IFluidAccess;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.modularui.IAddUIWidgets;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Utility;
import gregtech.common.gui.modularui.widget.FluidDisplaySlotWidget;

public class GT_MetaTileEntity_Hatch_MultiInput extends GT_MetaTileEntity_Hatch_Input implements IAddUIWidgets {

    public FluidStack[] mStoredFluid;
    public int mCapacityPer;

    public GT_MetaTileEntity_Hatch_MultiInput(int aID, int aSlot, String aName, String aNameRegional, int aTier) {
        super(aID, aSlot, aName, aNameRegional, aTier);
        this.mStoredFluid = new FluidStack[aSlot];
        mCapacityPer = 8000 * (1 << aTier) / aSlot;
    }

    public GT_MetaTileEntity_Hatch_MultiInput(String aName, int aSlot, int aTier, String[] aDescription,
            ITexture[][][] aTextures) {
        super(aName, aSlot, aTier, aDescription, aTextures);
        this.mStoredFluid = new FluidStack[aSlot];
        mCapacityPer = 8000 * (1 << aTier) / aSlot;
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_Hatch_MultiInput(mName, getMaxType(), mTier, mDescriptionArray, mTextures);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        if (mStoredFluid != null) {
            for (int i = 0; i < mStoredFluid.length; i++) {
                if (mStoredFluid[i] != null)
                    aNBT.setTag("mFluid" + i, mStoredFluid[i].writeToNBT(new NBTTagCompound()));
            }
        }
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        if (mStoredFluid != null) {
            for (int i = 0; i < mStoredFluid.length; i++) {
                if (aNBT.hasKey("mFluid" + i)) {
                    mStoredFluid[i] = FluidStack.loadFluidStackFromNBT(aNBT.getCompoundTag("mFluid" + i));
                }
            }
        }
    }

    @Override
    public boolean displaysStackSize() {
        return true;
    }

    public FluidStack[] getStoredFluid() {
        return mStoredFluid;
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, TextureFactory.of(OVERLAY_INPUT_HATCH_2x2) };
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, TextureFactory.of(OVERLAY_INPUT_HATCH_2x2) };
    }

    public int getMaxType() {
        return mStoredFluid.length;
    }

    @Override
    public FluidStack getFluid() {
        for (FluidStack tFluid : mStoredFluid) {
            if (tFluid != null && tFluid.amount > 0) return tFluid;
        }
        return null;
    }

    public FluidStack getFluid(int aSlot) {
        if (mStoredFluid == null || aSlot < 0 || aSlot >= getMaxType()) return null;
        return mStoredFluid[aSlot];
    }

    @Override
    public int getFluidAmount() {
        if (getFluid() != null) {
            return getFluid().amount;
        }
        return 0;
    }

    @Override
    public int getCapacity() {
        return mCapacityPer;
    }

    public int getFirstEmptySlot() {
        for (int i = 0; i < mStoredFluid.length; i++) {
            if (mStoredFluid[i] == null) return i;
        }
        return -1;
    }

    public boolean hasFluid(FluidStack aFluid) {
        if (aFluid == null) return false;
        for (FluidStack tFluid : mStoredFluid) {
            if (aFluid.isFluidEqual(tFluid)) return true;
        }
        return false;
    }

    public int getFluidSlot(FluidStack tFluid) {
        if (tFluid == null) return -1;
        for (int i = 0; i < mStoredFluid.length; i++) {
            if (tFluid.equals(mStoredFluid[i])) return i;
        }
        return -1;
    }

    public int getFluidAmount(FluidStack tFluid) {
        int tSlot = getFluidSlot(tFluid);
        if (tSlot != -1) {
            return mStoredFluid[tSlot].amount;
        }
        return 0;
    }

    public void setFluid(FluidStack aFluid, int aSlot) {
        if (aSlot < 0 || aSlot >= getMaxType()) return;
        mStoredFluid[aSlot] = aFluid;
    }

    public void addFluid(FluidStack aFluid, int aSlot) {
        if (aSlot < 0 || aSlot >= getMaxType()) return;
        if (aFluid.equals(mStoredFluid[aSlot])) mStoredFluid[aSlot].amount += aFluid.amount;
        if (mStoredFluid[aSlot] == null) mStoredFluid[aSlot] = aFluid.copy();
    }

    @Override
    public void onPreTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isServerSide()) {
            mFluid = getFluid();
        }
        super.onPreTick(aBaseMetaTileEntity, aTick);
    }

    @Override
    public int fill(FluidStack aFluid, boolean doFill) {
        if (aFluid == null || aFluid.getFluid()
                                    .getID()
                <= 0 || aFluid.amount <= 0 || !canTankBeFilled() || !isFluidInputAllowed(aFluid))
            return 0;
        if (!hasFluid(aFluid) && getFirstEmptySlot() != -1) {
            int tFilled = Math.min(aFluid.amount, mCapacityPer);
            if (doFill) {
                FluidStack tFluid = aFluid.copy();
                tFluid.amount = tFilled;
                addFluid(tFluid, getFirstEmptySlot());
                getBaseMetaTileEntity().markDirty();
            }
            return tFilled;
        }
        if (hasFluid(aFluid)) {
            int tLeft = mCapacityPer - getFluidAmount(aFluid);
            int tFilled = Math.min(tLeft, aFluid.amount);
            if (doFill) {
                FluidStack tFluid = aFluid.copy();
                tFluid.amount = tFilled;
                addFluid(tFluid, getFluidSlot(tFluid));
                getBaseMetaTileEntity().markDirty();
            }
            return tFilled;
        }
        return 0;
    }

    @Override
    public FluidStack drain(int maxDrain, boolean doDrain) {
        if (getFluid() == null || !canTankBeEmptied()) return null;
        if (getFluid().amount <= 0 && isFluidChangingAllowed()) {
            setFluid(null, getFluidSlot(getFluid()));
            getBaseMetaTileEntity().markDirty();
            return null;
        }
        FluidStack tRemove = getFluid().copy();
        tRemove.amount = Math.min(maxDrain, tRemove.amount);
        if (doDrain) {
            getFluid().amount -= tRemove.amount;
            getBaseMetaTileEntity().markDirty();
        }
        if (getFluid() == null || getFluid().amount <= 0 && isFluidChangingAllowed()) {
            setFluid(null, getFluidSlot(getFluid()));
            getBaseMetaTileEntity().markDirty();
        }
        return tRemove;
    }

    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
        return fill(resource, doFill);
    }

    @Override
    public FluidStack drain(ForgeDirection from, FluidStack aFluid, boolean doDrain) {
        if (aFluid == null || !hasFluid(aFluid)) return null;
        FluidStack tStored = mStoredFluid[getFluidSlot(aFluid)];
        if (tStored.amount <= 0 && isFluidChangingAllowed()) {
            setFluid(null, getFluidSlot(tStored));
            getBaseMetaTileEntity().markDirty();
            return null;
        }
        FluidStack tRemove = tStored.copy();
        tRemove.amount = Math.min(aFluid.amount, tRemove.amount);
        if (doDrain) {
            tStored.amount -= tRemove.amount;
            getBaseMetaTileEntity().markDirty();
        }
        if (tStored.amount <= 0 && isFluidChangingAllowed()) {
            setFluid(null, getFluidSlot(tStored));
            getBaseMetaTileEntity().markDirty();
        }
        return tRemove;
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection from) {
        FluidTankInfo[] FTI = new FluidTankInfo[getMaxType()];
        for (int i = 0; i < getMaxType(); i++) {
            FTI[i] = new FluidTankInfo(mStoredFluid[i], mCapacityPer);
        }
        return FTI;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isServerSide() && mStoredFluid != null) {
            for (int i = 0; i < getMaxType(); i++) {
                if (mStoredFluid[i] != null && mStoredFluid[i].amount <= 0) {
                    mStoredFluid[i] = null;
                }
            }
        }
        super.onPostTick(aBaseMetaTileEntity, aTick);
    }

    @Override
    public boolean isValidSlot(int aIndex) {
        return aIndex >= 4;
    }

    @Override
    public void updateFluidDisplayItem() {
        for (int i = 0; i < 4; i++) {
            updateFluidDisplayItem(i);
        }
    }

    public void updateFluidDisplayItem(int index) {
        if (getFluid(index) == null || getFluid(index).amount <= 0) {
            if (ItemList.Display_Fluid.isStackEqual(mInventory[index], true, true)) mInventory[index] = null;
        } else {
            mInventory[index] = GT_Utility.getFluidDisplayStack(getFluid(index), true, !displaysStackSize());
        }
    }

    @Override
    public boolean useModularUI() {
        return true;
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        final int SLOT_NUMBER = 4;
        final Pos2d[] positions = new Pos2d[] { new Pos2d(70, 25), new Pos2d(88, 25), new Pos2d(70, 43),
                new Pos2d(88, 43), };

        for (int i = 0; i < SLOT_NUMBER; i++) {
            final int slotId = i;
            builder.widget(
                    new FluidDisplaySlotWidget(inventoryHandler, slotId)
                                                                        .setFluidAccessConstructor(
                                                                                () -> constructFluidAccess(slotId))
                                                                        .setIHasFluidDisplay(this)
                                                                        .setCanDrain(true)
                                                                        .setCanFill(!isDrainableStackSeparate())
                                                                        .setActionRealClick(
                                                                                FluidDisplaySlotWidget.Action.TRANSFER)
                                                                        .setBeforeRealClick((clickData, widget) -> {
                                                                            if (NetworkUtils.isClient()) {
                                                                                // propagate display item content to
                                                                                // actual fluid stored in this tank
                                                                                setFluid(
                                                                                        GT_Utility.getFluidFromDisplayStack(
                                                                                                widget.getMcSlot()
                                                                                                      .getStack()),
                                                                                        slotId);
                                                                            }
                                                                            ItemStack tStackHeld = widget.getContext()
                                                                                                         .getPlayer().inventory.getItemStack();
                                                                            FluidStack tFluidHeld = GT_Utility.getFluidForFilledItem(
                                                                                    tStackHeld,
                                                                                    true);
                                                                            return constructFluidAccess(slotId).isMatch(
                                                                                    tFluidHeld,
                                                                                    slotId);
                                                                        })
                                                                        .setUpdateFluidDisplayItem(
                                                                                () -> updateFluidDisplayItem(slotId))
                                                                        .setBackground(ModularUITextures.FLUID_SLOT)
                                                                        .setPos(positions[slotId]));
        }
    }

    protected MultiFluidAccess constructFluidAccess(int aSlot) {
        return new MultiFluidAccess(this, aSlot);
    }

    protected static class MultiFluidAccess implements IFluidAccess {

        private final GT_MetaTileEntity_Hatch_MultiInput mTank;
        private final int mSlot;

        public MultiFluidAccess(GT_MetaTileEntity_Hatch_MultiInput aTank, int aSlot) {
            this.mTank = aTank;
            this.mSlot = aSlot;
        }

        public boolean isMatch(FluidStack stack, int slot) {
            if (!mTank.hasFluid(stack)) return true;
            if (stack == null) return true;
            return stack.equals(mTank.getFluid(slot));
        }

        @Override
        public void set(FluidStack stack) {
            mTank.setFluid(stack, mSlot);
        }

        @Override
        public FluidStack get() {
            return mTank.getFluid(mSlot);
        }

        @Override
        public int getCapacity() {
            return mTank.getCapacity();
        }
    }
}
