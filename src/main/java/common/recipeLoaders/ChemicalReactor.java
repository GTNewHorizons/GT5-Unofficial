package common.recipeLoaders;

import static gregtech.api.util.GT_RecipeBuilder.SECONDS;
import static gregtech.api.util.GT_RecipeConstants.UniversalChemical;

import common.items.MetaItem_CraftingComponent;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GT_Utility;
import kekztech.Items;

public class ChemicalReactor implements Runnable {

    @Override
    public void run() {
        final MetaItem_CraftingComponent craftingItem = MetaItem_CraftingComponent.getInstance();

        // Ceria Dust
        GT_Values.RA.stdBuilder()
            .itemInputs(Materials.Cerium.getDust(1), GT_Utility.getIntegratedCircuit(6))
            .itemOutputs(craftingItem.getStackOfAmountFromDamage(Items.CeriaDust.getMetaID(), 3))
            .fluidInputs(Materials.Oxygen.getGas(2000))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

    }
}
