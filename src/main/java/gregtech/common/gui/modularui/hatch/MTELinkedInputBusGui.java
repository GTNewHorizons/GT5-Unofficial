package gregtech.common.gui.modularui.hatch;

import net.minecraft.util.StatCollector;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.value.sync.StringSyncValue;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.ToggleButton;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;

import ggfab.mte.MTELinkedInputBus;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.gui.modularui.hatch.base.MTEHatchBaseGui;
import gregtech.common.gui.modularui.synchandler.NBTSerializableSyncHandler;
import gregtech.common.modularui2.widget.builder.ItemSlotGridBuilder;

public class MTELinkedInputBusGui extends MTEHatchBaseGui<MTELinkedInputBus> {

    private static final int SLOT_ROW = 2;
    private static final int SLOT_PER_ROW = 9;

    public MTELinkedInputBusGui(MTELinkedInputBus hatch) {
        super(hatch);
    }

    @Override
    protected ParentWidget<?> createContentSection(ModularPanel panel, PanelSyncManager syncManager) {
        StringSyncValue channelSyncer = new StringSyncValue(machine::getChannel, machine::setChannel).allowC2S();
        BooleanSyncValue isPrivateSyncer = new BooleanSyncValue(machine::isPrivate, machine::setPrivate).allowC2S();

        Flow mainColumn = Flow.column()
            .coverChildren()
            .childPadding(3);

        Flow inputRow = Flow.row()
            .coverChildren()
            .childPadding(3);

        // channel label
        inputRow.child(
            IKey.lang("ggfab.gui.linked_input_bus.channel")
                .asWidget());

        // channel input field
        inputRow.child(
            new TextFieldWidget().value(channelSyncer)
                .width(60)
                .addTooltipLine(StatCollector.translateToLocal("ggfab.tooltip.linked_input_bus.change_freq_warn")));

        // private label
        inputRow.child(
            IKey.lang("ggfab.gui.linked_input_bus.private")
                .asWidget());

        // private toggle button
        inputRow.child(
            new ToggleButton().value(isPrivateSyncer)
                .overlay(true, GTGuiTextures.OVERLAY_BUTTON_CHECKMARK)
                .overlay(false, GTGuiTextures.OVERLAY_BUTTON_CROSS)
                .addTooltipLine(StatCollector.translateToLocal("ggfab.tooltip.linked_input_bus.private")));

        mainColumn.child(inputRow);

        // inventory slots
        mainColumn.child(
            new ItemSlotGridBuilder(machine.getHandler(), syncManager).size(SLOT_PER_ROW, SLOT_ROW)
                .filter(
                    _ -> !channelSyncer.getStringValue()
                        .isEmpty())
                .build());

        return super.createContentSection(panel, syncManager).child(mainColumn);
    }

    @Override
    public void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);

        syncManager.syncValue("handler", new NBTSerializableSyncHandler<>(machine::getHandler));
    }

    @Override
    protected int getBasePanelHeight() {
        return super.getBasePanelHeight() + 3;
    }
}
