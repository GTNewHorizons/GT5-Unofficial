package gtPlusPlus.core.client.renderer;

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
import gregtech.GTMod;
import gregtech.api.render.ISBRInventoryContext;
import gregtech.api.render.ISBRWorldContext;
import gregtech.api.render.SBRContextHolder;
import gregtech.mixin.interfaces.accessors.TesselatorAccessor;
import gtPlusPlus.api.interfaces.ITexturedBlock;

@ThreadSafeISBRH(perThread = true)
public class CustomOreBlockRenderer implements ISimpleBlockRenderingHandler {

    public static final int mRenderID = RenderingRegistry.getNextAvailableRenderId();
    private final SBRContextHolder contextHolder = new SBRContextHolder();

    @Override
    public void renderInventoryBlock(Block aBlock, int aMeta, int aModelID, RenderBlocks aRenderer) {
        final ISBRInventoryContext ctx = contextHolder.getSBRInventoryContext(aBlock, aMeta, aModelID, aRenderer);
        aRenderer.enableAO = false;
        aRenderer.useInventoryTint = true;
        GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        aBlock.setBlockBoundsForItemRender();
        aRenderer.setRenderBoundsFromBlock(aBlock);

        ITexturedBlock textures = (ITexturedBlock) aBlock;

        // spotless:off
        ctx.renderNegativeYFacing(textures.getTexture(ForgeDirection.DOWN));
        ctx.renderPositiveYFacing(textures.getTexture(ForgeDirection.UP));
        ctx.renderNegativeZFacing(textures.getTexture(ForgeDirection.NORTH));
        ctx.renderPositiveZFacing(textures.getTexture(ForgeDirection.SOUTH));
        ctx.renderNegativeXFacing(textures.getTexture(ForgeDirection.WEST));
        ctx.renderPositiveXFacing(textures.getTexture(ForgeDirection.EAST));
        // spotless:on
        aBlock.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        aRenderer.setRenderBoundsFromBlock(aBlock);
        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
        aRenderer.useInventoryTint = false;
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess aWorld, int aX, int aY, int aZ, Block aBlock, int aModelID,
        RenderBlocks aRenderer) {
        if (!(aBlock instanceof ITexturedBlock textures)) {
            return false;
        }

        final TesselatorAccessor tessAccess = (TesselatorAccessor) Tessellator.instance;
        final ISBRWorldContext ctx = contextHolder.getSBRWorldContext(aX, aY, aZ, aBlock, aModelID, aRenderer);

        aRenderer.enableAO = Minecraft.isAmbientOcclusionEnabled() && GTMod.proxy.mRenderTileAmbientOcclusion;
        aRenderer.useInventoryTint = false;
        aBlock.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        aRenderer.setRenderBoundsFromBlock(aBlock);

        ctx.renderNegativeYFacing(textures.getTexture(ForgeDirection.DOWN));
        ctx.renderPositiveYFacing(textures.getTexture(ForgeDirection.UP));
        ctx.renderNegativeZFacing(textures.getTexture(ForgeDirection.NORTH));
        ctx.renderPositiveZFacing(textures.getTexture(ForgeDirection.SOUTH));
        ctx.renderNegativeXFacing(textures.getTexture(ForgeDirection.WEST));
        ctx.renderPositiveXFacing(textures.getTexture(ForgeDirection.EAST));

        aRenderer.enableAO = false;
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
