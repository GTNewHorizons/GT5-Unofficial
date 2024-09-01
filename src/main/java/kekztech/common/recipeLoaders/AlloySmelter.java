package kekztech.common.recipeLoaders;

import static gregtech.api.recipe.RecipeMaps.alloySmelterRecipes;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.TierEU;
import kekztech.Items;
import kekztech.common.items.MetaItemCraftingComponent;

public class AlloySmelter implements Runnable {

    @Override
    public void run() {
        final MetaItemCraftingComponent craftingItem = MetaItemCraftingComponent.getInstance();

        // YSZ Cermic Plate
        GT_Values.RA.stdBuilder()
            .itemInputs(
                craftingItem.getStackOfAmountFromDamage(Items.YSZCeramicDust.getMetaID(), 3),
                ItemList.Shape_Mold_Plate.get(0))
            .itemOutputs(craftingItem.getStackOfAmountFromDamage(Items.YSZCeramicPlate.getMetaID(), 1))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(alloySmelterRecipes);
    }
}
