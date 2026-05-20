package gregtech.common.gui.modularui.hatch;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.ToggleButton;
import com.cleanroommc.modularui.widgets.layout.Flow;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.util.GTUtility;
import gregtech.common.gui.modularui.hatch.base.MTEHatchBaseGui;
import gregtech.common.tileentities.machines.multi.compressor.MTEBlackHoleUtility;

public class MTEBlackHoleUtilityGui extends MTEHatchBaseGui<MTEBlackHoleUtility> {

    public MTEBlackHoleUtilityGui(MTEBlackHoleUtility hatch) {
        super(hatch);
    }

    @Override
    protected ParentWidget<?> createContentSection(ModularPanel panel, PanelSyncManager syncManager) {
        return super.createContentSection(panel, syncManager).child(createInvertButtonRow());
    }

    private Flow createInvertButtonRow() {
        BooleanSyncValue invertedSyncer = new BooleanSyncValue(machine::getMode, machine::setMode);
        return Flow.row()
            .child(
                new ToggleButton().value(invertedSyncer)
                    .overlay(true, GTGuiTextures.OVERLAY_BUTTON_REDSTONE_ON)
                    .overlay(false, GTGuiTextures.OVERLAY_BUTTON_ANALOG))
            .child(
                IKey.dynamic(
                    () -> GTUtility.translate(
                        invertedSyncer.getValue() ? "GT5U.gui.text.static_mode" : "GT5U.gui.text.pulse_mode"))
                    .asWidget())
            .coverChildren()
            .childPadding(2);
    }
}
