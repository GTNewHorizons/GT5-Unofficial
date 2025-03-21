package gregtech.common.covers.redstone;

import java.text.FieldPosition;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StatCollector;

import org.jetbrains.annotations.NotNull;

import com.google.common.io.ByteArrayDataInput;
import com.gtnewhorizons.modularui.api.NumberFormatMUI;
import com.gtnewhorizons.modularui.api.math.Alignment;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import gregtech.api.covers.CoverContext;
import gregtech.api.gui.modularui.CoverUIBuildContext;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GTUtility;
import gregtech.common.covers.Cover;
import gregtech.common.covers.CoverItemMeter;
import gregtech.common.gui.modularui.widget.CoverDataControllerWidget;
import gregtech.common.gui.modularui.widget.CoverDataFollowerNumericWidget;
import gregtech.common.gui.modularui.widget.CoverDataFollowerToggleButtonWidget;
import gregtech.common.gui.modularui.widget.ItemWatcherSlotWidget;
import gregtech.common.tileentities.storage.MTEDigitalChestBase;
import io.netty.buffer.ByteBuf;

public class CoverWirelessItemDetector extends CoverAdvancedRedstoneTransmitterBase {

    /**
     * The special value {@code -1} means all slots.
     */
    private int slot;
    /**
     * The special value {@code 0} means threshold check is disabled.
     */
    private int threshold;
    /** Whether the wireless detector cover also sets the tiles sided Redstone output */
    private boolean physical;

    public CoverWirelessItemDetector(CoverContext context, ITexture coverTexture) {
        super(context, coverTexture);
    }

    public int getSlot() {
        return this.slot;
    }

    public CoverWirelessItemDetector setSlot(int slot) {
        this.slot = slot;
        return this;
    }

    public int getThreshold() {
        return this.threshold;
    }

    public CoverWirelessItemDetector setThresdhold(int threshold) {
        this.threshold = threshold;
        return this;
    }

    public boolean isPhysical() {
        return physical;
    }

    public CoverWirelessItemDetector setPhysical(boolean physical) {
        this.physical = physical;
        return this;
    }

    @Override
    protected void initializeData() {
        super.initializeData();
        this.threshold = 0;
        this.slot = -1;
        this.physical = true;
    }

    @Override
    protected void loadFromNbt(NBTBase nbt) {
        super.loadFromNbt(nbt);

        NBTTagCompound tag = (NBTTagCompound) nbt;
        threshold = tag.getInteger("threshold");
        slot = tag.getInteger("slot");

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
        slot = byteData.readInt();
        physical = byteData.readBoolean();
    }

    @Override
    protected @NotNull NBTBase saveDataToNbt() {
        NBTTagCompound tag = (NBTTagCompound) super.saveDataToNbt();
        tag.setInteger("threshold", threshold);
        tag.setInteger("slot", slot);
        tag.setBoolean("physical", physical);

        return tag;
    }

    @Override
    protected void writeDataToByteBuf(ByteBuf byteBuf) {
        super.writeToByteBuf(byteBuf);
        byteBuf.writeInt(threshold);
        byteBuf.writeInt(slot);
        byteBuf.writeBoolean(physical);
    }

    @Override
    public void doCoverThings(byte aInputRedstone, long aTimer) {
        ICoverable coverable = coveredTile.get();
        if (coverable == null) {
            return;
        }
        byte signal = CoverItemMeter.computeSignalBasedOnItems(coverable, invert, threshold, slot, coverSide.ordinal());
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
        return new WirelessItemDetectorUIFactory(buildContext).createWindow();
    }

    private static class WirelessItemDetectorUIFactory
        extends AdvancedRedstoneTransmitterBaseUIFactory<CoverWirelessItemDetector> {

        private int maxSlot;
        private int maxThreshold;
        /**
         * Display the text "All" instead of a number when the slot is set to -1.
         */
        private static final NumberFormatMUI numberFormatAll = new NumberFormatMUI() {

            @Override
            public StringBuffer format(double number, StringBuffer toAppendTo, FieldPosition pos) {
                if (number < 0) {
                    return toAppendTo.append(GTUtility.trans("ALL", "All"));
                } else {
                    return super.format(number, toAppendTo, pos);
                }
            }
        };

        public WirelessItemDetectorUIFactory(CoverUIBuildContext buildContext) {
            super(buildContext);
        }

        @Override
        protected int getGUIHeight() {
            return 143;
        }

        @Override
        protected CoverWirelessItemDetector adaptCover(Cover cover) {
            if (cover instanceof CoverWirelessItemDetector adaptedCover) {
                return adaptedCover;
            }
            return null;
        }

        @Override
        protected @NotNull CoverDataControllerWidget<CoverWirelessItemDetector> getDataController() {
            return new CoverDataControllerWidget<>(this::getCover, getUIBuildContext());
        }

        @Override
        protected void addUIWidgets(ModularWindow.Builder builder) {
            setMaxSlot();
            setMaxThreshold();
            super.addUIWidgets(builder);
            builder.widget(
                new ItemWatcherSlotWidget().setGetter(this::getTargetItem)
                    .setPos(startX + spaceX * 4 - 1, startY + spaceY * 3))
                .widget(
                    new TextWidget(GTUtility.trans("221", "Item threshold")).setDefaultColor(COLOR_TEXT_GRAY.get())
                        .setPos(startX + spaceX * 5, 4 + startY + spaceY * 2))
                .widget(
                    new TextWidget(GTUtility.trans("254", "Detect Slot #")).setDefaultColor(COLOR_TEXT_GRAY.get())
                        .setPos(startX + spaceX * 5, 4 + startY + spaceY * 3))
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
                        .setPos(startX + spaceX, 5 + startY + spaceY * 4)
                        .setSize(spaceX * 10, 12));
        }

        @Override
        protected void addUIForDataController(CoverDataControllerWidget<CoverWirelessItemDetector> controller) {
            super.addUIForDataController(controller);
            controller
                .addFollower(
                    new CoverDataFollowerNumericWidget<>(),
                    coverData -> (double) coverData.getThreshold(),
                    (coverData, state) -> coverData.setThresdhold(state.intValue()),
                    widget -> widget.setBounds(0, maxThreshold)
                        .setScrollValues(1, 64, 1000)
                        .setFocusOnGuiOpen(true)
                        .setPos(1, 2 + spaceY * 2)
                        .setSize(spaceX * 5 - 4, 12))
                .addFollower(
                    new CoverDataFollowerNumericWidget<>(),
                    coverData -> (double) coverData.getSlot(),
                    (coverData, state) -> coverData.setSlot(state.intValue()),
                    widget -> widget.setBounds(-1, maxSlot)
                        .setDefaultValue(-1)
                        .setScrollValues(1, 100, 10)
                        .setNumberFormat(numberFormatAll)
                        .setPos(1, 2 + spaceY * 3)
                        .setSize(spaceX * 4 - 8, 12))
                .addFollower(
                    CoverDataFollowerToggleButtonWidget.ofDisableable(),
                    CoverWirelessItemDetector::isPhysical,
                    CoverWirelessItemDetector::setPhysical,
                    widget -> widget
                        .addTooltip(StatCollector.translateToLocal("gt.cover.wirelessdetector.redstone.tooltip"))
                        .setPos(0, 2 + spaceY * 4));
        }

        private void setMaxSlot() {
            final ICoverable tile = getUIBuildContext().getTile();
            if (!tile.isDead() && tile instanceof IGregTechTileEntity gtTile
                && !(gtTile.getMetaTileEntity() instanceof MTEDigitalChestBase)) {
                maxSlot = tile.getSizeInventory() - 1;
            } else {
                maxSlot = -1;
            }
        }

        private void setMaxThreshold() {
            final ICoverable tile = getUIBuildContext().getTile();
            if (!tile.isDead() && tile instanceof IGregTechTileEntity gtTile
                && gtTile.getMetaTileEntity() instanceof MTEDigitalChestBase) {
                maxThreshold = gtTile.getMaxItemCount();
            } else {
                maxThreshold = maxSlot > 0 ? maxSlot * 64 : Integer.MAX_VALUE;
            }
        }

        private ItemStack getTargetItem() {
            final ICoverable tile = getUIBuildContext().getTile();
            final CoverWirelessItemDetector cover = getCover();
            if (cover != null && cover.getSlot() >= 0
                && tile instanceof TileEntity
                && !tile.isDead()
                && tile.getSizeInventory() >= cover.getSlot()) {
                return tile.getStackInSlot(cover.getSlot());
            } else {
                return null;
            }
        }
    }
}
