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

    private static final ResourceLocation inner_model_texture =
            new ResourceLocation(MODID ,"models/Earth.png");

    private static final ResourceLocation outer_model_texture = new ResourceLocation(MODID ,"models/OuterEOH.png");

    public static IModelCustom modelCustom;

    public RenderEyeOfHarmony() {
        modelCustom =
                AdvancedModelLoader.loadModel(new ResourceLocation(MODID, "models/Earth.obj"));
    }

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float timeSinceLastTick) {
        if (!(tile instanceof TileEyeOfHarmony)) return;

        TileEyeOfHarmony EOHRenderTile = (TileEyeOfHarmony) tile;
        float scale = 0.01f * EOHRenderTile.getSize();

        GL11.glPushMatrix();
        GL11.glColor4f(1F, 1F, 1F, 1F);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 150f, 150f);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glTranslated(x + 0.5, y + 0.5, z + 0.5);
        FMLClientHandler.instance().getClient().getTextureManager().bindTexture(inner_model_texture);

        float brightness = (float) Math.abs(Math.sin((float) Minecraft.getSystemTime() / 3000f) * 100f);

        float starRed = 0.3f;
        float starGreen = 0.0f;
        float starBlue = 0.3f;

        GL11.glScalef(scale, scale, scale);

            GL11.glPushMatrix();
            GL11.glRotatef(-180, 1F, 0F, 1F);
            GL11.glRotatef(90, 1F, 0F, 0F);
            GL11.glColor4d(starRed, starGreen, starBlue, 1F);
            if (EOHRenderTile.getRotationSpeed() != 0) {
                GL11.glRotatef((System.currentTimeMillis() / EOHRenderTile.getRotationSpeed()) % 360, 0F, 0F, 1F);
            }
            //        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 80f + brightness, 80f +
            // brightness);
            modelCustom.renderAll();
            GL11.glPopMatrix();

        float outerShellScaleIncrease = 1.05f;
        GL11.glScalef(outerShellScaleIncrease, outerShellScaleIncrease, outerShellScaleIncrease);
        GL11.glDepthMask(false);
        FMLClientHandler.instance().getClient().getTextureManager().bindTexture(outer_model_texture);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 200F, 200F);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(starRed, starGreen, starBlue, 0.3F);
        modelCustom.renderAll();
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_LIGHTING);




        GL11.glColor4d(starRed, starGreen, starBlue, 0.1F);


        GL11.glPopMatrix();
    }
}
