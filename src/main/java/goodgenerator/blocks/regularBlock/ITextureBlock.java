package goodgenerator.blocks.regularBlock;

import net.minecraft.block.Block;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.interfaces.ITexture;

public interface ITextureBlock {

    default ITexture[] getTexture(Block aBlock, ForgeDirection side) {
        return getTexture(aBlock, 0, side);
    }

    ITexture[] getTexture(Block aBlock, ForgeDirection side, IBlockAccess aWorld, int xCoord, int yCoord, int zCoord);

    ITexture[] getTexture(Block aBlock, int aMeta, ForgeDirection side);
}
