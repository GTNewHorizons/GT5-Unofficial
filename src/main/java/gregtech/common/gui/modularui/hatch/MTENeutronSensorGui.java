package gregtech.common.gui.modularui.hatch;

import static net.minecraft.util.StatCollector.translateToLocal;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.ToggleButton;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;

import goodgenerator.blocks.tileEntity.GTMetaTileEntity.MTENeutronSensor;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.modularui2.GTGuis;

public class MTENeutronSensorGui {

    private final MTENeutronSensor sensor;

    public MTENeutronSensorGui(MTENeutronSensor sensor) {
        this.sensor = sensor;
    }

    public ModularPanel build(PosGuiData data, PanelSyncManager syncManager, UISettings uiSettings) {
        return GTGuis.mteTemplatePanelBuilder(sensor, data, syncManager, uiSettings)
            .build()
            .child(
                Flow.column()
                    .child(createInvertButtonRow())
                    .child(createThresholdFieldRow())
                    .coverChildren()
                    .crossAxisAlignment(com.cleanroommc.modularui.utils.Alignment.CrossAxis.START)
                    .childPadding(2)
                    .pos(8, 6));
    }

    public Flow createInvertButtonRow() {
        BooleanSyncValue invertedSyncer = new BooleanSyncValue(sensor::isInverted, sensor::setInverted);
        return Flow.row()
            .child(
                new ToggleButton().value(invertedSyncer)
                    .overlay(true, GTGuiTextures.OVERLAY_BUTTON_REDSTONE_ON)
                    .overlay(false, GTGuiTextures.OVERLAY_BUTTON_REDSTONE_OFF)
                    .size(16, 16))
            .child(
                IKey.dynamic(
                    () -> invertedSyncer.getValue() ? translateToLocal("gt.interact.desc.inverted")
                        : translateToLocal("gt.interact.desc.normal"))
                    .asWidget())
            .coverChildren()
            .childPadding(2);
    }

    public Flow createThresholdFieldRow() {
        return Flow.row()
            .child(
                new TextFieldWidget().setFormatAsInteger(true)
                    .setNumbers(0, 1200000000)
                    .size(77, 12)
                    .value(new IntSyncValue(sensor::getThreshold, sensor::setThreshold))
                    .setFocusOnGuiOpen(true))
            .child(
                IKey.lang("gui.NeutronSensor.4")
                    .asWidget())
            .coverChildren()
            .childPadding(2);
    }
}
