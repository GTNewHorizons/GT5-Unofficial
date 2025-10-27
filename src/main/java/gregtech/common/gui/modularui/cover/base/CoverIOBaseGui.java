package gregtech.common.gui.modularui.cover.base;

import net.minecraft.util.StatCollector;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.drawable.DynamicDrawable;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.EnumSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.layout.Grid;

import gregtech.api.modularui2.CoverGuiData;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.covers.CoverIOBase;
import gregtech.common.covers.modes.BlockMode;
import gregtech.common.covers.modes.MachineProcessingCondition;
import gregtech.common.covers.modes.TransferMode;
import gregtech.common.modularui2.widget.builder.EnumRowBuilder;

public class CoverIOBaseGui extends CoverBaseGui<CoverIOBase> {

    private final String string;

    public CoverIOBaseGui(CoverIOBase cover, String string) {
        super(cover);
        this.string = string;
    }

    @Override
    protected String getGuiId() {
        return string;
    }

    @Override
    public void addUIWidgets(PanelSyncManager syncManager, Flow column, CoverGuiData data) {
        EnumSyncValue<TransferMode> ioModeSyncValue = new EnumSyncValue<>(
            TransferMode.class,
            cover::getIOMode,
            cover::setIOMode);
        syncManager.syncValue("io_mode", ioModeSyncValue);
        IWidget exportImportButtons = new EnumRowBuilder<>(TransferMode.class).value(ioModeSyncValue)
            .overlay(GTGuiTextures.OVERLAY_BUTTON_EXPORT, GTGuiTextures.OVERLAY_BUTTON_IMPORT)
            .build();
        IWidget exportImportLabel = IKey.lang("gt.interact.desc.Pump.ExpImp")
            .asWidget();

        EnumSyncValue<MachineProcessingCondition> conditionModeSyncValue = new EnumSyncValue<>(
            MachineProcessingCondition.class,
            cover::getMachineProcessingCondition,
            cover::setMachineProcessingCondition);
        syncManager.syncValue("condition_mode", conditionModeSyncValue);
        IWidget conditionButtons = new EnumRowBuilder<>(MachineProcessingCondition.class).value(conditionModeSyncValue)
            .overlay(
                GTGuiTextures.OVERLAY_BUTTON_CHECKMARK,
                GTGuiTextures.OVERLAY_BUTTON_USE_PROCESSING_STATE,
                GTGuiTextures.OVERLAY_BUTTON_USE_INVERTED_PROCESSING_STATE)
            .build();
        IWidget conditionLabel = IKey.lang("gt.interact.desc.conveyor.Conditional")
            .asWidget();

        EnumSyncValue<BlockMode> blockModeSyncValue = new EnumSyncValue<>(
            BlockMode.class,
            cover::getBlockMode,
            cover::setBlockMode);
        syncManager.syncValue("block_mode", blockModeSyncValue);
        IWidget blockingButtons = new EnumRowBuilder<>(BlockMode.class).value(blockModeSyncValue)
            .overlay(
                new DynamicDrawable(
                    () -> ioModeSyncValue.getValue() == TransferMode.IMPORT ? GTGuiTextures.OVERLAY_BUTTON_ALLOW_INPUT
                        : GTGuiTextures.OVERLAY_BUTTON_ALLOW_OUTPUT),
                new DynamicDrawable(
                    () -> ioModeSyncValue.getValue() == TransferMode.IMPORT ? GTGuiTextures.OVERLAY_BUTTON_BLOCK_INPUT
                        : GTGuiTextures.OVERLAY_BUTTON_BLOCK_OUTPUT))
            .tooltip(
                IKey.dynamic(
                    () -> ioModeSyncValue.getValue() == TransferMode.IMPORT
                        ? StatCollector.translateToLocal("gt.interact.desc.conveyor.AllowIn")
                        : StatCollector.translateToLocal("gt.interact.desc.conveyor.AllowOut")),
                IKey.dynamic(
                    () -> ioModeSyncValue.getValue() == TransferMode.IMPORT
                        ? StatCollector.translateToLocal("gt.interact.desc.conveyor.BlockIn")
                        : StatCollector.translateToLocal("gt.interact.desc.conveyor.BlockOut")))
            .build();
        IWidget blockingLabel = IKey
            .dynamic(
                () -> ioModeSyncValue.getValue() == TransferMode.IMPORT
                    ? StatCollector.translateToLocal("gt.interact.desc.conveyor.InputBlock")
                    : StatCollector.translateToLocal("gt.interact.desc.conveyor.OutputBlock"))
            .asWidget();

        column.child(
            new Grid().marginLeft(WIDGET_MARGIN)
                .coverChildren()
                .minElementMarginRight(WIDGET_MARGIN)
                .minElementMarginBottom(2)
                .minElementMarginTop(0)
                .minElementMarginLeft(0)
                .alignment(Alignment.CenterLeft)
                .row(exportImportButtons, exportImportLabel)
                .row(conditionButtons, conditionLabel)
                .row(blockingButtons, blockingLabel));
    }
}
