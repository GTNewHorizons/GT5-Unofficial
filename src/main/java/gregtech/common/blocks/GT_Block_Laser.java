package gregtech.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.GregTech_API;
import gregtech.api.enums.Textures;
import gregtech.common.tileentities.render.TileLaser;

public class GT_Block_Laser extends Block implements ITileEntityProvider {

    public static IIcon[] textures;

    public GT_Block_Laser() {
        super(Material.iron);
        setBlockName("Heat Resistant Laser Receiver Casing");
        this.setCreativeTab(GregTech_API.TAB_GREGTECH);
        setHarvestLevel("pickaxe", 2);
        GameRegistry.registerBlock(this, getUnlocalizedName());
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        blockIcon = Textures.BlockIcons.MACHINE_CASING_SOLID_STEEL.getIcon();
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        return blockIcon;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public float getBlockHardness(World world, int x, int y, int z) {
        return 1.0f;
    }

    @Override
    public int damageDropped(int meta) {
        return meta;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean hasTileEntity(int metadata) {
        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileLaser();
    }
}
