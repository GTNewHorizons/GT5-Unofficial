package gregtech.common.gui.modularui.item;

import java.util.function.Supplier;

import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import org.jetbrains.annotations.Nullable;

import com.cleanroommc.modularui.api.ITheme;
import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.ISynced;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.drawable.UITexture;
import com.cleanroommc.modularui.factory.PlayerInventoryGuiData;
import com.cleanroommc.modularui.factory.inventory.InventoryTypes;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.RichTooltip;
import com.cleanroommc.modularui.theme.WidgetThemeEntry;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.utils.item.IItemHandler;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.SlotGroupWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;
import com.cleanroommc.modularui.widgets.slot.SlotGroup;

import gregtech.api.enums.ToolboxSlot;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.items.ItemGTToolbox;
import gregtech.crossmod.backhand.Backhand;
import ic2.api.item.IElectricItem;

/**
 * GUI for storing and retrieving items from the toolbox.
 *
 * @see gregtech.api.modularui2.ToolboxSelectGuiFactory
 */
public class ToolboxInventoryGui {

    private static final int WIDTH = 176;
    private static final int HEIGHT = 166;
    private static final String SLOT_GROUP_SYNC_NAME = "toolbox_internal_inventory";
    private static final int SLOT_DRAW_SIZE = 18;

    /** Draws a gap between two columns starting at the specified column. Set to something very large to disable. */
    private static final int GAP_COLUMN = 4;
    /** Defines the size of the gap to draw. Set to 0 to disable. */
    private static final int GAP_SIZE = SLOT_DRAW_SIZE;

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

        if (offHandStack != null && offHandStack.getItem() instanceof ItemGTToolbox
            && Backhand.getOffhandSlot(data.getPlayer()) != -1) {
            usedSlot = Backhand.getOffhandSlot(data.getPlayer());
        } else if (mainHandStack != null && mainHandStack.getItem() instanceof ItemGTToolbox) {
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

        Flow column = Flow.column()
            .sizeRel(1);
        column.child(
            IKey.lang("GT5U.gui.text.toolbox.title")
                .alignment(Alignment.Center)
                .asWidget()
                .widthRel(1)
                .height(SLOT_DRAW_SIZE));

        final SlotGroupWidget slotGroupWidget = new SlotGroupWidget();

        final int genericSlotSize = ToolboxSlot.GENERIC_SLOTS.size();
        final SlotGroup toolGroup = new SlotGroup(
            "gt5:toolbox:tools",
            ToolboxSlot.values().length - genericSlotSize,
            0,
            true);
        final SlotGroup genericGroup = new SlotGroup("gt5:toolbox:generic", genericSlotSize, 100, true);

        syncManager.registerSlotGroup(toolGroup);
        syncManager.registerSlotGroup(genericGroup);

        for (final ToolboxSlot slot : ToolboxSlot.values()) {

            final IWidget widget = new CustomItemSlot(() -> itemHandler, slot).slot(
                new ModularSlot(itemHandler, slot.getSlotID()).slotGroup(slot.isGeneric() ? genericGroup : toolGroup))
                .addTooltipLine(getTooltip(slot));

            final int leftShift = slot.getColumn() * SLOT_DRAW_SIZE + (slot.getColumn() >= GAP_COLUMN ? GAP_SIZE : 0);
            widget.flex()
                .left(leftShift)
                .top(slot.getRow() * SLOT_DRAW_SIZE);
            slotGroupWidget.child(widget);

            ((ISynced<?>) widget).syncHandler(SLOT_GROUP_SYNC_NAME, slot.getSlotID());
        }

        slotGroupWidget.flex()
            .size(
                ToolboxSlot.ROW_WIDTH * SLOT_DRAW_SIZE + GAP_SIZE,
                (int) (Math.ceil((double) ToolboxSlot.values().length / (double) ToolboxSlot.ROW_WIDTH))
                    * SLOT_DRAW_SIZE);
        column.child(
            slotGroupWidget.alignX(Alignment.Center)
                .alignY(0.2f));

        panel.child(column);
        return panel;
    }

    private static String getTooltip(final ToolboxSlot slot) {
        final String key = slot.isGeneric() ? "generic" : String.valueOf(slot.getSlotID());

        return StatCollector
            .translateToLocalFormatted(
                "GT5U.gui.text.toolbox.tooltip.base",
                StatCollector.translateToLocal("GT5U.gui.text.toolbox.slot_title." + key),
                StatCollector.translateToLocal("GT5U.gui.text.toolbox.tooltip." + key))
            .replace("\\n", "\n");
    }

    private static class CustomItemSlot extends ItemSlot {

        private static final UITexture[] BLANK_SLOT_TEXTURE = { GTGuiTextures.SLOT_ITEM_STANDARD };

        private final Supplier<IItemHandler> itemHandlerSupplier;
        private final ToolboxSlot slot;

        public CustomItemSlot(final Supplier<IItemHandler> itemHandlerSupplier, final ToolboxSlot slot) {
            super();

            this.itemHandlerSupplier = itemHandlerSupplier;
            this.slot = slot;
        }

        /**
         * Allows the background to dynamically remove the tool outline if there's a tool in the slot.
         */
        @Override
        public @Nullable IDrawable getCurrentBackground(final ITheme theme, final WidgetThemeEntry<?> widgetTheme) {
            final IItemHandler itemHandler = this.itemHandlerSupplier.get();

            if (itemHandler != null && slot.isTool()) {
                final int slotID = slot.getSlotID();
                final ItemStack stackInSlot = itemHandler.getStackInSlot(slotID);

                if (stackInSlot == null && slot.getOverlay()
                    .isPresent()) {
                    return IDrawable.of(
                        GTGuiTextures.SLOT_ITEM_STANDARD,
                        slot.getOverlay()
                            .get());
                }
            }

            return IDrawable.of(BLANK_SLOT_TEXTURE);
        }

        @Override
        public void buildTooltip(final ItemStack stack, final RichTooltip tooltip) {
            super.buildTooltip(stack, tooltip);
            if (stack != null && stack.getItem() instanceof final IElectricItem item && item.getMaxCharge(stack) > 0) {
                tooltip.add(StatCollector.translateToLocal("GT5U.gui.text.toolbox.tooltip.no_charge_while_open"));
            }
        }
    }
}
