package gregtech.api.interfaces.tileentity;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.interfaces.ITexture;

public interface ITexturedTileEntity {

    /**
     * @return the Textures rendered by the GT Rendering
     */
    ITexture[] getTexture(Block aBlock, ForgeDirection side);

    default ITexture[] getTexture(ForgeDirection side) {
        return getTexture(Blocks.air, side);
    }
}
