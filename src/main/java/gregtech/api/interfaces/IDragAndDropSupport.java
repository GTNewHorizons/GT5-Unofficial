package gregtech.api.interfaces;

import java.util.Collections;
import java.util.List;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;

import codechicken.nei.NEIClientUtils;
import codechicken.nei.VisiblityData;
import codechicken.nei.api.INEIGuiHandler;
import codechicken.nei.api.TaggedInventoryArea;
import cpw.mods.fml.common.Optional;
import gregtech.api.enums.Mods;

/**
 * Implement this interface if your GuiContainer supports Drag-And-Drop behavior on NEI.
 */
@Optional.Interface(modid = Mods.Names.NOT_ENOUGH_ITEMS, iface = "codechicken.nei.api.INEIGuiHandler")
public interface IDragAndDropSupport extends INEIGuiHandler {

    /**
     * Implement this to handle Drag-And-Drop behavior. This may be invoked on normal click too
     * ({@code isGhost==false}), so be careful if your slot supports both Drag-And-Drop and other behaviors e.g. fluid
     * I/O with FluidDisplay click
     *
     * @param gui          Current gui instance. Make sure to check if it is an instance of your GuiContainer.
     * @param mousex       X position of the mouse
     * @param mousey       Y position of the mouse
     * @param draggedStack ItemStack user is holding on cursor
     * @param button       0 = left click, 1 = right click
     * @param isGhost      Whether {@code draggedStack} is dragged from ItemPanel/BookmarkPanel, or actual item player
     *                     holds
     * @return True if success
     */
    boolean handleDragAndDropGT(GuiContainer gui, int mousex, int mousey, ItemStack draggedStack, int button,
        boolean isGhost);

    default boolean handleDragNDrop(GuiContainer gui, int mousex, int mousey, ItemStack draggedStack, int button) {
        return handleDragAndDropGT(gui, mousex, mousey, draggedStack, button, NEIClientUtils.getHeldItem() == null);
    }

    default VisiblityData modifyVisiblity(GuiContainer gui, VisiblityData currentVisibility) {
        return currentVisibility;
    }

    default Iterable<Integer> getItemSpawnSlots(GuiContainer gui, ItemStack item) {
        return Collections.emptyList();
    }

    default List<TaggedInventoryArea> getInventoryAreas(GuiContainer gui) {
        return null;
    }

    default boolean hideItemPanelSlot(GuiContainer gui, int x, int y, int w, int h) {
        return false;
    }
}
