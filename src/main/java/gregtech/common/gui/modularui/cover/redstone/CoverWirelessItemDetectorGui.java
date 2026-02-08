package gregtech.common.gui.modularui.cover.redstone;

import static net.minecraft.util.StatCollector.translateToLocal;

import net.minecraft.item.ItemStack;

import com.cleanroommc.modularui.utils.item.IItemHandler;
import com.cleanroommc.modularui.utils.item.InvWrapper;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.GenericSyncValue;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.ItemDisplayWidget;
import com.cleanroommc.modularui.widgets.TextWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;

import gregtech.api.enums.ItemList;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.modularui2.CoverGuiData;
import gregtech.common.covers.redstone.CoverAdvancedRedstoneTransmitterBase;
import gregtech.common.covers.redstone.CoverWirelessItemDetector;
import gregtech.common.gui.modularui.cover.base.CoverAdvancedRedstoneTransmitterBaseGui;

public class CoverWirelessItemDetectorGui extends CoverAdvancedRedstoneTransmitterBaseGui<CoverWirelessItemDetector> {

    public CoverWirelessItemDetectorGui(CoverAdvancedRedstoneTransmitterBase cover) {
        super(cover);
    }

    // TODO: numericTextField with "Any" as default.
    @Override
    protected Flow makeThirdFlow(PanelSyncManager syncManager, CoverGuiData data) {
        IntSyncValue thresholdSyncer = new IntSyncValue(cover::getThreshold, cover::setThreshold);
        BooleanSyncValue physicalSyncer = new BooleanSyncValue(cover::isPhysical, cover::setPhysical);
        IntSyncValue slotSyncer = new IntSyncValue(cover::getSlot, cover::setSlot);
        final ICoverable tile = data.getCoverable();
        IItemHandler inventoryHandler;
        if (!tile.isDead() && tile instanceof IGregTechTileEntity gtTile
            && gtTile.getMetaTileEntity() != null
            && tile.getSizeInventory() > 0) {
            inventoryHandler = new InvWrapper(gtTile.getMetaTileEntity());
        } else {
            inventoryHandler = null;
        }
        syncManager.syncValue(
            "display_item",
            GenericSyncValue.forItem(() -> queryMTEItem(inventoryHandler, slotSyncer.getIntValue()), null));
        ItemDisplayWidget displayWidget = new ItemDisplayWidget().syncHandler("display_item")
            .displayAmount(false);

        return Flow.column()
            .coverChildren()

            .child(
                Flow.row()
                    .height(16)
                    .marginBottom(4)

                    .child(
                        makeNumberField(88).value(thresholdSyncer)
                            .setDefaultNumber(0)
                            .setNumbers(0, Integer.MAX_VALUE)
                            .marginRight(2))
                    .child(new TextWidget<>(translateToLocal("gt.interact.desc.itemthreshold"))))
            .coverChildrenWidth()
            .child(
                Flow.row()
                    .size(140, 16)
                    .marginBottom(4)

                    .child(
                        // number field with 'Any' goes here
                        makeNumberField(88).value(slotSyncer)
                            .setFormatAsInteger(true)
                            .setDefaultNumber(-1)
                            .setNumbers(-1, tile.getSizeInventory() - 1)
                            .marginRight(2))
                    .child(displayWidget.tooltipDynamic(t -> {
                        // to convey the -1 = 'Any'.
                        if (slotSyncer.getIntValue() == -1) t.addLine(translateToLocal("gt.interact.desc.any_slot"));
                    }))
                    .child(new TextWidget<>(translateToLocal("gt.interact.desc.item_slot"))))
            .coverChildrenWidth()

            .child(physicalRow(physicalSyncer));
    }

    private ItemStack queryMTEItem(IItemHandler inv, int slot) {
        if (inv != null && slot >= 0 && inv.getSlots() >= slot && inv.getStackInSlot(slot) != null) {
            return inv.getStackInSlot(slot);
        }
        return ItemList.Display_ITS_FREE.get(1);
    }

    @Override
    protected int getGUIHeight() {
        return 160;
    }
}
