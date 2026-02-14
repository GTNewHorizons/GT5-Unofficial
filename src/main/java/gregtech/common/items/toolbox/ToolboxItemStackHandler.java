package gregtech.common.items.toolbox;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import com.cleanroommc.modularui.utils.item.ItemStackHandler;

import gregtech.api.enums.ToolboxSlot;
import gregtech.common.items.ItemGTToolbox;

/**
 * A special {@link ItemStackHandler} for working with toolboxes. Used both to drive the inventory GUI and for
 * general inventory management.
 */
public class ToolboxItemStackHandler extends ItemStackHandler {

    private final int currentTool;

    public ToolboxItemStackHandler(final ItemStack toolbox) {
        super(ToolboxSlot.values().length);
        int currentTool = ItemGTToolbox.NO_TOOL_SELECTED;

        Objects.requireNonNull(toolbox);
        if (!(toolbox.getItem() instanceof ItemGTToolbox)) {
            throw new IllegalArgumentException("Tried to create a toolbox stack handler with a non-toolbox");
        }

        NBTTagCompound itemData = toolbox.getTagCompound();
        if (itemData != null) {
            if (itemData.hasKey(ItemGTToolbox.CONTENTS_KEY)) {
                deserializeNBT(itemData.getCompoundTag(ItemGTToolbox.CONTENTS_KEY));
            }

            if (itemData.hasKey(ItemGTToolbox.CURRENT_TOOL_KEY)) {
                currentTool = itemData.getInteger(ItemGTToolbox.CURRENT_TOOL_KEY);
            }
        }

        this.currentTool = currentTool;
    }

    public ToolboxItemStackHandler(EntityPlayer player, int slot) {
        this(player.inventory.getStackInSlot(slot));
    }

    @Override
    public boolean isItemValid(final int slot, final ItemStack stack) {
        return ToolboxSlot.getBySlot(slot)
            .map(definition -> definition.test(stack))
            .orElse(false);
    }

    @Override
    public int getSlotLimit(final int slot) {
        return ToolboxSlot.getBySlot(slot)
            .map(ToolboxSlot::isGeneric)
            .map(x -> x ? 64 : 1)
            .orElse(64);
    }

    /**
     * Allows for the currently selected tool to change. Tool is saved to the handler after the action.
     *
     * @param action Receives the current tool. If the user has no tool selected, action does not run.
     */
    public void mutateCurrentTool(Consumer<ItemStack> action) {
        getCurrentTool().ifPresent(tool -> {
            action.accept(tool);
            setStackInSlot(currentTool, tool);
        });
    }

    /**
     * Retrieves the currently selected tool from the toolbox.
     *
     * @return An Optional containing the tool's {@link ItemStack}, or empty if the user hasn't selected one
     */
    public Optional<ItemStack> getCurrentTool() {
        if (currentTool == ItemGTToolbox.NO_TOOL_SELECTED || currentTool > this.stacks.size()) {
            return Optional.empty();
        }

        return Optional.of(this.stacks.get(currentTool));
    }

    public void setCurrentTool(ItemStack newStack) {
        this.validateSlotIndex(currentTool);
        setStackInSlot(currentTool, newStack);
    }

}
