package gregtech.api.ModernMaterials.Blocks.FrameBox;

import cofh.lib.render.RenderHelper;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import gregtech.api.ModernMaterials.ModernMaterial;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.init.Blocks;
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
        Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.locationBlocksTexture);

        ModernMaterial material = materialIDToMaterial.getOrDefault(ID, materialIDToMaterial.get(2));
        Color color = material.getColor();
        int red = color.getRed();
        int green = color.getGreen();
        int blue = color.getBlue();

        GL11.glPushMatrix();
        GL11.glColor4f(red, blue, green, 255);

        //renderer.renderBlockAsItem(block, 0, 1.0f);
        GL11.glPopMatrix();
    }

    private void renderBlockAsItem(Block p_147800_1_, int p_147800_2_, RenderBlocks renderBlocks) {

        Tessellator tessellator = Tessellator.instance;

        p_147800_1_.setBlockBoundsForItemRender();
        renderBlocks.setRenderBoundsFromBlock(p_147800_1_);
        GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, -1.0F, 0.0F);
        renderBlocks.renderFaceYNeg(p_147800_1_, 0.0D, 0.0D, 0.0D, renderBlocks.getBlockIconFromSideAndMetadata(p_147800_1_, 0, p_147800_2_));
        tessellator.draw();

        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 1.0F, 0.0F);
        renderBlocks.renderFaceYPos(p_147800_1_, 0.0D, 0.0D, 0.0D, renderBlocks.getBlockIconFromSideAndMetadata(p_147800_1_, 1, p_147800_2_));
        tessellator.draw();

        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, -1.0F);
        renderBlocks.renderFaceZNeg(p_147800_1_, 0.0D, 0.0D, 0.0D, renderBlocks.getBlockIconFromSideAndMetadata(p_147800_1_, 2, p_147800_2_));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, 1.0F);
        renderBlocks.renderFaceZPos(p_147800_1_, 0.0D, 0.0D, 0.0D, renderBlocks.getBlockIconFromSideAndMetadata(p_147800_1_, 3, p_147800_2_));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(-1.0F, 0.0F, 0.0F);
        renderBlocks.renderFaceXNeg(p_147800_1_, 0.0D, 0.0D, 0.0D, renderBlocks.getBlockIconFromSideAndMetadata(p_147800_1_, 4, p_147800_2_));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(1.0F, 0.0F, 0.0F);
        renderBlocks.renderFaceXPos(p_147800_1_, 0.0D, 0.0D, 0.0D, renderBlocks.getBlockIconFromSideAndMetadata(p_147800_1_, 5, p_147800_2_));
        tessellator.draw();
        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
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
