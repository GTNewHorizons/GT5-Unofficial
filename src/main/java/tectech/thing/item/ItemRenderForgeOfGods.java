package tectech.thing.item;

import static tectech.rendering.EOH.EOHRenderingUtils.renderStarLayer;
import static tectech.rendering.EOH.EOHTileEntitySR.*;
import static tectech.thing.block.RenderForgeOfGods.disableOpaqueColorInversion;
import static tectech.thing.block.RenderForgeOfGods.enableOpaqueColorInversion;
import static tectech.thing.block.RenderForgeOfGods.enablePseudoTransparentColorInversion;

import java.awt.Color;

import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

public class ItemRenderForgeOfGods implements IItemRenderer {

    @Override
    public boolean handleRenderType(ItemStack item, IItemRenderer.ItemRenderType type) {
        return true;
    }

    @Override
    public boolean shouldUseRenderHelper(IItemRenderer.ItemRenderType type, ItemStack item,
        IItemRenderer.ItemRendererHelper helper) {
        return true;
    }

    @Override
    public void renderItem(IItemRenderer.ItemRenderType type, ItemStack item, Object... data) {
        GL11.glPushMatrix();
        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);

        if (type == IItemRenderer.ItemRenderType.INVENTORY) GL11.glRotated(180, 0, 1, 0);
        else if (type == IItemRenderer.ItemRenderType.EQUIPPED
            || type == IItemRenderer.ItemRenderType.EQUIPPED_FIRST_PERSON) {
                GL11.glTranslated(0.5, 0.5, 0.5);
                if (type == IItemRenderer.ItemRenderType.EQUIPPED) GL11.glRotated(90, 0, 1, 0);
            }

        {

            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_BLEND);

            // Innermost layer should be opaque
            enableOpaqueColorInversion();
            renderStarLayer(0, STAR_LAYER_0, new Color(1.0f, 0.4f, 0.05f, 1.0f), 1.0f, 1);
            disableOpaqueColorInversion();

            enablePseudoTransparentColorInversion();
            renderStarLayer(1, STAR_LAYER_1, new Color(1.0f, 0.4f, 0.05f, 1.0f), 0.4f, 1);
            renderStarLayer(2, STAR_LAYER_2, new Color(1.0f, 0.4f, 0.05f, 1.0f), 0.2f, 1);

            GL11.glPopAttrib();
            GL11.glPopMatrix();
        }
    }
}
