package gregtech.api.ModernMaterials.Blocks.FrameBox;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.ModernMaterials.Blocks.BlocksEnum;
import gregtech.api.ModernMaterials.Blocks.DumbBase.DumbBlock;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
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

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister reg) {
        this.blockIcon = reg.registerIcon("gregtech:ModernMaterialsIcons/Blocks/frameGt");
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
    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z)
    {
        FrameBoxTileEntity frameBoxTile = (FrameBoxTileEntity) world.getTileEntity(x, y, z);
        return new ItemStack(Item.getItemFromBlock(this), 1, frameBoxTile.getMaterialID());
    }
}
