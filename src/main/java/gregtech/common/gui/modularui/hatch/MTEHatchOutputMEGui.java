package gregtech.common.gui.modularui.hatch;

import net.minecraftforge.fluids.FluidStack;

import com.cleanroommc.modularui.drawable.GuiTextures;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.ToggleButton;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;

import appeng.api.storage.data.IAEFluidStack;
import appeng.core.localization.GuiText;
import gregtech.api.util.GTUtility;
import gregtech.common.gui.modularui.hatch.base.MTEHatchBaseGui;
import gregtech.common.tileentities.machines.outputme.MTEHatchOutputME;
import gregtech.common.tileentities.machines.outputme.base.MTEHatchOutputMEBase;
import gregtech.common.tileentities.machines.outputme.filter.MEFilterFluid;

public class MTEHatchOutputMEGui extends MTEHatchBaseGui<MTEHatchOutputME> {

    public MTEHatchOutputMEGui(MTEHatchOutputME hatch) {
        super(hatch);
    }

    @Override
    protected ParentWidget<?> createContentSection(ModularPanel panel, PanelSyncManager syncManager) {
        MTEHatchOutputMEBase<IAEFluidStack, MEFilterFluid, FluidStack> provider = machine.getProvider();
        IntSyncValue prioritySyncer = new IntSyncValue(provider::getPriority, provider::setPriority).allowC2S();
        BooleanSyncValue isCaching = new BooleanSyncValue(provider::getCacheMode, provider::setCacheMode).allowC2S();
        BooleanSyncValue isChecking = new BooleanSyncValue(provider::getCheckMode, provider::setCheckMode).allowC2S();

        Flow mainRow = Flow.row()
            .coverChildren()
            .verticalCenter();

        // cell slot
        mainRow.child(new ItemSlot().slot(new ModularSlot(machine.inventoryHandler, 0).singletonSlotGroup()));

        // check mode toggle
        mainRow.child(
            new ToggleButton().value(isChecking)
                .overlay(GuiTextures.SEARCH)
                .addTooltipLine(GTUtility.translate("GT5U.hatch.outputme.toggle_checking"))
                .setEnabledIf(t -> isCaching.getBoolValue()));

        // caching mode toggle
        mainRow.child(
            new ToggleButton().value(isCaching)
                .overlay(GuiTextures.FOLDER)
                .addTooltipLine(GTUtility.translate("GT5U.hatch.outputme.toggle_caching")));

        // priority input text field
        mainRow.child(
            new TextFieldWidget().size(75, 14)
                .formatAsInteger(true)
                .value(prioritySyncer)
                .numbersInt(1, Integer.MAX_VALUE)
                .setMaxLength(10)
                .tooltip(t -> t.addLine(GuiText.Priority.getLocal()))
                .setEnabledIf(t -> isCaching.getBoolValue())
                .marginLeft(5));

        return super.createContentSection(panel, syncManager).child(mainRow);
    }

    @Override
    protected boolean supportsBottomRowOverlap() {
        return true;
    }
}
