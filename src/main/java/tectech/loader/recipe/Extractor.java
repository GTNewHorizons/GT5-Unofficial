package tectech.loader.recipe;

import static gregtech.api.recipe.RecipeMaps.extractorRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GTOreDictUnificator;
import tectech.thing.CustomItemList;

public class Extractor implements Runnable {

    @Override
    public void run() {
        // LV Tesla Capacitor
        GTValues.RA.stdBuilder()
            .itemInputs(CustomItemList.teslaCapacitor.getWithDamage(1, 0))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.itemCasing, Materials.BatteryAlloy, 4))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(extractorRecipes);
        // MV Tesla Capacitor
        GTValues.RA.stdBuilder()
            .itemInputs(CustomItemList.teslaCapacitor.getWithDamage(1, 1))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.itemCasing, Materials.BatteryAlloy, 6))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(extractorRecipes);
        // HV Tesla Capacitor
        GTValues.RA.stdBuilder()
            .itemInputs(CustomItemList.teslaCapacitor.getWithDamage(1, 2))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.itemCasing, Materials.BatteryAlloy, 8))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(extractorRecipes);
        // EV Tesla Capacitor
        GTValues.RA.stdBuilder()
            .itemInputs(CustomItemList.teslaCapacitor.getWithDamage(1, 3))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.itemCasing, Materials.BatteryAlloy, 10))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(extractorRecipes);
        // IV Tesla Capacitor
        GTValues.RA.stdBuilder()
            .itemInputs(CustomItemList.teslaCapacitor.getWithDamage(1, 4))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.itemCasing, Materials.BatteryAlloy, 12))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(extractorRecipes);
        // LuV Tesla Capacitor
        GTValues.RA.stdBuilder()
            .itemInputs(CustomItemList.teslaCapacitor.getWithDamage(1, 5))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.itemCasing, Materials.BatteryAlloy, 14))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(extractorRecipes);
        // ZPM Tesla Capacitor
        GTValues.RA.stdBuilder()
            .itemInputs(CustomItemList.teslaCapacitor.getWithDamage(1, 6))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.itemCasing, Materials.BatteryAlloy, 16))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(extractorRecipes);

    }
}
