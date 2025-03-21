package gregtech.common.covers;

import java.util.concurrent.atomic.AtomicBoolean;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;

import org.jetbrains.annotations.NotNull;

import com.google.common.io.ByteArrayDataInput;
import com.gtnewhorizons.modularui.api.NumberFormatMUI;
import com.gtnewhorizons.modularui.api.drawable.Text;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import gregtech.api.covers.CoverContext;
import gregtech.api.gui.modularui.CoverUIBuildContext;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IMachineProgress;
import gregtech.api.util.GTUtility;
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
public class CoverFluidRegulator extends CoverBehaviorBase {

    private static final int SPEED_LENGTH = 20;
    private static final int TICK_RATE_LENGTH = Integer.SIZE - SPEED_LENGTH - 1;
    private static final int TICK_RATE_MIN = 1;
    private static final int TICK_RATE_MAX = (-1 >>> (Integer.SIZE - TICK_RATE_LENGTH)) + TICK_RATE_MIN;
    private static final int TICK_RATE_BITMASK = (TICK_RATE_MAX - TICK_RATE_MIN) << SPEED_LENGTH;

    public final int mTransferRate;
    private boolean allowFluid = false;
    private int tickRate;
    private int speed;
    private Conditional condition;

    public CoverFluidRegulator(CoverContext context, int aTransferRate, ITexture coverTexture) {
        super(context, coverTexture);
        this.mTransferRate = aTransferRate;
        initializeData(context.getCoverInitializer());
    }

    public int getTransferRate() {
        return mTransferRate;
    }

    public int getTickRateForUi() {
        return tickRate;
    }

    public CoverFluidRegulator setTickRateForUi(int tickRate) {
        this.tickRate = tickRate;
        return this;
    }

    public int getSpeed() {
        return speed;
    }

    public CoverFluidRegulator setSpeed(int speed) {
        this.speed = speed;
        return this;
    }

    public Conditional getCondition() {
        return condition;
    }

    public CoverFluidRegulator setCondition(Conditional condition) {
        this.condition = condition;
        return this;
    }

    @Override
    protected void initializeData() {
        this.tickRate = TICK_RATE_MIN;
        this.speed = 0;
        this.condition = Conditional.Always;
    }

    @Override
    protected void loadFromNbt(NBTBase nbt) {
        if (!(nbt instanceof NBTTagCompound tag)) return; // not very good...
        speed = tag.getInteger("mSpeed");
        tickRate = tag.getInteger("mTickRate");
        condition = Conditional.VALUES[tag.getByte("mCondition")];
    }

    @Override
    protected void readFromPacket(ByteArrayDataInput byteData) {
        this.tickRate = byteData.readInt();
        this.speed = byteData.readInt();
        this.condition = Conditional.VALUES[byteData.readUnsignedByte()];
    }

    @Override
    protected @NotNull NBTBase saveDataToNbt() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger("mSpeed", speed);
        tag.setInteger("mTickRate", tickRate);
        tag.setByte("mCondition", (byte) condition.ordinal());
        return tag;
    }

    @Override
    protected void writeDataToByteBuf(ByteBuf byteBuf) {
        byteBuf.writeInt(tickRate)
            .writeInt(speed)
            .writeByte(condition.ordinal());
    }

    @Override
    public boolean isRedstoneSensitive(long aTimer) {
        return condition.isRedstoneSensitive();
    }

    @Override
    public void doCoverThings(byte aInputRedstone, long aTimer) {
        ICoverable coverable = coveredTile.get();
        if (coverable == null || speed == 0 || !condition.isAllowedToWork(coverSide, coverID, coverable)) {
            return;
        }
        if ((coverable instanceof IFluidHandler fluidHandler)) {
            final IFluidHandler tTank1;
            final IFluidHandler tTank2;
            final ForgeDirection directionFrom;
            if (speed > 0) {
                tTank2 = coverable.getITankContainerAtSide(coverSide);
                tTank1 = fluidHandler;
                directionFrom = coverSide;
            } else {
                tTank1 = coverable.getITankContainerAtSide(coverSide);
                tTank2 = fluidHandler;
                directionFrom = coverSide.getOpposite();
            }
            if (tTank1 != null && tTank2 != null) {
                allowFluid = true;
                GTUtility.moveFluid(tTank1, tTank2, directionFrom, Math.abs(speed), this::canTransferFluid);
                allowFluid = false;
            }
        }
    }

    private void adjustSpeed(EntityPlayer aPlayer, int scale) {
        int tSpeed = speed;
        tSpeed += scale;
        int tTickRate = tickRate;
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
    public void onCoverScrewdriverClick(EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (GTUtility.getClickedFacingCoords(coverSide, aX, aY, aZ)[0] >= 0.5F) {
            adjustSpeed(aPlayer, aPlayer.isSneaking() ? 256 : 16);
        } else {
            adjustSpeed(aPlayer, aPlayer.isSneaking() ? -256 : -16);
        }
    }

    protected boolean canTransferFluid(FluidStack fluid) {
        return true;
    }

    @Override
    public boolean letsRedstoneGoIn() {
        return true;
    }

    @Override
    public boolean letsRedstoneGoOut() {
        return true;
    }

    @Override
    public boolean letsEnergyIn() {
        return true;
    }

    @Override
    public boolean letsEnergyOut() {
        return true;
    }

    @Override
    public boolean letsItemsIn(int aSlot) {
        return true;
    }

    @Override
    public boolean letsItemsOut(int aSlot) {
        return true;
    }

    @Override
    public boolean letsFluidIn(Fluid aFluid) {
        return allowFluid;
    }

    @Override
    public boolean letsFluidOut(Fluid aFluid) {
        return allowFluid;
    }

    @Override
    public boolean alwaysLookConnected() {
        return true;
    }

    @Override
    public int getMinimumTickRate() {
        return tickRate;
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

    private static class FluidRegulatorUIFactory extends UIFactory<CoverFluidRegulator> {

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

        @Override
        protected CoverFluidRegulator adaptCover(Cover cover) {
            if (cover instanceof CoverFluidRegulator adapterCover) {
                return adapterCover;
            }
            return null;
        }

        @SuppressWarnings("PointlessArithmeticExpression")
        @Override
        protected void addUIWidgets(ModularWindow.Builder builder) {
            AtomicBoolean warn = new AtomicBoolean(false);

            builder.widget(
                new CoverDataControllerWidget<>(this::getCover, getUIBuildContext())
                    .addFollower(
                        CoverDataFollowerToggleButtonWidget.ofDisableable(),
                        coverData -> coverData.getSpeed() >= 0,
                        (coverData, state) -> coverData.setSpeed(Math.abs(coverData.getSpeed())),
                        widget -> widget.setStaticTexture(GTUITextures.OVERLAY_BUTTON_EXPORT)
                            .addTooltip(GTUtility.trans("006", "Export"))
                            .setPos(spaceX * 0, spaceY * 0))
                    .addFollower(
                        CoverDataFollowerToggleButtonWidget.ofDisableable(),
                        coverData -> coverData.getSpeed() <= 0,
                        (coverData, state) -> coverData.setSpeed(-Math.abs(coverData.getSpeed())),
                        widget -> widget.setStaticTexture(GTUITextures.OVERLAY_BUTTON_IMPORT)
                            .addTooltip(GTUtility.trans("007", "Import"))
                            .setPos(spaceX * 1, spaceY * 0))
                    .addFollower(
                        CoverDataFollowerToggleButtonWidget.ofDisableable(),
                        coverData -> coverData.getCondition() == Conditional.Always,
                        (coverData, state) -> coverData.setCondition(Conditional.Always),
                        widget -> widget.setStaticTexture(GTUITextures.OVERLAY_BUTTON_CHECKMARK)
                            .addTooltip(GTUtility.trans("224", "Always On"))
                            .setPos(spaceX * 0, spaceY * 1))
                    .addFollower(
                        CoverDataFollowerToggleButtonWidget.ofDisableable(),
                        coverData -> coverData.getCondition() == Conditional.Conditional,
                        (coverData, state) -> coverData.setCondition(Conditional.Conditional),
                        widget -> widget.setStaticTexture(GTUITextures.OVERLAY_BUTTON_USE_PROCESSING_STATE)
                            .addTooltip(GTUtility.trans("343", "Use Machine Processing State"))
                            .setPos(spaceX * 1, spaceY * 1))
                    .addFollower(
                        CoverDataFollowerToggleButtonWidget.ofDisableable(),
                        coverData -> coverData.getCondition() == Conditional.Inverted,
                        (coverData, state) -> coverData.setCondition(Conditional.Inverted),
                        widget -> widget.setStaticTexture(GTUITextures.OVERLAY_BUTTON_USE_INVERTED_PROCESSING_STATE)
                            .addTooltip(GTUtility.trans("343.1", "Use Inverted Machine Processing State"))
                            .setPos(spaceX * 2, spaceY * 1))
                    .addFollower(
                        new CoverDataFollowerNumericWidget<>(),
                        coverData -> (double) coverData.getSpeed(),
                        (coverData, state) -> coverData.setSpeed(state.intValue()),
                        widget -> widget.setBounds(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY)
                            .setValidator(val -> {
                                CoverFluidRegulator cover = getCover();
                                final int tickRate = cover != null ? cover.getTickRateForUi() : 0;
                                final long maxFlow = (cover != null ? (long) cover.getTransferRate() : 0)
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
                        coverData -> (double) coverData.getTickRateForUi(),
                        (coverData, state) -> coverData.setTickRateForUi(state.intValue()),
                        widget -> widget.setBounds(0, TICK_RATE_MAX)
                            .setValidator(val -> {
                                CoverFluidRegulator cover = getCover();
                                final int speed = cover != null ? cover.getSpeed() : 0;
                                final long transferRate = cover != null ? (long) cover.getTransferRate() : 0;
                                warn.set(false);
                                if (val > TICK_RATE_MAX) {
                                    val = (long) TICK_RATE_MAX;
                                    warn.set(true);
                                } else if (Math.abs(speed) > transferRate * val) {
                                    val = Math.min(TICK_RATE_MAX, (Math.abs(speed) + transferRate - 1) / transferRate);
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
                    CoverFluidRegulator cover = getCover();
                    if (cover == null) return new Text("");
                    int tickRate = cover.getTickRateForUi();
                    return new Text(
                        GTUtility.trans("210.1", "Average:") + " "
                            + numberFormat.format(tickRate == 0 ? 0 : cover.getSpeed() * 20d / tickRate)
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
            boolean isAllowedToWork(ForgeDirection coverSide, int coverID, ICoverable coverable) {
                return true;
            }
        },
        Conditional(true) {

            @Override
            boolean isAllowedToWork(ForgeDirection coverSide, int coverID, ICoverable coverable) {
                return !(coverable instanceof IMachineProgress) || ((IMachineProgress) coverable).isAllowedToWork();
            }
        },
        Inverted(true) {

            @Override
            boolean isAllowedToWork(ForgeDirection coverSide, int coverID, ICoverable coverable) {
                return !(coverable instanceof IMachineProgress) || !((IMachineProgress) coverable).isAllowedToWork();
            }
        };

        static final Conditional[] VALUES = values();
        private final boolean redstoneSensitive;

        Conditional(boolean redstoneSensitive) {
            this.redstoneSensitive = redstoneSensitive;
        }

        abstract boolean isAllowedToWork(ForgeDirection coverSide, int coverID, ICoverable coverable);

        boolean isRedstoneSensitive() {
            return redstoneSensitive;
        }
    }
}
