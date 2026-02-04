package tectech.thing.item;

import static tectech.rendering.EOH.EOHRenderingUtils.renderGORGEStar;
import static tectech.thing.block.RenderForgeOfGods.disableOpaqueColorInversion;
import static tectech.thing.block.RenderForgeOfGods.enableOpaqueColorInversion;
import static tectech.thing.block.RenderForgeOfGods.enablePseudoTransparentColorInversion;

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
            enableOpaqueColorInversion();
            renderGORGEStar(type, 0L, 0.82);
            disableOpaqueColorInversion();

            enablePseudoTransparentColorInversion();
        }

        GL11.glPopAttrib();
        GL11.glPopMatrix();
    }
}
