package goodgenerator.blocks.regularBlock;

import gregtech.api.interfaces.ITexture;
import net.minecraft.block.Block;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

public interface ITextureBlock {

    default ITexture[] getTexture(Block aBlock, ForgeDirection side) {
        return getTexture(aBlock, 0, side);
    }

    ITexture[] getTexture(Block aBlock, ForgeDirection side, IBlockAccess aWorld, int xCoord, int yCoord, int zCoord);

    ITexture[] getTexture(Block aBlock, int aMeta, ForgeDirection side);
}
