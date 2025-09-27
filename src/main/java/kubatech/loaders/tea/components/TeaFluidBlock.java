package kubatech.loaders.tea.components;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TeaFluidBlock extends BlockFluidClassic {

    IIcon stillIcon;
    IIcon flowingIcon;

    public TeaFluidBlock(Fluid fluid) {
        super(fluid, Material.water);
    }

    @Override
    public String getLocalizedName() {
        return getFluid().getLocalizedName();
    }

    @Override
    public String getUnlocalizedName() {
        return getFluid().getUnlocalizedName();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(IIconRegister reg) {
        stillIcon = reg.registerIcon(
            "kubatech:tea/" + this.getFluid()
                .getName());
        flowingIcon = reg.registerIcon(
            "kubatech:tea/" + this.getFluid()
                .getName() + "_flowing");

        getFluid().setIcons(stillIcon, flowingIcon);
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        return (side == 0 || side == 1) ? stillIcon : flowingIcon;
    }
}
