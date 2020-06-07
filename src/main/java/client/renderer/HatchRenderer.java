package client.renderer;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;

public class HatchRenderer implements ISimpleBlockRenderingHandler {

    public static final int RID = RenderingRegistry.getNextAvailableRenderId();
    private static final HatchRenderer INSTANCE = new HatchRenderer();

    private HatchRenderer() {

    }

    public static HatchRenderer getInstance() {
        return INSTANCE;
    }

    @Override
    public void renderInventoryBlock(Block block, int meta, int modelId, RenderBlocks renderer) {
        block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        renderer.setRenderBoundsFromBlock(block);
        final Tessellator t = Tessellator.instance;
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        t.startDrawingQuads();
        t.setNormal(0.0F, -1.0F, 0.0F);
        renderer.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, block.getIcon(0, meta));
        t.draw();
        t.startDrawingQuads();
        t.setNormal(0.0F, 1.0F, 0.0F);
        renderer.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, block.getIcon(1, meta));
        t.draw();
        t.startDrawingQuads();
        t.setNormal(1.0F, 0.0F, 0.0F);
        renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, block.getIcon(2, meta));
        t.draw();
        t.startDrawingQuads();
        t.setNormal(-1.0F, 0.0F, 0.0F);
        renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, block.getIcon(3, meta));
        t.draw();
        t.startDrawingQuads();
        t.setNormal(0.0F, 0.0F, 1.0F);
        renderer.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, block.getIcon(5, meta));
        t.draw();
        t.startDrawingQuads();
        t.setNormal(0.0F, 0.0F, -1.0F);
        renderer.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, block.getIcon(6, meta));
        t.draw();
        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId,
                                    RenderBlocks renderer) {
        final Tessellator t = Tessellator.instance;
        // Set colour
        int mb = block.getMixedBrightnessForBlock(world, x, y, z);
        t.setBrightness(mb);

        block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        renderer.setRenderBoundsFromBlock(block);

        if (block.shouldSideBeRendered(world, x, y - 1, z, 6)) {
            renderer.renderFaceYNeg(block, x, y, z, block.getIcon(world, x, y, z, 0));
        }
        if (block.shouldSideBeRendered(world, x, y + 1, z, 6)) {
            t.setNormal(0.0F, 1.0F, 0.0F);
            renderer.renderFaceYPos(block, x, y, z, block.getIcon(world, x, y, z, 1));
        }

        if (block.shouldSideBeRendered(world, x, y, z - 1, 6)) {
            renderer.renderFaceZNeg(block, x, y, z, block.getIcon(world, x, y, z, 2));
        }
        if (block.shouldSideBeRendered(world, x, y, z + 1, 6)) {
            renderer.renderFaceZPos(block, x, y, z, block.getIcon(world, x, y, z, 3));
        }

        if (block.shouldSideBeRendered(world, x - 1, y, z, 6)) {
            renderer.renderFaceXNeg(block, x, y, z, block.getIcon(world, x, y, z, 4));
        }
        if (block.shouldSideBeRendered(world, x + 1, y, z, 6)) {
            renderer.renderFaceXPos(block, x, y, z, block.getIcon(world, x, y, z, 5));
        }

        return false;
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId) {
        return true;
    }

    @Override
    public int getRenderId() {
        return HatchRenderer.RID;
    }
}
