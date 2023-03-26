package gregtech.api.ModernMaterials.Blocks.FrameBox;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.ModernMaterials.Blocks.BlocksEnum;
import gregtech.api.ModernMaterials.Blocks.DumbBase.DumbBlock;
import gregtech.api.ModernMaterials.Blocks.DumbBase.DumbTileEntity;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import static gregtech.api.enums.GT_Values.RES_PATH_BLOCK;

public class FrameBoxBlock extends DumbBlock {

    @Override
    public BlocksEnum getBlockEnum() {
        return BlocksEnum.FrameBox;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {
        return new FrameBoxTileEntity();
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister reg)
    {
        this.blockIcon = reg.registerIcon(RES_PATH_BLOCK + "bottom");
    }

    @Override
    public int getRenderType() {
        return FrameBoxRenderer.renderID;
    }

    @Override
    public boolean isOpaqueCube() {
        return true;
    }

}
