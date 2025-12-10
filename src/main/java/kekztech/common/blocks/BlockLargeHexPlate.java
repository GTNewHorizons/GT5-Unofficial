package kekztech.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

import cpw.mods.fml.common.registry.GameRegistry;

public final class BlockLargeHexPlate extends Block {

    private static final BlockLargeHexPlate INSTANCE = new BlockLargeHexPlate();
    private static final int BATCH_SIZE = 4;

    private final IIcon[] icons = new IIcon[2];

    private BlockLargeHexPlate() {
        super(Material.rock);
    }

    public static Block registerBlock() {
        final String blockName = "kekztech_largehextile_block";
        INSTANCE.setBlockName(blockName);
        INSTANCE.setCreativeTab(CreativeTabs.tabMisc);
        INSTANCE.setHardness(6.0f);
        INSTANCE.setResistance(10.0f);
        GameRegistry.registerBlock(INSTANCE, blockName);
        return INSTANCE;
    }

    @Override
    public void registerBlockIcons(IIconRegister ir) {
        icons[0] = ir.registerIcon("kekztech:LargeHexTile_0");
        icons[1] = ir.registerIcon("kekztech:LargeHexTile_1");
    }

    private int pick(int a, int b) {
        return (a + b) & 1; // % 2
    }

    private int wrap(int v) {
        return Math.floorMod(v, BATCH_SIZE);
    }

    @Override
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
        int xm = wrap(x);
        int ym = wrap(y);
        int zm = wrap(z);

        if (side == 0 || side == 1) {
            return icons[pick(xm, zm)];
        } else if (side == 2 || side == 3) {
            return icons[pick(xm, ym)];
        } else {
            return icons[pick(zm, ym)];
        }
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        return icons[0];
    }
}
