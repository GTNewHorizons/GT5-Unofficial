package gregtech.common.gui.modularui.hatch;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.RichTooltip;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;

import gregtech.common.gui.modularui.hatch.base.MTEHatchBaseGui;
import gregtech.common.gui.modularui.widget.settings.SettingsPanel;
import tectech.thing.metaTileEntity.hatch.bec.MTEHatchIONodeController;

public class MTEHatchIONodeControllerGui extends MTEHatchBaseGui<MTEHatchIONodeController> {

    public MTEHatchIONodeControllerGui(MTEHatchIONodeController machine) {
        super(machine);
    }

    @Override
    protected ParentWidget<?> createContentSection(ModularPanel panel, PanelSyncManager syncManager) {
        return super.createContentSection(panel, syncManager).child(
            SettingsPanel.builder()
                .addEnumCycleButton(
                    IKey.lang("GT5U.gui.text.bec-mode"),
                    MTEHatchIONodeController.Mode.class,
                    machine::getMode,
                    machine::setMode,
                    (_, _, button) -> button
                        .tooltip((MTEHatchIONodeController.Mode mode) -> new RichTooltip().add(mode.getTooltip())))
                .build(panel, syncManager, getContentHolderHeight())
                .horizontalCenter());
    }
}
