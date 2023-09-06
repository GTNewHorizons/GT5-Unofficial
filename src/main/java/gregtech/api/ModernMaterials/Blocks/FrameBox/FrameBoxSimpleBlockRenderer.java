package gregtech.api.ModernMaterials.Blocks.FrameBox;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import gregtech.api.ModernMaterials.ModernMaterial;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;

import java.awt.*;

import static gregtech.api.ModernMaterials.ModernMaterialUtilities.materialIDToMaterial;

public class FrameBoxSimpleBlockRenderer implements ISimpleBlockRenderingHandler {

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

    public static int renderID = 0;

    public FrameBoxSimpleBlockRenderer() {
        renderID = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(renderID, this);
    }

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
//        Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
//
//        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
//
//        // Get colour and apply it.
//        ModernMaterial material = materialIDToMaterial.get(metadata);
//        Color color = material.getColor();
//        int red = color.getRed();
//        int green = color.getGreen();
//        int blue = color.getBlue();
//        GL11.glColor4f(red, blue, green, 255);
//        renderer.renderBlockAsItem(block, metadata, 1.0f);
//
//        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
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
