package gregtech.common.gui.modularui.cover.base;

import static net.minecraft.util.StatCollector.translateToLocal;

import com.cleanroommc.modularui.value.sync.EnumSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.TextWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;

import gregtech.api.modularui2.CoverGuiData;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.covers.redstone.CoverAdvancedRedstoneReceiverBase;
import gregtech.common.covers.redstone.CoverAdvancedWirelessRedstoneBase;
import gregtech.common.modularui2.widget.builder.EnumRowBuilder;

public class CoverAdvancedRedstoneReceiverBaseGui
    extends CoverAdvancedWirelessRedstoneBaseGui<CoverAdvancedRedstoneReceiverBase> {

    public CoverAdvancedRedstoneReceiverBaseGui(CoverAdvancedWirelessRedstoneBase cover) {
        super(cover, true);
    }

    @Override
    public void addUIWidgets(PanelSyncManager syncManager, Flow column, CoverGuiData data) {
        EnumSyncValue<CoverAdvancedRedstoneReceiverBase.GateMode> gateModeSync = new EnumSyncValue<>(
            CoverAdvancedRedstoneReceiverBase.GateMode.class,
            cover::getGateMode,
            cover::setMode);
        syncManager.syncValue("gateMode", gateModeSync);
        super.addUIWidgets(syncManager, column, data);
    }

    @Override
    protected Flow makeThirdFlow(PanelSyncManager syncManager, CoverGuiData data) {
        EnumSyncValue<CoverAdvancedRedstoneReceiverBase.GateMode> gateMode = (EnumSyncValue<CoverAdvancedRedstoneReceiverBase.GateMode>) syncManager
            .getSyncHandlerFromMapKey("gateMode:0");
        return Flow.row()
            .size(140, 18)

            .child(
                new EnumRowBuilder<>(CoverAdvancedRedstoneReceiverBase.GateMode.class).value(gateMode)
                    .overlay(
                        GTGuiTextures.OVERLAY_BUTTON_GATE_AND,
                        GTGuiTextures.OVERLAY_BUTTON_GATE_NAND,
                        GTGuiTextures.OVERLAY_BUTTON_GATE_OR,
                        GTGuiTextures.OVERLAY_BUTTON_GATE_NOR,
                        GTGuiTextures.OVERLAY_BUTTON_ANALOG)
                    .build()
                    .width(120)
                    .marginRight(2))
            .child(new TextWidget(translateToLocal("gt.interact.desc.gatemode")));
    }

}
