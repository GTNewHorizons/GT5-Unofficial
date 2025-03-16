package gregtech.common.render;

import static gregtech.api.enums.GTValues.*;
import static gregtech.common.render.GTRendererBlock.*;
import static net.minecraftforge.common.util.ForgeDirection.VALID_DIRECTIONS;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import com.gtnewhorizons.angelica.api.ThreadSafeISBRH;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import gregtech.api.interfaces.ITexture;
import gregtech.api.render.RenderOverlay;
import gregtech.api.render.TextureFactory;

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

        setupBlockTexturesOnly(aBlock, aMeta);

        GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);

        aBlock.setBlockBoundsForItemRender();
        aRenderer.setRenderBoundsFromBlock(aBlock);
        // spotless:off
        renderNegativeYFacing(null, aRenderer, aBlock, 0, 0, 0, textureArray[SIDE_DOWN], true);
        renderPositiveYFacing(null, aRenderer, aBlock, 0, 0, 0, textureArray[SIDE_UP], true);
        renderNegativeZFacing(null, aRenderer, aBlock, 0, 0, 0, textureArray[SIDE_NORTH], true);
        renderPositiveZFacing(null, aRenderer, aBlock, 0, 0, 0, textureArray[SIDE_SOUTH], true);
        renderNegativeXFacing(null, aRenderer, aBlock, 0, 0, 0, textureArray[SIDE_WEST], true);
        renderPositiveXFacing(null, aRenderer, aBlock, 0, 0, 0, textureArray[SIDE_EAST], true);
        // spotless:on
        aBlock.setBlockBounds(blockMin, blockMin, blockMin, blockMax, blockMax, blockMax);

        aRenderer.setRenderBoundsFromBlock(aBlock);

        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
        aRenderer.useInventoryTint = false;
    }

    private void setupBlockTexturesOnly(Block aBlock, int aMeta) {
        ForgeDirection[] validDirections = VALID_DIRECTIONS;
        for (int i = 0, validDirectionsLength = validDirections.length; i < validDirectionsLength; i++) {
            ForgeDirection tFace = validDirections[i];
            textureArray[i][0] = TextureFactory.of(aBlock, aMeta, tFace);
            textureArray[i][1] = null;
        }
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess aWorld, int aX, int aY, int aZ, Block aBlock, int aModelID,
        RenderBlocks aRenderer) {
        aRenderer.enableAO = Minecraft.isAmbientOcclusionEnabled();
        aRenderer.useInventoryTint = false;

        int tMeta = aWorld.getBlockMetadata(aX, aY, aZ);

        ITexture[] overlayed = RenderOverlay.get(aWorld, aX, aY, aZ);
        if (overlayed == null) {
            setupBlockTexturesOnly(aBlock, tMeta);
        } else {
            ForgeDirection[] validDirections = VALID_DIRECTIONS;
            for (int i = 0, validDirectionsLength = validDirections.length; i < validDirectionsLength; i++) {
                ForgeDirection tFace = validDirections[i];
                textureArray[i][0] = TextureFactory.of(aBlock, tMeta, tFace);
                textureArray[i][1] = overlayed[i];
            }
        }

        aBlock.setBlockBounds(blockMin, blockMin, blockMin, blockMax, blockMax, blockMax);
        aRenderer.setRenderBoundsFromBlock(aBlock);

        // spotless:off
        renderNegativeYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, textureArray[SIDE_DOWN], true);
        renderPositiveYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, textureArray[SIDE_UP], true);
        renderNegativeZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, textureArray[SIDE_NORTH], true);
        renderPositiveZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, textureArray[SIDE_SOUTH], true);
        renderNegativeXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, textureArray[SIDE_WEST], true);
        renderPositiveXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, textureArray[SIDE_EAST], true);
        // spotless:on

        return true;
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
