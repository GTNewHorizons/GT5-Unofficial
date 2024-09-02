package gregtech.loaders.postload.recipes;

import static gregtech.api.enums.Mods.IndustrialCraft2;
import static gregtech.api.recipe.RecipeMaps.cannerRecipes;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;

public class CannerRecipes implements Runnable {

    @Override
    public void run() {

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Cadmium, 2L),
                ItemList.Battery_Hull_LV.get(1L))
            .itemOutputs(ItemList.Battery_RE_LV_Cadmium.get(1L))
            .duration(5 * SECONDS)
            .eut(2)
            .addTo(cannerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Lithium, 2L),
                ItemList.Battery_Hull_LV.get(1L))
            .itemOutputs(ItemList.Battery_RE_LV_Lithium.get(1L))
            .duration(5 * SECONDS)
            .eut(2)
            .addTo(cannerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Sodium, 2L),
                ItemList.Battery_Hull_LV.get(1L))
            .itemOutputs(ItemList.Battery_RE_LV_Sodium.get(1L))
            .duration(5 * SECONDS)
            .eut(2)
            .addTo(cannerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Cadmium, 8L),
                ItemList.Battery_Hull_MV.get(1L))
            .itemOutputs(ItemList.Battery_RE_MV_Cadmium.get(1L))
            .duration(20 * SECONDS)
            .eut(2)
            .addTo(cannerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Lithium, 8L),
                ItemList.Battery_Hull_MV.get(1L))
            .itemOutputs(ItemList.Battery_RE_MV_Lithium.get(1L))
            .duration(20 * SECONDS)
            .eut(2)
            .addTo(cannerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Sodium, 8L),
                ItemList.Battery_Hull_MV.get(1L))
            .itemOutputs(ItemList.Battery_RE_MV_Sodium.get(1L))
            .duration(20 * SECONDS)
            .eut(2)
            .addTo(cannerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Cadmium, 32L),
                ItemList.Battery_Hull_HV.get(1L))
            .itemOutputs(ItemList.Battery_RE_HV_Cadmium.get(1L))
            .duration(1 * MINUTES + 20 * SECONDS)
            .eut(2)
            .addTo(cannerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Lithium, 32L),
                ItemList.Battery_Hull_HV.get(1L))
            .itemOutputs(ItemList.Battery_RE_HV_Lithium.get(1L))
            .duration(1 * MINUTES + 20 * SECONDS)
            .eut(2)
            .addTo(cannerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Sodium, 32L),
                ItemList.Battery_Hull_HV.get(1L))
            .itemOutputs(ItemList.Battery_RE_HV_Sodium.get(1L))
            .duration(1 * MINUTES + 20 * SECONDS)
            .eut(2)
            .addTo(cannerRecipes);
        // Recipes to actually fill the empty hulls with content
        // IV 2048

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Sunnarium, 4L),
                ItemList.BatteryHull_EV.get(1L))
            .itemOutputs(ItemList.BatteryHull_EV_Full.get(1L))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(cannerRecipes);
        // EV 8192

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Sunnarium, 16L),
                ItemList.BatteryHull_IV.get(1L))
            .itemOutputs(ItemList.BatteryHull_IV_Full.get(1L))
            .duration(7 * SECONDS + 10 * TICKS)
            .eut(1024)
            .addTo(cannerRecipes);
        // LuV 32768

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Sunnarium, 32L),
                ItemList.BatteryHull_LuV.get(1L))
            .itemOutputs(ItemList.BatteryHull_LuV_Full.get(1L))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(cannerRecipes);
        // ZPM 131072

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Naquadria, 16L),
                ItemList.BatteryHull_ZPM.get(1L))
            .itemOutputs(ItemList.BatteryHull_ZPM_Full.get(1L))
            .duration(12 * SECONDS + 10 * TICKS)
            .eut(4096)
            .addTo(cannerRecipes);
        // UV 524288

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Naquadria, 32L),
                ItemList.BatteryHull_UV.get(1L))
            .itemOutputs(ItemList.BatteryHull_UV_Full.get(1L))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(cannerRecipes);
        // UHV 2097152

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Neutronium, 16L),
                ItemList.BatteryHull_UHV.get(1L))
            .itemOutputs(ItemList.BatteryHull_UHV_Full.get(1L))
            .duration(17 * SECONDS + 10 * TICKS)
            .eut(15720)
            .addTo(cannerRecipes);
        // UEV 8388608

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Neutronium, 32L),
                ItemList.BatteryHull_UEV.get(1L))
            .itemOutputs(ItemList.BatteryHull_UEV_Full.get(1L))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(cannerRecipes);
        // UIV 33554432

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Neutronium, 64L),
                ItemList.BatteryHull_UIV.get(1L))
            .itemOutputs(ItemList.BatteryHull_UIV_Full.get(1L))
            .duration(22 * SECONDS + 10 * TICKS)
            .eut(62880)
            .addTo(cannerRecipes);
        // UMV 134217728

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Infinity, 4L),
                ItemList.BatteryHull_UMV.get(1L))
            .itemOutputs(ItemList.BatteryHull_UMV_Full.get(1L))
            .duration(25 * SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .addTo(cannerRecipes);
        // UxV 536870912

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Infinity, 8L),
                ItemList.BatteryHull_UxV.get(1L))
            .itemOutputs(ItemList.BatteryHull_UxV_Full.get(1L))
            .duration(30 * SECONDS)
            .eut(251520)
            .addTo(cannerRecipes);

        // fuel rod canner recipes

        if (IndustrialCraft2.isModLoaded()) {
            // todo: remove tiny dust in this recipe
            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTModHandler.getIC2Item("fuelRod", 1),
                    GTOreDictUnificator.get(OrePrefixes.dustTiny, Materials.Lithium, 1L))
                .itemOutputs(GTModHandler.getIC2Item("reactorLithiumCell", 1, 1))
                .duration(16 * TICKS)
                .eut(64)
                .addTo(cannerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTModHandler.getIC2Item("fuelRod", 1),
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.Thorium, 3))
                .itemOutputs(ItemList.ThoriumCell_1.get(1L))
                .duration(1 * SECONDS + 10 * TICKS)
                .eut(16)
                .addTo(cannerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(GTModHandler.getIC2Item("fuelRod", 1), GTModHandler.getIC2Item("UranFuel", 1))
                .itemOutputs(ItemList.Uraniumcell_1.get(1))
                .duration(1 * SECONDS + 10 * TICKS)
                .eut(16)
                .addTo(cannerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(GTModHandler.getIC2Item("fuelRod", 1), GTModHandler.getIC2Item("MOXFuel", 1))
                .itemOutputs(ItemList.Moxcell_1.get(1))
                .duration(1 * SECONDS + 10 * TICKS)
                .eut(16)
                .addTo(cannerRecipes);
        }

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Large_Fluid_Cell_TungstenSteel.get(1L),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.NaquadahEnriched, 3))
            .itemOutputs(ItemList.NaquadahCell_1.get(1L))
            .duration(1 * SECONDS + 10 * TICKS)
            .eut(16)
            .addTo(cannerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Large_Fluid_Cell_TungstenSteel.get(1L),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Naquadria, 3))
            .itemOutputs(ItemList.MNqCell_1.get(1L))
            .duration(1 * SECONDS + 10 * TICKS)
            .eut(16)
            .addTo(cannerRecipes);
    }
}
