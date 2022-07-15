package goodgenerator.blocks.regularBlock;

import gregtech.api.interfaces.ITexture;
import net.minecraft.block.Block;
import net.minecraft.world.IBlockAccess;

public interface ITextureBlock {

    default ITexture[] getTexture(Block aBlock, byte aSide) {
        return getTexture(aBlock, 0, aSide);
    }

    ITexture[] getTexture(Block aBlock, byte aSide, IBlockAccess aWorld, int xCoord, int yCoord, int zCoord);

    ITexture[] getTexture(Block aBlock, int aMeta, byte aSide);
}
