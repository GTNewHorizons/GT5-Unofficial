package gregtech.common.gui.modularui.item;

import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.ISynced;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.factory.PlayerInventoryGuiData;
import com.cleanroommc.modularui.factory.inventory.InventoryTypes;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.utils.item.IItemHandler;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.SlotGroupWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;

import gregtech.common.items.ItemToolbox;
import gregtech.crossmod.backhand.Backhand;

public class ToolboxInventoryGui {

    private static final int WIDTH = 176;
    private static final int HEIGHT = 166;
    private static final String SLOT_GROUP_SYNC_NAME = "toolbox_internal_inventory";
    public static final int SLOT_DRAW_SIZE = 18;

    private final PanelSyncManager syncManager;
    private final PlayerInventoryGuiData data;
    private final IItemHandler itemHandler;

    private final int usedSlot;

    public ToolboxInventoryGui(PanelSyncManager syncManager, PlayerInventoryGuiData data,
        final IItemHandler itemHandler) {
        this.syncManager = syncManager;
        this.data = data;
        this.itemHandler = itemHandler;

        final ItemStack offHandStack = Backhand.getOffhandItem(data.getPlayer());
        final ItemStack mainHandStack = data.getUsedItemStack();

        if (offHandStack != null && offHandStack.getItem() instanceof ItemToolbox
            && Backhand.getOffhandSlot(data.getPlayer()) != -1) {
            usedSlot = Backhand.getOffhandSlot(data.getPlayer());
        } else if (mainHandStack != null && mainHandStack.getItem() instanceof ItemToolbox) {
            usedSlot = data.getSlotIndex();
        } else {
            throw new RuntimeException("Tried to open the toolbox GUI with no toolbox in main hand or offhand");
        }
    }

    public ModularPanel build() {
        ModularPanel panel = ModularPanel.defaultPanel("Field Engineer's Toolbox", WIDTH, HEIGHT);
        panel.bindPlayerInventory();

        if (data.getInventoryType() == InventoryTypes.PLAYER) {
            syncManager.bindPlayerInventory(
                data.getPlayer(),
                (inv, index) -> index == usedSlot ? new ModularSlot(inv, index).accessibility(false, false)
                    : new ModularSlot(inv, index));
        }

        syncManager.registerSlotGroup(SLOT_GROUP_SYNC_NAME, itemHandler.getSlots());

        Flow column = Flow.column()
            .sizeRel(1);
        column.child(
            IKey.lang("GT5U.gui.text.toolbox.title")
                .alignment(Alignment.Center)
                .asWidget()
                .widthRel(1)
                .height(SLOT_DRAW_SIZE));

        SlotGroupWidget slotGroupWidget = new SlotGroupWidget();

        for (final ItemToolbox.SlotDefinition slot : ItemToolbox.SlotDefinition.values()) {

            final IWidget widget = new ItemSlot()
                .slot(new ModularSlot(itemHandler, slot.getSlotID()).slotGroup(SLOT_GROUP_SYNC_NAME))
                .background(slot.getBackground())
                .addTooltipLine(getTooltip(slot));

            widget.flex()
                .left(slot.getColumn() * SLOT_DRAW_SIZE)
                .top(slot.getRow() * SLOT_DRAW_SIZE);
            slotGroupWidget.child(widget);

            ((ISynced<?>) widget).syncHandler(SLOT_GROUP_SYNC_NAME, slot.getSlotID());
        }

        slotGroupWidget.flex()
            .size(
                ItemToolbox.SlotDefinition.ROW_WIDTH * SLOT_DRAW_SIZE,
                (int) (Math.ceil(
                    (double) ItemToolbox.SlotDefinition.values().length
                        / (double) ItemToolbox.SlotDefinition.ROW_WIDTH))
                    * SLOT_DRAW_SIZE);
        column.child(
            slotGroupWidget.alignX(Alignment.Center)
                .alignY(0.2f));

        panel.child(column);
        return panel;
    }

    private static String getTooltip(final ItemToolbox.SlotDefinition slot) {
        final String key = slot.isGeneric() ? "generic" : String.valueOf(slot.getSlotID());

        return StatCollector
            .translateToLocalFormatted(
                "GT5U.gui.text.toolbox.tooltip.base",
                StatCollector.translateToLocal("GT5U.gui.text.toolbox.slot_title." + key),
                StatCollector.translateToLocal("GT5U.gui.text.toolbox.tooltip." + key))
            .replace("\\n", "\n");
    }
}
