package gregtech.common.gui.modularui.singleblock;

import java.util.List;
import java.util.function.Supplier;

import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.SlotGroupWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;
import com.cleanroommc.modularui.widgets.slot.PhantomItemSlot;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.gui.modularui.singleblock.base.MTEFilterBaseGui;
import gregtech.common.tileentities.automation.MTEFilter;
import xyz.wagyourtail.jvmdg.util.Pair;

public class MTEFilterGui extends MTEFilterBaseGui<MTEFilter> {

    public MTEFilterGui(MTEFilter machine) {
        super(machine);
    }

    @Override
    protected ParentWidget<?> createContentSection(ModularPanel panel, PanelSyncManager syncManager) {
        String[] matrix = new String[] { "sss", "sss", "sss" };

        Flow mainRow = Flow.row()
            .childPadding(1)
            .coverChildren()
            .marginLeft(3);

        // white arrow
        mainRow.child(createArrow(GTGuiTextures.PICTURE_ARROW_24_WHITE, 9, 24, false));

        // filter grid
        mainRow.child(
            SlotGroupWidget.builder()
                .matrix(matrix)
                .key('s', index -> new PhantomItemSlot().slot(new ModularSlot(machine.inventoryHandler, index + 9) {

                    // both of these are needed
                    @Override
                    public int getSlotStackLimit() {
                        return 1;
                    }

                    @Override
                    public int getItemStackLimit(@NotNull ItemStack stack) {
                        return 1;
                    }
                })
                    .disableThemeBackground(true)
                    .disableHoverThemeBackground(true))
                .build()
                .background(GTGuiTextures.PICTURE_SLOTS_HOLO_3BY3));

        // blue arrow
        mainRow.child(createArrow(GTGuiTextures.PICTURE_ARROW_24_BLUE, 25, 24, true));

        // inventory grid
        mainRow.child(
            SlotGroupWidget.builder()
                .matrix(matrix)
                .key(
                    's',
                    index -> new ItemSlot()
                        .slot(new ModularSlot(machine.inventoryHandler, index).slotGroup("item_inv")))
                .build());

        // red arrow
        mainRow.child(createArrow(GTGuiTextures.PICTURE_ARROW_24_RED, 19, 24, true));

        return super.createContentSection(panel, syncManager).child(mainRow);
    }

    @Override
    protected boolean supportsStocking() {
        return false;
    }

    @Override
    protected List<Pair<Boolean, Supplier<IWidget>>> createButtonList(ModularPanel panel,
        PanelSyncManager syncManager) {
        List<Pair<Boolean, Supplier<IWidget>>> buttons = super.createButtonList(panel, syncManager);

        // ignore NBT button
        buttons.add(
            new Pair<>(
                true,
                () -> createButton(
                    new BooleanSyncValue(machine::isIgnoreNbt, machine::setIgnoreNbt),
                    GTGuiTextures.OVERLAY_BUTTON_NBT,
                    configureTooltip("GT5U.machines.ignore_nbt.tooltip"))));

        return buttons;
    }

    @Override
    protected void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);

        syncManager.registerSlotGroup("item_inv", 3);
    }
}
