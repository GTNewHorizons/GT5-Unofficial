package com.github.technus.tectech.thing.block;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.IIcon;
import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import pers.gwyog.gtneioreplugin.plugin.block.ModBlocks;

import javax.vecmath.Vector3d;

import static com.github.technus.tectech.Reference.MODID;
import static gregtech.common.render.GT_Renderer_Block.renderStandardBlock;
import static java.lang.Math.*;

public class RenderEyeOfHarmony extends TileEntitySpecialRenderer {

    private static final ResourceLocation starLayer0 = new ResourceLocation(MODID ,"models/StarLayer0.png");
    private static final ResourceLocation starLayer1 = new ResourceLocation(MODID ,"models/StarLayer1.png");
    private static final ResourceLocation starLayer2 = new ResourceLocation(MODID ,"models/StarLayer2.png");
    private static final ResourceLocation blackHole = new ResourceLocation(MODID, "models/blackHole.png");
    public static IModelCustom modelCustom;

    public RenderEyeOfHarmony() {
        modelCustom =
                AdvancedModelLoader.loadModel(new ResourceLocation(MODID, "models/Star.obj"));
    }

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float timeSinceLastTick) {
        if (!(tile instanceof TileEyeOfHarmony)) return;

//        System.out.println("NAME " + this.field_147501_a.field_147551_g.getCommandSenderName());
//        EntityPlayer p = Minecraft.getMinecraft().thePlayer;
//        System.out.println("TESTING NAME " + p.getCommandSenderName());
//        System.out.println("TESTING X " + p.getLookVec().xCoord);
//        System.out.println("TESTING Y " + p.getLookVec().yCoord);
//        System.out.println("TESTING Z " + p.getLookVec().zCoord);

//        System.out.println(this.field_147501_a.field_147551_g.get());

        TileEyeOfHarmony EOHRenderTile = (TileEyeOfHarmony) tile;

        {
            GL11.glPushMatrix();
            GL11.glTranslated(x + 0.5, y + 0.5, z + 0.5);

            GL11.glPushMatrix();
            GL11.glRotatef((System.currentTimeMillis() / 32) % 360, 0F, 1F, 0F);
            GL11.glTranslated(-3, 0, 0);
            GL11.glRotatef((System.currentTimeMillis() / 2) % 360, 0F, 1F, 0F);
            renderBlockInWorld(ModBlocks.blocks.get("DD"), 0, 1.0f);
            GL11.glPopMatrix();

            GL11.glPushMatrix();
            GL11.glTranslated(6, 0, 0);
            renderBlockInWorld(ModBlocks.blocks.get("Ne"), 0, 0.5f);
            GL11.glPopMatrix();

            GL11.glPushMatrix();
            GL11.glTranslated(0, 0, 6);
            renderBlockInWorld(ModBlocks.blocks.get("Mo"), 0, 1.0f);
            GL11.glPopMatrix();

            GL11.glPushMatrix();
            GL11.glTranslated(0, 0, -6);
            renderBlockInWorld(ModBlocks.blocks.get("Ow"), 0, 2.0f);
            GL11.glPopMatrix();


            if (EOHRenderTile.getTier() < 9) {
                renderStarLayer(EOHRenderTile, 0, starLayer0, 1.0f);
                renderStarLayer(EOHRenderTile, 1, starLayer1, 0.4f);
                renderStarLayer(EOHRenderTile, 2, starLayer2, 0.2f);
            } else {
                renderStarLayer(EOHRenderTile, 0, blackHole, 1.0f);
            }

            GL11.glPopMatrix();
        }

    }


    void renderStarLayer(TileEyeOfHarmony EOHRenderTile, int layer, ResourceLocation texture, float alpha) {

        // Begin animation.
        GL11.glPushMatrix();

        // OpenGL settings, not sure exactly what these do.

        // Disables lighting, so star is always lit (I think).
        GL11.glDisable(GL11.GL_LIGHTING);
        // Culls things out of line of sight?
        GL11.glEnable(GL11.GL_CULL_FACE);
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
//        GL11.glRotatef(180, 1F, 1F, 1F);

        switch (layer) {
            case 0:
                GL11.glRotatef(194, 0F, 1F, 1F);
                break;
            case 1:
                GL11.glRotatef(-49, 1F, 1F, 0F);
                break;
            case 2:
                GL11.glRotatef(67, 1F, 0F, 1F);
                break;
        }

        // Set colour and alpha (transparency) of the star layer. Set by the current recipe.
        float starRed = EOHRenderTile.getColour().getRed() / 255.0f;
        float starGreen = EOHRenderTile.getColour().getGreen() / 255.0f;
        float starBlue = EOHRenderTile.getColour().getBlue() / 255.0f;
        GL11.glColor4f(starRed, starGreen, starBlue, alpha);

        // Spin the star around according to the multis time dilation tier.
        if (EOHRenderTile.getRotationSpeed() != 0) {
            GL11.glRotatef((System.currentTimeMillis() / (int) EOHRenderTile.getRotationSpeed()) % 360, 0F, 0F, 1F);
        }

        modelCustom.renderAll();
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_LIGHTING);

        // Finish animation.
        GL11.glPopMatrix();
    }


    class SphericalCoord {



    }
    class RotationInfo {

        float angle;
        boolean xEnabled = false;
        boolean yEnabled = false;
        boolean zEnabled = false;
        RotationInfo(float angle) {
            this.angle = angle;
        }

        void enableXRotation() {
            xEnabled = true;
        }

        void enableYRotation() {
            xEnabled = true;
        }

        void enableZRotation() {
            xEnabled = true;
        }


//        void performRotation() {
//            GL11.glRotatef(-180, xEnabled, 0F, 1F);
//
//        }




    }



    public void renderBlockInWorld(Block block, int meta, float blockSize) {
        Tessellator tes = Tessellator.instance;

        this.bindTexture(TextureMap.locationBlocksTexture);

        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        tes.setColorOpaque_F(1f, 1f, 1f);

        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        tes.setColorOpaque_F(1f, 1f, 1f);

        //Add the rendering calls here (Can and should use helper functions that do the vertex calls)

        double x = 0;
        double y = 0;
        double z = 0;

        double[] X = {x - 0.5 , x - 0.5 , x + 0.5 , x + 0.5 ,
                x + 0.5 , x + 0.5 , x - 0.5 , x - 0.5 };
        double[] Y = {y + 0.5 , y - 0.5 , y - 0.5 , y + 0.5 ,
                y + 0.5 , y - 0.5 , y - 0.5 , y + 0.5 };
        double[] Z = {z + 0.5 , z + 0.5 , z + 0.5 , z + 0.5 ,
                z - 0.5 , z - 0.5 , z - 0.5 , z - 0.5 };

        tes.startDrawingQuads();

        GL11.glScalef(blockSize, blockSize, blockSize);

        IIcon texture;

        double minU;
        double maxU;
        double minV;
        double maxV;

        {
            texture = block.getIcon(4, meta);

            minU = texture.getMinU();
            maxU = texture.getMaxU();
            minV = texture.getMinV();
            maxV = texture.getMaxV();

            tes.addVertexWithUV(X[1], Y[1], Z[1], maxU, maxV);
            tes.addVertexWithUV(X[0], Y[0], Z[0], maxU, minV);
            tes.addVertexWithUV(X[7], Y[7], Z[7], minU, minV);
            tes.addVertexWithUV(X[6], Y[6], Z[6], minU, maxV);
        }

        {
            // Bottom face.
            texture = block.getIcon(0, meta);

            minU = texture.getMinU();
            maxU = texture.getMaxU();
            minV = texture.getMinV();
            maxV = texture.getMaxV();

            tes.addVertexWithUV(X[2], Y[2], Z[2], maxU, maxV);
            tes.addVertexWithUV(X[5], Y[5], Z[5], maxU, minV);
            tes.addVertexWithUV(X[6], Y[6], Z[6], minU, minV);
            tes.addVertexWithUV(X[1], Y[1], Z[1], minU, maxV);
        }

        {
            texture = block.getIcon(2, meta);

            minU = texture.getMinU();
            maxU = texture.getMaxU();
            minV = texture.getMinV();
            maxV = texture.getMaxV();

            tes.addVertexWithUV(X[6], Y[6], Z[6], maxU, maxV);
            tes.addVertexWithUV(X[7], Y[7], Z[7], maxU, minV);
            tes.addVertexWithUV(X[4], Y[4], Z[4], minU, minV);
            tes.addVertexWithUV(X[5], Y[5], Z[5], minU, maxV);
        }

        {
            texture = block.getIcon(5, meta);

            minU = texture.getMinU();
            maxU = texture.getMaxU();
            minV = texture.getMinV();
            maxV = texture.getMaxV();

            tes.addVertexWithUV(X[5], Y[5], Z[5], maxU, maxV);
            tes.addVertexWithUV(X[4], Y[4], Z[4], maxU, minV);
            tes.addVertexWithUV(X[3], Y[3], Z[3], minU, minV);
            tes.addVertexWithUV(X[2], Y[2], Z[2], minU, maxV);
        }

        {
            texture = block.getIcon(1, meta);

            minU = texture.getMinU();
            maxU = texture.getMaxU();
            minV = texture.getMinV();
            maxV = texture.getMaxV();

            tes.addVertexWithUV(X[3], Y[3], Z[3], maxU, maxV);
            tes.addVertexWithUV(X[4], Y[4], Z[4], maxU, minV);
            tes.addVertexWithUV(X[7], Y[7], Z[7], minU, minV);
            tes.addVertexWithUV(X[0], Y[0], Z[0], minU, maxV);
        }

        {
            texture = block.getIcon(3, meta);

            minU = texture.getMinU();
            maxU = texture.getMaxU();
            minV = texture.getMinV();
            maxV = texture.getMaxV();

            tes.addVertexWithUV(X[2], Y[2], Z[2], maxU, maxV);
            tes.addVertexWithUV(X[3], Y[3], Z[3], maxU, minV);
            tes.addVertexWithUV(X[0], Y[0], Z[0], minU, minV);
            tes.addVertexWithUV(X[1], Y[1], Z[1], minU, maxV);
        }

        tes.draw();

        // ----------------------------------------------
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_CULL_FACE);
        //spotless:on
    }

    Vector3d f(int time) {

        final int radius = 4;
        final double azimuthalAngle = PI * 0.4;

        final double x = radius * sin(azimuthalAngle) * cos(time);
        final double y = radius * sin(azimuthalAngle) * sin(time);
        final double z = radius * cos(azimuthalAngle);

        return new Vector3d(x, y, z);
    }
}

