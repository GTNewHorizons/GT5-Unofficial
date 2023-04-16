package gregtech.api.interfaces.tileentity;

import net.minecraft.block.Block;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.interfaces.ITexture;

public interface ITexturedTileEntity {

    /**
     * @return the Textures rendered by the GT Rendering
     */
    ITexture[] getTexture(Block aBlock, ForgeDirection aSide);
}
