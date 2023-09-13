package gregtech.api.CustomStructureRendering.Base.Util;

import gregtech.api.CustomStructureRendering.Structures.BaseModelStructure;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

public class RenderHelper {

    private static void centreModel(BaseModelStructure model) {

        String[][] testShape = model.getStructureString();

        int x = testShape.length / 2;
        int z = testShape[0][0].length() / 2;
        int y = testShape[0].length / 2;

        GL11.glTranslated(-x, -1 - y, -1 - z);
    }

    private static void rotation() {
        GL11.glRotatef((System.currentTimeMillis() / 16) % 360, 0.3f, 1, 0.5f);
    }

    private static void buildModel(World world, BaseModelStructure model) {

        Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.locationBlocksTexture);

        CustomRenderBlocks renderBlocks = new CustomRenderBlocks(world);

        for (int x = 0; x < model.getXLength(); x++) {
            for (int y = 0; y < model.getYLength(); y++) {
                for (int z = 0; z < model.getZLength(); z++) {
                    Character blockChar = model.getStructureString()[x][z].charAt(y);

                    if (blockChar.equals(' ')) continue;
                    if (model.renderFacesArray[x][z][y].allHidden()) continue;

                    BaseModelStructure.BlockInfo blockInfo = model.getAssociatedBlockInfo(blockChar);

                    renderBlocks.renderFacesInfo = model.renderFacesArray[x][z][y];
                    renderBlock(blockInfo.block, blockInfo.metadata, renderBlocks, x, z+1, y+1);
                }
            }
        }
    }

    private static void scaleModel(final BaseModelStructure model) {
        final float maxScale = 1.0f / model.maxAxisSize();
        GL11.glScalef(maxScale, maxScale, maxScale);
    }

    public static void renderModel(World world, double x, double y, double z, final BaseModelStructure model) {

        GL11.glPushMatrix();

        GL11.glTranslated(x + 0.5, y + 0.5, z + 0.5);
        scaleModel(model);

        //rotation();

        centreModel(model);

        buildModel(world, model);

        GL11.glPopMatrix();
    }

    public static void renderBlock(Block block, int metadata, CustomRenderBlocks renderBlocks, int x, int y, int z) {
        GL11.glPushMatrix();

        GL11.glTranslated(x, y, z);
        GL11.glRotated(-90, 0.0, 1.0, 0.0);
        renderBlocks.renderBlockAsItem(block, metadata, 1.0f);

        GL11.glPopMatrix();
    }


}
