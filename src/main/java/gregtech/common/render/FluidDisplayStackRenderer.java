package gregtech.common.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;

import org.lwjgl.opengl.GL11;

import appeng.util.ReadableNumberConverter;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.common.items.ItemFluidDisplay;

@SideOnly(Side.CLIENT)
public class FluidDisplayStackRenderer implements IItemRenderer {

    public FluidDisplayStackRenderer() {
        MinecraftForgeClient.registerItemRenderer(ItemList.Display_Fluid.getItem(), this);
    }

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        if (!item.hasTagCompound()) return false;
        return type == ItemRenderType.INVENTORY;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        // not sure what this does.
        return false;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
        if (item == null || item.getItem() == null || !(item.getItem() instanceof ItemFluidDisplay)) return;

        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_ALPHA_TEST);

        Materials associatedFluidMaterial = Materials.get(item.stackTagCompound.getString("mFluidMaterialName"));
        if (associatedFluidMaterial.renderer == null
            || !associatedFluidMaterial.renderer.renderFluidDisplayItem(type, item, data)) {
            IIcon icon = item.getItem()
                .getIconFromDamage(item.getItemDamage());
            Tessellator tess = Tessellator.instance;
            tess.startDrawingQuads();
            // draw a simple rectangle for the inventory icon
            final float x_min = icon.getMinU();
            final float x_max = icon.getMaxU();
            final float y_min = icon.getMinV();
            final float y_max = icon.getMaxV();
            tess.addVertexWithUV(0, 16, 0, x_min, y_max);
            tess.addVertexWithUV(16, 16, 0, x_max, y_max);
            tess.addVertexWithUV(16, 0, 0, x_max, y_min);
            tess.addVertexWithUV(0, 0, 0, x_min, y_min);
            tess.draw();
        }

        if (item.getTagCompound() == null) {
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glDisable(GL11.GL_ALPHA_TEST);
            return;
        }

        // Render Fluid amount text
        long fluidAmount = item.getTagCompound()
            .getLong("mFluidDisplayAmount");
        if (fluidAmount > 0L && !item.getTagCompound()
            .getBoolean("mHideStackSize")) {
            String amountString;

            if (fluidAmount < 10_000) {
                amountString = "" + fluidAmount + "L";
            } else {
                amountString = ReadableNumberConverter.INSTANCE.toWideReadableForm(fluidAmount) + "L";
            }

            FontRenderer fontRender = Minecraft.getMinecraft().fontRenderer;
            float smallTextScale = fontRender.getUnicodeFlag() ? 3F / 4F : 1F / 2F;
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glPushMatrix();
            GL11.glScalef(smallTextScale, smallTextScale, 1.0f);

            fontRender
                .drawString(amountString, 0, (int) (16 / smallTextScale) - fontRender.FONT_HEIGHT + 1, 0xFFFFFF, true);
            GL11.glPopMatrix();
            GL11.glDisable(GL11.GL_ALPHA_TEST);
        }
    }
}
