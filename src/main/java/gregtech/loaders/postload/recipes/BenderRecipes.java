package gregtech.loaders.postload.recipes;

import static gregtech.api.recipe.RecipeMaps.benderRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import goodgenerator.items.GGMaterial;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;

public class BenderRecipes implements Runnable {

    @Override
    public void run() {
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.stick, Materials.Aluminium, 20L),
                GTUtility.getIntegratedCircuit(10))
            .itemOutputs(ItemList.RC_Rail_Standard.get(64L))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(benderRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.stick, Materials.Iron, 48L),
                GTUtility.getIntegratedCircuit(10))
            .itemOutputs(ItemList.RC_Rail_Standard.get(64L))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(benderRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.stick, Materials.WroughtIron, 32L),
                GTUtility.getIntegratedCircuit(10))
            .itemOutputs(ItemList.RC_Rail_Standard.get(64L))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(benderRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.stick, Materials.Bronze, 32L),
                GTUtility.getIntegratedCircuit(10))
            .itemOutputs(ItemList.RC_Rail_Standard.get(64L))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(benderRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.stick, Materials.Steel, 24L),
                GTUtility.getIntegratedCircuit(10))
            .itemOutputs(ItemList.RC_Rail_Standard.get(64L))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(benderRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.stick, Materials.StainlessSteel, 16L),
                GTUtility.getIntegratedCircuit(10))
            .itemOutputs(ItemList.RC_Rail_Standard.get(64L))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(benderRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.stick, Materials.Titanium, 12L),
                GTUtility.getIntegratedCircuit(10))
            .itemOutputs(ItemList.RC_Rail_Standard.get(64L))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(benderRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.stick, Materials.TungstenSteel, 9L),
                GTUtility.getIntegratedCircuit(10))
            .itemOutputs(ItemList.RC_Rail_Standard.get(64L))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(benderRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.stick, Materials.Iridium, 6L),
                GTUtility.getIntegratedCircuit(10))
            .itemOutputs(ItemList.RC_Rail_Standard.get(64L))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(benderRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.stick, Materials.Osmium, 3L),
                GTUtility.getIntegratedCircuit(10))
            .itemOutputs(ItemList.RC_Rail_Standard.get(64L))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(benderRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.stick, Materials.Osmiridium, 2L),
                GTUtility.getIntegratedCircuit(10))
            .itemOutputs(ItemList.RC_Rail_Standard.get(64L))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(benderRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.stick, Materials.Obsidian, 24L),
                GTUtility.getIntegratedCircuit(11))
            .itemOutputs(ItemList.RC_Rail_Reinforced.get(64L))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(benderRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.stick, Materials.StainlessSteel, 12L),
                GTUtility.getIntegratedCircuit(11))
            .itemOutputs(ItemList.RC_Rail_Reinforced.get(64L))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(benderRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.stick, Materials.TungstenSteel, 6L),
                GTUtility.getIntegratedCircuit(11))
            .itemOutputs(ItemList.RC_Rail_Reinforced.get(64L))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(benderRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.stick, Materials.Iridium, 3L),
                GTUtility.getIntegratedCircuit(11))
            .itemOutputs(ItemList.RC_Rail_Reinforced.get(64L))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(benderRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.stick, Materials.Osmium, 1L),
                GTUtility.getIntegratedCircuit(11))
            .itemOutputs(ItemList.RC_Rail_Reinforced.get(64L))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(benderRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.stick, Materials.Aluminium, 20L),
                GTUtility.getIntegratedCircuit(12))
            .itemOutputs(ItemList.RC_Rebar.get(64L))
            .duration(10 * SECONDS)
            .eut(200)
            .addTo(benderRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.stick, Materials.Iron, 48L),
                GTUtility.getIntegratedCircuit(12))
            .itemOutputs(ItemList.RC_Rebar.get(64L))
            .duration(10 * SECONDS)
            .eut(200)
            .addTo(benderRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.stick, Materials.WroughtIron, 24L),
                GTUtility.getIntegratedCircuit(12))
            .itemOutputs(ItemList.RC_Rebar.get(64L))
            .duration(10 * SECONDS)
            .eut(200)
            .addTo(benderRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.stick, Materials.Bronze, 32L),
                GTUtility.getIntegratedCircuit(12))
            .itemOutputs(ItemList.RC_Rebar.get(64L))
            .duration(10 * SECONDS)
            .eut(200)
            .addTo(benderRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.stick, Materials.Steel, 16L),
                GTUtility.getIntegratedCircuit(12))
            .itemOutputs(ItemList.RC_Rebar.get(64L))
            .duration(10 * SECONDS)
            .eut(200)
            .addTo(benderRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.stick, Materials.StainlessSteel, 12L),
                GTUtility.getIntegratedCircuit(12))
            .itemOutputs(ItemList.RC_Rebar.get(64L))
            .duration(10 * SECONDS)
            .eut(200)
            .addTo(benderRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.stick, Materials.Titanium, 8),
                GTUtility.getIntegratedCircuit(12))
            .itemOutputs(ItemList.RC_Rebar.get(64L))
            .duration(10 * SECONDS)
            .eut(200)
            .addTo(benderRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.stick, Materials.TungstenSteel, 6L),
                GTUtility.getIntegratedCircuit(12))
            .itemOutputs(ItemList.RC_Rebar.get(64L))
            .duration(10 * SECONDS)
            .eut(200)
            .addTo(benderRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.stick, Materials.Iridium, 4L),
                GTUtility.getIntegratedCircuit(12))
            .itemOutputs(ItemList.RC_Rebar.get(64L))
            .duration(10 * SECONDS)
            .eut(200)
            .addTo(benderRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.stick, Materials.Osmium, 2L),
                GTUtility.getIntegratedCircuit(12))
            .itemOutputs(ItemList.RC_Rebar.get(64L))
            .duration(10 * SECONDS)
            .eut(200)
            .addTo(benderRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.IC2_Mixed_Metal_Ingot.get(1L), GTUtility.getIntegratedCircuit(1))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.plateAlloy, Materials.Advanced, 1L))
            .duration(5 * SECONDS)
            .eut(8)
            .addTo(benderRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Tin, 2L),
                GTUtility.getIntegratedCircuit(12))
            .itemOutputs(ItemList.Cell_Empty.get(1L))
            .duration(10 * SECONDS)
            .eut(8)
            .addTo(benderRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Steel, 1L),
                GTUtility.getIntegratedCircuit(12))
            .itemOutputs(ItemList.Cell_Empty.get(1L))
            .duration(5 * SECONDS)
            .eut(8)
            .addTo(benderRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Polytetrafluoroethylene, 1L),
                GTUtility.getIntegratedCircuit(12))
            .itemOutputs(ItemList.Cell_Empty.get(4L))
            .duration(5 * SECONDS)
            .eut(8)
            .addTo(benderRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Iron, 3L),
                GTUtility.getIntegratedCircuit(12))
            .itemOutputs(new ItemStack(Items.bucket, 1, 0))
            .duration(10 * SECONDS)
            .eut(4)
            .addTo(benderRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.WroughtIron, 3L),
                GTUtility.getIntegratedCircuit(12))
            .itemOutputs(new ItemStack(Items.bucket, 1, 0))
            .duration(10 * SECONDS)
            .eut(4)
            .addTo(benderRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.itemCasing, Materials.Iron, 2L),
                GTUtility.getIntegratedCircuit(2))
            .itemOutputs(GTModHandler.getIC2Item("fuelRod", 1L))
            .duration(5 * SECONDS)
            .eut(8)
            .addTo(benderRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GGMaterial.tairitsu.get(OrePrefixes.ingot, 9), GTUtility.getIntegratedCircuit(9))
            .itemOutputs(GGMaterial.tairitsu.get(OrePrefixes.plateDense, 1))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .addTo(benderRecipes);

        if (GTOreDictUnificator.get(OrePrefixes.itemCasing, Materials.Tin, 1L) != null) {
            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.itemCasing, Materials.Tin, 1L),
                    GTUtility.getIntegratedCircuit(1))
                .itemOutputs(ItemList.IC2_Food_Can_Empty.get(1L))
                .duration(20 * TICKS)
                .eut((int) TierEU.RECIPE_HV)
                .addTo(benderRecipes);
        }

    }
}
