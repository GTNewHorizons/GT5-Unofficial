package kubatech.client.renderer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import com.gtnewhorizon.gtnhlib.util.ItemRenderUtil;

import gregtech.api.enums.Materials;
import kubatech.loaders.HTGRLoader;

public class HTGRItemRenderer implements IItemRenderer {

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return item != null && HTGRLoader.HTGR_ITEM.getItemMaterial(item) != null;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return false;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
        if (item == null) return;
        Materials material = HTGRLoader.HTGR_ITEM.getItemMaterial(item);
        if (material == null) return;
        Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationItemsTexture);
        IIcon icon = item.getIconIndex();

        int color = material.mColor.rgba;
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor3ub((byte) ((color >>> 24) & 0xff), (byte) ((color >>> 16) & 0xff), (byte) ((color >>> 8) & 0xff));
        ItemRenderUtil.renderItem(type, icon);
        GL11.glColor3ub((byte) -1, (byte) -1, (byte) -1);
    }
}
