package common.recipeLoaders;

import common.items.ErrorItem;
import common.items.MetaItem_CraftingComponent;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;
import kekztech.Items;
import net.minecraft.item.ItemStack;

public class Mixer implements Runnable{
    @Override
    public void run() {
        final MetaItem_CraftingComponent craftingItem = MetaItem_CraftingComponent.getInstance();

        // YSZ Ceramic Dust
        GT_Values.RA.addMixerRecipe(
            Items.YttriaDust.getOreDictedItemStack(1),
            Items.ZirconiaDust.getOreDictedItemStack(5),
            GT_Utility.getIntegratedCircuit(6),
            null,
            null,
            null,
            craftingItem.getStackOfAmountFromDamage(Items.YSZCeramicDust.getMetaID(), 6),
            400,
            96);

        // GDC Ceramic Dust
        GT_Values.RA.addMixerRecipe(
            GT_OreDictUnificator
                .get(OrePrefixes.dust, Materials.Gadolinium, new ItemStack(ErrorItem.getInstance(), 1), 1),
            craftingItem.getStackOfAmountFromDamage(Items.CeriaDust.getMetaID(), 9),
            GT_Utility.getIntegratedCircuit(6),
            null,
            null,
            null,
            craftingItem.getStackOfAmountFromDamage(Items.GDCCeramicDust.getMetaID(), 10),
            400,
            1920);
    }
}
