package common.blocks;

import common.tileentities.TE_ItemProxyCable;
import common.tileentities.TE_SpaceElevatorTether;
import cpw.mods.fml.common.registry.GameRegistry;
import kekztech.KekzCore;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class Block_SpaceElevatorTether extends BaseGTUpdateableBlock {

    private static final Block_SpaceElevatorTether INSTANCE = new Block_SpaceElevatorTether();

    private IIcon top;
    private IIcon side;

    private Block_SpaceElevatorTether() {
        super(Material.glass);
    }

    public static Block registerBlock() {
        final String blockName = "kekztech_spaceelevatortether_block";
        INSTANCE.setBlockName(blockName);
        INSTANCE.setCreativeTab(CreativeTabs.tabMisc);
        INSTANCE.setHardness(15.0f);
        INSTANCE.setResistance(15.0f);
        GameRegistry.registerBlock(INSTANCE, blockName);

        return INSTANCE;
    }

    @Override
    public void registerBlockIcons(IIconRegister ir) {
        top = ir.registerIcon("kekztech:Tether_top");
        side = ir.registerIcon("kekztech:Tether_side");
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        return (side < 2) ? this.top : this.side;
    }

    @Override
    public TileEntity createTileEntity(World world, int p_149915_2_) {
        return new TE_SpaceElevatorTether();
    }

    @Override
    public boolean hasTileEntity(int metadata) {
        return true;
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }

}
