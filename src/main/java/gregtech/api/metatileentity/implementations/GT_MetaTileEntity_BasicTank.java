package gregtech.api.metatileentity.implementations;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;

import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.internal.network.NetworkUtils;
import com.gtnewhorizons.modularui.common.widget.DrawableWidget;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import gregtech.api.enums.ItemList;
import gregtech.api.gui.GT_Container_BasicTank;
import gregtech.api.gui.GT_GUIContainer_BasicTank;
import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.api.interfaces.IFluidAccess;
import gregtech.api.interfaces.IHasFluidDisplayItem;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.modularui.IAddUIWidgets;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GT_Utility;
import gregtech.common.gui.modularui.widget.FluidDisplaySlotWidget;

/**
 * NEVER INCLUDE THIS FILE IN YOUR MOD!!!
 *
 * This is the main construct for my generic Tanks. Filling and emptying behavior have to be implemented manually
 */
public abstract class GT_MetaTileEntity_BasicTank extends GT_MetaTileEntity_TieredMachineBlock
    implements IHasFluidDisplayItem, IAddUIWidgets {

    public FluidStack mFluid;
    protected int mOpenerCount;

    /**
     * @param aInvSlotCount should be 3
     */
    public GT_MetaTileEntity_BasicTank(int aID, String aName, String aNameRegional, int aTier, int aInvSlotCount,
        String aDescription, ITexture... aTextures) {
        super(aID, aName, aNameRegional, aTier, aInvSlotCount, aDescription, aTextures);
    }

    public GT_MetaTileEntity_BasicTank(int aID, String aName, String aNameRegional, int aTier, int aInvSlotCount,
        String[] aDescription, ITexture... aTextures) {
        super(aID, aName, aNameRegional, aTier, aInvSlotCount, aDescription, aTextures);
    }

    public GT_MetaTileEntity_BasicTank(String aName, int aTier, int aInvSlotCount, String aDescription,
        ITexture[][][] aTextures) {
        super(aName, aTier, aInvSlotCount, aDescription, aTextures);
    }

    public GT_MetaTileEntity_BasicTank(String aName, int aTier, int aInvSlotCount, String[] aDescription,
        ITexture[][][] aTextures) {
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
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        mFluid = FluidStack.loadFluidStackFromNBT(aNBT.getCompoundTag("mFluid"));
    }

    public abstract boolean doesFillContainers();

    public abstract boolean doesEmptyContainers();

    public abstract boolean canTankBeFilled();

    public abstract boolean canTankBeEmptied();

    public abstract boolean displaysItemStack();

    /**
     * @return If fluid amount is shown on FluidDisplayItem
     */
    public abstract boolean displaysStackSize();

    public int getInputSlot() {
        return 0;
    }

    public int getOutputSlot() {
        return 1;
    }

    public int getStackDisplaySlot() {
        return 2;
    }

    public boolean isFluidInputAllowed(FluidStack aFluid) {
        return true;
    }

    public boolean isFluidChangingAllowed() {
        return true;
    }

    public FluidStack getFillableStack() {
        return mFluid;
    }

    public FluidStack setFillableStack(FluidStack aFluid) {
        mFluid = aFluid;
        return mFluid;
    }

    /**
     * If you override this and change the field returned, be sure to override {@link #isDrainableStackSeparate()} as
     * well!
     */
    public FluidStack getDrainableStack() {
        return mFluid;
    }

    public FluidStack setDrainableStack(FluidStack aFluid) {
        mFluid = aFluid;
        return mFluid;
    }

    public boolean isDrainableStackSeparate() {
        return false;
    }

    public FluidStack getDisplayedFluid() {
        return getDrainableStack();
    }

    @Deprecated
    @Override
    public Object getServerGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_Container_BasicTank(aPlayerInventory, aBaseMetaTileEntity);
    }

    @Deprecated
    @Override
    public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_GUIContainer_BasicTank(aPlayerInventory, aBaseMetaTileEntity, getLocalName());
    }

    @Override
    public void onOpenGUI() {
        super.onOpenGUI();
        mOpenerCount++;
        if (mOpenerCount == 1) updateFluidDisplayItem();
    }

    @Override
    public void onCloseGUI() {
        super.onCloseGUI();
        mOpenerCount--;
    }

    @Override
    public void onPreTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isServerSide()) {
            if (isFluidChangingAllowed() && getFillableStack() != null && getFillableStack().amount <= 0)
                setFillableStack(null);

            if (mOpenerCount > 0) updateFluidDisplayItem();

            if (doesEmptyContainers()) {
                FluidStack tFluid = GT_Utility.getFluidForFilledItem(mInventory[getInputSlot()], true);
                if (tFluid != null && isFluidInputAllowed(tFluid)) {
                    if (getFillableStack() == null) {
                        if (isFluidInputAllowed(tFluid) && tFluid.amount <= getCapacity()) {
                            if (aBaseMetaTileEntity.addStackToSlot(
                                getOutputSlot(),
                                GT_Utility.getContainerForFilledItem(mInventory[getInputSlot()], true),
                                1)) {
                                setFillableStack(tFluid.copy());
                                this.onEmptyingContainerWhenEmpty();
                                aBaseMetaTileEntity.decrStackSize(getInputSlot(), 1);
                            }
                        }
                    } else {
                        if (tFluid.isFluidEqual(getFillableStack())
                            && ((long) tFluid.amount + getFillableStack().amount) <= (long) getCapacity()) {
                            if (aBaseMetaTileEntity.addStackToSlot(
                                getOutputSlot(),
                                GT_Utility.getContainerForFilledItem(mInventory[getInputSlot()], true),
                                1)) {
                                getFillableStack().amount += tFluid.amount;
                                aBaseMetaTileEntity.decrStackSize(getInputSlot(), 1);
                            }
                        }
                    }
                }
            }

            if (doesFillContainers()) {
                ItemStack tOutput = GT_Utility
                    .fillFluidContainer(getDrainableStack(), mInventory[getInputSlot()], false, true);
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
    public void updateFluidDisplayItem() {
        if (displaysItemStack() && getStackDisplaySlot() >= 0 && getStackDisplaySlot() < mInventory.length) {
            if (getDisplayedFluid() == null) {
                if (ItemList.Display_Fluid.isStackEqual(mInventory[getStackDisplaySlot()], true, true))
                    mInventory[getStackDisplaySlot()] = null;
            } else {
                mInventory[getStackDisplaySlot()] = GT_Utility
                    .getFluidDisplayStack(getDisplayedFluid(), true, !displaysStackSize());
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
        if (aFluid == null || aFluid.getFluid()
            .getID() <= 0 || aFluid.amount <= 0 || !canTankBeFilled() || !isFluidInputAllowed(aFluid)) return 0;

        if (getFillableStack() == null || getFillableStack().getFluid()
            .getID() <= 0) {
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

        if (!getFillableStack().isFluidEqual(aFluid)) return 0;

        int space = getCapacity() - getFillableStack().amount;
        if (aFluid.amount <= space) {
            if (doFill) {
                getFillableStack().amount += aFluid.amount;
                getBaseMetaTileEntity().markDirty();
            }
            return aFluid.amount;
        }
        if (doFill) getFillableStack().amount = getCapacity();
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
        if (getDrainableStack().amount < used) used = getDrainableStack().amount;

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
    public FluidTankInfo[] getTankInfo(ForgeDirection side) {
        if (getCapacity() <= 0 && !getBaseMetaTileEntity().hasSteamEngineUpgrade()) return new FluidTankInfo[] {};
        if (isDrainableStackSeparate()) {
            return new FluidTankInfo[] { new FluidTankInfo(getFillableStack(), getCapacity()),
                new FluidTankInfo(getDrainableStack(), getCapacity()) };
        } else {
            return new FluidTankInfo[] { new FluidTankInfo(this) };
        }
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return aIndex == getOutputSlot();
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return aIndex == getInputSlot();
    }

    protected void onEmptyingContainerWhenEmpty() {
        // Do nothing
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        builder.widget(
            new DrawableWidget().setDrawable(GT_UITextures.PICTURE_SCREEN_BLACK)
                .setPos(7, 16)
                .setSize(71, 45))
            .widget(
                new DrawableWidget().setDrawable(GT_UITextures.PICTURE_GAUGE)
                    .setPos(79, 34)
                    .setSize(18, 18))
            .widget(
                new SlotWidget(inventoryHandler, getInputSlot())
                    .setBackground(getGUITextureSet().getItemSlot(), GT_UITextures.OVERLAY_SLOT_IN)
                    .setPos(79, 16))
            .widget(
                new SlotWidget(inventoryHandler, getOutputSlot())
                    .setBackground(getGUITextureSet().getItemSlot(), GT_UITextures.OVERLAY_SLOT_OUT)
                    .setPos(79, 52))
            .widget(
                createDrainableFluidSlot().setBackground(GT_UITextures.TRANSPARENT)
                    .setPos(58, 41))
            .widget(
                new TextWidget("Liquid Amount").setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setPos(10, 20))
            .widget(
                TextWidget.dynamicString(() -> GT_Utility.parseNumberToString(mFluid != null ? mFluid.amount : 0))
                    .setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setPos(10, 30));
    }

    protected FluidDisplaySlotWidget createDrainableFluidSlot() {
        return new FluidDisplaySlotWidget(inventoryHandler, getStackDisplaySlot())
            .setFluidAccessConstructor(() -> constructFluidAccess(false))
            .setIHasFluidDisplay(this)
            .setCanDrain(true)
            .setCanFill(!isDrainableStackSeparate())
            .setActionRealClick(FluidDisplaySlotWidget.Action.TRANSFER)
            .setBeforeRealClick((clickData, widget) -> {
                if (NetworkUtils.isClient()) {
                    // propagate display item content to actual fluid stored in this tank
                    setDrainableStack(
                        GT_Utility.getFluidFromDisplayStack(
                            widget.getMcSlot()
                                .getStack()));
                }
                return true;
            });
    }

    protected IFluidAccess constructFluidAccess(boolean aIsFillableStack) {
        return new BasicTankFluidAccess(this, aIsFillableStack);
    }

    protected static class BasicTankFluidAccess implements IFluidAccess {

        protected final GT_MetaTileEntity_BasicTank mTank;
        protected final boolean mIsFillableStack;

        public BasicTankFluidAccess(GT_MetaTileEntity_BasicTank aTank, boolean aIsFillableStack) {
            this.mTank = aTank;
            this.mIsFillableStack = aIsFillableStack;
        }

        @Override
        public void set(FluidStack stack) {
            if (mIsFillableStack) mTank.setFillableStack(stack);
            else mTank.setDrainableStack(stack);
        }

        @Override
        public FluidStack get() {
            return mIsFillableStack ? mTank.getFillableStack() : mTank.getDrainableStack();
        }

        @Override
        public int getCapacity() {
            return mTank.getCapacity();
        }
    }
}
