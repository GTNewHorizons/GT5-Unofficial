package gregtech.common.render;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.render.ISBRInventoryContext;
import gregtech.api.render.SBRContextHolder;
import gregtech.common.blocks.BlockMetal;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.src.FMLRenderAccessLibrary;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.opengl.GL11;

public class MHDCSMBlockRenderer implements ISimpleBlockRenderingHandler {

    public static float pixel = 1F / 16F;
    public static float middle = (1F - pixel) / 2F;

    public static IIcon textureInner;
    public static IIcon textureHalo;
    public static int mhdcsm_meta = 13;
    public static boolean _texturesLoaded = false;

    public static boolean texturesLoaded() {
        if (!_texturesLoaded) {
            textureInner = Materials.RawStarMatter.mFluid.getIcon();
            textureHalo = Textures.BlockIcons.MHDCSM_HALO.getIcon();
            _texturesLoaded = textureInner != null && textureHalo != null;
        }
        return _texturesLoaded;
    }

    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        if (world.getBlockMetadata(x, y, z) != mhdcsm_meta) {
            return FMLRenderAccessLibrary.renderWorldBlock(renderer, world, x, y ,z, block, GTRendererCasing.mRenderID);
        }
        if (!texturesLoaded()) {
            return false;
        }

        Tessellator tessellator = Tessellator.instance;
        tessellator.setColorOpaque_F(1.0f, 1.0f, 1.0f);
        renderer.enableAO = false;

        renderer.renderAllFaces = true;
        renderer.setRenderBounds(pixel, pixel, pixel, 1.0F - pixel, 1.0F - pixel, 1.0F - pixel);
        // remove this call to better understand the renderer's logic
        renderEmissiveBlock(block, x, y, z, renderer, textureInner);
        renderer.renderAllFaces = false;

        for (int rx = -1; rx <= 1; rx++) {
            for (int ry = -1; ry <= 1; ry++) {
                for (int rz = -1; rz <= 1; rz++) {
                    int taxicabDistance = Math.abs(rx) + Math.abs(ry) + Math.abs(rz);
                    boolean face = taxicabDistance == 1;
                    boolean edge = taxicabDistance == 2;
                    boolean corner = taxicabDistance == 3;
                    if ((face || edge) && canConnectTo(block, x+rx, y+ry, z+rz)) {
                        // only handle diagonals with blocks
                        if (edge) {
                            int neighborCount = 0;
                            if (rx != 0 && canConnectTo(block, x+rx, y, z)) neighborCount++;
                            if (ry != 0 && canConnectTo(block, x, y+ry, z)) neighborCount++;
                            if (rz != 0 && canConnectTo(block, x, y, z+rz)) neighborCount++;
                            if (neighborCount != 2) continue;
                        }
                        renderer.renderAllFaces = true;
                        // spotless:off
                        float minX = (rx == 0) ?        pixel : middle * (1 + rx);
                        float minY = (ry == 0) ?        pixel : middle * (1 + ry);
                        float minZ = (rz == 0) ?        pixel : middle * (1 + rz);
                        float maxX = (rx == 0) ? 1.0f - pixel : middle * (1 + rx) + pixel;
                        float maxY = (ry == 0) ? 1.0f - pixel : middle * (1 + ry) + pixel;
                        float maxZ = (rz == 0) ? 1.0f - pixel : middle * (1 + rz) + pixel;
                        // spotless:on
                        renderer.setRenderBounds(minX, minY, minZ, maxX, maxY, maxZ);
                        renderEmissiveBlock(block, x, y, z, renderer, textureInner);
                        renderer.renderAllFaces = false;
                    } else if (face) {
                        // prevents Z-fighting with non-solid blocks
                        float depthAdjustment = world.getBlock(x+rx, y+ry, z+rz).isOpaqueCube() ? 0 : 0.0001f;
                        renderer.setRenderBounds(
                            depthAdjustment,
                            depthAdjustment,
                            depthAdjustment,
                            1 - depthAdjustment,
                            1 - depthAdjustment,
                            1 - depthAdjustment
                        );
                        renderer.renderFromInside = true;
                        // spotless:off
                        if      (rx == -1) renderer.renderFaceXNeg(block, x, y, z, textureHalo);
                        else if (rx ==  1) renderer.renderFaceXPos(block, x, y, z, textureHalo);
                        else if (ry == -1) renderer.renderFaceYNeg(block, x, y, z, textureHalo);
                        else if (ry ==  1) renderer.renderFaceYPos(block, x, y, z, textureHalo);
                        else if (rz == -1) renderer.renderFaceZNeg(block, x, y, z, textureHalo);
                        else if (rz ==  1) renderer.renderFaceZPos(block, x, y, z, textureHalo);
                        // spotless:on
                        renderer.renderFromInside = false;
                    }
                }
            }
        }

        block.setBlockBoundsForItemRender();
        renderer.enableAO = true;
        return true;
    }

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
        if (metadata != mhdcsm_meta) {
            FMLRenderAccessLibrary.renderInventoryBlock(renderer, block, metadata, GTRendererCasing.mRenderID);
            return;
        }

        Tessellator tessellator = Tessellator.instance;

        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        renderer.setRenderBounds(pixel, pixel, pixel, 1 - pixel, 1 - pixel, 1 - pixel);
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0f, 1.0f, 0.0f);
        renderer.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, textureInner);
        tessellator.setNormal(0.0f, 1.0f, 0.0f);
        renderer.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, textureInner);
        tessellator.setNormal(0.0f, 1.0f, 0.0f);
        renderer.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, textureInner);
        tessellator.setNormal(0.0f, 1.0f, 0.0f);
        renderer.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, textureInner);
        tessellator.setNormal(0.0f, 1.0f, 0.0f);
        renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, textureInner);
        tessellator.setNormal(0.0f, 1.0f, 0.0f);
        renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, textureInner);
        tessellator.draw();
        renderer.setRenderBounds(0, 0, 0, 1, 1, 1);
        renderer.renderFromInside = true;
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0f, 1.0f, 0.0f);
        renderer.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, textureHalo);
        tessellator.setNormal(0.0f, 1.0f, 0.0f);
        renderer.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, textureHalo);
        tessellator.setNormal(0.0f, 1.0f, 0.0f);
        renderer.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, textureHalo);
        tessellator.setNormal(0.0f, 1.0f, 0.0f);
        renderer.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, textureHalo);
        tessellator.setNormal(0.0f, 1.0f, 0.0f);
        renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, textureHalo);
        tessellator.setNormal(0.0f, 1.0f, 0.0f);
        renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, textureHalo);
        tessellator.setNormal(0.0f, 1.0f, 0.0f);
        tessellator.draw();
        renderer.renderFromInside = false;

        GL11.glTranslatef(0.5F, 0.5F, 0.5F);

        block.setBlockBoundsForItemRender();
    }

    public void renderEmissiveBlock(Block block, int x, int y, int z, RenderBlocks renderer, IIcon texture) {
        // for some reason +x and -z need field_152631_f set to true, but not any other sides
        renderer.field_152631_f = true;
        renderer.renderFaceXPos(block, x, y, z, texture);
        renderer.renderFaceZNeg(block, x, y, z, texture);
        renderer.field_152631_f = false;
        renderer.renderFaceXNeg(block, x, y, z, texture);
        renderer.renderFaceYNeg(block, x, y, z, texture);
        renderer.renderFaceYPos(block, x, y, z, texture);
        renderer.renderFaceZPos(block, x, y, z, texture);
    }

    public static boolean canConnectTo(Block block, int x, int y, int z) {
        IBlockAccess world = Minecraft.getMinecraft().theWorld;
        return world.getBlock(x, y, z).isAssociatedBlock(block) && world.getBlockMetadata(x, y, z) == mhdcsm_meta;
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId) {
        return true;
    }

    @Override
    public int getRenderId() {
        return BlockMetal.RENDER_ID;
    }
}
