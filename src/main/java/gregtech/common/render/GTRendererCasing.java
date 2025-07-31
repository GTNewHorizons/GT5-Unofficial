package gregtech.common.render;

import static gregtech.api.enums.GTValues.*;
import static gregtech.common.render.GTRendererBlock.*;
import static net.minecraftforge.common.util.ForgeDirection.VALID_DIRECTIONS;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import com.gtnewhorizons.angelica.api.ThreadSafeISBRH;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import gregtech.api.interfaces.ITexture;
import gregtech.api.render.RenderOverlay;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.LightingHelper;
import gregtech.mixin.interfaces.accessors.TesselatorAccessor;

/**
 * This renderer is almost the same as vanilla simple block renderer, with the exception that it can render overlays
 * registered to {@link RenderOverlay}
 */
@ThreadSafeISBRH(perThread = true)
public class GTRendererCasing implements ISimpleBlockRenderingHandler {

    private static final GTRendererCasing INSTANCE = new GTRendererCasing();
    public static int mRenderID;

    public static void register() {
        mRenderID = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(INSTANCE);
    }

    private final ITexture[][] textureArray = new ITexture[6][2];

    @Override
    public void renderInventoryBlock(Block aBlock, int aMeta, int aModelID, RenderBlocks aRenderer) {
        aRenderer.enableAO = false;
        aRenderer.useInventoryTint = true;
        final LightingHelper lightingHelper = new LightingHelper(aRenderer);

        setupBlockTexturesOnly(aBlock, aMeta, true);

        GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);

        aBlock.setBlockBoundsForItemRender();
        aRenderer.setRenderBoundsFromBlock(aBlock);
        // spotless:off
        renderNegativeYFacing(null, aRenderer, lightingHelper, aBlock, 0, 0, 0, textureArray[SIDE_DOWN], true, -1);
        renderPositiveYFacing(null, aRenderer, lightingHelper, aBlock, 0, 0, 0, textureArray[SIDE_UP], true, -1);
        renderNegativeZFacing(null, aRenderer, lightingHelper, aBlock, 0, 0, 0, textureArray[SIDE_NORTH], true, -1);
        renderPositiveZFacing(null, aRenderer, lightingHelper, aBlock, 0, 0, 0, textureArray[SIDE_SOUTH], true, -1);
        renderNegativeXFacing(null, aRenderer, lightingHelper, aBlock, 0, 0, 0, textureArray[SIDE_WEST], true, -1);
        renderPositiveXFacing(null, aRenderer, lightingHelper, aBlock, 0, 0, 0, textureArray[SIDE_EAST], true, -1);
        // spotless:on
        aBlock.setBlockBounds(blockMin, blockMin, blockMin, blockMax, blockMax, blockMax);

        aRenderer.setRenderBoundsFromBlock(aBlock);

        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
        aRenderer.useInventoryTint = false;
    }

    private void setupBlockTexturesOnly(Block aBlock, int aMeta, boolean noCoord) {
        ForgeDirection[] validDirections = VALID_DIRECTIONS;
        for (int i = 0, validDirectionsLength = validDirections.length; i < validDirectionsLength; i++) {
            ForgeDirection tFace = validDirections[i];
            if (noCoord) {
                textureArray[i][0] = TextureFactory.builder()
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
        final LightingHelper lightingHelper = new LightingHelper(aRenderer);
        final int worldRenderPass = ForgeHooksClient.getWorldRenderPass();

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

        aBlock.setBlockBounds(blockMin, blockMin, blockMin, blockMax, blockMax, blockMax);
        aRenderer.setRenderBoundsFromBlock(aBlock);

        // spotless:off
        renderNegativeYFacing(aWorld, aRenderer, lightingHelper, aBlock, aX, aY, aZ, textureArray[SIDE_DOWN], true, worldRenderPass);
        renderPositiveYFacing(aWorld, aRenderer, lightingHelper, aBlock, aX, aY, aZ, textureArray[SIDE_UP], true, worldRenderPass);
        renderNegativeZFacing(aWorld, aRenderer, lightingHelper, aBlock, aX, aY, aZ, textureArray[SIDE_NORTH], true, worldRenderPass);
        renderPositiveZFacing(aWorld, aRenderer, lightingHelper, aBlock, aX, aY, aZ, textureArray[SIDE_SOUTH], true, worldRenderPass);
        renderNegativeXFacing(aWorld, aRenderer, lightingHelper, aBlock, aX, aY, aZ, textureArray[SIDE_WEST], true, worldRenderPass);
        renderPositiveXFacing(aWorld, aRenderer, lightingHelper, aBlock, aX, aY, aZ, textureArray[SIDE_EAST], true, worldRenderPass);
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
