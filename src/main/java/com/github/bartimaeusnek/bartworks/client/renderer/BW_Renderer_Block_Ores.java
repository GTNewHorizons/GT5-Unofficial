/*
 * Copyright (c) 2018-2020 bartimaeusnek Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following
 * conditions: The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software. THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
 * ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */

package com.github.bartimaeusnek.bartworks.client.renderer;

import static gregtech.common.render.GT_Renderer_Block.renderNegativeXFacing;
import static gregtech.common.render.GT_Renderer_Block.renderNegativeYFacing;
import static gregtech.common.render.GT_Renderer_Block.renderNegativeZFacing;
import static gregtech.common.render.GT_Renderer_Block.renderPositiveXFacing;
import static gregtech.common.render.GT_Renderer_Block.renderPositiveYFacing;
import static gregtech.common.render.GT_Renderer_Block.renderPositiveZFacing;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import com.github.bartimaeusnek.bartworks.system.material.BW_MetaGenerated_Block_TE;
import com.github.bartimaeusnek.bartworks.system.material.BW_MetaGenerated_Blocks;
import com.gtnewhorizons.angelica.api.ThreadSafeISBRH;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import gregtech.GT_Mod;

@ThreadSafeISBRH(perThread = true)
public class BW_Renderer_Block_Ores implements ISimpleBlockRenderingHandler {

    public static BW_Renderer_Block_Ores INSTANCE;
    public static int renderID;
    public static final float blockMin = 0.0F;
    public static final float blockMax = 1.0F;

    public static void register() {
        renderID = RenderingRegistry.getNextAvailableRenderId();
        INSTANCE = new BW_Renderer_Block_Ores();
        RenderingRegistry.registerBlockHandler(INSTANCE);
    }

    @Override
    public void renderInventoryBlock(Block aBlock, int aMeta, int modelId, RenderBlocks aRenderer) {
        BW_MetaGenerated_Block_TE tTileEntity = ((BW_MetaGenerated_Blocks) aBlock).getProperTileEntityForRendering();
        tTileEntity.mMetaData = (short) aMeta;
        aRenderer.enableAO = false;
        aRenderer.useInventoryTint = true;
        aBlock.setBlockBoundsForItemRender();
        aRenderer.setRenderBoundsFromBlock(aBlock);
        GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        renderNegativeYFacing(
            null,
            aRenderer,
            aBlock,
            0,
            0,
            0,
            tTileEntity.getTexture(aBlock, ForgeDirection.DOWN),
            true);
        renderPositiveYFacing(
            null,
            aRenderer,
            aBlock,
            0,
            0,
            0,
            tTileEntity.getTexture(aBlock, ForgeDirection.UP),
            true);
        renderNegativeZFacing(
            null,
            aRenderer,
            aBlock,
            0,
            0,
            0,
            tTileEntity.getTexture(aBlock, ForgeDirection.NORTH),
            true);
        renderPositiveZFacing(
            null,
            aRenderer,
            aBlock,
            0,
            0,
            0,
            tTileEntity.getTexture(aBlock, ForgeDirection.SOUTH),
            true);
        renderNegativeXFacing(
            null,
            aRenderer,
            aBlock,
            0,
            0,
            0,
            tTileEntity.getTexture(aBlock, ForgeDirection.WEST),
            true);
        renderPositiveXFacing(
            null,
            aRenderer,
            aBlock,
            0,
            0,
            0,
            tTileEntity.getTexture(aBlock, ForgeDirection.EAST),
            true);
        aRenderer.setRenderBoundsFromBlock(aBlock);
        aBlock.setBlockBounds(blockMin, blockMin, blockMin, blockMax, blockMax, blockMax);
        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
        aRenderer.useInventoryTint = false;
    }

    // spotless:off
    @Override
    public boolean renderWorldBlock(IBlockAccess aWorld, int aX, int aY, int aZ, Block aBlock, int modelId, RenderBlocks aRenderer) {
        BW_MetaGenerated_Blocks tBlock = (BW_MetaGenerated_Blocks) aBlock;
        if(tBlock == null) return false;

        BW_MetaGenerated_Block_TE fakeTileEntity = tBlock.getProperTileEntityForRendering(); // meh
        if(fakeTileEntity == null) return false;

        BW_MetaGenerated_Block_TE actualTileEntity = (BW_MetaGenerated_Block_TE) aWorld.getTileEntity(aX, aY, aZ);
        if(actualTileEntity == null) return false;

        fakeTileEntity.mMetaData = actualTileEntity.mMetaData;
        aRenderer.useInventoryTint = false;
        aBlock.setBlockBounds(blockMin, blockMin, blockMin, blockMax, blockMax, blockMax);
        aRenderer.enableAO = Minecraft.isAmbientOcclusionEnabled() && GT_Mod.gregtechproxy.mRenderTileAmbientOcclusion;
        aRenderer.setRenderBoundsFromBlock(aBlock);
        renderNegativeYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, fakeTileEntity.getTexture(aBlock, ForgeDirection.DOWN), true);
        renderPositiveYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, fakeTileEntity.getTexture(aBlock, ForgeDirection.UP), true);
        renderNegativeZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, fakeTileEntity.getTexture(aBlock, ForgeDirection.NORTH), true);
        renderPositiveZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, fakeTileEntity.getTexture(aBlock, ForgeDirection.SOUTH), true);
        renderNegativeXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, fakeTileEntity.getTexture(aBlock, ForgeDirection.WEST), true);
        renderPositiveXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, fakeTileEntity.getTexture(aBlock, ForgeDirection.EAST), true);
        return true;
    }
    // spotless:on

    @Override
    public boolean shouldRender3DInInventory(int modelId) {
        return true;
    }

    @Override
    public int getRenderId() {
        return renderID;
    }
}
