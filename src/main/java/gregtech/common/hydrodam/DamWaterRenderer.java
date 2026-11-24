package gregtech.common.hydrodam;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizons.angelica.api.ThreadSafeISBRH;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;

/// Renders dam water like vanilla still water, except that the surface is flat and sits at the volume's water level.
/// Faces are drawn on both sides so the surface is visible from underwater.
@ThreadSafeISBRH(perThread = false)
public class DamWaterRenderer implements ISimpleBlockRenderingHandler {

    private final int renderId = RenderingRegistry.getNextAvailableRenderId();

    /// Registers the renderer and points {@link BlockDamWater} at it. Assumption: called once, on the client.
    public static void register() {
        DamWaterRenderer renderer = new DamWaterRenderer();

        RenderingRegistry.registerBlockHandler(renderer);
        BlockDamWater.renderId = renderer.renderId;
    }

    /// Faces are inset by this much to avoid z-fighting with neighbouring blocks.
    private static final double INSET = 0.001;

    /// Cells filled less than this are not drawn.
    private static final float MIN_VISIBLE_FILL = 1 / 64f;

    private static final float TOP_SHADE = 1.0f;
    private static final float BOTTOM_SHADE = 0.5f;
    private static final float Z_SIDE_SHADE = 0.8f;
    private static final float X_SIDE_SHADE = 0.6f;

    private static final ForgeDirection[] SIDES = { ForgeDirection.NORTH, ForgeDirection.SOUTH, ForgeDirection.WEST,
        ForgeDirection.EAST };

    @Override
    public void renderInventoryBlock(Block block, int meta, int modelId, RenderBlocks renderer) {}

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId,
        RenderBlocks renderer) {
        float fill = getFill(world, x, y, z);

        if (fill < MIN_VISIBLE_FILL) return false;

        int colour = block.colorMultiplier(world, x, y, z);
        float red = (colour >> 16 & 255) / 255f;
        float green = (colour >> 8 & 255) / 255f;
        float blue = (colour & 255) / 255f;

        // A surface (no visible water above) is drawn lower than the water it holds, like vanilla water.
        boolean covered = !block.shouldSideBeRendered(world, x, y + 1, z, 1)
            && !isInvisibleDamWater(world, x, y + 1, z, block);
        float height = covered ? fill : DamWaterHeights.getSurfaceHeight(fill);

        boolean rendered = false;

        if (renderer.renderAllFaces || fill < 1 || !covered) {
            renderTop(world, x, y, z, block, height, red, green, blue);
            rendered = true;
        }

        if (renderer.renderAllFaces || block.shouldSideBeRendered(world, x, y - 1, z, 0)) {
            renderBottom(world, x, y, z, block);
            rendered = true;
        }

        for (ForgeDirection dir : SIDES) {
            int nx = x + dir.offsetX;
            int nz = z + dir.offsetZ;

            if (renderer.renderAllFaces || block.shouldSideBeRendered(world, nx, y, nz, dir.ordinal())) {
                renderSide(world, x, y, z, block, dir, height, red, green, blue);
                rendered = true;
            }
        }

        return rendered;
    }

    /// Whether the block is dam water that is too empty to be drawn. Faces against it must be drawn, since it doesn't
    /// hide them.
    private static boolean isInvisibleDamWater(IBlockAccess world, int x, int y, int z, Block damWater) {
        return world.getBlock(x, y, z) == damWater && getFill(world, x, y, z) < MIN_VISIBLE_FILL;
    }

    private static float getFill(IBlockAccess world, int x, int y, int z) {
        int dimension = Minecraft.getMinecraft().theWorld.provider.dimensionId;
        int volumeId = DamWaterMeta.getVolumeId(world.getBlockMetadata(x, y, z));

        return ClientDamLevels.getFill(dimension, volumeId, y);
    }

    private static void renderTop(IBlockAccess world, int x, int y, int z, Block block, float height, float red,
        float green, float blue) {
        Tessellator tessellator = Tessellator.instance;
        IIcon icon = block.getIcon(1, 0);

        double top = y + height - INSET;
        double minU = icon.getMinU();
        double maxU = icon.getMaxU();
        double minV = icon.getMinV();
        double maxV = icon.getMaxV();

        tessellator.setBrightness(block.getMixedBrightnessForBlock(world, x, y, z));
        tessellator.setColorOpaque_F(TOP_SHADE * red, TOP_SHADE * green, TOP_SHADE * blue);

        tessellator.addVertexWithUV(x, top, z, minU, minV);
        tessellator.addVertexWithUV(x, top, z + 1, minU, maxV);
        tessellator.addVertexWithUV(x + 1, top, z + 1, maxU, maxV);
        tessellator.addVertexWithUV(x + 1, top, z, maxU, minV);

        tessellator.addVertexWithUV(x, top, z, minU, minV);
        tessellator.addVertexWithUV(x + 1, top, z, maxU, minV);
        tessellator.addVertexWithUV(x + 1, top, z + 1, maxU, maxV);
        tessellator.addVertexWithUV(x, top, z + 1, minU, maxV);
    }

    private static void renderBottom(IBlockAccess world, int x, int y, int z, Block block) {
        Tessellator tessellator = Tessellator.instance;
        IIcon icon = block.getIcon(0, 0);

        double bottom = y + INSET;
        double minU = icon.getMinU();
        double maxU = icon.getMaxU();
        double minV = icon.getMinV();
        double maxV = icon.getMaxV();

        tessellator.setBrightness(block.getMixedBrightnessForBlock(world, x, y - 1, z));
        tessellator.setColorOpaque_F(BOTTOM_SHADE, BOTTOM_SHADE, BOTTOM_SHADE);

        tessellator.addVertexWithUV(x, bottom, z + 1, minU, maxV);
        tessellator.addVertexWithUV(x, bottom, z, minU, minV);
        tessellator.addVertexWithUV(x + 1, bottom, z, maxU, minV);
        tessellator.addVertexWithUV(x + 1, bottom, z + 1, maxU, maxV);
    }

    /// Draws a side face from the bottom of the cell up to the water level, using the top half of the flowing texture
    /// like vanilla water.
    private static void renderSide(IBlockAccess world, int x, int y, int z, Block block, ForgeDirection dir,
        float height, float red, float green, float blue) {
        Tessellator tessellator = Tessellator.instance;
        IIcon icon = block.getIcon(dir.ordinal(), 0);

        double x1, z1, x2, z2;

        switch (dir) {
            case NORTH -> {
                x1 = x;
                x2 = x + 1;
                z1 = z2 = z + INSET;
            }
            case SOUTH -> {
                x1 = x + 1;
                x2 = x;
                z1 = z2 = z + 1 - INSET;
            }
            case WEST -> {
                x1 = x2 = x + INSET;
                z1 = z + 1;
                z2 = z;
            }
            default -> {
                x1 = x2 = x + 1 - INSET;
                z1 = z;
                z2 = z + 1;
            }
        }

        double top = y + height;
        double minU = icon.getInterpolatedU(0);
        double maxU = icon.getInterpolatedU(8);
        double topV = icon.getInterpolatedV((1 - height) * 8);
        double bottomV = icon.getInterpolatedV(8);

        float shade = dir == ForgeDirection.NORTH || dir == ForgeDirection.SOUTH ? Z_SIDE_SHADE : X_SIDE_SHADE;

        tessellator.setBrightness(block.getMixedBrightnessForBlock(world, x + dir.offsetX, y, z + dir.offsetZ));
        tessellator.setColorOpaque_F(shade * red, shade * green, shade * blue);

        tessellator.addVertexWithUV(x1, top, z1, minU, topV);
        tessellator.addVertexWithUV(x2, top, z2, maxU, topV);
        tessellator.addVertexWithUV(x2, y, z2, maxU, bottomV);
        tessellator.addVertexWithUV(x1, y, z1, minU, bottomV);

        tessellator.addVertexWithUV(x1, y, z1, minU, bottomV);
        tessellator.addVertexWithUV(x2, y, z2, maxU, bottomV);
        tessellator.addVertexWithUV(x2, top, z2, maxU, topV);
        tessellator.addVertexWithUV(x1, top, z1, minU, topV);
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId) {
        return false;
    }

    @Override
    public int getRenderId() {
        return renderId;
    }
}
