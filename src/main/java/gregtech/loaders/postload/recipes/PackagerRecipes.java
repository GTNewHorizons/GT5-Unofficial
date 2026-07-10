package gregtech.loaders.postload.recipes;

import static gregtech.api.recipe.RecipeMaps.packagerRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.enums.materials2.Materials2Shapes;

public class PackagerRecipes implements Runnable {

    @Override
    public void run() {
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Tool_Matches.get(16L),
                MaterialLibAPI.getStack(Materials2Materials.Paper, Materials2Shapes.shapePlateDouble, (int) (1L)))
            .itemOutputs(ItemList.Tool_MatchBox_Full.get(1L))
            .duration(3 * SECONDS + 4 * TICKS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(packagerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.IC2_Scrap.get(9), ItemList.Schematic_3by3.get(0))
            .itemOutputs(ItemList.IC2_Scrapbox.get(1))
            .duration(16 * TICKS)
            .eut(1)
            .addTo(packagerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Food_Fries.get(1),
                MaterialLibAPI.getStack(Materials2Materials.Paper, Materials2Shapes.shapePlateDouble, (int) (1)))
            .itemOutputs(ItemList.Food_Packaged_Fries.get(1))
            .duration(3 * SECONDS + 4 * TICKS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(packagerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Food_PotatoChips.get(1),
                MaterialLibAPI.getStack(Materials2Materials.Aluminium, Materials2Shapes.shapeFoil, (int) (1)))
            .itemOutputs(ItemList.Food_Packaged_PotatoChips.get(1))
            .duration(3 * SECONDS + 4 * TICKS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(packagerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Food_ChiliChips.get(1),
                MaterialLibAPI.getStack(Materials2Materials.Aluminium, Materials2Shapes.shapeFoil, (int) (1)))
            .itemOutputs(ItemList.Food_Packaged_ChiliChips.get(1))
            .duration(3 * SECONDS + 4 * TICKS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(packagerRecipes);
    }
}
