package gregtech.loaders.postload.chains;

import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sCentrifugeRecipes;
import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sChemicalBathRecipes;
import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sMixerRecipes;
import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sMultiblockChemicalRecipes;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;
import static gregtech.api.util.GT_RecipeBuilder.TICKS;

import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.*;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;

public class GT_BauxiteRefineChain {

    public static void run() {

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.crushed, Materials.Bauxite, 32),
                Materials.SodiumHydroxide.getDust(9),
                Materials.Quicklime.getDust(4),
                GT_Utility.getIntegratedCircuit(8))
            .noItemOutputs()
            .fluidInputs(Materials.Water.getFluid(5000))
            .fluidOutputs(MaterialsOreAlum.BauxiteSlurry.getFluid(8000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(sMixerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.crushedPurified, Materials.Bauxite, 32),
                Materials.SodiumHydroxide.getDust(9),
                Materials.Quicklime.getDust(4),
                GT_Utility.getIntegratedCircuit(8))
            .noItemOutputs()
            .fluidInputs(Materials.Water.getFluid(5000))
            .fluidOutputs(MaterialsOreAlum.BauxiteSlurry.getFluid(8000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(sMixerRecipes);

        GT_Values.RA.addCrackingRecipe(
            1,
            MaterialsOreAlum.BauxiteSlurry.getFluid(32000),
            GT_ModHandler.getSteam(2000),
            MaterialsOreAlum.HeatedBauxiteSlurry.getFluid(32000),
            160,
            400);

        GT_Values.RA.stdBuilder()
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
            .addTo(sMultiblockChemicalRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(MaterialsOreAlum.BauxiteSlag.getDust(1))
            .itemOutputs(
                Materials.Rutile.getDust(1),
                Materials.Gallium.getDust(1),
                Materials.Quicklime.getDust(1),
                Materials.SiliconDioxide.getDust(1),
                Materials.Iron.getDust(1))
            .outputChances(8000, 3000, 2000, 9000, 8000)
            .noFluidInputs()
            .noFluidOutputs()
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(sCentrifugeRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(GT_OreDictUnificator.get(OrePrefixes.crushedPurified, Materials.Ilmenite, 1))
            .itemOutputs(Materials.Rutile.getDust(1), MaterialsOreAlum.IlmeniteSlag.getDust(1))
            .outputChances(8500, 3000)
            .fluidInputs(Materials.SulfuricAcid.getFluid(1000))
            .fluidOutputs(new FluidStack(ItemList.sGreenVitriol, 2000))
            .duration(21 * SECONDS)
            .eut(1000)
            .addTo(sChemicalBathRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(GT_OreDictUnificator.get(OrePrefixes.crushed, Materials.Ilmenite, 1))
            .itemOutputs(Materials.Rutile.getDust(1), MaterialsOreAlum.IlmeniteSlag.getDust(1))
            .outputChances(8500, 6000)
            .fluidInputs(Materials.SulfuricAcid.getFluid(1000))
            .fluidOutputs(new FluidStack(ItemList.sGreenVitriol, 2000))
            .duration(21 * SECONDS)
            .eut(1000)
            .addTo(sChemicalBathRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(MaterialsOreAlum.IlmeniteSlag.getDust(1))
            .itemOutputs(
                Materials.Iron.getDust(1),
                Materials.Niobium.getDust(1),
                Materials.Tantalum.getDust(1),
                Materials.Manganese.getDust(1),
                Materials.Magnesium.getDust(1))
            .outputChances(8000, 500, 2000, 5000, 6000)
            .noFluidInputs()
            .noFluidOutputs()
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(sCentrifugeRecipes);

        OrePrefixes[] washable = new OrePrefixes[] { OrePrefixes.crushed, OrePrefixes.crushedPurified,
            OrePrefixes.dustImpure, OrePrefixes.dustPure };

        for (OrePrefixes ore : washable) {
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_OreDictUnificator.get(ore, Materials.Sapphire, 1),
                    GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.SodiumHydroxide, 1),
                    GT_Utility.getIntegratedCircuit(1))
                .noItemOutputs()
                .fluidInputs(Materials.HydrochloricAcid.getFluid(1000))
                .fluidOutputs(MaterialsOreAlum.SapphireJuice.getFluid(1000))
                .duration(2 * SECONDS)
                .eut(100)
                .addTo(sMixerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_OreDictUnificator.get(ore, Materials.GreenSapphire, 1),
                    GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.SodiumHydroxide, 1),
                    GT_Utility.getIntegratedCircuit(1))
                .noItemOutputs()
                .fluidInputs(Materials.HydrochloricAcid.getFluid(1000))
                .fluidOutputs(MaterialsOreAlum.GreenSapphireJuice.getFluid(1000))
                .duration(2 * SECONDS)
                .eut(100)
                .addTo(sMixerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_OreDictUnificator.get(ore, Materials.Ruby, 1),
                    GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.SodiumHydroxide, 1),
                    GT_Utility.getIntegratedCircuit(1))
                .noItemOutputs()
                .fluidInputs(Materials.HydrochloricAcid.getFluid(1000))
                .fluidOutputs(MaterialsOreAlum.RubyJuice.getFluid(1000))
                .duration(2 * SECONDS)
                .eut(100)
                .addTo(sMixerRecipes);
        }

        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.getIntegratedCircuit(1))
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
            .addTo(sCentrifugeRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.getIntegratedCircuit(1))
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
            .addTo(sCentrifugeRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.getIntegratedCircuit(1))
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
            .addTo(sCentrifugeRecipes);

        GT_Values.RA.stdBuilder()
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
            .addTo(sCentrifugeRecipes);

        GT_Values.RA.stdBuilder()
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
            .addTo(sCentrifugeRecipes);

        GT_Values.RA.stdBuilder()
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
            .addTo(sCentrifugeRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(Materials.Andradite.getDust(1))
            .itemOutputs(
                Materials.Quicklime.getDust(1),
                Materials.Iron.getDust(1),
                Materials.Aluminiumoxide.getDust(1),
                Materials.Gold.getDust(1),
                Materials.Vanadium.getDust(1),
                Materials.Rutile.getDust(1))
            .outputChances(5000, 4000, 300, 300, 200, 200)
            .fluidInputs(Materials.NitricAcid.getFluid(10))
            .fluidOutputs(MaterialsOreAlum.SluiceJuice.getFluid(10))
            .duration(2 * SECONDS + 5 * TICKS)
            .eut(TierEU.RECIPE_MV)
            .addTo(sCentrifugeRecipes);

        GT_Values.RA.stdBuilder()
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
            .addTo(sCentrifugeRecipes);

        GT_Values.RA.stdBuilder()
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
            .addTo(sCentrifugeRecipes);
    }
}
