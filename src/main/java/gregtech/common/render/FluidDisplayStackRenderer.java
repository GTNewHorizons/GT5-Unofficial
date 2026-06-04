package gregtech.common.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import org.lwjgl.opengl.GL11;

import appeng.util.ReadableNumberConverter;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.CondensateType;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.IOreMaterial;
import gregtech.client.handler.CondensateAnimationTickHandler;
import gregtech.common.items.ItemFluidDisplay;
import gtPlusPlus.core.item.base.BaseItemComponent;
import gtPlusPlus.core.material.Material;

@SideOnly(Side.CLIENT)
public class FluidDisplayStackRenderer implements IItemRenderer {

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

        Fluid fluid = FluidRegistry.getFluid(item.getItemDamage());
        IOreMaterial baseMaterial = ItemFluidDisplay.getMaterial(fluid);
        Materials associatedFluidMaterial = Materials.get(item.stackTagCompound.getString("mFluidMaterialName"));
        if (associatedFluidMaterial.renderer == null
            || !associatedFluidMaterial.renderer.renderFluidDisplayItem(type, item, data)) {
            IIcon icon = item.getItem()
                .getIconFromDamage(item.getItemDamage());
            int tint;
            CondensateType condensate = CondensateType.getCondensateType(fluid);
            if (baseMaterial instanceof Material gtppMaterial && gtppMaterial.getRGBA()[3] > 1) {
                tint = BaseItemComponent.getMaterialCustomColor(gtppMaterial);
            } else if (condensate != null) {
                tint = CondensateType.getRenderColor(fluid);
            } else {
                tint = fluid != null ? fluid.getColor() : 0xFFFFFF;
            }
            GL11.glColor3ub((byte) (tint >> 16 & 0xFF), (byte) (tint >> 8 & 0xFF), (byte) (tint & 0xFF));
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

            if (condensate != null) {
                Minecraft.getMinecraft()
                    .getTextureManager()
                    .bindTexture(CondensateAnimationTickHandler.texture);
                GL11.glColor4f(1f, 1f, 1f, 1f);
                final float vMin = CondensateAnimationTickHandler.currentFrame / 64f;
                final float vMax = (CondensateAnimationTickHandler.currentFrame + 1) / 64f;
                tess.startDrawingQuads();
                tess.addVertexWithUV(0, 16, 0, 0, vMax);
                tess.addVertexWithUV(16, 16, 0, 1, vMax);
                tess.addVertexWithUV(16, 0, 0, 1, vMin);
                tess.addVertexWithUV(0, 0, 0, 0, vMin);
                tess.draw();
            }
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
                amountString = fluidAmount + "L";
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
