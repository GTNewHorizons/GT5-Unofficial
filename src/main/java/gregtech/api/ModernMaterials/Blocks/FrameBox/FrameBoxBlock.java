package gregtech.api.ModernMaterials.Blocks.FrameBox;

import gregtech.api.ModernMaterials.Blocks.BlocksEnum;
import gregtech.api.ModernMaterials.Blocks.DumbBase.Base.BaseBlock;
import gregtech.api.ModernMaterials.Blocks.DumbBase.Special.SpecialBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class FrameBoxBlock extends SpecialBlock {

    @Override
    public BlocksEnum getBlockEnum() {
        return BlocksEnum.FrameBox;
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata)
    {
        return new FrameBoxTileEntity();
    }

    // Make block see through.
    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public int getRenderBlockPass() {
        return 0;
    }

    @Override
    public int getRenderType() {
        return FrameBoxSimpleBlockRenderer.renderID;
    }

    // Yes I know this is deprecated but if you don't override it then walia won't get the right block
    // and all rendering will fail causing big errors.
//    @Override
//    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z)
//    {
//        return getDroppedItemStack(world, x, y, z);
//    }
}
