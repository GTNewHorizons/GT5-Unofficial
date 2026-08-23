package gregtech.common.gui.modularui.hatch.base;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.ToggleButton;
import com.cleanroommc.modularui.widgets.layout.Flow;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.modularui2.common.CommonWidgets;
import gregtech.common.tileentities.machines.multi.MTEHatchRedstoneBase;

public class MTEHatchRedstoneBaseGui extends MTEHatchBaseGui<MTEHatchRedstoneBase> {

    public MTEHatchRedstoneBaseGui(MTEHatchRedstoneBase machine) {
        super(machine);
    }

    @Override
    protected ParentWidget<?> createContentSection(ModularPanel panel, PanelSyncManager syncManager) {
        Flow column = Flow.column()
            .child(createDirectionalButtonRow())
            .childIf(
                MTEHatchRedstoneBase.supportsInvertedSignal(),
                () -> CommonWidgets
                    .createInvertButtonRow(new BooleanSyncValue(machine::isInverted, machine::setInverted).allowC2S()));
        return super.createContentSection(panel, syncManager).child(column);
    }

    protected Flow createDirectionalButtonRow() {
        BooleanSyncValue directionalSyncer = new BooleanSyncValue(machine::isDirectional, machine::setDirectional);
        return Flow.row()
            .child(
                new ToggleButton().overlay(true, GTGuiTextures.OVERLAY_BUTTON_DIRECTIONAL_ON)
                    .overlay(false, GTGuiTextures.OVERLAY_BUTTON_DIRECTIONAL_OFF)
                    .value(directionalSyncer)
                    .size(16)
                    .addTooltip(true, "GT5U.gui.button.directional_on")
                    .addTooltip(false, "GT5U.gui.button.directional_off"))
            .child(
                IKey.lang("GT5U.gui.button.directional")
                    .asWidget());
    }
}
