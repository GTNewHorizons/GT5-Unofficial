package gregtech.api.ModernMaterials.Blocks.FrameBox;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import gregtech.api.ModernMaterials.ModernMaterial;
import gregtech.api.interfaces.ITexture;
import gregtech.api.util.LightingHelper;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;

import java.awt.*;

import static gregtech.api.ModernMaterials.ModernMaterialUtilities.materialIDToMaterial;

public class FrameBoxSimpleBlockRenderer implements ISimpleBlockRenderingHandler {

    public static final int renderID = RenderingRegistry.getNextAvailableRenderId();

    public FrameBoxSimpleBlockRenderer() {
        RenderingRegistry.registerBlockHandler(renderID, this);
    }

    @Override
    public void renderInventoryBlock(Block block, int ID, int modelId, RenderBlocks renderer) {
/*        Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.locationBlocksTexture);

        ModernMaterial material = materialIDToMaterial.getOrDefault(ID, materialIDToMaterial.get(2));
        Color color = material.getColor();
        int red = color.getRed();
        int green = color.getGreen();
        int blue = color.getBlue();

        GL11.glPushMatrix();
        //GL11.glColor4f(red, green, blue, 255);
        renderer.useInventoryTint = false;
        renderBlockAsItem(block, 0, renderer);
        GL11.glPopMatrix();*/
        GL11.glPushMatrix();
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        GL11.glColor4f(255, 1, 2, 255);
        renderPositiveYFacing(null, renderer, block, 0, 0, 0, new ITexture[] { (ITexture) renderer.getBlockIconFromSideAndMetadata(block, 1, ID) }, true);
        GL11.glPopMatrix();
    }

    public static void renderPositiveYFacing(IBlockAccess aWorld, RenderBlocks aRenderer, Block aBlock, int aX, int aY,
                                             int aZ, ITexture[] aIcon, boolean aFullBlock) {
        if (aWorld != null) {
            if ((aFullBlock) && (!aBlock.shouldSideBeRendered(aWorld, aX, aY + 1, aZ, 1))) return;
            Tessellator.instance
                .setBrightness(aBlock.getMixedBrightnessForBlock(aWorld, aX, aFullBlock ? aY + 1 : aY, aZ));
        }
        if (aIcon == null) return;
        for (final ITexture iTexture : aIcon) {
            if (iTexture != null) {
                iTexture.renderYPos(aRenderer, aBlock, aX, aY, aZ);
            }
        }
    }


    public static void drawBlock(Block block, int meta, RenderBlocks renderer) {
        Tessellator tessellator = Tessellator.instance;

        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, -1.0F, 0.0F);
        renderer.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, block.getIcon(0, meta));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 1.0F, 0.0F);
        renderer.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, block.getIcon(1, meta));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, -1.0F);
        renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, block.getIcon(2, meta));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, 1.0F);
        renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, block.getIcon(3, meta));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(-1.0F, 0.0F, 0.0F);
        renderer.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, block.getIcon(4, meta));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(1.0F, 0.0F, 0.0F);
        renderer.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, block.getIcon(5, meta));
        tessellator.draw();
    }

    public void renderBlockAsItem(Block p_147800_1_, int metadata, RenderBlocks renderer)
    {
        Tessellator tessellator = Tessellator.instance;
        renderer.setRenderBoundsFromBlock(p_147800_1_);

        boolean cond = renderer.enableAO;
        renderer.enableAO = false;
        renderer.colorRedTopRight = 255;

        {
            p_147800_1_.setBlockBoundsForItemRender();
            renderer.setRenderBoundsFromBlock(p_147800_1_);
            GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
            GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
            GL11.glColor4f(255, 1, 1, 255);

            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, -1.0F, 0.0F);
            renderer.renderFaceYNeg(p_147800_1_, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(p_147800_1_, 0, metadata));
            tessellator.draw();
            GL11.glColor4f(255, 1, 1, 255);

            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, 1.0F, 0.0F);
            renderFaceYPos(p_147800_1_, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(p_147800_1_, 1, metadata), renderer);
            tessellator.draw();
            GL11.glColor4f(255, 1, 1, 255);

            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, 0.0F, -1.0F);
            renderer.renderFaceZNeg(p_147800_1_, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(p_147800_1_, 2, metadata));
            tessellator.draw();
            GL11.glColor4f(255, 1, 1, 255);

            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, 0.0F, 1.0F);
            renderer.renderFaceZPos(p_147800_1_, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(p_147800_1_, 3, metadata));
            tessellator.draw();
            GL11.glColor4f(255, 1, 1, 255);

            tessellator.startDrawingQuads();
            tessellator.setNormal(-1.0F, 0.0F, 0.0F);
            renderer.renderFaceXNeg(p_147800_1_, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(p_147800_1_, 4, metadata));
            tessellator.draw();
            GL11.glColor4f(255, 1, 1, 255);

            tessellator.startDrawingQuads();
            tessellator.setNormal(1.0F, 0.0F, 0.0F);
            renderer.renderFaceXPos(p_147800_1_, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(p_147800_1_, 5, metadata));
            tessellator.draw();

            GL11.glTranslatef(0.5F, 0.5F, 0.5F);
        }

        renderer.enableAO = cond;
    }

    public void renderFaceYPos(Block p_147806_1_, double p_147806_2_, double p_147806_4_, double p_147806_6_, IIcon p_147806_8_, RenderBlocks renderer)
    {
        Tessellator tessellator = Tessellator.instance;

        if (renderer.hasOverrideBlockTexture())
        {
            p_147806_8_ = renderer.overrideBlockTexture;
        }

        double d3 = (double)p_147806_8_.getInterpolatedU(renderer.renderMinX * 16.0D);
        double d4 = (double)p_147806_8_.getInterpolatedU(renderer.renderMaxX * 16.0D);
        double d5 = (double)p_147806_8_.getInterpolatedV(renderer.renderMinZ * 16.0D);
        double d6 = (double)p_147806_8_.getInterpolatedV(renderer.renderMaxZ * 16.0D);

        if (renderer.renderMinX < 0.0D || renderer.renderMaxX > 1.0D)
        {
            d3 = (double)p_147806_8_.getMinU();
            d4 = (double)p_147806_8_.getMaxU();
        }

        if (renderer.renderMinZ < 0.0D || renderer.renderMaxZ > 1.0D)
        {
            d5 = (double)p_147806_8_.getMinV();
            d6 = (double)p_147806_8_.getMaxV();
        }

        double d7 = d4;
        double d8 = d3;
        double d9 = d5;
        double d10 = d6;

        if (renderer.uvRotateTop == 1)
        {
            d3 = (double)p_147806_8_.getInterpolatedU(renderer.renderMinZ * 16.0D);
            d5 = (double)p_147806_8_.getInterpolatedV(16.0D - renderer.renderMaxX * 16.0D);
            d4 = (double)p_147806_8_.getInterpolatedU(renderer.renderMaxZ * 16.0D);
            d6 = (double)p_147806_8_.getInterpolatedV(16.0D - renderer.renderMinX * 16.0D);
            d9 = d5;
            d10 = d6;
            d7 = d3;
            d8 = d4;
            d5 = d6;
            d6 = d9;
        }
        else if (renderer.uvRotateTop == 2)
        {
            d3 = (double)p_147806_8_.getInterpolatedU(16.0D - renderer.renderMaxZ * 16.0D);
            d5 = (double)p_147806_8_.getInterpolatedV(renderer.renderMinX * 16.0D);
            d4 = (double)p_147806_8_.getInterpolatedU(16.0D - renderer.renderMinZ * 16.0D);
            d6 = (double)p_147806_8_.getInterpolatedV(renderer.renderMaxX * 16.0D);
            d7 = d4;
            d8 = d3;
            d3 = d4;
            d4 = d8;
            d9 = d6;
            d10 = d5;
        }
        else if (renderer.uvRotateTop == 3)
        {
            d3 = (double)p_147806_8_.getInterpolatedU(16.0D - renderer.renderMinX * 16.0D);
            d4 = (double)p_147806_8_.getInterpolatedU(16.0D - renderer.renderMaxX * 16.0D);
            d5 = (double)p_147806_8_.getInterpolatedV(16.0D - renderer.renderMinZ * 16.0D);
            d6 = (double)p_147806_8_.getInterpolatedV(16.0D - renderer.renderMaxZ * 16.0D);
            d7 = d4;
            d8 = d3;
            d9 = d5;
            d10 = d6;
        }

        double d11 = p_147806_2_ + renderer.renderMinX;
        double d12 = p_147806_2_ + renderer.renderMaxX;
        double d13 = p_147806_4_ + renderer.renderMaxY;
        double d14 = p_147806_6_ + renderer.renderMinZ;
        double d15 = p_147806_6_ + renderer.renderMaxZ;

        if (renderer.renderFromInside)
        {
            d11 = p_147806_2_ + renderer.renderMaxX;
            d12 = p_147806_2_ + renderer.renderMinX;
        }


        tessellator.addVertexWithUV(d12, d13, d15, d4, d6);
        tessellator.addVertexWithUV(d12, d13, d14, d7, d9);
        tessellator.addVertexWithUV(d11, d13, d14, d3, d5);
        tessellator.addVertexWithUV(d11, d13, d15, d8, d10);

    }


    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.locationBlocksTexture);

        FrameBoxTileEntity frameBoxTile = (FrameBoxTileEntity) world.getTileEntity(x, y, z);
        int ID = frameBoxTile.getMaterialID();

        ModernMaterial material = materialIDToMaterial.get(ID);

        Color color = material.getColor();
        int red = color.getRed();
        int green = color.getGreen();
        int blue = color.getBlue();

        GL11.glPushMatrix();
        GL11.glColor4f(red, blue, green, 255);
        renderer.renderStandardBlock(block, x, y, z);
        GL11.glPopMatrix();

        return true;
    }

    @Override
    public boolean shouldRender3DInInventory(int modelID) {
        return true;
    }

    @Override
    public int getRenderId() {
        return renderID;
    }

}
