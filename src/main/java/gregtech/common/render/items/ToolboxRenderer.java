package gregtech.common.render.items;

import gregtech.common.items.toolbox.ToolboxUtil;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;

import gregtech.common.items.ItemGTToolbox;
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
            && itemStack.getItem() instanceof ItemGTToolbox
            && ToolboxUtil.getSelectedToolType(itemStack)
                .isPresent();
    }

    @Override
    public boolean shouldUseRenderHelper(final ItemRenderType type, final ItemStack item,
        final ItemRendererHelper helper) {
        return false;
    }

    @Override
    public void renderItem(final ItemRenderType type, final ItemStack toolboxStack, final Object... data) {
        ToolboxUtil.getSelectedTool(toolboxStack)
            .ifPresent(selectedTool -> META_TOOL_RENDERER.renderItem(type, selectedTool, data));
    }
}
