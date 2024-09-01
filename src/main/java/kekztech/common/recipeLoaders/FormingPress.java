package kekztech.common.recipeLoaders;

import static gregtech.api.recipe.RecipeMaps.formingPressRecipes;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.TierEU;
import kekztech.Items;
import kekztech.common.items.MetaItemCraftingComponent;

public class FormingPress implements Runnable {

    @Override
    public void run() {
        final MetaItemCraftingComponent craftingItem = MetaItemCraftingComponent.getInstance();

        // YSZ Ceramic Plate
        GT_Values.RA.stdBuilder()
            .itemInputs(
                craftingItem.getStackOfAmountFromDamage(Items.GDCCeramicDust.getMetaID(), 10),
                ItemList.Shape_Mold_Plate.get(0))
            .itemOutputs(craftingItem.getStackOfAmountFromDamage(Items.GDCCeramicPlate.getMetaID(), 1))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(formingPressRecipes);
    }
}
