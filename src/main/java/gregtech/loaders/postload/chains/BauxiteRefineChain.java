package gregtech.loaders.postload.chains;

import static gregtech.api.recipe.RecipeMaps.centrifugeRecipes;
import static gregtech.api.recipe.RecipeMaps.chemicalBathRecipes;
import static gregtech.api.recipe.RecipeMaps.fluidHeaterRecipes;
import static gregtech.api.recipe.RecipeMaps.mixerRecipes;
import static gregtech.api.recipe.RecipeMaps.multiblockChemicalReactorRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;

import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTOreDictUnificator;

public class BauxiteRefineChain {

    public static void run() {

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.crushed, Materials.Bauxite, 32),
                Materials.SodiumHydroxide.getDust(9),
                Materials.Quicklime.getDust(4))
            .circuit(8)
            .fluidInputs(Materials.Water.getFluid(5_000))
            .fluidOutputs(Materials.BauxiteSlurry.getFluid(8_000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.crushedPurified, Materials.Bauxite, 32),
                Materials.SodiumHydroxide.getDust(9),
                Materials.Quicklime.getDust(4))
            .circuit(8)
            .fluidInputs(Materials.Water.getFluid(5_000))
            .fluidOutputs(Materials.BauxiteSlurry.getFluid(8_000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .circuit(1)
            .fluidInputs(Materials.BauxiteSlurry.getFluid(2_000))
            .fluidOutputs(Materials.HeatedBauxiteSlurry.getFluid(2_000))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(fluidHeaterRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Aluminiumhydroxide.getDust(1))
            .itemOutputs(
                Materials.Aluminiumoxide.getDust(64),
                Materials.Aluminiumoxide.getDust(16),
                Materials.SodiumCarbonate.getDust(9),
                Materials.Calcite.getDust(10),
                Materials.BauxiteSlag.getDust(16))
            .fluidInputs(Materials.CarbonDioxide.getGas(5_000), Materials.HeatedBauxiteSlurry.getFluid(8_000))
            .fluidOutputs(Materials.SluiceJuice.getFluid(5_000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.BauxiteSlag.getDust(1))
            .itemOutputs(
                Materials.Rutile.getDust(1),
                Materials.Gallium.getDust(1),
                Materials.Quicklime.getDust(1),
                Materials.SiliconDioxide.getDust(1),
                Materials.Iron.getDust(1))
            .outputChances(10000, 3000, 2000, 9000, 8000)
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.crushedPurified, Materials.Ilmenite, 1))
            .itemOutputs(Materials.Rutile.getDust(2), Materials.IlmeniteSlag.getDust(1))
            .outputChances(10000, 3000)
            .fluidInputs(Materials.SulfuricAcid.getFluid(1_000))
            .fluidOutputs(new FluidStack(ItemList.sGreenVitriol, 2_000))
            .duration(21 * SECONDS)
            .eut(1000)
            .addTo(chemicalBathRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.crushed, Materials.Ilmenite, 1))
            .itemOutputs(Materials.Rutile.getDust(2), Materials.IlmeniteSlag.getDust(1))
            .outputChances(10000, 6000)
            .fluidInputs(Materials.SulfuricAcid.getFluid(1_000))
            .fluidOutputs(new FluidStack(ItemList.sGreenVitriol, 2_000))
            .duration(21 * SECONDS)
            .eut(1000)
            .addTo(chemicalBathRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.IlmeniteSlag.getDust(1))
            .itemOutputs(
                Materials.Iron.getDust(1),
                Materials.Niobium.getDust(1),
                Materials.Tantalum.getDust(1),
                Materials.Manganese.getDust(1),
                Materials.Magnesium.getDust(1))
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
                    GTOreDictUnificator.get(OrePrefixes.dustTiny, Materials.SodiumHydroxide, 1))
                .circuit(1)
                .fluidInputs(Materials.HydrochloricAcid.getFluid(1_000))
                .fluidOutputs(Materials.SapphireJuice.getFluid(1_000))
                .duration(2 * SECONDS)
                .eut(TierEU.RECIPE_MV)
                .addTo(mixerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTOreDictUnificator.get(ore, Materials.GreenSapphire, 1),
                    GTOreDictUnificator.get(OrePrefixes.dustTiny, Materials.SodiumHydroxide, 1))
                .circuit(1)
                .fluidInputs(Materials.HydrochloricAcid.getFluid(1_000))
                .fluidOutputs(Materials.GreenSapphireJuice.getFluid(1_000))
                .duration(2 * SECONDS)
                .eut(TierEU.RECIPE_MV)
                .addTo(mixerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTOreDictUnificator.get(ore, Materials.Ruby, 1),
                    GTOreDictUnificator.get(OrePrefixes.dustTiny, Materials.SodiumHydroxide, 1))
                .circuit(1)
                .fluidInputs(Materials.HydrochloricAcid.getFluid(1_000))
                .fluidOutputs(Materials.RubyJuice.getFluid(1_000))
                .duration(2 * SECONDS)
                .eut(TierEU.RECIPE_MV)
                .addTo(mixerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTOreDictUnificator.get(ore, Materials.Sapphire, 9),
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.SodiumHydroxide, 1))
                .circuit(9)
                .fluidInputs(Materials.HydrochloricAcid.getFluid(9_000))
                .fluidOutputs(Materials.SapphireJuice.getFluid(9_000))
                .duration(3 * SECONDS)
                .eut(TierEU.RECIPE_HV)
                .addTo(mixerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTOreDictUnificator.get(ore, Materials.GreenSapphire, 9),
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.SodiumHydroxide, 1))
                .circuit(9)
                .fluidInputs(Materials.HydrochloricAcid.getFluid(9_000))
                .fluidOutputs(Materials.GreenSapphireJuice.getFluid(9_000))
                .duration(3 * SECONDS)
                .eut(TierEU.RECIPE_HV)
                .addTo(mixerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTOreDictUnificator.get(ore, Materials.Ruby, 9),
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.SodiumHydroxide, 1))
                .circuit(9)
                .fluidInputs(Materials.HydrochloricAcid.getFluid(9_000))
                .fluidOutputs(Materials.RubyJuice.getFluid(9_000))
                .duration(3 * SECONDS)
                .eut(TierEU.RECIPE_HV)
                .addTo(mixerRecipes);
        }

        GTValues.RA.stdBuilder()
            .circuit(1)
            .itemOutputs(
                Materials.Aluminiumhydroxide.getDust(2),
                Materials.Iron.getDust(1),
                Materials.Vanadium.getDust(1),
                Materials.Magnesium.getDust(1))
            .outputChances(10000, 300, 200, 200)
            .fluidInputs(Materials.SapphireJuice.getFluid(1_000))
            .fluidOutputs(Materials.HydrochloricAcid.getFluid(1_000))
            .duration(2 * SECONDS + 5 * TICKS)
            .eut(100)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .circuit(1)
            .itemOutputs(
                Materials.Aluminiumhydroxide.getDust(2),
                Materials.Iron.getDust(1),
                Materials.Vanadium.getDust(1),
                Materials.Manganese.getDust(1),
                Materials.Beryllium.getDust(1))
            .outputChances(10000, 300, 200, 200, 200)
            .fluidInputs(Materials.GreenSapphireJuice.getFluid(1_000))
            .fluidOutputs(Materials.HydrochloricAcid.getFluid(1_000))
            .duration(2 * SECONDS + 5 * TICKS)
            .eut(100)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .circuit(1)
            .itemOutputs(
                Materials.Aluminiumhydroxide.getDust(2),
                Materials.Chrome.getDust(1),
                Materials.Iron.getDust(1),
                Materials.Vanadium.getDust(1),
                Materials.Magnesium.getDust(1))
            .outputChances(10000, 5000, 300, 200, 200)
            .fluidInputs(Materials.RubyJuice.getFluid(1_000))
            .fluidOutputs(Materials.HydrochloricAcid.getFluid(1_000))
            .duration(2 * SECONDS + 5 * TICKS)
            .eut(100)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Pyrope.getDust(1))
            .itemOutputs(
                Materials.Aluminiumoxide.getDust(1),
                Materials.Magnesia.getDust(1),
                Materials.Silver.getDust(1),
                Materials.Iron.getDust(1),
                Materials.Calcite.getDust(1),
                Materials.Vanadium.getDust(1))
            .outputChances(5000, 4000, 300, 300, 300, 200)
            .fluidInputs(Materials.NitricAcid.getFluid(10))
            .fluidOutputs(Materials.SluiceJuice.getFluid(10))
            .duration(2 * SECONDS + 5 * TICKS)
            .eut(TierEU.RECIPE_MV)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Almandine.getDust(1))
            .itemOutputs(
                Materials.Aluminiumoxide.getDust(1),
                Materials.Iron.getDust(1),
                Materials.Gold.getDust(1),
                Materials.Calcite.getDust(1),
                Materials.Chrome.getDust(1),
                Materials.Vanadium.getDust(1))
            .outputChances(5000, 4000, 300, 300, 200, 200)
            .fluidInputs(Materials.NitricAcid.getFluid(10))
            .fluidOutputs(Materials.SluiceJuice.getFluid(10))
            .duration(2 * SECONDS + 5 * TICKS)
            .eut(TierEU.RECIPE_MV)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Spessartine.getDust(1))
            .itemOutputs(
                Materials.Aluminiumoxide.getDust(1),
                Materials.Pyrolusite.getDust(1),
                Materials.Iron.getDust(1),
                Materials.Calcite.getDust(1),
                Materials.Magnesium.getDust(1),
                Materials.Tantalum.getDust(1))
            .outputChances(5000, 4000, 300, 300, 300, 200)
            .fluidInputs(Materials.NitricAcid.getFluid(10))
            .fluidOutputs(Materials.SluiceJuice.getFluid(10))
            .duration(2 * SECONDS + 5 * TICKS)
            .eut(TierEU.RECIPE_MV)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Andradite.getDust(1))
            .itemOutputs(
                Materials.Quicklime.getDust(1),
                Materials.Iron.getDust(1),
                Materials.Aluminiumoxide.getDust(1),
                Materials.Gold.getDust(1),
                Materials.Vanadium.getDust(1),
                Materials.Rutile.getDust(1))
            .outputChances(5000, 4000, 300, 300, 200, 600)
            .fluidInputs(Materials.NitricAcid.getFluid(10))
            .fluidOutputs(Materials.SluiceJuice.getFluid(10))
            .duration(2 * SECONDS + 5 * TICKS)
            .eut(TierEU.RECIPE_MV)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Uvarovite.getDust(1))
            .itemOutputs(
                Materials.Quicklime.getDust(1),
                Materials.Chrome.getDust(1),
                Materials.Iron.getDust(1),
                Materials.Silver.getDust(1),
                Materials.Aluminiumoxide.getDust(1),
                Materials.Manganese.getDust(1))
            .outputChances(5000, 1000, 300, 300, 200, 200)
            .fluidInputs(Materials.NitricAcid.getFluid(10))
            .fluidOutputs(Materials.SluiceJuice.getFluid(10))
            .duration(2 * SECONDS + 5 * TICKS)
            .eut(TierEU.RECIPE_MV)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Grossular.getDust(1))
            .itemOutputs(
                Materials.Quicklime.getDust(1),
                Materials.Aluminiumoxide.getDust(1),
                Materials.Iron.getDust(1),
                Materials.Gold.getDust(1),
                Materials.Calcite.getDust(1),
                Materials.Vanadium.getDust(1))
            .outputChances(5000, 4000, 300, 300, 300, 200)
            .fluidInputs(Materials.NitricAcid.getFluid(10))
            .fluidOutputs(Materials.SluiceJuice.getFluid(10))
            .duration(2 * SECONDS + 5 * TICKS)
            .eut(TierEU.RECIPE_MV)
            .addTo(centrifugeRecipes);
    }
}
