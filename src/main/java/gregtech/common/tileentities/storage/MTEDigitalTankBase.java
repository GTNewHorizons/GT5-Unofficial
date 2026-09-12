package gregtech.common.tileentities.storage;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumberCompact;
import static gregtech.api.enums.Textures.BlockIcons.MACHINE_CASINGS;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_PIPE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_QTANK;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_QTANK_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_SCREEN_GLASS;
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

import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.google.common.io.ByteArrayDataInput;
import com.gtnewhorizon.gtnhlib.item.ItemStackNBT;
import com.gtnewhorizon.gtnhlib.util.numberformatting.options.CompactOptions;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IFluidContainerItemMetaTile;
import gregtech.api.interfaces.metatileentity.IFluidLockableMui2;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEBasicTank;
import gregtech.api.render.ISBRInventoryContext;
import gregtech.api.render.ISBRWorldContext;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTByteBuffer;
import gregtech.api.util.GTUtility;
import gregtech.common.gui.modularui.singleblock.base.MTEDigitalTankBaseGui;
import gregtech.common.render.DigitalStorageRenderer;
import gregtech.common.render.IMTERenderer;
import io.netty.buffer.ByteBuf;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.SpecialChars;

public abstract class MTEDigitalTankBase extends MTEBasicTank
    implements IFluidLockableMui2, IFluidContainerItemMetaTile, IMTERenderer {

    public static final int DISPLAY_FILL_LEVELS = 64;
    private static final long RENDER_UPDATE_INTERVAL = 20;
    public static final CompactOptions DISPLAY_COUNT_FORMAT = new CompactOptions().setDecimalPlaces(1);

    protected boolean mOutputFluid = false, mVoidFluidPart = false, mVoidFluidFull = false, mLockFluid = false;
    protected Fluid lockedFluid = null;
    protected boolean mAllowInputFromOutputSide = false;
    protected boolean mDisableFilter = true;

    private FluidStack lastRenderFluid;
    private int lastRenderAmount;
    private long lastRenderPacketTick = Long.MIN_VALUE;

    private FluidStack displayFluidStack;
    private int displayFluidAmount;
    private int displayFillLevel;
    private String displayAmountText;

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
    @SideOnly(Side.CLIENT)
    public boolean renderInWorld(ISBRWorldContext ctx) {
        return DigitalStorageRenderer.renderTankInWorld(this, ctx);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean renderInInventory(ISBRInventoryContext ctx) {
        return DigitalStorageRenderer.renderTankInInventory(this, ctx);
    }

    @Override
    public void renderTESR(double x, double y, double z, float timeSinceLastTick) {
        DigitalStorageRenderer.renderTankStack(this, x, y, z, timeSinceLastTick);
    }

    @Override
    public void writeToStream(ByteBuf buffer) {
        super.writeToStream(buffer);
        FluidStack fluid = getFluid();
        boolean hasFluid = fluid != null && fluid.amount > 0 && fluid.getFluid() != null;
        buffer.writeBoolean(hasFluid);
        if (hasFluid) ByteBufUtils.writeTag(buffer, fluid.writeToNBT(new NBTTagCompound()));
    }

    @Override
    public void readFromStream(ByteBuf buffer) {
        super.readFromStream(buffer);
        FluidStack fluid = buffer.readBoolean() ? FluidStack.loadFluidStackFromNBT(ByteBufUtils.readTag(buffer)) : null;
        updateClientDisplay(fluid);
    }

    @Override
    public void encodeRenderData(ByteBuf buffer) {
        FluidStack fluid = getFluid();
        boolean hasFluid = fluid != null && fluid.amount > 0 && fluid.getFluid() != null;
        buffer.writeBoolean(hasFluid);
        if (hasFluid) ByteBufUtils.writeTag(buffer, fluid.writeToNBT(new NBTTagCompound()));
    }

    @Override
    public void decodeRenderData(ByteArrayDataInput buffer) {
        FluidStack fluid = buffer.readBoolean()
            ? FluidStack.loadFluidStackFromNBT(GTByteBuffer.readCompoundTagFromGreggyByteBuf(buffer))
            : null;
        updateClientDisplay(fluid);
    }

    public FluidStack getClientDisplayFluidStack() {
        return displayFluidStack;
    }

    public int getClientDisplayFillLevel() {
        return displayFillLevel;
    }

    public String getClientDisplayAmountText() {
        if (displayAmountText == null) {
            displayAmountText = formatNumberCompact(displayFluidAmount, DISPLAY_COUNT_FORMAT) + " / "
                + formatNumberCompact(getRealCapacity(), DISPLAY_COUNT_FORMAT);
        }
        return displayAmountText;
    }

    private void updateClientDisplay(FluidStack fluid) {
        if (fluid == null || fluid.amount <= 0 || fluid.getFluid() == null) fluid = null;
        int amount = fluid == null ? 0 : fluid.amount;
        if (displayFluidStack != null && fluid != null
            && displayFluidStack.isFluidEqual(fluid)
            && displayFluidAmount == amount) return;
        if (displayFluidStack == null && fluid == null && displayFluidAmount == amount) return;

        displayFluidStack = fluid == null ? null : fluid.copy();
        displayFluidAmount = amount;
        int capacity = getRealCapacity();
        displayFillLevel = getDisplayFillLevel(amount);
        displayAmountText = formatNumberCompact(amount) + " / " + formatNumberCompact(capacity);
    }

    public int getDisplayFillLevel(int amount) {
        int capacity = getRealCapacity();
        int fillLevel = capacity <= 0 ? 0
            : Math.min(DISPLAY_FILL_LEVELS, (int) (((long) amount * DISPLAY_FILL_LEVELS + capacity / 2L) / capacity));
        return amount > 0 && fillLevel == 0 ? 1 : fillLevel;
    }

    @Override
    public ITexture[][][] getTextureSet(ITexture[] aTextures) {
        return new ITexture[0][0][0];
    }

    @Override
    public void addAdditionalTooltipInformation(ItemStack stack, List<String> tooltip) {
        if (ItemStackNBT.hasKey(stack, "mFluid") || ItemStackNBT.hasKey(stack, "lockedFluidName")) {
            final FluidStack tContents = FluidStack
                .loadFluidStackFromNBT(stack.stackTagCompound.getCompoundTag("mFluid"));
            if (tContents != null && tContents.amount > 0) {
                tooltip.add(
                    StatCollector.translateToLocalFormatted("gt.tileentity.tank_info", tContents.getLocalizedName()));
                tooltip.add(
                    StatCollector
                        .translateToLocalFormatted("gt.tileentity.tank_amount", formatNumber(tContents.amount)));
            } else if (ItemStackNBT.hasKey(stack, "lockedFluidName")) {
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
        if (mLockFluid && lockedFluid != null) aNBT.setString("lockedFluidName", lockedFluid.getName());
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
        if (mLockFluid && lockedFluid != null) aNBT.setString("lockedFluidName", lockedFluid.getName());
        else aNBT.removeTag("lockedFluidName");
        aNBT.setBoolean("mAllowInputFromOutputSide", this.mAllowInputFromOutputSide);
        aNBT.setBoolean("mDisableFilter", this.mDisableFilter);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        mOutputFluid = aNBT.getBoolean("mOutputFluid");
        mVoidFluidPart = aNBT.getBoolean("mVoidOverflow");
        mVoidFluidFull = aNBT.getBoolean("mVoidFluidFull");
        mLockFluid = aNBT.getBoolean("mLockFluid");
        if (mLockFluid) {
            setLockedFluid(FluidRegistry.getFluid(aNBT.getString("lockedFluidName")));
        } else {
            setLockedFluid(null);
        }
        mAllowInputFromOutputSide = aNBT.getBoolean("mAllowInputFromOutputSide");
        mDisableFilter = !aNBT.hasKey("mDisableFilter") || aNBT.getBoolean("mDisableFilter");
    }

    @Override
    public boolean isFluidInputAllowed(FluidStack aFluid) {
        return !mLockFluid || lockedFluid == null || lockedFluid.equals(aFluid.getFluid());
    }

    @Override
    public boolean isFluidChangingAllowed() {
        return !mLockFluid || lockedFluid == null;
    }

    @Override
    public void onEmptyingContainerWhenEmpty() {
        if (this.lockedFluid == null && this.mFluid != null && isFluidLocked()) {
            setLockedFluid(this.mFluid.getFluid());
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
    public void setLockedFluid(Fluid lockedFluid) {
        if (mVoidFluidFull) return;

        Fluid temp = this.lockedFluid;
        this.lockedFluid = lockedFluid;
        if (lockedFluid != null) {
            if (getFluidAmount() == 0) {
                // create new FluidStack, otherwise existing 0-amount FluidStack will
                // prevent new fluid from being locked
                setFillableStack(new FluidStack(lockedFluid, getFluidAmount()));
            }
            mLockFluid = true;
        }

        // disable lock if the lock slot was cleared
        if (temp != null && lockedFluid == null) mLockFluid = false;
    }

    @Override
    public Fluid getLockedFluid() {
        return this.lockedFluid;
    }

    @Override
    public void lockFluid(boolean lock) {
        if (mVoidFluidFull) return;

        this.mLockFluid = lock;
        fluidTank.setPreventDraining(lock);

        if (lock) {
            if (mFluid == null) {
                setLockedFluid(null);
            } else {
                setLockedFluid(getDrainableStack().getFluid());
            }
        } else {
            setLockedFluid(null);
            fluidTank.drain(0, true);
        }
    }

    @Override
    public boolean isFluidLocked() {
        return this.mLockFluid;
    }

    @Override
    public boolean acceptsFluidLock(Fluid fluid) {
        if (fluid == null || getFluidAmount() == 0) return true;

        return mFluid != null && mFluid.getFluid()
            .equals(fluid);
    }

    public void resetFluidLockOnShiftBreak() {
        if (mLockFluid && getFluidAmount() == 0) {
            lockFluid(false);
        }
    }

    public boolean isOutputFluid() {
        return mOutputFluid;
    }

    public void setOutputFluid(boolean mOutputFluid) {
        this.mOutputFluid = mOutputFluid;
    }

    public boolean isVoidFluidPart() {
        return mVoidFluidPart;
    }

    public void setVoidFluidPart(boolean mVoidFluidPart) {
        this.mVoidFluidPart = mVoidFluidPart;
        fluidTank.setAllowOverflow(allowOverflow());
    }

    public boolean isVoidFluidFull() {
        return mVoidFluidFull;
    }

    public void setVoidFluidFull(boolean mVoidFluidFull) {
        this.mVoidFluidFull = mVoidFluidFull;
        fluidTank.setAllowOverflow(allowOverflow());

        // clear locked fluid and disable locking
        if (mVoidFluidFull) {
            lockedFluid = null;
            mLockFluid = false;
        }
    }

    public boolean isAllowInputFromOutputSide() {
        return mAllowInputFromOutputSide;
    }

    public void setAllowInputFromOutputSide(boolean mAllowInputFromOutputSide) {
        this.mAllowInputFromOutputSide = mAllowInputFromOutputSide;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection sideDirection,
        ForgeDirection facingDirection, int colorIndex, boolean active, boolean redstoneLevel) {
        if (sideDirection != ForgeDirection.UP) {
            ForgeDirection outputSide = baseMetaTileEntity == null ? facingDirection
                : baseMetaTileEntity.getFrontFacing();
            if (sideDirection == outputSide) {
                return new ITexture[] { MACHINE_CASINGS[mTier][colorIndex + 1], TextureFactory.of(OVERLAY_PIPE) };
            }
            boolean hasWindow = (sideDirection == ForgeDirection.NORTH || sideDirection == ForgeDirection.SOUTH
                || sideDirection == ForgeDirection.WEST
                || sideDirection == ForgeDirection.EAST)
                && (baseMetaTileEntity == null || !baseMetaTileEntity.hasCoverAtSide(sideDirection));
            if (hasWindow) {
                return new ITexture[] { MACHINE_CASINGS[mTier][colorIndex + 1],
                    TextureFactory.of(OVERLAY_SCREEN_GLASS) };
            }
            return new ITexture[] { MACHINE_CASINGS[mTier][colorIndex + 1] };
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
        if (side != getBaseMetaTileEntity().getFrontFacing()) return;
        if (aPlayer.isSneaking()) {
            mDisableFilter = !mDisableFilter;
            GTUtility.sendChatTrans(aPlayer, "GT5U.hatch.disableFilter." + mDisableFilter);
        } else {
            mAllowInputFromOutputSide = !mAllowInputFromOutputSide;
            GTUtility.sendChatTrans(
                aPlayer,
                mAllowInputFromOutputSide ? "gt.interact.desc.input_from_output_on"
                    : "gt.interact.desc.input_from_output_off");
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
        if (!aBaseMetaTileEntity.isServerSide()) return;

        if (isFluidChangingAllowed() && getFillableStack() != null && getFillableStack().amount <= 0) {
            setFillableStack(null);
        }

        if (mVoidFluidFull && getFillableStack() != null) {
            mVoidFluidPart = false;
            mLockFluid = false;
            setFillableStack(null);
        }

        if (doesEmptyContainers()) {
            FluidStack tFluid = GTUtility.getFluidForFilledItem(mInventory[getInputSlot()], true);
            if (tFluid != null && isFluidInputAllowed(tFluid)) {
                if (getFillableStack() == null) {
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
                } else if (tFluid.isFluidEqual(getFillableStack())) {
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
            if (this.mLockFluid && this.lockedFluid == null) {
                setLockedFluid(aFluid.getFluid());
            }
            getBaseMetaTileEntity().markDirty();
        }
        return (mVoidFluidPart || mVoidFluidFull) ? aFluid.amount : amount;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);

        if (!aBaseMetaTileEntity.isServerSide()) return;

        if (mOutputFluid && getDrainableStack() != null && (aTick % 20 == 0)) {
            IFluidHandler tTank = aBaseMetaTileEntity.getITankContainerAtSide(aBaseMetaTileEntity.getFrontFacing());
            if (tTank != null) {
                FluidStack tDrained = drain(20 * (1 << (3 + 2 * tierPump(mTier))), false);
                if (tDrained != null) {
                    int tFilledAmount = tTank.fill(aBaseMetaTileEntity.getBackFacing(), tDrained, false);
                    if (tFilledAmount > 0) {
                        tTank.fill(aBaseMetaTileEntity.getBackFacing(), drain(tFilledAmount, true), true);
                    }
                }
            }
        }

        updateRenderData(aTick);
    }

    private void updateRenderData(long aTick) {
        FluidStack fluid = getFluid();
        if (fluid == null || fluid.amount <= 0 || fluid.getFluid() == null) fluid = null;
        int amount = fluid == null ? 0 : fluid.amount;
        boolean identityChanged = lastRenderFluid == null ? fluid != null
            : fluid == null || !lastRenderFluid.isFluidEqual(fluid);
        boolean amountChanged = amount != lastRenderAmount;
        if (!identityChanged && !amountChanged) return;
        if (!identityChanged && lastRenderPacketTick != Long.MIN_VALUE
            && aTick - lastRenderPacketTick < RENDER_UPDATE_INTERVAL) return;

        lastRenderFluid = fluid == null ? null : fluid.copy();
        lastRenderAmount = amount;
        lastRenderPacketTick = aTick;
        sendRenderDataToClient(this);
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

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        if (!super.allowPutStack(aBaseMetaTileEntity, aIndex, side, aStack)) return false;
        if (mDisableFilter) return true;
        FluidStack tFluid = GTUtility.getFluidForFilledItem(aStack, true);
        return tFluid == null || isFluidInputAllowed(tFluid);
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

    public void setLockIfEmpty() {
        if (mLockFluid && lockedFluid == null) {
            FluidStack fluidStack = getFluid();
            if (fluidStack != null) setLockedFluid(fluidStack.getFluid());
        }
    }

    public ModularPanel buildUI(PosGuiData data, PanelSyncManager syncManager, UISettings uiSettings) {
        return new MTEDigitalTankBaseGui<>(this).build(data, syncManager, uiSettings);
    }
}
