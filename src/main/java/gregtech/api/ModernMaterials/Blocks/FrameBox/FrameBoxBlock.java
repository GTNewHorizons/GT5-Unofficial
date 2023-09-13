package gregtech.api.ModernMaterials.Blocks.FrameBox;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.ModernMaterials.Blocks.BlocksEnum;
import gregtech.api.ModernMaterials.Blocks.DumbBase.Simple.DumbBlock;
import gregtech.api.ModernMaterials.ModernMaterialsTextureRegister;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class FrameBoxBlock extends DumbBlock {

    @Override
    public BlocksEnum getBlockEnum() {
        return BlocksEnum.FrameBox;
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata)
    {
        return new FrameBoxTileEntity();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister reg) {
        this.blockIcon = reg.registerIcon("gregtech:ModernMaterialsIcons/Blocks/frameGt");
    }


    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIcon(IBlockAccess worldIn, int x, int y, int z, int side)
    {
        this.blockIcon = ModernMaterialsTextureRegister.frameGT;
        return ModernMaterialsTextureRegister.frameGT;
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta)
    {
        this.blockIcon = ModernMaterialsTextureRegister.frameGT;
        return ModernMaterialsTextureRegister.frameGT;
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
        return getDroppedItemStack(world, x, y, z);
    }
}
