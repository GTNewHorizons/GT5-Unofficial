package gregtech.common.gui.modularui.singleblock.base;

import java.util.List;
import java.util.function.Supplier;

import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.drawable.Rectangle;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;

import gregtech.api.metatileentity.implementations.MTEFilterBase;
import gregtech.api.metatileentity.implementations.MTESpecialFilter;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.modularui2.common.CommonButtons;
import gregtech.common.modularui2.widget.builder.ItemSlotGridBuilder;
import it.unimi.dsi.fastutil.booleans.BooleanObjectImmutablePair;
import it.unimi.dsi.fastutil.booleans.BooleanObjectPair;

public abstract class MTESpecialFilterBaseGui<T extends MTESpecialFilter> extends MTEFilterBaseGui<T> {

    public MTESpecialFilterBaseGui(T machine) {
        super(machine);
    }

    protected boolean supportsAllowNBT() {
        return true;
    }

    @Override
    protected ParentWidget<?> createContentSection(ModularPanel panel, PanelSyncManager syncManager) {
        Flow mainRow = Flow.row()
            .coverChildren()
            .childPadding(1);

        // white arrow shaft
        mainRow.child(
            new Rectangle().asWidget()
                .size(26, 6));

        // filter slot
        mainRow.child(createFilterSlot(panel, syncManager));

        // blue arrow
        mainRow.child(
            GTGuiTextures.PICTURE_ARROW_24_BLUE.asWidget()
                .size(43, 24));

        // buffer grid
        mainRow.child(
            new ItemSlotGridBuilder(machine.inventoryHandler, syncManager).size(3)
                .build());

        // red arrow
        mainRow.child(
            GTGuiTextures.PICTURE_ARROW_24_RED.asWidget()
                .size(17, 24));

        return super.createContentSection(panel, syncManager).child(mainRow);
    }

    protected ItemSlot createFilterSlot(ModularPanel panel, PanelSyncManager syncManager) {
        ItemSlot itemSlot = createFilterSlotBase(panel, syncManager)
            .slot(new ModularSlot(machine.inventoryHandler, MTEFilterBase.FILTER_SLOT_INDEX));

        itemSlot.tooltipDynamic(t -> t.addStringLines(getEmptyFilterSlotTooltip(panel, syncManager)))
            .background(GTGuiTextures.BUTTON_STANDARD);

        itemSlot.itemTooltip()
            .tooltipBuilder(
                t -> t.clearText()
                    .addStringLines(getFilledFilterSlotTooltip(panel, syncManager)));

        return itemSlot;
    }

    protected abstract ItemSlot createFilterSlotBase(ModularPanel panel, PanelSyncManager syncManager);

    protected abstract List<String> getEmptyFilterSlotTooltip(ModularPanel panel, PanelSyncManager syncManager);

    protected abstract List<String> getFilledFilterSlotTooltip(ModularPanel panel, PanelSyncManager syncManager);

    @Override
    protected List<BooleanObjectPair<Supplier<IWidget>>> createButtonList(ModularPanel panel,
        PanelSyncManager syncManager) {
        List<BooleanObjectPair<Supplier<IWidget>>> buttons = super.createButtonList(panel, syncManager);

        // allow NBT button
        buttons.add(
            new BooleanObjectImmutablePair<>(
                supportsAllowNBT(),
                () -> CommonButtons.createToggleButtonDynamicTooltip(
                    new BooleanSyncValue(machine::isAllowNbt, machine::setAllowNbt).allowC2S(),
                    GTGuiTextures.OVERLAY_BUTTON_NBT,
                    configureTooltip("GT5U.machines.allow_nbt.tooltip"))));

        return buttons;
    }
}
