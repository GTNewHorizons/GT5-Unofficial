package com.github.technus.tectech.thing.item;

import static com.github.technus.tectech.rendering.EOH.EOH_TESR.*;
import static java.lang.Math.pow;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;

public class RenderForgeOfGodsItem implements IItemRenderer {

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

        if (type == IItemRenderer.ItemRenderType.INVENTORY) GL11.glRotated(180, 0, 1, 0);
        else if (type == IItemRenderer.ItemRenderType.EQUIPPED
                || type == IItemRenderer.ItemRenderType.EQUIPPED_FIRST_PERSON) {
                    GL11.glTranslated(0.5, 0.5, 0.5);
                    if (type == IItemRenderer.ItemRenderType.EQUIPPED) GL11.glRotated(90, 0, 1, 0);
                }

        // Render star stuff
        renderStarLayer(0, STAR_LAYER_0, 1.0f);
        renderStarLayer(1, STAR_LAYER_1, 0.4f);
        renderStarLayer(2, STAR_LAYER_2, 0.2f);

        GL11.glPopMatrix();
    }

    private void renderStarLayer(int layer, ResourceLocation texture, float alpha) {

        GL11.glPushMatrix();

        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        FMLClientHandler.instance().getClient().getTextureManager().bindTexture(texture);

        // 0.01f magic number to shrink sphere obj down
        float scale = 0.01f;

        // Put each layer further out
        scale *= pow(1.04f, layer);

        // Scale the star up in the x, y and z directions
        GL11.glScalef(scale, scale, scale);

        switch (layer) {
            case 0:
                GL11.glRotatef(130 + (System.currentTimeMillis() / 64) % 360, 0F, 1F, 1F);
                break;
            case 1:
                GL11.glRotatef(-49 + (System.currentTimeMillis() / 64) % 360, 1F, 1F, 0F);
                break;
            case 2:
                GL11.glRotatef(67 + (System.currentTimeMillis() / 64) % 360, 1F, 0F, 1F);
                break;
        }

        // Set colour and alpha of the star layer
        GL11.glColor4f(222, 243, 250, alpha);

        starModel.renderAll();
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_LIGHTING);

        GL11.glPopMatrix();
    }
}
