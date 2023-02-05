package gregtech.loaders.postload.recipes;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import gregtech.api.enums.*;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;

public class BenderRecipes implements Runnable {

    @Override
    public void run() {
        GT_Values.RA.addBenderRecipe(
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Aluminium, 20L),
                (GT_Utility.getIntegratedCircuit(10)),
                ItemList.RC_Rail_Standard.get(64L),
                300,
                (int) TierEU.RECIPE_LV);
        GT_Values.RA.addBenderRecipe(
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Iron, 48L),
                (GT_Utility.getIntegratedCircuit(10)),
                ItemList.RC_Rail_Standard.get(64L),
                300,
                (int) TierEU.RECIPE_LV);
        GT_Values.RA.addBenderRecipe(
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.WroughtIron, 32L),
                (GT_Utility.getIntegratedCircuit(10)),
                ItemList.RC_Rail_Standard.get(64L),
                300,
                (int) TierEU.RECIPE_LV);
        GT_Values.RA.addBenderRecipe(
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Bronze, 32L),
                (GT_Utility.getIntegratedCircuit(10)),
                ItemList.RC_Rail_Standard.get(64L),
                300,
                (int) TierEU.RECIPE_LV);
        GT_Values.RA.addBenderRecipe(
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Steel, 24L),
                (GT_Utility.getIntegratedCircuit(10)),
                ItemList.RC_Rail_Standard.get(64L),
                300,
                (int) TierEU.RECIPE_LV);
        GT_Values.RA.addBenderRecipe(
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.StainlessSteel, 16L),
                (GT_Utility.getIntegratedCircuit(10)),
                ItemList.RC_Rail_Standard.get(64L),
                300,
                (int) TierEU.RECIPE_LV);
        GT_Values.RA.addBenderRecipe(
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Titanium, 12L),
                (GT_Utility.getIntegratedCircuit(10)),
                ItemList.RC_Rail_Standard.get(64L),
                300,
                (int) TierEU.RECIPE_LV);
        GT_Values.RA.addBenderRecipe(
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.TungstenSteel, 9L),
                (GT_Utility.getIntegratedCircuit(10)),
                ItemList.RC_Rail_Standard.get(64L),
                300,
                (int) TierEU.RECIPE_LV);
        GT_Values.RA.addBenderRecipe(
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Iridium, 6L),
                (GT_Utility.getIntegratedCircuit(10)),
                ItemList.RC_Rail_Standard.get(64L),
                300,
                (int) TierEU.RECIPE_LV);
        GT_Values.RA.addBenderRecipe(
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Osmium, 3L),
                (GT_Utility.getIntegratedCircuit(10)),
                ItemList.RC_Rail_Standard.get(64L),
                300,
                (int) TierEU.RECIPE_LV);
        GT_Values.RA.addBenderRecipe(
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Osmiridium, 2L),
                (GT_Utility.getIntegratedCircuit(10)),
                ItemList.RC_Rail_Standard.get(64L),
                300,
                (int) TierEU.RECIPE_LV);
        GT_Values.RA.addBenderRecipe(
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Obsidian, 24L),
                (GT_Utility.getIntegratedCircuit(11)),
                ItemList.RC_Rail_Reinforced.get(64L),
                600,
                (int) TierEU.RECIPE_LV);
        GT_Values.RA.addBenderRecipe(
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.StainlessSteel, 12L),
                (GT_Utility.getIntegratedCircuit(11)),
                ItemList.RC_Rail_Reinforced.get(64L),
                600,
                (int) TierEU.RECIPE_LV);
        GT_Values.RA.addBenderRecipe(
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.TungstenSteel, 6L),
                (GT_Utility.getIntegratedCircuit(11)),
                ItemList.RC_Rail_Reinforced.get(64L),
                600,
                (int) TierEU.RECIPE_LV);
        GT_Values.RA.addBenderRecipe(
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Iridium, 3L),
                (GT_Utility.getIntegratedCircuit(11)),
                ItemList.RC_Rail_Reinforced.get(64L),
                600,
                (int) TierEU.RECIPE_LV);
        GT_Values.RA.addBenderRecipe(
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Osmium, 1L),
                (GT_Utility.getIntegratedCircuit(11)),
                ItemList.RC_Rail_Reinforced.get(64L),
                600,
                (int) TierEU.RECIPE_LV);
        GT_Values.RA.addBenderRecipe(
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Aluminium, 20L),
                (GT_Utility.getIntegratedCircuit(12)),
                ItemList.RC_Rebar.get(64L),
                200,
                15);
        GT_Values.RA.addBenderRecipe(
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Iron, 48L),
                (GT_Utility.getIntegratedCircuit(12)),
                ItemList.RC_Rebar.get(64L),
                200,
                15);
        GT_Values.RA.addBenderRecipe(
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.WroughtIron, 24L),
                (GT_Utility.getIntegratedCircuit(12)),
                ItemList.RC_Rebar.get(64L),
                200,
                15);
        GT_Values.RA.addBenderRecipe(
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Bronze, 32L),
                (GT_Utility.getIntegratedCircuit(12)),
                ItemList.RC_Rebar.get(64L),
                200,
                15);
        GT_Values.RA.addBenderRecipe(
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Steel, 16L),
                (GT_Utility.getIntegratedCircuit(12)),
                ItemList.RC_Rebar.get(64L),
                200,
                15);
        GT_Values.RA.addBenderRecipe(
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.StainlessSteel, 12L),
                (GT_Utility.getIntegratedCircuit(12)),
                ItemList.RC_Rebar.get(64L),
                200,
                15);
        GT_Values.RA.addBenderRecipe(
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Titanium, 8),
                (GT_Utility.getIntegratedCircuit(12)),
                ItemList.RC_Rebar.get(64L),
                200,
                15);
        GT_Values.RA.addBenderRecipe(
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.TungstenSteel, 6L),
                (GT_Utility.getIntegratedCircuit(12)),
                ItemList.RC_Rebar.get(64L),
                200,
                15);
        GT_Values.RA.addBenderRecipe(
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Iridium, 4L),
                (GT_Utility.getIntegratedCircuit(12)),
                ItemList.RC_Rebar.get(64L),
                200,
                15);
        GT_Values.RA.addBenderRecipe(
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Osmium, 2L),
                (GT_Utility.getIntegratedCircuit(12)),
                ItemList.RC_Rebar.get(64L),
                200,
                15);

        GT_Values.RA.addBenderRecipe(
                ItemList.IC2_Mixed_Metal_Ingot.get(1L),
                GT_OreDictUnificator.get(OrePrefixes.plateAlloy, Materials.Advanced, 1L),
                100,
                8);

        // cell, bucket, food can
        GT_Values.RA.addBenderRecipe(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Tin, 12L),
                ItemList.Cell_Empty.get(6L),
                1200,
                8);
        GT_Values.RA.addBenderRecipe(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Steel, 12L),
                ItemList.Cell_Empty.get(12L),
                1200,
                8);
        GT_Values.RA.addBenderRecipe(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Polytetrafluoroethylene, 12L),
                ItemList.Cell_Empty.get(48L),
                1200,
                8);
        GT_Values.RA.addBenderRecipe(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Iron, 12L),
                new ItemStack(Items.bucket, 4, 0),
                800,
                4);
        GT_Values.RA.addBenderRecipe(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.WroughtIron, 12L),
                new ItemStack(Items.bucket, 4, 0),
                800,
                4);
        GT_Values.RA.addBenderRecipe(
                GT_OreDictUnificator.get(OrePrefixes.itemCasing, Materials.Iron, 2L),
                GT_ModHandler.getIC2Item("fuelRod", 1L),
                100,
                8);
        GT_Values.RA.addBenderRecipe(
                GT_OreDictUnificator.get(OrePrefixes.itemCasing, Materials.Tin, 1L),
                ItemList.IC2_Food_Can_Empty.get(1L),
                20,
                (int) TierEU.RECIPE_HV);
    }
}
