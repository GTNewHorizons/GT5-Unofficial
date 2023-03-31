package gregtech.loaders.postload.recipes;

import static gregtech.api.enums.ModIDs.Forestry;
import static gregtech.api.util.GT_ModHandler.getModItem;
import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sLatheRecipes;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;
import static gregtech.api.util.GT_RecipeBuilder.TICKS;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.loaders.postload.GT_MachineRecipeLoader;

public class LatheRecipes implements Runnable {

    @Override
    public void run() {
        GT_Values.RA.stdBuilder()
            .itemInputs( new ItemStack(Blocks.wooden_slab, 1, GT_Values.W))
            .itemOutputs(
                new ItemStack(Items.bowl, 1),
                GT_OreDictUnificator.get(OrePrefixes.dustSmall, Materials.Wood, 1)
            )
            .noFluidInputs()
            .noFluidOutputs()
            .duration(2*SECONDS+10*TICKS)
            .eut(8)
            .addTo(sLatheRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(getModItem(Forestry.modID, "slabs", 1L, GT_Values.W))
            .itemOutputs(
                new ItemStack(Items.bowl, 1),
                GT_OreDictUnificator.get(OrePrefixes.dustSmall, Materials.Wood, 1)
            )
            .noFluidInputs()
            .noFluidOutputs()
            .duration(2*SECONDS + 10 * TICKS)
            .eut(8)
            .addTo(sLatheRecipes);
    }
}
