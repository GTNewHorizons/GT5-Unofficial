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
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials2.Materials2FluidShapes;
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.enums.materials2.Materials2Shapes;
import gregtech.api.util.GTOreDictUnificator;

public class BauxiteRefineChain {

    public static void run() {

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Bauxite, Materials2Shapes.crushed, (int) (32)),
                MaterialLibAPI.getStack(Materials2Materials.SodiumHydroxideGT5U, Materials2Shapes.dust, 9),
                MaterialLibAPI.getStack(Materials2Materials.Quicklime, Materials2Shapes.dust, (int) (4)))
            .circuit(8)
            .fluidInputs(Materials.Water.getFluid(5_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.BauxiteSlurry, Materials2FluidShapes.fluidLiquid, (int) (8_000)))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Bauxite, Materials2Shapes.crushedPurified, (int) (32)),
                MaterialLibAPI.getStack(Materials2Materials.SodiumHydroxideGT5U, Materials2Shapes.dust, 9),
                MaterialLibAPI.getStack(Materials2Materials.Quicklime, Materials2Shapes.dust, (int) (4)))
            .circuit(8)
            .fluidInputs(Materials.Water.getFluid(5_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.BauxiteSlurry, Materials2FluidShapes.fluidLiquid, (int) (8_000)))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.BauxiteSlurry, Materials2FluidShapes.fluidLiquid, (int) (2_000)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.HeadedBauxiteSlurry, Materials2FluidShapes.fluidLiquid, 2_000))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(fluidHeaterRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.AluminiumHydroxide, Materials2Shapes.dust, 1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Alumina, Materials2Shapes.dust, 64),
                MaterialLibAPI.getStack(Materials2Materials.Alumina, Materials2Shapes.dust, 16),
                MaterialLibAPI.getStack(Materials2Materials.SodiumCarbonate, Materials2Shapes.dust, (int) (9)),
                MaterialLibAPI.getStack(Materials2Materials.Calcite, Materials2Shapes.dust, (int) (10)),
                MaterialLibAPI.getStack(Materials2Materials.BauxiteSlag, Materials2Shapes.dust, (int) (16)))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.CarbonDioxide, Materials2FluidShapes.fluidGas, (int) (5_000)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.HeadedBauxiteSlurry, Materials2FluidShapes.fluidLiquid, 8_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SluiceJuice, Materials2FluidShapes.fluidLiquid, (int) (5_000)))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.BauxiteSlag, Materials2Shapes.dust, (int) (1)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Rutile, Materials2Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Gallium, Materials2Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Quicklime, Materials2Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.SiliconDioxide, Materials2Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Iron, Materials2Shapes.dust, (int) (1)))
            .outputChances(10000, 3000, 2000, 9000, 8000)
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Ilmenite, Materials2Shapes.crushedPurified, (int) (1)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Rutile, Materials2Shapes.dust, (int) (2)),
                MaterialLibAPI.getStack(Materials2Materials.IlmeniteSlag, Materials2Shapes.dust, (int) (1)))
            .outputChances(10000, 3000)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SulfuricAcid, Materials2FluidShapes.fluidLiquid, (int) (1_000)))
            .fluidOutputs(new FluidStack(ItemList.sGreenVitriol, 2_000))
            .duration(21 * SECONDS)
            .eut(1000)
            .addTo(chemicalBathRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Ilmenite, Materials2Shapes.crushed, (int) (1)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Rutile, Materials2Shapes.dust, (int) (2)),
                MaterialLibAPI.getStack(Materials2Materials.IlmeniteSlag, Materials2Shapes.dust, (int) (1)))
            .outputChances(10000, 6000)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SulfuricAcid, Materials2FluidShapes.fluidLiquid, (int) (1_000)))
            .fluidOutputs(new FluidStack(ItemList.sGreenVitriol, 2_000))
            .duration(21 * SECONDS)
            .eut(1000)
            .addTo(chemicalBathRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.IlmeniteSlag, Materials2Shapes.dust, (int) (1)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Iron, Materials2Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Niobium, Materials2Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Tantalum, Materials2Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Manganese, Materials2Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Magnesium, Materials2Shapes.dust, (int) (1)))
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
                    MaterialLibAPI.getStack(Materials2Materials.SodiumHydroxideGT5U, Materials2Shapes.dustTiny, 1))
                .circuit(1)
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.HydrochloricAcidGT5U,
                        Materials2FluidShapes.fluidLiquid,
                        1_000))
                .fluidOutputs(
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.SapphireJuice,
                        Materials2FluidShapes.fluidLiquid,
                        (int) (1_000)))
                .duration(2 * SECONDS)
                .eut(TierEU.RECIPE_MV)
                .addTo(mixerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTOreDictUnificator.get(ore, Materials.GreenSapphire, 1),
                    MaterialLibAPI.getStack(Materials2Materials.SodiumHydroxideGT5U, Materials2Shapes.dustTiny, 1))
                .circuit(1)
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.HydrochloricAcidGT5U,
                        Materials2FluidShapes.fluidLiquid,
                        1_000))
                .fluidOutputs(
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.GreenSapphireJuice,
                        Materials2FluidShapes.fluidLiquid,
                        (int) (1_000)))
                .duration(2 * SECONDS)
                .eut(TierEU.RECIPE_MV)
                .addTo(mixerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTOreDictUnificator.get(ore, Materials.Ruby, 1),
                    MaterialLibAPI.getStack(Materials2Materials.SodiumHydroxideGT5U, Materials2Shapes.dustTiny, 1))
                .circuit(1)
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.HydrochloricAcidGT5U,
                        Materials2FluidShapes.fluidLiquid,
                        1_000))
                .fluidOutputs(
                    MaterialLibAPI
                        .getFluidStack(Materials2Materials.RubyJuice, Materials2FluidShapes.fluidLiquid, (int) (1_000)))
                .duration(2 * SECONDS)
                .eut(TierEU.RECIPE_MV)
                .addTo(mixerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTOreDictUnificator.get(ore, Materials.Sapphire, 9),
                    MaterialLibAPI.getStack(Materials2Materials.SodiumHydroxideGT5U, Materials2Shapes.dust, 1))
                .circuit(9)
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.HydrochloricAcidGT5U,
                        Materials2FluidShapes.fluidLiquid,
                        9_000))
                .fluidOutputs(
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.SapphireJuice,
                        Materials2FluidShapes.fluidLiquid,
                        (int) (9_000)))
                .duration(3 * SECONDS)
                .eut(TierEU.RECIPE_HV)
                .addTo(mixerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTOreDictUnificator.get(ore, Materials.GreenSapphire, 9),
                    MaterialLibAPI.getStack(Materials2Materials.SodiumHydroxideGT5U, Materials2Shapes.dust, 1))
                .circuit(9)
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.HydrochloricAcidGT5U,
                        Materials2FluidShapes.fluidLiquid,
                        9_000))
                .fluidOutputs(
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.GreenSapphireJuice,
                        Materials2FluidShapes.fluidLiquid,
                        (int) (9_000)))
                .duration(3 * SECONDS)
                .eut(TierEU.RECIPE_HV)
                .addTo(mixerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTOreDictUnificator.get(ore, Materials.Ruby, 9),
                    MaterialLibAPI.getStack(Materials2Materials.SodiumHydroxideGT5U, Materials2Shapes.dust, 1))
                .circuit(9)
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.HydrochloricAcidGT5U,
                        Materials2FluidShapes.fluidLiquid,
                        9_000))
                .fluidOutputs(
                    MaterialLibAPI
                        .getFluidStack(Materials2Materials.RubyJuice, Materials2FluidShapes.fluidLiquid, (int) (9_000)))
                .duration(3 * SECONDS)
                .eut(TierEU.RECIPE_HV)
                .addTo(mixerRecipes);
        }

        GTValues.RA.stdBuilder()
            .circuit(1)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.AluminiumHydroxide, Materials2Shapes.dust, 2),
                MaterialLibAPI.getStack(Materials2Materials.Iron, Materials2Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Vanadium, Materials2Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Magnesium, Materials2Shapes.dust, (int) (1)))
            .outputChances(10000, 300, 200, 200)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SapphireJuice, Materials2FluidShapes.fluidLiquid, (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.HydrochloricAcidGT5U, Materials2FluidShapes.fluidLiquid, 1_000))
            .duration(2 * SECONDS + 5 * TICKS)
            .eut(100)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .circuit(1)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.AluminiumHydroxide, Materials2Shapes.dust, 2),
                MaterialLibAPI.getStack(Materials2Materials.Iron, Materials2Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Vanadium, Materials2Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Manganese, Materials2Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Beryllium, Materials2Shapes.dust, (int) (1)))
            .outputChances(10000, 300, 200, 200, 200)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.GreenSapphireJuice,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.HydrochloricAcidGT5U, Materials2FluidShapes.fluidLiquid, 1_000))
            .duration(2 * SECONDS + 5 * TICKS)
            .eut(100)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .circuit(1)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.AluminiumHydroxide, Materials2Shapes.dust, 2),
                MaterialLibAPI.getStack(Materials2Materials.Chrome, Materials2Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Iron, Materials2Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Vanadium, Materials2Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Magnesium, Materials2Shapes.dust, (int) (1)))
            .outputChances(10000, 5000, 300, 200, 200)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.RubyJuice, Materials2FluidShapes.fluidLiquid, (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.HydrochloricAcidGT5U, Materials2FluidShapes.fluidLiquid, 1_000))
            .duration(2 * SECONDS + 5 * TICKS)
            .eut(100)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Pyrope, Materials2Shapes.dust, (int) (1)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Alumina, Materials2Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Magnesia, Materials2Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Silver, Materials2Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Iron, Materials2Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Calcite, Materials2Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Vanadium, Materials2Shapes.dust, (int) (1)))
            .outputChances(5000, 4000, 300, 300, 300, 200)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.NitricAcid, Materials2FluidShapes.fluidLiquid, (int) (10)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SluiceJuice, Materials2FluidShapes.fluidLiquid, (int) (10)))
            .duration(2 * SECONDS + 5 * TICKS)
            .eut(TierEU.RECIPE_MV)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Almandine, Materials2Shapes.dust, (int) (1)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Alumina, Materials2Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Iron, Materials2Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Gold, Materials2Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Calcite, Materials2Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Chrome, Materials2Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Vanadium, Materials2Shapes.dust, (int) (1)))
            .outputChances(5000, 4000, 300, 300, 200, 200)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.NitricAcid, Materials2FluidShapes.fluidLiquid, (int) (10)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SluiceJuice, Materials2FluidShapes.fluidLiquid, (int) (10)))
            .duration(2 * SECONDS + 5 * TICKS)
            .eut(TierEU.RECIPE_MV)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Spessartine, Materials2Shapes.dust, (int) (1)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Alumina, Materials2Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Pyrolusite, Materials2Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Iron, Materials2Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Calcite, Materials2Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Magnesium, Materials2Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Tantalum, Materials2Shapes.dust, (int) (1)))
            .outputChances(5000, 4000, 300, 300, 300, 200)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.NitricAcid, Materials2FluidShapes.fluidLiquid, (int) (10)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SluiceJuice, Materials2FluidShapes.fluidLiquid, (int) (10)))
            .duration(2 * SECONDS + 5 * TICKS)
            .eut(TierEU.RECIPE_MV)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Andradite, Materials2Shapes.dust, (int) (1)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Quicklime, Materials2Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Iron, Materials2Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Alumina, Materials2Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Gold, Materials2Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Vanadium, Materials2Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Rutile, Materials2Shapes.dust, (int) (1)))
            .outputChances(5000, 4000, 300, 300, 200, 600)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.NitricAcid, Materials2FluidShapes.fluidLiquid, (int) (10)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SluiceJuice, Materials2FluidShapes.fluidLiquid, (int) (10)))
            .duration(2 * SECONDS + 5 * TICKS)
            .eut(TierEU.RECIPE_MV)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Uvarovite, Materials2Shapes.dust, (int) (1)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Quicklime, Materials2Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Chrome, Materials2Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Iron, Materials2Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Silver, Materials2Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Alumina, Materials2Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Manganese, Materials2Shapes.dust, (int) (1)))
            .outputChances(5000, 1000, 300, 300, 200, 200)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.NitricAcid, Materials2FluidShapes.fluidLiquid, (int) (10)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SluiceJuice, Materials2FluidShapes.fluidLiquid, (int) (10)))
            .duration(2 * SECONDS + 5 * TICKS)
            .eut(TierEU.RECIPE_MV)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Grossular, Materials2Shapes.dust, (int) (1)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Quicklime, Materials2Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Alumina, Materials2Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Iron, Materials2Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Gold, Materials2Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Calcite, Materials2Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Vanadium, Materials2Shapes.dust, (int) (1)))
            .outputChances(5000, 4000, 300, 300, 300, 200)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.NitricAcid, Materials2FluidShapes.fluidLiquid, (int) (10)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SluiceJuice, Materials2FluidShapes.fluidLiquid, (int) (10)))
            .duration(2 * SECONDS + 5 * TICKS)
            .eut(TierEU.RECIPE_MV)
            .addTo(centrifugeRecipes);
    }
}
