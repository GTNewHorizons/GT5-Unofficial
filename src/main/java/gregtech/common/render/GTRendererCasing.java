package gregtech.common.render;

import static gregtech.api.enums.GTValues.*;
import static gregtech.common.render.GTRendererBlock.*;
import static net.minecraftforge.common.util.ForgeDirection.VALID_DIRECTIONS;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import com.gtnewhorizons.angelica.api.ThreadSafeISBRH;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import gregtech.api.interfaces.ITexture;
import gregtech.api.render.ISBRInventoryContext;
import gregtech.api.render.ISBRWorldContext;
import gregtech.api.render.RenderOverlay;
import gregtech.api.render.SBRContextHolder;
import gregtech.api.render.TextureFactory;
import gregtech.mixin.interfaces.accessors.TesselatorAccessor;

/**
 * This renderer is almost the same as vanilla simple block renderer, with the exception that it can render overlays
 * registered to {@link RenderOverlay}
 */
@ThreadSafeISBRH(perThread = true)
public class GTRendererCasing implements ISimpleBlockRenderingHandler {

    public static final int mRenderID = RenderingRegistry.getNextAvailableRenderId();
    private final ITexture[][] textureArray = new ITexture[6][2];
    private final SBRContextHolder sbrContextHolder = new SBRContextHolder();

    @Override
    public void renderInventoryBlock(Block aBlock, int aMeta, int aModelID, RenderBlocks aRenderer) {
        aRenderer.enableAO = false;
        aRenderer.useInventoryTint = true;
        final ISBRInventoryContext ctx = sbrContextHolder.getSBRInventoryContext(aBlock, aMeta, aModelID, aRenderer);

        setupBlockTexturesOnly(aBlock, aMeta, true);

        GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);

        aBlock.setBlockBoundsForItemRender();
        aRenderer.setRenderBoundsFromBlock(aBlock);
        // spotless:off
        ctx.renderNegativeYFacing(textureArray[SIDE_DOWN]);
        ctx.renderPositiveYFacing(textureArray[SIDE_UP]);
        ctx.renderNegativeZFacing(textureArray[SIDE_NORTH]);
        ctx.renderPositiveZFacing(textureArray[SIDE_SOUTH]);
        ctx.renderNegativeXFacing(textureArray[SIDE_WEST]);
        ctx.renderPositiveXFacing(textureArray[SIDE_EAST]);
        // spotless:on
        aBlock.setBlockBounds(BLOCK_MIN, BLOCK_MIN, BLOCK_MIN, BLOCK_MAX, BLOCK_MAX, BLOCK_MAX);

        aRenderer.setRenderBoundsFromBlock(aBlock);

        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
        aRenderer.useInventoryTint = false;
    }

    private void setupBlockTexturesOnly(Block aBlock, int aMeta, boolean noCoord) {
        ForgeDirection[] validDirections = VALID_DIRECTIONS;
        for (int i = 0, validDirectionsLength = validDirections.length; i < validDirectionsLength; i++) {
            ForgeDirection tFace = validDirections[i];
            if (noCoord) {
                textureArray[i][0] = TextureFactory.blockBuilder()
                    .setFromBlock(aBlock, aMeta)
                    .setFromSide(tFace)
                    .noWorldCoord()
                    .build();
            } else {
                textureArray[i][0] = TextureFactory.of(aBlock, aMeta, tFace);
            }
            textureArray[i][1] = null;
        }
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess aWorld, int aX, int aY, int aZ, Block aBlock, int aModelID,
        RenderBlocks aRenderer) {
        aRenderer.enableAO = Minecraft.isAmbientOcclusionEnabled();
        aRenderer.useInventoryTint = false;

        final TesselatorAccessor tessAccess = (TesselatorAccessor) Tessellator.instance;
        final ISBRWorldContext ctx = sbrContextHolder.getSBRWorldContext(aX, aY, aZ, aBlock, aModelID, aRenderer);

        int tMeta = aWorld.getBlockMetadata(aX, aY, aZ);

        ITexture[] overlaid = RenderOverlay.get(aWorld, aX, aY, aZ);
        if (overlaid == null) {
            setupBlockTexturesOnly(aBlock, tMeta, false);
        } else {
            ForgeDirection[] validDirections = VALID_DIRECTIONS;
            for (int i = 0, validDirectionsLength = validDirections.length; i < validDirectionsLength; i++) {
                ForgeDirection tFace = validDirections[i];
                textureArray[i][0] = TextureFactory.of(aBlock, tMeta, tFace);
                textureArray[i][1] = overlaid[i];
            }
        }

        aBlock.setBlockBounds(BLOCK_MIN, BLOCK_MIN, BLOCK_MIN, BLOCK_MAX, BLOCK_MAX, BLOCK_MAX);
        aRenderer.setRenderBoundsFromBlock(aBlock);

        // spotless:off
        ctx.renderNegativeYFacing(textureArray[SIDE_DOWN]);
        ctx.renderPositiveYFacing(textureArray[SIDE_UP]);
        ctx.renderNegativeZFacing(textureArray[SIDE_NORTH]);
        ctx.renderPositiveZFacing(textureArray[SIDE_SOUTH]);
        ctx.renderNegativeXFacing(textureArray[SIDE_WEST]);
        ctx.renderPositiveXFacing(textureArray[SIDE_EAST]);
        // spotless:on

        return tessAccess.gt5u$hasVertices();
    }

    @Override
    public boolean shouldRender3DInInventory(int aModel) {
        return true;
    }

    @Override
    public int getRenderId() {
        return mRenderID;
    }
}
