package gregtech.loaders.postload.recipes;

import static gregtech.api.recipe.RecipeMaps.solarFactoryRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.TierEU;
import gregtech.api.recipe.metadata.SolarFactoryKey;

// If you want to make a new recipe for Solar Factory, you'll need to make a hidden one and a fake one. This is so we
// can display the minimum wafer in NEI without having problems with the Solar Factory processing logic.

// Recipe specialValue should correspond to the stack size of the wafer input.
// Recipe metadata serves dual purpose, in the fake recipes it displays the minimum tier in NEI, and in the hidden
// recipes it gives the multi the minimum wafer tier for processing logic.

// If the recipe you are making doesn't use a wafer, just make it like a normal recipe and put down a metadata of 0.
public class SolarFactoryRecipes implements Runnable {

    public void run() {
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Bottle_Beer.get(1))
            .itemOutputs(ItemList.Cover_SolarPanel_UV.get(1))
            .duration(10 * SECONDS)
            .eut((int) TierEU.RECIPE_HV)
            .specialValue(2)
            .metadata(SolarFactoryKey.INSTANCE, 3)
            .hidden()
            .addTo(solarFactoryRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Bottle_Beer.get(1), ItemList.Circuit_Silicon_Wafer3.get(2))
            .itemOutputs(ItemList.Cover_SolarPanel_UV.get(1))
            .duration(10 * SECONDS)
            .eut((int) TierEU.RECIPE_HV)
            .fake()
            .metadata(SolarFactoryKey.INSTANCE, 3)
            .addTo(solarFactoryRecipes);

        // Test case for a recipe that doesn't use a wafer.
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Bottle_Cider.get(2))
            .itemOutputs(ItemList.Cover_SolarPanel_LuV.get(1))
            .duration(10 * SECONDS)
            .eut((int) TierEU.RECIPE_HV)
            .metadata(SolarFactoryKey.INSTANCE, 0)
            .addTo(solarFactoryRecipes);
    }
}
