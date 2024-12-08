package goodgenerator.blocks.regularBlock;

import net.minecraft.block.Block;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

import goodgenerator.client.render.BlockRenderHandler;
import goodgenerator.main.GoodGenerator;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.render.TextureFactory;

public class BlockTurbineCasing extends BlockCasing implements ITextureBlock {

    public IIconContainer base;

    public BlockTurbineCasing(String name, String texture) {
        super(name, new String[] { GoodGenerator.MOD_ID + ":" + texture });
        base = new Textures.BlockIcons.CustomIcon("icons/" + texture);
    }

    @Override
    public ITexture[] getTexture(Block aBlock, ForgeDirection side, IBlockAccess aWorld, int xCoord, int yCoord,
        int zCoord) {
        return getTexture(aBlock, side);
    }

    @Override
    public ITexture[] getTexture(Block aBlock, int aMeta, ForgeDirection side) {
        return new ITexture[] { TextureFactory.of(base) };
    }

    @Override
    public int getRenderType() {
        if (BlockRenderHandler.INSTANCE == null) {
            return super.getRenderType();
        }
        return BlockRenderHandler.INSTANCE.mRenderID;
    }
}
