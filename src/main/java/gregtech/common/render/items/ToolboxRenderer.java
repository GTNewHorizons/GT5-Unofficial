package gregtech.common.render.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.client.IItemRenderer;

import com.gtnewhorizon.gtnhlib.util.ItemRenderUtil;

import gregtech.api.enums.ToolboxSlot;
import gregtech.common.items.ItemGTToolbox;
import gregtech.common.items.toolbox.ToolboxUtil;
import gregtech.common.render.MetaGeneratedToolRenderer;

/**
 * Handles rendering the fake offhand toolbox when a tool is selected.
 */
public class ToolboxRenderer implements IItemRenderer {

    private static final MetaGeneratedToolRenderer META_TOOL_RENDERER = new MetaGeneratedToolRenderer();

    @Override
    public boolean handleRenderType(final ItemStack itemStack, final ItemRenderType type) {
        return type == ItemRenderType.EQUIPPED_FIRST_PERSON && itemStack != null
            && itemStack.getItem() instanceof ItemGTToolbox
            && (ToolboxUtil.getSelectedToolType(itemStack)
                .isPresent()
                || itemStack.getTagCompound()
                    .hasKey(ItemGTToolbox.BROKEN_TOOL_ANIMATION_END_KEY));
    }

    @Override
    public boolean shouldUseRenderHelper(final ItemRenderType type, final ItemStack item,
        final ItemRendererHelper helper) {
        return false;
    }

    @Override
    public void renderItem(final ItemRenderType type, final ItemStack toolbox, final Object... data) {
        ToolboxUtil.getSelectedTool(toolbox)
            .ifPresent(selectedTool -> META_TOOL_RENDERER.renderItem(type, selectedTool, data));

        // Renders a slowly blinking outline of the tool the user most recently broke.
        final NBTTagCompound tag = toolbox.hasTagCompound() ? toolbox.getTagCompound() : new NBTTagCompound();
        if (tag.hasKey(ItemGTToolbox.BROKEN_TOOL_ANIMATION_END_KEY) && data[1] instanceof final EntityPlayer player) {
            ToolboxSlot.getBySlot(tag.getInteger(ItemGTToolbox.RECENTLY_BROKEN_SLOT_KEY)).ifPresent(slot -> {
                final long ticksRemaining = tag.getLong(ItemGTToolbox.BROKEN_TOOL_ANIMATION_END_KEY) - player.getEntityWorld().getTotalWorldTime();

                if ((ticksRemaining % 20) / 10 == 0 && slot.getIcon().isPresent()) {
                    ItemRenderUtil.renderItem(type, slot.getIcon().get());
                }
            });
        }
    }
}
