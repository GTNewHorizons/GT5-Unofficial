package gregtech.common.gui.modularui.singleblock.base;

import net.minecraft.item.ItemStack;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.Widget;
import com.cleanroommc.modularui.widgets.TextWidget;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;

import gregtech.api.interfaces.metatileentity.IItemLockable;
import gregtech.api.metatileentity.implementations.MTETieredMachineBlock;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.modularui2.GTWidgetThemes;

public abstract class MTEMachineWithItemScreenBaseGui<T extends MTETieredMachineBlock>
    extends MTEMachineWithScreenBaseGui<T> {

    public MTEMachineWithItemScreenBaseGui(T machine) {
        super(machine);
    }

    @Override
    protected TextWidget<?> createScreenAmountLabel(ModularPanel panel, PanelSyncManager syncManager) {
        return IKey.lang("GT5U.gui.text.item_amount")
            .asWidget()
            .widgetTheme(GTWidgetThemes.DISPLAY_TEXT_WHITE);
    }

    @Override
    protected ItemSlot createSlot(ModularPanel panel, PanelSyncManager syncManager) {
        return new ItemSlot().slot(new ModularSlot(machine.inventoryHandler, getSlotIndex()) {

            @Override
            public ItemStack getStack() {
                // only display one of the item
                ItemStack itemStack = super.getStack();
                if (itemStack != null) {
                    itemStack = itemStack.copy();
                    itemStack.stackSize = 1;
                }
                return itemStack;
            }
        }.accessibility(false, false))
            .background(GTGuiTextures.SLOT_ITEM_TANK)
            .hoverBackground(GTGuiTextures.SLOT_ITEM_TANK)
            .bottomRel(0)
            .rightRel(0);
    }

    protected abstract int getSlotIndex();

    @Override
    protected boolean supportsFilterScreen() {
        return machine instanceof IItemLockable;
    }

    @Override
    protected TextWidget<?> createFilterScreenAmountLabel(ModularPanel panel, PanelSyncManager syncManager) {
        return null; // TODO in super chest port
    }

    @Override
    protected TextWidget<?> createFilterScreenAmount(ModularPanel panel, PanelSyncManager syncManager) {
        return null; // TODO in super chest port
    }

    @Override
    protected Widget<?> createFilterSlot(ModularPanel panel, PanelSyncManager syncManager) {
        return null; // TODO in super chest port
    }
}
