package gregtech.loaders.postload.recipes;

import static gregtech.api.enums.GT_Values.RA;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_OreDictUnificator;

public class ThermalCentrifugeRecipes implements Runnable {

    @Override
    public void run() {
        RA.addThermalCentrifugeRecipe(
                ItemList.SunnariumCell.get(1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sunnarium, 1L),
                new ItemStack(Items.glowstone_dust, 2),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Iron, 1L),
                500,
                48);
    }
}
