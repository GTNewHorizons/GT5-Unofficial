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
        syncManager.syncValue("Pressure", new DoubleSyncValue(()-> base., dub -> base.ECCFCurrentPressure = dub));
        syncManager.syncValue("Temperature", new DoubleSyncValue(() -> base.ECCFCurrentTemp, dub-> base.ECCFCurrentTemp = dub));
        syncManager.syncValue("Compress", new IntSyncValue(() -> base.compressCoilTier, dub -> base.compressCoilTier = dub));
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
            .background(GTGuiTextures.BUTTON_STANDARD, GTGuiTextures.OVERLAY_BUTTON_CYCLIC)
            .disableHoverBackground()
            .tooltip(tooltip -> tooltip.add("Configuration Menu"))
            .pos(174, 130)
            .size(18, 18));
    }

    public ModularPanel createECCFPanel(PanelSyncManager syncManager) {
        ModularPanel ui = ModularPanel.defaultPanel("gt:eccf")
            .size(176, 207)
            .background(GTGuiTextures.BACKGROUND_STANDARD);

        DoubleSyncValue pressureSyncer = (DoubleSyncValue) syncManager.getSyncHandler ("Pressure:0");
        DoubleSyncValue tempSyncer = (DoubleSyncValue) syncManager.getSyncHandler("Temperature:0");
        DoubleSyncValue heatCoilSyncer = (DoubleSyncValue) syncManager.getSyncHandler ("Heat:0");

        PagedWidget.Controller tabController = new PagedWidget.Controller();
        PagedWidget<?> pagedWidget = new PagedWidget<>().controller(tabController);
        ParentWidget<?> infoPage = new ParentWidget<>()
            .child(
                new TextWidget(IKey.dynamic(() -> String.format("Temperature: %.2f K", tempSyncer.getValue())))
                    .pos(5, 110)
                    .size(70, 10)
                    .alignment(Alignment.Center))
            .child(
                new TextWidget(IKey.dynamic(() -> String.format("Pressure: %.2f Pa", pressureSyncer.getValue())))
                    .pos(105, 110)
                    .size(70, 10)
                    .alignment(Alignment.Center));
        infoPage.sizeRel(1.0f);
        return ui.child(
            pagedWidget
                .addPage(infoPage)
                .sizeRel(1.0f)
        ).posRel(0.3f, 0.5f);
    }
}
