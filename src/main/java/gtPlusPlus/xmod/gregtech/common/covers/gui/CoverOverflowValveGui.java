package gtPlusPlus.xmod.gregtech.common.covers.gui;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.value.sync.EnumSyncValue;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.layout.Flow;

import gregtech.api.modularui2.CoverGuiData;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.covers.modes.BlockMode;
import gregtech.common.gui.modularui.cover.base.CoverBaseGui;
import gregtech.common.modularui2.widget.builder.EnumRowBuilder;
import gtPlusPlus.xmod.gregtech.common.covers.CoverOverflowValve;

public class CoverOverflowValveGui extends CoverBaseGui<CoverOverflowValve> {

    public CoverOverflowValveGui(CoverOverflowValve cover) {
        super(cover);
    }

    @Override
    public void addUIWidgets(PanelSyncManager syncManager, Flow column, CoverGuiData data) {
        column.child(
            makeRowLayout()
                .child(
                    makeNamedColumn(IKey.lang("GTPP.gui.text.cover_overflow_valve_overflow_point"))
                        .child(positionRow(makeOverflowPointRow())))
                .child(
                    makeNamedColumn(IKey.lang("GTPP.gui.text.cover_overflow_valve_voiding_rate"))
                        .child(positionRow(makeVoidingRateRow())))
                .child(positionRow(makeIoRow(syncManager))));
    }

    private Flow makeOverflowPointRow() {
        return Flow.row()
            .child(
                makeNumberField().value(new IntSyncValue(cover::getOverflowPoint, cover::setOverflowPoint))
                    .setNumbers(cover::getMinOverflowPoint, cover::getMaxOverflowPoint))
            .child(
                IKey.lang("GTPP.gui.text.cover_overflow_valve_liter")
                    .asWidget());
    }

    private Flow makeVoidingRateRow() {
        return Flow.row()
            .child(
                makeNumberField().value(new IntSyncValue(cover::getVoidingRate, cover::setVoidingRate))
                    .setNumbers(cover::getMinOverflowPoint, cover::getMaxOverflowPoint)
                    .setFocusOnGuiOpen(true))
            .child(
                IKey.lang("GTPP.gui.text.cover_overflow_valve_l_per_update")
                    .asWidget());
    }

    private Flow makeIoRow(PanelSyncManager syncManager) {
        EnumSyncValue<BlockMode> outputModeSyncValue = new EnumSyncValue<>(
            BlockMode.class,
            cover::getFluidOutputMode,
            cover::setFluidOutputMode);
        syncManager.syncValue("output_mode", outputModeSyncValue);
        EnumSyncValue<BlockMode> inputModeSyncValue = new EnumSyncValue<>(
            BlockMode.class,
            cover::getFluidInputMode,
            cover::setFluidInputMode);
        syncManager.syncValue("input_mode", inputModeSyncValue);
        return Flow.row()
            .child(
                new EnumRowBuilder<>(BlockMode.class).value(outputModeSyncValue)
                    .overlay(GTGuiTextures.OVERLAY_BUTTON_ALLOW_OUTPUT, GTGuiTextures.OVERLAY_BUTTON_BLOCK_OUTPUT)
                    .tooltip(
                        IKey.lang("GTPP.gui.text.cover_overflow_valve_allow_fluid_output"),
                        IKey.lang("GTPP.gui.text.cover_overflow_valve_block_fluid_output"))
                    .build())
            .child(
                new EnumRowBuilder<>(BlockMode.class).value(inputModeSyncValue)
                    .overlay(GTGuiTextures.OVERLAY_BUTTON_ALLOW_INPUT, GTGuiTextures.OVERLAY_BUTTON_BLOCK_INPUT)
                    .tooltip(
                        IKey.lang("GTPP.gui.text.cover_overflow_valve_allow_fluid_input"),
                        IKey.lang("GTPP.gui.text.cover_overflow_valve_block_fluid_input"))
                    .build());
    }

}
