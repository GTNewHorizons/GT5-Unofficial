package gregtech.common.covers;

import java.util.concurrent.atomic.AtomicBoolean;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;

import com.google.common.io.ByteArrayDataInput;
import com.gtnewhorizons.modularui.api.NumberFormatMUI;
import com.gtnewhorizons.modularui.api.drawable.Text;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import gregtech.api.gui.modularui.CoverUIBuildContext;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IMachineProgress;
import gregtech.api.util.CoverBehaviorBase;
import gregtech.api.util.GTUtility;
import gregtech.api.util.ISerializableObject;
import gregtech.common.gui.modularui.widget.CoverDataControllerWidget;
import gregtech.common.gui.modularui.widget.CoverDataFollowerNumericWidget;
import gregtech.common.gui.modularui.widget.CoverDataFollowerToggleButtonWidget;
import io.netty.buffer.ByteBuf;

/**
 * Cover variable
 *
 * <pre>
 * 1111 1111 1111 1111 1111 1111 1111 1111
 *  |- interval-| |- flow rate 2 compl. -|
 * ^ export?
 * </pre>
 *
 * Concat export and flow rate 2 compl. together to get actual flow rate. A positive actual flow rate is export, and
 * vice versa.
 * <p>
 * Interval is an unsigned 11 bit integer minus 1, so the range is 1~2048. The stored bits will be flipped bitwise if
 * speed is negative. This way, `0` means 1tick interval, while `-1` means 1 tick interval as well, preserving the
 * legacy behavior.
 */
public class CoverFluidRegulator extends CoverBehaviorBase<CoverFluidRegulator.FluidRegulatorData> {

    private static final int SPEED_LENGTH = 20;
    private static final int TICK_RATE_LENGTH = Integer.SIZE - SPEED_LENGTH - 1;
    private static final int TICK_RATE_MIN = 1;
    private static final int TICK_RATE_MAX = (-1 >>> (Integer.SIZE - TICK_RATE_LENGTH)) + TICK_RATE_MIN;
    private static final int TICK_RATE_BITMASK = (TICK_RATE_MAX - TICK_RATE_MIN) << SPEED_LENGTH;

    public final int mTransferRate;
    private boolean allowFluid = false;

    public CoverFluidRegulator(int aTransferRate, ITexture coverTexture) {
        super(FluidRegulatorData.class, coverTexture);
        if (aTransferRate > (-1 >>> (Integer.SIZE - SPEED_LENGTH)))
            throw new IllegalArgumentException("aTransferRate too big: " + aTransferRate);
        this.mTransferRate = aTransferRate;
    }

    @Override
    public FluidRegulatorData createDataObject(int aLegacyData) {
        return new FluidRegulatorData(aLegacyData);
    }

    @Override
    public FluidRegulatorData createDataObject() {
        return new FluidRegulatorData();
    }

    @Override
    protected boolean isRedstoneSensitiveImpl(ForgeDirection side, int aCoverID, FluidRegulatorData aCoverVariable,
        ICoverable aTileEntity, long aTimer) {
        return aCoverVariable.condition.isRedstoneSensitive();
    }

    @Override
    protected FluidRegulatorData doCoverThingsImpl(ForgeDirection side, byte aInputRedstone, int aCoverID,
        FluidRegulatorData aCoverVariable, ICoverable aTileEntity, long aTimer) {
        if (aCoverVariable.speed == 0 || !aCoverVariable.condition.isAllowedToWork(side, aCoverID, aTileEntity)) {
            return aCoverVariable;
        }
        if ((aTileEntity instanceof IFluidHandler)) {
            final IFluidHandler tTank1;
            final IFluidHandler tTank2;
            final ForgeDirection directionFrom;
            final ForgeDirection directionTo;
            if (aCoverVariable.speed > 0) {
                tTank2 = aTileEntity.getITankContainerAtSide(side);
                tTank1 = (IFluidHandler) aTileEntity;
                directionFrom = side;
                directionTo = side.getOpposite();
            } else {
                tTank1 = aTileEntity.getITankContainerAtSide(side);
                tTank2 = (IFluidHandler) aTileEntity;
                directionFrom = side.getOpposite();
                directionTo = side;
            }
            if (tTank1 != null && tTank2 != null) {
                allowFluid = true;
                GTUtility
                    .moveFluid(tTank1, tTank2, directionFrom, Math.abs(aCoverVariable.speed), this::canTransferFluid);
                allowFluid = false;
            }
        }
        return aCoverVariable;
    }

    private void adjustSpeed(EntityPlayer aPlayer, FluidRegulatorData aCoverVariable, int scale) {
        int tSpeed = aCoverVariable.speed;
        tSpeed += scale;
        int tTickRate = aCoverVariable.tickRate;
        if (Math.abs(tSpeed) > mTransferRate * tTickRate) {
            tSpeed = mTransferRate * tTickRate * (tSpeed > 0 ? 1 : -1);
            GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("316", "Pump speed limit reached!"));
        }
        if (tTickRate == 1) {
            GTUtility.sendChatToPlayer(
                aPlayer,
                GTUtility.trans("048", "Pump speed: ") + tSpeed
                    + GTUtility.trans("049", "L/tick ")
                    + tSpeed * 20
                    + GTUtility.trans("050", "L/sec"));
        } else {
            GTUtility.sendChatToPlayer(
                aPlayer,
                String.format(
                    GTUtility.trans("207", "Pump speed: %dL every %d ticks, %.2f L/sec on average"),
                    tSpeed,
                    tTickRate,
                    tSpeed * 20d / tTickRate));
        }
    }

    @Override
    public FluidRegulatorData onCoverScrewdriverClickImpl(ForgeDirection side, int aCoverID,
        FluidRegulatorData aCoverVariable, ICoverable aTileEntity, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (GTUtility.getClickedFacingCoords(side, aX, aY, aZ)[0] >= 0.5F) {
            adjustSpeed(aPlayer, aCoverVariable, aPlayer.isSneaking() ? 256 : 16);
        } else {
            adjustSpeed(aPlayer, aCoverVariable, aPlayer.isSneaking() ? -256 : -16);
        }
        return aCoverVariable;
    }

    protected boolean canTransferFluid(FluidStack fluid) {
        return true;
    }

    @Override
    public boolean letsRedstoneGoInImpl(ForgeDirection side, int aCoverID, FluidRegulatorData aCoverVariable,
        ICoverable aTileEntity) {
        return true;
    }

    @Override
    public boolean letsRedstoneGoOutImpl(ForgeDirection side, int aCoverID, FluidRegulatorData aCoverVariable,
        ICoverable aTileEntity) {
        return true;
    }

    @Override
    public boolean letsEnergyInImpl(ForgeDirection side, int aCoverID, FluidRegulatorData aCoverVariable,
        ICoverable aTileEntity) {
        return true;
    }

    @Override
    public boolean letsEnergyOutImpl(ForgeDirection side, int aCoverID, FluidRegulatorData aCoverVariable,
        ICoverable aTileEntity) {
        return true;
    }

    @Override
    public boolean letsItemsInImpl(ForgeDirection side, int aCoverID, FluidRegulatorData aCoverVariable, int aSlot,
        ICoverable aTileEntity) {
        return true;
    }

    @Override
    public boolean letsItemsOutImpl(ForgeDirection side, int aCoverID, FluidRegulatorData aCoverVariable, int aSlot,
        ICoverable aTileEntity) {
        return true;
    }

    @Override
    public boolean letsFluidInImpl(ForgeDirection side, int aCoverID, FluidRegulatorData aCoverVariable, Fluid aFluid,
        ICoverable aTileEntity) {
        return allowFluid;
    }

    @Override
    public boolean letsFluidOutImpl(ForgeDirection side, int aCoverID, FluidRegulatorData aCoverVariable, Fluid aFluid,
        ICoverable aTileEntity) {
        return allowFluid;
    }

    @Override
    protected boolean alwaysLookConnectedImpl(ForgeDirection side, int aCoverID, FluidRegulatorData aCoverVariable,
        ICoverable aTileEntity) {
        return true;
    }

    @Override
    protected int getTickRateImpl(ForgeDirection side, int aCoverID, FluidRegulatorData aCoverVariable,
        ICoverable aTileEntity) {
        return aCoverVariable.tickRate;
    }

    // GUI stuff

    @Override
    public boolean hasCoverGUI() {
        return true;
    }

    @Override
    public ModularWindow createWindow(CoverUIBuildContext buildContext) {
        return new FluidRegulatorUIFactory(buildContext).createWindow();
    }

    private class FluidRegulatorUIFactory extends UIFactory {

        private static final int startX = 10;
        private static final int startY = 25;
        private static final int spaceX = 18;
        private static final int spaceY = 18;

        private static final NumberFormatMUI numberFormat;
        static {
            numberFormat = new NumberFormatMUI();
            numberFormat.setMaximumFractionDigits(2);
        }

        public FluidRegulatorUIFactory(CoverUIBuildContext buildContext) {
            super(buildContext);
        }

        @SuppressWarnings("PointlessArithmeticExpression")
        @Override
        protected void addUIWidgets(ModularWindow.Builder builder) {
            AtomicBoolean warn = new AtomicBoolean(false);

            builder.widget(
                new CoverDataControllerWidget<>(this::getCoverData, this::setCoverData, CoverFluidRegulator.this)
                    .addFollower(
                        CoverDataFollowerToggleButtonWidget.ofDisableable(),
                        coverData -> coverData.speed >= 0,
                        (coverData, state) -> {
                            coverData.speed = Math.abs(coverData.speed);
                            return coverData;
                        },
                        widget -> widget.setStaticTexture(GTUITextures.OVERLAY_BUTTON_EXPORT)
                            .addTooltip(GTUtility.trans("006", "Export"))
                            .setPos(spaceX * 0, spaceY * 0))
                    .addFollower(
                        CoverDataFollowerToggleButtonWidget.ofDisableable(),
                        coverData -> coverData.speed <= 0,
                        (coverData, state) -> {
                            coverData.speed = -Math.abs(coverData.speed);
                            return coverData;
                        },
                        widget -> widget.setStaticTexture(GTUITextures.OVERLAY_BUTTON_IMPORT)
                            .addTooltip(GTUtility.trans("007", "Import"))
                            .setPos(spaceX * 1, spaceY * 0))
                    .addFollower(
                        CoverDataFollowerToggleButtonWidget.ofDisableable(),
                        coverData -> coverData.condition == Conditional.Always,
                        (coverData, state) -> {
                            coverData.condition = Conditional.Always;
                            return coverData;
                        },
                        widget -> widget.setStaticTexture(GTUITextures.OVERLAY_BUTTON_CHECKMARK)
                            .addTooltip(GTUtility.trans("224", "Always On"))
                            .setPos(spaceX * 0, spaceY * 1))
                    .addFollower(
                        CoverDataFollowerToggleButtonWidget.ofDisableable(),
                        coverData -> coverData.condition == Conditional.Conditional,
                        (coverData, state) -> {
                            coverData.condition = Conditional.Conditional;
                            return coverData;
                        },
                        widget -> widget.setStaticTexture(GTUITextures.OVERLAY_BUTTON_USE_PROCESSING_STATE)
                            .addTooltip(GTUtility.trans("343", "Use Machine Processing State"))
                            .setPos(spaceX * 1, spaceY * 1))
                    .addFollower(
                        CoverDataFollowerToggleButtonWidget.ofDisableable(),
                        coverData -> coverData.condition == Conditional.Inverted,
                        (coverData, state) -> {
                            coverData.condition = Conditional.Inverted;
                            return coverData;
                        },
                        widget -> widget.setStaticTexture(GTUITextures.OVERLAY_BUTTON_USE_INVERTED_PROCESSING_STATE)
                            .addTooltip(GTUtility.trans("343.1", "Use Inverted Machine Processing State"))
                            .setPos(spaceX * 2, spaceY * 1))
                    .addFollower(
                        new CoverDataFollowerNumericWidget<>(),
                        coverData -> (double) coverData.speed,
                        (coverData, state) -> {
                            coverData.speed = state.intValue();
                            return coverData;
                        },
                        widget -> widget.setBounds(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY)
                            .setValidator(val -> {
                                final int tickRate = getCoverData() != null ? getCoverData().tickRate : 0;
                                final long maxFlow = (long) mTransferRate
                                    * GTUtility.clamp(tickRate, TICK_RATE_MIN, TICK_RATE_MAX);
                                warn.set(false);
                                if (val > maxFlow) {
                                    val = maxFlow;
                                    warn.set(true);
                                } else if (val < -maxFlow) {
                                    val = -maxFlow;
                                    warn.set(true);
                                }
                                return val;
                            })
                            .setScrollValues(1, 144, 1000)
                            .setFocusOnGuiOpen(true)
                            .setPos(spaceX * 0, spaceY * 2 + 2)
                            .setSize(spaceX * 4 - 3, 12))
                    .addFollower(
                        new CoverDataFollowerNumericWidget<>(),
                        coverData -> (double) coverData.tickRate,
                        (coverData, state) -> {
                            coverData.tickRate = state.intValue();
                            return coverData;
                        },
                        widget -> widget.setBounds(0, TICK_RATE_MAX)
                            .setValidator(val -> {
                                final int speed = getCoverData() != null ? getCoverData().speed : 0;
                                warn.set(false);
                                if (val > TICK_RATE_MAX) {
                                    val = (long) TICK_RATE_MAX;
                                    warn.set(true);
                                } else if (Math.abs(speed) > mTransferRate * val) {
                                    val = (long) Math
                                        .min(TICK_RATE_MAX, (Math.abs(speed) + mTransferRate - 1) / mTransferRate);
                                    warn.set(true);
                                } else if (val < TICK_RATE_MIN) {
                                    val = 1L;
                                }
                                return val;
                            })
                            .setPos(spaceX * 5, spaceY * 2 + 2)
                            .setSize(spaceX * 2 - 3, 12))
                    .setPos(startX, startY))
                .widget(
                    new TextWidget(GTUtility.trans("229", "Export/Import")).setDefaultColor(COLOR_TEXT_GRAY.get())
                        .setPos(3 + startX + spaceX * 4, 4 + startY + spaceY * 0))
                .widget(
                    new TextWidget(GTUtility.trans("230", "Conditional")).setDefaultColor(COLOR_TEXT_GRAY.get())
                        .setPos(3 + startX + spaceX * 4, 4 + startY + spaceY * 1))
                .widget(
                    new TextWidget(GTUtility.trans("208", " L")).setDefaultColor(COLOR_TEXT_GRAY.get())
                        .setPos(startX + spaceX * 4, 4 + startY + spaceY * 2))
                .widget(
                    new TextWidget(GTUtility.trans("209", " ticks")).setDefaultColor(COLOR_TEXT_GRAY.get())
                        .setPos(startX + spaceX * 7, 4 + startY + spaceY * 2))
                .widget(new TextWidget().setTextSupplier(() -> {
                    FluidRegulatorData coverVariable = getCoverData();
                    if (coverVariable == null) return new Text("");
                    return new Text(
                        GTUtility.trans("210.1", "Average:") + " "
                            + numberFormat.format(
                                coverVariable.tickRate == 0 ? 0 : coverVariable.speed * 20d / coverVariable.tickRate)
                            + " "
                            + GTUtility.trans("210.2", "L/sec"))
                                .color(warn.get() ? COLOR_TEXT_WARN.get() : COLOR_TEXT_GRAY.get());
                })
                    .setPos(startX + spaceX * 0, 4 + startY + spaceY * 3));
        }
    }

    @Override
    public boolean allowsTickRateAddition() {
        return false;
    }

    public enum Conditional {

        Always(false) {

            @Override
            boolean isAllowedToWork(ForgeDirection side, int aCoverID, ICoverable aTileEntity) {
                return true;
            }
        },
        Conditional(true) {

            @Override
            boolean isAllowedToWork(ForgeDirection side, int aCoverID, ICoverable aTileEntity) {
                return !(aTileEntity instanceof IMachineProgress) || ((IMachineProgress) aTileEntity).isAllowedToWork();
            }
        },
        Inverted(true) {

            @Override
            boolean isAllowedToWork(ForgeDirection side, int aCoverID, ICoverable aTileEntity) {
                return !(aTileEntity instanceof IMachineProgress)
                    || !((IMachineProgress) aTileEntity).isAllowedToWork();
            }
        };

        static final Conditional[] VALUES = values();
        private final boolean redstoneSensitive;

        Conditional(boolean redstoneSensitive) {
            this.redstoneSensitive = redstoneSensitive;
        }

        abstract boolean isAllowedToWork(ForgeDirection side, int aCoverID, ICoverable aTileEntity);

        boolean isRedstoneSensitive() {
            return redstoneSensitive;
        }
    }

    public static class FluidRegulatorData implements ISerializableObject {

        private int tickRate;
        private int speed;
        private Conditional condition;

        private static int getSpeed(int aCoverVariable) {
            // positive or 0 -> interval bits need to be set to zero
            // negative -> interval bits need to be set to one
            return aCoverVariable >= 0 ? aCoverVariable & ~TICK_RATE_BITMASK : aCoverVariable | TICK_RATE_BITMASK;
        }

        private static int getTickRate(int aCoverVariable) {
            // range: TICK_RATE_MIN ~ TICK_RATE_MAX
            return ((Math.abs(aCoverVariable) & TICK_RATE_BITMASK) >>> SPEED_LENGTH) + TICK_RATE_MIN;
        }

        public FluidRegulatorData() {
            this(0);
        }

        public FluidRegulatorData(int legacy) {
            this(getTickRate(legacy), getSpeed(legacy), Conditional.Always);
        }

        public FluidRegulatorData(int tickRate, int speed, Conditional condition) {
            this.tickRate = tickRate;
            this.speed = speed;
            this.condition = condition;
        }

        @Nonnull
        @Override
        public ISerializableObject copy() {
            return new FluidRegulatorData(tickRate, speed, condition);
        }

        @Nonnull
        @Override
        public NBTBase saveDataToNBT() {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setInteger("mSpeed", speed);
            tag.setInteger("mTickRate", tickRate);
            tag.setByte("mCondition", (byte) condition.ordinal());
            return tag;
        }

        @Override
        public void writeToByteBuf(ByteBuf aBuf) {
            aBuf.writeInt(tickRate)
                .writeInt(speed)
                .writeByte(condition.ordinal());
        }

        @Override
        public void loadDataFromNBT(NBTBase aNBT) {
            if (!(aNBT instanceof NBTTagCompound tag)) return; // not very good...
            speed = tag.getInteger("mSpeed");
            tickRate = tag.getInteger("mTickRate");
            condition = Conditional.VALUES[tag.getByte("mCondition")];
        }

        @Nonnull
        @Override
        public ISerializableObject readFromPacket(ByteArrayDataInput aBuf, @Nullable EntityPlayerMP aPlayer) {
            return new FluidRegulatorData(aBuf.readInt(), aBuf.readInt(), Conditional.VALUES[aBuf.readUnsignedByte()]);
        }

        public int getTickRate() {
            return tickRate;
        }

        public void setTickRate(int tickRate) {
            this.tickRate = tickRate;
        }

        public int getSpeed() {
            return speed;
        }

        public void setSpeed(int speed) {
            this.speed = speed;
        }

        public Conditional getCondition() {
            return condition;
        }

        public void setCondition(Conditional condition) {
            this.condition = condition;
        }
    }
}
