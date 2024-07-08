package com.github.technus.tectech.loader.recipe;

import com.github.technus.tectech.thing.CustomItemList;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_OreDictUnificator;

import static gregtech.api.recipe.RecipeMaps.extractorRecipes;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;

public class Extractor implements Runnable {

    @Override
    public void run() {
        // LV Tesla Capacitor
        GT_Values.RA.stdBuilder()
            .itemInputs(
                CustomItemList.teslaCapacitor.getWithDamage(1, 0)
            )
            .itemOutputs(
                GT_OreDictUnificator.get(OrePrefixes.itemCasing, Materials.BatteryAlloy, 4)
            )
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(extractorRecipes);
        // MV Tesla Capacitor
        GT_Values.RA.stdBuilder()
            .itemInputs(
                CustomItemList.teslaCapacitor.getWithDamage(1, 1)
            )
            .itemOutputs(
                GT_OreDictUnificator.get(OrePrefixes.itemCasing, Materials.BatteryAlloy, 6)
            )
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(extractorRecipes);
        // HV Tesla Capacitor
        GT_Values.RA.stdBuilder()
            .itemInputs(
                CustomItemList.teslaCapacitor.getWithDamage(1, 2)
            )
            .itemOutputs(
                GT_OreDictUnificator.get(OrePrefixes.itemCasing, Materials.BatteryAlloy, 8)
            )
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(extractorRecipes);
        // EV Tesla Capacitor
        GT_Values.RA.stdBuilder()
            .itemInputs(
                CustomItemList.teslaCapacitor.getWithDamage(1, 3)
            )
            .itemOutputs(
                GT_OreDictUnificator.get(OrePrefixes.itemCasing, Materials.BatteryAlloy, 10)
            )
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(extractorRecipes);
        // IV Tesla Capacitor
        GT_Values.RA.stdBuilder()
            .itemInputs(
                CustomItemList.teslaCapacitor.getWithDamage(1, 4)
            )
            .itemOutputs(
                GT_OreDictUnificator.get(OrePrefixes.itemCasing, Materials.BatteryAlloy, 12)
            )
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(extractorRecipes);
        // LuV Tesla Capacitor
        GT_Values.RA.stdBuilder()
            .itemInputs(
                CustomItemList.teslaCapacitor.getWithDamage(1, 5)
            )
            .itemOutputs(
                GT_OreDictUnificator.get(OrePrefixes.itemCasing, Materials.BatteryAlloy, 14)
            )
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(extractorRecipes);
        // ZPM Tesla Capacitor
        GT_Values.RA.stdBuilder()
            .itemInputs(
                CustomItemList.teslaCapacitor.getWithDamage(1, 6)
            )
            .itemOutputs(
                GT_OreDictUnificator.get(OrePrefixes.itemCasing, Materials.BatteryAlloy, 16)
            )
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(extractorRecipes);

    }
}
