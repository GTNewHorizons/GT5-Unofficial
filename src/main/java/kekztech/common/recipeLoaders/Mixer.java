package kekztech.common.recipeLoaders;

import static gregtech.api.recipe.RecipeMaps.mixerRecipes;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;
import kekztech.Items;
import kekztech.common.items.ErrorItem;
import kekztech.common.items.MetaItemCraftingComponent;

public class Mixer implements Runnable {

    @Override
    public void run() {
        final MetaItemCraftingComponent craftingItem = MetaItemCraftingComponent.getInstance();

        // YSZ Ceramic Dust
        GT_Values.RA.stdBuilder()
            .itemInputs(
                Items.YttriaDust.getOreDictedItemStack(1),
                Items.ZirconiaDust.getOreDictedItemStack(5),
                GT_Utility.getIntegratedCircuit(6))
            .itemOutputs(craftingItem.getStackOfAmountFromDamage(Items.YSZCeramicDust.getMetaID(), 6))
            .duration(20 * SECONDS)
            .eut(96)
            .addTo(mixerRecipes);

        // GDC Ceramic Dust
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator
                    .get(OrePrefixes.dust, Materials.Gadolinium, new ItemStack(ErrorItem.getInstance(), 1), 1),
                craftingItem.getStackOfAmountFromDamage(Items.CeriaDust.getMetaID(), 9),
                GT_Utility.getIntegratedCircuit(6))
            .itemOutputs(craftingItem.getStackOfAmountFromDamage(Items.GDCCeramicDust.getMetaID(), 10))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(mixerRecipes);
    }
}
