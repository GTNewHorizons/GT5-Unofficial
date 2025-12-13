package gregtech.api.metatileentity.implementations;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;

import com.gtnewhorizons.modularui.api.NumberFormatMUI;
import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.drawable.UITexture;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.api.widget.Widget;
import com.gtnewhorizons.modularui.common.fluid.FluidStackTank;
import com.gtnewhorizons.modularui.common.widget.ButtonWidget;
import com.gtnewhorizons.modularui.common.widget.DrawableWidget;
import com.gtnewhorizons.modularui.common.widget.FluidSlotWidget;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import gregtech.api.enums.GTValues;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.modularui.IAddUIWidgets;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GTUtility;

/**
 * NEVER INCLUDE THIS FILE IN YOUR MOD!!!
 * <p>
 * This is the main construct for my generic Tanks. Filling and emptying behavior have to be implemented manually
 */
public abstract class MTEBasicTank extends MTETieredMachineBlock implements IAddUIWidgets {

    public FluidStack mFluid;
    // Due to class initializing order, getCapacity might not work properly at this time.
    // So we pass supplier instead of current value here.
    protected final FluidStackTank fluidTank = new FluidStackTank(
        () -> mFluid,
        fluidStack -> mFluid = fluidStack,
        this::getRealCapacity);

    public FluidStackTank getFluidTank() {
        return fluidTank;
    }

    /**
     * @param aInvSlotCount should be 3
     */
    public MTEBasicTank(int aID, String aName, String aNameRegional, int aTier, int aInvSlotCount, String aDescription,
        ITexture... aTextures) {
        super(aID, aName, aNameRegional, aTier, aInvSlotCount, aDescription, aTextures);
    }

    public MTEBasicTank(int aID, String aName, String aNameRegional, int aTier, int aInvSlotCount,
        String[] aDescription, ITexture... aTextures) {
        super(aID, aName, aNameRegional, aTier, aInvSlotCount, aDescription, aTextures);
    }

    public MTEBasicTank(String aName, int aTier, int aInvSlotCount, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aInvSlotCount, aDescription, aTextures);
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

    @Override
    public void onPreTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isServerSide()) {
            if (isFluidChangingAllowed() && getFillableStack() != null && getFillableStack().amount <= 0) {
                setFillableStack(null);
            }

            final int inputSlot = getInputSlot();

            if (doesEmptyContainers()) {
                FluidStack tFluid = GTUtility.getFluidForFilledItem(mInventory[inputSlot], true);
                if (tFluid != null && isFluidInputAllowed(tFluid)) {
                    if (getFillableStack() == null) {
                        if (isFluidInputAllowed(tFluid) && tFluid.amount <= getCapacity()) {
                            if (aBaseMetaTileEntity.addStackToSlot(
                                getOutputSlot(),
                                GTUtility.getContainerForFilledItem(mInventory[inputSlot], true),
                                1)) {
                                setFillableStack(tFluid.copy());
                                this.onEmptyingContainerWhenEmpty();
                                aBaseMetaTileEntity.decrStackSize(inputSlot, 1);
                            }
                        }
                    } else {
                        if (tFluid.isFluidEqual(getFillableStack())
                            && ((long) tFluid.amount + getFillableStack().amount) <= (long) getCapacity()) {
                            if (aBaseMetaTileEntity.addStackToSlot(
                                getOutputSlot(),
                                GTUtility.getContainerForFilledItem(mInventory[inputSlot], true),
                                1)) {
                                getFillableStack().amount += tFluid.amount;
                                aBaseMetaTileEntity.decrStackSize(inputSlot, 1);
                            }
                        }
                    }
                }
            }

            if (doesFillContainers()) {
                ItemStack tOutput = GTUtility
                    .fillFluidContainer(getDrainableStack(), mInventory[inputSlot], false, true);
                if (tOutput != null && aBaseMetaTileEntity.addStackToSlot(getOutputSlot(), tOutput, 1)) {
                    FluidStack tFluid = GTUtility.getFluidForFilledItem(tOutput, true);
                    aBaseMetaTileEntity.decrStackSize(inputSlot, 1);
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
        if (getCapacity() <= 0 && !isSteampowered()) return GTValues.emptyFluidTankInfo;
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

    protected static final NumberFormatMUI numberFormat = new NumberFormatMUI();

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        builder.widget(
            new DrawableWidget().setDrawable(GTUITextures.PICTURE_SCREEN_BLACK)
                .setPos(7, 16)
                .setSize(71, 45))
            .widget(
                new DrawableWidget().setDrawable(GTUITextures.PICTURE_GAUGE)
                    .setPos(79, 34)
                    .setSize(18, 18))
            .widget(
                new SlotWidget(inventoryHandler, getInputSlot())
                    .setBackground(getGUITextureSet().getItemSlot(), GTUITextures.OVERLAY_SLOT_IN)
                    .setPos(79, 16))
            .widget(
                new SlotWidget(inventoryHandler, getOutputSlot()).setAccess(true, false)
                    .setBackground(getGUITextureSet().getItemSlot(), GTUITextures.OVERLAY_SLOT_OUT)
                    .setPos(79, 52))
            .widget(
                createFluidSlot().setBackground(GTUITextures.TRANSPARENT)
                    .setPos(58, 41))
            .widget(
                new TextWidget(StatCollector.translateToLocal("GT5U.machines.basic_tank.liquid_amount"))
                    .setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setPos(10, 20))
            .widget(
                new TextWidget().setStringSupplier(() -> numberFormat.format(mFluid != null ? mFluid.amount : 0))
                    .setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setPos(10, 30));
    }

    protected Widget createMuffleButton() {
        return new ButtonWidget().setOnClick((clickData, widget) -> {
            if (getBaseMetaTileEntity().isClientSide()) return;
            getBaseMetaTileEntity().setMuffler(!getBaseMetaTileEntity().isMuffled());
        })
            .setPlayClickSound(true)
            .setBackground(() -> {
                List<UITexture> ret = new ArrayList<>();
                if (getBaseMetaTileEntity().isMuffled()) {
                    ret.add(GTUITextures.OVERLAY_BUTTON_MUFFLE_ON);
                } else {
                    ret.add(GTUITextures.OVERLAY_BUTTON_MUFFLE_OFF);
                }
                return ret.toArray(new IDrawable[0]);
            })
            .addTooltip(StatCollector.translateToLocal("GT5U.machines.muffled"))
            .setPos(getGUIWidth() - 16, 4)
            .setSize(12, 12);
    }

    protected FluidSlotWidget createFluidSlot() {
        return new FluidSlotWidget(fluidTank);
    }
}
