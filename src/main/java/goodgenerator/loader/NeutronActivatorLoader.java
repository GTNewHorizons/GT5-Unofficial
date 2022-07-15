package goodgenerator.loader;

import static goodgenerator.items.MyMaterial.*;

import goodgenerator.util.MyRecipeAdder;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class NeutronActivatorLoader {
    public static void NARecipeLoad() {
        MyRecipeAdder.instance.addNeutronActivatorRecipe(
                new FluidStack[] {thoriumBasedLiquidFuelExcited.getFluidOrGas(200)},
                null,
                new FluidStack[] {thoriumBasedLiquidFuelDepleted.getFluidOrGas(200)},
                null,
                10000,
                700,
                500);

        MyRecipeAdder.instance.addNeutronActivatorRecipe(
                new FluidStack[] {uraniumBasedLiquidFuel.getFluidOrGas(100)},
                new ItemStack[] {
                    GT_Utility.copyAmount(0, GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Tungsten, 1))
                },
                new FluidStack[] {uraniumBasedLiquidFuelExcited.getFluidOrGas(100)},
                null,
                80,
                550,
                450);

        MyRecipeAdder.instance.addNeutronActivatorRecipe(
                new FluidStack[] {plutoniumBasedLiquidFuel.getFluidOrGas(100)},
                new ItemStack[] {
                    GT_Utility.copyAmount(0, GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Tritanium, 1))
                },
                new FluidStack[] {plutoniumBasedLiquidFuelExcited.getFluidOrGas(100)},
                null,
                80,
                600,
                500);
    }
}
