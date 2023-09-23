package common.recipeLoaders;

import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sPressRecipes;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;

import common.items.MetaItem_CraftingComponent;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.TierEU;
import kekztech.Items;

public class FormingPress implements Runnable {

    @Override
    public void run() {
        final MetaItem_CraftingComponent craftingItem = MetaItem_CraftingComponent.getInstance();

        // YSZ Ceramic Plate
        GT_Values.RA.stdBuilder()
                .itemInputs(
                        craftingItem.getStackOfAmountFromDamage(Items.GDCCeramicDust.getMetaID(), 10),
                        ItemList.Shape_Mold_Plate.get(0))
                .itemOutputs(craftingItem.getStackOfAmountFromDamage(Items.GDCCeramicPlate.getMetaID(), 1))
                .duration(40 * SECONDS).eut(TierEU.RECIPE_HV).addTo(sPressRecipes);
    }
}
