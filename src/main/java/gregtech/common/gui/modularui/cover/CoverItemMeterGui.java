package gregtech.common.gui.modularui.cover;

import static net.minecraft.util.StatCollector.translateToLocal;

import java.util.function.Consumer;

import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.screen.RichTooltip;
import com.cleanroommc.modularui.utils.item.IItemHandler;
import com.cleanroommc.modularui.utils.item.InvWrapper;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.GenericSyncValue;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.ItemDisplayWidget;
import com.cleanroommc.modularui.widgets.ToggleButton;
import com.cleanroommc.modularui.widgets.layout.Flow;

import gregtech.api.enums.ItemList;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.modularui2.CoverGuiData;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.covers.CoverItemMeter;
import gregtech.common.gui.modularui.cover.base.CoverBaseGui;

public class CoverItemMeterGui extends CoverBaseGui<CoverItemMeter> {

    public CoverItemMeterGui(CoverItemMeter cover) {
        super(cover);
    }

    @Override
    public void addUIWidgets(PanelSyncManager syncManager, Flow column, CoverGuiData data) {
        column.child(positionRow(createRedstoneModeRow()))
            .child(positionRow(createItemThresholdRow()))
            .child(positionRow(createSlotRow(syncManager)));
    }

    private Flow createRedstoneModeRow() {
        return Flow.row()
            .marginBottom(4)
            .child(createRedstoneModeButton())
            .child(
                IKey.dynamic(this::getRedstoneModeTranslation)
                    .asWidget());
    }

    private IWidget createRedstoneModeButton() {
        BooleanSyncValue isInvertedSyncer = new BooleanSyncValue(cover::isInverted, cover::setInverted);

        return new ToggleButton().value(isInvertedSyncer)
            .overlay(true, GTGuiTextures.OVERLAY_BUTTON_REDSTONE_ON)
            .overlay(false, GTGuiTextures.OVERLAY_BUTTON_REDSTONE_OFF)
            .tooltipDynamic(t -> t.addLine(getRedstoneModeTranslation()))
            .marginRight(4);
    }

    private String getRedstoneModeTranslation() {
        return StatCollector.translateToLocal(
            cover.isInverted() ? "gt.interact.desc.inverted.tooltip" : "gt.interact.desc.normal.tooltip");
    }

    private Flow createItemThresholdRow() {
        IntSyncValue thresholdSyncer = new IntSyncValue(cover::getThreshold, cover::setThreshold);

        return Flow.row()
            .marginBottom(4)
            .child(
                makeNumberField(50).value(thresholdSyncer)
                    .setDefaultNumber(0)
                    .setNumbers(0, Integer.MAX_VALUE)
                    .marginRight(2))
            .child(
                IKey.lang("gt.interact.desc.itemthreshold")
                    .asWidget());
    }

    private Flow createSlotRow(PanelSyncManager syncManager) {
        IntSyncValue slotSyncer = new IntSyncValue(cover::getSlot, cover::setSlot);
        IItemHandler inventoryHandler = getInventoryHandler();

        syncManager.syncValue(
            "display_item",
            GenericSyncValue.forItem(() -> queryMTEItem(inventoryHandler, slotSyncer.getIntValue()), null));

        return Flow.row()
            .child(createSlotInputField(slotSyncer))
            .child(createItemDisplayWidget(slotSyncer))
            .child(
                IKey.lang("gt.interact.desc.item_slot")
                    .asWidget());
    }

    private IWidget createSlotInputField(IntSyncValue slotSyncer) {
        // number field with 'Any' goes here
        return makeNumberField(50).value(slotSyncer)
            .setDefaultNumber(-1)
            .setNumbers(
                -1,
                cover.getTile()
                    .getSizeInventory() - 1)
            .marginRight(2);
    }

    private IItemHandler getInventoryHandler() {
        final ICoverable tile = cover.getTile();

        if (tile.isDead()) {
            return null;
        }
        if (!(tile instanceof IGregTechTileEntity gtTile)) {
            return null;
        }
        if (gtTile.getMetaTileEntity() == null) {
            return null;
        }
        if (tile.getSizeInventory() <= 0) {
            return null;
        }

        return new InvWrapper(gtTile.getMetaTileEntity());
    }

    private Consumer<RichTooltip> getItemDisplayTooltip(IntSyncValue slotSyncer) {
        return t -> {
            // to convey the -1 = 'Any'.
            if (slotSyncer.getIntValue() != -1) {
                return;
            }
            t.addLine(translateToLocal("gt.interact.desc.any_slot"));
        };
    }

    private IWidget createItemDisplayWidget(IntSyncValue slotSyncer) {
        return new ItemDisplayWidget().syncHandler("display_item")
            .displayAmount(false)
            .tooltipDynamic(getItemDisplayTooltip(slotSyncer));
    }

    private ItemStack queryMTEItem(IItemHandler inv, int slot) {
        if (inv != null && slot >= 0 && inv.getSlots() >= slot && inv.getStackInSlot(slot) != null) {
            return inv.getStackInSlot(slot);
        }
        return ItemList.Display_ITS_FREE.get(1);
    }

}
