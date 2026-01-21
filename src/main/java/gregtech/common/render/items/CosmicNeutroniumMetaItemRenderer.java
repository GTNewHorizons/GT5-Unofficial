package gregtech.common.render.items;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import com.gtnewhorizon.gtnhlib.util.ItemRenderUtil;

import gregtech.api.items.MetaGeneratedItem;

public class CosmicNeutroniumMetaItemRenderer implements IItemRenderer {

    @Override
    public boolean handleRenderType(final ItemStack item, final ItemRenderType type) {
        return type == ItemRenderType.EQUIPPED || type == ItemRenderType.EQUIPPED_FIRST_PERSON
            || type == ItemRenderType.INVENTORY
            || type == ItemRenderType.ENTITY;

    }

    @Override
    public boolean shouldUseRenderHelper(final ItemRenderType type, final ItemStack item,
        final ItemRendererHelper helper) {
        return type == ItemRenderType.ENTITY && helper == ItemRendererHelper.ENTITY_BOBBING
            || (helper == ItemRendererHelper.ENTITY_ROTATION && Minecraft.getMinecraft().gameSettings.fancyGraphics);

    }

    @Override
    public void renderItem(final ItemRenderType type, final ItemStack item, final Object... data) {
        if (item.getItem() instanceof MetaGeneratedItem mgItem) {
            IIcon[] icons = mgItem.mIconList[item.getItemDamage() - mgItem.mOffset];
            if (icons != null && icons.length > 0 && icons[0] != null) {

                if (type == ItemRenderType.INVENTORY) {
                    GL11.glEnable(GL11.GL_BLEND);
                    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                    GL11.glDisable(GL11.GL_ALPHA_TEST);
                    GL11.glDisable(GL11.GL_DEPTH_TEST);
                    CosmicNeutroniumRenderer.renderHalo(type);
                }

                // Restore state before rendering the actual item
                GL11.glEnable(GL11.GL_ALPHA_TEST);
                GL11.glEnable(GL11.GL_DEPTH_TEST);
                GL11.glDisable(GL11.GL_BLEND);
                GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f); // Reset color to avoid blending affecting icon

                ItemRenderUtil.renderItem(type, icons[0]);

            }
        }
    }
}
