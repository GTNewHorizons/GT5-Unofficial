package gregtech.common.gui.modularui.multiblock;

import static kekztech.util.Util.toStandardForm;
import static net.minecraft.util.StatCollector.translateToLocal;

import java.math.BigInteger;

import net.minecraft.util.EnumChatFormatting;

import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.BigIntSyncValue;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.ListWidget;
import com.cleanroommc.modularui.widgets.layout.Column;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import gregtech.common.tileentities.machines.multi.acal.MTEAdvancedCircuitAssemblyLine;

public class MTEAdvancedCircuitAssemblyLineGui extends MTEMultiBlockBaseGui<MTEAdvancedCircuitAssemblyLine> {

    private final IntSyncValue parallelSyncer = new IntSyncValue(multiblock::geteParallel, multiblock::seteParallel);
    private final IntSyncValue durationSyncer = new IntSyncValue(multiblock::geteDuration, multiblock::seteDuration);
    private final BigIntSyncValue finalConsumptionSync = new BigIntSyncValue(multiblock::getFinalConsumption, null);
    private final IntSyncValue mMaxProgresstimeSync = new IntSyncValue(multiblock::getMaxProgresstime);

    public MTEAdvancedCircuitAssemblyLineGui(MTEAdvancedCircuitAssemblyLine multiblock) {
        super(multiblock);
    }

    @Override
    protected void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);
        syncManager.syncValue("mMaxProgresstime", mMaxProgresstimeSync);
        syncManager.syncValue("finalConsumption", finalConsumptionSync);
        syncManager.syncValue("maximumParallels", parallelSyncer);
        syncManager.syncValue("maximumDuration", durationSyncer);
    }

    @Override
    protected Flow createButtonColumn(ModularPanel panel, PanelSyncManager syncManager) {
        return super.createButtonColumn(panel, syncManager).child(createAcalConfigButton(syncManager, panel));
    }

    protected IWidget createAcalConfigButton(PanelSyncManager syncManager, ModularPanel parent) {
        IPanelHandler acalSelectPanel = syncManager
            .panel("acalSelectPanel", (p_syncManager, syncHandler) -> openAcalConfigPanel(syncManager, parent), true);

        return new ButtonWidget<>().size(18)
            .overlay(GTGuiTextures.OVERLAY_BUTTON_BATCH_MODE_ON)
            .tooltip(t -> t.addLine(translateToLocal("GT5U.tpm.parallelwindow")))
            .onMousePressed(mouseButton -> {
                if (!acalSelectPanel.isPanelOpen()) {
                    acalSelectPanel.openPanel();
                } else {
                    acalSelectPanel.closePanel();
                }
                return true;
            });
    }

    private static final int WIDTH = 120;
    private static final int HEIGHT = 100;
    private static final int PADDING_SIDES = 4;

    private Flow buildSelect(String fieldName, IntSyncValue syncer, int defaultValue) {
        Flow holdingColumn = Flow.column()
            .size(WIDTH, HEIGHT / 2)
            .paddingTop(6);
        holdingColumn.child(
            IKey.lang(fieldName)
                .asWidget()
                .marginBottom(4));
        holdingColumn.child(
            new TextFieldWidget().setFormatAsInteger(true)
                .setNumbers(1, Integer.MAX_VALUE)
                .setTextAlignment(Alignment.CENTER)
                .setDefaultNumber(defaultValue)
                .value(syncer)
                .size(WIDTH - PADDING_SIDES * 2, 18)
                .align(Alignment.Center));
        return holdingColumn;

    }

    private ModularPanel openAcalConfigPanel(PanelSyncManager syncManager, ModularPanel parent) {
        ModularPanel panel = new ModularPanel("AcalConfigSelectPanel").size(WIDTH, HEIGHT)
            .relative(parent)
            .leftRel(1)
            .topRel(0.8f);

        Flow column = new Column().sizeRel(1);

        IntSyncValue parallelSyncer = syncManager.findSyncHandler("maximumParallels", IntSyncValue.class);
        Flow parallelSelect = buildSelect("GTPP.CC.parallel", parallelSyncer, 64);
        column.child(parallelSelect);

        IntSyncValue durationSyncer = syncManager.findSyncHandler("maximumDuration", IntSyncValue.class);
        Flow durationSelect = buildSelect("GTPP.CC.duration", durationSyncer, 70);
        column.child(durationSelect);

        panel.child(column);
        return panel;
    }

    @Override
    protected ListWidget<IWidget, ?> createTerminalTextWidget(PanelSyncManager syncManager, ModularPanel parent) {
        return super.createTerminalTextWidget(syncManager, parent).child(
            IKey.dynamic(
                () -> EnumChatFormatting.WHITE + "Energy used: "
                    + (mMaxProgresstimeSync.getValue() > 0 ? toStandardForm(
                        finalConsumptionSync.getValue()
                            .divide(BigInteger.valueOf(-mMaxProgresstimeSync.getValue())))
                        : "0")
                    + "EU"
            // enabled if active
            )
                .asWidget()
                .setEnabledIf(
                    (w) -> multiblock.getBaseMetaTileEntity()
                        .isActive()));
    }
}
