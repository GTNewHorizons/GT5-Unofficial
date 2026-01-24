package gregtech.common.render.items;

import static gregtech.common.tileentities.machines.multi.nanochip.util.CircuitComponent.tryGetFromFakeStack;
import static gregtech.loaders.ExtraIcons.circuitComponentOverlay;
import static net.minecraftforge.client.IItemRenderer.ItemRenderType.INVENTORY;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;

import org.lwjgl.opengl.GL11;

import com.gtnewhorizon.gtnhlib.util.ItemRenderUtil;

import gregtech.common.tileentities.machines.multi.nanochip.util.CircuitComponent;

public class CircuitComponentItemRenderer implements IItemRenderer {

    @Override
    public boolean handleRenderType(final ItemStack item, final ItemRenderType type) {
        return type == INVENTORY;
    }

    @Override
    public boolean shouldUseRenderHelper(final ItemRenderType type, final ItemStack item,
        final ItemRendererHelper helper) {
        return false;
    }

    @Override
    public void renderItem(final ItemRenderType type, final ItemStack item, final Object... data) {
        CircuitComponent cc = tryGetFromFakeStack(item);
        if (cc == null) return;
        if (!cc.isProcessed) {
            GL11.glPushMatrix();
            GL11.glTranslatef(2.4f, 2.4f, 0);
            GL11.glScalef(0.7f, 0.7f, 0);

            ItemStack realItem = cc.realComponent.get();

            if (realItem.getItem() instanceof ItemBlock) {
                RenderHelper.enableGUIStandardItemLighting();
                RenderItem.getInstance()
                    .renderItemIntoGUI(
                        Minecraft.getMinecraft().fontRenderer,
                        Minecraft.getMinecraft().renderEngine,
                        realItem,
                        0,
                        0,
                        false);
                RenderHelper.disableStandardItemLighting();
            } else {
                IItemRenderer baseRenderer = MinecraftForgeClient.getItemRenderer(realItem, INVENTORY);
                if (baseRenderer != null) {
                    baseRenderer.renderItem(type, realItem, data);
                }
            }

            GL11.glPopMatrix();

            GL11.glPushMatrix();
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

            Minecraft.getMinecraft()
                .getTextureManager()
                .bindTexture(TextureMap.locationItemsTexture);
            GL11.glTranslatef(0f, 0f, 2f);
            ItemRenderUtil.renderItem(type, circuitComponentOverlay);

            GL11.glDisable(GL11.GL_BLEND);

            GL11.glPopMatrix();

            RenderHelper.enableGUIStandardItemLighting();
        } else {
            ItemRenderUtil.renderItem(type, item.getIconIndex());
        }
    }
}
