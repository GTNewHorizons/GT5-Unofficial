package gregtech.loaders.postload.recipes;

import static gregtech.api.enums.Mods.AppliedEnergistics2;
import static gregtech.api.recipe.RecipeMaps.autoclaveRecipes;
import static gregtech.api.util.GTModHandler.getModItem;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsUEVplus;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;

public class AutoclaveRecipes implements Runnable {

    @Override
    public void run() {
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.IC2_Energium_Dust.get(9L))
            .itemOutputs(ItemList.IC2_EnergyCrystal.get(1L))
            .outputChances(10000)
            .fluidInputs(Materials.EnergeticAlloy.getMolten(288))
            .duration(30 * SECONDS)
            .eut(256)
            .addTo(autoclaveRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.IC2_Energium_Dust.get(9L))
            .itemOutputs(ItemList.IC2_EnergyCrystal.get(1L))
            .outputChances(10000)
            .fluidInputs(Materials.ConductiveIron.getMolten(576))
            .duration(60 * SECONDS)
            .eut(256)
            .addTo(autoclaveRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(AppliedEnergistics2.ID, "item.ItemCrystalSeed", 1L, 0))
            .itemOutputs(getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 1L, 10))
            .outputChances(8000)
            .fluidInputs(Materials.Water.getFluid(200L))
            .duration(1 * MINUTES + 40 * SECONDS)
            .eut(24)
            .addTo(autoclaveRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(AppliedEnergistics2.ID, "item.ItemCrystalSeed", 1L, 600))
            .itemOutputs(getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 1L, 11))
            .outputChances(8000)
            .fluidInputs(Materials.Water.getFluid(200L))
            .duration(1 * MINUTES + 40 * SECONDS)
            .eut(24)
            .addTo(autoclaveRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(AppliedEnergistics2.ID, "item.ItemCrystalSeed", 1L, 1200))
            .itemOutputs(getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 1L, 12))
            .outputChances(8000)
            .fluidInputs(Materials.Water.getFluid(200L))
            .duration(1 * MINUTES + 40 * SECONDS)
            .eut(24)
            .addTo(autoclaveRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(AppliedEnergistics2.ID, "item.ItemCrystalSeed", 1L, 0))
            .itemOutputs(getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 1L, 10))
            .outputChances(9000)
            .fluidInputs(GTModHandler.getDistilledWater(100L))
            .duration(50 * SECONDS)
            .eut(24)
            .addTo(autoclaveRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(AppliedEnergistics2.ID, "item.ItemCrystalSeed", 1L, 600))
            .itemOutputs(getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 1L, 11))
            .outputChances(9000)
            .fluidInputs(GTModHandler.getDistilledWater(100L))
            .duration(50 * SECONDS)
            .eut(24)
            .addTo(autoclaveRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(AppliedEnergistics2.ID, "item.ItemCrystalSeed", 1L, 1200))
            .itemOutputs(getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 1L, 12))
            .outputChances(9000)
            .fluidInputs(GTModHandler.getDistilledWater(100L))
            .duration(50 * SECONDS)
            .eut(24)
            .addTo(autoclaveRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(AppliedEnergistics2.ID, "item.ItemCrystalSeed", 1L, 0))
            .itemOutputs(getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 1L, 10))
            .outputChances(10000)
            .fluidInputs(Materials.Void.getMolten(36L))
            .duration(25 * SECONDS)
            .eut(24)
            .addTo(autoclaveRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(AppliedEnergistics2.ID, "item.ItemCrystalSeed", 1L, 600))
            .itemOutputs(getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 1L, 11))
            .outputChances(10000)
            .fluidInputs(Materials.Void.getMolten(36L))
            .duration(25 * SECONDS)
            .eut(24)
            .addTo(autoclaveRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(AppliedEnergistics2.ID, "item.ItemCrystalSeed", 1L, 1200))
            .itemOutputs(getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 1L, 12))
            .outputChances(10000)
            .fluidInputs(Materials.Void.getMolten(36L))
            .duration(25 * SECONDS)
            .eut(24)
            .addTo(autoclaveRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Carbon, 32))
            .itemOutputs(GTModHandler.getIC2Item("carbonFiber", 64L))
            .outputChances(10000)
            .fluidInputs(Materials.Polybenzimidazole.getMolten(36L))
            .duration(7 * SECONDS + 10 * TICKS)
            .eut((int) TierEU.RECIPE_EV)
            .addTo(autoclaveRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Carbon, 64))
            .itemOutputs(GTModHandler.getIC2Item("carbonFiber", 64L))
            .outputChances(10000)
            .fluidInputs(Materials.Epoxid.getMolten(144L))
            .duration(15 * SECONDS)
            .eut((int) TierEU.RECIPE_HV)
            .addTo(autoclaveRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Carbon, 64))
            .itemOutputs(GTModHandler.getIC2Item("carbonFiber", 32L))
            .outputChances(10000)
            .fluidInputs(Materials.Polytetrafluoroethylene.getMolten(288L))
            .duration(20 * SECONDS)
            .eut((int) TierEU.RECIPE_MV)
            .addTo(autoclaveRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Carbon, 64))
            .itemOutputs(GTModHandler.getIC2Item("carbonFiber", 16L))
            .outputChances(10000)
            .fluidInputs(Materials.Plastic.getMolten(576L))
            .duration(30 * SECONDS)
            .eut((int) TierEU.RECIPE_LV)
            .addTo(autoclaveRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.NetherStar, 1))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.gem, Materials.NetherStar, 1))
            .outputChances(3333)
            .fluidInputs(Materials.UUMatter.getFluid(576L))
            .duration(60 * MINUTES)
            .eut((int) TierEU.RECIPE_HV)
            .addTo(autoclaveRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(ItemList.QuantumStar.get(1L)))
            .itemOutputs(ItemList.Gravistar.get(1L))
            .outputChances(10000)
            .fluidInputs(Materials.Neutronium.getMolten(288))
            .duration(24 * SECONDS)
            .eut((int) TierEU.RECIPE_IV)
            .addTo(autoclaveRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(ItemList.Gravistar.get(16L)))
            .itemOutputs(ItemList.NuclearStar.get(1L))
            .outputChances(10000)
            .fluidInputs(Materials.Infinity.getMolten(288))
            .duration(24 * SECONDS)
            .eut(TierEU.RECIPE_UEV)
            .addTo(autoclaveRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.SiliconDioxide.getDust(1))
            .itemOutputs(Materials.Quartzite.getGems(1))
            .outputChances(750)
            .fluidInputs(Materials.Water.getFluid(200L))
            .duration(1 * MINUTES + 40 * SECONDS)
            .eut(24)
            .addTo(autoclaveRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.SiliconDioxide.getDust(1))
            .itemOutputs(Materials.Quartzite.getGems(1))
            .outputChances(1000)
            .fluidInputs(GTModHandler.getDistilledWater(100L))
            .duration(1 * MINUTES + 15 * SECONDS)
            .eut(24)
            .addTo(autoclaveRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.SiliconDioxide.getDust(1))
            .itemOutputs(Materials.Quartzite.getGems(1))
            .outputChances(10000)
            .fluidInputs(Materials.Void.getMolten(36L))
            .duration(50 * SECONDS)
            .eut(24)
            .addTo(autoclaveRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Silicon, 1),
                GTUtility.getIntegratedCircuit(1))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.SiliconDioxide, 3))
            .outputChances(7500)
            .fluidInputs(Materials.Water.getFluid(1000L))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(autoclaveRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Silicon, 1),
                GTUtility.getIntegratedCircuit(1))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.SiliconDioxide, 3))
            .outputChances(9000)
            .fluidInputs(GTModHandler.getDistilledWater(1000L))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(autoclaveRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.nanite, MaterialsUEVplus.TranscendentMetal, 1L),
                GTOreDictUnificator.get(OrePrefixes.dust, MaterialsUEVplus.Mellion, 32L))
            .itemOutputs(ItemList.Phononic_Seed_Crystal.get(8L))
            .fluidInputs(Materials.Grade8PurifiedWater.getFluid(32_000L))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_UMV)
            .addTo(autoclaveRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.round, MaterialsUEVplus.MagMatter, 1))
            .itemOutputs(ItemList.Phononic_Seed_Crystal.get(5))
            .fluidInputs(MaterialsUEVplus.PhononCrystalSolution.getFluid(250))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_UXV)
            .addTo(autoclaveRecipes);
    }
}
