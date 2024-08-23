package com.github.technus.tectech.thing.block;

import static com.github.technus.tectech.Reference.MODID;
import static java.lang.Math.pow;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;

public class RenderForgeOfGods extends TileEntitySpecialRenderer {

    public static final ResourceLocation STAR_LAYER_0 = new ResourceLocation(MODID, "models/StarLayer0.png");
    public static final ResourceLocation STAR_LAYER_1 = new ResourceLocation(MODID, "models/StarLayer1.png");
    public static final ResourceLocation STAR_LAYER_2 = new ResourceLocation(MODID, "models/StarLayer2.png");
    public static IModelCustom starModel;
    private static IModelCustom spaceModel;
    private static final float RED = 180 / 255f;
    private static final float GREEN = 180 / 255f;
    private static final float BLUE = 255 / 255f;

    public RenderForgeOfGods() {
        starModel = AdvancedModelLoader.loadModel(new ResourceLocation(MODID, "models/Star.obj"));
        spaceModel = AdvancedModelLoader.loadModel(new ResourceLocation(MODID, "models/Space.obj"));
    }

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float timeSinceLastTick) {
        if (!(tile instanceof TileForgeOfGods)) return;

        TileForgeOfGods FOGRenderTile = (TileForgeOfGods) tile;

        // Render outer space layer.

        {
            GL11.glPushMatrix();
            GL11.glTranslated(x + 0.5, y + 0.5, z + 0.5);
            renderOuterSpaceShell();

            // Render star stuff.
            renderStarLayer(FOGRenderTile, 0, STAR_LAYER_0, 1.0f);
            renderStarLayer(FOGRenderTile, 1, STAR_LAYER_1, 0.4f);
            renderStarLayer(FOGRenderTile, 2, STAR_LAYER_2, 0.2f);

            GL11.glPopMatrix();
        }
    }

    public static void renderOuterSpaceShell() {

        GL11.glPushMatrix();

        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_BLEND);

        FMLClientHandler.instance()
            .getClient()
            .getTextureManager()
            .bindTexture(new ResourceLocation(MODID, "models/spaceLayer.png"));

        final float scale = 0.01f * 17.5f;
        GL11.glScalef(scale, scale, scale);

        GL11.glColor4f(222 / 255f, 243 / 255f, 250 / 255f, 255 / 255f);

        spaceModel.renderAll();
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_LIGHTING);

        GL11.glPopMatrix();
    }

    private static final float STAR_RESCALE = 0.2f;

    private void renderStarLayer(TileForgeOfGods FOGRenderTile, int layer, ResourceLocation texture, float alpha) {

        GL11.glPushMatrix();

        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        FMLClientHandler.instance()
            .getClient()
            .getTextureManager()
            .bindTexture(texture);

        // 0.01f magic number to shrink sphere obj down
        float scale = 0.01f * STAR_RESCALE * FOGRenderTile.getRenderSize();

        // Put each layer further out
        scale *= pow(1.04f, layer);

        // Scale the star up in the x, y and z directions
        GL11.glScalef(scale, scale, scale);

        switch (layer) {
            case 0:
                GL11.glRotatef(130, 0F, 1F, 1F);
                break;
            case 1:
                GL11.glRotatef(-49, 1F, 1F, 0F);
                break;
            case 2:
                GL11.glRotatef(67, 1F, 0F, 1F);
                break;
        }

        // Set color and alpha of the star layer
        GL11.glColor4f(RED, GREEN, BLUE, alpha);

        // Spin the star
        GL11.glRotatef((0.03f * FOGRenderTile.angle * FOGRenderTile.getRenderRotationSpeed()) % 360.0f, 0F, 0F, 1F);

        starModel.renderAll();
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_LIGHTING);

        GL11.glPopMatrix();
    }
}
