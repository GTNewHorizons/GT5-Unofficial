package goodgenerator.blocks.regularBlock;

import net.minecraft.block.Block;
import net.minecraft.world.IBlockAccess;

import gregtech.api.interfaces.ITexture;

public interface ITextureBlock {

    default ITexture[] getTexture(Block aBlock, byte aSide) {
        return getTexture(aBlock, 0, aSide);
    }

    ITexture[] getTexture(Block aBlock, byte aSide, IBlockAccess aWorld, int xCoord, int yCoord, int zCoord);

    ITexture[] getTexture(Block aBlock, int aMeta, byte aSide);
}
