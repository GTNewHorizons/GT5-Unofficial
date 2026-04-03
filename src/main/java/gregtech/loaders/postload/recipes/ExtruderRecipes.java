package gregtech.loaders.postload.recipes;

import static gregtech.api.recipe.RecipeMaps.extruderRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Mods;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTModHandler;

public class ExtruderRecipes implements Runnable {

    @Override
    public void run() {
        // wax capsule
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.FR_Wax.get(1L), ItemList.Shape_Extruder_Cell.get(0L))
            .itemOutputs(ItemList.FR_WaxCapsule.get(1L))
            .duration(3 * SECONDS + 4 * TICKS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(extruderRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.FR_RefractoryWax.get(1L), ItemList.Shape_Extruder_Cell.get(0L))
            .itemOutputs(ItemList.FR_RefractoryCapsule.get(1L))
            .duration(6 * SECONDS + 8 * TICKS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(extruderRecipes);
        if (Mods.PamsHarvestCraft.isModLoaded()) {
            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.Food_Dough.get(1L), ItemList.Shape_Extruder_Bolt.get(0L))
                .itemOutputs(GTModHandler.getModItem(Mods.PamsHarvestCraft.ID, "pastaItem", 1, 0))
                .duration(4 * SECONDS)
                .eut(TierEU.RECIPE_LV / 2)
                .addTo(extruderRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.Food_Dough.get(1L), ItemList.Shape_Extruder_Plate.get(0L))
                .itemOutputs(GTModHandler.getModItem(Mods.PamsHarvestCraft.ID, "pastaItem", 1, 0))
                .duration(4 * SECONDS)
                .eut(TierEU.RECIPE_LV / 2)
                .addTo(extruderRecipes);
        }
    }
}
