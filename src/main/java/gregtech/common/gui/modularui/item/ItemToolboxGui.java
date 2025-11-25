package gregtech.common.gui.modularui.item;

import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.factory.PlayerInventoryGuiData;
import com.cleanroommc.modularui.factory.inventory.InventoryTypes;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.utils.ItemStackItemHandler;
import com.cleanroommc.modularui.utils.item.IItemHandlerModifiable;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.SlotGroupWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.items.ItemGTToolbox;
import ic2.api.item.ItemWrapper;

public class ItemToolboxGui {

    ItemStack boxStack;
    @NotNull
    ItemGTToolbox toolbox;
    PanelSyncManager syncManager;
    PlayerInventoryGuiData data;

    public ItemToolboxGui(PanelSyncManager syncManager, PlayerInventoryGuiData data) {
        this.syncManager = syncManager;
        this.data = data;
        this.boxStack = data.getMainHandItem();
        this.toolbox = (ItemGTToolbox) boxStack.getItem();
    }

    public ModularPanel build() {
        ModularPanel panel = ModularPanel.defaultPanel("toolbox", 180, 180);
        IItemHandlerModifiable itemHandler = new ItemStackItemHandler(data, 9);

        if (data.getInventoryType() == InventoryTypes.PLAYER) {
            syncManager.bindPlayerInventory(
                data.getPlayer(),
                (inv, index) -> index == data.getSlotIndex() ? new ModularSlot(inv, index).accessibility(false, false)
                    : new ModularSlot(inv, index));
        }

        syncManager.registerSlotGroup("slots", 9);
        Flow col = Flow.column()
            .sizeRel(1);
        col.child(
            IKey.lang("GT5U.gui.text.toolbox")
                .alignment(Alignment.Center)
                .asWidget()
                .widthRel(1f)
                .height(18));

        SlotGroupWidget slots = SlotGroupWidget.builder()
            .matrix("ccccccccc")
            .key(
                'c',
                index -> new ItemSlot()
                    .slot(
                        new ModularSlot(itemHandler, index).filter(ItemWrapper::canBeStoredInToolbox)
                            .slotGroup("slots"))
                    .background(GTGuiTextures.SLOT_ITEM_STANDARD))
            .build();
        col.child(
            slots.alignX(Alignment.CENTER)
                .alignY(0.2f));
        col.child(
            SlotGroupWidget.playerInventory(false)
                .alignY(0.8f)
                .alignX(Alignment.CENTER));
        panel.child(col);
        return panel;
    }
}
