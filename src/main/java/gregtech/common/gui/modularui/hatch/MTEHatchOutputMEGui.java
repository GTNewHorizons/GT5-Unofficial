package gregtech.common.gui.modularui.hatch;

import static net.minecraft.util.StatCollector.translateToLocal;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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
import com.glodblock.github.common.item.FCBaseItemCell;
import com.glodblock.github.common.item.ItemFluidVoidStorageCell;

import appeng.api.storage.data.IAEFluidStack;
import appeng.core.localization.GuiText;
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
        MTEHatchOutputMEBase<IAEFluidStack, MEFilterFluid, FluidStack> provider = hatch.getProvider();
        IntSyncValue prioritySyncer = new IntSyncValue(provider::getPriority, provider::setPriority);
        BooleanSyncValue isCaching = new BooleanSyncValue(provider::getCacheMode, provider::setCacheMode);
        BooleanSyncValue isChecking = new BooleanSyncValue(provider::getCheckMode, provider::setCheckMode);

        Flow mainRow = Flow.row()
            .full()
            .childPadding(5);

        // cell slot
        mainRow.child(
            new ItemSlot().slot(
                new ModularSlot(hatch.inventoryHandler, 0).singletonSlotGroup()
                    .filter(this::isFluidCell))
                .marginLeft(3));

        // check mode toggle
        mainRow.child(
            new ToggleButton().value(isChecking)
                .overlay(GuiTextures.SEARCH)
                .tooltip(t -> t.addLine(translateToLocal("GT5U.hatch.outputme.toggle_checking"))));

        // caching mode toggle
        mainRow.child(
            new ToggleButton().value(isCaching)
                .overlay(GuiTextures.FOLDER)
                .tooltip(t -> t.addLine(translateToLocal("GT5U.hatch.outputme.toggle_caching"))));

        // priority input text field
        mainRow.child(
            new TextFieldWidget().size(75, 14)
                .setFormatAsInteger(true)
                .value(prioritySyncer)
                .setNumbers(1, Integer.MAX_VALUE)
                .setMaxLength(10)
                .tooltip(t -> t.addLine(GuiText.Priority.getLocal()))
                .setEnabledIf(t -> isCaching.getBoolValue()));

        return super.createContentSection(panel, syncManager).child(mainRow);
    }

    private boolean isFluidCell(ItemStack itemStack) {
        Item item = itemStack.getItem();
        return item instanceof FCBaseItemCell || item instanceof ItemFluidVoidStorageCell;
    }
}
