package goodgenerator.blocks.regularBlock;

import gregtech.api.interfaces.ITexture;
import net.minecraft.block.Block;
import net.minecraft.world.IBlockAccess;

public interface ITextureBlock {

    ITexture[] getTexture(Block aBlock, byte aSide);

    ITexture[] getTexture(Block aBlock, byte aSide, IBlockAccess aWorld, int xCoord, int yCoord, int zCoord);

}
