package tectech.thing.block;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

/**
 * Created by danie_000 on 19.12.2016.
 */
public final class RenderQuantumGlass implements ISimpleBlockRenderingHandler {

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        Tessellator tessellator = Tessellator.instance;
        GL11.glPushMatrix();
        // Get icons from custom register (useful for renderers and fluids)
        IIcon side = BlockQuantumGlass.stuff;
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, -1.0F, 0.0F);
        renderer.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, side);
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, -1.0F);
        renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, side);
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, 1.0F);
        renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, side);
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(-1.0F, 0.0F, 0.0F);
        renderer.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, side);
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(1.0F, 0.0F, 0.0F);
        renderer.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, side);
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 1.0F, 0.0F);
        renderer.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, side);
        tessellator.draw();
        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
        GL11.glPopMatrix();
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId,
        RenderBlocks renderer) {

        renderer.renderStandardBlock(block, x, y, z);
        Tessellator tes = Tessellator.instance;
        GL11.glPushMatrix();
        tes.setNormal(0F, 1F, 0F);
        tes.setBrightness(15728880);
        tes.setColorOpaque_F(0F, 1F, 1F);
        IIcon side = BlockQuantumGlass.stuff;
        GL11.glDisable(GL11.GL_CULL_FACE);

        // South
        if (world.getBlock(x, y, z + 1)
            .getClass() != BlockQuantumGlass.class) {
            tes.addVertexWithUV(x, y, z + 0.999, side.getMinU(), side.getMaxV()); // 0.999 instead of 1 for fighting
                                                                                  // (textures overlapping)
            tes.addVertexWithUV(x, y + 1, z + 0.999, side.getMinU(), side.getMinV());
            tes.addVertexWithUV(x + 1, y + 1, z + 0.999, side.getMaxU(), side.getMinV());
            tes.addVertexWithUV(x + 1, y, z + 0.999, side.getMaxU(), side.getMaxV());
        }
        // East
        if (world.getBlock(x + 1, y, z)
            .getClass() != BlockQuantumGlass.class) {
            tes.addVertexWithUV(x + 0.999, y, z + 1, side.getMinU(), side.getMaxV());
            tes.addVertexWithUV(x + 0.999, y + 1, z + 1, side.getMinU(), side.getMinV());
            tes.addVertexWithUV(x + 0.999, y + 1, z, side.getMaxU(), side.getMinV());
            tes.addVertexWithUV(x + 0.999, y, z, side.getMaxU(), side.getMaxV());
        }
        // North
        if (world.getBlock(x, y, z - 1)
            .getClass() != BlockQuantumGlass.class) {
            tes.addVertexWithUV(x + 1, y, z + 0.001, side.getMinU(), side.getMaxV());
            tes.addVertexWithUV(x + 1, y + 1, z + 0.001, side.getMinU(), side.getMinV());
            tes.addVertexWithUV(x, y + 1, z + 0.001, side.getMaxU(), side.getMinV());
            tes.addVertexWithUV(x, y, z + 0.001, side.getMaxU(), side.getMaxV());
        }
        // West
        if (world.getBlock(x - 1, y, z)
            .getClass() != BlockQuantumGlass.class) {
            tes.addVertexWithUV(x + 0.001, y, z, side.getMinU(), side.getMaxV());
            tes.addVertexWithUV(x + 0.001, y + 1, z, side.getMinU(), side.getMinV());
            tes.addVertexWithUV(x + 0.001, y + 1, z + 1, side.getMaxU(), side.getMinV());
            tes.addVertexWithUV(x + 0.001, y, z + 1, side.getMaxU(), side.getMaxV());
        }
        // Top
        if (world.getBlock(x, y + 1, z)
            .getClass() != BlockQuantumGlass.class) {
            tes.addVertexWithUV(x, y + 0.999, z + 1, side.getMinU(), side.getMaxV());
            tes.addVertexWithUV(x, y + 0.999, z, side.getMinU(), side.getMinV());
            tes.addVertexWithUV(x + 1, y + 0.999, z, side.getMaxU(), side.getMinV());
            tes.addVertexWithUV(x + 1, y + 0.999, z + 1, side.getMaxU(), side.getMaxV());
        }
        // Bottom
        if (world.getBlock(x, y - 1, z)
            .getClass() != BlockQuantumGlass.class) {
            tes.addVertexWithUV(x, y + 0.001, z, side.getMinU(), side.getMaxV());
            tes.addVertexWithUV(x, y + 0.001, z + 1, side.getMinU(), side.getMinV());
            tes.addVertexWithUV(x + 1, y + 0.001, z + 1, side.getMaxU(), side.getMinV());
            tes.addVertexWithUV(x + 1, y + 0.001, z, side.getMaxU(), side.getMaxV());
        }
        GL11.glPopMatrix();
        GL11.glEnable(GL11.GL_CULL_FACE);
        return true;
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId) {
        return true;
    }

    @Override
    public int getRenderId() {
        return BlockQuantumGlass.renderID;
    }
}
