package gregtech.common.render;

import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.ItemList;
import gregtech.common.items.GT_FluidDisplayItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;
import org.lwjgl.opengl.GL11;

@SideOnly(cpw.mods.fml.relauncher.Side.CLIENT)
public class GT_FluidDisplayStackRenderer implements IItemRenderer {

    public GT_FluidDisplayStackRenderer() {
        MinecraftForgeClient.registerItemRenderer(ItemList.Display_Fluid.getItem(), this);
    }

    @Override
    public boolean handleRenderType (ItemStack item, ItemRenderType type)
    {
        if(!item.hasTagCompound())
            return false;
        return type == ItemRenderType.INVENTORY;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper)
    {
        //not sure what this does.
        return false;
    }

    @Override
    public void renderItem (ItemRenderType type, ItemStack item, Object... data) {
        if (item == null || item.getItem() == null || !(item.getItem() instanceof GT_FluidDisplayItem))
            return;

        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_ALPHA_TEST);

        IIcon icon = item.getItem().getIconFromDamage(item.getItemDamage());

        Tessellator tess = Tessellator.instance;
        tess.startDrawingQuads();
        // draw a simple rectangle for the inventory icon
        final float x_min = icon.getMinU();
        final float x_max = icon.getMaxU();
        final float y_min = icon.getMinV();
        final float y_max = icon.getMaxV();
        tess.addVertexWithUV( 0, 16, 0, x_min, y_max);
        tess.addVertexWithUV(16, 16, 0, x_max, y_max);
        tess.addVertexWithUV(16,  0, 0, x_max, y_min);
        tess.addVertexWithUV( 0,  0, 0, x_min, y_min);
        tess.draw();

        if(item.getTagCompound() == null) {
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glDisable(GL11.GL_ALPHA_TEST);
            return;
        }

        // Render Fluid amount text
        long fluidAmount = item.getTagCompound().getLong("mFluidDisplayAmount");
        if (fluidAmount > 0L && !item.getTagCompound().getBoolean("mHideStackSize")) {
            String amountString;

            if (fluidAmount < 10000) {
                amountString = "" + fluidAmount + "L";
            } else {
                int exp = (int) (Math.log(fluidAmount) / Math.log(1000));
                double shortAmount = fluidAmount / Math.pow(1000, exp);
                if ( shortAmount >= 100) {
                    amountString = String.format("%.0f%cL", shortAmount, "kMGTPE".charAt(exp - 1)); //heard it here first, PetaLiters
                } else if ( shortAmount >= 10) {
                    amountString = String.format("%.1f%cL", shortAmount, "kMGTPE".charAt(exp - 1));
                } else {
                    amountString = String.format("%.2f%cL", shortAmount, "kMGTPE".charAt(exp - 1));
                }
            }

            FontRenderer fontRender = Minecraft.getMinecraft().fontRenderer;
            float smallTextScale = fontRender.getUnicodeFlag() ? 3F/4F : 1F/2F;
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glScalef(smallTextScale, smallTextScale, 1.0f);

            fontRender.drawString( amountString, 0, (int) (16/smallTextScale) - fontRender.FONT_HEIGHT + 1, 0xFFFFFF, true);
            GL11.glScalef(1f, 1f, 1f);
            GL11.glDisable(GL11.GL_ALPHA_TEST);
        }
    }
}
