package gregtech.common.covers.gui;

import static gregtech.api.modularui2.GTGuiTextures.OVERLAY_BUTTON_CYCLIC;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.value.sync.EnumSyncValue;
import com.cleanroommc.modularui.value.sync.LongSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.CycleButtonWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;

import gregtech.api.util.GTUtility;
import gregtech.common.covers.CoverEUMeter;

public class CoverEUMeterGui extends CoverGui<CoverEUMeter> {

    public CoverEUMeterGui(CoverEUMeter cover) {
        super(cover);
    }

    @Override
    public void addUIWidgets(PanelSyncManager syncManager, Flow column) {
        column.child(
            makeRowLayout().child(positionRow(makeEnergyTypeRow()))
                .child(positionRow(Rows.makeInvertRedstoneRow(cover)))
                .child(positionRow(makeEnergyThresholdRow())));
    }

    private Flow makeEnergyTypeRow() {
        EnumSyncValue<CoverEUMeter.EnergyType> energyTypeSyncValue = new EnumSyncValue<>(
            CoverEUMeter.EnergyType.class,
            cover::getType,
            cover::setType);
        return Flow.row()
            .child(
                addEnergyTypeTooltips(
                    new CycleButtonWidget().overlay(OVERLAY_BUTTON_CYCLIC)
                        .value(energyTypeSyncValue)
                        .size(16)))
            .child(
                IKey.dynamic(
                    () -> cover.getType()
                        .getTitle())
                    .asWidget());
    }

    private CycleButtonWidget addEnergyTypeTooltips(CycleButtonWidget button) {
        for (CoverEUMeter.EnergyType energyType : CoverEUMeter.EnergyType.values()) {
            button.addTooltip(energyType.ordinal(), energyType.getTooltip());
        }
        return button;
    }

    private @NotNull Flow makeEnergyThresholdRow() {
        return makeNamedColumn(IKey.str(GTUtility.trans("222.1", "Energy threshold"))).child(
            makeNumberField(140).value(new LongSyncValue(cover::getThreshold, cover::setThresdhold))
                .setNumbersLong(
                    () -> 0L,
                    () -> cover.getType()
                        .getTileEntityEnergyCapacity(cover.getTile()))
                .setFocusOnGuiOpen(true));
    }
}
