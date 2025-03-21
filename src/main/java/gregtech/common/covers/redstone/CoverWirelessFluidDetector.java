package gregtech.common.covers.redstone;

import java.util.Arrays;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

import org.jetbrains.annotations.NotNull;

import com.google.common.io.ByteArrayDataInput;
import com.gtnewhorizons.modularui.api.math.Alignment;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import gregtech.api.covers.CoverContext;
import gregtech.api.gui.modularui.CoverUIBuildContext;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.util.GTUtility;
import gregtech.common.covers.Cover;
import gregtech.common.covers.CoverLiquidMeter;
import gregtech.common.gui.modularui.widget.CoverDataControllerWidget;
import gregtech.common.gui.modularui.widget.CoverDataFollowerNumericWidget;
import gregtech.common.gui.modularui.widget.CoverDataFollowerToggleButtonWidget;
import io.netty.buffer.ByteBuf;

public class CoverWirelessFluidDetector extends CoverAdvancedRedstoneTransmitterBase {

    /** The special value {@code 0} means threshold check is disabled. */
    private int threshold;
    /** Whether the wireless detector cover also sets the tiles sided Redstone output */
    private boolean physical;

    public CoverWirelessFluidDetector(CoverContext context, ITexture coverTexture) {
        super(context, coverTexture);
    }

    public int getThreshold() {
        return threshold;
    }

    public CoverWirelessFluidDetector setThreshold(int threshold) {
        this.threshold = threshold;
        return this;
    }

    public boolean isPhysical() {
        return physical;
    }

    public CoverWirelessFluidDetector setPhysical(boolean physical) {
        this.physical = physical;
        return this;
    }

    @Override
    protected void initializeData() {
        super.initializeData();
        this.threshold = 0;
        this.physical = true;
    }

    @Override
    protected void loadFromNbt(NBTBase nbt) {
        super.loadFromNbt(nbt);

        NBTTagCompound tag = (NBTTagCompound) nbt;
        threshold = tag.getInteger("threshold");
        if (tag.hasKey("physical")) {
            physical = tag.getBoolean("physical");
        } else {
            physical = false;
        }
    }

    @Override
    protected void readFromPacket(ByteArrayDataInput byteData) {
        super.readFromPacket(byteData);
        threshold = byteData.readInt();
        physical = byteData.readBoolean();
    }

    @Override
    protected @NotNull NBTBase saveDataToNbt() {
        NBTTagCompound tag = (NBTTagCompound) super.saveDataToNbt();
        tag.setInteger("threshold", threshold);
        tag.setBoolean("physical", physical);

        return tag;
    }

    @Override
    protected void writeDataToByteBuf(ByteBuf byteBuf) {
        super.writeToByteBuf(byteBuf);
        byteBuf.writeInt(threshold);
        byteBuf.writeBoolean(physical);
    }

    @Override
    public void doCoverThings(byte aInputRedstone, long aTimer) {
        ICoverable coverable = coveredTile.get();
        if (coverable == null) {
            return;
        }
        final byte signal = CoverLiquidMeter.computeSignalBasedOnFluid(coverable, invert, threshold);
        final long hash = hashCoverCoords(coverable, coverSide);
        setSignalAt(getUuid(), getFrequency(), hash, signal);

        if (physical) {
            coverable.setOutputRedstoneSignal(coverSide, signal);
        } else {
            coverable.setOutputRedstoneSignal(coverSide, (byte) 0);
        }
    }

    @Override
    public boolean letsRedstoneGoOut() {
        return true;
    }

    @Override
    public boolean manipulatesSidedRedstoneOutput() {
        return true;
    }

    // GUI stuff

    @Override
    public ModularWindow createWindow(CoverUIBuildContext buildContext) {
        return new WirelessFluidDetectorUIFactory(buildContext).createWindow();
    }

    private static class WirelessFluidDetectorUIFactory
        extends AdvancedRedstoneTransmitterBaseUIFactory<CoverWirelessFluidDetector> {

        private int maxCapacity;

        public WirelessFluidDetectorUIFactory(CoverUIBuildContext buildContext) {
            super(buildContext);
        }

        @Override
        protected int getGUIHeight() {
            return 123;
        }

        @Override
        protected CoverWirelessFluidDetector adaptCover(Cover cover) {
            if (cover instanceof CoverWirelessFluidDetector adaptedCover) {
                return adaptedCover;
            }
            return null;
        }

        @Override
        protected @NotNull CoverDataControllerWidget<CoverWirelessFluidDetector> getDataController() {
            return new CoverDataControllerWidget<>(this::getCover, getUIBuildContext());
        }

        @Override
        protected void addUIWidgets(ModularWindow.Builder builder) {
            setMaxCapacity();
            super.addUIWidgets(builder);
            builder
                .widget(
                    new TextWidget(GTUtility.trans("222", "Fluid threshold")).setDefaultColor(COLOR_TEXT_GRAY.get())
                        .setPos(startX + spaceX * 5, 4 + startY + spaceY * 2))
                .widget(
                    TextWidget
                        .dynamicString(
                            getCoverString(
                                c -> c.isPhysical()
                                    ? StatCollector.translateToLocal("gt.cover.wirelessdetector.redstone.1")
                                    : StatCollector.translateToLocal("gt.cover.wirelessdetector.redstone.0")))
                        .setSynced(false)
                        .setDefaultColor(COLOR_TEXT_GRAY.get())
                        .setTextAlignment(Alignment.CenterLeft)
                        .setPos(startX + spaceX, 4 + startY + spaceY * 3)
                        .setSize(spaceX * 10, 12));
        }

        @Override
        protected void addUIForDataController(CoverDataControllerWidget<CoverWirelessFluidDetector> controller) {
            super.addUIForDataController(controller);
            controller
                .addFollower(
                    new CoverDataFollowerNumericWidget<>(),
                    coverData -> (double) coverData.getThreshold(),
                    (coverData, state) -> coverData.setThreshold(state.intValue()),
                    widget -> widget.setBounds(0, maxCapacity > 0 ? maxCapacity : Integer.MAX_VALUE)
                        .setScrollValues(1000, 144, 100000)
                        .setFocusOnGuiOpen(true)
                        .setPos(1, 2 + spaceY * 2)
                        .setSize(spaceX * 5 - 4, 12))
                .addFollower(
                    CoverDataFollowerToggleButtonWidget.ofDisableable(),
                    CoverWirelessFluidDetector::isPhysical,
                    CoverWirelessFluidDetector::setPhysical,
                    widget -> widget
                        .addTooltip(StatCollector.translateToLocal("gt.cover.wirelessdetector.redstone.tooltip"))
                        .setPos(0, 1 + spaceY * 3));
        }

        private void setMaxCapacity() {
            final ICoverable tile = getUIBuildContext().getTile();
            if (!tile.isDead() && tile instanceof IFluidHandler) {
                FluidTankInfo[] tanks = ((IFluidHandler) tile).getTankInfo(ForgeDirection.UNKNOWN);
                maxCapacity = Arrays.stream(tanks)
                    .mapToInt(tank -> tank.capacity)
                    .sum();
            } else {
                maxCapacity = -1;
            }
        }
    }
}
