package gregtech.common.covers.gui.redstone;

import static net.minecraft.util.StatCollector.translateToLocal;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.factory.GuiData;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.TextWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;

import gregtech.common.covers.redstone.CoverAdvancedRedstoneTransmitterBase;
import gregtech.common.covers.redstone.CoverWirelessFluidDetector;

public class CoverWirelessFluidDetectorGui extends CoverAdvancedRedstoneTransmitterBaseGui<CoverWirelessFluidDetector> {

    public CoverWirelessFluidDetectorGui(CoverAdvancedRedstoneTransmitterBase cover) {
        super(cover);
    }

    @Override
    public void addUIWidgets(PanelSyncManager syncManager, Flow column, GuiData data) {
        super.addUIWidgets(syncManager, column, data);
    }

    @Override
    protected Flow makeThirdRow(PanelSyncManager syncManager) {
        IntSyncValue thresholdSyncer = new IntSyncValue(cover::getThreshold, cover::setThreshold);
        return Flow.row()
            .size(140, 18)
            .child(
                makeNumberField(88).value(thresholdSyncer)
                    .marginRight(2))
            .child(new TextWidget(IKey.lang(translateToLocal("gt.interact.desc.fluidthreshold"))));
    }

}
