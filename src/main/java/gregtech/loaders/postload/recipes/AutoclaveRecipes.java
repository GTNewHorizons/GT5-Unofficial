package gregtech.loaders.postload.recipes;

import static gregtech.api.enums.ModIDs.AppliedEnergistics2;
import static gregtech.api.util.GT_ModHandler.getModItem;
import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sAutoclaveRecipes;
import static gregtech.api.util.GT_RecipeBuilder.*;

import gregtech.api.enums.*;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;

public class AutoclaveRecipes implements Runnable {

    @Override
    public void run() {
        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.IC2_Energium_Dust.get(9L)
            )
            .itemOutputs(
                ItemList.IC2_EnergyCrystal.get(1L)
            )
            .outputChances(
                10000
            )
            .fluidInputs(
                Materials.EnergeticAlloy.getMolten(288)
            )
            .noFluidOutputs()
            .duration(30 * SECONDS)
            .eut(256)
            .addTo(sAutoclaveRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.IC2_Energium_Dust.get(9L)
            )
            .itemOutputs(
                ItemList.IC2_EnergyCrystal.get(1L)
            )
            .outputChances(
                10000
            )
            .fluidInputs(
                Materials.ConductiveIron.getMolten(576)
            )
            .noFluidOutputs()
            .duration(60 * SECONDS)
            .eut(256)
            .addTo(sAutoclaveRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                getModItem(AppliedEnergistics2.modID, "item.ItemCrystalSeed", 1L, 0)
            )
            .itemOutputs(
                getModItem(AppliedEnergistics2.modID, "item.ItemMultiMaterial", 1L, 10)
            )
            .outputChances(
                8000
            )
            .fluidInputs(
                Materials.Water.getFluid(200L)
            )
            .noFluidOutputs()
            .duration(1 * MINUTES + 40 * SECONDS)
            .eut(24)
            .addTo(sAutoclaveRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                getModItem(AppliedEnergistics2.modID, "item.ItemCrystalSeed", 1L, 600)
            )
            .itemOutputs(
                getModItem(AppliedEnergistics2.modID, "item.ItemMultiMaterial", 1L, 11)
            )
            .outputChances(
                8000
            )
            .fluidInputs(
                Materials.Water.getFluid(200L)
            )
            .noFluidOutputs()
            .duration(1 * MINUTES + 40 * SECONDS)
            .eut(24)
            .addTo(sAutoclaveRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                getModItem(AppliedEnergistics2.modID, "item.ItemCrystalSeed", 1L, 1200)
            )
            .itemOutputs(
                getModItem(AppliedEnergistics2.modID, "item.ItemMultiMaterial", 1L, 12)
            )
            .outputChances(
                8000
            )
            .fluidInputs(
                Materials.Water.getFluid(200L)
            )
            .noFluidOutputs()
            .duration(1 * MINUTES + 40 * SECONDS)
            .eut(24)
            .addTo(sAutoclaveRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                getModItem(AppliedEnergistics2.modID, "item.ItemCrystalSeed", 1L, 0)
            )
            .itemOutputs(
                getModItem(AppliedEnergistics2.modID, "item.ItemMultiMaterial", 1L, 10)
            )
            .outputChances(
                9000
            )
            .fluidInputs(
                GT_ModHandler.getDistilledWater(100L)
            )
            .noFluidOutputs()
            .duration(50 * SECONDS)
            .eut(24)
            .addTo(sAutoclaveRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                getModItem(AppliedEnergistics2.modID, "item.ItemCrystalSeed", 1L, 600)
            )
            .itemOutputs(
                getModItem(AppliedEnergistics2.modID, "item.ItemMultiMaterial", 1L, 11)
            )
            .outputChances(
                9000
            )
            .fluidInputs(
                GT_ModHandler.getDistilledWater(100L)
            )
            .noFluidOutputs()
            .duration(50 * SECONDS)
            .eut(24)
            .addTo(sAutoclaveRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                getModItem(AppliedEnergistics2.modID, "item.ItemCrystalSeed", 1L, 1200)
            )
            .itemOutputs(
                getModItem(AppliedEnergistics2.modID, "item.ItemMultiMaterial", 1L, 12)
            )
            .outputChances(
                9000
            )
            .fluidInputs(
                GT_ModHandler.getDistilledWater(100L)
            )
            .noFluidOutputs()
            .duration(50 * SECONDS)
            .eut(24)
            .addTo(sAutoclaveRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                getModItem(AppliedEnergistics2.modID, "item.ItemCrystalSeed", 1L, 0)
            )
            .itemOutputs(
                getModItem(AppliedEnergistics2.modID, "item.ItemMultiMaterial", 1L, 10)
            )
            .outputChances(
                10000
            )
            .fluidInputs(
                Materials.Void.getMolten(36L)
            )
            .noFluidOutputs()
            .duration(25 * SECONDS)
            .eut(24)
            .addTo(sAutoclaveRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                getModItem(AppliedEnergistics2.modID, "item.ItemCrystalSeed", 1L, 600)
            )
            .itemOutputs(
                getModItem(AppliedEnergistics2.modID, "item.ItemMultiMaterial", 1L, 11)
            )
            .outputChances(
                10000
            )
            .fluidInputs(
                Materials.Void.getMolten(36L)
            )
            .noFluidOutputs()
            .duration(25 * SECONDS)
            .eut(24)
            .addTo(sAutoclaveRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                getModItem(AppliedEnergistics2.modID, "item.ItemCrystalSeed", 1L, 1200)
            )
            .itemOutputs(
                getModItem(AppliedEnergistics2.modID, "item.ItemMultiMaterial", 1L, 12)
            )
            .outputChances(
                10000
            )
            .fluidInputs(
                Materials.Void.getMolten(36L)
            )
            .noFluidOutputs()
            .duration(25 * SECONDS)
            .eut(24)
            .addTo(sAutoclaveRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Carbon, 32)
            )
            .itemOutputs(
                GT_ModHandler.getIC2Item("carbonFiber", 64L)
            )
            .outputChances(
                10000
            )
            .fluidInputs(
                Materials.Polybenzimidazole.getMolten(36L)
            )
            .noFluidOutputs()
            .duration(7 * SECONDS + 10 * TICKS)
            .eut((int) TierEU.RECIPE_EV)
            .addTo(sAutoclaveRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Carbon, 64)
            )
            .itemOutputs(
                GT_ModHandler.getIC2Item("carbonFiber", 64L)
            )
            .outputChances(
                10000
            )
            .fluidInputs(
                Materials.Epoxid.getMolten(144L)
            )
            .noFluidOutputs()
            .duration(15 * SECONDS)
            .eut((int) TierEU.RECIPE_HV)
            .addTo(sAutoclaveRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Carbon, 64)
            )
            .itemOutputs(
                GT_ModHandler.getIC2Item("carbonFiber", 32L)
            )
            .outputChances(
                10000
            )
            .fluidInputs(
                Materials.Polytetrafluoroethylene.getMolten(288L)
            )
            .noFluidOutputs()
            .duration(20 * SECONDS)
            .eut((int) TierEU.RECIPE_MV)
            .addTo(sAutoclaveRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Carbon, 64)
            )
            .itemOutputs(
                GT_ModHandler.getIC2Item("carbonFiber", 16L)
            )
            .outputChances(
                10000
            )
            .fluidInputs(
                Materials.Plastic.getMolten(576L)
            )
            .noFluidOutputs()
            .duration(30 * SECONDS)
            .eut((int) TierEU.RECIPE_LV)
            .addTo(sAutoclaveRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.NetherStar, 1)
            )
            .itemOutputs(
                GT_OreDictUnificator.get(OrePrefixes.gem, Materials.NetherStar, 1)
            )
            .outputChances(
                3333
            )
            .fluidInputs(
                Materials.UUMatter.getFluid(576L)
            )
            .noFluidOutputs()
            .duration(60 * MINUTES)
            .eut((int) TierEU.RECIPE_HV)
            .addTo(sAutoclaveRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(ItemList.QuantumStar.get(1L))
            )
            .itemOutputs(
                ItemList.Gravistar.get(1L)
            )
            .outputChances(
                10000
            )
            .fluidInputs(
                Materials.Neutronium.getMolten(288)
            )
            .noFluidOutputs()
            .duration(24 * SECONDS)
            .eut((int) TierEU.RECIPE_IV)
            .addTo(sAutoclaveRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(ItemList.Gravistar.get(16L))
            )
            .itemOutputs(
                ItemList.NuclearStar.get(1L)
            )
            .outputChances(
                10000
            )
            .fluidInputs(
                Materials.Infinity.getMolten(288)
            )
            .noFluidOutputs()
            .duration(24 * SECONDS)
            .eut(TierEU.RECIPE_UEV)
            .addTo(sAutoclaveRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                Materials.SiliconDioxide.getDust(1)
            )
            .itemOutputs(
                Materials.Quartzite.getGems(1)
            )
            .outputChances(
                750
            )
            .fluidInputs(
                Materials.Water.getFluid(200L)
            )
            .noFluidOutputs()
            .duration(1 * MINUTES + 40 * SECONDS)
            .eut(24)
            .addTo(sAutoclaveRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                Materials.SiliconDioxide.getDust(1)
            )
            .itemOutputs(
                Materials.Quartzite.getGems(1)
            )
            .outputChances(
                1000
            )
            .fluidInputs(
                GT_ModHandler.getDistilledWater(100L)
            )
            .noFluidOutputs()
            .duration(1 * MINUTES + 15 * SECONDS)
            .eut(24)
            .addTo(sAutoclaveRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                Materials.SiliconDioxide.getDust(1)
            )
            .itemOutputs(
                Materials.Quartzite.getGems(1)
            )
            .outputChances(
                10000
            )
            .fluidInputs(
                Materials.Void.getMolten(36L)
            )
            .noFluidOutputs()
            .duration(50 * SECONDS)
            .eut(24)
            .addTo(sAutoclaveRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Silicon, 1),
                GT_Utility.getIntegratedCircuit(1)
            )
            .itemOutputs(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.SiliconDioxide, 3)
            )
            .outputChances(
                7500
            )
            .fluidInputs(
                Materials.Water.getFluid(1000L)
            )
            .noFluidOutputs()
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(sAutoclaveRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Silicon, 1),
                GT_Utility.getIntegratedCircuit(1)
            )
            .itemOutputs(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.SiliconDioxide, 3)
            )
            .outputChances(
                9000
            )
            .fluidInputs(
                GT_ModHandler.getDistilledWater(1000L)
            )
            .noFluidOutputs()
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(sAutoclaveRecipes);
    }
}
