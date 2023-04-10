package common.recipeLoaders;

import kekztech.Items;

import common.items.MetaItem_CraftingComponent;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.util.GT_Utility;

public class ChemicalReactor implements Runnable {

    @Override
    public void run() {
        final MetaItem_CraftingComponent craftingItem = MetaItem_CraftingComponent.getInstance();

        // Ceria Dust
        GT_Values.RA.addChemicalRecipe(
                Materials.Cerium.getDust(2),
                GT_Utility.getIntegratedCircuit(6),
                Materials.Oxygen.getGas(3000),
                null,
                craftingItem.getStackOfAmountFromDamage(Items.CeriaDust.getMetaID(), 2),
                null,
                400,
                30);
    }
}
