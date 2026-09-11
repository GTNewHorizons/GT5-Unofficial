package gregtech.common.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.enums.HarvestTool;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.interfaces.tileentity.IGregtechWailaProvider;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

/**
 * Invisible block that reserves extra space for a machine whose model is taller than one block. Any MTE can use
 * the shared instance ({@code GregTechAPI.sBlockGhostSpace}): on placement, place it in the extra space and forward
 * clicks to the real block. When either this block or the GT machine directly below it is destroyed by any means,
 * the other is destroyed too. Has no identity of its own - WAILA icon/name are derived live from whatever block is
 * below it.
 */
public class BlockGhostSpace extends Block implements IGregtechWailaProvider {

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
    public String getHarvestTool(int meta) {
        return HarvestTool.PickaxeLevel0.getHarvestTool();
    }

    @Override
    public int getHarvestLevel(int meta) {
        return HarvestTool.PickaxeLevel0.getHarvestLevel();
    }

    @Override
    public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config) {
        return getParentPickBlock(accessor.getWorld(), accessor.getPosition());
    }

    /** Whatever block sits directly below this one - used to show WAILA icon/name. */
    public static ItemStack getParentPickBlock(World world, MovingObjectPosition pos) {
        final int x = pos.blockX;
        final int y = pos.blockY - 1;
        final int z = pos.blockZ;
        return world.getBlock(x, y, z)
            .getPickBlock(null, world, x, y, z);
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
        final int mx = x, my = y - 1, mz = z;
        if (!isGTMachine(world, mx, my, mz)) return;

        // Drop and remove the machine silently
        final Block machineBlock = world.getBlock(mx, my, mz);
        final int machineMeta = world.getBlockMetadata(mx, my, mz);
        for (ItemStack drop : machineBlock.getDrops(world, mx, my, mz, machineMeta, 0)) {
            world.spawnEntityInWorld(new EntityItem(world, mx + 0.5, my + 0.5, mz + 0.5, drop));
        }
        world.setBlockToAir(mx, my, mz);
    }

    private static boolean isGTMachine(World world, int x, int y, int z) {
        final TileEntity te = world.getTileEntity(x, y, z);
        return te instanceof IGregTechTileEntity gtte && gtte.getMetaTileEntity() != null;
    }
}
