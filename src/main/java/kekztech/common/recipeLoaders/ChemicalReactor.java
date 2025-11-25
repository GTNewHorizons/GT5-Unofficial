package kekztech.common.recipeLoaders;

import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeConstants.UniversalChemical;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.TierEU;
import kekztech.Items;
import kekztech.common.items.MetaItemCraftingComponent;

public class ChemicalReactor implements Runnable {

    @Override
    public void run() {
        final MetaItemCraftingComponent craftingItem = MetaItemCraftingComponent.getInstance();

        // Ceria Dust
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Cerium.getDust(1))
            .circuit(6)
            .itemOutputs(craftingItem.getStackOfAmountFromDamage(Items.CeriaDust.getMetaID(), 3))
            .fluidInputs(Materials.Oxygen.getGas(2_000))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

    }
}
