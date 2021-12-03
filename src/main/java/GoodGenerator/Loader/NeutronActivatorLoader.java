package GoodGenerator.Loader;

import GoodGenerator.util.MyRecipeAdder;
import com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader;
import gregtech.api.enums.OrePrefixes;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import static GoodGenerator.Items.MyMaterial.*;

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
