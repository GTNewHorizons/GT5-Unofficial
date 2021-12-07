package goodgenerator.loader;

import goodgenerator.util.MyRecipeAdder;
import net.minecraftforge.fluids.FluidStack;

import static goodgenerator.items.MyMaterial.*;

public class NeutronActivatorLoader {
    public static void NARecipeLoad() {
        MyRecipeAdder.instance.addNeutronActivatorRecipe(
                new FluidStack[]{
                        thoriumBasedLiquidFuelExcited.getFluidOrGas(200)
                },
                null,
                new FluidStack[]{
                        thoriumBasedLiquidFuelDepleted.getFluidOrGas(200)
                },
                null,
                20000,
                700,
                500
        );
    }
}
