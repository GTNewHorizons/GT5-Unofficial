package gregtech.common.gui.modularui.multiblock;

import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.DoubleSyncValue;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.value.sync.StringSyncValue;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.ProgressWidget;
import com.cleanroommc.modularui.widgets.TextWidget;
import com.cleanroommc.modularui.widgets.ToggleButton;
import com.cleanroommc.modularui.widgets.layout.Flow;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.modularui2.GTGuis;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import gregtech.common.tileentities.machines.multi.MTECZPuller;

public class MTECZPullerGui extends MTEMultiBlockBaseGui<MTECZPuller> {

    private final MTECZPuller base;

    public MTECZPullerGui(MTECZPuller base) {
        super(base);
        this.base = base;
    }

    @Override
    protected void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);
        syncManager.syncValue("Type", new StringSyncValue(base::getBufferMaterialName));
        syncManager.syncValue("Amount", new IntSyncValue(base::getStoredMaterialAmount));
        syncManager
            .syncValue("Preheated", new BooleanSyncValue(base::isPreheatedMode, base::setPreheatedMode).allowC2S());
        syncManager.syncValue("Gas", new IntSyncValue(base::getStoredGasAmount));
        syncManager.syncValue("GasName", new StringSyncValue(base::getStoredGasName));
        syncManager.syncValue("Capacity", new IntSyncValue(base::getBufferCapacity));
    }

    @Override
    protected Flow createButtonColumn(ModularPanel parent, PanelSyncManager syncManager) {
        IPanelHandler storagePanel = syncManager
            .panel("storage", (panelSyncManager, syncHandler) -> createCZPanel(syncManager, parent), true);

        return super.createButtonColumn(parent, syncManager).child(
            new ToggleButton().syncHandler("Preheated")
                .overlay(true, GTGuiTextures.OVERLAY_BUTTON_CYCLIC)
                .overlay(false, GTGuiTextures.OVERLAY_BUTTON_CYCLIC)
                .tooltip(true, tooltip -> tooltip.add("Preheat mode: heating power scales with coil tier"))
                .tooltip(false, tooltip -> tooltip.add("Heat on demand: 120 EU/t while heating"))
                .size(18, 18))
            .child(new ButtonWidget<>().onMousePressed(mouseButton -> {
                if (!storagePanel.isPanelOpen()) {
                    storagePanel.openPanel();
                } else {
                    storagePanel.closePanel();
                }
                return true;
            })
                .background(GTGuiTextures.BUTTON_STANDARD, GTGuiTextures.OVERLAY_BUTTON_CYCLIC)
                .disableHoverBackground()
                .tooltip(tooltip -> tooltip.add("Internal growth storage"))
                .left(-20)
                .marginBottom(2)
                .size(18, 18));
    }

    public ModularPanel createCZPanel(PanelSyncManager syncManager, ModularPanel parent) {
        ModularPanel ui = GTGuis.createPopUpPanel("czpuller_storage")
            .relative(parent)
            .leftRel(1)
            .topRel(0)
            .size(180, 84);

        StringSyncValue typeSyncer = (StringSyncValue) syncManager.getSyncHandler("Type:0");
        IntSyncValue amountSyncer = (IntSyncValue) syncManager.getSyncHandler("Amount:0");
        IntSyncValue gasSyncer = (IntSyncValue) syncManager.getSyncHandler("Gas:0");
        StringSyncValue gasNameSyncer = (StringSyncValue) syncManager.getSyncHandler("GasName:0");
        IntSyncValue capacitySyncer = (IntSyncValue) syncManager.getSyncHandler("Capacity:0");

        ui.child(
            new TextWidget<>(IKey.str("CZ Puller Storage")).textAlign(Alignment.Center)
                .pos(8, 7)
                .size(150, 12));

        Flow infoColumn = Flow.column()
            .pos(8, 24)
            .size(164, 26)
            .childPadding(1)
            .child(new TextWidget<>(IKey.dynamic(() -> {
                String materialName = typeSyncer.getValue();
                String material = "Silicon".equals(materialName) ? "Si"
                    : "Microwaved Silicon".equals(materialName) ? "MW-Si" : materialName;
                return String.format("Material: %s", material);
            })))
            .child(
                new TextWidget<>(
                    IKey.dynamic(
                        () -> String
                            .format("Amount: %d/%d dust", amountSyncer.getValue(), capacitySyncer.getValue()))));
        ui.child(infoColumn);

        ProgressWidget storageFill = new ProgressWidget().value(new DoubleSyncValue(() -> {
            int capacity = capacitySyncer.getIntValue();
            return capacity <= 0 ? 0.0 : Math.clamp((double) amountSyncer.getIntValue() / capacity, 0.0, 1.0);
        }))
            .direction(ProgressWidget.Direction.RIGHT)
            .texture(GTGuiTextures.PROGRESSBAR_STORED_EU, 147)
            .pos(8, 53)
            .size(160, 10)
            .tooltip(
                tooltip -> tooltip.add(
                    String.format(
                        "Internal storage: %d / %d dust",
                        amountSyncer.getIntValue(),
                        capacitySyncer.getIntValue())));
        ui.child(storageFill);
        ui.child(
            new TextWidget<>(
                IKey.dynamic(() -> String.format("Gas (%s): %dL", gasNameSyncer.getValue(), gasSyncer.getValue())))
                    .textAlign(Alignment.Center)
                    .pos(8, 67)
                    .size(164, 12));
        return ui;
    }
}
