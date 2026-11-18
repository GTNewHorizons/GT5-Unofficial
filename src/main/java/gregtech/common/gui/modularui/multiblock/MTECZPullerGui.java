package gregtech.common.gui.modularui.multiblock;

import net.minecraft.util.StatCollector;

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
        syncManager.syncValue("Type", new StringSyncValue(base::getBufferMaterialTranslationKey));
        syncManager.syncValue("Amount", new IntSyncValue(base::getStoredMaterialAmount));
        syncManager
            .syncValue("Preheated", new BooleanSyncValue(base::isPreheatedMode, base::setPreheatedMode).allowC2S());
        syncManager.syncValue("Gas", new IntSyncValue(base::getStoredGasAmount));
        syncManager.syncValue("GasName", new StringSyncValue(base::getStoredGasName));
        syncManager.syncValue("Capacity", new IntSyncValue(base::getBufferCapacity));
        syncManager.syncValue("DumpBuffer", new IntSyncValue(() -> 0, value -> base.dumpBuffer()).allowC2S());
    }

    @Override
    protected Flow createButtonColumn(ModularPanel parent, PanelSyncManager syncManager) {
        IPanelHandler storagePanel = syncManager
            .panel("storage", (panelSyncManager, syncHandler) -> createCZPanel(syncManager, parent), true);

        return super.createButtonColumn(parent, syncManager).child(
            new ToggleButton().syncHandler("Preheated")
                .overlay(true, GTGuiTextures.OVERLAY_BUTTON_CYCLIC)
                .overlay(false, GTGuiTextures.OVERLAY_BUTTON_CYCLIC)
                .tooltip(true, tooltip -> tooltip.addLine(IKey.lang("GT5U.cz_puller.gui.preheat_tooltip")))
                .tooltip(false, tooltip -> tooltip.addLine(IKey.lang("GT5U.cz_puller.gui.on_demand_tooltip")))
                .size(18, 18))
            .child(new ButtonWidget<>().onMousePressed(mouseButton -> {
                storagePanel.togglePanel();
                return true;
            })
                .background(GTGuiTextures.BUTTON_STANDARD, GTGuiTextures.OVERLAY_BUTTON_CYCLIC)
                .disableHoverBackground()
                .tooltip(tooltip -> tooltip.addLine(IKey.lang("GT5U.cz_puller.gui.storage_tooltip")))
                .left(-20)
                .marginBottom(2)
                .size(18, 18));
    }

    public ModularPanel createCZPanel(PanelSyncManager syncManager, ModularPanel parent) {
        ModularPanel ui = GTGuis.createPopUpPanel("czpuller_storage")
            .relative(parent)
            .leftRel(1)
            .topRel(0)
            .size(180, 110);

        StringSyncValue typeSyncer = syncManager.findSyncHandler("Type", StringSyncValue.class);
        IntSyncValue amountSyncer = syncManager.findSyncHandler("Amount", IntSyncValue.class);
        IntSyncValue gasSyncer = syncManager.findSyncHandler("Gas", IntSyncValue.class);
        StringSyncValue gasNameSyncer = syncManager.findSyncHandler("GasName", StringSyncValue.class);
        IntSyncValue capacitySyncer = syncManager.findSyncHandler("Capacity", IntSyncValue.class);
        IntSyncValue dumpBufferSyncer = syncManager.findSyncHandler("DumpBuffer", IntSyncValue.class);

        ui.child(
            IKey.lang("GT5U.cz_puller.gui.storage_title")
                .asWidget()
                .textAlign(Alignment.Center)
                .pos(8, 7)
                .size(150, 12));

        Flow infoColumn = Flow.column()
            .pos(8, 24)
            .size(164, 26)
            .childPadding(1)
            .child(IKey.dynamic(() -> {
                String materialName = typeSyncer.getValue();
                String material = "GT5U.cz_puller.material.silicon".equals(materialName) ? "Si"
                    : "GT5U.cz_puller.material.microwaved_silicon".equals(materialName) ? "MW-Si"
                        : StatCollector.translateToLocal(materialName);
                return StatCollector.translateToLocalFormatted("GT5U.cz_puller.gui.material", material);
            })
                .asWidget())
            .child(
                IKey.dynamic(
                    () -> StatCollector.translateToLocalFormatted(
                        "GT5U.cz_puller.gui.amount",
                        amountSyncer.getValue(),
                        capacitySyncer.getValue()))
                    .asWidget());
        ui.child(infoColumn);

        ProgressWidget storageFill = new ProgressWidget().value(new DoubleSyncValue(() -> {
            int capacity = capacitySyncer.getIntValue();
            return capacity <= 0 ? 0.0 : Math.clamp(amountSyncer.getDoubleValue() / capacity, 0.0, 1.0);
        }))
            .direction(ProgressWidget.Direction.RIGHT)
            .texture(GTGuiTextures.PROGRESSBAR_STORED_EU, 147)
            .pos(8, 53)
            .size(160, 10)
            .tooltip(
                tooltip -> tooltip.addLine(
                    IKey.dynamic(
                        () -> StatCollector.translateToLocalFormatted(
                            "GT5U.cz_puller.gui.storage_amount",
                            amountSyncer.getIntValue(),
                            capacitySyncer.getIntValue()))));
        ui.child(storageFill);
        ui.child(
            IKey.dynamic(
                () -> StatCollector.translateToLocalFormatted(
                    "GT5U.cz_puller.gui.gas",
                    gasNameSyncer.getValue(),
                    gasSyncer.getValue()))
                .asWidget()
                .textAlign(Alignment.Center)
                .pos(8, 67)
                .size(164, 12));
        ui.child(
            new ButtonWidget<>().background(GTGuiTextures.BUTTON_STANDARD)
                .overlay(IKey.str("Dump"))
                .onMousePressed(mouse -> {
                    dumpBufferSyncer.setIntValue(dumpBufferSyncer.getIntValue() + 1);
                    return true;
                })
                .pos(8, 83)
                .size(160, 18));
        return ui;
    }
}
