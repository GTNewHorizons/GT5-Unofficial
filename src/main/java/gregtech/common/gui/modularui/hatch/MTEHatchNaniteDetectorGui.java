package gregtech.common.gui.modularui.hatch;

import java.util.Arrays;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.EnumSyncValue;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;

import gregtech.api.enums.ComparisonWithAnalog;
import gregtech.api.enums.NaniteTier;
import gregtech.api.util.GTDataUtils;
import gregtech.common.gui.modularui.hatch.base.MTEHatchBaseGui;
import gregtech.common.gui.modularui.widget.DisableableTextFieldWidget;
import gregtech.common.gui.modularui.widget.settings.SettingsPanel;
import tectech.thing.metaTileEntity.hatch.bec.MTEHatchNaniteDetector;

public class MTEHatchNaniteDetectorGui extends MTEHatchBaseGui<MTEHatchNaniteDetector> {

    public MTEHatchNaniteDetectorGui(MTEHatchNaniteDetector machine) {
        super(machine);
    }

    protected ParentWidget<?> createContentSection(ModularPanel panel, PanelSyncManager syncManager) {
        EnumSyncValue<ComparisonWithAnalog, ?> comparisonSyncer = new EnumSyncValue<>(
            ComparisonWithAnalog.class,
            machine::getComparison,
            machine::setComparison).allowC2S();

        return super.createContentSection(panel, syncManager).child(
            SettingsPanel.builder()
                .addEnumCycleButton(
                    IKey.lang("GT5U.gui.text.bec-operation"),
                    ComparisonWithAnalog.class,
                    comparisonSyncer)
                .addIntEditor(IKey.lang("GT5U.gui.text.bec-threshold"), machine::getConfiguredTier, i -> {
                    if (comparisonSyncer.getValue() != ComparisonWithAnalog.ANALOG) {
                        machine.setConfiguredTier(i);
                    }
                },
                    i -> Math.clamp(
                        i,
                        1,
                        Arrays.stream(NaniteTier.values())
                            .mapToInt(NaniteTier::getTier)
                            .max()
                            .getAsInt()),
                    (_, _, textField) -> textField
                        .setEditable(() -> comparisonSyncer.getValue() != ComparisonWithAnalog.ANALOG),
                    DisableableTextFieldWidget::new)
                .addReadout(
                    IKey.lang("GT5U.gui.text.bec-current"),
                    new IntSyncValue(
                        () -> machine.getRequiredTier() == null ? -1
                            : machine.getRequiredTier()
                                .ordinal()),
                    nanite -> {
                        NaniteTier tier = GTDataUtils.getIndexSafe(NaniteTier.values(), nanite);

                        if (tier == null) {
                            return IKey.lang("GT5U.gui.text.nil");
                        } else {
                            return IKey.lang("GT5U.gui.text.nanite-detector-tier", tier.tier);
                        }
                    })
                .build(panel, syncManager, getContentHolderHeight())
                .horizontalCenter());
    }
}
