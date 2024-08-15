package gregtech.loaders.postload.recipes;

import static gregtech.api.recipe.RecipeMaps.benderRecipes;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;
import static gregtech.api.util.GT_RecipeBuilder.TICKS;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;

public class BenderRecipes implements Runnable {

    @Override
    public void run() {
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Aluminium, 20L),
                GT_Utility.getIntegratedCircuit(10))
            .itemOutputs(ItemList.RC_Rail_Standard.get(64L))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(benderRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Iron, 48L),
                GT_Utility.getIntegratedCircuit(10))
            .itemOutputs(ItemList.RC_Rail_Standard.get(64L))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(benderRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.WroughtIron, 32L),
                GT_Utility.getIntegratedCircuit(10))
            .itemOutputs(ItemList.RC_Rail_Standard.get(64L))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(benderRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Bronze, 32L),
                GT_Utility.getIntegratedCircuit(10))
            .itemOutputs(ItemList.RC_Rail_Standard.get(64L))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(benderRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Steel, 24L),
                GT_Utility.getIntegratedCircuit(10))
            .itemOutputs(ItemList.RC_Rail_Standard.get(64L))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(benderRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.StainlessSteel, 16L),
                GT_Utility.getIntegratedCircuit(10))
            .itemOutputs(ItemList.RC_Rail_Standard.get(64L))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(benderRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Titanium, 12L),
                GT_Utility.getIntegratedCircuit(10))
            .itemOutputs(ItemList.RC_Rail_Standard.get(64L))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(benderRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.TungstenSteel, 9L),
                GT_Utility.getIntegratedCircuit(10))
            .itemOutputs(ItemList.RC_Rail_Standard.get(64L))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(benderRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Iridium, 6L),
                GT_Utility.getIntegratedCircuit(10))
            .itemOutputs(ItemList.RC_Rail_Standard.get(64L))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(benderRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Osmium, 3L),
                GT_Utility.getIntegratedCircuit(10))
            .itemOutputs(ItemList.RC_Rail_Standard.get(64L))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(benderRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Osmiridium, 2L),
                GT_Utility.getIntegratedCircuit(10))
            .itemOutputs(ItemList.RC_Rail_Standard.get(64L))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(benderRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Obsidian, 24L),
                GT_Utility.getIntegratedCircuit(11))
            .itemOutputs(ItemList.RC_Rail_Reinforced.get(64L))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(benderRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.StainlessSteel, 12L),
                GT_Utility.getIntegratedCircuit(11))
            .itemOutputs(ItemList.RC_Rail_Reinforced.get(64L))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(benderRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.TungstenSteel, 6L),
                GT_Utility.getIntegratedCircuit(11))
            .itemOutputs(ItemList.RC_Rail_Reinforced.get(64L))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(benderRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Iridium, 3L),
                GT_Utility.getIntegratedCircuit(11))
            .itemOutputs(ItemList.RC_Rail_Reinforced.get(64L))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(benderRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Osmium, 1L),
                GT_Utility.getIntegratedCircuit(11))
            .itemOutputs(ItemList.RC_Rail_Reinforced.get(64L))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(benderRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Aluminium, 20L),
                GT_Utility.getIntegratedCircuit(12))
            .itemOutputs(ItemList.RC_Rebar.get(64L))
            .duration(10 * SECONDS)
            .eut(200)
            .addTo(benderRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Iron, 48L),
                GT_Utility.getIntegratedCircuit(12))
            .itemOutputs(ItemList.RC_Rebar.get(64L))
            .duration(10 * SECONDS)
            .eut(200)
            .addTo(benderRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.WroughtIron, 24L),
                GT_Utility.getIntegratedCircuit(12))
            .itemOutputs(ItemList.RC_Rebar.get(64L))
            .duration(10 * SECONDS)
            .eut(200)
            .addTo(benderRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Bronze, 32L),
                GT_Utility.getIntegratedCircuit(12))
            .itemOutputs(ItemList.RC_Rebar.get(64L))
            .duration(10 * SECONDS)
            .eut(200)
            .addTo(benderRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Steel, 16L),
                GT_Utility.getIntegratedCircuit(12))
            .itemOutputs(ItemList.RC_Rebar.get(64L))
            .duration(10 * SECONDS)
            .eut(200)
            .addTo(benderRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.StainlessSteel, 12L),
                GT_Utility.getIntegratedCircuit(12))
            .itemOutputs(ItemList.RC_Rebar.get(64L))
            .duration(10 * SECONDS)
            .eut(200)
            .addTo(benderRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Titanium, 8),
                GT_Utility.getIntegratedCircuit(12))
            .itemOutputs(ItemList.RC_Rebar.get(64L))
            .duration(10 * SECONDS)
            .eut(200)
            .addTo(benderRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.TungstenSteel, 6L),
                GT_Utility.getIntegratedCircuit(12))
            .itemOutputs(ItemList.RC_Rebar.get(64L))
            .duration(10 * SECONDS)
            .eut(200)
            .addTo(benderRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Iridium, 4L),
                GT_Utility.getIntegratedCircuit(12))
            .itemOutputs(ItemList.RC_Rebar.get(64L))
            .duration(10 * SECONDS)
            .eut(200)
            .addTo(benderRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Osmium, 2L),
                GT_Utility.getIntegratedCircuit(12))
            .itemOutputs(ItemList.RC_Rebar.get(64L))
            .duration(10 * SECONDS)
            .eut(200)
            .addTo(benderRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.IC2_Mixed_Metal_Ingot.get(1L), GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.plateAlloy, Materials.Advanced, 1L))
            .duration(5 * SECONDS)
            .eut(8)
            .addTo(benderRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Tin, 2L),
                GT_Utility.getIntegratedCircuit(12))
            .itemOutputs(ItemList.Cell_Empty.get(1L))
            .duration(10 * SECONDS)
            .eut(8)
            .addTo(benderRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Steel, 1L),
                GT_Utility.getIntegratedCircuit(12))
            .itemOutputs(ItemList.Cell_Empty.get(1L))
            .duration(5 * SECONDS)
            .eut(8)
            .addTo(benderRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Polytetrafluoroethylene, 1L),
                GT_Utility.getIntegratedCircuit(12))
            .itemOutputs(ItemList.Cell_Empty.get(4L))
            .duration(5 * SECONDS)
            .eut(8)
            .addTo(benderRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Iron, 3L),
                GT_Utility.getIntegratedCircuit(12))
            .itemOutputs(new ItemStack(Items.bucket, 1, 0))
            .duration(10 * SECONDS)
            .eut(4)
            .addTo(benderRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.WroughtIron, 3L),
                GT_Utility.getIntegratedCircuit(12))
            .itemOutputs(new ItemStack(Items.bucket, 1, 0))
            .duration(10 * SECONDS)
            .eut(4)
            .addTo(benderRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.itemCasing, Materials.Iron, 2L),
                GT_Utility.getIntegratedCircuit(2))
            .itemOutputs(GT_ModHandler.getIC2Item("fuelRod", 1L))
            .duration(5 * SECONDS)
            .eut(8)
            .addTo(benderRecipes);

        if (GT_OreDictUnificator.get(OrePrefixes.itemCasing, Materials.Tin, 1L) != null) {
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_OreDictUnificator.get(OrePrefixes.itemCasing, Materials.Tin, 1L),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(ItemList.IC2_Food_Can_Empty.get(1L))
                .duration(20 * TICKS)
                .eut((int) TierEU.RECIPE_HV)
                .addTo(benderRecipes);
        }

    }
}
