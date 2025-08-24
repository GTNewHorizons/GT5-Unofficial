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

package bartworks.client.renderer;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import com.gtnewhorizons.angelica.api.ThreadSafeISBRH;

import bartworks.system.material.BWMetaGeneratedBlocks;
import bartworks.system.material.TileEntityMetaGeneratedBlock;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import gregtech.GTMod;
import gregtech.api.render.ISBRInventoryContext;
import gregtech.api.render.ISBRWorldContext;
import gregtech.api.render.SBRContextHolder;
import gregtech.mixin.interfaces.accessors.TesselatorAccessor;

@ThreadSafeISBRH(perThread = true)
public class BWBlockOreRenderer implements ISimpleBlockRenderingHandler {

    public static final int renderID = RenderingRegistry.getNextAvailableRenderId();
    public static final float blockMin = 0.0F;
    public static final float blockMax = 1.0F;
    private final SBRContextHolder contextHolder = new SBRContextHolder();

    @Override
    public void renderInventoryBlock(Block aBlock, int aMeta, int modelId, RenderBlocks aRenderer) {
        ISBRInventoryContext ctx = contextHolder.getSBRInventoryContext(aBlock, aMeta, modelId, aRenderer);
        TileEntityMetaGeneratedBlock tTileEntity = ((BWMetaGeneratedBlocks) aBlock).getProperTileEntityForRendering();
        tTileEntity.mMetaData = (short) aMeta;
        aRenderer.enableAO = false;
        aRenderer.useInventoryTint = true;
        aBlock.setBlockBoundsForItemRender();
        aRenderer.setRenderBoundsFromBlock(aBlock);
        GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        ctx.renderNegativeYFacing(tTileEntity.getTexture(aBlock, ForgeDirection.DOWN));
        ctx.renderPositiveYFacing(tTileEntity.getTexture(aBlock, ForgeDirection.UP));
        ctx.renderNegativeZFacing(tTileEntity.getTexture(aBlock, ForgeDirection.NORTH));
        ctx.renderPositiveZFacing(tTileEntity.getTexture(aBlock, ForgeDirection.SOUTH));
        ctx.renderNegativeXFacing(tTileEntity.getTexture(aBlock, ForgeDirection.WEST));
        ctx.renderPositiveXFacing(tTileEntity.getTexture(aBlock, ForgeDirection.EAST));
        aRenderer.setRenderBoundsFromBlock(aBlock);
        aBlock.setBlockBounds(blockMin, blockMin, blockMin, blockMax, blockMax, blockMax);
        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
        aRenderer.useInventoryTint = false;
    }

    // spotless:off
    @Override
    public boolean renderWorldBlock(IBlockAccess aWorld, int aX, int aY, int aZ, Block aBlock, int modelId, RenderBlocks aRenderer) {
        BWMetaGeneratedBlocks tBlock = (BWMetaGeneratedBlocks) aBlock;
        if(tBlock == null) return false;

        TileEntityMetaGeneratedBlock fakeTileEntity = tBlock.getProperTileEntityForRendering(); // meh
        if(fakeTileEntity == null) return false;

        TileEntityMetaGeneratedBlock actualTileEntity = (TileEntityMetaGeneratedBlock) aWorld.getTileEntity(aX, aY, aZ);
        if(actualTileEntity == null) return false;

        final TesselatorAccessor tessAccess = (TesselatorAccessor) Tessellator.instance;
        final ISBRWorldContext ctx = contextHolder.getSBRWorldContext(aX, aY, aZ, aBlock, modelId, aRenderer);

        fakeTileEntity.mMetaData = actualTileEntity.mMetaData;
        aRenderer.useInventoryTint = false;
        aBlock.setBlockBounds(blockMin, blockMin, blockMin, blockMax, blockMax, blockMax);
        aRenderer.enableAO = Minecraft.isAmbientOcclusionEnabled() && GTMod.proxy.mRenderTileAmbientOcclusion;
        aRenderer.setRenderBoundsFromBlock(aBlock);
        ctx.renderNegativeYFacing(fakeTileEntity.getTexture(aBlock, ForgeDirection.DOWN));
        ctx.renderPositiveYFacing(fakeTileEntity.getTexture(aBlock, ForgeDirection.UP));
        ctx.renderNegativeZFacing(fakeTileEntity.getTexture(aBlock, ForgeDirection.NORTH));
        ctx.renderPositiveZFacing(fakeTileEntity.getTexture(aBlock, ForgeDirection.SOUTH));
        ctx.renderNegativeXFacing(fakeTileEntity.getTexture(aBlock, ForgeDirection.WEST));
        ctx.renderPositiveXFacing(fakeTileEntity.getTexture(aBlock, ForgeDirection.EAST));
        return tessAccess.gt5u$hasVertices();
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
