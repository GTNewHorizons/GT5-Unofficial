package gregtech.common.gui.modularui.multiblock;

import static net.minecraft.util.StatCollector.translateToLocal;

import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import gregtech.common.tileentities.machines.multi.purification.MTEPurificationUnitBase;

public class MTEPurificationUnitBaseGui extends MTEMultiBlockBaseGui<MTEPurificationUnitBase<?>> {

    public MTEPurificationUnitBaseGui(MTEPurificationUnitBase<?> multiblock) {
        super(multiblock);
    }

    @Override
    protected void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);
        IntSyncValue parallelSyncer = new IntSyncValue(multiblock::getMaxParallel, multiblock::setMaxParallel);
        syncManager.syncValue("maximumParallels", parallelSyncer);
    }

    @Override
    protected Flow createButtonColumn(ModularPanel panel, PanelSyncManager syncManager) {
        return super.createButtonColumn(panel, syncManager).child(createParallelButton(syncManager, panel));
    }

    protected IWidget createParallelButton(PanelSyncManager syncManager, ModularPanel parent) {
        IPanelHandler parallelSelectPanel = syncManager.syncedPanel(
            "parallelSelectPanel",
            true,
            (p_syncManager, syncHandler) -> openParallelSelectPanel(syncManager, parent));

        return new ButtonWidget<>().size(18)
            .overlay(GTGuiTextures.OVERLAY_BUTTON_BATCH_MODE_ON)
            .tooltip(t -> t.addLine(translateToLocal("GT5U.tpm.parallelwindow")))
            .onMousePressed(mouseButton -> {
                if (!parallelSelectPanel.isPanelOpen()) {
                    parallelSelectPanel.openPanel();
                } else {
                    parallelSelectPanel.closePanel();
                }
                return true;
            });
    }

    private static final int WIDTH = 120;
    private static final int HEIGHT = 50;
    private static final int PADDING_SIDES = 4;

    private ModularPanel openParallelSelectPanel(PanelSyncManager syncManager, ModularPanel parent) {
        ModularPanel returnPanel = new ModularPanel("parallelSelectPanel").size(WIDTH, HEIGHT)
            .relative(parent)
            .leftRel(1)
            .topRel(0.8f);

        IntSyncValue parallelSyncer = syncManager.findSyncHandler("maximumParallels", IntSyncValue.class);
        Flow holdingColumn = Flow.column()
            .sizeRel(1)
            .paddingTop(12);
        holdingColumn.child(
            IKey.lang("GTPP.CC.parallel")
                .asWidget()
                .marginBottom(4));
        holdingColumn.child(
            new TextFieldWidget().setFormatAsInteger(true)
                .setNumbers(1, Integer.MAX_VALUE)
                .setTextAlignment(Alignment.CENTER)
                .setDefaultNumber(1)
                .value(parallelSyncer)
                .size(WIDTH - PADDING_SIDES * 2, 18)
                .align(Alignment.Center));

        returnPanel.child(holdingColumn);

        return returnPanel;
    }
}
