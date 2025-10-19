package gregtech.common.tileentities.machines.multi.gui;

import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.value.sync.StringSyncValue;
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
    protected void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);
        syncManager.syncValue("Type", new StringSyncValue(() -> base.materialType, dub -> base.materialType = dub));
        syncManager.syncValue("Heat", new IntSyncValue(() -> base.mHeat, dub -> base.mHeat = dub));
        syncManager.syncValue("Amount", new IntSyncValue(() -> base.mAmount, dub -> base.mAmount = dub));
    }

    @Override
    public ModularPanel build(PosGuiData data, PanelSyncManager syncManager, UISettings uiSettings) {
        ModularPanel ui = super.build(data, syncManager, uiSettings);
        IPanelHandler popupPanel = syncManager.panel("popup", (m, h) -> createCZPanel(syncManager), true);

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
            .pos(174, 124)
            .size(18, 18));
    }

    public ModularPanel createCZPanel(PanelSyncManager syncManager) {
        ModularPanel ui = ModularPanel.defaultPanel("gt:czpuller")
            .size(176, 207)
            .background(GTGuiTextures.BACKGROUND_STANDARD);

        StringSyncValue typeSyncer = (StringSyncValue) syncManager.getSyncHandler("Type:0");
        IntSyncValue heatSyncer = (IntSyncValue) syncManager.getSyncHandler("Heat:0");
        IntSyncValue amountSyncer = (IntSyncValue) syncManager.getSyncHandler("Amount:0");

        PagedWidget.Controller tabController = new PagedWidget.Controller();
        PagedWidget<?> pagedWidget = new PagedWidget<>().controller(tabController);
        ParentWidget<?> infoPage = new ParentWidget<>()
            .child(
                new TextWidget(IKey.dynamic(() -> String.format("Heat: %d%%", heatSyncer.getValue()))).pos(5, 30)
                    .size(164, 10)
                    .alignment(Alignment.TopLeft))
            .child(
                new TextWidget(IKey.dynamic(() -> String.format("Material type: %s", typeSyncer.getValue()))).pos(5, 40)
                    .size(164, 10)
                    .alignment(Alignment.TopLeft))
            .child(
                new TextWidget(IKey.dynamic(() -> String.format("Material amount: %d", amountSyncer.getValue())))
                    .pos(5, 50)
                    .size(164, 10)
                    .alignment(Alignment.TopLeft));
        infoPage.sizeRel(1.0f);
        return ui.child(
            pagedWidget.addPage(infoPage)
                .sizeRel(1.0f))
            .posRel(0.3f, 0.5f);
    }
}
