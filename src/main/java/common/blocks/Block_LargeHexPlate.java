package common.blocks;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

public class Block_LargeHexPlate extends Block {

    private static final Block_LargeHexPlate INSTANCE = new Block_LargeHexPlate();
    private static final int BATCH_SIZE = 4;

    private final IIcon[][] parts = new IIcon[BATCH_SIZE][BATCH_SIZE];

    private Block_LargeHexPlate() {
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
        for(int x = 0; x < BATCH_SIZE; x++) {
            for(int z = 0; z < BATCH_SIZE; z++) {
                parts[x][z] = ir.registerIcon("kekztech:LargeHexTile_" + x + "_" + z);
            }
        }
    }

    @Override
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
        final int xMod = x >= 0 ? Math.abs(x % BATCH_SIZE) : Math.abs((Math.abs(x) % BATCH_SIZE) - BATCH_SIZE) % BATCH_SIZE;
        final int yMod = y >= 0 ? Math.abs(y % BATCH_SIZE) : Math.abs((Math.abs(y) % BATCH_SIZE) - BATCH_SIZE) % BATCH_SIZE;
        final int zMod = z >= 0 ? Math.abs(z % BATCH_SIZE) : Math.abs((Math.abs(z) % BATCH_SIZE) - BATCH_SIZE) % BATCH_SIZE;

        if(side == 0 || side == 1) {
            return parts[xMod][zMod];
        } else if(side == 2 || side == 3) {
            return parts[xMod][yMod];
        } else {
            return parts[zMod][yMod];
        }
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        return parts[(int) Math.ceil(BATCH_SIZE / 2)][(int) Math.ceil(BATCH_SIZE / 2)];
    }
}
