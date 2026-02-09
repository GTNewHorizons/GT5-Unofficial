package gregtech.common.items.toolbox;

import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.utils.item.ItemStackHandler;

import gregtech.api.enums.ToolboxSlot;
import gregtech.api.items.MetaGeneratedTool;
import gregtech.common.items.ItemGTToolbox;
import ic2.api.item.IElectricItemManager;

/**
 * Contains various static methods used by toolbox classes.
 */
public class ToolboxUtil {

    /**
     * Convenience method for getting an item from the toolbox.
     * <p>
     * <b>NOTE:</b> Any changes to the retrieved item MUST be persisted via
     * {@link #saveItemInside(ItemStack, ItemStack, ToolboxSlot)}!
     * This is not done automatically.
     *
     * @param toolbox The toolbox you wish to search
     * @param slot    The slot to retrieve an item from
     * @return An optional containing a copy of the item's ItemStack, or empty if there isn't one.
     */
    public static Optional<ItemStack> getItemInside(final @NotNull ItemStack toolbox, final @NotNull ToolboxSlot slot) {
        return Optional.ofNullable(
            new ToolboxItemStackHandler(Objects.requireNonNull(toolbox)).getStackInSlot(
                Objects.requireNonNull(slot)
                    .getSlotID()));

    }

    /**
     * Convenience method for getting the battery from the toolbox.
     *
     * @see #getItemInside(ItemStack, ToolboxSlot)
     */
    public static Optional<ItemStack> getBattery(final @NotNull ItemStack toolbox) {
        return getItemInside(toolbox, ToolboxSlot.BATTERY);
    }

    /**
     * Get the {@link IElectricItemManager} for the battery inside a toolbox and runs an action with no return value.
     *
     * @param toolbox The toolbox to search
     * @param action  A function to run if a battery is found inside the toolbox.
     *                Arguments are the battery's {@link ItemStack} and its manager.
     */
    public static void withBatteryAndManager(final ItemStack toolbox,
        BiConsumer<? super ItemStack, ? super IElectricItemManager> action) {
        final Optional<ItemStack> stack = getBattery(toolbox);

        stack.flatMap(ItemGTToolbox::getElectricManager)
            .ifPresent(manager -> action.accept(stack.get(), manager));
    }

    /**
     * {@code additionalAction} defaults to no operation.
     *
     * @see #saveToolbox(ItemStack, ItemStackHandler, Consumer)
     */
    public static void saveToolbox(final ItemStack toolbox, final ItemStackHandler handler) {
        saveToolbox(toolbox, handler, null);
    }

    /**
     * Writes the toolbox's data to the {@link ItemStack ItemStack's} NBT data.
     *
     * @param toolbox          The toolbox to persist
     * @param handler          The {@link ItemStackHandler} that contains updated contents data
     * @param additionalAction Any additional changes to make to the NBT data of the toolbox before persisting.
     *                         If null, nothing happens.
     */
    public static void saveToolbox(final ItemStack toolbox, final ItemStackHandler handler,
        @Nullable Consumer<NBTTagCompound> additionalAction) {
        final NBTTagCompound tag = toolbox.hasTagCompound() ? toolbox.getTagCompound() : new NBTTagCompound();
        final int selectedTool = tag.hasKey(ItemGTToolbox.CURRENT_TOOL_KEY)
            ? tag.getInteger(ItemGTToolbox.CURRENT_TOOL_KEY)
            : ItemGTToolbox.NO_TOOL_SELECTED;

        for (int i = 0; i < handler.getSlots(); i++) {
            final ItemStack stack = handler.getStackInSlot(i);
            if (stack != null && stack.stackSize == 0) {
                handler.setStackInSlot(i, null);
                if (i == selectedTool) {
                    tag.removeTag(ItemGTToolbox.CURRENT_TOOL_KEY);
                    tag.setInteger(ItemGTToolbox.RECENTLY_BROKEN_SLOT_KEY, selectedTool);
                }
            }
        }

        tag.setTag(ItemGTToolbox.CONTENTS_KEY, handler.serializeNBT());

        if (additionalAction != null) {
            additionalAction.accept(tag);
        }

        toolbox.setTagCompound(tag);
    }

    /**
     * Writes the item to the toolbox in the selected slot. Use after mutating the item; changes to items are not
     * automatically propagated to the containing toolbox!
     *
     * @param toolbox     The toolbox to use
     * @param itemToStore An item to save to the toolbox, or null to remove the item
     * @param slot        The slot to store it in
     */
    public static void saveItemInside(final @NotNull ItemStack toolbox, final ItemStack itemToStore,
        final @NotNull ToolboxSlot slot) {
        ItemStackHandler handler = new ToolboxItemStackHandler(toolbox);
        handler.setStackInSlot(slot.getSlotID(), itemToStore);
        saveToolbox(toolbox, handler);
    }

    /**
     * Convenience method for saving the battery.
     *
     * @see #saveItemInside(ItemStack, ItemStack, ToolboxSlot)
     */
    public static void saveBattery(final ItemStack toolbox, final ItemStack battery) {
        saveItemInside(toolbox, battery, ToolboxSlot.BATTERY);
    }

    public static Optional<ToolboxSlot> getSelectedToolType(ItemStack toolbox) {
        if (toolbox == null || !toolbox.hasTagCompound()
            || !toolbox.getTagCompound()
                .hasKey(ItemGTToolbox.CURRENT_TOOL_KEY)) {
            return Optional.empty();
        }

        return ToolboxSlot.getBySlot(
            toolbox.getTagCompound()
                .getInteger(ItemGTToolbox.CURRENT_TOOL_KEY));
    }

    public static boolean isToolOfType(final ItemStack toolbox, final ToolboxSlot desiredType) {
        return getSelectedToolType(toolbox).filter(currentType -> currentType == desiredType)
            .isPresent();
    }

    /**
     * Helper method for getting the currently equipped tool.
     *
     * @param toolbox The ItemStack of the toolbox
     * @return The equipped tool, or empty if one isn't equipped
     */
    public static Optional<ItemStack> getSelectedTool(final ItemStack toolbox) {
        if (toolbox == null || !(toolbox.getItem() instanceof ItemGTToolbox)) {
            return Optional.empty();
        }

        final ToolboxItemStackHandler handler = new ToolboxItemStackHandler(toolbox);
        return handler.getCurrentTool();
    }

    public static void damageSelectedTool(final ItemStack toolbox) {
        if (toolbox != null && toolbox.getItem() instanceof ItemGTToolbox) {
            final ToolboxItemStackHandler handler = new ToolboxItemStackHandler(toolbox);
            handler.mutateCurrentTool(toolStack -> {
                if (toolStack.getItem() instanceof final MetaGeneratedTool toolItem) {
                    toolItem.doDamageToItem(toolStack, toolItem.getToolStats(toolStack).getToolDamagePerEntityAttack());
                }
            });
        }

    }

    /**
     * Check if the toolbox is allowed to charge or discharge.
     *
     * @param toolbox The ItemStack of the toolbox
     * @return true if the toolbox can be charged
     */
    public static boolean canCharge(final ItemStack toolbox) {
        final NBTTagCompound tag = toolbox.hasTagCompound() ? toolbox.getTagCompound() : new NBTTagCompound();

        return !tag.getBoolean(ItemGTToolbox.TOOLBOX_OPEN_KEY)
            && !tag.hasKey(ItemGTToolbox.BROKEN_TOOL_ANIMATION_END_KEY);
    }

    public static boolean hasAnyItems(final ItemStack toolbox) {
        if (toolbox == null) {
            return false;
        }

        return new ToolboxItemStackHandler(toolbox).getStacks()
            .stream()
            .anyMatch(Objects::nonNull);
    }
}
