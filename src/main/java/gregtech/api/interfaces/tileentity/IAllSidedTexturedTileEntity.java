package gregtech.api.interfaces.tileentity;

import net.minecraft.block.Block;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.interfaces.ITexture;

/**
 * Block which has same texture on all sides
 */
public interface IAllSidedTexturedTileEntity extends ITexturedTileEntity {

    /**
     * @return the Textures rendered by the GT Rendering
     */
    default ITexture[] getTexture(Block aBlock, ForgeDirection side) {
        return getTexture(aBlock);
    }

    /**
     * @return the Textures rendered by the GT Rendering
     */
    ITexture[] getTexture(Block aBlock);
}
