package gregtech.loaders.postload.recipes;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_OreDictUnificator;

public class SifterRecipes implements Runnable {

    @Override
    public void run() {
        GT_Values.RA.addSifterRecipe(
                new ItemStack(Blocks.gravel, 1, 0),
                new ItemStack[] { new ItemStack(Items.flint, 1, 0), new ItemStack(Items.flint, 1, 0),
                        new ItemStack(Items.flint, 1, 0), new ItemStack(Items.flint, 1, 0),
                        new ItemStack(Items.flint, 1, 0), new ItemStack(Items.flint, 1, 0) },
                new int[] { 10000, 9000, 8000, 6000, 3300, 2500 },
                600,
                16);
        GT_Values.RA.addSifterRecipe(
                GT_OreDictUnificator.get(OrePrefixes.crushedPurified, Materials.Coal, 1L),
                new ItemStack[] { new ItemStack(Items.coal, 1, 0), new ItemStack(Items.coal, 1, 0),
                        new ItemStack(Items.coal, 1, 0), new ItemStack(Items.coal, 1, 0),
                        new ItemStack(Items.coal, 1, 0),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Coal, 1L) },
                new int[] { 10000, 9000, 8000, 7000, 6000, 5000 },
                600,
                16);
    }
}
