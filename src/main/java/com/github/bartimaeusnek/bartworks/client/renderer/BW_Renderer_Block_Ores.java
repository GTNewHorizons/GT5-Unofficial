/*
 * Copyright (c) 2018-2019 bartimaeusnek
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.bartimaeusnek.bartworks.client.renderer;

import com.github.bartimaeusnek.bartworks.system.material.BW_MetaGenerated_Block_TE;
import com.github.bartimaeusnek.bartworks.system.material.BW_MetaGenerated_Blocks;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;

import static gregtech.common.render.GT_Renderer_Block.*;

public class BW_Renderer_Block_Ores implements ISimpleBlockRenderingHandler {
    public static BW_Renderer_Block_Ores INSTANCE = new BW_Renderer_Block_Ores();
    public final int mRenderID = RenderingRegistry.getNextAvailableRenderId();

    @Override
    public void renderInventoryBlock(Block aBlock, int aMeta, int modelId, RenderBlocks aRenderer) {
        BW_MetaGenerated_Block_TE tTileEntity = ((BW_MetaGenerated_Blocks)aBlock).getProperTileEntityForRendering();
        tTileEntity.mMetaData = (short) aMeta;
        aBlock.setBlockBoundsForItemRender();
        aRenderer.setRenderBoundsFromBlock(aBlock);
        GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        Tessellator.instance.startDrawingQuads();
        Tessellator.instance.setNormal(0.0F, -1.0F, 0.0F);
        renderNegativeYFacing(null, aRenderer, aBlock, 0, 0, 0, tTileEntity.getTexture(aBlock, (byte) 0), true);
        Tessellator.instance.draw();
        Tessellator.instance.startDrawingQuads();
        Tessellator.instance.setNormal(0.0F, 1.0F, 0.0F);
        renderPositiveYFacing(null, aRenderer, aBlock, 0, 0, 0, tTileEntity.getTexture(aBlock, (byte) 1), true);
        Tessellator.instance.draw();
        Tessellator.instance.startDrawingQuads();
        Tessellator.instance.setNormal(0.0F, 0.0F, -1.0F);
        renderNegativeZFacing(null, aRenderer, aBlock, 0, 0, 0, tTileEntity.getTexture(aBlock, (byte) 2), true);
        Tessellator.instance.draw();
        Tessellator.instance.startDrawingQuads();
        Tessellator.instance.setNormal(0.0F, 0.0F, 1.0F);
        renderPositiveZFacing(null, aRenderer, aBlock, 0, 0, 0, tTileEntity.getTexture(aBlock, (byte) 3), true);
        Tessellator.instance.draw();
        Tessellator.instance.startDrawingQuads();
        Tessellator.instance.setNormal(-1.0F, 0.0F, 0.0F);
        renderNegativeXFacing(null, aRenderer, aBlock, 0, 0, 0, tTileEntity.getTexture(aBlock, (byte) 4), true);
        Tessellator.instance.draw();
        Tessellator.instance.startDrawingQuads();
        Tessellator.instance.setNormal(1.0F, 0.0F, 0.0F);
        renderPositiveXFacing(null, aRenderer, aBlock, 0, 0, 0, tTileEntity.getTexture(aBlock, (byte) 5), true);
        Tessellator.instance.draw();
        aBlock.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        aRenderer.setRenderBoundsFromBlock(aBlock);
        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        return renderStandardBlock(world, x, y, z, block, renderer);
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId) {
        return true;
    }

    @Override
    public int getRenderId() {
        return this.mRenderID;
    }
}
