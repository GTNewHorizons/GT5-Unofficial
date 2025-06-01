package gregtech.common.covers.gui;

import static net.minecraft.util.StatCollector.translateToLocal;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.BinaryEnumSyncValue;
import com.cleanroommc.modularui.value.sync.EnumSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.ToggleButton;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.layout.Grid;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.util.GTUtility;
import gregtech.common.covers.CoverDoesWork;
import gregtech.common.covers.modes.DetectionMode;
import gregtech.common.covers.modes.RedstoneMode;
import gregtech.common.modularui2.widget.builder.EnumRowBuilder;

public class CoverDoesWorkGui extends CoverGui<CoverDoesWork> {

    public CoverDoesWorkGui(CoverDoesWork cover) {
        super(cover);
    }

    @Override
    protected String getGuiId() {
        return "cover.activity_detector";
    }

    @Override
    public void addUIWidgets(PanelSyncManager syncManager, Flow column) {
        EnumSyncValue<DetectionMode> detectionModeSyncValue = new EnumSyncValue<>(
            DetectionMode.class,
            cover::getDetectionMode,
            cover::setDetectionMode);
        syncManager.syncValue("detection_mode", detectionModeSyncValue);
        BinaryEnumSyncValue<RedstoneMode> redstoneModeSyncValue = new BinaryEnumSyncValue<>(
            RedstoneMode.class,
            cover::getRedstoneMode,
            cover::setRedstoneMode);

        column.child(
            new Grid().marginLeft(WIDGET_MARGIN)
                .coverChildren()
                .minElementMarginRight(WIDGET_MARGIN)
                .minElementMarginBottom(2)
                .minElementMarginTop(0)
                .minElementMarginLeft(0)
                .alignment(Alignment.CenterLeft)
                .row(
                    new EnumRowBuilder<>(DetectionMode.class).value(detectionModeSyncValue)
                        .overlay(
                            GTGuiTextures.OVERLAY_BUTTON_PROGRESS,
                            GTGuiTextures.OVERLAY_BUTTON_CHECKMARK,
                            GTGuiTextures.OVERLAY_BUTTON_POWER_SWITCH_ON)
                        .build(),
                    IKey.dynamic(() -> {
                        DetectionMode mode = detectionModeSyncValue.getValue();
                        if (mode == DetectionMode.MACHINE_ENABLED) {
                            return GTUtility.trans("271", "Machine enabled");
                        } else if (mode == DetectionMode.MACHINE_IDLE) {
                            return GTUtility.trans("242", "Machine idle");
                        } else {
                            return GTUtility.trans("241", "Recipe progress");
                        }
                    })
                        .asWidget())
                .row(
                    new ToggleButton().value(redstoneModeSyncValue)
                        .overlay(true, GTGuiTextures.OVERLAY_BUTTON_REDSTONE_ON)
                        .overlay(false, GTGuiTextures.OVERLAY_BUTTON_REDSTONE_OFF)
                        .size(16),
                    IKey.dynamic(
                        () -> redstoneModeSyncValue.getValue() == RedstoneMode.INVERTED
                            ? translateToLocal("gt.interact.desc.inverted")
                            : translateToLocal("gt.interact.desc.normal"))
                        .asWidget()));
    }

}
