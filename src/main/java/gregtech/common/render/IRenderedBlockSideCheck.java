package gregtech.common.render;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public interface IRenderedBlockSideCheck {

    /** returning false stops all the other Rendering from happening on that Side. */
    @SideOnly(Side.CLIENT)
    boolean renderFullBlockSide(Block aBlock, RenderBlocks aRenderer, ForgeDirection aSide);
}
