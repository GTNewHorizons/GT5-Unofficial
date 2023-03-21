package gregtech.api.ModernMaterials.Blocks.FrameBox;

import gregtech.api.ModernMaterials.Blocks.BlocksEnum;
import gregtech.api.ModernMaterials.Blocks.DumbBase.DumbBlock;
import gregtech.api.ModernMaterials.Blocks.DumbBase.DumbTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class FrameBoxBlock extends DumbBlock {

    @Override
    public BlocksEnum getBlockEnum() {
        return BlocksEnum.FrameBox;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {
        return new FrameBoxTileEntity();
    }


//    @Override
//    public IIcon getIcon(int side, int meta) {
//        return super.getIcon(side, meta);
//    }

}
