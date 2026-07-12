package tectech.loader.recipe;

import static gregtech.api.recipe.RecipeMaps.extractorRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.enums.materials2.Materials2Shapes;
import tectech.thing.CustomItemList;

public class Extractor implements Runnable {

    @Override
    public void run() {
        // LV Tesla Capacitor
        GTValues.RA.stdBuilder()
            .itemInputs(CustomItemList.teslaCapacitor.getWithDamage(1, 0))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.BatteryAlloy, Materials2Shapes.itemCasing, 4))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(extractorRecipes);
        // MV Tesla Capacitor
        GTValues.RA.stdBuilder()
            .itemInputs(CustomItemList.teslaCapacitor.getWithDamage(1, 1))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.BatteryAlloy, Materials2Shapes.itemCasing, 6))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(extractorRecipes);
        // HV Tesla Capacitor
        GTValues.RA.stdBuilder()
            .itemInputs(CustomItemList.teslaCapacitor.getWithDamage(1, 2))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.BatteryAlloy, Materials2Shapes.itemCasing, 8))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(extractorRecipes);
        // EV Tesla Capacitor
        GTValues.RA.stdBuilder()
            .itemInputs(CustomItemList.teslaCapacitor.getWithDamage(1, 3))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.BatteryAlloy, Materials2Shapes.itemCasing, 10))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(extractorRecipes);
        // IV Tesla Capacitor
        GTValues.RA.stdBuilder()
            .itemInputs(CustomItemList.teslaCapacitor.getWithDamage(1, 4))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.BatteryAlloy, Materials2Shapes.itemCasing, 12))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(extractorRecipes);
        // LuV Tesla Capacitor
        GTValues.RA.stdBuilder()
            .itemInputs(CustomItemList.teslaCapacitor.getWithDamage(1, 5))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.BatteryAlloy, Materials2Shapes.itemCasing, 14))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(extractorRecipes);
        // ZPM Tesla Capacitor
        GTValues.RA.stdBuilder()
            .itemInputs(CustomItemList.teslaCapacitor.getWithDamage(1, 6))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.BatteryAlloy, Materials2Shapes.itemCasing, 16))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(extractorRecipes);

    }
}
