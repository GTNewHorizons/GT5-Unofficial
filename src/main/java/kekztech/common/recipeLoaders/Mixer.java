package kekztech.common.recipeLoaders;

import static gregtech.api.recipe.RecipeMaps.mixerRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials.Materials;
import gregtech.api.enums.materials.Shapes;
import kekztech.Items;
import kekztech.common.items.MetaItemCraftingComponent;

public class Mixer implements Runnable {

    @Override
    public void run() {
        final MetaItemCraftingComponent craftingItem = MetaItemCraftingComponent.getInstance();

        // YSZ Ceramic Dust
        GTValues.RA.stdBuilder()
            .itemInputs(Items.YttriaDust.getOreDictedItemStack(1), Items.ZirconiaDust.getOreDictedItemStack(5))
            .circuit(6)
            .itemOutputs(craftingItem.getStackOfAmountFromDamage(Items.YSZCeramicDust.getMetaID(), 6))
            .duration(20 * SECONDS)
            .eut(96)
            .addTo(mixerRecipes);

        // GDC Ceramic Dust
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Gadolinium, Shapes.dust, 1),
                craftingItem.getStackOfAmountFromDamage(Items.CeriaDust.getMetaID(), 9))
            .circuit(6)
            .itemOutputs(craftingItem.getStackOfAmountFromDamage(Items.GDCCeramicDust.getMetaID(), 10))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(mixerRecipes);
    }
}
