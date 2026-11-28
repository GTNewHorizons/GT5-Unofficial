package gregtech.common.hydrodam;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.LongSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil;

import gregtech.api.enums.Comparison;
import gregtech.common.gui.modularui.hatch.base.MTEHatchBaseGui;
import gregtech.common.gui.modularui.widget.settings.SettingsPanel;

/// Configures the comparison and threshold of a {@link MTEHatchDamWaterLevelSensor}, and shows the dam's litres.
public class MTEHatchDamWaterLevelSensorGui extends MTEHatchBaseGui<MTEHatchDamWaterLevelSensor> {

    public MTEHatchDamWaterLevelSensorGui(MTEHatchDamWaterLevelSensor machine) {
        super(machine);
    }

    @Override
    protected ParentWidget<?> createContentSection(ModularPanel panel, PanelSyncManager syncManager) {
        return super.createContentSection(panel, syncManager).child(
            SettingsPanel.builder()
                .addEnumCycleButton(
                    IKey.lang("GT5U.gui.text.bec-operation"),
                    Comparison.class,
                    machine::getComparison,
                    v -> {
                        machine.setComparison(v);
                        machine.refreshOutput();
                    })
                .addLongEditor(IKey.lang("GT5U.gui.text.bec-threshold"), machine::getThreshold, l -> {
                    machine.setThreshold(l);
                    machine.refreshOutput();
                }, l -> Math.clamp(l, 0L, Long.MAX_VALUE))
                .addReadout(
                    IKey.lang("GT5U.gui.text.bec-current"),
                    new LongSyncValue(machine::getLitres),
                    litres -> litres == MTEHatchDamWaterLevelSensor.NO_DAM ? IKey.lang("GT5U.gui.text.nil")
                        : IKey.str(NumberFormatUtil.formatFluid(litres)))
                .build(panel, syncManager, getContentHolderHeight())
                .horizontalCenter());
    }
}
