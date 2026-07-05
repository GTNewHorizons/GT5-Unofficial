package gregtech.common.gui.modularui.singleblock;

import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.Widget;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;

import bartworks.common.tileentities.tiered.MTEBioLab;
import gregtech.api.recipe.BasicUIProperties;
import gregtech.common.gui.modularui.singleblock.base.MTEBasicMachineBaseGui;

public class MTEBioLabGui extends MTEBasicMachineBaseGui<MTEBioLab> {

    public MTEBioLabGui(MTEBioLab machine, BasicUIProperties properties) {
        super(machine, properties);
    }

    @Override
    public ModularPanel build(PosGuiData guiData, PanelSyncManager syncManager, UISettings uiSettings) {
        registerSyncValues(syncManager);

        ModularPanel panel = this.getBasePanel(guiData, syncManager, uiSettings);

        return panel.child(
            Flow.column()
                .full()
                .padding(borderRadius)
                .childIf(this.supportsTopRightCornerFlow(), this::createTopRightCornerFlow)
                .child(createContentHolder(panel, syncManager))
                .childIf(machine.supportsInventoryRow(), () -> createInventoryRow(panel, syncManager)));
    }

    @Override
    protected Widget<? extends Widget<?>> makeLogoWidget() {
        return super.makeLogoWidget().size(47, 20);
    }

    @Override
    protected Flow createBottomRightCornerFlow(ModularPanel panel, PanelSyncManager syncManager) {
        return Flow.row()
            .coverChildren()
            .verticalCenter()
            .rightRel(0)
            .child(createSpecialSlot());
    }

    @Override
    protected ItemSlot createSpecialSlot() {
        return super.createSpecialSlot().marginRight(SLOT_SIZE * 3 / 2);
    }

    @Override
    protected Flow createTopRightCornerFlow() {
        return Flow.row()
            .coverChildren()
            .crossAxisAlignment(Alignment.CrossAxis.START)
            .topRel(0)
            .rightRel(0)
            .child(makeLogoWidget())
            .child(
                Flow.column()
                    .coverChildren()
                    .child(createMufflerButton())
                    .child(createPowerSwitchButton()));
    }
}
