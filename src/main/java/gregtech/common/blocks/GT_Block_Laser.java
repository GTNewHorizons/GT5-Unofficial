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

    public TileLaser laserRender;
    public static IIcon[] textures;
    private static int renderID;

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

    public static void setRenderID(int id) {
        renderID = id;
    }

    public static int getRenderID() {
        return renderID;
    }

    @Override
    public int getRenderType() {
        return renderID;
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
    public TileEntity createNewTileEntity(World world, int meta) {
        laserRender = new TileLaser();
        return laserRender;
    }
}
