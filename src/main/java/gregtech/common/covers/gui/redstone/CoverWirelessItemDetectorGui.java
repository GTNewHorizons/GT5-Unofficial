package gregtech.common.covers.gui.redstone;

import static net.minecraft.util.StatCollector.translateToLocal;

import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.drawable.DynamicDrawable;
import com.cleanroommc.modularui.drawable.ItemDrawable;
import com.cleanroommc.modularui.drawable.UITexture;
import com.cleanroommc.modularui.factory.GuiData;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.Widget;
import com.cleanroommc.modularui.widgets.TextWidget;
import com.cleanroommc.modularui.widgets.ToggleButton;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;
import com.cleanroommc.modularui.widgets.slot.PhantomItemSlot;

import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.covers.redstone.CoverAdvancedRedstoneTransmitterBase;
import gregtech.common.covers.redstone.CoverWirelessItemDetector;

public class CoverWirelessItemDetectorGui extends CoverAdvancedRedstoneTransmitterBaseGui<CoverWirelessItemDetector> {

    public CoverWirelessItemDetectorGui(CoverAdvancedRedstoneTransmitterBase cover) {
        super(cover);
    }

    // wip
    @Override
    public void addUIWidgets(PanelSyncManager syncManager, Flow column, GuiData data) {
        IntSyncValue thresholdSyncer = new IntSyncValue(cover::getThreshold, cover::setThreshold);
        syncManager.syncValue("threshold", thresholdSyncer);
        BooleanSyncValue physicalSyncer = new BooleanSyncValue(cover::isPhysical, cover::setPhysical);
        syncManager.syncValue("physical", physicalSyncer);
        IntSyncValue slotSyncer = new IntSyncValue(cover::getSlot, cover::setSlot);
        syncManager.syncValue("slot", slotSyncer);
        super.addUIWidgets(syncManager, column, data);
    }

    @Override
    protected Flow makeThirdRow(PanelSyncManager syncManager) {
        IntSyncValue thresholdSyncer = new IntSyncValue(cover::getThreshold, cover::setThreshold);
        BooleanSyncValue physicalSyncer = new BooleanSyncValue(cover::isPhysical, cover::setPhysical);
        IntSyncValue slotSyncer = new IntSyncValue(cover::getSlot, cover::setSlot);
        final ICoverable tile = cover.getTile();
        return Flow.column()
            .size(200,100)
            .child(
                Flow.row().marginBottom(8)
                    .coverChildren()
                    .height(16)
                    .child(
                        makeNumberField(88).value(thresholdSyncer)
                            .marginRight(2))
                    .child(new TextWidget(IKey.lang(translateToLocal("gt.interact.desc.itemthreshold")))))
            .child(
                Flow.row()
                    .coverChildren()
                    .height(16)
                    .child(
                        makeNumberField(88).value(slotSyncer)
                            .setDefaultNumber(-1)
                            .setNumbers(
                                -1,
                                tile
                                    .getSizeInventory())
                            .marginRight(2))
                    .child(new DynamicDrawable(()->getTargetItemDrawable(slotSyncer)).asWidget() ))
            .child(
                Flow.row()
                    .coverChildren()
                    .width(200)
                    .child(
                        new ToggleButton().size(16)
                            .marginRight(2)
                            .value(physicalSyncer)
                            .overlay(true, GTGuiTextures.OVERLAY_BUTTON_CHECKMARK))
                    .child(
                        new TextWidget(
                            IKey.dynamic(
                                () -> translateToLocal(
                                    physicalSyncer.getValue() ? "gt.cover.wirelessdetector.redstone.1"
                                        : "gt.cover.wirelessdetector.redstone.0"))).height(16)));
    }

    private IDrawable getTargetItemDrawable(IntSyncValue slotSyncer) {
        final ICoverable tile = cover.getTile();
        int slotIndex = slotSyncer.getIntValue();
        if (slotIndex >= 0 && tile instanceof IGregTechTileEntity gtTile && (gtTile.getMetaTileEntity() != null)
            && !tile.isDead()
            && tile.getSizeInventory() >= slotIndex) {
           /* return new PhantomItemSlot()
                .slot(new ModularSlot(( gtTile.getMetaTileEntity().getInventoryHandler()), slotIndex));*/
            return new ItemDrawable(gtTile.getMetaTileEntity().getStackInSlot(slotIndex));
        } else {
            return GTGuiTextures.OVERLAY_BUTTON_CROSS;
        }
    }

    @Override
    protected int getGUIHeight() {
        return 160;
    }
}
