package kubatech.loaders.tea.components;

import net.minecraftforge.fluids.Fluid;

public class TeaFluid extends Fluid {

    public TeaFluid(String fluidName) {
        super("tea_" + fluidName);
        setUnlocalizedName("kubatech.tea." + fluidName);
    }

    @Override
    public String getUnlocalizedName() {
        return this.unlocalizedName;
    }
}
