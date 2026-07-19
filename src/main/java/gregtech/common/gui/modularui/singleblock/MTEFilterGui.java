package gregtech.common.gui.modularui.singleblock;

import java.util.List;
import java.util.function.Supplier;

import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.layout.Grid;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;
import com.cleanroommc.modularui.widgets.slot.PhantomItemSlot;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.modularui2.common.CommonButtons;
import gregtech.common.gui.modularui.singleblock.base.MTEFilterBaseGui;
import gregtech.common.modularui2.widget.builder.ItemSlotGridBuilder;
import gregtech.common.tileentities.automation.MTEFilter;
import it.unimi.dsi.fastutil.booleans.BooleanObjectImmutablePair;
import it.unimi.dsi.fastutil.booleans.BooleanObjectPair;

public class MTEFilterGui extends MTEFilterBaseGui<MTEFilter> {

    public MTEFilterGui(MTEFilter machine) {
        super(machine);
    }

    @Override
    protected ParentWidget<?> createContentSection(ModularPanel panel, PanelSyncManager syncManager) {
        Flow mainRow = Flow.row()
            .childPadding(1)
            .coverChildren();

        // white arrow shaft
        mainRow.child(
            GTGuiTextures.PICTURE_ARROW_6_WHITE_PRE.asWidget()
                .size(8, 6));

        // filter grid
        mainRow.child(
            new Grid().coverChildren()
                .gridOfWidthHeight(
                    3,
                    3,
                    ($x, $y, index) -> new PhantomItemSlot().slot(new ModularSlot(machine.inventoryHandler, index + 9))
                        .disableThemeBackground(true)
                        .disableHoverThemeBackground(true))
                .background(GTGuiTextures.PICTURE_SLOTS_HOLO_3BY3));

        // blue arrow
        mainRow.child(
            GTGuiTextures.PICTURE_ARROW_24_BLUE.asWidget()
                .size(25, 24));

        // inventory grid
        mainRow.child(
            new ItemSlotGridBuilder(machine.inventoryHandler, syncManager).size(3)
                .build());

        // red arrow
        mainRow.child(
            GTGuiTextures.PICTURE_ARROW_24_RED.asWidget()
                .size(17, 24));

        return super.createContentSection(panel, syncManager).child(mainRow);
    }

    @Override
    protected boolean supportsStocking() {
        return false;
    }

    @Override
    protected List<BooleanObjectPair<Supplier<IWidget>>> createButtonList(ModularPanel panel,
        PanelSyncManager syncManager) {
        List<BooleanObjectPair<Supplier<IWidget>>> buttons = super.createButtonList(panel, syncManager);

        // ignore NBT button
        buttons.add(
            new BooleanObjectImmutablePair<>(
                true,
                () -> CommonButtons.createToggleButtonDynamicTooltip(
                    new BooleanSyncValue(machine::isIgnoreNbt, machine::setIgnoreNbt).allowC2S(),
                    GTGuiTextures.OVERLAY_BUTTON_NBT,
                    configureTooltip("GT5U.machines.ignore_nbt.tooltip"))));

        return buttons;
    }
}
