package gtPlusPlus.xmod.thermalfoundation.recipe;

import static gregtech.api.recipe.RecipeMaps.chemicalBathRecipes;
import static gregtech.api.recipe.RecipeMaps.mixerRecipes;
import static gregtech.api.recipe.RecipeMaps.vacuumFreezerRecipes;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;

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
import gregtech.api.util.GTOreDictUnificator;
import gtPlusPlus.xmod.thermalfoundation.fluid.TFFluids;

public class TFGregtechRecipes {

    public static void run() {
        start();
    }

    private static void start() {

        // Gelid Cryotheum
        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.ore, Materials.Cinnabar, 1L))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Cinnabar, Materials2Shapes.shapeDust, (int) (3)))
            .fluidInputs(new FluidStack(TFFluids.fluidCryotheum, 1 * INGOTS))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalBathRecipes);

        // Blizz Powder
        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.blaze_powder, 4))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Blizz, Materials2Shapes.shapeDust, (int) (4)))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.LiquidNitrogen,
                    Materials2FluidShapes.shapeFluidGas,
                    (int) (100)))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_HV / 2)
            .addTo(chemicalBathRecipes);

        // Blizz Rod
        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.blaze_rod))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.stick, Materials.Blizz, 1L))
            .duration(((int) Math.max((Materials.Blaze.getMass()) * 3L, 1L)) * TICKS)
            .eut(TierEU.RECIPE_MV)
            .addTo(vacuumFreezerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Coal, Materials2Shapes.shapeDust, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Sulfur, Materials2Shapes.shapeDust, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Redstone, Materials2Shapes.shapeDust, (int) (1)),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Blaze, 1L))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Pyrotheum, Materials2Shapes.shapeDust, (int) (1)))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Saltpeter, Materials2Shapes.shapeDust, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Snow, Materials2Shapes.shapeDust, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Lapis, Materials2Shapes.shapeDust, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Blizz, Materials2Shapes.shapeDust, (int) (1)))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Cryotheum, Materials2Shapes.shapeDust, (int) (1)))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Niter, Materials2Shapes.shapeDust, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Snow, Materials2Shapes.shapeDust, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Lapis, Materials2Shapes.shapeDust, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Blizz, Materials2Shapes.shapeDust, (int) (1)))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Cryotheum, Materials2Shapes.shapeDust, (int) (1)))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(mixerRecipes);
    }
}
