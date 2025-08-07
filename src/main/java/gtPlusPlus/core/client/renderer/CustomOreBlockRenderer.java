package gtPlusPlus.core.client.renderer;

import static gregtech.common.render.GTRendererBlock.renderNegativeXFacing;
import static gregtech.common.render.GTRendererBlock.renderNegativeYFacing;
import static gregtech.common.render.GTRendererBlock.renderNegativeZFacing;
import static gregtech.common.render.GTRendererBlock.renderPositiveXFacing;
import static gregtech.common.render.GTRendererBlock.renderPositiveYFacing;
import static gregtech.common.render.GTRendererBlock.renderPositiveZFacing;

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
import gregtech.GTMod;
import gregtech.api.render.SBRContext;
import gregtech.mixin.interfaces.accessors.TesselatorAccessor;
import gtPlusPlus.api.interfaces.ITexturedBlock;
import gtPlusPlus.api.objects.Logger;

@ThreadSafeISBRH(perThread = true)
public class CustomOreBlockRenderer implements ISimpleBlockRenderingHandler {

    public static CustomOreBlockRenderer INSTANCE;
    public final int mRenderID;

    public CustomOreBlockRenderer() {
        INSTANCE = this;
        this.mRenderID = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(this);
        Logger.INFO("Registered Custom Ore Block Renderer.");
    }

    @Override
    public void renderInventoryBlock(Block aBlock, int aMeta, int aModelID, RenderBlocks aRenderer) {
        final SBRContext ctx = new SBRContext(aBlock, aMeta, aModelID, aRenderer);
        aRenderer.enableAO = false;
        aRenderer.useInventoryTint = true;
        GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        aBlock.setBlockBoundsForItemRender();
        aRenderer.setRenderBoundsFromBlock(aBlock);

        ITexturedBlock textures = (ITexturedBlock) aBlock;

        // spotless:off
        renderNegativeYFacing(ctx, textures.getTexture(ForgeDirection.DOWN), true);
        renderPositiveYFacing(ctx, textures.getTexture(ForgeDirection.UP), true);
        renderNegativeZFacing(ctx, textures.getTexture(ForgeDirection.NORTH), true);
        renderPositiveZFacing(ctx, textures.getTexture(ForgeDirection.SOUTH), true);
        renderNegativeXFacing(ctx, textures.getTexture(ForgeDirection.WEST), true);
        renderPositiveXFacing(ctx, textures.getTexture(ForgeDirection.EAST), true);
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
        final SBRContext ctx = new SBRContext(aX, aY, aZ, aBlock, aModelID, aRenderer);
        final int worldRenderPass = ForgeHooksClient.getWorldRenderPass();

        aRenderer.enableAO = Minecraft.isAmbientOcclusionEnabled() && GTMod.proxy.mRenderTileAmbientOcclusion;
        aRenderer.useInventoryTint = false;
        aBlock.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        aRenderer.setRenderBoundsFromBlock(aBlock);

        renderNegativeYFacing(ctx, textures.getTexture(ForgeDirection.DOWN), true);
        renderPositiveYFacing(ctx, textures.getTexture(ForgeDirection.UP), true);
        renderNegativeZFacing(ctx, textures.getTexture(ForgeDirection.NORTH), true);
        renderPositiveZFacing(ctx, textures.getTexture(ForgeDirection.SOUTH), true);
        renderNegativeXFacing(ctx, textures.getTexture(ForgeDirection.WEST), true);
        renderPositiveXFacing(ctx, textures.getTexture(ForgeDirection.EAST), true);

        aRenderer.enableAO = false;
        return tessAccess.gt5u$hasVertices();
    }

    @Override
    public boolean shouldRender3DInInventory(int aModel) {
        return true;
    }

    @Override
    public int getRenderId() {
        return this.mRenderID;
    }
}
