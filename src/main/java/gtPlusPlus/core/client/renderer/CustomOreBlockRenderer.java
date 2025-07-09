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
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import com.gtnewhorizons.angelica.api.ThreadSafeISBRH;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import gregtech.GTMod;
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
        aRenderer.enableAO = false;
        aRenderer.useInventoryTint = true;
        GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        aBlock.setBlockBoundsForItemRender();
        aRenderer.setRenderBoundsFromBlock(aBlock);

        ITexturedBlock textures = (ITexturedBlock) aBlock;

        renderNegativeYFacing(null, aRenderer, aBlock, 0, 0, 0, textures.getTexture(ForgeDirection.DOWN), true);
        renderPositiveYFacing(null, aRenderer, aBlock, 0, 0, 0, textures.getTexture(ForgeDirection.UP), true);
        renderNegativeZFacing(null, aRenderer, aBlock, 0, 0, 0, textures.getTexture(ForgeDirection.NORTH), true);
        renderPositiveZFacing(null, aRenderer, aBlock, 0, 0, 0, textures.getTexture(ForgeDirection.SOUTH), true);
        renderNegativeXFacing(null, aRenderer, aBlock, 0, 0, 0, textures.getTexture(ForgeDirection.WEST), true);
        renderPositiveXFacing(null, aRenderer, aBlock, 0, 0, 0, textures.getTexture(ForgeDirection.EAST), true);

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

        aRenderer.enableAO = Minecraft.isAmbientOcclusionEnabled() && GTMod.proxy.mRenderTileAmbientOcclusion;
        aRenderer.useInventoryTint = false;
        aBlock.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        aRenderer.setRenderBoundsFromBlock(aBlock);

        renderNegativeYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, textures.getTexture(ForgeDirection.DOWN), true);
        renderPositiveYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, textures.getTexture(ForgeDirection.UP), true);
        renderNegativeZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, textures.getTexture(ForgeDirection.NORTH), true);
        renderPositiveZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, textures.getTexture(ForgeDirection.SOUTH), true);
        renderNegativeXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, textures.getTexture(ForgeDirection.WEST), true);
        renderPositiveXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, textures.getTexture(ForgeDirection.EAST), true);

        aRenderer.enableAO = false;
        return true;
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
