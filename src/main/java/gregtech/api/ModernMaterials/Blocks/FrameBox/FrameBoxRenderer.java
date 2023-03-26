package gregtech.api.ModernMaterials.Blocks.FrameBox;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class FrameBoxRenderer implements ISimpleBlockRenderingHandler {

    public static int renderID = 0;
    private final Color color = new Color(38, 49, 69, 255);

    public FrameBoxRenderer() {
        renderID = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(renderID, this);
    }

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks aRenderer) {
//        aRenderer.enableAO = false;
//        aRenderer.useInventoryTint = true;

        // Render the block in the inventory
//        RenderHelper.enableGUIStandardItemLighting();
//        Tessellator tessellator = Tessellator.instance;
//        tessellator.setColorOpaque_I(color.getRGB());
//        tessellator.setColorRGBA(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
//        GL11.glPushMatrix();
//        GL11.glColor4f(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
//        aRenderer.renderBlockAsItem(Blocks.stone, metadata, 1.0f);
//        GL11.glColor4f(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
//        GL11.glPopMatrix();

//        aRenderer.useInventoryTint = false;
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
//        Tessellator tessellator = Tessellator.instance;
//        tessellator.setColorOpaque_I(color.getRGB());
//        tessellator.setColorRGBA(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
//        GL11.glPushMatrix();
//        GL11.glColor3f(color.getRed(), color.getGreen(), color.getBlue());
//        renderer.renderStandardBlockWithAmbientOcclusion(Blocks.stone, x, y, z, color.getRed(), color.getGreen(), color.getBlue());
//        GL11.glPopMatrix();
        // Set the vertex format to include color information
        Tessellator tessellator = Tessellator.instance;
        tessellator.setBrightness(Blocks.lit_furnace.getMixedBrightnessForBlock(world, x, y, z));
//        tessellator.setColorRGBA(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()); // Set the desired RGBA color

        // Start drawing the custom mesh
//        tessellator.startDrawingQuads();

        // Add the block's vertices with the desired color
        renderer.renderStandardBlockWithColorMultiplier(Blocks.stone, x, y, z, color.getRed(), color.getGreen(), color.getBlue());

        // Finish drawing the custom mesh
//        tessellator.draw();
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
