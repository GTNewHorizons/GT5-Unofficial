package gregtech.common.gui.modularui.singleblock;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.drawable.DynamicDrawable;
import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.modularui2.GTGuis;
import gregtech.api.util.GTUtility;
import gregtech.common.tileentities.machines.basic.MTEMicrowaveEnergyTransmitter;

public class MTEMicrowaveEnergyTransmitterGui {

    MTEMicrowaveEnergyTransmitter base;

    public MTEMicrowaveEnergyTransmitterGui(MTEMicrowaveEnergyTransmitter base) {
        this.base = base;
    }

    // credit to purebluez
    public ModularPanel build(PosGuiData data, PanelSyncManager syncManager, UISettings uiSettings) {
        return GTGuis.mteTemplatePanelBuilder(base, data, syncManager, uiSettings)
            .doesAddGregTechLogo(false)
            .build()
            .child(
                Flow.row()
                    .child(
                        Flow.column()
                            .child(
                                GTGuiTextures.OVERLAY_BUTTON_BOUNDING_BOX.asWidget()
                                    .size(18, 18)
                                    .topRel(0.5F))
                            .heightRel(1)
                            .coverChildrenWidth())
                    .child(createSelectionColumn())
                    .crossAxisAlignment(Alignment.CrossAxis.START)
                    .pos(4, 6)
                    .coverChildren());
    }

    public Flow createSelectionColumn() {
        return Flow.column()
            .child(
                Flow.row()
                    .child(
                        new TextFieldWidget().setFormatAsInteger(true)
                            .value(new IntSyncValue(() -> base.mTargetX, i -> base.mTargetX = i))
                            .size(77, 12)
                            .margin(2, 0))
                    .child(
                        IKey.lang("GT5U.gui.text.microwave_energy_transmitter.x")
                            .asWidget())
                    .coverChildren())
            .child(
                Flow.row()
                    .child(
                        new TextFieldWidget().setFormatAsInteger(true)
                            .value(new IntSyncValue(() -> base.mTargetY, i -> base.mTargetY = i))
                            .size(77, 12)
                            .margin(2, 0))
                    .child(
                        IKey.lang("GT5U.gui.text.microwave_energy_transmitter.y")
                            .asWidget())
                    .coverChildren())
            .child(
                Flow.row()
                    .child(
                        new TextFieldWidget().setFormatAsInteger(true)
                            .value(new IntSyncValue(() -> base.mTargetZ, i -> base.mTargetZ = i))
                            .size(77, 12)
                            .margin(2, 0))
                    .child(
                        IKey.lang("GT5U.gui.text.microwave_energy_transmitter.z")
                            .asWidget())
                    .coverChildren())
            .child(
                Flow.row()
                    .child(
                        new TextFieldWidget().setFormatAsInteger(true)
                            .value(new IntSyncValue(() -> base.mTargetD, i -> base.mTargetD = i))
                            .size(77, 12)
                            .margin(2, 0))
                    .child(
                        IKey.lang("GT5U.gui.text.microwave_energy_transmitter.d")
                            .asWidget())
                    .child(
                        new DynamicDrawable(
                            () -> GTUtility.isRealDimension(base.mTargetD) ? GTGuiTextures.OVERLAY_BUTTON_CHECKMARK
                                : GTGuiTextures.OVERLAY_BUTTON_CROSS).asWidget()
                                    .size(16, 16))
                    .coverChildren())
            .crossAxisAlignment(Alignment.CrossAxis.START)
            .childPadding(2)
            .coverChildren();
    }
}
