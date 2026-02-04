package gregtech.common.render.items;

import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;

import gregtech.common.items.ItemToolbox;
import gregtech.common.render.MetaGeneratedToolRenderer;

/**
 * Handles rendering the fake offhand toolbox when a tool is selected.
 */
public class ToolboxRenderer implements IItemRenderer {

    private static final MetaGeneratedToolRenderer META_TOOL_RENDERER = new MetaGeneratedToolRenderer();

    @Override
    public boolean handleRenderType(final ItemStack itemStack, final ItemRenderType type) {
        // We need to be explicit with the tag checks. getInteger annoyingly returns 0 if the key is missing, and a
        // value of 0 actually indicates that the wrench is equipped, while the tag being missing indicates that no tool
        // is selected.
        return type == ItemRenderType.EQUIPPED_FIRST_PERSON && itemStack != null
            && itemStack.getItem() instanceof ItemToolbox
            && itemStack.hasTagCompound()
            && itemStack.getTagCompound()
                .hasKey(ItemToolbox.CURRENT_TOOL_NBT_KEY)
            && itemStack.getTagCompound()
                .getInteger(ItemToolbox.CURRENT_TOOL_NBT_KEY) != ItemToolbox.NO_TOOL_SELECTED;
    }

    @Override
    public boolean shouldUseRenderHelper(final ItemRenderType type, final ItemStack item,
        final ItemRendererHelper helper) {
        return false;
    }

    @Override
    public void renderItem(final ItemRenderType type, final ItemStack toolboxStack, final Object... data) {
        ItemToolbox.getSelectedTool(toolboxStack)
            .ifPresent(selectedTool -> META_TOOL_RENDERER.renderItem(type, selectedTool, data));
    }
}
