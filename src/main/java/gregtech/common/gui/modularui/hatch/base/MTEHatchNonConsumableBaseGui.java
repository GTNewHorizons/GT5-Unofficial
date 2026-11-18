package gregtech.common.gui.modularui.hatch.base;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.TextWidget;
import com.cleanroommc.modularui.widgets.ToggleButton;
import com.cleanroommc.modularui.widgets.layout.Flow;

import gregtech.api.metatileentity.implementations.MTEHatchNonConsumableBase;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.modularui2.GTWidgetThemes;
import gregtech.common.gui.modularui.singleblock.base.MTEMachineWithItemScreenBaseGui;

public class MTEHatchNonConsumableBaseGui extends MTEMachineWithItemScreenBaseGui<MTEHatchNonConsumableBase> {

    public MTEHatchNonConsumableBaseGui(MTEHatchNonConsumableBase machine) {
        super(machine);
    }

    @Override
    protected boolean supportsTopRightCornerFlow() {
        return false;
    }

    @Override
    protected ParentWidget<?> createContentSection(ModularPanel panel, PanelSyncManager syncManager) {
        Flow mainRow = Flow.row()
            .coverChildren()
            .childPadding(1);

        mainRow.child(createScreen(panel, syncManager));
        mainRow.child(createScreenIO(panel, syncManager));

        return super.createContentSection(panel, syncManager).child(mainRow);
    }

    @Override
    protected int getInputSlotIndex() {
        return machine.getInputSlot();
    }

    @Override
    protected int getOutputSlotIndex() {
        return machine.getOutputSlot();
    }

    @Override
    protected int getSlotIndex() {
        return machine.getSlotIndex();
    }

    @Override
    protected ToggleButton createScreenIOWidget(ModularPanel panel, PanelSyncManager syncManager) {
        BooleanSyncValue lockOutputSyncer = new BooleanSyncValue(
            machine::isOutputSlotLocked,
            machine::setOutputSlotLocked).allowC2S();

        return new ToggleButton().value(lockOutputSyncer)
            .size(16)
            .addTooltipLine(IKey.lang("GT5U.gui.button.toggle_output_slot_lock"))
            .overlay(true, GTGuiTextures.OVERLAY_BUTTON_BLOCK_OUTPUT)
            .overlay(false, GTGuiTextures.OVERLAY_BUTTON_ALLOW_OUTPUT);
    }

    @Override
    protected TextWidget<?> createScreenAmount(ModularPanel panel, PanelSyncManager syncManager) {
        IntSyncValue itemAmountSyncer = new IntSyncValue(machine::getItemCount);
        syncManager.syncValue("itemAmount", itemAmountSyncer);

        return IKey.dynamic(() -> formatNumber(itemAmountSyncer.getIntValue()))
            .asWidget()
            .widgetTheme(GTWidgetThemes.DISPLAY_TEXT_WHITE);
    }
}
