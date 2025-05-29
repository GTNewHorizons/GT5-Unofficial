package gregtech.common.tileentities.machines.multi.gui;

import gregtech.api.metatileentity.implementations.MTEMultiBlockBase;
import gregtech.api.metatileentity.implementations.gui.MTEMultiBlockBaseGui;
import gregtech.api.modularui2.GTGuiTextures;
package gregtech.common.tileentities.machines.multi.gui;

import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.DoubleSyncValue;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.PagedWidget;
import com.cleanroommc.modularui.widgets.TextWidget;

import gregtech.api.metatileentity.implementations.gui.MTEMultiBlockBaseGui;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.tileentities.machines.multi.MTECZPuller;

public class MTECZPullerGui extends MTEMultiBlockBaseGui {

    private final MTECZPuller base;

    public MTECZPullerGui(MTECZPuller base) {
        super(base);
        this.base = base;
    }

    @Override
    protected void registerSyncValues (PanelSyncManager syncManager) {
        super.registerSyncValues (syncManager);
        syncManager.syncValue("Pressure", new DoubleSyncValue(()-> base.ECCFCurrentPressure, dub -> base.ECCFCurrentPressure = dub));
        syncManager.syncValue("Temperature", new DoubleSyncValue(() -> base.ECCFCurrentTemp, dub-> base.ECCFCurrentTemp = dub));
        syncManager.syncValue("Compress", new IntSyncValue(() -> base.compressCoilTier, dub -> base.compressCoilTier = dub));
        syncManager.syncValue("Vacuum", new IntSyncValue(() -> base.vacuumCoilTier, dub -> base.vacuumCoilTier = dub));
        syncManager.syncValue("Heat", new IntSyncValue(() -> base.heatCoilTier, dub -> base.heatCoilTier = dub));
        syncManager.syncValue("Cool", new IntSyncValue(() -> base.coolCoilTier, dub -> base.coolCoilTier = dub));
    }

    @Override
    public ModularPanel build(PosGuiData data, PanelSyncManager syncManager, UISettings uiSettings) {
        ModularPanel ui = super.build(data, syncManager, uiSettings);
        IPanelHandler popupPanel = syncManager.panel("popup", (m, h) -> createECCFPanel(syncManager), true);

        return ui.child(new ButtonWidget<>().onMousePressed(mouseButton -> {
                if (!popupPanel.isPanelOpen()) {
                    popupPanel.openPanel();
                } else {
                    popupPanel.closePanel();
                }
                return true;
            })
            .background(GTGuiTextures.BUTTON_STANDARD, GTGuiTextures.OVERLAY_BUTTON_THERMOMETER)
            .disableHoverBackground()
            .tooltip(tooltip -> tooltip.add("Temperature Control"))
            .pos(156, 102)
            .size(18, 18));
    }

    public ModularPanel createECCFPanel(PanelSyncManager syncManager) {
        ModularPanel ui = ModularPanel.defaultPanel("gt:eccf")
            .size(176, 207)
            .background(GTGuiTextures.BACKGROUND_STANDARD);

        DoubleSyncValue pressureSyncer = (DoubleSyncValue) syncManager.getSyncHandler ("Pressure:0");
        DoubleSyncValue tempSyncer = (DoubleSyncValue) syncManager.getSyncHandler("Temperature:0");
        DoubleSyncValue heatCoilSyncer = (DoubleSyncValue) syncManager.getSyncHandler ("Heat:0");
        DoubleSyncValue coolCoilSyncer = (DoubleSyncValue) syncManager.getSyncHandler ("Cool:0");
        DoubleSyncValue vacuumCoilSyncer = (DoubleSyncValue) syncManager.getSyncHandler ("Vacuum:0");
        DoubleSyncValue compressCoilSyncer = (DoubleSyncValue) syncManager.getSyncHandler ("Compress:0");

        PagedWidget.Controller tabController = new PagedWidget.Controller();
        PagedWidget<?> pagedWidget = new PagedWidget<>().controller(tabController);
        ParentWidget<?> infoPage = new ParentWidget<>().child(
                GTGuiTextures.PROGRESSBAR_ECCF_TEMPERATURE.asWidget()
                    .pos(40 - 24 / 2, 5)
                    .size(24, 100))
            .child(
                new TextWidget(IKey.dynamic(() -> String.format("Temperature: %.2f K", tempSyncer.getValue())))
                    .pos(5, 110)
                    .size(70, 10)
                    .alignment(Alignment.Center))
            .child(
                GTGuiTextures.PROGRESSBAR_ECCF_PRESSURE.asWidget()
                    .pos(140 - 24 / 2, 5)
                    .size(24, 100))
            .child(
                new TextWidget(IKey.dynamic(() -> String.format("Pressure: %.2f Pa", pressureSyncer.getValue())))
                    .pos(105, 110)
                    .size(70, 10)
                    .alignment(Alignment.Center));
        if (coolCoilSyncer.getValue() > 0) {
            infoPage.child(
                GTGuiTextures.COOL_MODULE_ECCF_INDICATOR.asWidget()
                    .pos(10, 130)
                    .size(24, 48));
        }
        infoPage.sizeRel(1.0f);
        return ui.child(
            pagedWidget
                .addPage(infoPage)
                .sizeRel(1.0f)
        ).posRel(0.3f, 0.5f);
    }
}
