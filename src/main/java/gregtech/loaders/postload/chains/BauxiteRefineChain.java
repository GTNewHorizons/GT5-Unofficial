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
import gregtech.api.enums.MaterialsOreAlum;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;

public class BauxiteRefineChain {

    public static void run() {

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.crushed, Materials.Bauxite, 32),
                Materials.SodiumHydroxide.getDust(9),
                Materials.Quicklime.getDust(4),
                GTUtility.getIntegratedCircuit(8))
            .fluidInputs(Materials.Water.getFluid(5000))
            .fluidOutputs(MaterialsOreAlum.BauxiteSlurry.getFluid(8000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.crushedPurified, Materials.Bauxite, 32),
                Materials.SodiumHydroxide.getDust(9),
                Materials.Quicklime.getDust(4),
                GTUtility.getIntegratedCircuit(8))
            .fluidInputs(Materials.Water.getFluid(5000))
            .fluidOutputs(MaterialsOreAlum.BauxiteSlurry.getFluid(8000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(1))
            .fluidInputs(MaterialsOreAlum.BauxiteSlurry.getFluid(2000))
            .fluidOutputs(MaterialsOreAlum.HeatedBauxiteSlurry.getFluid(2000))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .noOptimize()
            .addTo(fluidHeaterRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Aluminiumhydroxide.getDust(1))
            .itemOutputs(
                Materials.Aluminiumoxide.getDust(64),
                Materials.Aluminiumoxide.getDust(16),
                Materials.SodiumCarbonate.getDust(9),
                Materials.Calcite.getDust(10),
                MaterialsOreAlum.BauxiteSlag.getDust(16))
            .fluidInputs(Materials.CarbonDioxide.getGas(5000), MaterialsOreAlum.HeatedBauxiteSlurry.getFluid(8000))
            .fluidOutputs(MaterialsOreAlum.SluiceJuice.getFluid(5000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialsOreAlum.BauxiteSlag.getDust(1))
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
            .itemOutputs(Materials.Rutile.getDust(2), MaterialsOreAlum.IlmeniteSlag.getDust(1))
            .outputChances(10000, 3000)
            .fluidInputs(Materials.SulfuricAcid.getFluid(1000))
            .fluidOutputs(new FluidStack(ItemList.sGreenVitriol, 2000))
            .duration(21 * SECONDS)
            .eut(1000)
            .addTo(chemicalBathRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.crushed, Materials.Ilmenite, 1))
            .itemOutputs(Materials.Rutile.getDust(2), MaterialsOreAlum.IlmeniteSlag.getDust(1))
            .outputChances(10000, 6000)
            .fluidInputs(Materials.SulfuricAcid.getFluid(1000))
            .fluidOutputs(new FluidStack(ItemList.sGreenVitriol, 2000))
            .duration(21 * SECONDS)
            .eut(1000)
            .addTo(chemicalBathRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialsOreAlum.IlmeniteSlag.getDust(1))
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
                    GTOreDictUnificator.get(OrePrefixes.dustTiny, Materials.SodiumHydroxide, 1),
                    GTUtility.getIntegratedCircuit(1))
                .fluidInputs(Materials.HydrochloricAcid.getFluid(1000))
                .fluidOutputs(MaterialsOreAlum.SapphireJuice.getFluid(1000))
                .duration(2 * SECONDS)
                .eut(100)
                .addTo(mixerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTOreDictUnificator.get(ore, Materials.GreenSapphire, 1),
                    GTOreDictUnificator.get(OrePrefixes.dustTiny, Materials.SodiumHydroxide, 1),
                    GTUtility.getIntegratedCircuit(1))
                .fluidInputs(Materials.HydrochloricAcid.getFluid(1000))
                .fluidOutputs(MaterialsOreAlum.GreenSapphireJuice.getFluid(1000))
                .duration(2 * SECONDS)
                .eut(100)
                .addTo(mixerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTOreDictUnificator.get(ore, Materials.Ruby, 1),
                    GTOreDictUnificator.get(OrePrefixes.dustTiny, Materials.SodiumHydroxide, 1),
                    GTUtility.getIntegratedCircuit(1))
                .fluidInputs(Materials.HydrochloricAcid.getFluid(1000))
                .fluidOutputs(MaterialsOreAlum.RubyJuice.getFluid(1000))
                .duration(2 * SECONDS)
                .eut(100)
                .addTo(mixerRecipes);
        }

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(1))
            .itemOutputs(
                Materials.Aluminiumhydroxide.getDust(3),
                Materials.Iron.getDust(1),
                Materials.Vanadium.getDust(1),
                Materials.Magnesium.getDust(1))
            .outputChances(10000, 300, 200, 200)
            .fluidInputs(MaterialsOreAlum.SapphireJuice.getFluid(1000))
            .fluidOutputs(Materials.HydrochloricAcid.getFluid(1000))
            .duration(2 * SECONDS + 5 * TICKS)
            .eut(100)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(1))
            .itemOutputs(
                Materials.Aluminiumhydroxide.getDust(3),
                Materials.Iron.getDust(1),
                Materials.Vanadium.getDust(1),
                Materials.Manganese.getDust(1),
                Materials.Beryllium.getDust(1))
            .outputChances(10000, 300, 200, 200, 200)
            .fluidInputs(MaterialsOreAlum.GreenSapphireJuice.getFluid(1000))
            .fluidOutputs(Materials.HydrochloricAcid.getFluid(1000))
            .duration(2 * SECONDS + 5 * TICKS)
            .eut(100)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(1))
            .itemOutputs(
                Materials.Aluminiumhydroxide.getDust(3),
                Materials.Chrome.getDust(1),
                Materials.Iron.getDust(1),
                Materials.Vanadium.getDust(1),
                Materials.Magnesium.getDust(1))
            .outputChances(10000, 5000, 300, 200, 200)
            .fluidInputs(MaterialsOreAlum.RubyJuice.getFluid(1000))
            .fluidOutputs(Materials.HydrochloricAcid.getFluid(1000))
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
            .fluidOutputs(MaterialsOreAlum.SluiceJuice.getFluid(10))
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
            .fluidOutputs(MaterialsOreAlum.SluiceJuice.getFluid(10))
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
            .fluidOutputs(MaterialsOreAlum.SluiceJuice.getFluid(10))
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
            .fluidOutputs(MaterialsOreAlum.SluiceJuice.getFluid(10))
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
            .fluidOutputs(MaterialsOreAlum.SluiceJuice.getFluid(10))
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
            .fluidOutputs(MaterialsOreAlum.SluiceJuice.getFluid(10))
            .duration(2 * SECONDS + 5 * TICKS)
            .eut(TierEU.RECIPE_MV)
            .addTo(centrifugeRecipes);
    }
}
