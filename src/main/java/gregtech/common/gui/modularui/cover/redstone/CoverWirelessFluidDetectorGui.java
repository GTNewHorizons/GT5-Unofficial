package gregtech.common.gui.modularui.cover.redstone;

import static net.minecraft.util.StatCollector.translateToLocal;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.TextWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;

import gregtech.api.modularui2.CoverGuiData;
import gregtech.common.covers.redstone.CoverAdvancedRedstoneTransmitterBase;
import gregtech.common.covers.redstone.CoverWirelessFluidDetector;
import gregtech.common.gui.modularui.cover.base.CoverAdvancedRedstoneTransmitterBaseGui;

public class CoverWirelessFluidDetectorGui extends CoverAdvancedRedstoneTransmitterBaseGui<CoverWirelessFluidDetector> {

    public CoverWirelessFluidDetectorGui(CoverAdvancedRedstoneTransmitterBase cover) {
        super(cover);
    }

    @Override
    public void addUIWidgets(PanelSyncManager syncManager, Flow column, CoverGuiData data) {
        super.addUIWidgets(syncManager, column, data);
    }

    @Override
    protected Flow makeThirdFlow(PanelSyncManager syncManager, CoverGuiData data) {
        IntSyncValue thresholdSyncer = new IntSyncValue(cover::getThreshold, cover::setThreshold);
        BooleanSyncValue physicalSyncer = new BooleanSyncValue(cover::isPhysical, cover::setPhysical);
        return Flow.column()
            .coverChildren()

            .child(
                Flow.row()
                    .height(16)
                    .marginBottom(4)

                    .child(
                        makeNumberField(88).value(thresholdSyncer)
                            .marginRight(2))
                    .child(new TextWidget(IKey.lang(translateToLocal("gt.interact.desc.fluidthreshold")))))
            .child(physicalRow(physicalSyncer));
    }

}
