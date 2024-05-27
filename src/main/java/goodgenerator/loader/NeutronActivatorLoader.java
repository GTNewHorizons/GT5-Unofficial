package goodgenerator.loader;

import static goodgenerator.items.MyMaterial.plutoniumBasedLiquidFuel;
import static goodgenerator.items.MyMaterial.plutoniumBasedLiquidFuelExcited;
import static goodgenerator.items.MyMaterial.thoriumBasedLiquidFuelDepleted;
import static goodgenerator.items.MyMaterial.thoriumBasedLiquidFuelExcited;
import static goodgenerator.items.MyMaterial.uraniumBasedLiquidFuel;
import static goodgenerator.items.MyMaterial.uraniumBasedLiquidFuelExcited;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import goodgenerator.util.MyRecipeAdder;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;

public class NeutronActivatorLoader {

    public static void NARecipeLoad() {
        MyRecipeAdder.instance.addNeutronActivatorRecipe(
            new FluidStack[] { thoriumBasedLiquidFuelExcited.getFluidOrGas(200) },
            null,
            new FluidStack[] { thoriumBasedLiquidFuelDepleted.getFluidOrGas(200) },
            null,
            10000,
            700,
            500);

        MyRecipeAdder.instance.addNeutronActivatorRecipe(
            new FluidStack[] { uraniumBasedLiquidFuel.getFluidOrGas(100) },
            new ItemStack[] {
                GT_Utility.copyAmount(0, GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Tungsten, 1)) },
            new FluidStack[] { uraniumBasedLiquidFuelExcited.getFluidOrGas(100) },
            null,
            80,
            550,
            450);

        MyRecipeAdder.instance.addNeutronActivatorRecipe(
            new FluidStack[] { plutoniumBasedLiquidFuel.getFluidOrGas(100) },
            new ItemStack[] {
                GT_Utility.copyAmount(0, GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Tritanium, 1)) },
            new FluidStack[] { plutoniumBasedLiquidFuelExcited.getFluidOrGas(100) },
            null,
            80,
            600,
            500);
    }
}
