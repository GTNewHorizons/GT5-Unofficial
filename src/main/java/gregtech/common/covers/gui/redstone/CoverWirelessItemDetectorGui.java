package gregtech.common.covers.gui.redstone;

import static net.minecraft.util.StatCollector.translateToLocal;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.drawable.DynamicDrawable;
import com.cleanroommc.modularui.drawable.Icon;
import com.cleanroommc.modularui.drawable.ItemDrawable;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.TextWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;

import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.covers.redstone.CoverAdvancedRedstoneTransmitterBase;
import gregtech.common.covers.redstone.CoverWirelessItemDetector;

public class CoverWirelessItemDetectorGui extends CoverAdvancedRedstoneTransmitterBaseGui<CoverWirelessItemDetector> {

    public CoverWirelessItemDetectorGui(CoverAdvancedRedstoneTransmitterBase cover) {
        super(cover);
    }

    // TODO: a real slot widget listener and numericTextField with "Any" as default.
    /*
     * this workaround is pretty bad and its quite unresponsive. To render items client side, it requires the client to
     * open the MTE.
     * after doing so, i just use a dynamicdrawable and supplier.
     */
    @Override
    protected Flow makeThirdFlow(PanelSyncManager syncManager) {
        IntSyncValue thresholdSyncer = new IntSyncValue(cover::getThreshold, cover::setThreshold);
        BooleanSyncValue physicalSyncer = new BooleanSyncValue(cover::isPhysical, cover::setPhysical);
        IntSyncValue slotSyncer = new IntSyncValue(cover::getSlot, cover::setSlot);
        final ICoverable tile = cover.getTile();
        return Flow.column()
            .coverChildren()

            .child(
                Flow.row()
                    .height(16)
                    .marginBottom(4)

                    .child(
                        makeNumberField(88).value(thresholdSyncer)
                            .marginRight(2))
                    .child(new TextWidget(IKey.lang(translateToLocal("gt.interact.desc.itemthreshold")))))
            .coverChildrenWidth()
            .child(
                Flow.row()
                    .size(140, 16)
                    .marginBottom(4)

                    .child(
                        makeNumberField(88).value(slotSyncer)
                            .setDefaultNumber(-1)
                            .setNumbers(-1, tile.getSizeInventory())
                            .marginRight(2))
                    .child(new DynamicDrawable(() -> getTargetItemDrawable(slotSyncer)).asWidget())
                    .child(new TextWidget(IKey.lang(translateToLocal("gt.interact.desc.item_slot")))))
            .coverChildrenWidth()

            .child(physicalRow(physicalSyncer));
    }

    private Icon getTargetItemDrawable(IntSyncValue slotSyncer) {
        final ICoverable tile = cover.getTile();
        if (slotSyncer.getIntValue() >= 0 && !tile.isDead()
            && tile instanceof IGregTechTileEntity gtTile
            && (gtTile.getMetaTileEntity() != null)
            && tile.getSizeInventory() >= slotSyncer.getIntValue()) {
            return new ItemDrawable(
                gtTile.getMetaTileEntity()
                    .getStackInSlot(slotSyncer.getIntValue())).asIcon()
                        .size(16, 16);
        } else {
            return new Icon(GTGuiTextures.OVERLAY_BUTTON_BOUNDING_BOX);
        }
    }

    @Override
    protected int getGUIHeight() {
        return 160;
    }
}
