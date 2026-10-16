package gregtech.common.gui.modularui.hatch;

import net.minecraft.util.StatCollector;

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

import appeng.api.storage.data.IAEItemStack;
import appeng.core.localization.GuiText;
import gregtech.common.gui.modularui.hatch.base.MTEHatchBaseGui;
import gregtech.common.tileentities.machines.outputme.MTEHatchOutputBusME;
import gregtech.common.tileentities.machines.outputme.base.MTEHatchOutputMEBase;

public class MTEHatchOutputBusMEGui extends MTEHatchBaseGui<MTEHatchOutputBusME> {

    public MTEHatchOutputBusMEGui(MTEHatchOutputBusME hatch) {
        super(hatch);
    }

    @Override
    protected ParentWidget<?> createContentSection(ModularPanel panel, PanelSyncManager syncManager) {
        MTEHatchOutputMEBase<IAEItemStack> provider = machine.getProvider();
        IntSyncValue prioritySyncer = new IntSyncValue(provider::getPriority, provider::setPriority).allowC2S();
        BooleanSyncValue isCaching = new BooleanSyncValue(provider::getCacheMode, provider::setCacheMode).allowC2S();
        BooleanSyncValue isChecking = new BooleanSyncValue(provider::getCheckMode, provider::setCheckMode).allowC2S();
        // The provider stores ticks, the player sets seconds.
        IntSyncValue refreshSyncer = new IntSyncValue(
            () -> provider.getRefreshTime() / MTEHatchOutputMEBase.TICKS_PER_SECOND,
            seconds -> provider.setRefreshTime(seconds * MTEHatchOutputMEBase.TICKS_PER_SECOND)).allowC2S();

        Flow mainRow = Flow.row()
            .coverChildren()
            .verticalCenter()
            .collapseDisabledChild();

        // drive slot
        mainRow.child(new ItemSlot().slot(new ModularSlot(machine.inventoryHandler, 0).singletonSlotGroup()));

        // caching mode toggle
        mainRow.child(
            new ToggleButton().value(isCaching)
                .overlay(GuiTextures.FOLDER)
                .addTooltipLine(StatCollector.translateToLocal("GT5U.hatch.outputme.toggle_caching")));

        // priority input text field
        mainRow.child(
            new TextFieldWidget().size(60, 14)
                .formatAsInteger(true)
                .value(prioritySyncer)
                .numbersInt(Integer.MIN_VALUE, Integer.MAX_VALUE)
                .setMaxLength(10)
                .tooltip(t -> t.addLine(GuiText.Priority.getLocal()))
                .setEnabledIf(t -> isCaching.getBoolValue())
                .marginLeft(4));

        // check mode toggle
        mainRow.child(
            new ToggleButton().value(isChecking)
                .overlay(GuiTextures.SEARCH)
                .addTooltipLine(StatCollector.translateToLocal("GT5U.hatch.outputme.toggle_checking")));

        // refresh time input text field
        mainRow.child(
            new TextFieldWidget().size(40, 14)
                .formatAsInteger(true)
                .value(refreshSyncer)
                .numbersInt(
                    MTEHatchOutputMEBase.MIN_REFRESH_TIME / MTEHatchOutputMEBase.TICKS_PER_SECOND,
                    Integer.MAX_VALUE / MTEHatchOutputMEBase.TICKS_PER_SECOND)
                .setMaxLength(10)
                .tooltip(t -> {
                    t.addLine(StatCollector.translateToLocal("GT5U.hatch.outputme.refresh_time"));
                    t.addLine(
                        StatCollector.translateToLocalFormatted(
                            "GT5U.hatch.outputme.refresh_time.tooltip",
                            MTEHatchOutputMEBase.MIN_REFRESH_TIME / MTEHatchOutputMEBase.TICKS_PER_SECOND));
                })
                .marginLeft(3));

        return super.createContentSection(panel, syncManager).child(mainRow);
    }

    @Override
    protected boolean supportsBottomRowOverlap() {
        return true;
    }
}
