package gregtech.api.materials.bec;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import gregtech.api.interfaces.IItemTexture;
import gregtech.api.materials.ItemWithTextures;
import gregtech.common.render.GTRenderUtil;

public class GTTexturedItemRenderer implements IItemRenderer {

    public static final GTTexturedItemRenderer INSTANCE = new GTTexturedItemRenderer();

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return type != ItemRenderType.FIRST_PERSON_MAP;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return type == ItemRenderType.ENTITY && helper == ItemRendererHelper.ENTITY_BOBBING
            || (helper == ItemRendererHelper.ENTITY_ROTATION && Minecraft.getMinecraft().gameSettings.fancyGraphics);
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack stack, Object... data) {
        if (!(stack.getItem() instanceof ItemWithTextures texturedItem)) return;

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GTRenderUtil.applyStandardItemTransform(type);

        IItemTexture[] textures = texturedItem.getTextures(stack);

        if (textures != null) {
            for (IItemTexture texture : textures) {
                texture.render(type, stack);
            }
        }

        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glDisable(GL11.GL_BLEND);
    }
}
