package gregtech.common.gui.modularui.cover.redstone;

import net.minecraft.util.StatCollector;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.EnumSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.TextWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;

import gregtech.api.modularui2.CoverGuiData;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.covers.redstone.CoverWirelessDoesWorkDetector;
import gregtech.common.gui.modularui.cover.base.CoverAdvancedRedstoneTransmitterBaseGui;
import gregtech.common.modularui2.widget.builder.EnumRowBuilder;

public class CoverWirelessDoesWorkDetectorGui
    extends CoverAdvancedRedstoneTransmitterBaseGui<CoverWirelessDoesWorkDetector> {

    public CoverWirelessDoesWorkDetectorGui(CoverWirelessDoesWorkDetector cover) {
        super(cover);
    }

    @Override
    public void addUIWidgets(PanelSyncManager syncManager, Flow column, CoverGuiData data) {
        EnumSyncValue<CoverWirelessDoesWorkDetector.ActivityMode> activityModeSync = new EnumSyncValue<>(
            CoverWirelessDoesWorkDetector.ActivityMode.class,
            cover::getMode,
            cover::setMode);
        syncManager.syncValue("activityMode", activityModeSync);

        super.addUIWidgets(syncManager, column, data);
    }

    @Override
    protected Flow makeThirdFlow(PanelSyncManager syncManager, CoverGuiData data) {
        EnumSyncValue<CoverWirelessDoesWorkDetector.ActivityMode> activityMode = (EnumSyncValue<CoverWirelessDoesWorkDetector.ActivityMode>) syncManager
            .getSyncHandlerFromMapKey("activityMode:0");
        BooleanSyncValue physicalSyncer = new BooleanSyncValue(cover::isPhysical, cover::setPhysical);
        return Flow.column()
            .coverChildren()
            .child(
                Flow.row()
                    .marginBottom(4)
                    .size(140, 18)

                    .child(
                        new EnumRowBuilder<>(CoverWirelessDoesWorkDetector.ActivityMode.class).value(activityMode)
                            .overlay(
                                GTGuiTextures.OVERLAY_BUTTON_PROGRESS,
                                GTGuiTextures.OVERLAY_BUTTON_CHECKMARK,
                                GTGuiTextures.OVERLAY_BUTTON_POWER_SWITCH_ON)
                            .build()
                            .width(120)
                            .marginRight(2)
                            .child(
                                new TextWidget<>(
                                    IKey.dynamic(
                                        () -> StatCollector.translateToLocal(
                                            activityMode.getValue()
                                                .getKey()))))))
            .child(physicalRow(physicalSyncer));
    }
}
