package kubatech.client.renderer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import org.lwjgl.opengl.GL11;

import gregtech.common.render.GTRenderUtil;
import kubatech.loaders.tea.components.TeaItem;

public class TeaItemsRenderer implements IItemRenderer {

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return item != null && item.getItem() instanceof TeaItem;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return false;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
        TeaItem tea = (TeaItem) item.getItem();
        IIcon icon = item.getIconIndex();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GTRenderUtil.applyStandardItemTransform(type);

        assert tea != null;
        FluidStack fs = tea.getFluidStack(item, 1);
        if (fs != null) {
            IIcon fluidKey = tea.getFluidKeyIcon(item);
            Fluid fluid = fs.getFluid();
            IIcon fluidIcon = fluid.getIcon(fs);
            int fluidColor = fluid.getColor(fs);

            Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationItemsTexture);
            GL11.glBlendFunc(GL11.GL_ZERO, GL11.GL_ONE);
            GTRenderUtil.renderItem(type, fluidKey);

            Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glDepthFunc(GL11.GL_EQUAL);
            GL11.glColor3ub((byte) (fluidColor >> 16), (byte) (fluidColor >> 8), (byte) fluidColor);
            GTRenderUtil.renderItem(type, fluidIcon);
            GL11.glColor3ub((byte) -1, (byte) -1, (byte) -1);
            GL11.glDepthFunc(GL11.GL_LEQUAL);
        } else icon = tea.getEmptyIcon(item);

        Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationItemsTexture);

        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GTRenderUtil.renderItem(type, icon);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glDisable(GL11.GL_BLEND);
    }
}
