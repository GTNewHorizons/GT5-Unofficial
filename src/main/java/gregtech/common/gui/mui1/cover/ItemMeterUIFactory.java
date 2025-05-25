package gregtech.common.gui.mui1.cover;

import static net.minecraft.util.StatCollector.translateToLocal;

import java.text.FieldPosition;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

import com.gtnewhorizons.modularui.api.NumberFormatMUI;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import gregtech.api.gui.modularui.CoverUIBuildContext;
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

public class ItemMeterUIFactory extends CoverUIFactory<CoverItemMeter> {

    private static final int startX = 10;
    private static final int startY = 25;
    private static final int spaceX = 18;
    private static final int spaceY = 18;

    /**
     * Display the text "All" instead of a number when the slot is set to -1.
     */
    protected static final NumberFormatMUI numberFormatAll = new NumberFormatMUI() {

        @Override
        public StringBuffer format(double number, StringBuffer toAppendTo, FieldPosition pos) {
            if (number < 0) {
                return toAppendTo.append(GTUtility.trans("ALL", "All"));
            } else {
                return super.format(number, toAppendTo, pos);
            }
        }
    };
    private static final int MAX_SLOT_NUMBER = 1073741822;

    private int maxSlot;
    private int maxThreshold;

    public ItemMeterUIFactory(CoverUIBuildContext buildContext) {
        super(buildContext);
    }

    @Override
    protected CoverItemMeter adaptCover(Cover cover) {
        if (cover instanceof CoverItemMeter adapterCover) {
            return adapterCover;
        }
        return null;
    }

    @Override
    protected void addUIWidgets(ModularWindow.Builder builder) {
        setMaxSlot();
        setMaxThreshold();

        builder
            .widget(
                new CoverDataControllerWidget<>(this::getCover, getUIBuildContext())
                    .addFollower(
                        CoverDataFollowerToggleButtonWidget.ofRedstone(),
                        CoverItemMeter::isInverted,
                        CoverItemMeter::setInverted,
                        widget -> widget.addTooltip(0, translateToLocal("gt.interact.desc.normal"))
                            .addTooltip(1, translateToLocal("gt.interact.desc.inverted"))
                            .setPos(0, 0))
                    .addFollower(
                        new CoverDataFollowerNumericWidget<>(),
                        coverData -> (double) coverData.getThreshold(),
                        (coverData, state) -> coverData.setThresdhold(state.intValue()),
                        widget -> widget.setBounds(0, maxThreshold)
                            .setScrollValues(1, 64, 1000)
                            .setFocusOnGuiOpen(true)
                            .setPos(0, 2 + spaceY)
                            .setSize(spaceX * 4 + 5, 12))
                    .addFollower(
                        new CoverDataFollowerNumericWidget<>(),
                        coverData -> (double) coverData.getSlot(),
                        (coverData, state) -> coverData.setSlot(state.intValue()),
                        widget -> widget.setBounds(-1, maxSlot)
                            .setDefaultValue(-1)
                            .setScrollValues(1, 100, 10)
                            .setNumberFormat(numberFormatAll)
                            .setPos(0, 2 + spaceY * 2)
                            .setSize(spaceX * 3 + 1, 12))
                    .setPos(startX, startY))
            .widget(
                new ItemWatcherSlotWidget().setGetter(this::getTargetItem)
                    .setPos(startX + spaceX * 3 + 8, startY + spaceY * 2))
            .widget(
                new TextWidget()
                    .setStringSupplier(
                        getCoverString(
                            c -> c.isInverted() ? translateToLocal("gt.interact.desc.inverted")
                                : translateToLocal("gt.interact.desc.normal")))
                    .setDefaultColor(COLOR_TEXT_GRAY.get())
                    .setPos(startX + spaceX, 4 + startY))
            .widget(
                new TextWidget(GTUtility.trans("254", "Detect slot #")).setDefaultColor(COLOR_TEXT_GRAY.get())
                    .setPos(startX + spaceX * 4 + 9, 4 + startY + spaceY * 2))
            .widget(
                new TextWidget(GTUtility.trans("221", "Item threshold")).setDefaultColor(COLOR_TEXT_GRAY.get())
                    .setPos(startX + spaceX * 4 + 9, 4 + startY + spaceY));
    }

    private void setMaxSlot() {
        final ICoverable tile = getUIBuildContext().getTile();
        if (!tile.isDead() && tile instanceof IGregTechTileEntity gtTile
            && !(gtTile.getMetaTileEntity() instanceof MTEDigitalChestBase)) {
            maxSlot = Math.min(tile.getSizeInventory() - 1, MAX_SLOT_NUMBER);
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
        CoverItemMeter cover = getCover();
        if (cover == null || cover.getSlot() < 0) {
            return null;
        }
        ICoverable tile = getUIBuildContext().getTile();
        if (tile instanceof TileEntity && !tile.isDead()) {
            if (tile.getSizeInventory() >= cover.getSlot()) {
                return tile.getStackInSlot(cover.getSlot());
            }
        }
        return null;
    }
}
