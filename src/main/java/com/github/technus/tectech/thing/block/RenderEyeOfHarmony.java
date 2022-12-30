package com.github.technus.tectech.thing.block;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

import static com.github.technus.tectech.Reference.MODID;


public class RenderEyeOfHarmony extends TileEntitySpecialRenderer {

    private static final ResourceLocation starLayer0 =
            new ResourceLocation(MODID ,"models/StarLayer0.png");

    private static final ResourceLocation starLayer1 = new ResourceLocation(MODID ,"models/StarLayer1.png");

    private static final ResourceLocation starLayer2 = new ResourceLocation(MODID ,"models/StarLaye1.png");


    public static IModelCustom modelCustom;

    public RenderEyeOfHarmony() {
        modelCustom =
                AdvancedModelLoader.loadModel(new ResourceLocation(MODID, "models/lowerres.obj"));
    }

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float timeSinceLastTick) {
        if (!(tile instanceof TileEyeOfHarmony)) return;

        TileEyeOfHarmony EOHRenderTile = (TileEyeOfHarmony) tile;
        float scale = 0.01f * EOHRenderTile.getSize();

        {
            GL11.glPushMatrix();
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 150f, 150f);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_CULL_FACE);
            GL11.glTranslated(x + 0.5, y + 0.5, z + 0.5);
            FMLClientHandler.instance().getClient().getTextureManager().bindTexture(starLayer0);

            float starRed = 1.0f;
            float starGreen = 1.0f;
            float starBlue = 1.0f;

            GL11.glScalef(scale, scale, scale);

            GL11.glPushMatrix();

            GL11.glColor4d(starRed, starGreen, starBlue, 1F);
            if (EOHRenderTile.getRotationSpeed() != 0) {
                GL11.glRotatef((System.currentTimeMillis() / (int) EOHRenderTile.getRotationSpeed()) % 360, 0F, 0F, 1F);
            }
            modelCustom.renderAll();
            GL11.glPopMatrix();

            float outerShellScaleIncrease = 1.05f;
            GL11.glScalef(outerShellScaleIncrease, outerShellScaleIncrease, outerShellScaleIncrease);
            GL11.glDepthMask(false);
            FMLClientHandler.instance().getClient().getTextureManager().bindTexture(starLayer1);
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 200F, 200F);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    //        GL11.glRotatef(-180, 1F, 0F, 1F);
    //        GL11.glRotatef(90, 1F, 0F, 0F);
            // Random axis of rotation.
            GL11.glRotatef(42, 1F, 1F, 0F);
            GL11.glRotatef(-139, 1F, 0F, 0F);
            GL11.glRotatef(29, 0F, 1F, 1F);
            // End.
            GL11.glColor4f(starRed, starGreen, starBlue, 0.3F);
            if (EOHRenderTile.getRotationSpeed() != 0) {
                GL11.glRotatef(-(System.currentTimeMillis() / (int) EOHRenderTile.getRotationSpeed()) % 360, 0F, 0F, 1F);
            }
            modelCustom.renderAll();
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glDepthMask(true);
            GL11.glEnable(GL11.GL_LIGHTING);


            GL11.glColor4d(starRed, starGreen, starBlue, 0.1F);


            GL11.glPopMatrix();
        }
    }
}
