package tectech.thing.block;

import static gregtech.api.enums.Mods.Angelica;

import java.util.function.Supplier;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import com.prupe.mcpatcher.ctm.CTMUtils;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class RenderDoubleSidedGlass implements ISimpleBlockRenderingHandler {

    private static final double OFFSET_HI = 0.999;
    private static final double OFFSET_LO = 0.001;

    private final Supplier<IIcon> iconProvider;
    private final int renderID;

    public RenderDoubleSidedGlass(Supplier<IIcon> iconProvider, int renderID) {
        this.iconProvider = iconProvider;
        this.renderID = renderID;
    }

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        Tessellator tessellator = Tessellator.instance;
        GL11.glPushMatrix();
        // Get icons from custom register (useful for renderers and fluids)
        IIcon side = iconProvider.get();
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

    private IIcon getWorldIcon(ForgeDirection direction, IBlockAccess world, int x, int y, int z, Block block) {
        final IIcon icon = iconProvider.get();
        if (!Angelica.isModLoaded()) return icon;
        return CTMUtils.getBlockIcon(icon, block, world, x, y, z, direction.ordinal());
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId,
        RenderBlocks renderer) {

        renderer.renderStandardBlock(block, x, y, z);
        Tessellator tes = Tessellator.instance;
        boolean wasAOEnabled = renderer.enableAO;
        renderer.enableAO = false;
        tes.setNormal(0F, 1F, 0F);
        tes.setBrightness(15728880);
        tes.setColorOpaque_F(0F, 1F, 1F);
        // South
        if (shouldRenderSide(block, world, x, y, z, ForgeDirection.SOUTH)) {
            IIcon side = getWorldIcon(ForgeDirection.SOUTH, world, x, y, z, block);
            renderer.renderMinZ = OFFSET_HI;
            renderer.renderFaceZNeg(block, x, y, z, side);
            renderer.renderMinZ = 0;
        }
        // West
        if (shouldRenderSide(block, world, x, y, z, ForgeDirection.EAST)) {
            IIcon side = getWorldIcon(ForgeDirection.EAST, world, x, y, z, block);
            renderer.renderMinX = OFFSET_HI;
            renderer.renderFaceXNeg(block, x, y, z, side);
            renderer.renderMinX = 0;
        }
        // North
        if (shouldRenderSide(block, world, x, y, z, ForgeDirection.NORTH)) {
            IIcon side = getWorldIcon(ForgeDirection.NORTH, world, x, y, z, block);
            renderer.renderMaxZ = OFFSET_LO;
            renderer.renderFaceZPos(block, x, y, z, side);
            renderer.renderMaxZ = 1;
        }
        // West
        if (shouldRenderSide(block, world, x, y, z, ForgeDirection.WEST)) {
            IIcon side = getWorldIcon(ForgeDirection.WEST, world, x, y, z, block);
            renderer.renderMaxX = OFFSET_LO;
            renderer.renderFaceXPos(block, x, y, z, side);
            renderer.renderMaxX = 1;
        }
        // Top
        if (shouldRenderSide(block, world, x, y, z, ForgeDirection.UP)) {
            IIcon side = getWorldIcon(ForgeDirection.UP, world, x, y, z, block);
            renderer.renderMinY = OFFSET_HI;
            renderer.renderFaceYNeg(block, x, y, z, side);
            renderer.renderMinY = 0;
        }
        // Bottom
        if (shouldRenderSide(block, world, x, y, z, ForgeDirection.DOWN)) {
            IIcon side = getWorldIcon(ForgeDirection.DOWN, world, x, y, z, block);
            renderer.renderMaxY = OFFSET_LO;
            renderer.renderFaceYPos(block, x, y, z, side);
            renderer.renderMaxY = 1;
        }
        renderer.enableAO = wasAOEnabled;
        return true;
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId) {
        return true;
    }

    @Override
    public int getRenderId() {
        return renderID;
    }

    private boolean shouldRenderSide(Block block, IBlockAccess world, int x, int y, int z, ForgeDirection direction) {
        return block.shouldSideBeRendered(
            world,
            x + direction.offsetX,
            y + direction.offsetY,
            z + direction.offsetZ,
            direction.ordinal());
    }
}
