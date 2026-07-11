package gtPlusPlus.recipes;

import static gregtech.api.recipe.RecipeMaps.pyrolyseRecipes;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.cokeOvenRecipes;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials2.Materials2FluidShapes;
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.enums.materials2.Materials2Shapes;
import gregtech.api.objects.OreDictItemStack;
import gregtech.api.util.GTOreDictUnificator;
import gtPlusPlus.core.fluids.GTPPFluids;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;

public class CokeAndPyrolyseOven {

    public static void postInit() {
        // Wood to Charcoal
        // Try use all woods found
        GTValues.RA.stdBuilder()
            .itemInputs(new OreDictItemStack("logWood", 20))
            .circuit(20)
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.gem, Materials.Charcoal, 24))
            .fluidInputs(Materials.Steam.getGas(1_000))
            .fluidOutputs(new FluidStack(GTPPFluids.CoalGas, 1440))
            .duration(72 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(pyrolyseRecipes, cokeOvenRecipes);

        // Coal to Coke
        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.gem, Materials.Coal, 16))
            .circuit(22)
            .itemOutputs(GTOreDictUnificator.get("fuelCoke", 10))
            .fluidInputs(Materials.Steam.getGas(1_000))
            .fluidOutputs(new FluidStack(GTPPFluids.CoalGas, 20 * INGOTS))
            .duration(36 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(pyrolyseRecipes, cokeOvenRecipes);

        // Cactus and Sugar charcoal/coke, copied over from the ICO and adjusted for pyrolyse (*20 input and output,
        // duration*1.25 per item.)
        addCokingRecipes(
            new ItemStack(Blocks.cactus, 20),
            GregtechItemList.CactusCharcoal.get(20),
            GregtechItemList.CactusCoke.get(20));
        addCokingRecipes(
            new ItemStack(Items.reeds, 20),
            GregtechItemList.SugarCharcoal.get(20),
            GregtechItemList.SugarCoke.get(20));

        // Coke & Coal
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.gem, Materials.Coal, 12),
                GTOreDictUnificator.get("fuelCoke", 6))
            .itemOutputs(GTOreDictUnificator.get("fuelCoke", 14))
            .fluidInputs(Materials.Steam.getGas(2_000))
            .fluidOutputs(new FluidStack(GTPPFluids.CoalGas, 5040))
            .eut(TierEU.RECIPE_HV / 2)
            .duration(1 * MINUTES)
            .addTo(cokeOvenRecipes);
    }

    private static void addCokingRecipes(ItemStack plant, ItemStack charcoal, ItemStack coke) {
        // Plant to Charcoal
        GTValues.RA.stdBuilder()
            .itemInputs(plant)
            .circuit(3)
            .itemOutputs(charcoal)
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Creosote, Materials2FluidShapes.shapeFluidLiquid, (int) (2_000)))
            .eut(TierEU.RECIPE_LV / 2)
            .duration(25 * SECONDS)
            .addTo(pyrolyseRecipes, cokeOvenRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(plant)
            .circuit(4)
            .itemOutputs(charcoal)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Nitrogen, Materials2FluidShapes.shapeFluidGas, (int) (2_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.CharcoalByproducts,
                    Materials2FluidShapes.shapeFluidGas,
                    (int) (4_000)))
            .eut(TierEU.RECIPE_LV / 2)
            .duration(250 * TICKS)
            .addTo(pyrolyseRecipes, cokeOvenRecipes);

        // Charcoal to Coke
        GTValues.RA.stdBuilder()
            .itemInputs(charcoal)
            .circuit(3)
            .itemOutputs(coke)
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Creosote, Materials2FluidShapes.shapeFluidLiquid, (int) (4_000)))
            .eut(TierEU.RECIPE_LV / 2)
            .duration(50 * SECONDS)
            .addTo(pyrolyseRecipes, cokeOvenRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(charcoal)
            .circuit(4)
            .itemOutputs(coke)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Nitrogen, Materials2FluidShapes.shapeFluidGas, (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.CharcoalByproducts,
                    Materials2FluidShapes.shapeFluidGas,
                    (int) (2_000)))
            .eut(TierEU.RECIPE_LV / 2)
            .duration(25 * SECONDS)
            .addTo(pyrolyseRecipes, cokeOvenRecipes);

        // Coke to Wood tar/Wood gas
        GTValues.RA.stdBuilder()
            .itemInputs(coke)
            .circuit(5)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Ash, Materials2Shapes.shapeDust, (int) (5)))
            .fluidInputs(Materials.Steam.getGas(2_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.WoodTar, Materials2FluidShapes.shapeFluidLiquid, (int) (4_000)))
            .eut(TierEU.RECIPE_HV / 2)
            .duration(75 * SECONDS)
            .addTo(pyrolyseRecipes, cokeOvenRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(coke)
            .circuit(6)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Ash, Materials2Shapes.shapeDust, (int) (5)))
            .fluidInputs(Materials.Steam.getGas(2_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.WoodGas, Materials2FluidShapes.shapeFluidGas, (int) (6_000)))
            .eut(TierEU.RECIPE_HV / 2)
            .duration(75 * SECONDS)
            .addTo(pyrolyseRecipes, cokeOvenRecipes);
    }
}
