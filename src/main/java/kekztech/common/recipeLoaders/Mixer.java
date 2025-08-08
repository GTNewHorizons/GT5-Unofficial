package kekztech.common.recipeLoaders;

import static gregtech.api.recipe.RecipeMaps.mixerRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import kekztech.Items;
import kekztech.common.items.ErrorItem;
import kekztech.common.items.MetaItemCraftingComponent;

public class Mixer implements Runnable {

    @Override
    public void run() {
        final MetaItemCraftingComponent craftingItem = MetaItemCraftingComponent.getInstance();

        // YSZ Ceramic Dust
        GTValues.RA.stdBuilder()
            .itemInputs(
                Items.YttriaDust.getOreDictedItemStack(1),
                Items.ZirconiaDust.getOreDictedItemStack(5),
                GTUtility.getIntegratedCircuit(6))
            .itemOutputs(craftingItem.getStackOfAmountFromDamage(Items.YSZCeramicDust.getMetaID(), 6))
            .duration(20 * SECONDS)
            .eut(96)
            .addTo(mixerRecipes);

        // GDC Ceramic Dust
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator
                    .get(OrePrefixes.dust, Materials.Gadolinium, new ItemStack(ErrorItem.getInstance(), 1), 1),
                craftingItem.getStackOfAmountFromDamage(Items.CeriaDust.getMetaID(), 9),
                GTUtility.getIntegratedCircuit(6))
            .itemOutputs(craftingItem.getStackOfAmountFromDamage(Items.GDCCeramicDust.getMetaID(), 10))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(mixerRecipes);
    }
}
