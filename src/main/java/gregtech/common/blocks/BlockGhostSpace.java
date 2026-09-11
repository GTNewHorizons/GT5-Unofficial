package gregtech.common.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;

/**
 * Invisible block that reserves extra space for a machine whose model is taller than one block. Any MTE can use
 * the shared instance ({@code GregTechAPI.sBlockGhostSpace}): on placement, place it in the extra space (and forward
 * clicks to the real block); when either this block or the GT machine directly below it is destroyed by any means,
 * the other is destroyed too.
 */
public class BlockGhostSpace extends Block {

    public BlockGhostSpace() {
        super(new MaterialMachines());
        setBlockName("gt.ghostspace");
        setHardness(1.0F);
        setResistance(10.0F);
        setStepSound(soundTypeMetal);
        setCreativeTab(null);
        GameRegistry.registerBlock(this, getUnlocalizedName());
    }

    @Override
    public boolean isReplaceable(IBlockAccess world, int x, int y, int z) {
        return false;
    }

    @Override
    public int quantityDropped(Random random) {
        return 0;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public int getRenderType() {
        return -1;
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        return Blocks.iron_block.getIcon(side, meta);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX,
        float hitY, float hitZ) {
        return world.getBlock(x, y - 1, z)
            .onBlockActivated(world, x, y - 1, z, player, side, hitX, hitY, hitZ);
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
        super.breakBlock(world, x, y, z, block, meta);
        if (isGTMachine(world, x, y - 1, z)) {
            world.func_147480_a(x, y - 1, z, true);
        }
    }

    private static boolean isGTMachine(World world, int x, int y, int z) {
        final TileEntity te = world.getTileEntity(x, y, z);
        return te instanceof IGregTechTileEntity gtte && gtte.getMetaTileEntity() != null;
    }
}
