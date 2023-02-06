package gregtech.loaders.postload.recipes;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_OreDictUnificator;

public class PackagerRecipes implements Runnable {

    @Override
    public void run() {
        GT_Values.RA.addBoxingRecipe(
                ItemList.IC2_Scrap.get(9L),
                ItemList.Schematic_3by3.get(0L),
                ItemList.IC2_Scrapbox.get(1L),
                16,
                1);
        GT_Values.RA.addBoxingRecipe(
                ItemList.Food_Fries.get(1L),
                GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.Paper, 1L),
                ItemList.Food_Packaged_Fries.get(1L),
                64,
                16);
        GT_Values.RA.addBoxingRecipe(
                ItemList.Food_PotatoChips.get(1L),
                GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Aluminium, 1L),
                ItemList.Food_Packaged_PotatoChips.get(1L),
                64,
                16);
        GT_Values.RA.addBoxingRecipe(
                ItemList.Food_ChiliChips.get(1L),
                GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Aluminium, 1L),
                ItemList.Food_Packaged_ChiliChips.get(1L),
                64,
                16);
    }
}
