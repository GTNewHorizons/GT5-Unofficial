package goodgenerator.blocks.myFluids;

import static goodgenerator.main.GoodGenerator.MOD_ID;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public class BaseFluid extends BlockFluidClassic {
    @SideOnly(Side.CLIENT)
    protected IIcon stillIcon;

    @SideOnly(Side.CLIENT)
    protected IIcon flowingIcon;

    private String stillTexture;
    private String flowingTexture;

    public BaseFluid(Fluid fluid, Material material) {
        super(fluid, material);
    }

    public void SetTexture(String fluidName) {
        stillTexture = MOD_ID + ":fluids/" + fluidName + ".still";
        flowingTexture = MOD_ID + ":fluids/" + fluidName + ".flowing";
    }

    public static Fluid BuildFluid(String fluidName) {
        Fluid tFluid = new Fluid(fluidName);
        FluidRegistry.registerFluid(tFluid);
        return tFluid;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(IIconRegister register) {
        stillIcon = register.registerIcon(stillTexture);
        flowingIcon = register.registerIcon(flowingTexture);

        super.getFluid().setIcons(stillIcon, flowingIcon);
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        return (side == 0 || side == 1) ? stillIcon : flowingIcon;
    }
}
