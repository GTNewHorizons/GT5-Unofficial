package gregtech.common.gui.modularui.hatch;

import static net.minecraft.util.StatCollector.translateToLocal;

import java.util.Arrays;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.value.sync.StringSyncValue;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.SlotGroupWidget;
import com.cleanroommc.modularui.widgets.ToggleButton;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;

import ggfab.mte.MTELinkedInputBus;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.util.StringUtils;
import gregtech.common.gui.modularui.hatch.base.MTEHatchBaseGui;
import gregtech.common.gui.modularui.synchandler.NBTSerializableSyncHandler;

public class MTELinkedInputBusGui extends MTEHatchBaseGui<MTELinkedInputBus> {

    private static final int SLOT_ROW = 2;
    private static final int SLOT_PER_ROW = 9;

    public MTELinkedInputBusGui(MTELinkedInputBus hatch) {
        super(hatch);
    }

    @Override
    protected ParentWidget<?> createContentSection(ModularPanel panel, PanelSyncManager syncManager) {
        StringSyncValue channelSyncer = new StringSyncValue(hatch::getChannel, hatch::setChannel);
        BooleanSyncValue isPrivateSyncer = new BooleanSyncValue(hatch::isPrivate, hatch::setPrivate);

        Flow mainColumn = Flow.column()
            .coverChildren()
            .childPadding(3)
            .paddingLeft(3);

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
                .tooltip(t -> t.addLine(translateToLocal("ggfab.tooltip.linked_input_bus.change_freq_warn"))));

        // private label
        inputRow.child(
            IKey.lang("ggfab.gui.linked_input_bus.private")
                .asWidget());

        // private toggle button
        inputRow.child(
            new ToggleButton().value(isPrivateSyncer)
                .size(18)
                .overlay(true, GTGuiTextures.OVERLAY_BUTTON_CHECKMARK)
                .overlay(false, GTGuiTextures.OVERLAY_BUTTON_CROSS)
                .tooltip(t -> t.addLine(translateToLocal("ggfab.tooltip.linked_input_bus.private"))));

        mainColumn.child(inputRow);

        // inventory slots
        mainColumn.child(createSlots(syncManager, channelSyncer));

        return super.createContentSection(panel, syncManager).child(mainColumn);
    }

    private SlotGroupWidget createSlots(PanelSyncManager syncManager, StringSyncValue channelSyncer) {
        syncManager.registerSlotGroup("item_inv", SLOT_ROW);

        String[] matrix = new String[SLOT_ROW];
        String repeat = StringUtils.getRepetitionOf('s', SLOT_PER_ROW);
        Arrays.fill(matrix, repeat);

        return SlotGroupWidget.builder()
            .matrix(matrix)
            .key(
                's',
                index -> new ItemSlot().slot(
                    new ModularSlot(hatch.getHandler(), index).slotGroup("item_inv")
                        .filter(
                            s -> !channelSyncer.getStringValue()
                                .isEmpty())))
            .build();
    }

    @Override
    public void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);

        syncManager.syncValue("handler", new NBTSerializableSyncHandler<>(hatch::getHandler));
    }
}
