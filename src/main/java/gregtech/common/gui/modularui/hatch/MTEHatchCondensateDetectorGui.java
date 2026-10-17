package gregtech.common.gui.modularui.hatch;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.LongSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil;

import gregtech.api.enums.Comparison;
import gregtech.common.gui.modularui.hatch.base.MTEHatchBaseGui;
import gregtech.common.gui.modularui.widget.settings.SettingsPanel;
import tectech.thing.metaTileEntity.hatch.bec.MTEHatchCondensateDetector;

public class MTEHatchCondensateDetectorGui extends MTEHatchBaseGui<MTEHatchCondensateDetector> {

    public MTEHatchCondensateDetectorGui(MTEHatchCondensateDetector machine) {
        super(machine);
    }

    @Override
    protected boolean supportsBottomRowOverlap() {
        return true;
    }

    @Override
    protected int getBasePanelHeight() {
        return super.getBasePanelHeight() + SLOT_SIZE / 2;
    }

    @Override
    protected ParentWidget<?> createContentSection(ModularPanel panel, PanelSyncManager syncManager) {
        return super.createContentSection(panel, syncManager).child(
            SettingsPanel.builder()
                .addPhantomFluidSlot(IKey.lang("GT5U.gui.text.bec-filter"), machine::getCondensateFilter, f -> {
                    machine.setCondensateFilter(f);
                    machine.refreshOutput();
                })
                .addEnumCycleButton(
                    IKey.lang("GT5U.gui.text.bec-operation"),
                    Comparison.class,
                    machine::getComparison,
                    v -> {
                        machine.setComparison(v);
                        machine.refreshOutput();
                    })
                .addLongEditor(IKey.lang("GT5U.gui.text.bec-threshold"), machine::getRequestedAmount, l -> {
                    machine.setRequestedAmount(l);
                    machine.refreshOutput();
                }, l -> Math.clamp(l, 1L, Long.MAX_VALUE))
                .addReadout(
                    IKey.lang("GT5U.gui.text.bec-current"),
                    new LongSyncValue(machine::getActualAmount),
                    amount -> IKey.str(NumberFormatUtil.formatFluid(amount)))
                .build(panel, syncManager, getContentHolderHeight())
                .horizontalCenter());
    }
}
