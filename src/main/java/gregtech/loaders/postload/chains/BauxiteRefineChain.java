package gregtech.loaders.postload.chains;

import static gregtech.api.recipe.RecipeMaps.centrifugeRecipes;
import static gregtech.api.recipe.RecipeMaps.chemicalBathRecipes;
import static gregtech.api.recipe.RecipeMaps.fluidHeaterRecipes;
import static gregtech.api.recipe.RecipeMaps.mixerRecipes;
import static gregtech.api.recipe.RecipeMaps.multiblockChemicalReactorRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;

import net.minecraftforge.fluids.FluidStack;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials.FluidShapes;
import gregtech.api.enums.materials.Materials;
import gregtech.api.enums.materials.Shapes;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;

public class BauxiteRefineChain {

    public static void run() {

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Bauxite, Shapes.crushed, 32),
                MaterialLibAPI.getStack(Materials.SodiumHydroxideGT5U, Shapes.dust, 9),
                MaterialLibAPI.getStack(Materials.Quicklime, Shapes.dust, 4))
            .circuit(8)
            .fluidInputs(GTUtility.getWater(5_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.BauxiteSlurry, FluidShapes.fluidLiquid, 8_000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Bauxite, Shapes.crushedPurified, 32),
                MaterialLibAPI.getStack(Materials.SodiumHydroxideGT5U, Shapes.dust, 9),
                MaterialLibAPI.getStack(Materials.Quicklime, Shapes.dust, 4))
            .circuit(8)
            .fluidInputs(GTUtility.getWater(5_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.BauxiteSlurry, FluidShapes.fluidLiquid, 8_000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.BauxiteSlurry, FluidShapes.fluidLiquid, 2_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.HeadedBauxiteSlurry, FluidShapes.fluidLiquid, 2_000))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(fluidHeaterRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.AluminiumHydroxide, Shapes.dust, 1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Alumina, Shapes.dust, 64),
                MaterialLibAPI.getStack(Materials.Alumina, Shapes.dust, 16),
                MaterialLibAPI.getStack(Materials.SodiumCarbonate, Shapes.dust, 9),
                MaterialLibAPI.getStack(Materials.Calcite, Shapes.dust, 10),
                MaterialLibAPI.getStack(Materials.BauxiteSlag, Shapes.dust, 16))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.CarbonDioxide, FluidShapes.fluidGas, 5_000),
                MaterialLibAPI.getFluidStack(Materials.HeadedBauxiteSlurry, FluidShapes.fluidLiquid, 8_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.SluiceJuice, FluidShapes.fluidLiquid, 5_000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.BauxiteSlag, Shapes.dust, 1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Rutile, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Gallium, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Quicklime, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.SiliconDioxide, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Iron, Shapes.dust, 1))
            .outputChances(10000, 3000, 2000, 9000, 8000)
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Ilmenite, Shapes.crushedPurified, 1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Rutile, Shapes.dust, 2),
                MaterialLibAPI.getStack(Materials.IlmeniteSlag, Shapes.dust, 1))
            .outputChances(10000, 3000)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.SulfuricAcid, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(new FluidStack(ItemList.sGreenVitriol, 2_000))
            .duration(21 * SECONDS)
            .eut(1000)
            .addTo(chemicalBathRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Ilmenite, Shapes.crushed, 1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Rutile, Shapes.dust, 2),
                MaterialLibAPI.getStack(Materials.IlmeniteSlag, Shapes.dust, 1))
            .outputChances(10000, 6000)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.SulfuricAcid, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(new FluidStack(ItemList.sGreenVitriol, 2_000))
            .duration(21 * SECONDS)
            .eut(1000)
            .addTo(chemicalBathRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.IlmeniteSlag, Shapes.dust, 1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Iron, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Niobium, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Tantalum, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Manganese, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Magnesium, Shapes.dust, 1))
            .outputChances(8000, 500, 2000, 5000, 6000)
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(centrifugeRecipes);

        OrePrefixes[] washable = new OrePrefixes[] { OrePrefixes.crushed, OrePrefixes.crushedPurified,
            OrePrefixes.dustImpure, OrePrefixes.dustPure };

        for (OrePrefixes ore : washable) {
            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTOreDictUnificator.get(ore, Materials.Sapphire, 1),
                    MaterialLibAPI.getStack(Materials.SodiumHydroxideGT5U, Shapes.dustTiny, 1))
                .circuit(1)
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(Materials.HydrochloricAcidGT5U, FluidShapes.fluidLiquid, 1_000))
                .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.SapphireJuice, FluidShapes.fluidLiquid, 1_000))
                .duration(2 * SECONDS)
                .eut(TierEU.RECIPE_MV)
                .addTo(mixerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTOreDictUnificator.get(ore, Materials.GreenSapphire, 1),
                    MaterialLibAPI.getStack(Materials.SodiumHydroxideGT5U, Shapes.dustTiny, 1))
                .circuit(1)
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(Materials.HydrochloricAcidGT5U, FluidShapes.fluidLiquid, 1_000))
                .fluidOutputs(
                    MaterialLibAPI.getFluidStack(Materials.GreenSapphireJuice, FluidShapes.fluidLiquid, 1_000))
                .duration(2 * SECONDS)
                .eut(TierEU.RECIPE_MV)
                .addTo(mixerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTOreDictUnificator.get(ore, Materials.Ruby, 1),
                    MaterialLibAPI.getStack(Materials.SodiumHydroxideGT5U, Shapes.dustTiny, 1))
                .circuit(1)
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(Materials.HydrochloricAcidGT5U, FluidShapes.fluidLiquid, 1_000))
                .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.RubyJuice, FluidShapes.fluidLiquid, 1_000))
                .duration(2 * SECONDS)
                .eut(TierEU.RECIPE_MV)
                .addTo(mixerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTOreDictUnificator.get(ore, Materials.Sapphire, 9),
                    MaterialLibAPI.getStack(Materials.SodiumHydroxideGT5U, Shapes.dust, 1))
                .circuit(9)
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(Materials.HydrochloricAcidGT5U, FluidShapes.fluidLiquid, 9_000))
                .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.SapphireJuice, FluidShapes.fluidLiquid, 9_000))
                .duration(3 * SECONDS)
                .eut(TierEU.RECIPE_HV)
                .addTo(mixerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTOreDictUnificator.get(ore, Materials.GreenSapphire, 9),
                    MaterialLibAPI.getStack(Materials.SodiumHydroxideGT5U, Shapes.dust, 1))
                .circuit(9)
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(Materials.HydrochloricAcidGT5U, FluidShapes.fluidLiquid, 9_000))
                .fluidOutputs(
                    MaterialLibAPI.getFluidStack(Materials.GreenSapphireJuice, FluidShapes.fluidLiquid, 9_000))
                .duration(3 * SECONDS)
                .eut(TierEU.RECIPE_HV)
                .addTo(mixerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTOreDictUnificator.get(ore, Materials.Ruby, 9),
                    MaterialLibAPI.getStack(Materials.SodiumHydroxideGT5U, Shapes.dust, 1))
                .circuit(9)
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(Materials.HydrochloricAcidGT5U, FluidShapes.fluidLiquid, 9_000))
                .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.RubyJuice, FluidShapes.fluidLiquid, 9_000))
                .duration(3 * SECONDS)
                .eut(TierEU.RECIPE_HV)
                .addTo(mixerRecipes);
        }

        GTValues.RA.stdBuilder()
            .circuit(1)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.AluminiumHydroxide, Shapes.dust, 2),
                MaterialLibAPI.getStack(Materials.Iron, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Vanadium, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Magnesium, Shapes.dust, 1))
            .outputChances(10000, 300, 200, 200)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.SapphireJuice, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.HydrochloricAcidGT5U, FluidShapes.fluidLiquid, 1_000))
            .duration(2 * SECONDS + 5 * TICKS)
            .eut(100)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .circuit(1)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.AluminiumHydroxide, Shapes.dust, 2),
                MaterialLibAPI.getStack(Materials.Iron, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Vanadium, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Manganese, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Beryllium, Shapes.dust, 1))
            .outputChances(10000, 300, 200, 200, 200)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.GreenSapphireJuice, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.HydrochloricAcidGT5U, FluidShapes.fluidLiquid, 1_000))
            .duration(2 * SECONDS + 5 * TICKS)
            .eut(100)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .circuit(1)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.AluminiumHydroxide, Shapes.dust, 2),
                MaterialLibAPI.getStack(Materials.Chrome, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Iron, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Vanadium, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Magnesium, Shapes.dust, 1))
            .outputChances(10000, 5000, 300, 200, 200)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.RubyJuice, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.HydrochloricAcidGT5U, FluidShapes.fluidLiquid, 1_000))
            .duration(2 * SECONDS + 5 * TICKS)
            .eut(100)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Pyrope, Shapes.dust, 1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Alumina, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Magnesia, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Silver, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Iron, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Calcite, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Vanadium, Shapes.dust, 1))
            .outputChances(5000, 4000, 300, 300, 300, 200)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.NitricAcid, FluidShapes.fluidLiquid, 10))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.SluiceJuice, FluidShapes.fluidLiquid, 10))
            .duration(2 * SECONDS + 5 * TICKS)
            .eut(TierEU.RECIPE_MV)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Almandine, Shapes.dust, 1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Alumina, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Iron, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Gold, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Calcite, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Chrome, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Vanadium, Shapes.dust, 1))
            .outputChances(5000, 4000, 300, 300, 200, 200)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.NitricAcid, FluidShapes.fluidLiquid, 10))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.SluiceJuice, FluidShapes.fluidLiquid, 10))
            .duration(2 * SECONDS + 5 * TICKS)
            .eut(TierEU.RECIPE_MV)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Spessartine, Shapes.dust, 1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Alumina, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Pyrolusite, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Iron, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Calcite, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Magnesium, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Tantalum, Shapes.dust, 1))
            .outputChances(5000, 4000, 300, 300, 300, 200)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.NitricAcid, FluidShapes.fluidLiquid, 10))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.SluiceJuice, FluidShapes.fluidLiquid, 10))
            .duration(2 * SECONDS + 5 * TICKS)
            .eut(TierEU.RECIPE_MV)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Andradite, Shapes.dust, 1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Quicklime, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Iron, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Alumina, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Gold, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Vanadium, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Rutile, Shapes.dust, 1))
            .outputChances(5000, 4000, 300, 300, 200, 600)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.NitricAcid, FluidShapes.fluidLiquid, 10))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.SluiceJuice, FluidShapes.fluidLiquid, 10))
            .duration(2 * SECONDS + 5 * TICKS)
            .eut(TierEU.RECIPE_MV)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Uvarovite, Shapes.dust, 1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Quicklime, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Chrome, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Iron, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Silver, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Alumina, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Manganese, Shapes.dust, 1))
            .outputChances(5000, 1000, 300, 300, 200, 200)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.NitricAcid, FluidShapes.fluidLiquid, 10))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.SluiceJuice, FluidShapes.fluidLiquid, 10))
            .duration(2 * SECONDS + 5 * TICKS)
            .eut(TierEU.RECIPE_MV)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Grossular, Shapes.dust, 1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Quicklime, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Alumina, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Iron, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Gold, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Calcite, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Vanadium, Shapes.dust, 1))
            .outputChances(5000, 4000, 300, 300, 300, 200)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.NitricAcid, FluidShapes.fluidLiquid, 10))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.SluiceJuice, FluidShapes.fluidLiquid, 10))
            .duration(2 * SECONDS + 5 * TICKS)
            .eut(TierEU.RECIPE_MV)
            .addTo(centrifugeRecipes);
    }
}
