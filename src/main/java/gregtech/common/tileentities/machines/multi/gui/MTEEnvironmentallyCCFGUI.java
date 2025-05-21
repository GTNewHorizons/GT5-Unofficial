package gregtech.common.tileentities.machines.multi.gui;

import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.DoubleSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.PagedWidget;
import com.cleanroommc.modularui.widgets.TextWidget;
import gregtech.api.metatileentity.implementations.gui.MTEMultiBlockBaseGui;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.tileentities.machines.multi.MTEEnvironmentallyControlledChemicalFacility;

import static gregtech.api.modularui2.GTGuis.createPopUpPanel;

public class MTEEnvironmentallyCCFGUI extends MTEMultiBlockBaseGui {
    private final MTEEnvironmentallyControlledChemicalFacility base;

    public MTEEnvironmentallyCCFGUI(MTEEnvironmentallyControlledChemicalFacility base) {
        super(base);
        this.base = base;
    }

    @Override
    public ModularPanel build(PosGuiData data, PanelSyncManager syncManager, UISettings uiSettings) {
        ModularPanel ui = super.build(data, syncManager, uiSettings);
        IPanelHandler popupPanel = syncManager.panel("popup", (m, h) -> createECCFPanel(syncManager), true);

        return ui.child(new ButtonWidget<>()
            .onMousePressed(mouseButton -> {
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
        ModularPanel ui = createPopUpPanel("gt:eccf", false, false).size(176, 207);

        DoubleSyncValue tempSyncer = new DoubleSyncValue(
            () -> base.ECCFCurrentTemp,
            dub -> base.ECCFCurrentTemp = dub
        );
        DoubleSyncValue pressureSyncer = new DoubleSyncValue(
            () -> base.ECCFCurrentPressure,
            dub -> base.ECCFCurrentPressure = dub
        );
        syncManager.syncValue("Temperature", tempSyncer);
        syncManager.syncValue("Pressure", pressureSyncer);

        PagedWidget.Controller tabController = new PagedWidget.Controller();
        PagedWidget<?> pagedWidget = new PagedWidget<>().controller(tabController);

        ParentWidget<?> infoPage = new ParentWidget<>()
                .child(GTGuiTextures.PROGRESSBAR_ECCF_TEMPERATURE.asWidget()
                        .pos(40-24/2, 5)
                        .size(24, 100))
                .child(new TextWidget(IKey.dynamic(() -> String.format("Temperature: %.2f K", tempSyncer.getValue())))
                        .pos(5, 110)
                        .size(70, 10)
                        .alignment(Alignment.Center))
                .child(GTGuiTextures.PROGRESSBAR_ECCF_PRESSURE.asWidget()
                        .pos(140-24/2, 5)
                        .size(24, 100))
            .child(new TextWidget(IKey.dynamic(() -> String.format("Pressure: %.2f Pa", pressureSyncer.getValue())))
                .pos(105, 110)
                .size(70, 10)
                .alignment(Alignment.Center))
            .sizeRel(1.0f);
        return ui.child(
            pagedWidget
                .addPage(infoPage)
        ).posRel( 0.35f, 0.5f);
    }
}
