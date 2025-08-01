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
import gregtech.api.util.LightingHelper;
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
        final LightingHelper lightingHelper = new LightingHelper(aRenderer);
        aRenderer.enableAO = false;
        aRenderer.useInventoryTint = true;
        GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        aBlock.setBlockBoundsForItemRender();
        aRenderer.setRenderBoundsFromBlock(aBlock);

        ITexturedBlock textures = (ITexturedBlock) aBlock;

        // spotless:off
        renderNegativeYFacing(null, aRenderer, lightingHelper, aBlock, 0, 0, 0, textures.getTexture(ForgeDirection.DOWN), true, -1);
        renderPositiveYFacing(null, aRenderer, lightingHelper, aBlock, 0, 0, 0, textures.getTexture(ForgeDirection.UP), true, -1);
        renderNegativeZFacing(null, aRenderer, lightingHelper, aBlock, 0, 0, 0, textures.getTexture(ForgeDirection.NORTH), true, -1);
        renderPositiveZFacing(null, aRenderer, lightingHelper, aBlock, 0, 0, 0, textures.getTexture(ForgeDirection.SOUTH), true, -1);
        renderNegativeXFacing(null, aRenderer, lightingHelper, aBlock, 0, 0, 0, textures.getTexture(ForgeDirection.WEST), true, -1);
        renderPositiveXFacing(null, aRenderer, lightingHelper, aBlock, 0, 0, 0, textures.getTexture(ForgeDirection.EAST), true, -1);
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
        final LightingHelper lightingHelper = new LightingHelper(aRenderer);
        final int worldRenderPass = ForgeHooksClient.getWorldRenderPass();

        aRenderer.enableAO = Minecraft.isAmbientOcclusionEnabled() && GTMod.proxy.mRenderTileAmbientOcclusion;
        aRenderer.useInventoryTint = false;
        aBlock.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        aRenderer.setRenderBoundsFromBlock(aBlock);

        // spotless:off
        renderNegativeYFacing(aWorld, aRenderer, lightingHelper, aBlock, aX, aY, aZ, textures.getTexture(ForgeDirection.DOWN), true, worldRenderPass);
        renderPositiveYFacing(aWorld, aRenderer, lightingHelper, aBlock, aX, aY, aZ, textures.getTexture(ForgeDirection.UP), true, worldRenderPass);
        renderNegativeZFacing(aWorld, aRenderer, lightingHelper, aBlock, aX, aY, aZ, textures.getTexture(ForgeDirection.NORTH), true, worldRenderPass);
        renderPositiveZFacing(aWorld, aRenderer, lightingHelper, aBlock, aX, aY, aZ, textures.getTexture(ForgeDirection.SOUTH), true, worldRenderPass);
        renderNegativeXFacing(aWorld, aRenderer, lightingHelper, aBlock, aX, aY, aZ, textures.getTexture(ForgeDirection.WEST), true, worldRenderPass);
        renderPositiveXFacing(aWorld, aRenderer, lightingHelper, aBlock, aX, aY, aZ, textures.getTexture(ForgeDirection.EAST), true, worldRenderPass);
        // spotless:on

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
