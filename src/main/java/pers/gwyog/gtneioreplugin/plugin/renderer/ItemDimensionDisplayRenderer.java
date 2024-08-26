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

    // Renders the actual text on top of planet items.
    @Override
    public void renderItem(ItemRenderType type, ItemStack stack, Object... data) {
        String dimension = ItemDimensionDisplay.getDimension(stack);
        if (dimension == null) {
            return;
        }

        renderItem.renderItemIntoGUI(
            Minecraft.getMinecraft().fontRenderer,
            Minecraft.getMinecraft().renderEngine,
            stack,
            0,
            0,
            false);

        FontRenderer fontRender = Minecraft.getMinecraft().fontRenderer;
        float smallTextScale = 3F / 4F;

        GL11.glPushMatrix();
        GL11.glTranslatef(0, 0, 300);
        GL11.glScalef(smallTextScale, smallTextScale, 1.0f);

        long prefix = getPrefix(dimension);
        String tooltipPrefix = prefix != -1 ? "T" + prefix : "INVALID. Please, report this to the GTNH team";

        fontRender
            .drawString(tooltipPrefix, 0, (int) (16 / smallTextScale) - fontRender.FONT_HEIGHT + 1, 0xFFFFFF, true);

        GL11.glPopMatrix();

        GL11.glDisable(GL11.GL_ALPHA_TEST);
    }

    // See DimensionHelper.DimNameDisplayed for real names of these.
    public static long getPrefix(String dimName) {
        return switch (dimName) {
            case "Ow", "Ne", "TF", "ED", "VA", "EA" -> 0L;
            case "Mo" -> 1L;
            case "De", "Ma", "Ph" -> 2L;
            case "As", "Ca", "Ce", "Eu", "Ga", "Rb" -> 3L;
            case "Io", "Me", "Ve" -> 4L;
            case "En", "Mi", "Ob", "Ti", "Ra" -> 5L;
            case "Pr", "Tr" -> 6L;
            case "Ha", "KB", "MM", "Pl" -> 7L;
            case "BC", "BE", "BF", "CB", "TE", "VB" -> 8L;
            case "An", "Ho", "Np", "Mh", "MB", "Se" -> 9L;
            case "DD" -> 10L;
            default -> -1L;
        };
    }
}
