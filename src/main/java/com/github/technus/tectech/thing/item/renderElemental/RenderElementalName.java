package com.github.technus.tectech.thing.item.renderElemental;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import com.github.technus.tectech.font.TecTechFontRender;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderElementalName implements IItemRenderer {

    public static final RenderElementalName INSTANCE = new RenderElementalName();

    public boolean handleRenderType(final ItemStack itemStack, final IItemRenderer.ItemRenderType type) {
        return type == ItemRenderType.INVENTORY;
    }

    public boolean shouldUseRenderHelper(final IItemRenderer.ItemRenderType type, final ItemStack itemStack,
            final IItemRenderer.ItemRendererHelper helper) {
        return false;
    }

    public void renderItem(final IItemRenderer.ItemRenderType type, final ItemStack itemStack, final Object... data) {
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPushMatrix();
        Item item = itemStack.getItem();
        final Tessellator tessellator = Tessellator.instance;
        for (int i = 0; i < item.getRenderPasses(itemStack.getItemDamage()); i++) {
            IIcon icon = item.getIcon(itemStack, i);
            tessellator.startDrawingQuads();
            tessellator.addVertexWithUV(0.0, 0.0, 0.0, icon.getMinU(), icon.getMinV());
            tessellator.addVertexWithUV(0.0, 16, 0.0, icon.getMinU(), icon.getMaxV());
            tessellator.addVertexWithUV(16, 16, 0.0, icon.getMaxU(), icon.getMaxV());
            tessellator.addVertexWithUV(16, 0.0, 0.0, icon.getMaxU(), icon.getMinV());
            tessellator.draw();
        }
        String sym = ((IElementalItem) item).getSymbol(itemStack, (int) (System.currentTimeMillis() / 1000));
        if (sym != null) {
            if (sym.length() > 4) {
                sym = "...";
            }

            GL11.glPushMatrix();
            GL11.glTranslatef(.8F, .8F, 0F);
            TecTechFontRender.INSTANCE.drawSplitString(sym, 0, 0, 16, 0x222200);
            GL11.glTranslatef(-1.6f, 0, 0);
            TecTechFontRender.INSTANCE.drawSplitString(sym, 0, 0, 16, 0x222200);
            GL11.glTranslatef(0, -1.6f, 0);
            TecTechFontRender.INSTANCE.drawSplitString(sym, 0, 0, 16, 0x222200);
            GL11.glTranslatef(0, 1.6f, 0);
            TecTechFontRender.INSTANCE.drawSplitString(sym, 0, 0, 16, 0x222200);
            GL11.glPopMatrix();

            GL11.glPushMatrix();
            GL11.glTranslatef(.4F, .4F, 0F);
            TecTechFontRender.INSTANCE.drawSplitString(sym, 0, 0, 16, 0x222200);
            GL11.glTranslatef(-.8f, 0, 0);
            TecTechFontRender.INSTANCE.drawSplitString(sym, 0, 0, 16, 0x222200);
            GL11.glTranslatef(0, -.8f, 0);
            TecTechFontRender.INSTANCE.drawSplitString(sym, 0, 0, 16, 0x222200);
            GL11.glTranslatef(0, .8f, 0);
            TecTechFontRender.INSTANCE.drawSplitString(sym, 0, 0, 16, 0x222200);
            GL11.glPopMatrix();

            TecTechFontRender.INSTANCE.drawSplitString(sym, 0, 0, 16, 0xffff00);
        }
        GL11.glPopMatrix();
    }
}
