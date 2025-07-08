package gregtech.common.covers.gui.redstone;

import static net.minecraft.util.StatCollector.translateToLocal;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.factory.GuiData;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.TextWidget;
import com.cleanroommc.modularui.widgets.ToggleButton;
import com.cleanroommc.modularui.widgets.layout.Flow;

import gregtech.api.modularui2.GTGuiTextures;
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
        BooleanSyncValue physicalSyncer = new BooleanSyncValue(cover::isPhysical, cover::setPhysical);
        return Flow.column()
            .coverChildren()
            .height(120)
            .child(
                Flow.row()
                    .coverChildren()
                    .height(16)
                    .child(
                        makeNumberField(88).value(thresholdSyncer)
                            .marginRight(2)
                            .setNumbers(0, Integer.MAX_VALUE))
                    .child(new TextWidget(IKey.lang(translateToLocal("gt.interact.desc.fluidthreshold"))))
                    .marginBottom(8))
            .child(
                Flow.row()
                    .size(160, 16)
                    .child(
                        new ToggleButton().marginRight(2)
                            .value(physicalSyncer)
                            .overlay(true, GTGuiTextures.OVERLAY_BUTTON_CHECKMARK))
                    .child(
                        new TextWidget(
                            IKey.dynamic(
                                () -> translateToLocal(
                                    physicalSyncer.getValue() ? "gt.cover.wirelessdetector.redstone.1"
                                        : "gt.cover.wirelessdetector.redstone.0"))).height(16)));
    }

}
