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
import static com.github.technus.tectech.TecTech.RANDOM;
import static java.lang.Math.pow;


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

        {
            GL11.glPushMatrix();
            GL11.glTranslated(x + 0.5, y + 0.5, z + 0.5);

            renderStarLayer(EOHRenderTile, 0, starLayer0, 1.0f);
            renderStarLayer(EOHRenderTile, 1, starLayer1, 0.3f);

            GL11.glPopMatrix();
        }

        // Todo remove
        tile.getWorldObj().spawnParticle(
                "largesmoke",
                x + RANDOM.nextFloat() * 0.5F,
                y + RANDOM.nextFloat() * 0.5F,
                z + RANDOM.nextFloat() * 0.5F,
                0.0,
                0,
                0);
    }

    void renderStarLayer(TileEyeOfHarmony EOHRenderTile, int layer, ResourceLocation texture, float alpha) {

        // Begin animation.
        GL11.glPushMatrix();

        // OpenGL settings, not sure exactly what these do.

        // Disables lighting, so star is always lit (I think).
        GL11.glDisable(GL11.GL_LIGHTING);
        // Culls things out of line of sight?
        GL11.glDisable(GL11.GL_CULL_FACE);
        // Merges colours of the various layers of the star?
        GL11.glEnable(GL11.GL_BLEND);
        // ???
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        // Bind animation to layer of star.
        FMLClientHandler.instance().getClient().getTextureManager().bindTexture(texture);

        // 0.01f magic number to shrink sphere obj down.
        // Size obtained from the multis current recipe.
        float scale = 0.01f * EOHRenderTile.getSize();

        // Put each subsequent layer further out.
        scale *= pow(1.05f, layer);

        // Scale the star up in the x, y and z directions.
        GL11.glScalef(scale, scale, scale);

        // Rotate star upright.
        GL11.glRotatef(-180, 1F, 0F, 1F);
        GL11.glRotatef(90, 1F, 0F, 0F);

        // Set colour and alpha (transparency) of the star layer. Set by the current recipe.
        float starRed = EOHRenderTile.getColour().getRed() / 255.0f;
        float starGreen = EOHRenderTile.getColour().getGreen() / 255.0f;
        float starBlue = EOHRenderTile.getColour().getBlue() / 255.0f;
        GL11.glColor4f(starRed, starGreen, starBlue, alpha);

        // Spin the star around according to the multis time dilation tier.
        if (EOHRenderTile.getRotationSpeed() != 0) {
            GL11.glRotatef(-(System.currentTimeMillis() / (int) EOHRenderTile.getRotationSpeed()) % 360, 0F, 0F, 1F);
        }
        modelCustom.renderAll();
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_LIGHTING);

        // Finish animation.
        GL11.glPopMatrix();
    }
}

