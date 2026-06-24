package gregtech.common.gui.modularui.cover;

import static gregtech.api.modularui2.GTGuiTextures.OVERLAY_BUTTON_CYCLIC;

import com.cleanroommc.modularui.value.sync.StringSyncValue;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;
import com.gtnewhorizon.gtnhlib.util.parsing.MathExpressionParser;
import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.value.sync.EnumSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.CycleButtonWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;

import gregtech.api.modularui2.CoverGuiData;
import gregtech.common.covers.CoverEUMeter;
import gregtech.common.gui.modularui.cover.base.CoverBaseGui;

public class CoverEUMeterGui extends CoverBaseGui<CoverEUMeter> {

    public CoverEUMeterGui(CoverEUMeter cover) {
        super(cover);
    }

    @Override
    public void addUIWidgets(PanelSyncManager syncManager, Flow column, CoverGuiData data) {
        column.child(
            makeRowLayout().child(positionRow(makeEnergyTypeRow()))
                .child(positionRow(RowHelper.makeInvertRedstoneRow(cover)))
                .child(positionRow(makeEnergyThresholdRow())));
    }

    private Flow makeEnergyTypeRow() {
        EnumSyncValue<CoverEUMeter.EnergyType, ?> energyTypeSyncValue = new EnumSyncValue<>(
            CoverEUMeter.EnergyType.class,
            cover::getType,
            cover::setType).allowC2S();
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
        return makeNamedColumn(IKey.lang("gt.interact.desc.EnergyThreshold"))
            .child(
                new TextFieldWidget()
                    .width(120)
                    .height(12)
                    .value(new StringSyncValue(
                        () -> String.valueOf(cover.getThreshold()),
                        s -> {
                            if (s.endsWith("%")) {
                                cover.setThresdhold((long) (MathExpressionParser.parse(s.substring(0, s.length() - 1))
                                    / 100 * cover.getType().getTileEntityEnergyCapacity(cover.getTile())
                                ));
                            } else {
                                cover.setThresdhold(((long) MathExpressionParser.parse(s)));
                            }
                        }).allowC2S())
                    .setFocusOnGuiOpen(true))
            .paddingRight(TICK_RATE_BUTTON_SIZE);
    }
}
