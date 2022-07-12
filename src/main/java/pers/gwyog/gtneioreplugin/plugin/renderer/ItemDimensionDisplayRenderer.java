package pers.gwyog.gtneioreplugin.plugin.renderer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;
import pers.gwyog.gtneioreplugin.plugin.item.ItemDimensionDisplay;

public class ItemDimensionDisplayRenderer implements IItemRenderer {

    private final RenderItem renderItem = new RenderItem();

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return type == ItemRenderType.INVENTORY;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return false;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack stack, Object... data) {
        String dimension = ItemDimensionDisplay.getDimension(stack);
        if (dimension == null) {
            return;
        }

        renderItem.renderItemIntoGUI(
                Minecraft.getMinecraft().fontRenderer, Minecraft.getMinecraft().renderEngine, stack, 0, 0, false);

        String prefix = getPrefix(dimension);
        if (!prefix.isEmpty()) {
            FontRenderer fontRender = Minecraft.getMinecraft().fontRenderer;
            float smallTextScale = 3F / 4F;

            GL11.glPushMatrix();
            GL11.glTranslatef(0, 0, 300);
            GL11.glScalef(smallTextScale, smallTextScale, 1.0f);

            fontRender.drawString(prefix, 0, (int) (16 / smallTextScale) - fontRender.FONT_HEIGHT + 1, 0xFFFFFF, true);

            GL11.glPopMatrix();
        }

        GL11.glDisable(GL11.GL_ALPHA_TEST);
    }

    private static String getPrefix(String dimName) {
        switch (dimName) {
            case "Mo":
                return "T1";
            case "De":
            case "Ma":
            case "Ph":
                return "T2";
            case "As":
            case "Ca":
            case "Ce":
            case "Eu":
            case "Ga":
                return "T3";
            case "Io":
            case "Me":
            case "Ve":
                return "T4";
            case "En":
            case "Mi":
            case "Ob":
            case "Ti":
                return "T5";
            case "Pr":
            case "Tr":
                return "T6";
            case "Ha":
            case "KB":
            case "MM":
            case "Pl":
                return "T7";
            case "BC":
            case "BE":
            case "BF":
            case "CB":
            case "TE":
            case "DD":
            case "VB":
                return "T8";
            default:
                return "";
        }
    }
}
