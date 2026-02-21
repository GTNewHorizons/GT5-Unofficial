package gregtech.common.tileentities.storage;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static gregtech.api.enums.Textures.BlockIcons.MACHINE_CASINGS;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_PIPE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_QTANK;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_QTANK_GLOW;
import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;
import static net.minecraft.util.StatCollector.translateToLocal;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;

import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

import com.gtnewhorizons.modularui.api.NumberFormatMUI;
import com.gtnewhorizons.modularui.api.math.Alignment;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.widget.CycleButtonWidget;
import com.gtnewhorizons.modularui.common.widget.DrawableWidget;
import com.gtnewhorizons.modularui.common.widget.FluidSlotWidget;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IFluidLockable;
import gregtech.api.interfaces.modularui.IAddGregtechLogo;
import gregtech.api.interfaces.modularui.IAddUIWidgets;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEBasicTank;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTLanguageManager;
import gregtech.api.util.GTUtility;
import gregtech.common.gui.modularui.widget.FluidLockWidget;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.SpecialChars;

public abstract class MTEDigitalTankBase extends MTEBasicTank
    implements IFluidLockable, IAddUIWidgets, IAddGregtechLogo {

    public boolean mOutputFluid = false, mVoidFluidPart = false, mVoidFluidFull = false, mLockFluid = false;
    protected String lockedFluidName = null;
    public boolean mAllowInputFromOutputSide = false;

    public MTEDigitalTankBase(int aID, String aName, String aNameRegional, int aTier) {
        super(
            aID,
            aName,
            aNameRegional,
            aTier,
            3,
            new String[] {
                translateToLocalFormatted("GT5U.machines.digitaltank.tooltip", formatNumber(commonSizeCompute(aTier))),
                translateToLocal("GT5U.machines.digitaltank.tooltip1"), });
    }

    protected static int commonSizeCompute(int tier) {
        return switch (tier) {
            case 1 -> 4000000;
            case 2 -> 8000000;
            case 3 -> 16000000;
            case 4 -> 32000000;
            case 5 -> 64000000;
            case 6 -> 128000000;
            case 7 -> 256000000;
            case 8 -> 512000000;
            case 9 -> 1024000000;
            case 10 -> 2147483640;
            default -> 0;
        };
    }

    private static int tierPump(int tier) {
        return switch (tier) {
            case 1 -> 2;
            case 2, 3 -> 3;
            case 4, 5 -> 4;
            case 6, 7 -> 5;
            case 8 -> 6;
            case 9 -> 7;
            case 10 -> 8;
            default -> 0;
        };
    }

    public MTEDigitalTankBase(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 3, aDescription, aTextures);
    }

    @Override
    public ITexture[][][] getTextureSet(ITexture[] aTextures) {
        return new ITexture[0][0][0];
    }

    @Override
    public void addAdditionalTooltipInformation(ItemStack stack, List<String> tooltip) {
        if (stack.hasTagCompound()
            && (stack.stackTagCompound.hasKey("mFluid") || stack.stackTagCompound.hasKey("lockedFluidName"))) {
            final FluidStack tContents = FluidStack
                .loadFluidStackFromNBT(stack.stackTagCompound.getCompoundTag("mFluid"));
            if (tContents != null && tContents.amount > 0) {
                tooltip.add(
                    GTLanguageManager.addStringLocalization("TileEntity_TANK_INFO", "Contains Fluid: ")
                        + EnumChatFormatting.YELLOW
                        + tContents.getLocalizedName()
                        + EnumChatFormatting.GRAY);
                tooltip.add(
                    GTLanguageManager.addStringLocalization("TileEntity_TANK_AMOUNT", "Fluid Amount: ")
                        + EnumChatFormatting.GREEN
                        + formatNumber(tContents.amount)
                        + " L"
                        + EnumChatFormatting.GRAY);
            } else if (stack.stackTagCompound.hasKey("lockedFluidName")) {
                String fluidName = stack.stackTagCompound.getString("lockedFluidName");
                Fluid fluid = FluidRegistry.getFluid(fluidName);
                if (fluid == null) return;
                // noinspection deprecation
                tooltip.add(
                    translateToLocalFormatted(
                        "GT5U.item.tank.locked_to",
                        EnumChatFormatting.YELLOW + fluid.getLocalizedName()));
            }
        }
    }

    @Override
    public void setItemNBT(NBTTagCompound aNBT) {
        if (mFluid != null && mFluid.amount >= 0) {
            aNBT.setTag("mFluid", mFluid.writeToNBT(new NBTTagCompound()));
        }
        if (mOutputFluid) aNBT.setBoolean("mOutputFluid", true);
        if (mVoidFluidPart) aNBT.setBoolean("mVoidOverflow", true);
        if (mVoidFluidFull) aNBT.setBoolean("mVoidFluidFull", true);
        if (mLockFluid) aNBT.setBoolean("mLockFluid", true);
        if (mLockFluid && GTUtility.isStringValid(lockedFluidName)) aNBT.setString("lockedFluidName", lockedFluidName);
        if (this.mAllowInputFromOutputSide) aNBT.setBoolean("mAllowInputFromOutputSide", true);

        super.setItemNBT(aNBT);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setBoolean("mOutputFluid", this.mOutputFluid);
        aNBT.setBoolean("mVoidOverflow", this.mVoidFluidPart);
        aNBT.setBoolean("mVoidFluidFull", this.mVoidFluidFull);
        aNBT.setBoolean("mLockFluid", mLockFluid);
        if (mLockFluid && GTUtility.isStringValid(lockedFluidName)) aNBT.setString("lockedFluidName", lockedFluidName);
        else aNBT.removeTag("lockedFluidName");
        aNBT.setBoolean("mAllowInputFromOutputSide", this.mAllowInputFromOutputSide);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        mOutputFluid = aNBT.getBoolean("mOutputFluid");
        mVoidFluidPart = aNBT.getBoolean("mVoidOverflow");
        mVoidFluidFull = aNBT.getBoolean("mVoidFluidFull");
        mLockFluid = aNBT.getBoolean("mLockFluid");
        if (mLockFluid) {
            setLockedFluidName(aNBT.getString("lockedFluidName"));
        } else {
            setLockedFluidName(null);
        }
        mAllowInputFromOutputSide = aNBT.getBoolean("mAllowInputFromOutputSide");
    }

    @Override
    public boolean isFluidInputAllowed(FluidStack aFluid) {
        return !mLockFluid || lockedFluidName == null
            || lockedFluidName.equals(
                aFluid.getFluid()
                    .getName());
    }

    @Override
    public boolean isFluidChangingAllowed() {
        return !mLockFluid || lockedFluidName == null;
    }

    @Override
    public void onEmptyingContainerWhenEmpty() {
        if (this.lockedFluidName == null && this.mFluid != null && isFluidLocked()) {
            setLockedFluidName(
                this.mFluid.getFluid()
                    .getName());
        }
    }

    @Override
    public boolean doesFillContainers() {
        return true;
    }

    @Override
    public boolean doesEmptyContainers() {
        return true;
    }

    @Override
    public boolean canTankBeFilled() {
        return true;
    }

    @Override
    public boolean canTankBeEmptied() {
        return true;
    }

    @Override
    public void setLockedFluidName(String lockedFluidName) {
        lockedFluidName = GTUtility.isStringInvalid(lockedFluidName) ? null : lockedFluidName;
        this.lockedFluidName = lockedFluidName;
        if (lockedFluidName != null) {
            Fluid fluid = FluidRegistry.getFluid(lockedFluidName);
            if (fluid != null) {
                // create new FluidStack, otherwise existing 0-amount FluidStack will
                // prevent new fluid from being locked
                setFillableStack(new FluidStack(fluid, getFluidAmount()));
                mLockFluid = true;
            }
        }
        // Don't unlock if lockedFluidName == null,
        // as player might explicitly enable fluid locking with no fluid contained
    }

    @Override
    public String getLockedFluidName() {
        return this.lockedFluidName;
    }

    @Override
    public void lockFluid(boolean lock) {
        this.mLockFluid = lock;
        if (!lock) {
            setLockedFluidName(null);
        }
    }

    @Override
    public boolean isFluidLocked() {
        return this.mLockFluid;
    }

    @Override
    public boolean acceptsFluidLock(String name) {
        if (name == null || getFluidAmount() == 0) return true;
        return mFluid != null && mFluid.getFluid()
            .getName()
            .equals(name);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection sideDirection,
        ForgeDirection facingDirection, int colorIndex, boolean active, boolean redstoneLevel) {
        if (sideDirection != ForgeDirection.UP) {
            if (sideDirection == baseMetaTileEntity.getFrontFacing()) {
                return new ITexture[] { MACHINE_CASINGS[mTier][colorIndex + 1], TextureFactory.of(OVERLAY_PIPE) };
            } else return new ITexture[] { MACHINE_CASINGS[mTier][colorIndex + 1] };
        }
        return new ITexture[] { MACHINE_CASINGS[mTier][colorIndex + 1], TextureFactory.of(OVERLAY_QTANK),
            TextureFactory.builder()
                .addIcon(OVERLAY_QTANK_GLOW)
                .glow()
                .build() };
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        openGui(aPlayer);
        return true;
    }

    @Override
    public final void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ,
        ItemStack aTool) {
        if (side == getBaseMetaTileEntity().getFrontFacing()) {
            mAllowInputFromOutputSide = !mAllowInputFromOutputSide;
            GTUtility.sendChatToPlayer(
                aPlayer,
                mAllowInputFromOutputSide ? translateToLocal("gt.interact.desc.input_from_output_on")
                    : translateToLocal("gt.interact.desc.input_from_output_off"));
        }
    }

    @Override
    public FluidStack setFillableStack(FluidStack aFluid) {
        mFluid = aFluid;
        if (mFluid != null) {
            mFluid.amount = Math.min(mFluid.amount, getRealCapacity());
        }
        return mFluid;
    }

    @Override
    public FluidStack setDrainableStack(FluidStack aFluid) {
        mFluid = aFluid;
        if (mFluid != null) {
            mFluid.amount = Math.min(mFluid.amount, getRealCapacity());
        }
        return mFluid;
    }

    @Override
    public void onPreTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isServerSide()) {
            if (isFluidChangingAllowed() && getFillableStack() != null && getFillableStack().amount <= 0)
                setFillableStack(null);

            if (mVoidFluidFull && getFillableStack() != null) {
                mVoidFluidPart = false;
                mLockFluid = false;
                setFillableStack(null);
            }

            if (doesEmptyContainers()) {
                FluidStack tFluid = GTUtility.getFluidForFilledItem(mInventory[getInputSlot()], true);
                if (tFluid != null && isFluidInputAllowed(tFluid)) {
                    if (getFillableStack() == null) {
                        if (isFluidInputAllowed(tFluid)) {
                            if ((tFluid.amount <= getRealCapacity()) || mVoidFluidPart) {
                                tFluid = tFluid.copy();
                                if (aBaseMetaTileEntity.addStackToSlot(
                                    getOutputSlot(),
                                    GTUtility.getContainerForFilledItem(mInventory[getInputSlot()], true),
                                    1)) {
                                    setFillableStack(tFluid);
                                    this.onEmptyingContainerWhenEmpty();
                                    aBaseMetaTileEntity.decrStackSize(getInputSlot(), 1);
                                }
                            }
                        }
                    } else {
                        if (tFluid.isFluidEqual(getFillableStack())) {
                            if ((((long) tFluid.amount + getFillableStack().amount) <= (long) getRealCapacity())
                                || mVoidFluidPart
                                || mVoidFluidFull) {
                                if (aBaseMetaTileEntity.addStackToSlot(
                                    getOutputSlot(),
                                    GTUtility.getContainerForFilledItem(mInventory[getInputSlot()], true),
                                    1)) {
                                    getFillableStack().amount += Math
                                        .min(tFluid.amount, getRealCapacity() - getFillableStack().amount);
                                    aBaseMetaTileEntity.decrStackSize(getInputSlot(), 1);
                                }
                            }
                        }
                    }
                }
            }

            if (doesFillContainers()) {
                ItemStack tOutput = GTUtility
                    .fillFluidContainer(getDrainableStack(), mInventory[getInputSlot()], false, true);
                if (tOutput != null && aBaseMetaTileEntity.addStackToSlot(getOutputSlot(), tOutput, 1)) {
                    FluidStack tFluid = GTUtility.getFluidForFilledItem(tOutput, true);
                    aBaseMetaTileEntity.decrStackSize(getInputSlot(), 1);
                    if (tFluid != null) getDrainableStack().amount -= tFluid.amount;
                    if (getDrainableStack().amount <= 0 && isFluidChangingAllowed()) setDrainableStack(null);
                }
            }
        }
    }

    @Override
    public int fill(FluidStack aFluid, boolean doFill) {
        if (aFluid == null || aFluid.getFluid()
            .getID() <= 0 || aFluid.amount <= 0 || !canTankBeFilled() || !isFluidInputAllowed(aFluid)) return 0;
        if (getFillableStack() != null && !getFillableStack().isFluidEqual(aFluid)) {
            return 0;
        }

        FluidStack fillableStack = getFillableStack();
        if (fillableStack == null) {
            fillableStack = aFluid.copy();
            fillableStack.amount = 0;
        }

        int amount = Math.min(aFluid.amount, getRealCapacity() - fillableStack.amount);
        if (doFill) {
            fillableStack.amount += amount;
            if (getFillableStack() == null) setFillableStack(fillableStack);
            if (this.mLockFluid && this.lockedFluidName == null) {
                setLockedFluidName(
                    aFluid.getFluid()
                        .getName());
            }
            getBaseMetaTileEntity().markDirty();
        }
        return (mVoidFluidPart || mVoidFluidFull) ? aFluid.amount : amount;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (aBaseMetaTileEntity.isServerSide()) {
            if (mOutputFluid && getDrainableStack() != null && (aTick % 20 == 0)) {
                IFluidHandler tTank = aBaseMetaTileEntity.getITankContainerAtSide(aBaseMetaTileEntity.getFrontFacing());
                if (tTank != null) {
                    FluidStack tDrained = drain(20 * (1 << (3 + 2 * tierPump(mTier))), false);
                    if (tDrained != null) {
                        int tFilledAmount = tTank.fill(aBaseMetaTileEntity.getBackFacing(), tDrained, false);
                        if (tFilledAmount > 0)
                            tTank.fill(aBaseMetaTileEntity.getBackFacing(), drain(tFilledAmount, true), true);
                    }
                }
            }
        }
    }

    @Override
    public boolean isFacingValid(ForgeDirection side) {
        return true;
    }

    @Override
    public boolean isInputFacing(ForgeDirection side) {
        return true;
    }

    @Override
    public boolean isLiquidInput(ForgeDirection side) {
        return mAllowInputFromOutputSide || side != getBaseMetaTileEntity().getFrontFacing();
    }

    public boolean allowOverflow() {
        return mVoidFluidPart || mVoidFluidFull;
    }

    @Override
    public int getCapacity() {
        return allowOverflow() ? Integer.MAX_VALUE : getRealCapacity();
    }

    @Override
    public int getRealCapacity() {
        return commonSizeCompute(mTier);
    }

    @Override
    public boolean isGivingInformation() {
        return true;
    }

    @Override
    public FluidTankInfo getInfo() {
        return new FluidTankInfo(getFluid(), getCapacity());
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection side) {
        return new FluidTankInfo[] { getInfo() };
    }

    @Nonnull
    public FluidTankInfo[] getRealTankInfo(ForgeDirection side) {
        return new FluidTankInfo[] { new FluidTankInfo(getFluid(), getRealCapacity()) };
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currenttip, accessor, config);

        NBTTagCompound tag = accessor.getNBTData();
        FluidStack fluid = tag.hasKey("mFluid") ? FluidStack.loadFluidStackFromNBT(tag.getCompoundTag("mFluid")) : null;
        if (fluid != null && fluid.amount >= 0) {
            currenttip.remove(0);
            currenttip.add(
                0,
                SpecialChars.getRenderString(
                    "waila.fluid",
                    fluid.getFluid()
                        .getName(),
                    fluid.getLocalizedName(),
                    fluid.amount + "",
                    getRealCapacity() + ""));
        } else {
            currenttip.add(0, StatCollector.translateToLocal("GT5U.waila.digital_tank.empty"));
        }
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
        FluidStack fluid = getFluid();
        if (fluid != null) tag.setTag("mFluid", fluid.writeToNBT(new NBTTagCompound()));
        else if (tag.hasKey("mFluid")) tag.removeTag("mFluid");
    }

    protected static final NumberFormatMUI numberFormat = new NumberFormatMUI();

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        fluidTank.setAllowOverflow(allowOverflow());
        fluidTank.setPreventDraining(mLockFluid);
        final boolean isServer = GTUtility.isServer();
        FluidSlotWidget fluidSlotWidget = new FluidSlotWidget(fluidTank);
        builder.widget(
            new DrawableWidget().setDrawable(GTUITextures.PICTURE_SCREEN_BLACK)
                .setPos(7, 16)
                .setSize(71, 45))
            .widget(
                new SlotWidget(inventoryHandler, getInputSlot())
                    .setBackground(getGUITextureSet().getItemSlot(), GTUITextures.OVERLAY_SLOT_IN)
                    .setPos(79, 16))
            .widget(
                new SlotWidget(inventoryHandler, getOutputSlot()).setAccess(true, false)
                    .setBackground(getGUITextureSet().getItemSlot(), GTUITextures.OVERLAY_SLOT_OUT)
                    .setPos(79, 43))
            .widget(
                fluidSlotWidget.setOnClickContainer(widget -> onEmptyingContainerWhenEmpty())
                    .setBackground(GTUITextures.TRANSPARENT)
                    .setPos(58, 41))
            .widget(
                new TextWidget(translateToLocal("GT5U.machines.digitaltank.fluid.amount"))
                    .setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setPos(10, 20))
            .widget(
                new TextWidget().setStringSupplier(() -> numberFormat.format(mFluid != null ? mFluid.amount : 0))
                    .setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setPos(10, 30))
            .widget(
                new DrawableWidget().setDrawable(GTUITextures.PICTURE_SCREEN_BLACK)
                    .setPos(98, 16)
                    .setSize(71, 45))
            .widget(new FluidLockWidget(this).setPos(149, 41))
            .widget(
                new TextWidget(translateToLocal("GT5U.machines.digitaltank.lockfluid.label"))
                    .setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setPos(101, 20))
            .widget(TextWidget.dynamicString(() -> {
                FluidStack fluidStack = FluidRegistry.getFluidStack(lockedFluidName, 1);
                return fluidStack != null ? fluidStack.getLocalizedName()
                    : translateToLocal("GT5U.machines.digitaltank.lockfluid.empty");
            })
                .setSynced(false)
                .setDefaultColor(COLOR_TEXT_WHITE.get())
                .setTextAlignment(Alignment.CenterLeft)
                .setMaxWidth(65)
                .setPos(101, 30))
            .widget(new CycleButtonWidget().setToggle(() -> mOutputFluid, val -> {
                mOutputFluid = val;
                if (isServer) {
                    if (!mOutputFluid) {
                        GTUtility.sendChatToPlayer(
                            buildContext.getPlayer(),
                            GTUtility.trans("262", "Fluid Auto Output Disabled"));
                    } else {
                        GTUtility.sendChatToPlayer(
                            buildContext.getPlayer(),
                            GTUtility.trans("263", "Fluid Auto Output Enabled"));
                    }
                }
            })
                .setVariableBackground(GTUITextures.BUTTON_STANDARD_TOGGLE)
                .setStaticTexture(GTUITextures.OVERLAY_BUTTON_AUTOOUTPUT_FLUID)
                .setGTTooltip(() -> mTooltipCache.getData("GT5U.machines.digitaltank.autooutput.tooltip"))
                .setTooltipShowUpDelay(TOOLTIP_DELAY)
                .setPos(7, 63)
                .setSize(18, 18))
            .widget(new CycleButtonWidget().setToggle(() -> mLockFluid, val -> {
                lockFluid(val);
                fluidTank.setPreventDraining(mLockFluid);

                String inBrackets;
                if (mLockFluid) {
                    if (mFluid == null) {
                        setLockedFluidName(null);
                        inBrackets = GTUtility
                            .trans("264", "currently none, will be locked to the next that is put in");
                    } else {
                        setLockedFluidName(
                            getDrainableStack().getFluid()
                                .getName());
                        inBrackets = getDrainableStack().getLocalizedName();
                    }
                    if (isServer) {
                        GTUtility.sendChatToPlayer(
                            buildContext.getPlayer(),
                            String.format("%s (%s)", GTUtility.trans("265", "1 specific Fluid"), inBrackets));
                    }
                } else {
                    fluidTank.drain(0, true);
                    if (isServer) {
                        GTUtility.sendChatToPlayer(
                            buildContext.getPlayer(),
                            GTUtility.trans("266", "Lock Fluid Mode Disabled"));
                    }
                }
                fluidSlotWidget.notifyTooltipChange();
            })
                .setVariableBackground(GTUITextures.BUTTON_STANDARD_TOGGLE)
                .setStaticTexture(GTUITextures.OVERLAY_BUTTON_LOCK)
                .setGTTooltip(() -> mTooltipCache.getData("GT5U.machines.digitaltank.lockfluid.tooltip"))
                .setTooltipShowUpDelay(TOOLTIP_DELAY)
                .setPos(25, 63)
                .setSize(18, 18))
            .widget(new CycleButtonWidget().setToggle(() -> mAllowInputFromOutputSide, val -> {
                mAllowInputFromOutputSide = val;
                if (isServer) {
                    if (!mAllowInputFromOutputSide) {
                        GTUtility.sendChatTrans(buildContext.getPlayer(), "gt.interact.desc.input_from_output_off");
                    } else {
                        GTUtility.sendChatTrans(buildContext.getPlayer(), "gt.interact.desc.input_from_output_on");
                    }
                }
            })
                .setVariableBackground(GTUITextures.BUTTON_STANDARD_TOGGLE)
                .setStaticTexture(GTUITextures.OVERLAY_BUTTON_INPUT_FROM_OUTPUT_SIDE)
                .setGTTooltip(() -> mTooltipCache.getData("GT5U.machines.digitaltank.inputfromoutput.tooltip"))
                .setTooltipShowUpDelay(TOOLTIP_DELAY)
                .setPos(43, 63)
                .setSize(18, 18))
            .widget(new CycleButtonWidget().setToggle(() -> mVoidFluidPart, val -> {
                mVoidFluidPart = val;
                fluidTank.setAllowOverflow(allowOverflow());
                if (isServer) {
                    if (!mVoidFluidPart) {
                        GTUtility.sendChatToPlayer(
                            buildContext.getPlayer(),
                            GTUtility.trans("267", "Overflow Voiding Mode Disabled"));
                    } else {
                        GTUtility.sendChatToPlayer(
                            buildContext.getPlayer(),
                            GTUtility.trans("268", "Overflow Voiding Mode Enabled"));
                    }
                }
            })
                .setVariableBackground(GTUITextures.BUTTON_STANDARD_TOGGLE)
                .setStaticTexture(GTUITextures.OVERLAY_BUTTON_TANK_VOID_EXCESS)
                .setGTTooltip(() -> mTooltipCache.getData("GT5U.machines.digitaltank.voidoverflow.tooltip"))
                .setTooltipShowUpDelay(TOOLTIP_DELAY)
                .setPos(98, 63)
                .setSize(18, 18))
            .widget(new CycleButtonWidget().setToggle(() -> mVoidFluidFull, val -> {
                mVoidFluidFull = val;
                fluidTank.setAllowOverflow(allowOverflow());
                if (isServer) {
                    if (!mVoidFluidFull) {
                        GTUtility.sendChatToPlayer(
                            buildContext.getPlayer(),
                            GTUtility.trans("269", "Void Full Mode Disabled"));
                    } else {
                        GTUtility.sendChatToPlayer(
                            buildContext.getPlayer(),
                            GTUtility.trans("270", "Void Full Mode Enabled"));
                    }
                }
            })
                .setVariableBackground(GTUITextures.BUTTON_STANDARD_TOGGLE)
                .setStaticTexture(GTUITextures.OVERLAY_BUTTON_TANK_VOID_ALL)
                .setGTTooltip(() -> mTooltipCache.getData("GT5U.machines.digitaltank.voidfull.tooltip"))
                .setTooltipShowUpDelay(TOOLTIP_DELAY)
                .setPos(116, 63)
                .setSize(18, 18));
    }
}
