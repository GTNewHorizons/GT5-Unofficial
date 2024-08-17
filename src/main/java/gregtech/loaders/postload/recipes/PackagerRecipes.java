package gregtech.loaders.postload.recipes;

import static gregtech.api.recipe.RecipeMaps.packagerRecipes;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;
import static gregtech.api.util.GT_RecipeBuilder.TICKS;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_OreDictUnificator;

public class PackagerRecipes implements Runnable {

    @Override
    public void run() {
        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Tool_Matches.get(16L),
                GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.Paper, 1L))
            .itemOutputs(ItemList.Tool_MatchBox_Full.get(1L))
            .duration(3 * SECONDS + 4 * TICKS)
            .eut(16)
            .addTo(packagerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Tool_MatchBox_Full.get(1L))
            .itemOutputs(ItemList.Tool_Matches.get(16L))
            .duration(1 * SECONDS + 12 * TICKS)
            .eut(16)
            .addTo(packagerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.IC2_Scrap.get(9), ItemList.Schematic_3by3.get(0))
            .itemOutputs(ItemList.IC2_Scrapbox.get(1))
            .duration(16 * TICKS)
            .eut(1)
            .addTo(packagerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Food_Fries.get(1),
                GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.Paper, 1))
            .itemOutputs(ItemList.Food_Packaged_Fries.get(1))
            .duration(3 * SECONDS + 4 * TICKS)
            .eut(16)
            .addTo(packagerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Food_PotatoChips.get(1),
                GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Aluminium, 1))
            .itemOutputs(ItemList.Food_Packaged_PotatoChips.get(1))
            .duration(3 * SECONDS + 4 * TICKS)
            .eut(16)
            .addTo(packagerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Food_ChiliChips.get(1),
                GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Aluminium, 1))
            .itemOutputs(ItemList.Food_Packaged_ChiliChips.get(1))
            .duration(3 * SECONDS + 4 * TICKS)
            .eut(16)
            .addTo(packagerRecipes);
    }
}
