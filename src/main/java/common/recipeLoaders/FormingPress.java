package common.recipeLoaders;

import kekztech.Items;

import common.items.MetaItem_CraftingComponent;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;

public class FormingPress implements Runnable {

    @Override
    public void run() {
        final MetaItem_CraftingComponent craftingItem = MetaItem_CraftingComponent.getInstance();

        // YSZ Ceramic Plate
        GT_Values.RA.addFormingPressRecipe(
                craftingItem.getStackOfAmountFromDamage(Items.GDCCeramicDust.getMetaID(), 10),
                ItemList.Shape_Mold_Plate.get(0),
                craftingItem.getStackOfAmountFromDamage(Items.GDCCeramicPlate.getMetaID(), 1),
                800,
                480);
    }
}
