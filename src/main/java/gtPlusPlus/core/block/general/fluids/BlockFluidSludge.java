package gtPlusPlus.core.block.general.fluids;

import static gregtech.api.enums.Mods.GTPlusPlus;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gtPlusPlus.core.creative.AddToCreativeTab;

public class BlockFluidSludge extends BlockFluidClassic {

    @SideOnly(Side.CLIENT)
    protected IIcon stillIcon;

    @SideOnly(Side.CLIENT)
    protected IIcon flowingIcon;

    public BlockFluidSludge(final Fluid fluid, final Material material) {
        super(fluid, material);
        this.setCreativeTab(AddToCreativeTab.tabMisc);
    }

    @Override
    public IIcon getIcon(final int ordinalSide, final int meta) {
        return ((ordinalSide == 0) || (ordinalSide == 1)) ? this.stillIcon : this.flowingIcon;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(final IIconRegister register) {
        this.stillIcon = register.registerIcon(GTPlusPlus.ID + ":fluids/fluid.jackdaniels");
        this.flowingIcon = register.registerIcon(GTPlusPlus.ID + ":fluids/fluid.jackdaniels");
    }

    @Override
    public boolean canDisplace(final IBlockAccess world, final int x, final int y, final int z) {
        if (world.getBlock(x, y, z).getMaterial().isLiquid()) {
            return false;
        }
        return super.canDisplace(world, x, y, z);
    }

    @Override
    public boolean displaceIfPossible(final World world, final int x, final int y, final int z) {
        if (world.getBlock(x, y, z).getMaterial().isLiquid()) {
            return false;
        }
        return super.displaceIfPossible(world, x, y, z);
    }

    @Override
    public boolean canCreatureSpawn(final EnumCreatureType type, final IBlockAccess world, final int x, final int y,
            final int z) {
        return false;
    }
}
