package gregtech.common.hydrodam;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidBlock;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/// The water inside a dam. Its metadata is laid out by {@link DamWaterMeta}, and its contents are managed by
/// {@link DamVolumes}. It never ticks or flows.
public class BlockDamWater extends Block implements IFluidBlock {

    /// Set by {@link DamWaterRenderer} when it is registered on the client. Kept here so that servers never load the
    /// renderer.
    static int renderId = -1;

    public BlockDamWater() {
        super(Material.water);
        setBlockName("gt.damwater");
        setHardness(100.0F);
        setLightOpacity(DamWaterHeights.FULL_LIGHT_OPACITY);
        disableStats();
        GameRegistry.registerBlock(this, getUnlocalizedName());
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
        if (!world.isRemote) {
            DamVolumes.get(world)
                .onCellRemoved(x, y, z, meta);
        }

        super.breakBlock(world, x, y, z, block, meta);
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block neighbour) {
        if (!world.isRemote) {
            DamVolumes.get(world)
                .queueNeighbourCheck(x, y, z);
        }
    }

    @Override
    public Fluid getFluid() {
        return FluidRegistry.WATER;
    }

    @Override
    public FluidStack drain(World world, int x, int y, int z, boolean doDrain) {
        return null;
    }

    @Override
    public boolean canDrain(World world, int x, int y, int z) {
        return false;
    }

    /// Clients use this for the underwater overlay, so it follows the drawn surface there. Servers use the real fill.
    @Override
    public float getFilledPercentage(World world, int x, int y, int z) {
        float fill = DamWaterHeights.getFill(world, x, y, z);

        if (Float.isNaN(fill)) return 1;

        return world.isRemote ? DamWaterHeights.getViewUnderwaterHeight(fill) : fill;
    }

    /// Partly filled blocks let more light through. {@link DamLighting} relights them when their opacity changes.
    @Override
    public int getLightOpacity(IBlockAccess world, int x, int y, int z) {
        if (!(world instanceof World realWorld)) return DamWaterHeights.FULL_LIGHT_OPACITY;

        float fill = DamWaterHeights.getFill(realWorld, x, y, z);

        return Float.isNaN(fill) ? DamWaterHeights.FULL_LIGHT_OPACITY : DamWaterHeights.getLightOpacity(fill);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
        return null;
    }

    @Override
    public boolean canCollideCheck(int meta, boolean hitIfLiquid) {
        return false;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public int getRenderBlockPass() {
        return 1;
    }

    @Override
    public int getRenderType() {
        return renderId;
    }

    /// Like vanilla liquids: faces next to any water are hidden, and the top face is otherwise always shown.
    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side) {
        if (world.getBlock(x, y, z)
            .getMaterial() == Material.water) return false;

        return side == 1 || super.shouldSideBeRendered(world, x, y, z, side);
    }

    /// The average water colour of the surrounding biomes, like vanilla water.
    @Override
    @SideOnly(Side.CLIENT)
    public int colorMultiplier(IBlockAccess world, int x, int y, int z) {
        int red = 0;
        int green = 0;
        int blue = 0;

        for (int dz = -1; dz <= 1; dz++) {
            for (int dx = -1; dx <= 1; dx++) {
                int colour = world.getBiomeGenForCoords(x + dx, z + dz)
                    .getWaterColorMultiplier();

                red += (colour & 0xFF0000) >> 16;
                green += (colour & 0x00FF00) >> 8;
                blue += colour & 0x0000FF;
            }
        }

        return (red / 9 & 255) << 16 | (green / 9 & 255) << 8 | blue / 9 & 255;
    }

    /// The brighter of this block and the block above it, like vanilla liquids.
    @Override
    @SideOnly(Side.CLIENT)
    public int getMixedBrightnessForBlock(IBlockAccess world, int x, int y, int z) {
        int here = world.getLightBrightnessForSkyBlocks(x, y, z, 0);
        int above = world.getLightBrightnessForSkyBlocks(x, y + 1, z, 0);

        int blockLight = Math.max(here & 0xFF, above & 0xFF);
        int skyLight = Math.max(here >> 16 & 0xFF, above >> 16 & 0xFF);

        return blockLight | skyLight << 16;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister register) {}

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        return Blocks.water.getIcon(side, 0);
    }

    @Override
    public Item getItemDropped(int meta, Random random, int fortune) {
        return null;
    }

    @Override
    public int quantityDropped(Random random) {
        return 0;
    }
}
