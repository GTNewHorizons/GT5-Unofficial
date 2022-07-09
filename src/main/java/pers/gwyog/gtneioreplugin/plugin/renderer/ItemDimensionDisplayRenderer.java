package pers.gwyog.gtneioreplugin.plugin.renderer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;
import org.lwjgl.opengl.GL11;
import pers.gwyog.gtneioreplugin.GTNEIOrePlugin;
import pers.gwyog.gtneioreplugin.plugin.items.ItemDimensionDisplay;
import pers.gwyog.gtneioreplugin.plugin.items.ModItems;

public class ItemDimensionDisplayRenderer implements IItemRenderer {

    public ItemDimensionDisplayRenderer() {
        MinecraftForgeClient.registerItemRenderer(ModItems.itemDimensionDisplay, this);
    }

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return true;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return false;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
        String dimension = ItemDimensionDisplay.getDimension(item);
        if (dimension == null) {
            return;
        }

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_ALPHA_TEST);

        Minecraft.getMinecraft().renderEngine.bindTexture(getResourceLocation(dimension));

        Tessellator tess = Tessellator.instance;
        tess.startDrawingQuads();
        tess.addVertexWithUV(0, 16, 0, 0, 1);
        tess.addVertexWithUV(16, 16, 0, 1, 1);
        tess.addVertexWithUV(16, 0, 0, 1, 0);
        tess.addVertexWithUV(0, 0, 0, 0, 0);
        tess.draw();

        GL11.glDisable(GL11.GL_BLEND);

        String prefix = getPrefix(dimension);
        if (!prefix.isEmpty()) {
            FontRenderer fontRender = Minecraft.getMinecraft().fontRenderer;
            float smallTextScale = fontRender.getUnicodeFlag() ? 3F / 4F : 1F / 2F;
            GL11.glScalef(smallTextScale, smallTextScale, 1.0f);

            fontRender.drawString(prefix, 0, (int) (16 / smallTextScale) - fontRender.FONT_HEIGHT + 1, 0xFFFFFF, true);
        }

        GL11.glDisable(GL11.GL_ALPHA_TEST);
    }

    private ResourceLocation getResourceLocation(String dimension) {
        switch (dimension) {
            case "Ow":
            case "Ne":
            case "TF":
            case "EN":
            case "VA":
            case "EA":
                return new ResourceLocation(
                        GTNEIOrePlugin.MODID, String.format("textures/items/dimensionDisplay/%s.png", dimension));
            case "Mo":
                return new ResourceLocation("galacticraftcore:textures/gui/celestialbodies/moon.png");
            case "De":
                return new ResourceLocation("galaxyspace:textures/gui/celestialbodies/deimos.png");
            case "Ma":
                return new ResourceLocation("galacticraftcore:textures/gui/celestialbodies/mars.png");
            case "Ph":
                return new ResourceLocation("galaxyspace:textures/gui/celestialbodies/phobos.png");
            case "As":
                return new ResourceLocation("galacticraftcore:textures/gui/celestialbodies/asteroid.png");
            case "Ca":
                return new ResourceLocation("galaxyspace:textures/gui/celestialbodies/callisto.png");
            case "Ce":
                return new ResourceLocation("galaxyspace:textures/gui/celestialbodies/ceres.png");
            case "Eu":
                return new ResourceLocation("galaxyspace:textures/gui/celestialbodies/europa.png");
            case "Ga":
                return new ResourceLocation("galaxyspace:textures/gui/celestialbodies/ganymede.png");
            case "Io":
                return new ResourceLocation("galaxyspace:textures/gui/celestialbodies/io.png");
            case "Me":
                return new ResourceLocation("galaxyspace:textures/gui/celestialbodies/mercury.png");
            case "Ve":
                return new ResourceLocation("galacticraftcore:textures/gui/celestialbodies/venus.png");
            case "En":
                return new ResourceLocation("galaxyspace:textures/gui/celestialbodies/enceladus.png");
            case "Mi":
                return new ResourceLocation("galaxyspace:textures/gui/celestialbodies/miranda.png");
            case "Ob":
                return new ResourceLocation("galaxyspace:textures/gui/celestialbodies/oberon.png");
            case "Ti":
                return new ResourceLocation("galaxyspace:textures/gui/celestialbodies/titan.png");
            case "Pr":
                return new ResourceLocation("galaxyspace:textures/gui/celestialbodies/proteus.png");
            case "Tr":
                return new ResourceLocation("galaxyspace:textures/gui/celestialbodies/triton.png");
            case "Ha":
                return new ResourceLocation("galaxyspace:textures/gui/celestialbodies/haumea.png");
            case "KB":
                return new ResourceLocation("galaxyspace:textures/gui/celestialbodies/kuiperbelt.png");
            case "MM":
                return new ResourceLocation("galaxyspace:textures/gui/celestialbodies/makemake.png");
            case "Pl":
                return new ResourceLocation("galaxyspace:textures/gui/celestialbodies/pluto.png");
            case "BC":
                return new ResourceLocation("galaxyspace:textures/gui/celestialbodies/barnards/Barnarda2.png");
            case "BE":
                return new ResourceLocation("galaxyspace:textures/gui/celestialbodies/barnards/Barnarda4.png");
            case "BF":
                return new ResourceLocation("galaxyspace:textures/gui/celestialbodies/barnards/Barnarda5.png");
            case "CB":
                return new ResourceLocation("galaxyspace:textures/gui/celestialbodies/aCentauri/CentauriBb.png");
            case "TE":
                return new ResourceLocation("galaxyspace:textures/gui/celestialbodies/tceti/TCetiE.png");
            case "DD":
                return new ResourceLocation("extrautils:textures/blocks/dark_portal.png");
            case "VB":
                return new ResourceLocation("galaxyspace:textures/gui/celestialbodies/vega/Vega1.png");
            default:
                return new ResourceLocation(GTNEIOrePlugin.MODID, "textures/items/dimensionDisplay/_unknown.png");
        }
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
